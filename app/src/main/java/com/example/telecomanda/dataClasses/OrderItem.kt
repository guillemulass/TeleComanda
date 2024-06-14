package com.example.telecomanda.dataClasses

data class OrderItem(
    val name: String = "",
    val price: String = "",
    val type: String = "",
    var quantity: Int = 0
)
