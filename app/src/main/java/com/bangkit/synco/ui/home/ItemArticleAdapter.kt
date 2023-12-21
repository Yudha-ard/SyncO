package com.bangkit.synco.ui.home

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.synco.R
import com.bangkit.synco.data.model.ArticleModel
import com.bangkit.synco.ui.webview.WebViewActivity
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView

class ItemArticleAdapter(private var articles: List<ArticleModel>) : RecyclerView.Adapter<ItemArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_article, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.item_entry_text)
        val image = itemView.findViewById<ImageView>(R.id.item_entry_image)
        val card = itemView.findViewById<MaterialCardView>(R.id.card)

        init {
            card.setOnClickListener {
                val context = itemView.context
                val article = articles[adapterPosition]

                val intent = Intent(context, WebViewActivity::class.java)
                intent.putExtra("link", article.link)
                Log.d("webview","ini link: $article.link")
                context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val article = articles[position]
        holder.title.text  = article.title
        Glide.with(holder.itemView.context)
        .load(article.thumbnail)
        .into(holder.image)

    }

    override fun getItemCount() = articles.size

    fun updateArticles(newArticles: List<ArticleModel>) {
        articles = newArticles
        for (article in articles) {
            Log.d("ArticleLog", "Article Title: ${article.title}")
        }
        notifyDataSetChanged()
    }

}
