package com.example.intrior_app


import CartItem
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ProductDetailActivity : AppCompatActivity() {

    private val cartItems = mutableListOf<CartItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val title = intent.getStringExtra("ITEM_TITLE")
        val description = intent.getStringExtra("ITEM_DESCRIPTION")
        val cost = intent.getIntExtra("ITEM_COST", -1)
        val imageUrl = intent.getStringExtra("ITEM_IMAGE_URL")

        if (title == null || description == null || cost == -1 || imageUrl == null) {
            Log.e("ProductDetailActivity", "Missing intent extras")
            Toast.makeText(this, "Failed to load item details", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val tvProductTitle: TextView = findViewById(R.id.tvProductTitle)
        val tvProductDescription: TextView = findViewById(R.id.tvProductDescription)
        val tvProductCost: TextView = findViewById(R.id.tvProductCost)
        val ivProductImage: ImageView = findViewById(R.id.ivProductImage)
        val btnAddToCart: Button = findViewById(R.id.btnAddToCart)
        val recyclerViewRelatedProducts: RecyclerView = findViewById(R.id.recyclerViewRelatedProducts)

        tvProductTitle.text = title
        tvProductDescription.text = description
        tvProductCost.text = "₹$cost"
        Picasso.get().load(imageUrl).into(ivProductImage)

        // Sample list of related products with resource IDs (use R.drawable.lamp instead of string URLs)
        val relatedProducts = listOf(
            Product("Lamp", 599, R.drawable.lamp),
            Product("Bed", 5999, R.drawable.bed),
            Product("Table-lamp", 899, R.drawable.table),
            Product("carpet", 1599, R.drawable.carpet),
            Product("ceiling-light", 1599, R.drawable.ceiling),
            Product("sofa", 8999, R.drawable.sofa),
            Product("curtains", 499, R.drawable.room),
            Product("Plants", 499, R.drawable.succulent),

        )


        recyclerViewRelatedProducts.layoutManager = GridLayoutManager(this, 2)  // Grid Layout
        recyclerViewRelatedProducts.adapter = RelatedProductAdapter(
            relatedProducts,
            onItemClick = { product ->
                // Handle item click, e.g., navigate to product details
            },
            onAddToCartClick = { product ->
                // Add product to cart
                cartItems.add(CartItem(product.name, product.price, product.imageResourceId))
                Toast.makeText(this, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
            }
        )

        btnAddToCart.setOnClickListener {
            // Send the cart items to CartActivity (Pass cart items through Intent)
            val intent = Intent(this, CartActivity::class.java)
            intent.putParcelableArrayListExtra("CART_ITEMS", ArrayList(cartItems))
            startActivity(intent)
        }
    }
}
