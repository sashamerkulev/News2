package ru.merkulyevsasha.news.presentation.articledetails

import io.reactivex.android.schedulers.AndroidSchedulers
import ru.merkulyevsasha.core.domain.ArticlesInteractor
import ru.merkulyevsasha.core.routers.ApplicationRouter
import ru.merkulyevsasha.news.presentation.base.BasePresenterImpl
import ru.merkulyevsasha.news.presentation.common.ArticleLikeClickHandler
import timber.log.Timber

class ArticleDetailsPresenterImpl(
    private val articlesInteractor: ArticlesInteractor,
    private val applicationRouter: ApplicationRouter
) : BasePresenterImpl<ArticleDetailsView>() {

    private val articleLikeClickHandler = ArticleLikeClickHandler(articlesInteractor,
        { view?.updateItem(it) },
        { view?.showError() })

    fun onFirstLoad(articleId: Int) {
        compositeDisposable.add(
            articlesInteractor.getArticle(articleId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view?.showProgress() }
                .doAfterTerminate { view?.hideProgress() }
                .subscribe(
                    { view?.showItem(it) },
                    {
                        Timber.e(it)
                        view?.showError()
                    }))
    }

    fun onArticleLikeClicked(articleId: Int) {
        compositeDisposable.add(articleLikeClickHandler.onArticleLikeClicked(articleId))
    }

    fun onArticleDislikeClicked(articleId: Int) {
        compositeDisposable.add(articleLikeClickHandler.onArticleDislikeClicked(articleId))
    }

    fun onCommentClicked(articleId: Int) {
        applicationRouter.showArticleComments(articleId)
    }

    fun onShareClicked(articleId: Int) {
    }
}