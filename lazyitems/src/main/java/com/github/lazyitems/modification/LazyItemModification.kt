package com.github.lazyitems.modification

import com.github.lazyitems.item.LazyItem
import com.github.lazyitems.item.LazyItemPosition

sealed interface LazyItemModification<ItemKey : Any, ItemType : LazyItem<ItemKey>> {

    data class Add<ItemKey : Any, ItemType : LazyItem<ItemKey>>(
        val position: LazyItemPosition,
        val item: ItemType
    ) : LazyItemModification<ItemKey, ItemType>

    data class Update<ItemKey : Any, ItemType : LazyItem<ItemKey>>(
        val item: ItemType
    ) : LazyItemModification<ItemKey, ItemType>

    data class Modify<ItemKey : Any, ItemType : LazyItem<ItemKey>>(
        val key: ItemKey,
        val modifier: (ItemType) -> ItemType
    ) : LazyItemModification<ItemKey, ItemType>

    data class Remove<ItemKey : Any, ItemType : LazyItem<ItemKey>>(
        val key: ItemKey
    ) : LazyItemModification<ItemKey, ItemType>
}

typealias LazyItemsModifications<ItemKey, ItemType> = Map<ItemKey, LazyItemModification<ItemKey, ItemType>>
