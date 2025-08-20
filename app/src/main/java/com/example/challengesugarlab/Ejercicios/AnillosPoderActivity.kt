package com.example.challengesugarlab.Ejercicios

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.challengesugarlab.R
import com.example.challengesugarlab.ui.components.BarraSuperior
import com.example.challengesugarlab.ui.theme.AppColors
import com.example.challengesugarlab.ui.theme.ChallengeSugarLabTheme

class AnillosPoderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengeSugarLabTheme {
                AnillosPoderScreen()
            }
        }
    }
}
private fun calcularPoder(ejercito: Map<String, Int>, lista: List<Pair<String, Pair<String, Int>>>): Int {
    var poder = 0
    for ((nombre, cantidad) in ejercito) {
        val valor = lista.find { it.first == nombre }?.second?.second ?: 0
        poder += valor * cantidad
    }
    return poder
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnillosPoderScreen() {
    // Datos de razas y su poder
    val razasBondadosas = listOf(
        "pelosos" to Pair("🧙‍♂️", 1),
        "sureños buenos" to Pair("🛡️", 2),
        "enanos" to Pair("⛏️", 3),
        "númenóreanos" to Pair("⚓", 4),
        "elfos" to Pair("🏹", 5)
    )

    val razasMalvadas = listOf(
        "sureños malos" to Pair("⚔️", 2),
        "orcos" to Pair("👹", 2),
        "goblins" to Pair("👺", 2),
        "huargos" to Pair("🐺", 3),
        "trolls" to Pair("🪨", 5)
    )

    // Contadores
    var ejercitoBien by remember { mutableStateOf(razasBondadosas.associate { it.first to 0 }) }
    var ejercitoMal by remember { mutableStateOf(razasMalvadas.associate { it.first to 0 }) }

    var resultado by remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                BarraSuperior(
                    titulo = "Los anillos del poder",
                    backgroundColor = AppColors.AzulOscuro
                )
            }
        ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Ejército del Bien", style = MaterialTheme.typography.titleMedium, color = Color(0xFF2E7D32))
            razasBondadosas.forEach { (nombre, datos) ->
                ContadorRaza(
                    nombre = nombre,
                    emoji = datos.first,
                    cantidad = ejercitoBien[nombre] ?: 0,
                    onCantidadChange = { nuevaCantidad ->
                        ejercitoBien = ejercitoBien.toMutableMap().apply { put(nombre, nuevaCantidad) }
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            Text("Ejército del Mal", style = MaterialTheme.typography.titleMedium, color = Color.Red)
            razasMalvadas.forEach { (nombre, datos) ->
                ContadorRaza(
                    nombre = nombre,
                    emoji = datos.first,
                    cantidad = ejercitoMal[nombre] ?: 0,
                    onCantidadChange = { nuevaCantidad ->
                        ejercitoMal = ejercitoMal.toMutableMap().apply { put(nombre, nuevaCantidad) }
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val poderBien = calcularPoder(ejercitoBien, razasBondadosas)
                    val poderMal = calcularPoder(ejercitoMal, razasMalvadas)

                    resultado = when {
                        poderBien > poderMal -> "🏆 ¡Gana el Bien! ($poderBien vs $poderMal)"
                        poderMal > poderBien -> "💀 ¡Gana el Mal! ($poderMal vs $poderBien)"
                        else -> "🤝 Empate ($poderBien vs $poderMal)"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calcular Resultado")
            }

            if (resultado.isNotEmpty()) {
                Text(resultado, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

@Composable
fun ContadorRaza(nombre: String, emoji: String, cantidad: Int, onCantidadChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$emoji $nombre", style = MaterialTheme.typography.bodyLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { if (cantidad > 0) onCantidadChange(cantidad - 1) }) {
                Icon(Icons.Default.Delete, contentDescription = "Restar")
            }
            Text("$cantidad", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
            IconButton(onClick = { onCantidadChange(cantidad + 1) }) {
                Icon(Icons.Default.Add, contentDescription = "Sumar")
            }
        }
    }
}

