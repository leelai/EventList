package com.lailee.eventlist.eventDetail

import com.lailee.eventlist.BasePresenter
import com.lailee.eventlist.BaseView

/**
 * This specifies the contract between the view and the presenter.
 */
interface EventDetailContract {

    interface View : BaseView<Presenter> {

        val isActive: Boolean

        fun setLoadingIndicator(active: Boolean)

        fun showMissingEvent()

        fun hideTitle()

        fun showTitle(title: String)

        fun showCategory(category: String)

        fun showStartTime(startTime: String)

        fun showEndTime(endTime: String)

        fun hideDescription()

        fun showDescription(description: String)

        fun showEditEvent(eventId: String)

        fun showEventDeleted()
    }

    interface Presenter : BasePresenter {

        fun editEvent()

        fun deleteEvent()
    }
}
