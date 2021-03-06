package ru.merkulyevsasha.domain

import android.content.Context
import android.content.Intent
import ru.merkulyevsasha.core.ArticleDistributor
import ru.merkulyevsasha.core.ResourceProvider
import ru.merkulyevsasha.core.models.Article
import ru.merkulyevsasha.core.models.ArticleComment

class ArticleDistributorImpl(
    private val context: Context,
    private val resourceProvider: ResourceProvider
) : ArticleDistributor {
    override fun distribute(item: Article) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val sb = StringBuilder()
        sb.append(String.format(resourceProvider.getString(R.string.article_info_send), item.sourceName, item.title))
        sb.append(resourceProvider.getString(R.string.application_info_send))
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun distribute(item: ArticleComment) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val sb = StringBuilder()
        sb.append(String.format(resourceProvider.getString(R.string.articlecomment_info_send), item.userName, item.comment))
        sb.append(resourceProvider.getString(R.string.application_info_send))
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}