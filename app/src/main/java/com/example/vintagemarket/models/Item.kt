package com.example.vintagemarket.models

data class Item(
    val id: Int,
    val description: String,
    val price: Int,
    val sellerEmail: String,
    val sellerPhone: String,
)
