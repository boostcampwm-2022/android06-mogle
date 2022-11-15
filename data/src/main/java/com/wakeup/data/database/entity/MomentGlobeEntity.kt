package com.wakeup.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "moment_globe",
    foreignKeys = [
        ForeignKey(
            entity = MomentEntity::class,
            parentColumns = ["id"],
            childColumns = ["moment_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GlobeEntity::class,
            parentColumns = ["id"],
            childColumns = ["globe_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MomentGlobeEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "moment_id", index = true) val momentId: Int,
    @ColumnInfo(name = "globe_id", index = true) val globeId: Int,
)
