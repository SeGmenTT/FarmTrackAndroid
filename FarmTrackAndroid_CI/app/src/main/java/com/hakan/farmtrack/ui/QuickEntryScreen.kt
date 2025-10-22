package com.hakan.farmtrack.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.hakan.farmtrack.data.*
import com.hakan.farmtrack.util.today
import kotlinx.coroutines.launch

@Composable
fun QuickEntryScreen(){
    val ctx = LocalContext.current
    val db = remember { AppDatabase.get(ctx) }
    var tab by remember { mutableStateOf(0) }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Hızlı Kayıt", style=MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        TabRow(selectedTabIndex = tab){
            listOf("Süt","Ağırlık","Sağlık","Üreme","Görev").forEachIndexed { i, t ->
                Tab(selected = tab==i, onClick = { tab=i }, text = { Text(t) })
            }
        }
        Spacer(Modifier.height(12.dp))
        when(tab){
            0 -> MilkForm(db)
            1 -> WeightForm(db)
            2 -> HealthForm(db)
            3 -> BreedForm(db)
            4 -> TaskForm(db)
        }
    }
}

@Composable
fun MilkForm(db: AppDatabase){
    var id by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(today()) }
    var liters by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    Column {
        OutlinedTextField(value=id, onValueChange={id=it}, label={Text("Hayvan ID")}, modifier=Modifier.fillMaxWidth())
        OutlinedTextField(value=date, onValueChange={date=it}, label={Text("Tarih (YYYY-MM-DD)")}, modifier=Modifier.fillMaxWidth())
        OutlinedTextField(value=liters, onValueChange={liters=it}, label={Text("Günlük Süt (L)")}, modifier=Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Button(onClick={
            liters.toDoubleOrNull()?.let { l ->
                scope.launch { db.milk().insert(Milk(id=id, date=date, liters=l)) }
            }
        }){ Text("Kaydet") }
    }
}

@Composable
fun WeightForm(db: AppDatabase){
    var id by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(today()) }
    var kg by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    Column {
        OutlinedTextField(value=id, onValueChange={id=it}, label={Text("Hayvan ID")}, modifier=Modifier.fillMaxWidth())
        OutlinedTextField(value=date, onValueChange={date=it}, label={Text("Tarih (YYYY-MM-DD)")}, modifier=Modifier.fillMaxWidth())
        OutlinedTextField(value=kg, onValueChange={kg=it}, label={Text("Kilo (kg)")}, modifier=Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Button(onClick={ kg.toDoubleOrNull()?.let { v ->
            val scope = androidx.compose.runtime.rememberCoroutineScope()
        } }){ Text("Kaydet") }
        Button(onClick={
            kg.toDoubleOrNull()?.let { v ->
                val scope2 = rememberCoroutineScope()
            }
        }){}
        Button(onClick={
            kg.toDoubleOrNull()?.let { v ->
                scope.launch { db.weight().insert(Weight(id=id, date=date, kg=v)) }
            }
        }){ Text("Kaydet") }
    }
}

@Composable
fun HealthForm(db: AppDatabase){
    var id by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(today()) }
    var action by remember { mutableStateOf("") }
    var drug by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    Column {
        OutlinedTextField(value=id,onValueChange={id=it},label={Text("Hayvan ID")},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(value=date,onValueChange={date=it},label={Text("Tarih (YYYY-MM-DD)")},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(value=action,onValueChange={action=it},label={Text("İşlem")},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(value=drug,onValueChange={drug=it},label={Text("İlaç/Aşı")},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(value=notes,onValueChange={notes=it},label={Text("Not")},modifier=Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Button(onClick={
            if(id.isNotBlank() && action.isNotBlank()){
                scope.launch { db.health().insert(Health(id=id,date=date,action=action,drug=drug.ifBlank{null},notes=notes.ifBlank{null})) }
            }
        }){ Text("Kaydet") }
    }
}

@Composable
fun BreedForm(db: AppDatabase){
    var id by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(today()) }
    val scope = rememberCoroutineScope()
    Column {
        OutlinedTextField(value=id,onValueChange={id=it},label={Text("Dişi Hayvan ID")},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(value=date,onValueChange={date=it},label={Text("Tohumlama (YYYY-MM-DD)")},modifier=Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Button(onClick={
            if(id.isNotBlank()){
                val due = com.hakan.farmtrack.util.addDays(date, 283)
                scope.launch { db.breed().insert(Breed(id=id,date=date,due=due)) }
            }
        }){ Text("Kaydet") }
    }
}

@Composable
fun TaskForm(db: AppDatabase){
    var id by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var due by remember { mutableStateOf(today()) }
    var repeat by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    Column {
        OutlinedTextField(value=id,onValueChange={id=it},label={Text("Hayvan ID (opsiyonel)")},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(value=title,onValueChange={title=it},label={Text("Görev Adı")},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(value=due,onValueChange={due=it},label={Text("Son Tarih (YYYY-MM-DD)")},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(value=repeat,onValueChange={repeat=it},label={Text("Tekrar (örn P30D)")},modifier=Modifier.fillMaxWidth())
        OutlinedTextField(value=note,onValueChange={note=it},label={Text("Not")},modifier=Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Button(onClick={
            if(title.isNotBlank()){
                scope.launch { db.task().upsert(Task(id=id.ifBlank{null}, title=title, due=due, repeat=repeat.ifBlank{null}, note=note.ifBlank{null})) }
            }
        }){ Text("Kaydet") }
    }
}
