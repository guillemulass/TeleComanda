package com.example.telecomanda.dataClasses

data class Drink(
    var name: String = "",
    var price: String = "",
    var type: String = ""
) {
    constructor() : this("", "", "")
}