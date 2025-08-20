package com.example.challengesugarlab.Ejercicios

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.challengesugarlab.ui.components.BarraSuperior
import com.example.challengesugarlab.ui.theme.AppColors
import com.example.challengesugarlab.ui.theme.ChallengeSugarLabTheme


class AnagramaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengeSugarLabTheme {
                mostraAnagrama()
            }
        }

    }
}

fun esAnagrama(palabra1: String, palabra2: String): Boolean{
    return palabra1.toCharArray().sorted() == palabra2.toCharArray().sorted() //separo las letras de la palabra y las ordeno alfabéticamente
}

@Composable
fun mostraAnagrama(){
    var palabra1 by remember { mutableStateOf("") }
    var palabra2 by remember { mutableStateOf("") }
    var tituloDialogo by remember { mutableStateOf("") }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var mensajeDialogo by remember { mutableStateOf("") }
    Scaffold (
        topBar = {
            BarraSuperior(
                titulo = "¿Es un Anagrama?",
                backgroundColor = AppColors.Celeste
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
                text = "•Un Anagrama consiste en formar una palabra reordenando TODAS las letras de otra palabra inicial.\n" +
                        "•NO hace falta comprobar que ambas palabras existan.\n" +
                        "•Dos palabras exactamente iguales no son anagrama.",
                style = MaterialTheme.typography.titleMedium

            )
            OutlinedTextField(
                value = palabra1,
                onValueChange = {palabra1 = it},
                placeholder = { Text("Primera palabra") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = palabra2,
                onValueChange = {palabra2 = it},
                placeholder = { Text("Segunda palabra") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                singleLine = true
            )
            Button(
                onClick = {
                    val texto1 = palabra1.trim().lowercase()
                    val texto2 = palabra2.trim().lowercase()

                    when {
                        texto1.isEmpty() || texto2.isEmpty() -> {
                            tituloDialogo = "Faltan datos"
                            mensajeDialogo = "⚠\uFE0F Completá ambas palabras."
                        }
                        texto1 == texto2 -> {
                            tituloDialogo = "❗Atención"
                            mensajeDialogo = "Las palabras son iguales. ❌ No es un anagrama."
                        }
                        esAnagrama(texto1, texto2) -> {
                            tituloDialogo = "Felicidades! 🎉"
                            mensajeDialogo = "\"$texto1\" y \"$texto2\" ✅ SON ANAGRAMAS"
                        }
                        else -> {
                            tituloDialogo = "Oops..."
                            mensajeDialogo = "\"$texto1\" y \"$texto2\" ❌ NO son anagramas"
                        }
                    }

                    mostrarDialogo = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.Celeste)
            )  {
                Text("Verificar", color = Color.White)
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
