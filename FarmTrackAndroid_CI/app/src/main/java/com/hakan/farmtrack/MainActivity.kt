package com.hakan.farmtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.Settings
import androidx.compose.material3.icons.filled.ListAlt
import androidx.compose.material3.icons.filled.AddCircle
import androidx.compose.material3.icons.filled.Notifications
import androidx.navigation.compose.*
import com.hakan.farmtrack.ui.*
import com.hakan.farmtrack.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                App()
            }
        }
    }
}

enum class Dest(val route:String, val label:String, val icon: ImageVector){
    Animals("animals","Hayvanlar", Icons.Filled.ListAlt),
    Quick("quick","Hızlı Kayıt", Icons.Filled.AddCircle),
    Alerts("alerts","Uyarılar", Icons.Filled.Notifications),
    Settings("settings","Ayarlar", Icons.Filled.Settings)
}

@Composable
fun App(){
    val nav = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar {
                Dest.values().forEach { d ->
                    val sel = nav.currentBackStackEntryAsState().value?.destination?.route==d.route
                    NavigationBarItem(
                        selected = sel,
                        onClick = { nav.navigate(d.route){ launchSingleTop = true } },
                        icon = { Icon(d.icon, d.label) },
                        label = { Text(d.label) }
                    )
                }
            }
        }
    ){ pad ->
        NavHost(nav, startDestination = Dest.Animals.route, modifier = androidx.compose.ui.Modifier.padding(pad)){
            composable(Dest.Animals.route){ AnimalsScreen() }
            composable(Dest.Quick.route){ QuickEntryScreen() }
            composable(Dest.Alerts.route){ AlertsScreen() }
            composable(Dest.Settings.route){ SettingsScreen() }
        }
    }
}
