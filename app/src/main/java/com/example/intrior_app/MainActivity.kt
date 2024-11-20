package com.example.intrior_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            handleLogin()
        }
    }

    private fun handleLogin() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        when {
            username == "user" && password == "user123" -> {
                // Navigate to UserActivity
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
                finish()
            }
            username == "admin" && password == "admin123" -> {
                // Navigate to AdminActivity
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
                finish()
            }
            else -> {
                // Show error message
                Toast.makeText(this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
