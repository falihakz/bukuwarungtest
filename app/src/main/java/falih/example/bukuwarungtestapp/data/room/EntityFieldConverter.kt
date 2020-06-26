package falih.example.bukuwarungtestapp.data.room

import androidx.room.TypeConverter
import java.util.*

class EntityFieldConverter {
    @TypeConverter
    fun fromDate(date: Date?) = date?.time

    @TypeConverter
    fun toDate(millis: Long?) = millis?.let { Date(millis) }
}
