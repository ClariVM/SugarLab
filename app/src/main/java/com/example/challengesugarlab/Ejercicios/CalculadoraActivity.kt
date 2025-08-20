package com.example.challengesugarlab.Ejercicios

import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.challengesugarlab.R
import com.example.challengesugarlab.ui.components.BarraSuperior
import com.example.challengesugarlab.ui.theme.AppColors
import com.example.challengesugarlab.ui.theme.ChallengeSugarLabTheme

class CalculadoraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengeSugarLabTheme {
                MostrarCalculadora()
            }
        }
    }
}
/**
 * Función que calcula el resultado de una lista de valores representando
 * una operación matemática.
 *
 * Soporta:
 * - Paréntesis (resueltos recursivamente)
 * - Porcentaje (%)
 * - Operaciones con prioridad (* y / antes que + y -)
 * - Manejo de errores (retorna NaN si la entrada es inválida)
 */
fun calcular(valores: List<String>): Double {
    if (valores.isEmpty()) return 0.0

    // Creamos una copia mutable para poder modificar la lista durante el cálculo
    val valores = valores.toMutableList()

    // Paréntesis: Resolver paréntesis de adentro hacia afuera (recursivo)
    while ("(" in valores) {
        val openIndex = valores.lastIndexOf("(")
        val closeIndex = valores.subList(openIndex, valores.size).indexOf(")") + openIndex
        if (closeIndex <= openIndex) return Double.NaN
        val subExp = valores.subList(openIndex + 1, closeIndex).toList()
        val subResult = calcular(subExp)
        for (i in closeIndex downTo openIndex) valores.removeAt(i)
        valores.add(openIndex, subResult.toString())
    }

    // Porcentajes
    var i = 0
    while (i < valores.size) {
        if (valores[i] == "%") {
            val num = valores[i - 1].toDoubleOrNull() ?: return Double.NaN
            valores[i - 1] = (num / 100.0).toString()
            valores.removeAt(i)
            i--
        }
        i++
    }

    // Multiplicación y división
    i = 0
    while (i < valores.size) {
        if (valores[i] == "*" || valores[i] == "/") {
            val num1 = valores[i - 1].toDoubleOrNull() ?: return Double.NaN
            val num2 = valores[i + 1].toDoubleOrNull() ?: return Double.NaN
            val result = when (valores[i]) {
                "*" -> num1 * num2
                "/" -> if (num2 != 0.0) num1 / num2 else Double.NaN
                else -> 0.0
            }
            valores[i - 1] = result.toString()
            valores.removeAt(i)
            valores.removeAt(i)
            i--
        }
        i++
    }

    // Suma y resta
    i = 0
    while (i < valores.size) {
        if (valores[i] == "+" || valores[i] == "-") {
            val num1 = valores[i - 1].toDoubleOrNull() ?: return Double.NaN
            val num2 = valores[i + 1].toDoubleOrNull() ?: return Double.NaN
            val result = when (valores[i]) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                else -> 0.0
            }
            valores[i - 1] = result.toString()
            valores.removeAt(i)
            valores.removeAt(i)
            i--
        }
        i++
    }

    return valores.firstOrNull()?.toDoubleOrNull() ?: Double.NaN
}

@Composable
fun MostrarCalculadora() {
    // Estado que guarda lo que se muestra en pantalla
    var display by remember { mutableStateOf("0") }
    var valores by remember { mutableStateOf(listOf<String>()) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            context.contentResolver.openInputStream(it)?.use { stream ->
                val lineas = stream.bufferedReader().readLines()
                val resultado = calcular(lineas)
                display = if (resultado.isNaN()) "Error" else resultado.toString()
                valores = lineas
            }
        }
    }

    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Calculadora",
                backgroundColor = AppColors.VerdeLima
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f)
                    .padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Text(
                        text = display,
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val botones = listOf(
                    "C", "(", ")", "%",
                    "7", "8", "9", "/",
                    "4", "5", "6", "*",
                    "1", "2", "3", "-",
                    "0", ".", "=", "+"
                )

                items(botones.size) { index ->
                    val boton = botones[index]
                    Button(
                        onClick = {
                            when (boton) {
                                "=" -> {
                                    val resultado = calcular(valores)
                                    display = if (resultado.isNaN()) "Error" else resultado.toString()
                                    valores = listOf(resultado.toString())
                                }
                                "C" -> {
                                    valores = emptyList()
                                    display = "0"
                                }
                                else -> {
                                    valores = valores + boton
                                    display = valores.joinToString(" ")
                                }
                            }
                        },
                        modifier = Modifier.aspectRatio(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (boton in listOf("/", "*", "-", "+", "="))
                                AppColors.VerdeLima else AppColors.AzulCielo
                        )
                    ) {
                        Text(text = boton, style = MaterialTheme.typography.titleLarge)
                    }
                }

                // Botón TXT que ocupa todo el ancho
                item(span = { GridItemSpan(4) }) {
                    Button(
                        onClick = { launcher.launch("text/plain") },
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.Azul),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Agregar TXT", style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
        }
    }
}





