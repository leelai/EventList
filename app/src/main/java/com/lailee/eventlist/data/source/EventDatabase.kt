package com.lailee.eventlist.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Event::class], version = 1)
abstract class EventDatabase : RoomDatabase() {

    abstract fun eventDao(): EventsDao

    companion object {

        private var INSTANCE: EventDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): EventDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        EventDatabase::class.java, "Events.db")
                        .build()
                }
                return INSTANCE!!
            }
        }
    }

}