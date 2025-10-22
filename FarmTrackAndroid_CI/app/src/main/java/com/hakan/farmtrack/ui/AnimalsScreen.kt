package com.hakan.farmtrack.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.hakan.farmtrack.data.*
import com.hakan.farmtrack.util.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@Composable
fun AnimalsScreen(){
    val ctx = LocalContext.current
    val db = remember { AppDatabase.get(ctx) }
    val all: LiveData<List<Animal>> = db.animal().all().asLiveData()
    val list by all.observeAsState(emptyList())
    var q by remember { mutableStateOf(TextFieldValue("")) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Hayvan Listesi", style=MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = q, onValueChange = { q = it },
            label = { Text("Ara: ID, ad, ırk…") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        LazyColumn{
            items(list.filter { a ->
                val t = q.text.lowercase()
                t.isBlank() || a.id.lowercase().contains(t) || (a.name?:"").lowercase().contains(t) || (a.breed?:"").lowercase().contains(t)
            }){ a ->
                AnimalRow(a, onSave = { na ->
                    val scope = rememberCoroutineScope()
                })
                AnimalCard(a, onSave = { na ->
                    val scope = rememberCoroutineScope()
                })
            }
        }
        Spacer(Modifier.height(12.dp))
        Divider()
        Spacer(Modifier.height(12.dp))
        Text("Yeni / Düzenle", style=MaterialTheme.typography.titleMedium)
        AnimalForm(onSave = { a ->
            val scope = rememberCoroutineScope()
            scope.launch { db.animal().upsert(a) }
        })
    }
}

@Composable
fun AnimalCard(a: Animal, onSave:(Animal)->Unit){
    ElevatedCard(Modifier.fillMaxWidth().padding(vertical=6.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("${a.id} — ${a.name?:""}", style=MaterialTheme.typography.titleMedium)
            Text("${a.species} • ${a.sex} • ${a.breed?:""}", style=MaterialTheme.typography.bodyMedium)
            Text("Doğum: ${a.birth?:"—"} | Anne: ${a.dam?:"—"} | Baba: ${a.sire?:"—"}", style=MaterialTheme.typography.bodySmall)
            Text("Tohumlama: ${a.breeding?:"—"}", style=MaterialTheme.typography.bodySmall)
            if(!a.notes.isNullOrBlank()) Text(a.notes!!, style=MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun AnimalForm(onSave:(Animal)->Unit){
    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var species by remember { mutableStateOf("sığır") }
    var sex by remember { mutableStateOf("dişi") }
    var breed by remember { mutableStateOf("") }
    var birth by remember { mutableStateOf("") }
    var dam by remember { mutableStateOf("") }
    var sire by remember { mutableStateOf("") }
    var breeding by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(value=id,onValueChange={id=it},label={Text("ID (kulak/RFID)")},modifier=Modifier.fillMaxWidth())
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)){
            OutlinedTextField(value=name,onValueChange={name=it},label={Text("Ad")},modifier=Modifier.weight(1f))
            OutlinedTextField(value=species,onValueChange={species=it},label={Text("Tür")},modifier=Modifier.weight(1f))
            OutlinedTextField(value=sex,onValueChange={sex=it},label={Text("Cinsiyet")},modifier=Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)){
            OutlinedTextField(value=breed,onValueChange={breed=it},label={Text("Irk")},modifier=Modifier.weight(1f))
            OutlinedTextField(value=birth,onValueChange={birth=it},label={Text("Doğum (YYYY-MM-DD)")},modifier=Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)){
            OutlinedTextField(value=dam,onValueChange={dam=it},label={Text("Anne ID")},modifier=Modifier.weight(1f))
            OutlinedTextField(value=sire,onValueChange={sire=it},label={Text("Baba ID")},modifier=Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)){
            OutlinedTextField(value=breeding,onValueChange={breeding=it},label={Text("Son tohumlama (YYYY-MM-DD)")},modifier=Modifier.weight(1f))
        }
        OutlinedTextField(value=notes,onValueChange={notes=it},label={Text("Not")},modifier=Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            if(id.isNotBlank()){
                onSave(Animal(id=id, name=name.ifBlank{null}, species=species, sex=sex, breed=breed.ifBlank{null},
                    birth=birth.ifBlank{null}, dam=dam.ifBlank{null}, sire=sire.ifBlank{null}, breeding=breeding.ifBlank{null}, notes=notes.ifBlank{null}
                ))
            }
        }){ Text("Kaydet") }
    }
}
