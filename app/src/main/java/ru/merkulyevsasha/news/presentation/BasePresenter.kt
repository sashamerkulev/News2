package ru.merkulyevsasha.news.presentation

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<T> {

    protected var view: T? = null
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun bindView(view: T) {
        this.view = view
    }

    fun unbindView() {
        this.view = null
    }

    fun onDestroy() {
        compositeDisposable.dispose()
    }
}
