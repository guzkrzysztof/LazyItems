package com.github.lazyitems.viewmodel

import androidx.lifecycle.ViewModel
import com.github.lazyitems.extensions.findAddition
import com.github.lazyitems.item.LazyItem
import com.github.lazyitems.item.LazyItemPosition
import com.github.lazyitems.modification.LazyItemModification
import com.github.lazyitems.modification.LazyItemsModifications
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class LazyItemsViewModel<ItemKey : Any, ItemType : LazyItem<ItemKey>> : ViewModel() {

    private val _modifications = MutableStateFlow<LazyItemsModifications<ItemKey, ItemType>>(emptyMap())
    val modifications = _modifications.asStateFlow()

    var selectedItem: ItemType? = null
        private set

    fun setSelectedItem(item: ItemType) {
        selectedItem = item
    }

    fun resetSelectedItem() {
        selectedItem = null
    }

    fun addItem(position: LazyItemPosition, item: ItemType) {
        val modification = LazyItemModification.Add(position, item)
        _modifications.update { modifications ->
            modifications.toMutableMap().apply {
                put(modification.item.key(), modification)
            }
        }
    }

    fun updateItem(item: ItemType) {
        val addition = modifications.value.findAddition(item.key())
        if (addition == null) {
            val modification = LazyItemModification.Update(item)
            _modifications.update { modifications ->
                modifications.toMutableMap().apply {
                    put(modification.item.key(), modification)
                }
            }
        } else {
            _modifications.update { modifications ->
                modifications.toMutableMap().apply {
                    put(addition.item.key(), addition.copy(item = item))
                }
            }
        }
    }

    fun modifyItem(key: ItemKey, modifier: (ItemType) -> ItemType) {
        val addition = modifications.value.findAddition(key)
        if (addition == null) {
            val modification = LazyItemModification.Modify(key, modifier)
            _modifications.update { modifications ->
                modifications.toMutableMap().apply {
                    put(modification.key, modification)
                }
            }
        } else {
            _modifications.update { modifications ->
                modifications.toMutableMap().apply {
                    put(addition.item.key(), addition.copy(item = modifier.invoke(addition.item)))
                }
            }
        }
    }

    fun removeItem(key: ItemKey) {
        val addition = modifications.value.findAddition(key)
        if (addition == null) {
            val modification = LazyItemModification.Remove<ItemKey, ItemType>(key)
            _modifications.update { modifications ->
                modifications.toMutableMap().apply {
                    put(modification.key, modification)
                }
            }
        } else {
            _modifications.update { modifications ->
                modifications.toMutableMap().apply {
                    remove(addition.item.key())
                }
            }
        }
    }

    fun resetModifications() {
        _modifications.value = emptyMap()
    }
}
