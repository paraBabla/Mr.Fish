package com.example.mrfish.domain.model

import com.example.mrfish.R
import com.example.mrfish.data.local.entities.FishingPointEntity
import java.util.UUID

data class FishingPoint(
    val id: String = UUID.randomUUID().toString(),
    val lat: Double,
    val lng: Double,
    val categories: Set<PointCategory>,
    var title: String,
    var description: String? = null,
    var isOpen: Boolean? = null,
    val isPaid: Boolean? = null,
    val regionId: String? = null
){
    val iconResId: Int
        get() = when {
            PointCategory.FISHING_SHOP in categories ->
                if (isOpen == true) R.drawable.ic_shop_open
                else R.drawable.ic_shop_closed
            PointCategory.PAID_FISHING in categories -> R.drawable.ic_paid
            else -> R.drawable.ic_free
        }
    fun toEntity() = FishingPointEntity( // маппинг полей
        id, lat, lng, categories = categories.joinToString(",") { it.name }, title, description, isOpen, iconResId, isPaid, regionId
    )
}