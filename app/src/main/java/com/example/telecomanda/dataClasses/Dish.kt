package com.example.telecomanda.dataClasses

data class Dish(
    var name: String = "",
    var price: String = "",
    var type: String = "",
    var ingredients: List<String> = listOf()
) {
    constructor() : this("", "", "", listOf())
}
