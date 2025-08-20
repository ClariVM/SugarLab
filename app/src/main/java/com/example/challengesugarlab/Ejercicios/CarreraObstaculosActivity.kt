package com.example.challengesugarlab.Ejercicios

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.challengesugarlab.ui.components.BarraSuperior
import com.example.challengesugarlab.ui.theme.AppColors
import com.example.challengesugarlab.ui.theme.ChallengeSugarLabTheme
import kotlin.random.Random

class CarreraObstaculosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengeSugarLabTheme {
                mostrarCarrera()
            }
        }
    }
}

fun generarPista(largoPista: Int):List<Char>{
    return List(largoPista){ if(Random.nextBoolean()) '_' else '|'}
}

fun evaluarCarrera(movimientos: List<String>, pista: List<Char>): Pair<Boolean,List<Char>> {
    val pistaModif = pista.toMutableList()
    var exito = true

    for (i in movimientos.indices){
        val mov = movimientos[i].lowercase()
        val pistaSeg = pista[i]

        when{
            pistaSeg == '_' && mov == "run" -> {} //esta condicion es correcta por lo tanto no hace nada
            pistaSeg == '|' && mov == "jump" -> {} //esta condicion es correcta por lo tanto no hace nada
            pistaSeg == '_' && mov == "jump" -> {
                pistaModif[i] = 'x' //este es un error porque saltó en el suelo
                exito = false
            }
            pistaSeg == '|' && mov == "run" -> {
                pistaModif[i] = '/'
                exito = false
            }
        }
    }
    return Pair(exito, pistaModif)
}

// ===== Enum para las etapas del juego =====
enum class EtapaJuego{Inicio, Juego, Resultado}

// ===== Composable principal =====
@Composable
fun mostrarCarrera(){
    //Estado que controla la pantalla
    var etapa by remember { mutableStateOf(EtapaJuego.Inicio) }

    // longitud elegida por el usuario (6 por defecto)
    var largoPista by rememberSaveable { mutableStateOf(6) }

    // la pista generada (oculta hasta evaluar)
    var pista by remember { mutableStateOf<List<Char>>(emptyList()) }

    // movimientos acumulados por el jugador ("run"/"jump")
    var movimientos by remember { mutableStateOf<List<String>>(emptyList()) }

    // índice del tramo actual
    var indiceActual by remember { mutableStateOf(0) }

    // pista con marcas después de evaluar
    var pistaModificada by remember { mutableStateOf<List<Char>>(emptyList()) }

    var exito by remember { mutableStateOf(false) }

   Scaffold (
       topBar = {
           BarraSuperior(
               titulo = "Carrera de obstáculos",
               backgroundColor = AppColors.Turquesa
           )
       }
   ){innerPadding ->
     Column(
         modifier = Modifier
             .fillMaxSize()
             .padding(innerPadding)
             .padding(16.dp)
     ) {
         Text(
             text = "Adiviná cada tramo: Run para suelo \uD83C\uDFC3\u200D♂, Jump para saltar la valla \uD83D\uDEA7",
             style = MaterialTheme.typography.titleMedium,
             modifier = Modifier.padding(16.dp)
         )
         // Cambio de UI según etapa
         Crossfade(targetState = etapa) { s ->
             when (s) {
                 EtapaJuego.Inicio -> pantallaInicio(
                     largoPista = largoPista,
                     onPistaModif = { largoPista = it },
                     onComenzar = {
                         pista = generarPista(largoPista)
                         movimientos = emptyList()
                         indiceActual = 0
                         etapa = EtapaJuego.Juego
                     }
                 )

                 EtapaJuego.Juego -> mostrarJuego(
                     indiceActual = indiceActual,
                     total = pista.size,
                     onElegido = { eleccion ->
                         movimientos = movimientos + eleccion
                         if (indiceActual + 1 < pista.size) {
                             indiceActual++
                         } else {
                             val resultado = evaluarCarrera(movimientos, pista)
                             exito = resultado.first
                             pistaModificada = resultado.second
                             etapa = EtapaJuego.Resultado
                         }
                     }
                 )

                 EtapaJuego.Resultado -> {
                     if (!exito) {
                         Column(
                             horizontalAlignment = Alignment.CenterHorizontally,
                             verticalArrangement = Arrangement.spacedBy(16.dp)
                         ) {
                             Text("Ups, cometiste errores. Intentá de nuevo:")
                             revelarPista(pistaModificada)

                             Button(onClick = {
                                 movimientos = emptyList()
                                 indiceActual = 0
                                 etapa = EtapaJuego.Juego
                             }) {
                                 Text("Reintentar")
                             }
                         }
                     } else {
                         Text("¡Felicidades! Carrera perfecta 🏆")
                     }
                 }
             }
         }
     }
   }
}


@Composable
fun pantallaInicio(largoPista: Int, onPistaModif: (Int)->Unit, onComenzar: () -> Unit){
    Column {
        Text("Elegí la longitud de la pista", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier =  Modifier.height(8.dp))
        Row (horizontalArrangement = Arrangement.spacedBy(8.dp)){
            listOf(4,6,8).forEach { long ->
                Button(
                    onClick = { onPistaModif(long) },
                    colors = ButtonDefaults.buttonColors(containerColor = if (long == largoPista) AppColors.Celeste else AppColors.Violeta)
                ) {
                    Text("$long tramos")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onComenzar, modifier = Modifier.fillMaxWidth()) {
            Text("Generar pista y jugar")
        }
    }
}
@Composable
fun mostrarJuego(indiceActual: Int, total: Int, onElegido: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text("Tramo ${indiceActual + 1} de $total", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { onElegido("run") }, modifier = Modifier.weight(1f).height(64.dp)) {
                Text("🏃 Run")
            }
            Button(onClick = { onElegido("jump") }, modifier = Modifier.weight(1f).height(64.dp)) {
                Text("\uD83D\uDEA7 Jump")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressIndicator(
            progress = (indiceActual.toFloat() / total.toFloat()),
            modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun revelarPista(modified: List<Char>) {
    Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        modified.forEach { c ->
            when (c) {
                '_' -> SegmentBox(label = "🏃‍♂️", bg = Color(0xFFD0F0C0)) // suelo correcto
                '|' -> SegmentBox(label = "\uD83D\uDEA7️", bg = Color(0xFFD0E8FF)) // valla correcta
                '/' -> SegmentBox(label = "↗\uFE0F", bg = Color(0xFFFFF3C0)) // error: corrió en valla
                'x' -> SegmentBox(label = "❌", bg = Color(0xFFFFE0E0)) // error: saltó en suelo
                else -> SegmentBox(label = c.toString(), bg = Color.LightGray)
            }
        }
    }
}

@Composable
fun SegmentBox(label: String, bg: Color) {
    Box(
        modifier = Modifier.size(48.dp).background(bg, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(label)
    }
}
