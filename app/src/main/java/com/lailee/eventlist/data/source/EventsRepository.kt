package com.lailee.eventlist.data.source

import android.util.Log
import java.util.LinkedHashMap

class EventsRepository(
    val eventsDataSource: EventsDataSource
) : EventsDataSource {

    var cachedEvents: LinkedHashMap<String, Event> = LinkedHashMap()
    private var cachedEvents2: ArrayList<Event> = ArrayList()
    private var lastPosition: Int = -1

    override fun getEvents(callback: EventsDataSource.LoadEventsCallback) {
        //Log.d(TAG, "getEvents")
        if (cachedEvents.isNotEmpty()) {
            callback.onEventsLoaded(ArrayList(cachedEvents.values))
            return
        }

        eventsDataSource.getEvents(object : EventsDataSource.LoadEventsCallback {
            override fun onEventsLoaded(events: List<Event>) {
                refreshCache(events)
                callback.onEventsLoaded(ArrayList(cachedEvents.values))
            }

            override fun onDataNotAvailable() {
                Log.d(TAG, "onDataNotAvailable")
                callback.onDataNotAvailable()
            }
        })
    }

    override fun getEvent(eventId: String, callback: EventsDataSource.GetEventCallback) {
        val eventInCache = getEventWithId(eventId)
        if (eventInCache != null) {
            callback.onEventLoaded(eventInCache)
            return
        }

        eventsDataSource.getEvent(eventId, object : EventsDataSource.GetEventCallback {
            override fun onEventLoaded(event: Event) {
                cacheAndPerform(event) {
                    callback.onEventLoaded(it)
                }
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })

    }

    override fun updateEvent(event: Event) {
        cacheAndPerform(event) {
            eventsDataSource.updateEvent(it)
        }
    }

    override fun saveEvent(event: Event) {
        cacheAndPerform(event) {
            eventsDataSource.saveEvent(it)
        }
    }

    override fun refreshEvents() {
    }

    override fun deleteAllEvents() {
        eventsDataSource.deleteAllEvents()
        cachedEvents2.clear()
    }

    override fun deleteEvent(eventId: String) {
        Log.d(TAG, "deleteEvent$eventId")
        eventsDataSource.deleteEvent(eventId)

        val event = cachedEvents[eventId]
        cachedEvents2.remove(event)
        Log.d(TAG, "cachedEvents2 size=${cachedEvents2.size}")
        cachedEvents.remove(eventId)
        Log.d(TAG, "cachedEvents size=${cachedEvents.size}")
    }

    private fun getEventWithId(id: String) = cachedEvents[id]

    private fun refreshCache(events: List<Event>) {
        cachedEvents2.clear()
        cachedEvents2 = events as ArrayList<Event>

        cachedEvents.clear()
        events.forEach {
            cacheAndPerform(it) {}
        }
    }

    private inline fun cacheAndPerform(event: Event, perform: (Event) -> Unit) {
        val cachedEvent = Event(event.title, event.category, event.start, event.end, event.description, event.position, event.id)
        //evaluate the position:
        if (cachedEvent.position == -1) {
            cachedEvent.position = lastPosition + 1
            lastPosition = cachedEvent.position
            cachedEvents2.add(cachedEvent)
        } else if (cachedEvent.position > lastPosition) {
            lastPosition = cachedEvent.position
        }
        cachedEvents[cachedEvent.id] = cachedEvent
        perform(cachedEvent)
    }

    override fun swap(from: Int, to: Int) {
        //cachedEvents
        val targetEvent = cachedEvents2[to]
        val fromEvent = cachedEvents2[from]

        val newPosition = targetEvent.position
        targetEvent.position = fromEvent.position
        fromEvent.position = newPosition

        cachedEvents2.remove(fromEvent)
        cachedEvents2.add(to, fromEvent)

        eventsDataSource.updateEvent(fromEvent)
        eventsDataSource.updateEvent(targetEvent)
    }

    companion object {

        private val TAG = EventsRepository::class.java.simpleName
        private var INSTANCE: EventsRepository? = null

        @JvmStatic fun getInstance(eventsDataSource: EventsDataSource): EventsRepository {
            return INSTANCE ?: EventsRepository(eventsDataSource)
                .apply { INSTANCE = this }
        }

        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}