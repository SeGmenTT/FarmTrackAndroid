package com.hakan.farmtrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Animal(
    @PrimaryKey val id: String,
    val name: String? = null,
    val species: String = "sığır",
    val sex: String = "dişi",
    val breed: String? = null,
    val birth: String? = null,
    val dam: String? = null,
    val sire: String? = null,
    val breeding: String? = null,
    val notes: String? = null
)

@Entity
data class Milk(@PrimaryKey(autoGenerate = true) val key: Long = 0, val id:String, val date:String, val liters: Double)
@Entity
data class Weight(@PrimaryKey(autoGenerate = true) val key: Long = 0, val id:String, val date:String, val kg: Double)
@Entity
data class Health(@PrimaryKey(autoGenerate = true) val key: Long = 0, val id:String, val date:String, val action:String, val drug:String?, val notes:String?)
@Entity
data class Breed(@PrimaryKey(autoGenerate = true) val key: Long = 0, val id:String, val date:String, val due:String)
@Entity
data class Task(@PrimaryKey(autoGenerate = true) val key: Long = 0, val id:String?, val title:String, val due:String, val repeat:String?, val note:String?, val done:Boolean=false)
