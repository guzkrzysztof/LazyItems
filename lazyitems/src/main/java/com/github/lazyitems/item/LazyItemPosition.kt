package com.github.lazyitems.item

sealed interface LazyItemPosition {

    data object Start : LazyItemPosition

    data object End : LazyItemPosition

    data class Index(val value: Int) : LazyItemPosition
}
