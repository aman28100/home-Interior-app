package com.example.intrior_app

import CartItem
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {

    private val cartItems = mutableListOf<CartItem>()
    private lateinit var totalPriceTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnConfirmOrder: Button  // Initialize the Confirm Order Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Initialize your views
        recyclerView = findViewById(R.id.recyclerViewCartItems)
        totalPriceTextView = findViewById(R.id.tvTotalPrice)
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder)  // Initialize Confirm Order button

        // Get the cart items from the intent
        val intent = intent
        cartItems.addAll(intent.getParcelableArrayListExtra<CartItem>("CART_ITEMS") ?: mutableListOf())

        // Set up the RecyclerView with your CartAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CartAdapter(cartItems)

        // Calculate and display the total price
        calculateTotalPrice()

        // Set up the confirm order button
        btnConfirmOrder.setOnClickListener {
            // Navigate to Order Confirmation Activity
            val intent = Intent(this, OrderConfirmationActivity::class.java)
            intent.putParcelableArrayListExtra("CART_ITEMS", ArrayList(cartItems)) // Pass the cart items
            startActivity(intent)
        }
    }

    private fun calculateTotalPrice() {
        var totalPrice = 0
        // Sum up the price of each item in the cart
        for (item in cartItems) {
            totalPrice += item.price
        }

        // Add service charge (₹200) and shipping charge (Free)
        val serviceCharge = 200
        val finalPrice = totalPrice + serviceCharge

        // Update the total price TextView
        totalPriceTextView.text = "Total: ₹$finalPrice"
    }
}
