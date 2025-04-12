package com.example.bookstoreinventoryapp

data class Book(
    val id: Long = 0,
    val title: String,
    val author: String,
    val price: String,
    val category: String,
    val quantity: Int,
    val publisher: String,
    val edition: String,
    val vendor: String,
    val imageResId: Int
)
