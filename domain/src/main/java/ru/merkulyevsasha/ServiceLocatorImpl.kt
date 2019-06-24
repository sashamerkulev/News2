package ru.merkulyevsasha

import android.content.Context
import ru.merkulyevsasha.articles.ArticlesApiRepositoryImpl
import ru.merkulyevsasha.comments.ArticleCommentsApiRepositoryImpl
import ru.merkulyevsasha.core.NewsDistributor
import ru.merkulyevsasha.core.ResourceProvider
import ru.merkulyevsasha.core.ServiceLocator
import ru.merkulyevsasha.core.domain.ArticleCommentsInteractor
import ru.merkulyevsasha.core.domain.ArticlesInteractor
import ru.merkulyevsasha.core.domain.SetupInteractor
import ru.merkulyevsasha.core.domain.UsersInteractor
import ru.merkulyevsasha.core.preferences.KeyValueStorage
import ru.merkulyevsasha.core.repositories.ArticleCommentsApiRepository
import ru.merkulyevsasha.core.repositories.ArticlesApiRepository
import ru.merkulyevsasha.core.repositories.DatabaseRepository
import ru.merkulyevsasha.core.repositories.SetupApiRepository
import ru.merkulyevsasha.core.repositories.UsersApiRepository
import ru.merkulyevsasha.core.routers.MainActivityRouter
import ru.merkulyevsasha.core.routers.MainFragmentRouter
import ru.merkulyevsasha.database.DatabaseRepositoryImpl
import ru.merkulyevsasha.domain.ArticleCommentsInteractorImpl
import ru.merkulyevsasha.domain.ArticlesInteractorImpl
import ru.merkulyevsasha.domain.NewsDistributorImpl
import ru.merkulyevsasha.domain.ResourceProviderImpl
import ru.merkulyevsasha.domain.SetupInteractorImpl
import ru.merkulyevsasha.domain.UsersInteractorImpl
import ru.merkulyevsasha.domain.mappers.SourceNameMapper
import ru.merkulyevsasha.preferences.KeyValueStorageImpl
import ru.merkulyevsasha.setup.SetupApiRepositoryImpl
import ru.merkulyevsasha.users.UsersApiRepositoryImpl

class ServiceLocatorImpl(context: Context, mainActivityRouter: MainActivityRouter) : ServiceLocator {

    private val maps = HashMap<Any, Any>()

    init {
        val prefs = KeyValueStorageImpl(context)
        val resourceProvider = ResourceProviderImpl(context)
        maps[KeyValueStorage::class.java] = prefs
        maps[ResourceProvider::class.java] = resourceProvider
        maps[NewsDistributor::class.java] = NewsDistributorImpl(context, resourceProvider)
        maps[SetupApiRepository::class.java] = SetupApiRepositoryImpl(prefs)
        maps[ArticlesApiRepository::class.java] = ArticlesApiRepositoryImpl(prefs)
        maps[ArticleCommentsApiRepository::class.java] = ArticleCommentsApiRepositoryImpl(prefs)
        maps[UsersApiRepository::class.java] = UsersApiRepositoryImpl(prefs)
        maps[DatabaseRepository::class.java] = DatabaseRepositoryImpl(context, prefs)
        maps[MainActivityRouter::class.java] = mainActivityRouter
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(clazz: Class<T>): T {
        if (maps.containsKey(clazz)) {
            return maps[clazz] as T
        }
        when (clazz) {
            ArticlesInteractor::class.java -> maps[clazz] = ArticlesInteractorImpl(
                getArticlesApiRepository(),
                getPreferences(),
                getDatabaseRepository(),
                SourceNameMapper(getDatabaseRepository())
            )
            UsersInteractor::class.java -> maps[clazz] = UsersInteractorImpl(
                getUsersApiRepository(),
                getDatabaseRepository(),
                getPreferences()
            )
            ArticleCommentsInteractor::class.java -> maps[clazz] = ArticleCommentsInteractorImpl(
                getArticlesApiRepository(),
                getArticleCommentsApiRepository(),
                getPreferences(),
                getDatabaseRepository(),
                SourceNameMapper(getDatabaseRepository())
            )
            SetupInteractor::class.java -> maps[clazz] = SetupInteractorImpl(
                getPreferences(),
                getSetupApiRepository(),
                getDatabaseRepository()
            )
        }
        return maps[clazz] as T
    }

    override fun <T> release(clazz: Class<T>) {
        if (maps.containsKey(clazz)) {
            maps.remove(clazz)
        }
    }

    override fun releaseAll() {
        maps.clear()
    }

    override fun addFragmentRouter(mainFragmentRouter: MainFragmentRouter) {
        maps[MainFragmentRouter::class.java] = mainFragmentRouter
    }

    override fun releaseFragmentRouter() {

    }

    private fun getArticlesApiRepository(): ArticlesApiRepository {
        return maps[ArticlesApiRepository::class.java] as ArticlesApiRepository
    }

    private fun getArticleCommentsApiRepository(): ArticleCommentsApiRepository {
        return maps[ArticleCommentsApiRepository::class.java] as ArticleCommentsApiRepository
    }

    private fun getUsersApiRepository(): UsersApiRepository {
        return maps[UsersApiRepository::class.java] as UsersApiRepository
    }

    private fun getSetupApiRepository(): SetupApiRepository {
        return maps[SetupApiRepository::class.java] as SetupApiRepository
    }

    private fun getDatabaseRepository(): DatabaseRepository {
        return maps[DatabaseRepository::class.java] as DatabaseRepository
    }

    private fun getPreferences(): KeyValueStorage {
        return maps[KeyValueStorage::class.java] as KeyValueStorage
    }
}