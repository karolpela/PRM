package pl.edu.pjwstk.s20265.wishlist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import pl.edu.pjwstk.s20265.wishlist.data.model.ListItemEntity
import pl.edu.pjwstk.s20265.wishlist.model.ListItem

@Dao
interface ListItemDao {
    @Query("select * from listItem;")
    fun getAll(): List<ListItemEntity>

    @Query("select * from listItem order by name asc;") //TODO change to date
    fun getAllSortedByName(): List<ListItemEntity>

    @Query("select * from listItem where id = :id")
    fun getListItem(id: Long): ListItemEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addListItem(listItem: ListItemEntity)

    @Update
    fun updateListItem(listItem: ListItemEntity)

    @Query("delete from listItem where id = :id")
    fun deleteListItem(id: Long)
}