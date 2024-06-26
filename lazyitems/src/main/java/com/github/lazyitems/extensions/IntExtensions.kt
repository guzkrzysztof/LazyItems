package com.github.lazyitems.extensions

internal fun Int.safeCoerceIn(
    minimumValue: Int,
    maximumValue: Int,
    fallbackValue: Int
): Int {
    if (minimumValue > maximumValue) {
        return fallbackValue
    }
    if (this < minimumValue) {
        return minimumValue
    }
    if (this > maximumValue) {
        return maximumValue
    }
    return this
}
