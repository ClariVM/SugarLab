package com.example.challengesugarlab.Ejercicios


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.challengesugarlab.ui.components.BarraSuperior
import com.example.challengesugarlab.ui.theme.AppColors
import com.example.challengesugarlab.ui.theme.ChallengeSugarLabTheme

// ------------------ MODELO -------------------
data class Ninio(
    val nombre: String,
    val edad: Int,
    val altura: Int
)

// ------------------ ACTIVITY -----------------
class TrucoTratoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengeSugarLabTheme {
                MostrarTrucoTrato()
            }
        }
    }
}

// ------------------ COMPOSABLE PRINCIPAL -----------------
@Composable
fun MostrarTrucoTrato() {
    var opcion by remember { mutableStateOf("Truco") } // Truco o Trato
    var resultado by remember { mutableStateOf<List<String>>(emptyList()) }
    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var edad by remember { mutableStateOf(TextFieldValue("")) }
    var altura by remember { mutableStateOf(TextFieldValue("")) }
    var actual by remember { mutableStateOf<Ninio?>(null) }

    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Truco o trato",
                backgroundColor = AppColors.Naranja
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // -------- FORMULARIO --------
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = edad,
                onValueChange = { edad = it },
                label = { Text("Edad") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = altura,
                onValueChange = { altura = it },
                label = { Text("Altura (cm)") },
                modifier = Modifier.fillMaxWidth()
            )

            // -------- SELECTOR TRUCO / TRATO --------
            Row {
                Button(onClick = { opcion = "Truco" }) {
                    Text("Truco 👻")
                }
                Spacer(Modifier.width(8.dp))
                Button(onClick = { opcion = "Trato" }) {
                    Text("Trato 🍬")
                }
            }

            // -------- BOTÓN GIRAR RULETA --------
            Button(
                onClick = {
                    val e = edad.text.toIntOrNull() ?: 0
                    val a = altura.text.toIntOrNull() ?: 0
                    if (nombre.text.isNotBlank() && e > 0 && a > 0) {
                        val nuevo = Ninio(nombre.text, e, a)
                        actual = nuevo
                        resultado = calcularResultados(opcion, listOf(nuevo))

                        // limpiar inputs
                        nombre = TextFieldValue("")
                        edad = TextFieldValue("")
                        altura = TextFieldValue("")
                    }
                }
            ) {
                Text("🎡 Girar Ruleta")
            }

            // -------- RESULTADO SOLO DEL ACTUAL --------
            actual?.let { n ->
                if (resultado.isNotEmpty()) {
                    Spacer(Modifier.height(16.dp))
                    Text("Resultado para ${n.nombre}:", fontSize = 20.sp)
                    Spacer(Modifier.height(8.dp))
                    LazyRow {
                        items(items = resultado) { item ->
                            Text(
                                text = item,
                                fontSize = 28.sp,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ------------------ LÓGICA DE CÁLCULO -----------------
fun calcularResultados(opcion: String, ninios: List<Ninio>): List<String> {
    val sustos = listOf("🎃", "👻", "💀", "🕷", "🕸", "🦇")
    val dulces = listOf("🍬", "🍭", "🍫", "🧁")

    val resultado = mutableListOf<String>()

    ninios.forEach { n ->
        if (opcion == "Truco") {
            // Un susto por cada 2 letras del nombre
            repeat(n.nombre.length / 2) { resultado.add(sustos.random()) }
            // Dos sustos por cada edad par
            if (n.edad % 2 == 0) repeat(2) { resultado.add(sustos.random()) }
            // Tres sustos por cada 100 cm
            repeat(n.altura / 100 * 3) { resultado.add(sustos.random()) }
        } else {
            // Un dulce por cada letra
            repeat(n.nombre.length) { resultado.add(dulces.random()) }
            // Un dulce por cada 3 años hasta un máximo de 10
            repeat(minOf(n.edad / 3, 10)) { resultado.add(dulces.random()) }
            // Dos dulces por cada 50 cm hasta 150
            repeat(minOf(n.altura / 50 * 2, 6)) { resultado.add(dulces.random()) }
        }
    }
    return resultado
}
