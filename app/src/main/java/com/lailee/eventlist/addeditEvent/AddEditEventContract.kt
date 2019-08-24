package com.lailee.eventlist.addeditEvent

import com.lailee.eventlist.BasePresenter
import com.lailee.eventlist.BaseView

/**
 * This specifies the contract between the view and the presenter.
 */
interface AddEditEventContract {

    interface View : BaseView<Presenter> {

        fun showEmptyEventError()

        fun showEventsList()

        fun setTitle(title: String)

        fun setDescription(description: String)

        fun setCategory(category: String)

        fun setStartTime(time: Long)

        fun setEndTime(time: Long)

        fun showCategories(categories: List<String>)
    }

    interface Presenter : BasePresenter {

        fun selectCategory()

        fun saveEvent(title: String, category: String, start: Long, end: Long, description: String)

        fun populateEvent()
    }
}
