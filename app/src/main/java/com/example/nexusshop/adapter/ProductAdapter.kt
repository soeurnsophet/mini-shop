package com.example.nexusshop.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nexusshop.ProductDetailActivity
import com.example.nexusshop.R
import com.example.nexusshop.databinding.ItemProductBinding
import com.example.nexusshop.model.Product
import com.example.nexusshop.utils.CartManager

class ProductAdapter(private var products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var allProducts: List<Product> = products

    class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        val context = holder.itemView.context
        val cartManager = CartManager(context)
        
        holder.binding.tvTitle.text = product.title
        holder.binding.tvPrice.text = "$${product.price}"
        
        Glide.with(context)
            .load(product.thumbnail)
            .placeholder(android.R.drawable.ic_menu_report_image)
            .into(holder.binding.imgProduct)

        // Heart Icon toggle
        updateHeartIcon(holder, product.id, cartManager)
        holder.binding.btnLike.setOnClickListener {
            cartManager.toggleFavorite(product)
            updateHeartIcon(holder, product.id, cartManager)
        }

        // View Details button
        holder.binding.btnViewDetails.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("PRODUCT", product)
            context.startActivity(intent)
        }

        // Add to Cart button
        holder.binding.btnAddToCart.setOnClickListener {
            cartManager.addToCart(product)
            Toast.makeText(context, "${product.title} added to cart!", Toast.LENGTH_SHORT).show()
        }

        // Removed the full item click listener to prefer the "View Details" button
        holder.itemView.setOnClickListener(null)
    }

    private fun updateHeartIcon(holder: ProductViewHolder, productId: Int, cartManager: CartManager) {
        val isFav = cartManager.isFavorite(productId)
        val icon = if (isFav) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
        holder.binding.btnLike.setImageResource(icon)
        
        if (isFav) {
            holder.binding.btnLike.setColorFilter(holder.itemView.context.getColor(android.R.color.holo_red_light))
        } else {
            holder.binding.btnLike.clearColorFilter()
        }
    }

    override fun getItemCount(): Int = products.size

    fun filter(query: String) {
        products = if (query.isEmpty()) {
            allProducts
        } else {
            allProducts.filter { it.title.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }

    fun updateData(newList: List<Product>) {
        allProducts = newList
        products = newList
        notifyDataSetChanged()
    }
}