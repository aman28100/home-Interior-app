package com.example.interior_app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.intrior_app.R
import com.example.intrior_app.RoomEntity
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.Locale

class CategoryItemAdapter(private val context: Context, private val items: List<RoomEntity>) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): RoomEntity = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_room, parent, false)

        val titleTextView = view.findViewById<TextView>(R.id.tvRoomTitle)
        val descriptionTextView = view.findViewById<TextView>(R.id.tvRoomDescription)
        val costTextView = view.findViewById<TextView>(R.id.tvRoomCost)
        val imageView = view.findViewById<ImageView>(R.id.ivRoomImage)

        val item = getItem(position)
        titleTextView.text = item.title
        descriptionTextView.text = item.description

        val formattedCost = NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(item.cost)
        costTextView.text = "Cost: $formattedCost"


        Picasso.get()
            .load(item.imageUrl)
            .into(imageView)

        return view
    }
}
