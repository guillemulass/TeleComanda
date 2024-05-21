package com.example.telecomanda.dataClasses

data class Table(
    val number: Int = 0,
    val code: String = "",
    val orders: MutableList<OrderItem> = mutableListOf()
)
