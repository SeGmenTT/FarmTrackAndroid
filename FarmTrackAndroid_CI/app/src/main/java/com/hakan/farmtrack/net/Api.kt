package com.hakan.farmtrack.net

import com.hakan.farmtrack.data.*
import retrofit2.http.Body
import retrofit2.http.POST

data class SyncPayload(
    val animals: List<Animal>,
    val milk: List<Milk>,
    val weight: List<Weight>,
    val health: List<Health>,
    val breed: List<Breed>,
    val tasks: List<Task>
)
data class SimpleResp(val ok:Boolean?=null, val error:String?=null)

interface Api {
    @POST("/api/sync")
    suspend fun sync(@Body body: SyncPayload): SimpleResp

    @POST("/api/test")
    suspend fun test(): SimpleResp

    @POST("/api/notify-now")
    suspend fun notifyNow(): SimpleResp
}
