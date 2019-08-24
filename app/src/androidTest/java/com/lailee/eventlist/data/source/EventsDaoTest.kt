package com.lailee.eventlist.data.source

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Test


class EventsDaoTest {

    private lateinit var context: Context
    private lateinit var database: EventDatabase

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, EventDatabase::class.java).build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertEventAndGetById() {
        database.eventDao().insertEvent(DEFAULT_EVENT)
        var event = database.eventDao().getEventById(DEFAULT_EVENT.id)
        assertEvent(event, DEFAULT_ID, DEFAULT_TITLE, DEFAULT_CATEGORY, DEFAULT_START, DEFAULT_END, DEFAULT_DESCRIPTION)
    }

    @Test
    fun insertEventsAndAllEvents() {
        database.eventDao().insertEvent(DEFAULT_EVENT)
        var events = database.eventDao().getEvents()
        assertEvent(events[0], DEFAULT_ID, DEFAULT_TITLE, DEFAULT_CATEGORY, DEFAULT_START, DEFAULT_END, DEFAULT_DESCRIPTION)
    }

    @Test
    fun updateEventAndGetById() {
        database.eventDao().insertEvent(DEFAULT_EVENT)
        var event = database.eventDao().getEventById(DEFAULT_EVENT.id)
        assertEvent(event, DEFAULT_ID, DEFAULT_TITLE, DEFAULT_CATEGORY, DEFAULT_START, DEFAULT_END, DEFAULT_DESCRIPTION)

        event!!.title = DEFAULT_TITLE2
        event.category = DEFAULT_CATEGORY2
        event.start = DEFAULT_START2
        event.end = DEFAULT_END2
        event.description = DEFAULT_DESCRIPTION2
        database.eventDao().updateEvent(event)

        event = database.eventDao().getEventById(DEFAULT_EVENT.id)
        assertEvent(event, DEFAULT_ID, DEFAULT_TITLE2, DEFAULT_CATEGORY2, DEFAULT_START2, DEFAULT_END2, DEFAULT_DESCRIPTION2)
    }


    @Test
    fun deleteEventByIdAndGettingEvents() {
        database.eventDao().insertEvent(DEFAULT_EVENT)
        database.eventDao().deleteEventById(DEFAULT_EVENT.id)
        var events = database.eventDao().getEvents()
        MatcherAssert.assertThat(events.size, CoreMatchers.`is`(0))
    }

    @Test
    fun deleteEventsAndGettingEvents() {
        database.eventDao().insertEvent(DEFAULT_EVENT)
        database.eventDao().deleteEvents()
        var events = database.eventDao().getEvents()
        MatcherAssert.assertThat(events.size, CoreMatchers.`is`(0))
    }

    private fun assertEvent(
        event: Event?,
        id: String,
        title: String,
        category: String,
        start: Long,
        end: Long,
        description: String
    ) {
        MatcherAssert.assertThat<Event>(event as Event, CoreMatchers.notNullValue())

        MatcherAssert.assertThat(event.id, CoreMatchers.`is`(id))
        MatcherAssert.assertThat(event.title, CoreMatchers.`is`(title))
        MatcherAssert.assertThat(event.description, CoreMatchers.`is`(description))
        MatcherAssert.assertThat(event.category, CoreMatchers.`is`(category))
        MatcherAssert.assertThat(event.start, CoreMatchers.`is`(start))
        MatcherAssert.assertThat(event.end, CoreMatchers.`is`(end))
    }

    companion object {

        private val TAG = EventsDaoTest::class.java.simpleName
        private const val DEFAULT_TITLE = "title"
        private const val DEFAULT_DESCRIPTION = "description"
        private const val DEFAULT_ID = "id"
        private const val DEFAULT_CATEGORY = "DEFAULT_CATEGORY"
        private val DEFAULT_START = System.currentTimeMillis()
        private val DEFAULT_END = DEFAULT_START + 60 * 1000


        private const val DEFAULT_TITLE2 = "title2"
        private const val DEFAULT_DESCRIPTION2 = "description2"
        private const val DEFAULT_ID2 = "id2"
        private const val DEFAULT_CATEGORY2 = "DEFAULT_CATEGORY2"
        private val DEFAULT_START2 = System.currentTimeMillis()
        private val DEFAULT_END2 = DEFAULT_START + 60 * 1000

        private val DEFAULT_EVENT = Event(
            DEFAULT_TITLE,
            DEFAULT_CATEGORY,
            DEFAULT_START,
            DEFAULT_END,
            DEFAULT_DESCRIPTION,
            1,
            DEFAULT_ID
        )
        private val DEFAULT_EVENT2 = Event(
            DEFAULT_TITLE2,
            DEFAULT_CATEGORY2,
            DEFAULT_START2,
            DEFAULT_END2,
            DEFAULT_DESCRIPTION2,
            2,
            DEFAULT_ID2
        )
    }
}