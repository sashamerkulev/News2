package ru.merkulyevsasha.coreandroid.common.newsadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.row_news.view.imageViewComment
import kotlinx.android.synthetic.main.row_news.view.imageViewDislike
import kotlinx.android.synthetic.main.row_news.view.imageViewLike
import kotlinx.android.synthetic.main.row_news.view.imageViewThumb
import kotlinx.android.synthetic.main.row_news.view.layoutButtonComment
import kotlinx.android.synthetic.main.row_news.view.layoutButtonDislike
import kotlinx.android.synthetic.main.row_news.view.layoutButtonLike
import kotlinx.android.synthetic.main.row_news.view.layoutButtonShare
import kotlinx.android.synthetic.main.row_news.view.layoutSourceName
import kotlinx.android.synthetic.main.row_news.view.newsDateSource
import kotlinx.android.synthetic.main.row_news.view.newsDescription
import kotlinx.android.synthetic.main.row_news.view.newsTitle
import kotlinx.android.synthetic.main.row_news.view.textViewComment
import kotlinx.android.synthetic.main.row_news.view.textViewDislike
import kotlinx.android.synthetic.main.row_news.view.textViewLike
import ru.merkulyevsasha.core.models.Article
import ru.merkulyevsasha.coreandroid.R
import ru.merkulyevsasha.coreandroid.common.ColorThemeResolver
import java.text.SimpleDateFormat
import java.util.*

class NewsViewAdapter constructor(
    private val context: Context,
    private val articleCallbackClickHandler: ArticleClickCallbackHandler?,
    private val sourceArticleClickCallbackHandler: SourceArticleClickCallbackHandler?,
    private val likeCallbackClickHandler: ArticleLikeCallbackClickHandler?,
    private val commentCallbackClickHandler: ArticleCommentArticleCallbackClickHandler?,
    private val shareCallbackClickHandler: ArticleShareCallbackClickHandler?,
    private val colorThemeResolver: ColorThemeResolver,
    private val items: MutableList<Article>
) : RecyclerView.Adapter<ItemViewHolder>() {

    private val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_news, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = items[position]

        val source = item.sourceName
        val title = item.title.trim()
        val description = item.description
        val url = item.pictureUrl

        val pubDate = item.pubDate
        holder.itemView.newsDateSource.text = String.format("%s %s", format.format(pubDate), source)

        initDescription(title, description, holder)

        holder.itemView.imageViewThumb.setImageResource(0)
        if (url.isNullOrEmpty()) {
            holder.itemView.imageViewThumb.visibility = View.GONE
        } else {
            holder.itemView.imageViewThumb.visibility = View.VISIBLE
            Glide.with(context).load(url).into(holder.itemView.imageViewThumb)
        }

        holder.itemView.textViewLike.text = item.usersLikeCount.toString()
        holder.itemView.textViewDislike.text = item.usersDislikeCount.toString()
        holder.itemView.textViewComment.text = item.usersCommentCount.toString()

        colorThemeResolver.setArticleActivityColor(item.isUserLiked, holder.itemView.textViewLike, holder.itemView.imageViewLike)
        colorThemeResolver.setArticleActivityColor(item.isUserDisliked, holder.itemView.textViewDislike, holder.itemView.imageViewDislike)
        colorThemeResolver.setArticleActivityColor(item.isUserCommented, holder.itemView.textViewComment, holder.itemView.imageViewComment)

        initClickListeners(holder)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<Article>) {
        this.items.clear()
        this.items.addAll(items)
        this.notifyDataSetChanged()
    }

    fun updateItems(items: List<Article>) {
        var index = 0
        for (item in items) {
            val oldItemIndex = this.items.indexOfFirst { it.articleId == item.articleId }
            if (oldItemIndex >= 0) {
                this.items.set(oldItemIndex, item)
            } else {
                this.items.add(index, item)
                index++
            }
        }
        this.notifyDataSetChanged()
    }

    fun updateItem(item: Article) {
        val index = items.indexOfFirst { it.articleId == item.articleId }
        items[index] = item
        this.notifyDataSetChanged()
    }

    private fun initDescription(title: String, description: String?, holder: ItemViewHolder) {
        if (title == description || description.isNullOrEmpty()) {
            holder.itemView.newsDescription.visibility = View.GONE
        } else {
            holder.itemView.newsDescription.visibility = View.VISIBLE
            holder.itemView.newsDescription.text = description.trim { it <= ' ' }
        }
        holder.itemView.newsTitle.text = title
    }

    private fun initClickListeners(holder: ItemViewHolder) {
        holder.itemView.setOnClickListener {
            val newItem = items[holder.adapterPosition]
            articleCallbackClickHandler?.onArticleCliked(newItem)
        }

        holder.itemView.layoutSourceName.setOnClickListener {
            val newItem = items[holder.adapterPosition]
            sourceArticleClickCallbackHandler?.onSourceArticleCliked(newItem.sourceId, newItem.sourceName)
        }

        holder.itemView.layoutButtonLike.setOnClickListener {
            val newItem = items[holder.adapterPosition]
            likeCallbackClickHandler?.onArticleLikeClicked(newItem)
        }

        holder.itemView.layoutButtonComment.setOnClickListener {
            val newItem = items[holder.adapterPosition]
            commentCallbackClickHandler?.onArticleCommentArticleClicked(newItem.articleId)
        }

        holder.itemView.layoutButtonDislike.setOnClickListener {
            val newItem = items[holder.adapterPosition]
            likeCallbackClickHandler?.onArticleDislikeClicked(newItem)
        }

        holder.itemView.layoutButtonShare.setOnClickListener {
            val newItem = items[holder.adapterPosition]
            shareCallbackClickHandler?.onArticleShareClicked(newItem)
        }
    }
}
