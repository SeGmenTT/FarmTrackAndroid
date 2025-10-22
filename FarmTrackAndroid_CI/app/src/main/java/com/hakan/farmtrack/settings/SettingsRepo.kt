package com.hakan.farmtrack.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

object Keys{
    val SERVER = stringPreferencesKey("server")
    val AUTOSYNC = booleanPreferencesKey("autosync")
}

class SettingsRepo(private val ctx: Context){
    val server = ctx.dataStore.data.map { it[Keys.SERVER] ?: "" }
    val autosync = ctx.dataStore.data.map { it[Keys.AUTOSYNC] ?: true }

    suspend fun setServer(v:String){ ctx.dataStore.edit { it[Keys.SERVER] = v } }
    suspend fun setAutosync(v:Boolean){ ctx.dataStore.edit { it[Keys.AUTOSYNC] = v } }
}
