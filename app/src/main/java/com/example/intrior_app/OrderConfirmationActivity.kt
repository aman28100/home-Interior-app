package com.example.intrior_app

import CartItem
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

class OrderConfirmationActivity : AppCompatActivity() {

    private lateinit var tvStoreName: TextView
    private lateinit var llProductDetails: LinearLayout
    private lateinit var tvServiceCharge: TextView
    private lateinit var tvShippingCharge: TextView
    private lateinit var tvTotalPrice: TextView
    private lateinit var btnConfirmOrder: Button
    private lateinit var orderPlacedVideo: VideoView
    private lateinit var cartItems: List<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirmation)

        // Initialize views
        tvStoreName = findViewById(R.id.tvStoreName)
        llProductDetails = findViewById(R.id.llProductDetails)
        tvServiceCharge = findViewById(R.id.tvServiceCharge)
        tvShippingCharge = findViewById(R.id.tvShippingCharge)
        tvTotalPrice = findViewById(R.id.tvTotalPrice)
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder)
        orderPlacedVideo = findViewById(R.id.orderPlacedVideo)

        // Get cart items from Intent
        cartItems = intent.getParcelableArrayListExtra<CartItem>("CART_ITEMS") ?: emptyList()

        // Show product details
        displayProductDetails()

        // Calculate and show the total price
        calculateTotalPrice()

        // Confirm order button click
        btnConfirmOrder.setOnClickListener {
            // Show notification
            showOrderNotification()

            // Start the video after confirming the order
            playOrderPlacedVideo()
        }
    }

    private fun displayProductDetails() {
        // Display product details (name, price, and quantity)
        for (item in cartItems) {
            val productDetailTextView = TextView(this)
            productDetailTextView.text = "${item.name} - ₹${item.price}"
            llProductDetails.addView(productDetailTextView)
        }
    }

    private fun calculateTotalPrice() {
        var totalPrice = 0
        for (item in cartItems) {
            totalPrice += item.price
        }
        val serviceCharge = 200
        val finalPrice = totalPrice + serviceCharge

        tvTotalPrice.text = "Total Price: ₹$finalPrice"
    }

    private fun playOrderPlacedVideo() {
        // Set up the video URI and make video view visible
        val videoUri: Uri = Uri.parse("android.resource://$packageName/raw/confirmorder")
        orderPlacedVideo.setVideoURI(videoUri)
        orderPlacedVideo.visibility = View.VISIBLE

        // Start the video playback once it's prepared
        orderPlacedVideo.setOnPreparedListener {
            orderPlacedVideo.start()
        }

        // Handle errors in case the video can't be loaded
        orderPlacedVideo.setOnErrorListener { _, _, _ ->
            // Hide the video if there's an error
            orderPlacedVideo.visibility = View.GONE
            true
        }
    }

    // Show notification after the order is placed
    private fun showOrderNotification() {
        // Get Notification Manager service
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Check if the version is Android 8.0 (API 26) or higher, and create the channel if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "order_confirmation",
                "Order Confirmation",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.description = "This channel is used for order confirmations"
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(this, "order_confirmation")
            .setContentTitle("Order Placed Successfully")
            .setContentText("Your order will be delivered in 7 working days!")
            .setSmallIcon(R.drawable.ic_order) // Ensure you have this icon in your drawable folder
            .setAutoCancel(true) // Optional: Auto-cancel the notification when clicked
            .build()

        // Notify the user
        notificationManager.notify(1, notification)
    }
}
