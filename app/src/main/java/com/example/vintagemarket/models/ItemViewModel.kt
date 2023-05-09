package com.example.vintagemarket.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.vintagemarket.repository.ItemRepository

class ItemViewModel : ViewModel() {
    private val repository = ItemRepository()
    val itemsLiveData: LiveData<List<Item>> = repository.itemsLiveData
    val errorMessageLiveData: LiveData<String> = repository.errorMessageLiveData
    val updateMessageLiveData: LiveData<String> = repository.updateMessageLiveData

    init {
        reload()
    }

    fun reload() {
        repository.getItems()
    }

    operator fun get(index: Int): Item? {
        return itemsLiveData.value?.get(index)
    }

    fun add(item: Item) {
         repository.add(item)
    }

    fun delete(id: Int) {
        repository.delete(id)
    }

    fun sortByDescription() {
        repository.sortByDescription()
    }

    fun sortByDescriptionDescending() {
        repository.sortByDescriptionDescending()
    }

    fun sortByPrice() {
        repository.sortByPrice()
    }

    fun sortByPriceDescending() {
        repository.sortByPriceDescending()
    }

    fun filterByDescription(description: String) {
        repository.filterByDescription(description)
    }
}