package com.example.telecomanda.dataClasses

data class Order(
    val items: List<OrderItem>,
    val totalPrice: Double
)