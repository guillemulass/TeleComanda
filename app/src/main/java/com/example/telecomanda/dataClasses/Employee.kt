package com.example.telecomanda.dataClasses

data class Employee(
    var name: String = "",
    var password: String = "",
) {
    constructor() : this("", "")
}
