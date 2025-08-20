package com.example.challengesugarlab.Ejercicios

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.challengesugarlab.ui.components.BarraSuperior
import com.example.challengesugarlab.ui.theme.AppColors
import com.example.challengesugarlab.ui.theme.ChallengeSugarLabTheme

class ConjuntosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengeSugarLabTheme {
                PantallaConjuntos()
            }
        }
    }
}

@Composable
fun PantallaConjuntos() {
    var conjunto1 by remember { mutableStateOf("") }
    var conjunto2 by remember { mutableStateOf("") }
    var buscarComunes by remember { mutableStateOf(true) }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var tituloDialogo by remember { mutableStateOf("") }
    var mensajeDialogo by remember { mutableStateOf("") }

    Scaffold (
        topBar = {
            BarraSuperior(
                titulo = "Conjuntos",
                backgroundColor = AppColors.Fucsia
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = conjunto1,
                onValueChange = { conjunto1 = it },
                label = { Text("Conjunto 1 (separar con comas)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = conjunto2,
                onValueChange = { conjunto2 = it },
                label = { Text("Conjunto 2 (separar con comas)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    checked = buscarComunes,
                    onCheckedChange = { buscarComunes = it }
                )
                Spacer(Modifier.width(8.dp))
                Text(if (buscarComunes) "Buscar comunes" else "Buscar no comunes")
            }

            Button(
                onClick = {
                    val texto1 = conjunto1.trim().lowercase()
                    val texto2 = conjunto2.trim().lowercase()

                    if (texto1.isEmpty() || texto2.isEmpty()) {
                        tituloDialogo = "Error"
                        mensajeDialogo = "Completá ambos conjuntos."
                        mostrarDialogo = true
                        return@Button
                    }

                    val array1 = texto1.split(",").map { it.trim() }
                    val array2 = texto2.split(",").map { it.trim() }

                    val resultado = obtenerResultado(array1, array2, buscarComunes)

                    mensajeDialogo = if (resultado.isEmpty()) {
                        if (buscarComunes) "No hay elementos comunes."
                        else "No hay elementos no comunes, todos los elementos son iguales."
                    } else {
                        if (buscarComunes) "Elementos comunes: ${resultado.joinToString(", ")}"
                        else "Elementos no comunes: ${resultado.joinToString(", ")}"
                    }

                    tituloDialogo = "Resultado"
                    mostrarDialogo = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(AppColors.Fucsia)
            ) {
                Text("Comparar")
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
            title = { Text(tituloDialogo) },
            text = { Text(mensajeDialogo) }
        )
    }
}

private fun obtenerResultado(arr1: List<String>, arr2: List<String>, buscarComunes: Boolean): List<String> {
    val resultado = mutableListOf<String>()

    for (item in arr1) {
        val estaEnAmbos = arr2.contains(item)
        if (buscarComunes && estaEnAmbos) {
            if (!resultado.contains(item)) resultado.add(item)
        } else if (!buscarComunes && !estaEnAmbos) {
            if (!resultado.contains(item)) resultado.add(item)
        }
    }

    if (!buscarComunes) {
        for (item in arr2) {
            if (!arr1.contains(item) && !resultado.contains(item)) {
                resultado.add(item)
            }
        }
    }

    return resultado
}
