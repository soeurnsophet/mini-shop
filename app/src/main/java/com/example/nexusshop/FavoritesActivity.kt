package com.example.nexusshop

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nexusshop.adapter.ProductAdapter
import com.example.nexusshop.databinding.ActivityFavoritesBinding
import com.example.nexusshop.utils.CartManager

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var cartManager: CartManager
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle custom back button
        binding.btnBackFav.setOnClickListener {
            onBackPressed()
        }

        cartManager = CartManager(this)
        setupRecyclerView()
        loadFavorites()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(emptyList())
        // Changed to LinearLayoutManager for 1 column layout
        binding.rvFavorites.layoutManager = LinearLayoutManager(this)
        binding.rvFavorites.adapter = adapter
    }

    private fun loadFavorites() {
        val favoritesMap = cartManager.getFavoriteProducts()
        val products = favoritesMap.values.toList()

        if (products.isEmpty()) {
            binding.tvEmptyFavorites.visibility = View.VISIBLE
        } else {
            binding.tvEmptyFavorites.visibility = View.GONE
            adapter.updateData(products)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}