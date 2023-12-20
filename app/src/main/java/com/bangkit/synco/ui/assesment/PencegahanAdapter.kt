package com.bangkit.synco.ui.assesment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.synco.R

class PencegahanAdapter(private val pencegahanList: List<String>) : RecyclerView.Adapter<PencegahanAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewPencegahan: TextView = itemView.findViewById(R.id.tv_Pencegahan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pencegahan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = pencegahanList[position]
        holder.textViewPencegahan.text = item
    }

    override fun getItemCount(): Int {
        return pencegahanList.size
    }
}
