package com.example.mrfish.data.repository

import android.util.Log
import com.example.mrfish.data.Utils.GeoUtils.createBbox
import com.example.mrfish.data.local.dao.PointDao
import com.example.mrfish.domain.usecase.OverpassApiService
import com.example.mrfish.domain.filter.FilterState
import com.example.mrfish.domain.model.FishingPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

class PointRepository @Inject constructor(
    private val dao: PointDao,
    private val overpassService: OverpassApiService //
) {
    fun getPoints(filter: FilterState): Flow<List<FishingPoint>> {
        return dao.getFilteredPoints(
            showOpenShops = filter.showWorkingShops,
            showClosedShops = filter.showClosedShops,
            showPaid = filter.showPaidFishing,
            showFree = filter.showFreeFishing,
            showArtificialPonds = filter.showArtificialPonds,
            showWildSpots = filter.showWildSpots
        ).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    // Динамическое обновление точки
    suspend fun updatePoint(point: FishingPoint) {
        dao.updatePoint(point.toEntity())
    }

    private suspend fun fetchAndCache(regionId: String): List<FishingPoint> {
        val points = overpassService.getWaterObjects(regionId)
        Log.d("MainActivity", "массив точек получен: $points")
        dao.insertAll(points.map { it.toEntity() })
        return points
    }

    // Загрузка и кэширование новых данных
    suspend fun refreshPoints(center: GeoPoint, radiusKm: Double): List<FishingPoint> {
        val bbox = try {
            createBbox(center, radiusKm).also {
                Log.d("MainActivity", "Generated bbox: $it")
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error creating bbox", e)
            return emptyList()
        }
        Log.d("MainActivity", "bbox: $bbox")
        return fetchAndCache(bbox)
    }

    suspend fun getPointsInRegion(regionId: String): List<FishingPoint> {
        return dao.getByRegion(regionId).map { it.toDomain() }.ifEmpty {
            fetchAndCache(regionId)
        }
    }

    suspend fun prefetchRegion(regionId: String) {
        withContext(Dispatchers.IO) {
            if (dao.getRegionCount(regionId) == 0) {
                getPointsInRegion(regionId) // Фоновая загрузка
            }
        }
    }
}










