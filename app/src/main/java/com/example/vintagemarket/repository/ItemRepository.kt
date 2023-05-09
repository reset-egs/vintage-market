package com.example.vintagemarket.repository

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.lifecycle.MutableLiveData
import com.example.vintagemarket.models.Item

class ItemRepository {
    private val baseUrl = "https://anbo-salesitems.azurewebsites.net/api/"

    private val itemService: ItemService
    val itemsLiveData: MutableLiveData<List<Item>> = MutableLiveData<List<Item>>()
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val updateMessageLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        val build: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()) // GSON
            .build()
        itemService = build.create(ItemService::class.java)
        getItems()
    }

    fun getItems() {
        itemService.getAllItems().enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if (response.isSuccessful) {
                    val b: List<Item>? = response.body()
                    itemsLiveData.postValue(b!!)
                    errorMessageLiveData.postValue("")
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
            }
        })
    }

    fun getItemById(id: Int): Item {
        var item: Item = Item(0, "", 0, "", "")
        itemService.getItemById(id).enqueue(object : Callback<Item> {
            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                if (response.isSuccessful) {
                    item = response.body()!!
                    errorMessageLiveData.postValue("")
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
            }
        })
        return item
    }



    fun add(item: Item) {
        itemService.saveItem(item).enqueue(object : Callback<Item> {
            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                if (response.isSuccessful) {
                    updateMessageLiveData.postValue("Added: " + response.body())
                    getItems()
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                }
            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
            }
        })
    }

    fun delete(id: Int) {
        itemService.deleteItem(id).enqueue(object : Callback<Item> {
            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                if (response.isSuccessful) {
                    updateMessageLiveData.postValue("Deleted: " + response.body())
                    getItems()
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                }
            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
            }
        })
    }

    fun sortByDescription() {
        itemsLiveData.value = itemsLiveData.value?.sortedBy { it.description }
    }

    fun sortByDescriptionDescending() {
        itemsLiveData.value = itemsLiveData.value?.sortedByDescending { it.description }
    }

    fun sortByPrice() {
        itemsLiveData.value = itemsLiveData.value?.sortedBy { it.price }
    }

    fun sortByPriceDescending() {
        itemsLiveData.value = itemsLiveData.value?.sortedByDescending { it.price }
    }

    fun filterByDescription(description: String) {
        if(description.isBlank()){
            getItems()
        } else {
            itemsLiveData.value = itemsLiveData.value?.filter { it.description.contains(description, ignoreCase = true) }
        }
    }
}