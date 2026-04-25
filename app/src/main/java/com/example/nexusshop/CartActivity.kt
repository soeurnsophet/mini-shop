package com.example.nexusshop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nexusshop.databinding.ActivityCartBinding
import com.example.nexusshop.databinding.ItemCartBinding
import com.example.nexusshop.utils.CartManager

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var cartManager: CartManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle custom back button
        binding.btnBackCart.setOnClickListener {
            onBackPressed()
        }

        cartManager = CartManager(this)
        updateCartUI()
    }

    private fun updateCartUI() {
        val cartItems = cartManager.getCart().values.toList()
        
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show()
        }

        binding.rvCartItems.layoutManager = LinearLayoutManager(this)
        binding.rvCartItems.adapter = CartAdapter(cartItems) { productId ->
            cartManager.removeFromCart(productId)
            updateCartUI() // Refresh after removal
        }

        binding.tvTotalPrice.text = "$${String.format("%.2f", cartManager.getTotalPrice())}"
    }

    class CartAdapter(
        private val items: List<CartManager.CartItem>,
        private val onRemove: (Int) -> Unit
    ) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

        class ViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.binding.tvCartTitle.text = item.product.title
            holder.binding.tvCartPrice.text = "$${item.product.price}"
            holder.binding.tvCartQty.text = "Quantity: ${item.quantity}"
            
            Glide.with(holder.itemView.context).load(item.product.thumbnail).into(holder.binding.imgCartItem)
            
            holder.binding.btnRemoveItem.setOnClickListener { onRemove(item.product.id) }
        }

        override fun getItemCount() = items.size
    }
}