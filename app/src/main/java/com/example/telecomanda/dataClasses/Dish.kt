package com.example.telecomanda.dataClasses

data class Dish(
    var name: String = "",
    var price: String = "",
    var type: String = "",
    var ingredients: List<String> = listOf(),
    var imageUrl: String? = null
) {
    constructor() : this("", "", "", listOf(), null)
}


