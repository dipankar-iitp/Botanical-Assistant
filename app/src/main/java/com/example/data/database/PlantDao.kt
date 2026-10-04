package com.example.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.PlantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {

    @Query("SELECT * FROM plants ORDER BY timestamp DESC")
    fun getAllPlants(): Flow<List<PlantEntity>>

    @Query("SELECT * FROM plants WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoritePlants(): Flow<List<PlantEntity>>

    @Query("SELECT * FROM plants WHERE toxicityWarning = 1 ORDER BY timestamp DESC")
    fun getToxicPlants(): Flow<List<PlantEntity>>

    @Query("SELECT * FROM plants WHERE id = :id LIMIT 1")
    fun getPlantById(id: Long): Flow<PlantEntity?>

    @Query("SELECT * FROM plants WHERE scientificName LIKE '%' || :query || '%' OR commonNames LIKE '%' || :query || '%' OR activeCompounds LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchPlants(query: String): Flow<List<PlantEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: PlantEntity): Long

    @Update
    suspend fun updatePlant(plant: PlantEntity)

    @Delete
    suspend fun deletePlant(plant: PlantEntity)

    @Query("DELETE FROM plants WHERE id = :id")
    suspend fun deletePlantById(id: Long)

    @Query("SELECT COUNT(*) FROM plants")
    suspend fun count(): Int
}
