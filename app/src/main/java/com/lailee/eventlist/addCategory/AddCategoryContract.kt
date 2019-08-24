package com.lailee.eventlist.addeditEvent

import com.lailee.eventlist.BasePresenter
import com.lailee.eventlist.BaseView

/**
 * This specifies the contract between the view and the presenter.
 */
interface AddCategoryContract {

    interface View : BaseView<Presenter> {

        fun showEmptyEventError()

        fun showCategories(categories: List<String>)

        fun successfullySaved(category: String)
    }

    interface Presenter : BasePresenter {

        fun saveCategory(category: String)

        fun deleteCategory(category: String)

        fun populateCategories()
    }
}
