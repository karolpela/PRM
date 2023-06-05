package pl.edu.pjwstk.s20265.wishlist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import pl.edu.pjwstk.s20265.wishlist.data.model.ListItemEntity

@Dao
interface ListItemDao {
    @Query("select * from listItem;")
    fun getAll(): List<ListItemEntity>

    @Query("select * from listItem order by addedOn asc;")
    fun getAllSortedByDate(): List<ListItemEntity>

    @Query("select * from listItem where id = :id")
    fun getListItem(id: Long): ListItemEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addListItem(listItem: ListItemEntity): Long

    @Update
    fun updateListItem(listItem: ListItemEntity)

    @Query("delete from listItem where id = :id")
    fun deleteListItem(id: Long)
}