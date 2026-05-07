package com.example.cafeapp.data.remote.dto

data class CreateOrderRequest(
    val tableId: String,
    val order_type: String = "Dine-in",
    val items: List<OrderItemRequest>
)

data class OrderItemRequest(
    val menuId: String, // Ensure this matches the ID type in your Menu table
    val quantity: Int,
    val price: Double,
    val tableId: String, // Node.js reads this as table_number
    val userId: String,  // Node.js reads this as the staff ID
    val customization: String = ""
)

data class StockRequest(
    val ingredient_name: String,
    val amount: Int
)

data class TableRequest(
    val table_number: Int,
    val area: String
)

data class TableStatusRequest(
    val status: String
)