package com.example.nexusshop.utils

import android.content.Context
import com.example.nexusshop.model.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Manages the Shopping Cart and Favorites using SharedPreferences.
 */
class CartManager(context: Context) {
    private val sharedPref = context.getSharedPreferences("NexusShopPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // --- Favorites Logic ---

    fun toggleFavorite(product: Product) {
        val favorites = getFavoriteProducts().toMutableMap()
        if (favorites.containsKey(product.id.toString())) {
            favorites.remove(product.id.toString())
        } else {
            favorites[product.id.toString()] = product
        }
        val json = gson.toJson(favorites)
        sharedPref.edit().putString("favorites_json", json).apply()
    }

    fun isFavorite(productId: Int): Boolean {
        return getFavoriteProducts().containsKey(productId.toString())
    }

    fun getFavoriteProducts(): Map<String, Product> {
        val json = sharedPref.getString("favorites_json", null) ?: return emptyMap()
        val type = object : TypeToken<Map<String, Product>>() {}.type
        return gson.fromJson(json, type)
    }

    // --- Cart Logic ---

    fun addToCart(product: Product) {
        val cart = getCart().toMutableMap()
        val currentQty = cart[product.id.toString()]?.quantity ?: 0
        cart[product.id.toString()] = CartItem(product, currentQty + 1)
        saveCart(cart)
    }

    fun removeFromCart(productId: Int) {
        val cart = getCart().toMutableMap()
        cart.remove(productId.toString())
        saveCart(cart)
    }

    fun getCart(): Map<String, CartItem> {
        val json = sharedPref.getString("cart_json", null) ?: return emptyMap()
        val type = object : TypeToken<Map<String, CartItem>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun saveCart(cart: Map<String, CartItem>) {
        val json = gson.toJson(cart)
        sharedPref.edit().putString("cart_json", json).apply()
    }

    fun getTotalPrice(): Double {
        return getCart().values.sumOf { it.product.price * it.quantity }
    }

    data class CartItem(val product: Product, var quantity: Int)
}