package com.example.intrior_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RelatedProductAdapter(
    private val relatedProducts: List<Product>,
    private val onItemClick: (Product) -> Unit,
    private val onAddToCartClick: (Product) -> Unit
) : RecyclerView.Adapter<RelatedProductAdapter.RelatedProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_related_product, parent, false)
        return RelatedProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: RelatedProductViewHolder, position: Int) {
        val product = relatedProducts[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = relatedProducts.size

    inner class RelatedProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productImage: ImageView = itemView.findViewById(R.id.ivProductImage)
        private val productName: TextView = itemView.findViewById(R.id.tvProductName)
        private val productPrice: TextView = itemView.findViewById(R.id.tvProductPrice)

        fun bind(product: Product) {
            productName.text = product.name
            productPrice.text = "₹${product.price}"

            // Use Picasso to load the image resource into the ImageView
            productImage.setImageResource(product.imageResourceId)

            itemView.setOnClickListener {
                onItemClick(product)
            }

            // Button to add to cart
            itemView.findViewById<Button>(R.id.btnAddToCart).setOnClickListener {
                onAddToCartClick(product)
            }
        }
    }
}
