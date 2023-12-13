package com.bangkit.synco.ui.article

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bangkit.synco.data.article.Article
import com.bangkit.synco.databinding.ItemArticleBinding
import com.bangkit.synco.ui.webview.WebViewActivity

class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    private val listArticle: ArrayList<Article> = arrayListOf()

    fun setListArticle(listArticle: List<Article>) {
        this.listArticle.clear()
        this.listArticle.addAll(listArticle)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listArticle.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = listArticle[position]
        val context = holder.itemView.context

        with(holder.binding) {
            tvArticleTitle.text = article.title
            tvArticleDetail.text = article.description

            Glide.with(context)
                .load(article.imageUrl)
                .into(ivArticle)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("articleUrl", article.articleUrl)
            context.startActivity(intent)
        }
    }


    class ViewHolder(val binding: ItemArticleBinding): RecyclerView.ViewHolder(binding.root)
}
