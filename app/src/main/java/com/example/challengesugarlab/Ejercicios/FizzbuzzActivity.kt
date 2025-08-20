package com.example.challengesugarlab.Ejercicios

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.challengesugarlab.ui.components.BarraSuperior
import com.example.challengesugarlab.ui.theme.AppColors
import com.example.challengesugarlab.ui.theme.ChallengeSugarLabTheme

/**
 * FizzbuzzActivity:
 * Actividad principal que muestra una cuadrícula interactiva de números del 1 al 100,
 * donde el usuario puede jugar al clásico juego de FizzBuzz.
 *
 * Cada botón representa un número:
 * - Si aún no fue presionado: muestra el número.
 * - Si se presiona: se revela si ese número es Fizz, Buzz, FizzBuzz o el número.
 */
class FizzbuzzActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengeSugarLabTheme {
                mostrarFizzBuzz()
            }
        }
    }
}
/**
 * generarFizzBuzz:
 * Función que aplica la lógica del juego FizzBuzz.
 *
 * @param numero Número entero a evaluar.
 * @return Un String con "Fizz", "Buzz", "FizzBuzz" o el número como texto.
 */
fun generarFizzBuzz(numero: Int): String {
    return when {
        numero % 15 == 0 -> "FizzBuzz"
        numero % 3 == 0 -> "Fizz"
        numero % 5 == 0 -> "Buzz"
        else -> numero.toString()
    }
}
/**
 * mostrarFizzBuzz:
 * Composable principal que construye la interfaz del juego FizzBuzz.
 *
 * Incluye:
 * - Barra superior con título.
 * - Instrucciones del juego.
 * - Cuadrícula de botones del 1 al 100.
 *   Cada botón cambia de texto y color al presionarse, según la lógica FizzBuzz.
 */
@Composable
fun mostrarFizzBuzz(){
    Scaffold (
        topBar = {
            BarraSuperior(
                titulo = "FizzBuzz",
                backgroundColor = AppColors.Violeta
            )
        }
    )
    { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Título del juego
            Text(
                text = "Adivina el FizzBuzz!",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            // Explicación de las reglas
            Text(
                text =  "• Múltiplos de 3 -> \"Fizz\".\n• Múltiplos de 5 -> \"Buzz\".\n• Múltiplos de 3 y 5 -> \"FizzBuzz\".",
                style = MaterialTheme.typography.titleMedium
            )
            // Cuadrícula de botones (del 1 al 100)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.weight(1f),   //esto ocupa el espacio que sobra
                contentPadding = PaddingValues(8.dp)
            ) {
                items(100){index->
                    val numero = index +1
                    var mostrado by remember { mutableStateOf(false) }

                    // Resultado basado en la lógica FizzBuzz
                    val resultado = generarFizzBuzz(numero)
                    val setMostrado = if(mostrado) resultado else numero.toString()

                    // Color del botón según resultado
                    val color = if(!mostrado) {
                        AppColors.AzulOscuro
                    }else{
                        when(resultado){
                            "FizzBuzz" -> AppColors.Violeta
                            "Fizz" -> AppColors.Rosa
                            "Buzz" -> AppColors.Durazno
                            else -> AppColors.Celeste
                        }
                    }
                    // Botón individual del grid
                    Button(
                        onClick = {
                            mostrado =  !mostrado
                                  },
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = color)
                    ) {
                        Text(setMostrado, maxLines = 1, fontSize = 12.sp) // Ajuste para evitar cambios de tamaño
                    }
                }
            }
        }
    }
}


