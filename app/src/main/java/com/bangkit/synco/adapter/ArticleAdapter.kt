package com.bangkit.synco.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.synco.data.model.ArticleModel
import com.bangkit.synco.databinding.ItemArticleBinding
import com.bangkit.synco.ui.home.ItemArticleAdapter
import com.bangkit.synco.ui.webview.WebViewActivity
import com.bumptech.glide.Glide

class ArticleAdapter(private var articles: List<ArticleModel>) : ListAdapter<ArticleModel, ArticleAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = getItem(position) // Use getItem(position) to retrieve the correct item

        // Bind data to your ViewHolder's views here
        Glide.with(holder.itemView.context)
            .load(article.thumbnail)
            .into(holder.binding.ivArticle)

        holder.binding.tvArticleTitle.text = article.title
        holder.binding.tvArticleDetail.text = article.intro.take(30)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("link", article.link)
            Log.d("webview", "ini link: ${article.link}")
            context.startActivity(intent)
        }
    }


    interface OnItemClickListener {
        fun onItemClick(item: ArticleModel)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class MyViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticleModel>() {
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
