package com.example.nexusshop

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.nexusshop.databinding.ActivityProductDetailBinding
import com.example.nexusshop.model.Product
import com.example.nexusshop.utils.CartManager

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle custom back button
        binding.btnBackDetail.setOnClickListener {
            onBackPressed()
        }

        val product = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("PRODUCT", Product::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("PRODUCT") as? Product
        }

        if (product != null) {
            displayProductDetails(product)
            
            binding.btnAddToCart.setOnClickListener {
                CartManager(this).addToCart(product)
                Toast.makeText(this, "${product.title} added to cart!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Error: Product details not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun displayProductDetails(product: Product) {
        binding.tvTitleDetail.text = product.title
        binding.tvCategoryDetail.text = "Category: ${product.category}"
        binding.tvPriceDetail.text = "$${product.price}"
        binding.tvDescriptionDetail.text = product.description

        Glide.with(this)
            .load(product.thumbnail)
            .placeholder(android.R.drawable.ic_menu_report_image)
            .into(binding.imgProductLarge)
    }
}