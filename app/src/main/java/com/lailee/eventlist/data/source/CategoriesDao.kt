package com.lailee.eventlist.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao interface CategoriesDao {
    @Query("SELECT * FROM categories") fun getCategories(): List<Category>
    @Query("SELECT * FROM categories WHERE entryid = :categoryId") fun getCategoryById(categoryId: String): Category?
    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertCategory(category: Category)
    @Query("DELETE FROM categories WHERE entryid = :categoryId") fun deleteCategoryById(categoryId: String): Int
}