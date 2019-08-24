package com.lailee.eventlist.data.source

import com.lailee.eventlist.capture
import org.junit.After
import org.junit.Before
import org.junit.Test

import com.google.common.collect.Lists
import com.lailee.eventlist.any
import com.lailee.eventlist.eq
import org.hamcrest.core.Is.`is`
import org.junit.Assert
import org.junit.Assert.assertThat

import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class EventsRepositoryTest {


    private val EVENTS = Lists.newArrayList(DEFAULT_EVENT, DEFAULT_EVENT2)

    private lateinit var eventsRepository: EventsRepository

    @Mock private lateinit var eventsDataSource: EventsDataSource
    @Mock private lateinit var loadEventsCallback: EventsDataSource.LoadEventsCallback
    @Mock private lateinit var getEventCallback: EventsDataSource.GetEventCallback
    @Captor private lateinit var eventsCallbackCaptor: ArgumentCaptor<EventsDataSource.LoadEventsCallback>
    @Captor private lateinit var eventCallbackCaptor: ArgumentCaptor<EventsDataSource.GetEventCallback>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        eventsRepository = EventsRepository.getInstance(eventsDataSource)
    }

    @After
    fun tearDown() {
        EventsRepository.destroyInstance()
    }

    @Test
    fun getEvents() {
        eventsRepository.getEvents(loadEventsCallback)
        verify(eventsDataSource).getEvents(capture(eventsCallbackCaptor))

        //cached events
        eventsCallbackCaptor.value.onEventsLoaded(EVENTS)

        eventsRepository.getEvents(loadEventsCallback)
        verify(eventsDataSource).getEvents(any<EventsDataSource.LoadEventsCallback>())
    }

    @Test
    fun getEvent() {
        eventsRepository.getEvent(DEFAULT_ID, getEventCallback)
        verify(eventsDataSource).getEvent(eq(DEFAULT_ID), capture(eventCallbackCaptor))

        //cached events
        eventCallbackCaptor.value.onEventLoaded(DEFAULT_EVENT)

        eventsRepository.getEvent(DEFAULT_ID, getEventCallback)
        verify(eventsDataSource).getEvent(eq(DEFAULT_ID), any<EventsDataSource.GetEventCallback>())
    }

    @Test
    fun saveEvent() {
        eventsRepository.saveEvent(DEFAULT_EVENT)

        verify(eventsDataSource).saveEvent(DEFAULT_EVENT)
        assertThat(eventsRepository.cachedEvents.size, `is`(1))
    }

    @Test
    fun getEvent_requestEventFromDataSource() {
        eventsRepository.getEvent(DEFAULT_EVENT.id, getEventCallback)
        verify(eventsDataSource).getEvent(eq(DEFAULT_EVENT.id), any())
    }

    @Test
    fun getEvents_loadEventsFromDataSource() {
        eventsRepository.getEvents(loadEventsCallback)
        verify(eventsDataSource).getEvents(capture(eventsCallbackCaptor))
    }

    @Test
    fun updateEvent() {
        eventsRepository.updateEvent(DEFAULT_EVENT)

        verify(eventsDataSource).updateEvent(DEFAULT_EVENT)
        assertThat(eventsRepository.cachedEvents.size, `is`(1))
    }

    @Test fun updateEvent_updateEventToServiceAPIUpdatesCache() {
        with(eventsRepository) {
            var newEvent = DEFAULT_EVENT
            saveEvent(newEvent)

            newEvent.title = DEFAULT_TITLE2
            updateEvent(newEvent)
            verify(eventsDataSource).updateEvent(newEvent)

            assertThat(cachedEvents.size, `is`(1))
            val cachedNewEvent = cachedEvents[newEvent.id]
            Assert.assertNotNull(cachedNewEvent as Event)
            assertThat(cachedNewEvent.title, `is`(DEFAULT_TITLE2))
        }
    }

    companion object {

        private const val DEFAULT_TITLE = "title"
        private const val DEFAULT_DESCRIPTION = "description"
        private const val DEFAULT_ID = "id"
        private const val DEFAULT_CATEGORY = "DEFAULT_CATEGORY"
        private val DEFAULT_START = System.currentTimeMillis()
        private val DEFAULT_END = DEFAULT_START + 60 * 1000
        private const val DEFAULT_POSITION = 1


        private const val DEFAULT_TITLE2 = "title2"
        private const val DEFAULT_DESCRIPTION2 = "description2"
        private const val DEFAULT_ID2 = "id2"
        private const val DEFAULT_CATEGORY2 = "DEFAULT_CATEGORY2"
        private val DEFAULT_START2 = System.currentTimeMillis()
        private val DEFAULT_END2 = DEFAULT_START + 60 * 1000
        private const val DEFAULT_POSITION2 = 2
        private val DEFAULT_EVENT = Event(
            DEFAULT_TITLE,
            DEFAULT_CATEGORY,
            DEFAULT_START,
            DEFAULT_END,
            DEFAULT_DESCRIPTION,
            DEFAULT_POSITION,
            DEFAULT_ID
        )
        private val DEFAULT_EVENT2 = Event(
            DEFAULT_TITLE2,
            DEFAULT_CATEGORY2,
            DEFAULT_START2,
            DEFAULT_END2,
            DEFAULT_DESCRIPTION2,
            DEFAULT_POSITION2,
            DEFAULT_ID2
        )
    }
}