package pl.edu.pjwstk.s20265.wishlist.data

import androidx.room.TypeConverter
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun toDate(dateString: String): LocalDateTime? {
        return LocalDateTime.parse(dateString)
    }

    @TypeConverter
    fun toDateString(date: LocalDateTime): String {
        return date.toString()
    }
}