package com.lailee.eventlist.data.source

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "categories")
data class Category @JvmOverloads constructor(
    @ColumnInfo(name = "name") var title: String = "",
    @PrimaryKey @ColumnInfo(name = "entryid") var id: String = UUID.randomUUID().toString()
) {}