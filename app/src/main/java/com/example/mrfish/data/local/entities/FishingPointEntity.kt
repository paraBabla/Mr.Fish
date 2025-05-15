package com.example.mrfish.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mrfish.domain.model.FishingPoint
import com.example.mrfish.domain.model.PointCategory
import org.osmdroid.util.GeoPoint

@Entity(tableName = "fishing_points")
data class FishingPointEntity(
    @PrimaryKey val id: String,
    val lat: Double,
    val lng: Double,
    val categories: String,
    val title: String,
    val description: String?,
    val isOpen: Boolean?,
    val iconResId: Int,
    val isPaid: Boolean? = null,

    @ColumnInfo(name = "region_code")
    val regionId: String? = null
) {
    fun toDomain() = FishingPoint(
        id, lat, lng,
        categories = categories.split(",").map { PointCategory.valueOf(it) }.toSet(), // Конвертация String -> enum
        title, description, isOpen, isPaid, regionId
    )
}