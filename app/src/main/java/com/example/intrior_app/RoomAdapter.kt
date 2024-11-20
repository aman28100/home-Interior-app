package com.example.intrior_app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

// Custom adapter to display rooms with images in a ListView
class RoomAdapter(
    private val context: Context,
    private val roomList: List<RoomEntity>
) : ArrayAdapter<RoomEntity>(context, 0, roomList) {

    // RoomAdapter.kt

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(R.layout.item_room, parent, false)

        val room = getItem(position)

        val titleTextView = view.findViewById<TextView>(R.id.tvRoomTitle)
        val descriptionTextView = view.findViewById<TextView>(R.id.tvRoomDescription)
        val costTextView = view.findViewById<TextView>(R.id.tvRoomCost)
        val imageView = view.findViewById<ImageView>(R.id.ivRoomImage)

        titleTextView.text = room?.title
        descriptionTextView.text = room?.description
        costTextView.text = room?.cost.toString()

        // Safely handle the image URI
        try {
            if (room?.imageUrl != null) {
                val imageUri = Uri.parse(room.imageUrl)
                imageView.setImageURI(imageUri)
                context.grantUriPermission(context.packageName, imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else {
                imageView.setImageResource(R.drawable.img) // Fallback image
            }
        } catch (e: Exception) {
            e.printStackTrace()
            imageView.setImageResource(R.drawable.img) // Fallback image in case of failure
        }

        return view
    }

}
