package com.hakan.farmtrack.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

val fmt: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

fun today(): String = LocalDate.now().format(fmt)
fun addDays(date:String, days:Int): String = LocalDate.parse(date, fmt).plusDays(days.toLong()).format(fmt)
fun daysBetween(a:String, b:String): Long = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.parse(a), LocalDate.parse(b))

val gestationDays = mapOf("sığır" to 283, "düve" to 283, "koyun" to 150, "keçi" to 150)

data class Alert(val title:String, val date:String, val kind:String, val extra:String?=null)

fun buildAlerts(tasks: List<com.hakan.farmtrack.data.Task>, breed: List<com.hakan.farmtrack.data.Breed>, animals: List<com.hakan.farmtrack.data.Animal>): List<Alert>{
    val out = mutableListOf<Alert>()
    val today = LocalDate.now()
    val soon = today.plusDays(30)

    // tasks
    tasks.filter { !it.done }.forEach { t ->
        val d = LocalDate.parse(t.due)
        if(!d.isAfter(soon)){
            val kind = if(d.isBefore(today)) "danger" else "warn"
            out += Alert("Görev: ${t.title}", t.due, kind, t.id?.let{"Hayvan: $it"} ?: "Genel görev")
        }
    }

    // births
    breed.forEach { b ->
        val d = LocalDate.parse(b.due)
        if(!d.isAfter(soon)){
            val kind = if(d.isBefore(today)) "danger" else "warn"
            out += Alert("Doğum beklenen: ${b.id}", b.due, kind, "Hazırlıkları kontrol et")
        }
    }

    // dry-off 60 days before due
    animals.forEach { a ->
        a.breeding?.let { br ->
            val due = LocalDate.parse(br).plusDays((gestationDays[a.species] ?: 283).toLong())
            val dry = due.minusDays(60)
            if(!dry.isAfter(soon) && !dry.isBefore(today)){
                out += Alert("Kuruya alma önerisi: ${a.id}", dry.format(fmt), "warn", "Rasyon ve sağlık planını kontrol et")
            }
        }
    }

    return out.sortedWith(compareBy({ it.date }, { it.title }))
}
