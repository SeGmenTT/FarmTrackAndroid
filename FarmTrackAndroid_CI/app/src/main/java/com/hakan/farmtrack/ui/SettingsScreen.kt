package com.hakan.farmtrack.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.hakan.farmtrack.settings.SettingsRepo
import com.hakan.farmtrack.data.AppDatabase
import com.hakan.farmtrack.net.ApiClient
import com.hakan.farmtrack.net.SyncPayload
import com.hakan.farmtrack.sync.SyncWorker
import androidx.lifecycle.asLiveData
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun SettingsScreen(){
    val ctx = LocalContext.current
    val repo = remember { SettingsRepo(ctx) }
    val server by repo.server.collectAsState(initial = "")
    val autosync by repo.autosync.collectAsState(initial = true)
    val scope = rememberCoroutineScope()
    val db = remember { AppDatabase.get(ctx) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Ayarlar", style=MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value=server, onValueChange={ v -> scope.launch{ repo.setServer(v) } },
            label={ Text("Sunucu Adresi (WhatsApp için)") }, modifier=Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically){
            Checkbox(checked = autosync, onCheckedChange={ v -> scope.launch{ repo.setAutosync(v) } })
            Text("Anında Sunucuya Gönder")
        }

        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick={
                SyncWorker.schedule(ctx)
            }){ Text("Arkaplan Senkronu Planla") }

            Button(onClick={
                scope.launch {
                    if(server.isBlank()) return@launch
                    val api = ApiClient.create(server)
                    val payload = SyncPayload(
                        animals = db.animal().all().asLiveData().value ?: emptyList(),
                        milk = db.milk().all().asLiveData().value ?: emptyList(),
                        weight = db.weight().all().asLiveData().value ?: emptyList(),
                        health = db.health().all().asLiveData().value ?: emptyList(),
                        breed = db.breed().all().asLiveData().value ?: emptyList(),
                        tasks = db.task().all().asLiveData().value ?: emptyList(),
                    )
                    api.sync(payload)
                }
            }){ Text("Sunucuya Senkronize Et") }
        }

        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick={
                scope.launch {
                    if(server.isBlank()) return@launch
                    val api = ApiClient.create(server)
                    api.test()
                }
            }){ Text("WhatsApp Test Mesajı") }

            OutlinedButton(onClick={
                scope.launch {
                    if(server.isBlank()) return@launch
                    val api = ApiClient.create(server)
                    api.notifyNow()
                }
            }){ Text("Şimdi Hatırlatma Gönder") }
        }
    }
}
