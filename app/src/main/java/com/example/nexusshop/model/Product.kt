package com.example.nexusshop.model

import java.io.Serializable

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val thumbnail: String,
    val category: String
) : Serializable