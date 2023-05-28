package pl.edu.pjwstk.s20265.wishlist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.edu.pjwstk.s20265.wishlist.data.model.ListItemEntity

@Database([ListItemEntity::class], version = 1)
@TypeConverters(Converters::class)

abstract class ListItemDatabase : RoomDatabase() {
    abstract val listItems: ListItemDao

    companion object {
        fun open(context: Context): ListItemDatabase = Room.databaseBuilder(
            context, ListItemDatabase::class.java, "listItems.db"
        ).build()
    }
}