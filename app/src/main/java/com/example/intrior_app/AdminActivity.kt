package com.example.intrior_app

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.RoomDatabase
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AdminActivity : AppCompatActivity() {

    private lateinit var roomDatabase: RoomDatabase
    private lateinit var roomDao: RoomDao
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var costEditText: EditText
    private lateinit var spinnerReadingStatus: Spinner
    private lateinit var etSearch: EditText
    private lateinit var lvRooms: ListView
    private lateinit var tvHeader: TextView
    private lateinit var imageView: ImageView
    private val roomList = mutableListOf<RoomEntity>()
    private lateinit var roomAdapter: RoomAdapter
    private var selectedRoom: RoomEntity? = null
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        // Initialize UI elements
        titleEditText = findViewById(R.id.etTitle)
        descriptionEditText = findViewById(R.id.etAuthor)
        costEditText = findViewById(R.id.cost)
        spinnerReadingStatus = findViewById(R.id.spinnerReadingStatus)
        etSearch = findViewById(R.id.etSearch)
        lvRooms = findViewById(R.id.lvRooms)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnShow = findViewById<Button>(R.id.btnShow)
        val btnSearchNow = findViewById<Button>(R.id.btnSearchNow)
        tvHeader = findViewById(R.id.tvHeader)
        imageView = findViewById(R.id.imageView) // Initialize ImageView for image selection

        // Initialize roomDao
        val appDatabase = AppDatabase.getDatabase(this)
        roomDao = appDatabase.roomDao()

        // Initialize custom adapter
        roomAdapter = RoomAdapter(this, roomList)
        lvRooms.adapter = roomAdapter

        val readingStatusOptions = arrayOf("Select Category", "Living Room", "Kitchen", "Dining Hall", "Balcony")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, readingStatusOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerReadingStatus.adapter = spinnerAdapter

        btnSave.setOnClickListener {
            saveOrUpdateRoom()
        }

        btnShow.setOnClickListener {
            showAllRooms()
        }

        btnSearchNow.setOnClickListener {
            searchRooms()
        }

        // Handle image selection when clicking on ImageView
        imageView.setOnClickListener {
            // Open gallery to select an image
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 100)
        }

        lvRooms.setOnItemClickListener { _, _, position, _ ->
            val room = roomList[position]
            showUpdateDeleteDialog(room)
        }
    }

    // Handle image selection result from gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            imageView.setImageURI(selectedImageUri) // Display the selected image in ImageView
        }
    }

    private fun saveOrUpdateRoom() {
        val title = titleEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val cost = costEditText.text.toString()
        val category = spinnerReadingStatus.selectedItem.toString()

        if (title.isNotEmpty() && description.isNotEmpty() && cost.isNotEmpty() && category != "Select Category") {
            val imageUriString = selectedImageUri?.toString()  // Convert URI to String

            val room = RoomEntity(
                title = title,
                description = description,
                cost = cost.toInt(),
                category = category,
                imageUrl = imageUriString // Save the image URI
            )

            lifecycleScope.launch(Dispatchers.IO) {
                if (selectedRoom == null) {
                    roomDao.insertRoom(room)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AdminActivity, "Room Details Saved", Toast.LENGTH_SHORT).show()
                        clearFields()
                    }
                } else {
                    room.id = selectedRoom!!.id
                    roomDao.updateRoom(room)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AdminActivity, "Room Details Updated", Toast.LENGTH_SHORT).show()
                        clearFields()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAllRooms() {
        roomDao.getAllRooms().observe(this) { rooms ->
            roomList.clear()
            roomList.addAll(rooms)
            roomAdapter.notifyDataSetChanged() // Notify the adapter of data changes
        }
    }

    private fun searchRooms() {
        val searchText = etSearch.text.toString()
        if (searchText.isNotEmpty()) {
            roomDao.searchRooms("%$searchText%").observe(this) { results ->
                roomList.clear()
                roomList.addAll(results)
                roomAdapter.notifyDataSetChanged() // Notify the adapter of data changes
            }
        } else {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showUpdateDeleteDialog(room: RoomEntity) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update or Delete")
        builder.setMessage("What do you want to do with this room?")

        builder.setPositiveButton("Update") { _, _ ->
            titleEditText.setText(room.title)
            descriptionEditText.setText(room.description)
            costEditText.setText(room.cost.toString())
            spinnerReadingStatus.setSelection(getSpinnerPosition(room.category))
            selectedRoom = room
            if (room.imageUrl != null) {
                val imageUri = Uri.parse(room.imageUrl)

                Glide.with(imageView.context) // Use the ImageView's context to avoid issues
                    .load(imageUri)
                    .placeholder(R.drawable.img) // Fallback placeholder image
                    .error(R.drawable.img) // Image to show in case of an error
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.img) // Set default image if there's no URI
            }

        }

        builder.setNegativeButton("Delete") { _, _ ->
            lifecycleScope.launch(Dispatchers.IO) {
                roomDao.deleteRoom(room)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AdminActivity, "Room Deleted", Toast.LENGTH_SHORT).show()
                    showAllRooms()
                }
            }
        }

        builder.setNeutralButton("Cancel", null)
        builder.show()
    }

    private fun getSpinnerPosition(status: String): Int {
        val options = arrayOf("Select Category", "Living Room", "Kitchen", "Dining Hall", "Balcony")
        return options.indexOf(status)
    }

    private fun clearFields() {
        titleEditText.text.clear()
        descriptionEditText.text.clear()
        costEditText.text.clear()
        spinnerReadingStatus.setSelection(0)
        imageView.setImageResource(R.drawable.img) // Set a placeholder image
        selectedRoom = null
        selectedImageUri = null
    }
}
