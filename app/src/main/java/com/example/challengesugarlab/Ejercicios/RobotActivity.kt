package com.example.challengesugarlab.Ejercicios

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.challengesugarlab.ui.components.BarraSuperior
import com.example.challengesugarlab.ui.theme.AppColors
import com.example.challengesugarlab.ui.theme.ChallengeSugarLabTheme
import kotlinx.coroutines.delay


class RobotActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengeSugarLabTheme {
                RobotMinesweeperGame()
            }
        }
    }
}
// ---------- Composable principal ----------
@Composable
fun RobotMinesweeperGame() {
    val gridSize = 7                    // Tamaño del tablero (7x7)
    val cellSize = 50.dp                // Tamaño de cada celda
    val startPos = 0 to 0               // Posición inicial del robot (esquina inferior izquierda)

    // 🔹 Función para obtener las celdas vecinas (8 direcciones)
    fun neighbors(cell: Pair<Int, Int>): List<Pair<Int, Int>> {
        val (r, c) = cell
        return listOf(
            r - 1 to c, r + 1 to c, r to c - 1, r to c + 1,
            r - 1 to c - 1, r - 1 to c + 1, r + 1 to c - 1, r + 1 to c + 1
        ).filter { it.first in 0 until gridSize && it.second in 0 until gridSize }
    }

    // ---------- ESTADO DEL JUEGO ----------
    var robotPos by remember { mutableStateOf(startPos) }        // Posición actual del robot
    var starPos by remember { mutableStateOf(generateStar(startPos, gridSize)) }        // Posición de la estrella
    var obstacles by remember { mutableStateOf(generateMines(startPos, starPos, gridSize, 5)) } // Minas
    var deltaX by remember { mutableStateOf("") }           // Input de movimiento horizontal
    var deltaY by remember { mutableStateOf("") }           // Input de movimiento vertical
    var message by remember { mutableStateOf("Estás en el inicio 🚶") }      // Mensajes al jugador
    var moveRequest by remember { mutableStateOf<Pair<Int, Int>?>(null) }    // Solicitud de movimiento


    // ---------- Interfaz del juego ----------
    Scaffold (
        topBar = {
            BarraSuperior(
                titulo = "¿Dónde está el robot?",
                backgroundColor = AppColors.Vino
            )
        }
    ){padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(vertical = 56.dp)
        ) {
            Text("🤖 Busca la ⭐ evitando minas ocultas", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(16.dp))

            // 🔹 Instrucciones y campos de entrada
            Column {
                Text("*Mueve al robot \uD83E\uDD16: X (horizontal), Y (vertical).\n" +
                        "*Solo se muestran las minas cercanas al robot; planifica tus movimientos.\n" +
                        "*Evita las minas \uD83D\uDCA3 y llega a la estrella ⭐ para ganar \uD83C\uDF89!")
                // Campo para movimiento en X
                OutlinedTextField(
                    value = deltaX,
                    onValueChange = { deltaX = it },
                    label = { Text("Mover en X ⬅\uFE0F➡️") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )

                Spacer(Modifier.width(8.dp))

                // Campo para movimiento en Y
                OutlinedTextField(
                    value = deltaY,
                    onValueChange = { deltaY = it },
                    label = { Text("Mover en Y ⬆️ ⬇️") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            // 🔹 Botón para mover el robot
            Button(onClick = {
                val dx = deltaX.toIntOrNull() ?: 0      // Convertir input X a número
                val dy = deltaY.toIntOrNull() ?: 0      // Convertir input Y a número
                deltaX = ""
                deltaY = ""
                moveRequest = dx to dy                  // Guardar solicitud de movimiento
            }) {
                Text("Mover Robot")
            }

            Spacer(Modifier.height(16.dp))

            // ---------- Tablero ----------
            val visibleMines = obstacles.filter { it in neighbors(robotPos) }       // Minas visibles alrededor del robot

            // Dibujar filas de arriba hacia abajo
            Column {
                for (row in gridSize - 1 downTo 0) {
                    Row {
                        for (col in 0 until gridSize) {
                            val cell = row to col
                            val color = when {
                                cell == robotPos -> Color.Green
                                cell == starPos -> Color.Yellow
                                visibleMines.contains(cell) -> Color.Red
                                else -> Color.LightGray
                            }

                            // Cada celda del tablero
                            Box(
                                modifier = Modifier
                                    .size(cellSize)
                                    .border(1.dp, Color.Black)
                                    .background(color),
                                contentAlignment = Alignment.Center
                            ) {
                                // Mostrar íconos según la celda
                                when (cell) {
                                    robotPos -> Text("🤖", fontSize = 20.sp)
                                    starPos -> Text("⭐", fontSize = 20.sp)
                                    in visibleMines -> Text("💣", fontSize = 20.sp)
                                }
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))

            // 🔹 Botón Reiniciar
            Button(onClick = {
                robotPos = startPos
                starPos = generateStar(startPos, gridSize)
                obstacles = generateMines(startPos, starPos, gridSize, 5)
                message = "Nuevo juego 🚀"
                deltaX = ""
                deltaY = ""
                moveRequest = null
            }) {
                Text("🔄 Reiniciar juego")
            }

            Spacer(Modifier.height(16.dp))
            if (message.isNotEmpty()) {
                Text(message, color = Color.Blue)
            }
        }
    }

    // ---------- Movimiento del robot----------
    moveRequest?.let { (dx, dy) ->
        LaunchedEffect(dx, dy) {
            var row = robotPos.first
            var col = robotPos.second

            // Mover en X paso a paso
            val stepX = if (dx >= 0) 1 else -1
            repeat(kotlin.math.abs(dx)) {
                col += stepX
                // Verificar límites
                if (col !in 0 until gridSize) {
                    message = "⚠️ Te saliste del tablero. Reiniciando..."
                    delay(1000)
                    robotPos = startPos
                    starPos = generateStar(startPos, gridSize)
                    obstacles = generateMines(startPos, starPos, gridSize, 5)
                    message = "Nuevo juego 🚀"
                    moveRequest = null
                    return@LaunchedEffect
                }
                robotPos = row to col
                // Verificar si pisa mina
                if (obstacles.contains(robotPos)) {
                    message = "💀 Pisaste una mina. Reiniciando..."
                    delay(1000)
                    robotPos = startPos
                    starPos = generateStar(startPos, gridSize)
                    obstacles = generateMines(startPos, starPos, gridSize, 5)
                    message = "Nuevo juego 🚀"
                    moveRequest = null
                    return@LaunchedEffect
                }
                delay(300)
            }

            // Mover en Y paso a paso
            val stepY = if (dy >= 0) 1 else -1
            repeat(kotlin.math.abs(dy)) {
                row += stepY
                if (row !in 0 until gridSize) {
                    message = "⚠️ Te saliste del tablero. Reiniciando..."
                    delay(1000)
                    robotPos = startPos
                    starPos = generateStar(startPos, gridSize)
                    obstacles = generateMines(startPos, starPos, gridSize, 5)
                    message = "Nuevo juego 🚀"
                    moveRequest = null
                    return@LaunchedEffect
                }
                robotPos = row to col
                if (obstacles.contains(robotPos)) {
                    message = "💀 Pisaste una mina. Reiniciando..."
                    delay(1000)
                    robotPos = startPos
                    starPos = generateStar(startPos, gridSize)
                    obstacles = generateMines(startPos, starPos, gridSize, 5)
                    message = "Nuevo juego 🚀"
                    moveRequest = null
                    return@LaunchedEffect
                }
                delay(300)
            }

            // Verificar si ganó
            if (robotPos == starPos) {
                message = "🎉 ¡Ganaste! El robot llegó a la estrella ⭐"
            } else {
                // Contar minas cercanas
                val nearbyMines = obstacles.count { it in neighbors(robotPos) }
                message = if (nearbyMines > 0) {
                    "⚠️ Cuidado: hay $nearbyMines minas cerca"
                } else {
                    "✅ Celda segura, no hay minas cercanas"
                }
            }

            moveRequest = null
        }
    }
}

// ---------- Funciones auxiliares ----------

// Genera posición aleatoria para la estrella evitando la posición inicial
fun generateStar(startPos: Pair<Int, Int>, gridSize: Int): Pair<Int, Int> {
    var pos: Pair<Int, Int>
    do {
        pos = (0 until gridSize).random() to (0 until gridSize).random()
    } while (pos == startPos)
    return pos
}

// Genera una lista de minas evitando la posición inicial y la estrella
fun generateMines(startPos: Pair<Int, Int>, starPos: Pair<Int, Int>, gridSize: Int, count: Int): List<Pair<Int, Int>> {
    val forbidden = mutableSetOf<Pair<Int, Int>>()
    forbidden.add(startPos)
    forbidden.add(starPos)

    // no poner minas al lado de la estrella
    val (r, c) = starPos
    listOf(
        r - 1 to c, r + 1 to c, r to c - 1, r to c + 1,
        r - 1 to c - 1, r - 1 to c + 1, r + 1 to c - 1, r + 1 to c + 1
    ).forEach {
        if (it.first in 0 until gridSize && it.second in 0 until gridSize) {
            forbidden.add(it)
        }
    }

    val mines = mutableSetOf<Pair<Int, Int>>()
    while (mines.size < count) {
        val pos = (0 until gridSize).random() to (0 until gridSize).random()
        if (pos !in forbidden) {
            mines.add(pos)
        }
    }
    return mines.toList()
}
