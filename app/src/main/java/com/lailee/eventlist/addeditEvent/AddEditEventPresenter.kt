package com.lailee.eventlist.addeditEvent

import android.util.Log
import com.lailee.eventlist.data.source.CategoriesDataSource
import com.lailee.eventlist.data.source.Category
import com.lailee.eventlist.data.source.Event
import com.lailee.eventlist.data.source.EventsDataSource


class AddEditEventPresenter(
    private val eventId: String?,
    val eventsRepository: EventsDataSource,
    val categoriesDataSource: CategoriesDataSource,
    val addEventView: AddEditEventContract.View
) : AddEditEventContract.Presenter, EventsDataSource.GetEventCallback, CategoriesDataSource.LoadCategoriesCallback {

    private val TAG = "AddEditEventPresenter"
    private var position: Int = 0
    init {
        addEventView.presenter = this
    }

    override fun start() {
        if (eventId != null) {
            populateEvent()
        }
    }

    override fun saveEvent(title: String, category: String, start: Long, end: Long, description: String) {
        if (eventId == null) {
            createEvent(title, category, start, end, description)
        } else {
            updateEvent(title, category, start, end, description)
        }
    }

    override fun populateEvent() {
        if (eventId == null) {
            throw RuntimeException("populateEvent() was called but event is new.")
        }
        eventsRepository.getEvent(eventId, this)
    }

    override fun onEventLoaded(event: Event) {
        // The view may not be able to handle UI updates anymore
        Log.d(TAG, "onEventLoaded:$event")
        addEventView.setTitle(event.title)
        addEventView.setCategory(event.category)
        addEventView.setDescription(event.description)
        addEventView.setStartTime(event.start)
        addEventView.setEndTime(event.end)
        position = event.position
    }

    override fun onDataNotAvailable() {
        // The view may not be able to handle UI updates anymore
        addEventView.showEmptyEventError()
    }

    private fun createEvent(title: String, category: String, start: Long, end: Long, description: String) {
        val newEvent = Event(title, category, start, end, description)
        if (newEvent.isEmpty) {
            addEventView.showEmptyEventError()
        } else {
            eventsRepository.saveEvent(newEvent)
            addEventView.showEventsList()
        }
    }

    private fun updateEvent(title: String, category: String, start: Long, end: Long, description: String) {
        if (eventId == null) {
            throw RuntimeException("updateEvent() was called but event is new.")
        }
        if (title.isEmpty()) {
            addEventView.showEmptyEventError()
        } else {
            eventsRepository.updateEvent(Event(title, category, start, end, description, position, eventId))
            addEventView.showEventsList() // After an edit, go back to the list.
        }
    }

    override fun onCategoriesLoaded(categories: List<Category>) {
        var categoryList = mutableListOf<String>()
        categories.forEach {
            categoryList.add(it.title)
        }
        addEventView.showCategories(categoryList)
    }

    override fun selectCategory() {
        categoriesDataSource.getCategories(this)
    }
}
