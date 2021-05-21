package de.nwuensche.qrcodestorage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemDAO {
    @Insert
    fun insertItem(value: ItemModel) //Cant only do string alone

    @Query(value = "SELECT value from valueTable")
    fun getAllItemValues(): List<String>
}