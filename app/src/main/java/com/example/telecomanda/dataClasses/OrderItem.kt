package com.example.telecomanda.dataClasses

data class OrderItem(
    val name: String,
    val price: String,
    val type: String // Puede ser "Dish" o "Drink"
)

