package com.example.intrior_app

import CartItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CartAdapter(private val cartItems: List<CartItem>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.bind(cartItem)
    }

    override fun getItemCount(): Int = cartItems.size

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemName: TextView = itemView.findViewById(R.id.tvItemName)
        private val itemPrice: TextView = itemView.findViewById(R.id.tvItemPrice)
        private val itemImage: ImageView = itemView.findViewById(R.id.ivItemImage)

        fun bind(cartItem: CartItem) {
            itemName.text = cartItem.name
            itemPrice.text = "₹${cartItem.price}"

            // Check if the imageUrl is a URL or a drawable resource ID
            if (cartItem.imageUrl is String) {
                // If it's a URL (String), use Picasso to load it
                Picasso.get().load(cartItem.imageUrl as String).into(itemImage)
            } else if (cartItem.imageUrl is Int) {
                // If it's a resource ID (Int), use setImageResource
                itemImage.setImageResource(cartItem.imageUrl as Int)
            }
        }
    }
}
