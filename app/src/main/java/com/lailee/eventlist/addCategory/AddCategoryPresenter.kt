package com.lailee.eventlist.addeditEvent

import com.lailee.eventlist.data.source.CategoriesDataSource
import com.lailee.eventlist.data.source.Category


class AddCategoryPresenter(
    val categoriesDataSource: CategoriesDataSource,
    val addCategoryView: AddCategoryContract.View
) : AddCategoryContract.Presenter, CategoriesDataSource.LoadCategoriesCallback {

    private val TAG = "AddCategoryPresenter"
    init {
        addCategoryView.presenter = this
    }

    override fun start() {
        populateCategories()
    }

    override fun saveCategory(category: String) {
        if (category.isEmpty()) {
            addCategoryView.showEmptyEventError()
        } else {
            categoriesDataSource.saveCategory(Category(category))
            addCategoryView.successfullySaved(category)
        }
    }

    override fun populateCategories() {
        categoriesDataSource.getCategories(this)
    }

    override fun onDataNotAvailable() {
        // The view may not be able to handle UI updates anymore
        addCategoryView.showEmptyEventError()
    }

    override fun onCategoriesLoaded(categories: List<Category>) {
        var categoryList = mutableListOf<String>()
        categories.forEach {
            categoryList.add(it.title)
        }
        addCategoryView.showCategories(categoryList)
    }

    override fun deleteCategory(category: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
