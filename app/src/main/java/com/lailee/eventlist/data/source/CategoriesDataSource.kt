package com.lailee.eventlist.data.source

import androidx.annotation.VisibleForTesting
import com.lailee.eventlist.util.AppExecutors

interface CategoriesDataSource{

    interface LoadCategoriesCallback {

        fun onCategoriesLoaded(categories: List<Category>)

        fun onDataNotAvailable()
    }

    interface GetCategoryCallback {

        fun onCategoryLoaded(category: Category)

        fun onDataNotAvailable()
    }

    fun getCategories(callback: LoadCategoriesCallback)

    fun getCategory(categoryId: String, callback: GetCategoryCallback)

    fun saveCategory(category: Category)

    fun refreshEvents()

    fun deleteCategory(categoryId: String)
}

class CategoriesDataSourceImpl private constructor(
    val appExecutors: AppExecutors,
    val categoriesDao: CategoriesDao
) : CategoriesDataSource {

    override fun getCategories(callback: CategoriesDataSource.LoadCategoriesCallback) {
        appExecutors.diskIO.execute {
            val events = categoriesDao.getCategories()
            appExecutors.mainThread.execute {
                if (events.isEmpty()) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onCategoriesLoaded(events)
                }
            }
        }
    }

    override fun getCategory(categoryId: String, callback: CategoriesDataSource.GetCategoryCallback) {
        appExecutors.diskIO.execute {
            val event = categoriesDao.getCategoryById(categoryId)
            appExecutors.mainThread.execute {
                if (event != null) {
                    callback.onCategoryLoaded(event)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    override fun saveCategory(category: Category) {
        appExecutors.diskIO.execute {
            categoriesDao.insertCategory(category)
        }
    }

    override fun refreshEvents() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteCategory(categoryId: String) {
        appExecutors.diskIO.execute { categoriesDao.deleteCategoryById(categoryId) }
    }


    companion object {

        private const val TAG = "CategoriesDataSource"
        private var INSTANCE: CategoriesDataSourceImpl? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, categoriesDao: CategoriesDao): CategoriesDataSourceImpl {
            if (INSTANCE == null) {
                synchronized(EventsDataSourceImpl::javaClass) {
                    INSTANCE = CategoriesDataSourceImpl(appExecutors, categoriesDao)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }
}