package com.example.smartgardendemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.example.smartgardendemo.core.theme.AppTheme
import com.example.smartgardendemo.ui.screens.HistorialScreen
import com.example.smartgardendemo.ui.screens.HomeScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ── Inicializar Firebase ──────────────────────────────────────────
        FirebaseApp.initializeApp(this)

        // ── Habilitar diseño de borde a borde ─────────────────────────────
        enableEdgeToEdge()

        setContent {
            AppTheme(darkTheme = isSystemInDarkTheme()) {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(
                            onNavigateToHistorial = {
                                navController.navigate("historial")
                            }
                        )
                    }
                    composable("historial") {
                        HistorialScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

// ── Preview para Android Studio ───────────────────────────────────────────────
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AppTheme {
        HomeScreen()
    }
}