package com.wakeup.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "picture",
    indices = [Index(value = ["bitmap"], unique = true)]
)
data class PictureEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "bitmap") val bitmap: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PictureEntity

        if (id != other.id) return false
        if (!bitmap.contentEquals(other.bitmap)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + bitmap.contentHashCode()
        return result
    }
}