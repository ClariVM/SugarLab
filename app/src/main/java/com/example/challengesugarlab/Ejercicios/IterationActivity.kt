package com.example.challengesugarlab.Ejercicios

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.challengesugarlab.ui.components.BarraSuperior
import com.example.challengesugarlab.ui.theme.ChallengeSugarLabTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class IterationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengeSugarLabTheme {
                IterationMaster()
            }
        }
    }
}
// Métodos de iteración
suspend fun runForLoop(print: suspend (Int) -> Unit) {
    for (i in 1.. 30) {
        print(i)
        delay(500)
    }
}

suspend fun runWhileLoop(print: suspend (Int) -> Unit) {
    var i = 1
    while (i <= 30) {
        print(i)
        delay(500)
        i++
    }
}

suspend fun runForEachLoop(print: suspend (Int) -> Unit) {
    (1..30).forEach {
        print(it)
        delay(500)
    }
}

suspend fun runRecursion(n: Int, print: suspend (Int) -> Unit) {
    if (n > 30) return
    print(n)
    delay(500)
    runRecursion(n + 1, print)
}

@Preview
@Composable
fun IterationMaster() {
    val scope = rememberCoroutineScope()

    // Outputs en vivo como MutableState para que se actualicen en tiempo real
    val outputStates = remember {
        listOf(
            mutableStateOf(""), // for
            mutableStateOf(""), // while
            mutableStateOf(""), // forEach
            mutableStateOf("")  // recursion
        )
    }

    // Tiempos
    var timeFor by remember { mutableStateOf(0L) }
    var timeWhile by remember { mutableStateOf(0L) }
    var timeForEach by remember { mutableStateOf(0L) }
    var timeRecursion by remember { mutableStateOf(0L) }

    // Estado del diálogo final
    var showDialog by remember { mutableStateOf(false) }

    // Estado para saber si todos terminaron
    var finishedCount by remember { mutableStateOf(0) }

    val itemsList = listOf(
        "for" to outputStates[0],
        "while" to outputStates[1],
        "forEach" to outputStates[2],
        "recursion" to outputStates[3]
    )

    Scaffold(topBar = {
        BarraSuperior(
            titulo = "Iterador",
            backgroundColor = Color.Green
        )
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "Iteration Master",
                style = TextStyle(
                    color = Color.Green,
                    fontSize = 28.sp,
                    fontFamily = FontFamily.Monospace
                )
            )

            Spacer(Modifier.height(16.dp))

            // Botón para iniciar todos
            Button(
                onClick = {
                    // Reset
                    outputStates.forEach { it.value = "" }
                    finishedCount = 0

                    // Ejecutar en paralelo
                    scope.launch {
                        timeFor = measureTimeMillis {
                            runForLoop { outputStates[0].value += "$it\n" }
                        }
                        finishedCount++
                        if (finishedCount == 4) showDialog = true
                    }

                    scope.launch {
                        timeWhile = measureTimeMillis {
                            runWhileLoop { outputStates[1].value += "$it\n" }
                        }
                        finishedCount++
                        if (finishedCount == 4) showDialog = true
                    }

                    scope.launch {
                        timeForEach = measureTimeMillis {
                            runForEachLoop { outputStates[2].value += "$it\n" }
                        }
                        finishedCount++
                        if (finishedCount == 4) showDialog = true
                    }

                    scope.launch {
                        timeRecursion = measureTimeMillis {
                            runRecursion(1) { outputStates[3].value += "$it\n" }
                        }
                        finishedCount++
                        if (finishedCount == 4) showDialog = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
            ) {
                Text("Iniciar Comparación", color = Color.Black)
            }

            Spacer(Modifier.height(16.dp))

            // LazyVerticalGrid con 4 columnas
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(itemsList) { (nombre, outputState) ->
                    MetodoColumn(nombre, outputState.value, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }

    // Diálogo final con tiempos
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Resultados de rendimiento") },
            text = {
                Column {
                    Text("for: $timeFor ms")
                    Text("while: $timeWhile ms")
                    Text("forEach: $timeForEach ms")
                    Text("recursion: $timeRecursion ms")
                }
            },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
fun MetodoColumn(nombre: String, output: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(4.dp)
            .height(630.dp)
            .background(Color.DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            nombre,
            color = Color.Green,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(4.dp),
            style = MaterialTheme.typography.labelSmall
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Black)
                .padding(4.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = output,
                style = TextStyle(
                    color = Color.Green,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace
                )
            )
        }
    }
}


