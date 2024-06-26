package com.github.lazyitems.list

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.github.lazyitems.extensions.forEach
import com.github.lazyitems.extensions.safeCoerceIn
import com.github.lazyitems.item.LazyItem
import com.github.lazyitems.item.LazyItemPosition
import com.github.lazyitems.modification.LazyItemModification
import com.github.lazyitems.modification.LazyItemsModifications

private const val START_INDEX = 0

fun <ItemKey : Any, ItemType : LazyItem<ItemKey>> LazyListScope.lazyItems(
    items: LazyPagingItems<ItemType>,
    modifications: LazyItemsModifications<ItemKey, ItemType> = emptyMap(),
    skeletons: Int = 0,
    skeletonContent: @Composable LazyItemScope.() -> Unit = {},
    errorContent: @Composable LazyItemScope.(state: LoadState) -> Unit = {},
    emptyContent: @Composable LazyItemScope.() -> Unit = {},
    itemContent: @Composable LazyItemScope.(index: Int, item: ItemType) -> Unit = { _, _ -> },
    onItemsChange: (size: Int) -> Unit = {}
) {
    val loadState = items.loadState
    when (val refreshLoadState = loadState.refresh) {
        LoadState.Loading -> {
            items(skeletons) {
                skeletonContent.invoke(this)
            }
        }
        is LoadState.Error -> {
            item {
                errorContent.invoke(this, refreshLoadState)
            }
        }
        is LoadState.NotLoading -> {
            val lazyItems = items with modifications
            if (lazyItems.isEmpty() && loadState.append.endOfPaginationReached) {
                item {
                    emptyContent.invoke(this)
                }
            } else {
                items(
                    count = lazyItems.size
                ) { index ->
                    itemContent.invoke(this, index, lazyItems[index])
                }
            }
            onItemsChange.invoke(lazyItems.size)
        }
    }
    if (loadState.append is LoadState.Loading) {
        item {
            skeletonContent.invoke(this)
        }
    }
}

private infix fun <ItemKey : Any, ItemType : LazyItem<ItemKey>> LazyPagingItems<ItemType>.with(
    modifications: LazyItemsModifications<ItemKey, ItemType>
): List<ItemType> {
    val lazyPagingItems = this
    val lazyItems = mutableListOf<ItemType>()

    // Apply modifications
    lazyPagingItems.forEach { item ->
        val lazyItem = when (val modification = modifications[item.key()]) {
            is LazyItemModification.Update -> modification.item
            is LazyItemModification.Modify -> modification.modifier.invoke(item)
            is LazyItemModification.Remove -> null
            else -> item
        }
        if (lazyItem != null) {
            lazyItems.add(lazyItem)
        }
    }

    // Apply additions
    modifications
        .values
        .filterIsInstance<LazyItemModification.Add<ItemKey, ItemType>>()
        .forEach { addition ->
            lazyItems.add(
                index = when (val position = addition.position) {
                    is LazyItemPosition.Start -> START_INDEX
                    is LazyItemPosition.End -> lazyItems.size
                    is LazyItemPosition.Index -> position.value.safeCoerceIn(
                        minimumValue = START_INDEX,
                        maximumValue = lazyItems.size,
                        fallbackValue = lazyItems.size
                    )
                },
                element = addition.item
            )
        }

    return lazyItems
}
