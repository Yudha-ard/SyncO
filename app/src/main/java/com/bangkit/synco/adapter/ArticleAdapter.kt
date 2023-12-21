package com.bangkit.synco.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.synco.data.model.ArticleModel
import com.bangkit.synco.databinding.ItemArticleBinding
import com.bumptech.glide.Glide

class ArticleAdapter(articles: ArticleModel) :ListAdapter<ArticleModel, ArticleAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(article)
        }
    }

    class MyViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article:ArticleModel) {

            Glide.with(binding.root.context)
                .load(article.thumbnail)
                .into(binding.ivArticle)

            binding.tvArticleTitle.text = article.title
            binding.tvArticleDetail.text = article.intro.take(30)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: ArticleModel)
    }
    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

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