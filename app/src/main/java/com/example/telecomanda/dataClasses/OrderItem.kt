package com.example.telecomanda.dataClasses

data class OrderItem(
    val name: String = "",
    val price: String = "",
    val type: String = "",
    var quantity: Int = 0
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "name" to name,
            "price" to price,
            "type" to type,
            "quantity" to quantity
        )
    }
}
