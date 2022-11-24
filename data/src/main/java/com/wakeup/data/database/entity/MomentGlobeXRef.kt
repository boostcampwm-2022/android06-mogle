package com.wakeup.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "moment_globe",
    foreignKeys = [
        ForeignKey(
            entity = MomentEntity::class,
            parentColumns = ["moment_id"],
            childColumns = ["moment_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = GlobeEntity::class,
            parentColumns = ["globe_id"],
            childColumns = ["globe_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        )
    ],
    primaryKeys = ["moment_id", "globe_id"]
)
data class MomentGlobeXRef(
    @ColumnInfo(name = "moment_id", index = true) val momentId: Long,
    @ColumnInfo(name = "globe_id", index = true) val globeId: Long,
)
