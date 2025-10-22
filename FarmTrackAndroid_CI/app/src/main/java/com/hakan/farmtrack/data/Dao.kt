package com.hakan.farmtrack.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimalDao{
    @Query("SELECT * FROM Animal ORDER BY id ASC")
    fun all(): Flow<List<Animal>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(a: Animal)
    @Delete suspend fun delete(a: Animal)
    @Query("SELECT * FROM Animal WHERE id = :id LIMIT 1")
    suspend fun byId(id:String): Animal?
}

@Dao interface MilkDao{
    @Query("SELECT * FROM Milk ORDER BY date DESC") fun all(): Flow<List<Milk>>
    @Insert suspend fun insert(m:Milk)
    @Query("DELETE FROM Milk WHERE key=:k") suspend fun del(k:Long)
}

@Dao interface WeightDao{
    @Query("SELECT * FROM Weight ORDER BY date DESC") fun all(): Flow<List<Weight>>
    @Insert suspend fun insert(x:Weight)
    @Query("DELETE FROM Weight WHERE key=:k") suspend fun del(k:Long)
}

@Dao interface HealthDao{
    @Query("SELECT * FROM Health ORDER BY date DESC") fun all(): Flow<List<Health>>
    @Insert suspend fun insert(x:Health)
    @Query("DELETE FROM Health WHERE key=:k") suspend fun del(k:Long)
}

@Dao interface BreedDao{
    @Query("SELECT * FROM Breed ORDER BY date DESC") fun all(): Flow<List<Breed>>
    @Insert suspend fun insert(x:Breed)
    @Query("DELETE FROM Breed WHERE key=:k") suspend fun del(k:Long)
}

@Dao interface TaskDao{
    @Query("SELECT * FROM Task ORDER BY due ASC") fun all(): Flow<List<Task>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(x:Task)
    @Query("DELETE FROM Task WHERE key=:k") suspend fun del(k:Long)
}
