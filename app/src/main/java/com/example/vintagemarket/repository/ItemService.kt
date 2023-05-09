package com.example.vintagemarket.repository

import com.example.vintagemarket.models.Item
import retrofit2.Call
import retrofit2.http.*

interface ItemService {
    @GET("SalesItems")
    fun getAllItems(): Call<List<Item>>

    @GET("SalesItems/{itemId}")
    fun getItemById(@Path("itemId") itemId: Int): Call<Item>

    @POST("SalesItems")
    fun saveItem(@Body item: Item): Call<Item>

    @DELETE("SalesItems/{id}")
    fun deleteItem(@Path("id") id: Int): Call<Item>

    @PUT("SalesItems/{id}")
    fun updateItem(@Path("id") id: Int, @Body item: Item): Call<Item>
}