package com.hakan.farmtrack.sync

import android.content.Context
import androidx.work.*
import com.hakan.farmtrack.data.AppDatabase
import com.hakan.farmtrack.net.ApiClient
import com.hakan.farmtrack.net.SyncPayload
import com.hakan.farmtrack.settings.SettingsRepo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

class SyncWorker(ctx: Context, params: WorkerParameters): CoroutineWorker(ctx, params){
    override suspend fun doWork(): Result {
        val db = AppDatabase.get(applicationContext)
        val s = SettingsRepo(applicationContext)
        val server = s.server.first()
        if(server.isBlank()) return Result.success()
        val api = ApiClient.create(server)
        val payload = SyncPayload(
            animals = db.animal().all().firstOrNull() ?: emptyList(),
            milk = db.milk().all().firstOrNull() ?: emptyList(),
            weight = db.weight().all().firstOrNull() ?: emptyList(),
            health = db.health().all().firstOrNull() ?: emptyList(),
            breed = db.breed().all().firstOrNull() ?: emptyList(),
            tasks = db.task().all().firstOrNull() ?: emptyList(),
        )
        return try{
            api.sync(payload)
            Result.success()
        }catch(e: Exception){
            Result.retry()
        }
    }

    companion object{
        fun schedule(ctx: Context){
            val req = PeriodicWorkRequestBuilder<SyncWorker>(12, TimeUnit.HOURS)
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .build()
            WorkManager.getInstance(ctx).enqueueUniquePeriodicWork("sync", ExistingPeriodicWorkPolicy.UPDATE, req)
        }
    }
}
