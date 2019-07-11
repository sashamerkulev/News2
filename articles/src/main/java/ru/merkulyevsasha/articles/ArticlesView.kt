package ru.merkulyevsasha.articles

import ru.merkulyevsasha.core.models.Article
import ru.merkulyevsasha.coreandroid.base.BaseView

interface ArticlesView : ru.merkulyevsasha.coreandroid.base.BaseView {
    fun showError()
    fun hideProgress()
    fun showProgress()
    fun showItems(items: List<Article>)
    fun updateItems(items: List<Article>)
    fun updateItem(item: Article)
}
