package com.example.intrior_app

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.w3c.dom.Text
import java.util.*
import android.Manifest

class UserActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient:FusedLocationProviderClient
private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var sliderImageView: ImageView
    private val images = arrayOf(R.drawable.img_1, R.drawable.img_2, R.drawable.img_3)
    private var currentIndex = 0
    private lateinit var mylocation:TextView
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        mylocation = findViewById(R.id.location)
        sliderImageView = findViewById(R.id.imageSlider)

        // Start the image slider
        startImageSlider()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation(mylocation)


        // Set up card views
        val livingRoomCard = findViewById<CardView>(R.id.cardLivingRoom)
        val kitchenCard = findViewById<CardView>(R.id.cardKitchen)
        val diningCard = findViewById<CardView>(R.id.cardDining)
        val balconyCard = findViewById<CardView>(R.id.cardBalcony)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set onClickListeners for each category
        livingRoomCard.setOnClickListener { openCategoryDetail("Living Room") }
        kitchenCard.setOnClickListener { openCategoryDetail("Kitchen") }
        diningCard.setOnClickListener { openCategoryDetail("Dining Hall") }
        balconyCard.setOnClickListener { openCategoryDetail("Balcony") }

        // Set up BottomNavigationView item selection listener
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Navigate to UserActivity
                    startActivity(Intent(this, UserActivity::class.java))
                    true
                }
                R.id.nav_category -> {
                    // Navigate to CategoryActivity (or you can keep it as UserActivity if needed)
                    startActivity(Intent(this, UserActivity::class.java))
                    true
                }
                R.id.nav_cart -> {
                    // Navigate to CartActivity
                    startActivity(Intent(this, CartActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun getLocation(mylocation:TextView) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    // Convert latitude and longitude to human-readable address using Geocoder
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses: List<Address>? =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)

                    if (addresses != null && addresses.isNotEmpty()) {
                        // Set the address in the TextView inside the dialog
                        mylocation.text = "Address: ${addresses[0].getAddressLine(0)}"
                    } else {
                        Toast.makeText(this, "Unable to fetch address", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener {
                Toast.makeText(this,"yay we got the location",Toast.LENGTH_LONG)
                    .show()
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                val locationTextView = findViewById<TextView>(R.id.location)
                getLocation(locationTextView)
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun startImageSlider() {
        val handler = Handler()
        val updateImage = Runnable {
            sliderImageView.setImageResource(images[currentIndex])
            currentIndex = (currentIndex + 1) % images.size
        }

        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                handler.post(updateImage)
            }
        }, 0, 1500) // Change image every 1.5 seconds
    }

    private fun openCategoryDetail(category: String) {
        val intent = Intent(this, CategoryDetailActivity::class.java)
        intent.putExtra("CATEGORY", category)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
