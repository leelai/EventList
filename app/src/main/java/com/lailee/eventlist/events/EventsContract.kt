package com.lailee.eventlist.events

import com.lailee.eventlist.BasePresenter
import com.lailee.eventlist.BaseView
import com.lailee.eventlist.data.source.Event

interface EventsContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)

        fun showEvents(evets: List<Event>)

        fun showAddEvent()

        fun showEventDetailsUi(eventId: String)

        fun showNoEvents()

        fun showSuccessfullySavedMessage()
    }

    interface Presenter : BasePresenter {

        fun result(requestCode: Int, resultCode: Int)

        fun loadEvents()

        fun addNewEvent()

        fun openEventDetails(requestedEvent: Event)

        fun swapEvent(from: Int, to: Int)

        fun deleteEvent(eventId: String)
    }
}