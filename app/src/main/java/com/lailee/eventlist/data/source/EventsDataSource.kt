package com.lailee.eventlist.data.source

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.lailee.eventlist.util.AppExecutors

interface EventsDataSource{

    interface LoadEventsCallback {

        fun onEventsLoaded(events: List<Event>)

        fun onDataNotAvailable()
    }

    interface GetEventCallback {

        fun onEventLoaded(event: Event)

        fun onDataNotAvailable()
    }

    fun getEvents(callback: LoadEventsCallback)

    fun getEvent(eventId: String, callback: GetEventCallback)

    fun saveEvent(event: Event)

    fun updateEvent(event: Event)

    fun refreshEvents()

    fun deleteAllEvents()

    fun deleteEvent(eventId: String)

    fun swap(from: Int, to: Int)
}

class EventsDataSourceImpl private constructor(
    val appExecutors: AppExecutors,
    val eventsDao: EventsDao
) : EventsDataSource {

    override fun getEvents(callback: EventsDataSource.LoadEventsCallback) {
        appExecutors.diskIO.execute {
            val events = eventsDao.getEvents()
            appExecutors.mainThread.execute {
                if (events.isEmpty()) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onEventsLoaded(events)
                }
            }
        }
    }

    override fun getEvent(eventId: String, callback: EventsDataSource.GetEventCallback) {
        appExecutors.diskIO.execute {
            val event = eventsDao.getEventById(eventId)
            appExecutors.mainThread.execute {
                if (event != null) {
                    callback.onEventLoaded(event)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    override fun saveEvent(event: Event) {
        appExecutors.diskIO.execute { eventsDao.insertEvent(event) }
    }

    override fun updateEvent(event: Event) {
        Log.d(TAG, "updateEvent event=$event")
        appExecutors.diskIO.execute { val result = eventsDao.updateEvent(event)
            Log.d(TAG, "updateEvent result=$result")}
    }

    override fun refreshEvents() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllEvents() {
        appExecutors.diskIO.execute { eventsDao.deleteEvents() }
    }

    override fun deleteEvent(eventId: String) {
        appExecutors.diskIO.execute { eventsDao.deleteEventById(eventId) }
    }

    override fun swap(from: Int, to: Int) {

    }

    companion object {

        private const val TAG = "EventsDataSource"
        private var INSTANCE: EventsDataSourceImpl? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, eventsDao: EventsDao): EventsDataSourceImpl {
            if (INSTANCE == null) {
                synchronized(EventsDataSourceImpl::javaClass) {
                    INSTANCE = EventsDataSourceImpl(appExecutors, eventsDao)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }
}