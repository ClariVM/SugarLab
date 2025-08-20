package com.example.challengesugarlab.Ejercicios

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.challengesugarlab.R
import com.example.challengesugarlab.ui.components.BarraSuperior
import com.example.challengesugarlab.ui.components.DropdawnPoligono
import com.example.challengesugarlab.ui.theme.AppColors
import com.example.challengesugarlab.ui.theme.ChallengeSugarLabTheme

class AreaPoligonoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengeSugarLabTheme {
                mostrarArea()
            }
        }
    }
}

fun calcularArea(poligono: String, base:Double, altura:Double ): Double{
    return when(poligono){
        "Triángulo" -> (base * altura) / 2
        "Cuadrado" -> base * base
        "Rectángulo" -> base * altura
        else -> 0.0
    }
}
@Preview
@Composable
fun mostrarArea(){
    var poligonoSeleccionado by remember { mutableStateOf("") }
    var base by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var mensajeDialogo by remember { mutableStateOf("") }
    var mostrarDialogo by remember { mutableStateOf(false) }
    Scaffold (
        topBar = {
            BarraSuperior(
                titulo = "Área de un polígono",
                backgroundColor = AppColors.Durazno
            )
        }
    )
    { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ){
            Text(
                text = "•Calcula el área de un polígono según su tipo: Triángulo, Cuadrado o Rectángulo.",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.padding(8.dp))
3
            DropdawnPoligono(
                opciones = listOf("Triángulo", "Cuadrado", "Rectángulo"),
                seleccion = poligonoSeleccionado,
                onSeleccionChange = {
                    poligonoSeleccionado = it
                    base =""
                    altura=""},
                label = "Seleccioná un polígono"
            )

            Spacer(modifier = Modifier.padding(8.dp))

            when(poligonoSeleccionado){
                "Triángulo", "Rectángulo" ->{
                    OutlinedTextField(
                        value = base,
                        onValueChange = {base = it},
                        label = { Text("Base") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    OutlinedTextField(
                        value = altura,
                        onValueChange = {altura = it},
                        label = { Text("Altura") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                "Cuadrado" -> {
                    OutlinedTextField(
                        value = base,
                        onValueChange = {base = it},
                        label = { Text("Lado") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.padding(8.dp))

            Button(
                onClick = {
                    if(poligonoSeleccionado.isBlank()){
                        mensajeDialogo = "⚠\uFE0F Seleccioná un polígono"
                    }else if (
                        (poligonoSeleccionado == "Cuadrado" && base.isBlank()) ||
                        ((poligonoSeleccionado == "Triángulo" || poligonoSeleccionado == "Rectángulo")&&
                                (base.isBlank() ||altura.isBlank()))
                    ){
                        mensajeDialogo ="\"⚠\uFE0F Completá todos los campos\""
                    } else{
                        val base = base.toDoubleOrNull()
                        val altura =altura.toDoubleOrNull()

                        if (base == null || (poligonoSeleccionado != "Cuadrado" && altura == null)){
                            mensajeDialogo = "❌ Ingresá valores numéricos válidos"
                        } else{
                            val area = calcularArea(
                                poligono = poligonoSeleccionado,
                                base = base,
                                altura = if (poligonoSeleccionado == "Cuadrado") 0.0 else altura!! //0.0 como altura porque no se necesita y !! porque no es null es 0
                            )
                            mensajeDialogo = "✅ El área del $poligonoSeleccionado es: $area"
                        }
                    }
                    mostrarDialogo = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.Durazno)
            ) {
                Text("Calcular área")
            }
        }
    }
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            confirmButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("OK")
                }
            },
            title = { Text("Resultado") },
            text = { Text(mensajeDialogo) }
        )
    }
}