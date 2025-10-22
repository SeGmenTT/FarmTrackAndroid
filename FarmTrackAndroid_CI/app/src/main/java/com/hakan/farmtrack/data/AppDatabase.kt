package com.hakan.farmtrack.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Animal::class, Milk::class, Weight::class, Health::class, Breed::class, Task::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase(){
    abstract fun animal(): AnimalDao
    abstract fun milk(): MilkDao
    abstract fun weight(): WeightDao
    abstract fun health(): HealthDao
    abstract fun breed(): BreedDao
    abstract fun task(): TaskDao

    companion object {
        @Volatile private var I: AppDatabase? = null
        fun get(ctx: Context): AppDatabase =
            I ?: synchronized(this){
                I ?: Room.databaseBuilder(ctx, AppDatabase::class.java, "farmtrack.db").build().also{ I = it }
            }
    }
}
