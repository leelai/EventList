package com.lailee.eventlist.eventDetail

import com.lailee.eventlist.data.source.Event
import com.lailee.eventlist.data.source.EventsDataSource
import com.lailee.eventlist.data.source.EventsRepository

/**
 * Listens to user actions from the UI ([EventDetailFragment]), retrieves the data and updates
 * the UI as required.
 */
class EventDetailPresenter(
    private val eventId: String,
    private val eventsRepository: EventsRepository,
    private val eventDetailView: EventDetailContract.View
) : EventDetailContract.Presenter {

    init {
        eventDetailView.presenter = this
    }

    override fun start() {
        openEvent()
    }

    private fun openEvent() {
        if (eventId.isEmpty()) {
            eventDetailView.showMissingEvent()
            return
        }

        eventDetailView.setLoadingIndicator(true)
        eventsRepository.getEvent(eventId, object : EventsDataSource.GetEventCallback {
            override fun onEventLoaded(event: Event) {
                with(eventDetailView) {
                    // The view may not be able to handle UI updates anymore
                    if (!isActive) {
                        return@onEventLoaded
                    }
                    setLoadingIndicator(false)
                }
                showEvent(event)
            }

            override fun onDataNotAvailable() {
                with(eventDetailView) {
                    // The view may not be able to handle UI updates anymore
                    if (!isActive) {
                        return@onDataNotAvailable
                    }
                    showMissingEvent()
                }
            }
        })
    }

    override fun editEvent() {
        if (eventId.isEmpty()) {
            eventDetailView.showMissingEvent()
            return
        }
        eventDetailView.showEditEvent(eventId)
    }

    override fun deleteEvent() {
        if (eventId.isEmpty()) {
            eventDetailView.showMissingEvent()
            return
        }
        eventsRepository.deleteEvent(eventId)
        eventDetailView.showEventDeleted()
    }

    private fun showEvent(event: Event) {
        with(eventDetailView) {
            if (eventId.isEmpty()) {
                hideTitle()
                hideDescription()
            } else {
                showTitle(event.title)
                showStartTime(event.startTime)
                showEndTime(event.endTime)
                showCategory(event.category.toString())
                showDescription(event.description)
            }
        }
    }
}
