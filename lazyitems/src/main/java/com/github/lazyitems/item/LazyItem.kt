package com.github.lazyitems.item

interface LazyItem<ItemKey : Any> {
    fun key(): ItemKey
}
