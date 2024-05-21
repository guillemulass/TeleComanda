package com.example.telecomanda.dataClasses

data class Order(
    val items: List<OrderItem> = listOf(),
    val totalPrice: Double = 0.0
)
