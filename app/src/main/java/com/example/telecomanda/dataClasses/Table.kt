package com.example.telecomanda.dataClasses

data class Table(
    val number: Int,
    val orders: MutableList<Order> = mutableListOf()
)
