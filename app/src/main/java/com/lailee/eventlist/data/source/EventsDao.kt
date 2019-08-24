package com.lailee.eventlist.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao interface EventsDao {
    @Query("SELECT * FROM Events ORDER BY position ASC") fun getEvents(): List<Event>
    @Query("SELECT * FROM Events WHERE entryid = :eventId") fun getEventById(eventId: String): Event?
    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertEvent(event: Event)
    @Update fun updateEvent(event: Event): Int
    @Query("DELETE FROM Events WHERE entryid = :eventId") fun deleteEventById(eventId: String): Int
    @Query("DELETE FROM Events") fun deleteEvents()
}