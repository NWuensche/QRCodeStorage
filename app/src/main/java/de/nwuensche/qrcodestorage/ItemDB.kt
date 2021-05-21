package de.nwuensche.qrcodestorage

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ItemModel::class], version = 1)
abstract class ItemDB : RoomDatabase() {
    abstract fun itemDAO(): ItemDAO
}