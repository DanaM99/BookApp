package com.example.bookapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.example.bookapp.ui.theme.BookAppTheme
// Importaciones de Firebase correctas
import com.google.firebase.FirebaseApp
// Nota: FirebaseAuth ya no es necesaria aquí, ya que el logout
// se maneja indirectamente a través de la función onLogout en NavGraph/MainScreen.
// Si no la usas, puedes eliminarla: import com.google.firebase.auth.FirebaseAuth

// Importa la función NavGraph que contiene toda la lógica de navegación
import com.example.bookapp.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializar Firebase, debe ir antes de usar cualquier otra función de Firebase
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()

        setContent {
            BookAppTheme {
                Surface {
                    // LLAMADA AL GRAFO DE NAVEGACIÓN CENTRALIZADO
                    NavGraph()
                }
            }
        }
    }
}


