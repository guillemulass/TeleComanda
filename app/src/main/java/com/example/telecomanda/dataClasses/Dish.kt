package com.example.telecomanda.dataClasses

data class Dish(
    var name: String = "",
    var price: String = ""
) {
    constructor() : this("", "")
}