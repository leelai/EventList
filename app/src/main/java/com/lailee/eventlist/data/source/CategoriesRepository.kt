package com.lailee.eventlist.data.source

import java.util.LinkedHashMap

class CategoriesRepository(
    val categoriesDataSource: CategoriesDataSource
) : CategoriesDataSource {

    private var cachedCategories: LinkedHashMap<String, Category> = LinkedHashMap()

    override fun getCategories(callback: CategoriesDataSource.LoadCategoriesCallback) {
        if (cachedCategories.isNotEmpty()) {
            callback.onCategoriesLoaded(ArrayList(cachedCategories.values))
            return
        }

        categoriesDataSource.getCategories(object : CategoriesDataSource.LoadCategoriesCallback {
            override fun onCategoriesLoaded(categories: List<Category>) {
                refreshCache(categories)
                callback.onCategoriesLoaded(ArrayList(cachedCategories.values))
            }

            override fun onDataNotAvailable() {
                //add default categories
                insertCategory(Category("Personal"))
                insertCategory(Category("Business"))
                insertCategory(Category("Others"))
                callback.onCategoriesLoaded(ArrayList(cachedCategories.values))
            }
        })
    }

    override fun getCategory(eventId: String, callback: CategoriesDataSource.GetCategoryCallback) {
        val eventInCache = getEventWithId(eventId)
        if (eventInCache != null) {
            callback.onCategoryLoaded(eventInCache)
            return
        }

        categoriesDataSource.getCategory(eventId, object : CategoriesDataSource.GetCategoryCallback {
            override fun onCategoryLoaded(event: Category) {
                cacheAndPerform2(event) {
                    callback.onCategoryLoaded(it)
                }
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })

    }

    private fun insertCategory(category: Category) {
        cacheAndPerform2(category) {
            categoriesDataSource.saveCategory(it)
        }
    }

    override fun saveCategory(category: Category) {
        cacheAndPerform2(category) {
            categoriesDataSource.saveCategory(it)
        }
    }

    override fun refreshEvents() {
    }

    override fun deleteCategory(eventId: String) {
        categoriesDataSource.deleteCategory(eventId)
        cachedCategories.remove(eventId)
    }

    private fun getEventWithId(id: String) = cachedCategories[id]

    private fun refreshCache(categories: List<Category>) {
        cachedCategories.clear()
        categories.forEach {
            cacheAndPerform2(it) {}
        }
    }

    private fun refreshCache2(categories: List<Category>) {
        cachedCategories.clear()
        categories.forEach {
            cacheAndPerform2(it) {}
        }
    }

    private inline fun cacheAndPerform2(category: Category, perform: (Category) -> Unit) {
        cachedCategories[category.id] = category
        perform(category)
    }

    companion object {

        private val TAG = CategoriesRepository::class.java.simpleName
        private var INSTANCE: CategoriesRepository? = null

        @JvmStatic fun getInstance(categoriesDataSource: CategoriesDataSource): CategoriesRepository {
            return INSTANCE ?: CategoriesRepository(categoriesDataSource)
                .apply { INSTANCE = this }
        }

        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}