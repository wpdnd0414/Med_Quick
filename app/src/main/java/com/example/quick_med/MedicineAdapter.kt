package com.example.quick_med

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class MedicineAdapter(private val context: Context, private val dataSource: List<Medicine>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = convertView ?: inflater.inflate(R.layout.list_item_medicine, parent, false)
        val nameTextView = rowView.findViewById<TextView>(R.id.medicine_name)
        val descriptionTextView = rowView.findViewById<TextView>(R.id.medicine_description)
        val imageView = rowView.findViewById<ImageView>(R.id.medicine_image)

        val medicine = getItem(position) as Medicine
        nameTextView.text = medicine.name
        descriptionTextView.text = medicine.description
        Picasso.get().load(medicine.imageUrl).into(imageView)

        return rowView
    }
}