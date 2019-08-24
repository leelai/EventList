package com.lailee.eventlist.data.source

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.lailee.eventlist.utils.SingleExecutors
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class EventsDataSourceImplTest {

    private lateinit var context: Context
    private lateinit var database: EventDatabase
    private lateinit var eventsDataSource: EventsDataSource
    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, EventDatabase::class.java).build()
        EventsDataSourceImpl.clearInstance()
        eventsDataSource = EventsDataSourceImpl.getInstance(SingleExecutors(), database.eventDao())
    }

    @After
    fun tearDown() {
        database.close()
        EventsDataSourceImpl.clearInstance()
    }

    @Test fun testPreConditions() {
        assertNotNull(eventsDataSource)
    }

    @Test
    fun saveEvent_getEventById() {

        with(eventsDataSource) {
            saveEvent(DEFAULT_EVENT)
            getEvent(DEFAULT_EVENT.id, object : EventsDataSource.GetEventCallback {
                override fun onEventLoaded(event: Event) {
                    assertThat(event, `is`(DEFAULT_EVENT))
                }

                override fun onDataNotAvailable() {
                    fail("onDataNotAvailable")
                }
            })
        }
    }

    @Test
    fun saveEvent_getEventById2() {

        with(eventsDataSource) {
            saveEvent(DEFAULT_EVENT)

            getEvent(DEFAULT_EVENT.id, object : EventsDataSource.GetEventCallback {
                override fun onEventLoaded(event: Event) {
                    assertThat(event, `is`(DEFAULT_EVENT))
                }

                override fun onDataNotAvailable() {
                    fail("onDataNotAvailable")
                }
            })
        }
    }

    @Test
    fun saveEvent() {

    }

    companion object {

        private val TAG = EventsDataSourceImplTest::class.java.simpleName
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
            System.currentTimeMillis(),
            System.currentTimeMillis() + 1000 * 60,
            DEFAULT_DESCRIPTION,
            1,
            DEFAULT_ID
        )
        private val DEFAULT_EVENT2 = Event(
            DEFAULT_TITLE2,
            DEFAULT_CATEGORY2,
            System.currentTimeMillis(),
            System.currentTimeMillis() + 1000 * 60,
            DEFAULT_DESCRIPTION2,
            2,
            DEFAULT_ID2
        )
    }
}