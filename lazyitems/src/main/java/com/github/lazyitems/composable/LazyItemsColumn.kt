package com.github.lazyitems.composable

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.github.lazyitems.item.LazyItem
import com.github.lazyitems.list.lazyItems
import com.github.lazyitems.modification.LazyItemsModifications

@Composable
fun <ItemKey : Any, ItemType : LazyItem<ItemKey>> LazyItemsColumn(
    items: LazyPagingItems<ItemType>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (reverseLayout) {
        Arrangement.Bottom
    } else {
        Arrangement.Top
    },
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    modifications: LazyItemsModifications<ItemKey, ItemType> = emptyMap(),
    skeletons: Int = 0,
    skeletonContent: @Composable LazyItemScope.() -> Unit = {},
    errorContent: @Composable LazyItemScope.(state: LoadState) -> Unit = {},
    emptyContent: @Composable LazyItemScope.() -> Unit = {},
    itemContent: @Composable LazyItemScope.(index: Int, item: ItemType) -> Unit = { _, _ -> },
    onItemsChange: (size: Int) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled
    ) {
        lazyItems(
            items = items,
            modifications = modifications,
            skeletons = skeletons,
            skeletonContent = skeletonContent,
            errorContent = errorContent,
            emptyContent = emptyContent,
            itemContent = itemContent,
            onItemsChange = onItemsChange
        )
    }
}
