package com.example.intrior_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.interior_app.adapters.CategoryItemAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class CategoryDetailActivity : AppCompatActivity() {

    private lateinit var lvCategoryItems: ListView
    private lateinit var appDatabase: AppDatabase
    private lateinit var etSearch: EditText
    private lateinit var btnSearch: Button
    private var category: String? = null
    private var allItems: List<RoomEntity> = emptyList() // Store all items for filtering

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_detail)

        category = intent.getStringExtra("CATEGORY")
        findViewById<TextView>(R.id.tvCategoryTitle).text = "Here you will see about $category"

        appDatabase = AppDatabase.getDatabase(this)
        lvCategoryItems = findViewById(R.id.lvCategoryItems)
        etSearch = findViewById(R.id.etSearch)
        btnSearch = findViewById(R.id.btnSearch)

        // Fetch and display category items
        loadCategoryItems()

        // Set up search button click listener
        btnSearch.setOnClickListener {
            val query = etSearch.text.toString().trim()
            searchCategoryItems(query)
        }
    }

    private fun loadCategoryItems() {
        GlobalScope.launch(Dispatchers.IO) {
            allItems = appDatabase.roomDao().getItemsForCategory(category!!)
            withContext(Dispatchers.Main) {
                displayItems(allItems)
            }
        }
    }

    private fun displayItems(items: List<RoomEntity>) {
        val adapter = CategoryItemAdapter(this@CategoryDetailActivity, items)
        lvCategoryItems.adapter = adapter

        // Set item click listener to open ProductDetailActivity with item details
        lvCategoryItems.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = items[position]

            Log.d("CategoryDetailActivity", "Selected item: ${selectedItem.title}, ID: ${selectedItem.id}")

            val intent = Intent(this@CategoryDetailActivity, ProductDetailActivity::class.java).apply {
                putExtra("ITEM_ID", selectedItem.id)
                putExtra("ITEM_TITLE", selectedItem.title)
                putExtra("ITEM_DESCRIPTION", selectedItem.description)
                putExtra("ITEM_COST", selectedItem.cost)
                putExtra("ITEM_IMAGE_URL", selectedItem.imageUrl)
            }
            startActivity(intent)
        }
    }

    private fun searchCategoryItems(query: String) {
        val filteredItems = allItems.filter { item ->
            item.title.contains(query, ignoreCase = true) ||
                    item.description.contains(query, ignoreCase = true) ||
                    item.cost.toString().contains(query)
        }
        displayItems(filteredItems)
    }
}
