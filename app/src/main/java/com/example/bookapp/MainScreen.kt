package com.example.bookapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    onLogout: () -> Unit,
    onGoToSearch: () -> Unit,
    onGoToSavedBooks: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "¡Bienvenido a tu Biblioteca!", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onGoToSearch) {
            Text("Buscar Libros 📚")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onGoToSavedBooks) {
            Text("Mis libros")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onLogout) {
            Text("Cerrar sesión")
        }
    }
}


