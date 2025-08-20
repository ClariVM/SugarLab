package com.example.challengesugarlab.Ejercicios

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.challengesugarlab.ui.components.BarraSuperior
import com.example.challengesugarlab.ui.theme.AppColors
import com.example.challengesugarlab.ui.theme.ChallengeSugarLabTheme

class PPTActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengeSugarLabTheme {
                PiedraPapelTijeraScreen()
            }
        }
    }
}

/* ----------------- LÓGICA ------------------ */

// Calcula el ganador de una ronda (R, P, S)
fun calcularGanador(j1: String, j2: String): String {
    val a = j1.uppercase()
    val b = j2.uppercase()
    return when {
        a == b -> "Empate"
        (a == "R" && b == "S") ||
                (a == "S" && b == "P") ||
                (a == "P" && b == "R") -> "Jugador 1"
        else -> "Jugador 2"
    }
}


/* --------------- UI / COMPOSABLE --------------- */

@Composable
fun PiedraPapelTijeraScreen() {
    var eleccionP1 by remember { mutableStateOf<String?>(null) }
    var eleccionP2 by remember { mutableStateOf<String?>(null) }
    var resultadoRonda by remember { mutableStateOf("") }
    var rondas by remember { mutableStateOf(listOf<Pair<String, String>>()) }

    var victoriasP1 by remember { mutableStateOf(0) }
    var victoriasP2 by remember { mutableStateOf(0) }
    var empates by remember { mutableStateOf(0) }

    // Opciones con emoji
    val opciones = listOf(
        "R" to "🪨",
        "P" to "📄",
        "S" to "✂️"
    )

    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Piedra, Papel, Tijera",
                backgroundColor = AppColors.Rosa
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Piedra, Papel o Tijera", fontSize = 22.sp, color = MaterialTheme.colorScheme.onBackground)

            Spacer(modifier = Modifier.height(24.dp))

            // Jugador 1
            Text("Jugador 1", fontSize = 18.sp)
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                opciones.forEach { (valor, emoji) ->
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                color = if (eleccionP1 == valor) Color(0xFFD0F0C0) else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { eleccionP1 = valor },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = emoji,
                            fontSize = 36.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Jugador 2
            Text("Jugador 2", fontSize = 18.sp)
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                opciones.forEach { (valor, emoji) ->
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                color = if (eleccionP2 == valor) Color(0xFFD0E0FF) else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { eleccionP2 = valor },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = emoji,
                            fontSize = 36.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para jugar una ronda
            Button(
                onClick = {
                    if (eleccionP1 != null && eleccionP2 != null) {
                        val pareja = eleccionP1!! to eleccionP2!!
                        rondas = rondas + pareja
                        val resultado = calcularGanador(eleccionP1!!, eleccionP2!!)
                        resultadoRonda = resultado
                        when (resultado) {
                            "Jugador 1" -> victoriasP1++
                            "Jugador 2" -> victoriasP2++
                            "Empate" -> empates++
                        }
                        // limpiar selecciones para la próxima ronda
                        eleccionP1 = null
                        eleccionP2 = null
                    } else {
                        resultadoRonda = "Ambos jugadores deben elegir."
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF8788F)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Jugar Ronda")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Resultado de la última ronda
            if (resultadoRonda.isNotEmpty()) {
                Text("Resultado: $resultadoRonda", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Marcador detallado
            if (rondas.isNotEmpty()) {
                Text("Marcador:", fontSize = 20.sp)
                Text("Jugador 1: $victoriasP1 victorias")
                Text("Jugador 2: $victoriasP2 victorias")
                Text("Empates: $empates")
                Text("Total rondas: ${rondas.size}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón reiniciar
            Button(
                onClick = {
                    rondas = emptyList()
                    resultadoRonda = ""
                    eleccionP1 = null
                    eleccionP2 = null
                    victoriasP1 = 0
                    victoriasP2 = 0
                    empates = 0
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reiniciar")
            }
        }
    }
}
