package com.lailee.eventlist.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Category::class], version = 1)
abstract class CategoryDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoriesDao

    companion object {

        private var INSTANCE: CategoryDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): CategoryDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        CategoryDatabase::class.java, "Category.db")
                        .build()
                }
                return INSTANCE!!
            }
        }
    }

}