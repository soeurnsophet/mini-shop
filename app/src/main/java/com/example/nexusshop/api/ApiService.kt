package com.example.nexusshop.api

import com.example.nexusshop.model.ProductResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("products")
    fun getProducts(): Call<ProductResponse>
}