package de.nwuensche.qrcodestorage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "valueTable")
data class ItemModel(@PrimaryKey(autoGenerate = true) val id: Long = 0,
                     val value: String)