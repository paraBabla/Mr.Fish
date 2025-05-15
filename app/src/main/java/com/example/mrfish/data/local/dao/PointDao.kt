package com.example.mrfish.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mrfish.data.local.entities.FishingPointEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PointDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(points: List<FishingPointEntity>)

    @Update
    suspend fun updatePoint(point: FishingPointEntity)

    @Query("SELECT * FROM fishing_points")
    fun getAllPoints(): Flow<List<FishingPointEntity>>

    @Query("""
    SELECT * FROM fishing_points 
    WHERE 
        (categories LIKE '%FISHING_SHOP%' AND 
            (:showOpenShops = 1 AND isOpen = 1 OR 
             :showClosedShops = 1 AND isOpen = 0)) OR
        (categories LIKE '%PAID_LOCATION%' AND :showPaid = 1) OR
        (categories LIKE '%FREE_LOCATION%' AND :showFree = 1) OR
        (categories LIKE '%ARTIFICIAL_POND%' AND :showArtificialPonds = 1) OR
        (categories LIKE '%WILD_SPOT%' AND :showWildSpots = 1)
""")
    fun getFilteredPoints(
        showOpenShops: Boolean,
        showClosedShops: Boolean,
        showPaid: Boolean,
        showFree: Boolean,
        showArtificialPonds: Boolean,
        showWildSpots: Boolean
    ): Flow<List<FishingPointEntity>>

    @Query("DELETE FROM fishing_points WHERE id = :pointId")
    suspend fun deletePoint(pointId: String)

    @Query("SELECT COUNT(*) FROM fishing_points WHERE region_code = :regionId")
    suspend fun getRegionCount(regionId: String): Int

    @Query("SELECT * FROM fishing_points WHERE region_code = :regionId")
    suspend fun getByRegion(regionId: String): List<FishingPointEntity>
}