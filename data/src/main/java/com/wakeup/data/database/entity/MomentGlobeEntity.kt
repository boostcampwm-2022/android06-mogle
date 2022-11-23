package com.wakeup.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "moment_globe",
    foreignKeys = [
        ForeignKey(
            entity = MomentEntity::class,
            parentColumns = ["moment_entity_id"],
            childColumns = ["moment_entity_id"],
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GlobeEntity::class,
            parentColumns = ["globe_entity_id"],
            childColumns = ["globe_entity_id"],
            onUpdate = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["moment_entity_id", "globe_entity_id"]
)
data class MomentGlobeEntity(
    @ColumnInfo(name = "moment_entity_id", index = true) val momentId: Long,
    @ColumnInfo(name = "globe_entity_id", index = true) val globeId: Long,
)
