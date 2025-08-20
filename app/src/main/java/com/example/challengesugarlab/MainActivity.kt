package com.example.challengesugarlab

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.challengesugarlab.Ejercicios.AnagramaActivity
import com.example.challengesugarlab.Ejercicios.AnillosPoderActivity
import com.example.challengesugarlab.Ejercicios.AreaPoligonoActivity
import com.example.challengesugarlab.Ejercicios.BatallaPokemonActivity
import com.example.challengesugarlab.Ejercicios.CalculadoraActivity
import com.example.challengesugarlab.Ejercicios.CarreraObstaculosActivity
import com.example.challengesugarlab.Ejercicios.ConjuntosActivity
import com.example.challengesugarlab.Ejercicios.FizzbuzzActivity
import com.example.challengesugarlab.Ejercicios.HoroscopoChinoActivity
import com.example.challengesugarlab.Ejercicios.IterationActivity
import com.example.challengesugarlab.Ejercicios.MaquinaExpActivity
import com.example.challengesugarlab.Ejercicios.PPTActivity
import com.example.challengesugarlab.Ejercicios.RobotActivity
import com.example.challengesugarlab.Ejercicios.TrucoTratoActivity
import com.example.challengesugarlab.ui.components.BotonMenu
import com.example.challengesugarlab.ui.theme.AppColors
import com.example.challengesugarlab.ui.theme.ChallengeSugarLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChallengeSugarLabTheme {
                MenuPrincipal(
                    onFizzBuzzClick = { startActivity(Intent(this, FizzbuzzActivity::class.java)) },
                    onAnagramaClick = { startActivity(Intent(this, AnagramaActivity::class.java)) },
                    onAreaPoligonoClick = { startActivity(Intent(this, AreaPoligonoActivity::class.java)) },
                    onCarreraObstaculosClick = { startActivity(Intent(this, CarreraObstaculosActivity::class.java)) },
                    onConjuntosClick = { startActivity(Intent(this, ConjuntosActivity::class.java)) },
                    onPiedraPapelTijeraClick = { startActivity(Intent(this, PPTActivity::class.java)) },
                    onAnillosPoderClick = { startActivity(Intent(this, AnillosPoderActivity::class.java)) },
                    onCalculadora = {startActivity(Intent(this, CalculadoraActivity::class.java))},
                    onBatallaPokemon = {startActivity(Intent(this, BatallaPokemonActivity::class.java ))},
                    onHoroscopoChino = {startActivity(Intent(this, HoroscopoChinoActivity::class.java))},
                    onIteration = {startActivity(Intent(this, IterationActivity::class.java))},
                    onTrucoTrato = {startActivity(Intent(this, TrucoTratoActivity::class.java))},
                    onRobot = {startActivity(Intent(this, RobotActivity::class.java ))},
                    onMaquina = {startActivity(Intent(this, MaquinaExpActivity::class.java))}
                )
            }
        }
    }
}

@Composable
fun MenuPrincipal(
    onFizzBuzzClick: () -> Unit,
    onAnagramaClick: () -> Unit,
    onAreaPoligonoClick: () -> Unit,
    onCarreraObstaculosClick: () -> Unit,
    onConjuntosClick: () -> Unit,
    onPiedraPapelTijeraClick: () -> Unit,
    onAnillosPoderClick: () -> Unit,
    onCalculadora: () -> Unit,
    onBatallaPokemon:() -> Unit,
    onHoroscopoChino: () -> Unit,
    onIteration: () -> Unit,
    onTrucoTrato: () -> Unit,
    onRobot: () -> Unit,
    onMaquina:() -> Unit
    )

{
    val scrollState = rememberScrollState()
    Column(

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(25.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Bienvenidos!",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier
                .padding(16.dp),
            )
        Text(text = "SugarLab Challenge",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(20.dp),
            )
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo SugarLab",
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 24.dp),
            contentScale = ContentScale.Fit
        )
        BotonMenu(texto = "FizzBuzz", color = AppColors.Violeta, onClick = onFizzBuzzClick, modifier = Modifier.fillMaxWidth())
        BotonMenu(texto = "¿Es un anagrama?", color = AppColors.Celeste, onClick = onAnagramaClick, modifier = Modifier.fillMaxWidth())
        BotonMenu(texto = "Área de un polígono", color = AppColors.Durazno, onClick = onAreaPoligonoClick, modifier = Modifier.fillMaxWidth())
        BotonMenu(texto = "La carrera de obstáculos", color = AppColors.Turquesa, onClick = onCarreraObstaculosClick, modifier = Modifier.fillMaxWidth())
        BotonMenu(texto = "Conjuntos", color = AppColors.Fucsia, onClick = onConjuntosClick, modifier = Modifier.fillMaxWidth())
        BotonMenu(texto = "Piedra, Papel, Tijera", color = AppColors.Rosa, onClick = onPiedraPapelTijeraClick, modifier = Modifier.fillMaxWidth())
        BotonMenu(texto = "Los anillos de poder", color = AppColors.AzulOscuro, onClick = onAnillosPoderClick, modifier = Modifier.fillMaxWidth())
        BotonMenu(texto = "Calculadora", color = AppColors.VerdeLima, onClick = onCalculadora, modifier = Modifier.fillMaxWidth())
        BotonMenu(texto = "Batalla Pokemon", color = AppColors.AmarilloSol, onClick = onBatallaPokemon, modifier = Modifier.fillMaxWidth())
        BotonMenu(texto = "Horóscopo Chino", color = AppColors.Rojo, onClick = onHoroscopoChino, modifier = Modifier.fillMaxWidth())
        BotonMenu(texto = "Iteration Master", color = Color.Green, onClick = onIteration, modifier = Modifier.fillMaxWidth())
        BotonMenu(texto = "Truco o Trato", color = AppColors.Naranja, onClick = onTrucoTrato, modifier = Modifier.fillMaxWidth())
        BotonMenu(texto = "¿Dónde está el Robot?", color = AppColors.Vino, onClick = onRobot, modifier = Modifier.fillMaxWidth())
        BotonMenu(texto = "Máquina expendedora", color = AppColors.Coral, onClick = onMaquina, modifier = Modifier.fillMaxWidth())
    }
}

