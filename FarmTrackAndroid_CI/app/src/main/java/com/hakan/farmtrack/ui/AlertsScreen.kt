package com.hakan.farmtrack.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.asLiveData
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.hakan.farmtrack.data.*
import com.hakan.farmtrack.util.*

@Composable
fun AlertsScreen(){
    val ctx = LocalContext.current
    val db = remember { AppDatabase.get(ctx) }
    val tasks by db.task().all().asLiveData().observeAsState(emptyList())
    val breed by db.breed().all().asLiveData().observeAsState(emptyList())
    val animals by db.animal().all().asLiveData().observeAsState(emptyList())
    val alerts = remember(tasks, breed, animals){ buildAlerts(tasks, breed, animals) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Yaklaşanlar & Uyarılar", style=MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        if(alerts.isEmpty()){
            Text("Önümüzdeki 30 gün için bekleyen uyarı yok.", style=MaterialTheme.typography.bodyMedium)
        }else{
            LazyColumn{
                items(alerts){ a ->
                    ElevatedCard(Modifier.fillMaxWidth().padding(vertical=6.dp)) {
                        Column(Modifier.padding(12.dp)) {
                            val badge = when(a.kind){ "danger"->"⛔"; "warn"->"⚠️"; else->"✅" }
                            Text("$badge ${a.title}", style=MaterialTheme.typography.titleMedium)
                            Text(a.date, style=MaterialTheme.typography.bodySmall)
                            a.extra?.let { Text(it, style=MaterialTheme.typography.bodySmall) }
                        }
                    }
                }
            }
        }
    }
}
