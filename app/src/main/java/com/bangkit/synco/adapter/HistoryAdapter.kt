package com.bangkit.synco.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.synco.data.model.HistoryItem
import com.bangkit.synco.databinding.ItemHistoryBinding
import com.bangkit.synco.helper.formatDate

class HistoryAdapter(history: List<HistoryItem>) :ListAdapter<HistoryItem, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val history = getItem(position)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.bind(history)
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(history)
        }
    }

    class MyViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(history:HistoryItem) {
            val tanggal = formatDate(history.history_tanggal)
            binding.tvDate.text = history.history_tanggal
            binding.tvSymptoms.text = history.nama_penyakit
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: HistoryItem)
    }
    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryItem>() {
            override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}