package com.lailee.eventlist.data.source

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

@Entity(tableName = "events")
data class Event @JvmOverloads constructor(
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "category") var category: String = "Personal", //todo: enum personal, business, others
    @ColumnInfo(name = "start") var start: Long,
    @ColumnInfo(name = "end") var end: Long,
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "position") var position: Int = -1,
    @PrimaryKey @ColumnInfo(name = "entryid") var id: String = UUID.randomUUID().toString()
) {
    val isEmpty
        get() = title.isEmpty() || start == 0L || end == 0L
    val startTime
        get() = DateTime(start).toLocalDateTime().toString(fmt)
    val endTime
        get() = DateTime(end).toLocalDateTime().toString(fmt)

    companion object {
        private val fmt = DateTimeFormat.forPattern("MMM dd,yyyy HH:mm")
    }

}