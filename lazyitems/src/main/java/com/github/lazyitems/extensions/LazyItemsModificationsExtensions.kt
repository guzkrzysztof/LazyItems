package com.github.lazyitems.extensions

import com.github.lazyitems.item.LazyItem
import com.github.lazyitems.modification.LazyItemModification
import com.github.lazyitems.modification.LazyItemsModifications

internal fun <ItemKey : Any, ItemType : LazyItem<ItemKey>> LazyItemsModifications<ItemKey, ItemType>.findAddition(
    key: ItemKey
): LazyItemModification.Add<ItemKey, ItemType>? {
    return this
        .values
        .filterIsInstance<LazyItemModification.Add<ItemKey, ItemType>>()
        .find { modification -> modification.item.key() == key }
}
