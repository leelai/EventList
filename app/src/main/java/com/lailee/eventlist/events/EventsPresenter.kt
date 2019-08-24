package com.lailee.eventlist.events

import android.util.Log
import com.lailee.eventlist.BuildConfig.DEBUG
import com.lailee.eventlist.data.source.Event
import com.lailee.eventlist.data.source.EventsDataSource
import com.lailee.eventlist.data.source.EventsRepository

class EventsPresenter(val eventsRepository: EventsRepository, val eventsView: EventsContract.View) : EventsContract.Presenter{

    init {
        eventsView.presenter = this
    }

    override fun result(requestCode: Int, resultCode: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadEvents() {
        loadEvents(true)
    }

    override fun addNewEvent() {
        eventsView.showAddEvent()
    }

    override fun deleteEvent(eventId: String) {
        eventsRepository.deleteEvent(eventId)
    }

    override fun openEventDetails(requestedEvent: Event) {
        eventsView.showEventDetailsUi(requestedEvent.id)
    }

    override fun start() {
        loadEvents()
    }

    private fun loadEvents(showLoadingUI: Boolean) {
        if (DEBUG) Log.d(TAG, "loadEvents")
        if (showLoadingUI) {
            eventsView.setLoadingIndicator(true)
        }

        eventsRepository.getEvents(object : EventsDataSource.LoadEventsCallback {
            override fun onEventsLoaded(events: List<Event>) {
                if (showLoadingUI) {
                    eventsView.setLoadingIndicator(false)
                }
                processEvents(events)
            }

            override fun onDataNotAvailable() {
                eventsView.showNoEvents()
            }
        })
    }

    private fun processEvents(events: List<Event>) {
        if (DEBUG) Log.d(TAG, "events size:" + events.size )
        if (events.isEmpty()) {
            eventsView.showNoEvents()
        } else {
            eventsView.showEvents(events)
        }
    }

    override fun swapEvent(from: Int, to: Int) {
        eventsRepository.swap(from, to)
    }

    companion object {
        //test data
        private val TAG = EventsPresenter::class.java.simpleName
        private const val DEFAULT_TITLE = "title"
        private const val DEFAULT_DESCRIPTION = "description"
        private const val DEFAULT_ID = "id"
        private const val DEFAULT_CATEGORY = "Personal"
        private val DEFAULT_START = System.currentTimeMillis()
        private val DEFAULT_END = DEFAULT_START + 60 * 1000


        private const val DEFAULT_TITLE2 = "title2"
        private const val DEFAULT_DESCRIPTION2 = "description2"
        private const val DEFAULT_ID2 = "id2"
        private const val DEFAULT_CATEGORY2 = "Personal"
        private val DEFAULT_START2 = System.currentTimeMillis()
        private val DEFAULT_END2 = DEFAULT_START + 60 * 1000

        private val DEFAULT_EVENT = Event(
            DEFAULT_TITLE,
            DEFAULT_CATEGORY,
            System.currentTimeMillis(),
            System.currentTimeMillis() + 1000 * 60,
            DEFAULT_DESCRIPTION
        )
        private val DEFAULT_EVENT2 = Event(
            DEFAULT_TITLE2,
            DEFAULT_CATEGORY2,
            System.currentTimeMillis(),
            System.currentTimeMillis() + 1000 * 60,
            DEFAULT_DESCRIPTION2
        )
        private val DEFAULT_EVENT3 = Event(
            "DEFAULT_EVENT3",
            DEFAULT_CATEGORY2,
            System.currentTimeMillis(),
            System.currentTimeMillis() + 1000 * 60,
            "DEFAULT_DESCRIPTION2"
        )
    }
}