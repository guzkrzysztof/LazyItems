package com.github.lazyitems.extensions

import androidx.paging.compose.LazyPagingItems

private const val START_INDEX = 0

internal fun <T : Any> LazyPagingItems<T>.forEach(
    action: (item: T) -> Unit
) {
    val lazyPagingItems = this
    (START_INDEX until lazyPagingItems.itemCount).forEach { index ->
        val item = lazyPagingItems[index]
        if (item != null) {
            action.invoke(item)
        }
    }
}
