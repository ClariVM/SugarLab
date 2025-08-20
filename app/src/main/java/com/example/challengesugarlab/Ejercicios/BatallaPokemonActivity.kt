package com.example.challengesugarlab.Ejercicios

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.example.challengesugarlab.Services.Pokemon
import com.example.challengesugarlab.Services.RetrofitInstance
import com.example.challengesugarlab.ui.components.BarraSuperior
import com.example.challengesugarlab.ui.theme.AppColors
import com.example.challengesugarlab.ui.theme.ChallengeSugarLabTheme
import kotlinx.coroutines.launch
import kotlin.random.Random

class BatallaPokemonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengeSugarLabTheme {
                BatallaPokemonScreen()
            }
        }
    }
}

/**
 * Calcula el daño que un Pokémon inflige a otro.
 *
 * @param ataque Valor de ataque del Pokémon atacante.
 * @param defensa Valor de defensa del Pokémon defensor.
 * @param tipoAtacante Tipo principal del atacante (e.g., "fire").
 * @param tipoDefensor Tipo principal del defensor (e.g., "grass").
 * @return Daño calculado como Double.
 */
fun calcularDanio(ataque: Int, defensa: Int, tipoAtacante: String, tipoDefensor: String): Double {
    // Multiplicador según la efectividad de tipo
    val efectividad = when (tipoAtacante to tipoDefensor) {
        "fire" to "grass", "water" to "fire", "grass" to "water", "electric" to "water" -> 2.0
        "grass" to "fire", "fire" to "water", "water" to "grass", "water" to "electric" -> 0.5
        else -> 1.0
    }
    // Fórmula de daño base
    return 50 * (ataque.toDouble() / defensa.toDouble()) * efectividad
}

/**
 * Tarjeta que muestra la información de un Pokémon.
 *
 * @param pokemon Objeto con los datos del Pokémon.
 * @param danoContra (Opcional) Daño calculado contra un oponente.
 * @param modifier Modificador de tamaño/estilo Compose.
 */
@Composable
fun PokemonCard(
    pokemon: Pokemon,
    danoContra: Double? = null,
    modifier: Modifier = Modifier
) {
    val ataque = pokemon.stats.firstOrNull { it.stat.name == "attack" }?.base_stat ?: 0
    val defensa = pokemon.stats.firstOrNull { it.stat.name == "defense" }?.base_stat ?: 0
    val tipo = pokemon.types.firstOrNull()?.type?.name ?: "normal"

    Card(
        modifier = modifier
            .size(width = 150.dp, height = 220.dp)
            .border(2.dp, Color.Black, RoundedCornerShape(12.dp)).padding(8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Image(
                painter = rememberAsyncImagePainter(pokemon.sprites.front_default),
                contentDescription = pokemon.name,
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.height(8.dp))
            Text(pokemon.name.capitalize(), style = MaterialTheme.typography.titleMedium)
            Text("Tipo: ${tipo.capitalize()}", style = MaterialTheme.typography.bodySmall)
            Text("Ataque: $ataque", style = MaterialTheme.typography.bodySmall)
            Text("Defensa: $defensa", style = MaterialTheme.typography.bodySmall)
            // Mostrar daño solo si fue calculado
            danoContra?.let {
                Spacer(Modifier.height(4.dp))
                Text("Daño: ${"%.1f".format(it)}", style = MaterialTheme.typography.bodySmall.copy(color = Color.Red))
            }
        }
    }
}
/**
 * Pantalla de la batalla Pokémon.
 * - Genera mazos aleatorios de 3 Pokémon para cada jugador.
 * - Muestra cartas con animación flip.
 * - Controla las rondas y el conteo de victorias.
 */
@Composable
fun BatallaPokemonScreen() {
    val scope = rememberCoroutineScope()

    // Estados para los mazos y progreso del juego

    var mazoJugador1 by remember { mutableStateOf<List<Pokemon>>(emptyList()) }
    var mazoJugador2 by remember { mutableStateOf<List<Pokemon>>(emptyList()) }

    var indiceActual by remember { mutableStateOf(0) }
    var mostrarResultado by remember { mutableStateOf(false) }
    var resultadoRonda by remember { mutableStateOf("") }

    // Contadores de victorias
    var victoriasJugador1 by remember { mutableStateOf(0) }
    var victoriasJugador2 by remember { mutableStateOf(0) }


    /**
     * Función para cargar mazos aleatorios.
     * Cada mazo contiene 3 Pokémon obtenidos de la PokeAPI.
     */
    fun cargarMazos() {
        scope.launch {
            val mazo1 = List(3) { RetrofitInstance.api.getPokemon(Random.nextInt(1, 151)) }
            val mazo2 = List(3) { RetrofitInstance.api.getPokemon(Random.nextInt(1, 151)) }
            mazoJugador1 = mazo1
            mazoJugador2 = mazo2
            indiceActual = 0
            mostrarResultado = false
            resultadoRonda = ""
            victoriasJugador1 = 0
            victoriasJugador2 = 0
        }
    }
    // Cargar mazos al inicio
    LaunchedEffect(Unit) { cargarMazos() }

    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Batalla Pokémon",
                backgroundColor = AppColors.AmarilloSol
            )
        }
    ) { innerPadding ->
        Box(
            Modifier.fillMaxSize().padding(innerPadding)
        ) {
            Ayuda(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .zIndex(1f)
            )

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (mazoJugador1.isEmpty() || mazoJugador2.isEmpty() || indiceActual >= mazoJugador1.size) {
                    Text("Cargando cartas...", style = MaterialTheme.typography.bodyMedium)
                } else {
                    val p1 = mazoJugador1[indiceActual]
                    val p2 = mazoJugador2[indiceActual]


                    Row(Modifier.fillMaxWidth().padding(top = 50.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Jugador 1", style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(8.dp))
                            // Carta jugador 1
                            FlipCard(
                                front = {
                                    // Sólo imagen y nombre en el frente
                                    PokemonCardSimple(
                                        pokemon = p1,
                                        modifier = Modifier.size(180.dp, 220.dp)
                                    )
                                },
                                back = {
                                    // Carta con datos y daño al reverso
                                    val ataque1 =
                                        p1.stats.firstOrNull { it.stat.name == "attack" }?.base_stat
                                            ?: 50
                                    val defensa2 =
                                        p2.stats.firstOrNull { it.stat.name == "defense" }?.base_stat
                                            ?: 50
                                    val tipo1 = p1.types.firstOrNull()?.type?.name ?: "normal"
                                    val tipo2 = p2.types.firstOrNull()?.type?.name ?: "normal"
                                    val danio = calcularDanio(ataque1, defensa2, tipo1, tipo2)
                                    PokemonCard(
                                        pokemon = p1,
                                        danoContra = danio,
                                        modifier = Modifier.size(180.dp, 260.dp)
                                    )
                                },
                                isFlipped = mostrarResultado
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Jugador 2", style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(8.dp))

                            // Carta jugador 2
                            FlipCard(
                                front = {
                                    PokemonCardSimple(
                                        pokemon = p2,
                                        modifier = Modifier.size(180.dp, 220.dp)
                                    )
                                },
                                back = {
                                    val ataque2 =
                                        p2.stats.firstOrNull { it.stat.name == "attack" }?.base_stat
                                            ?: 50
                                    val defensa1 =
                                        p1.stats.firstOrNull { it.stat.name == "defense" }?.base_stat
                                            ?: 50
                                    val tipo2 = p2.types.firstOrNull()?.type?.name ?: "normal"
                                    val tipo1 = p1.types.firstOrNull()?.type?.name ?: "normal"
                                    val danio = calcularDanio(ataque2, defensa1, tipo2, tipo1)
                                    PokemonCard(
                                        pokemon = p2,
                                        danoContra = danio,
                                        modifier = Modifier.size(180.dp, 260.dp)
                                    )
                                },
                                isFlipped = mostrarResultado
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    if (mostrarResultado) {
                        Text(resultadoRonda, style = MaterialTheme.typography.headlineSmall)
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "   Victorias:\nJugador 1 = $victoriasJugador1 \nJugador 2 = $victoriasJugador2",
                            style = MaterialTheme.typography.titleLarge, color = AppColors.GrisOscuro
                        )

                        // Mostrar botón para siguiente ronda solo si quedan rondas
                        if (indiceActual < mazoJugador1.size - 1) {
                            Button(
                                onClick = {
                                    indiceActual++
                                    mostrarResultado = false
                                    resultadoRonda = ""
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AppColors.AmarilloSol)
                            ) {
                                Text("Siguiente ronda", color = Color.Black)
                            }
                        } else {
                            Text("¡Batalla terminada!", style = MaterialTheme.typography.titleLarge, color = AppColors.Azul, modifier = Modifier.padding(top = 8.dp))

                            Spacer(Modifier.height(24.dp))

                            val ganador = when{
                                victoriasJugador1 > victoriasJugador2 -> "JUGADOR 1 GANA LA BATALLA"
                                victoriasJugador2 > victoriasJugador1 -> "JUGADOR 2 GANA LA BATALLA"
                                else -> "Fue un empate!"
                            }
                            Text(ganador, style = MaterialTheme.typography.titleLarge, color = AppColors.Verde)

                            Button(onClick = {
                                victoriasJugador1 = 0
                                victoriasJugador2 = 0
                                indiceActual = 0
                                mostrarResultado = false
                                resultadoRonda = ""
                                cargarMazos()
                            },
                                colors = ButtonDefaults.buttonColors(containerColor = AppColors.AmarilloSol), modifier = Modifier.padding(top = 8.dp)) {
                                Text("Volver a comenzar", color = Color.Black)
                            }
                        }
                    }


                    // Mostrar botones solo si NO están dadas vuelta las cartas (antes de pelear)
                    if (!mostrarResultado) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(onClick = {
                                cargarMazos()
                            },
                                colors = ButtonDefaults.buttonColors(containerColor = AppColors.AmarilloSol)) {
                                Text("Recargar Mazos", color = Color.Black)
                            }
                            Button(onClick = {
                                if (mazoJugador1.isNotEmpty() && mazoJugador2.isNotEmpty() && indiceActual < mazoJugador1.size) {
                                    val p1 = mazoJugador1[indiceActual]
                                    val p2 = mazoJugador2[indiceActual]

                                    val ataque1 =
                                        p1.stats.firstOrNull { it.stat.name == "attack" }?.base_stat
                                            ?: 50
                                    val defensa1 =
                                        p1.stats.firstOrNull { it.stat.name == "defense" }?.base_stat
                                            ?: 50
                                    val tipo1 = p1.types.firstOrNull()?.type?.name ?: "normal"

                                    val ataque2 =
                                        p2.stats.firstOrNull { it.stat.name == "attack" }?.base_stat
                                            ?: 50
                                    val defensa2 =
                                        p2.stats.firstOrNull { it.stat.name == "defense" }?.base_stat
                                            ?: 50
                                    val tipo2 = p2.types.firstOrNull()?.type?.name ?: "normal"

                                    val danio1 = calcularDanio(ataque1, defensa2, tipo1, tipo2)
                                    val danio2 = calcularDanio(ataque2, defensa1, tipo2, tipo1)

                                    resultadoRonda = when {
                                        danio1 > danio2 -> {
                                            victoriasJugador1++
                                            "Jugador 1 gana la ronda"
                                        }

                                        danio2 > danio1 -> {
                                            victoriasJugador2++
                                            "Jugador 2 gana la ronda"
                                        }

                                        else -> "Empate"
                                    }
                                    mostrarResultado = true
                                }
                            },
                                colors = ButtonDefaults.buttonColors(containerColor = AppColors.AmarilloSol)) {
                                Text("Pelear ronda", color = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable que muestra una tarjeta simple de Pokémon:
 * - Imagen
 * - Nombre
 * Sirve para mostrar las cartas antes de que se "den vuelta"
 */
@Composable
fun PokemonCardSimple(pokemon: Pokemon, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .border(2.dp, AppColors.AmarilloSol, RoundedCornerShape(12.dp)).padding(8.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.AmarilloClaro)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Image(
                painter = rememberAsyncImagePainter(pokemon.sprites.front_default),
                contentDescription = pokemon.name,
                modifier = Modifier.size(150.dp),
            )
            Spacer(Modifier.height(8.dp))
            Text(pokemon.name.capitalize(), style = MaterialTheme.typography.titleMedium)
        }
    }
}

/**
 * Composable que crea una carta "flip" (dar vuelta).
 *
 * @param frente Contenido que se muestra en el frente de la carta.
 * @param dorso Contenido que se muestra en el dorso de la carta.
 * @param volteada Indica si la carta está mostrando el dorso (true) o el frente (false).
 * @param onClick Acción a ejecutar cuando el usuario toca la carta.
 */
@Composable
fun FlipCard(
    front: @Composable () -> Unit,
    back: @Composable () -> Unit,
    isFlipped: Boolean,
    modifier: Modifier = Modifier
) {
    val rotation = animateFloatAsState(targetValue = if (isFlipped) 180f else 0f)

    Box(
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 12f * density
            }
    ) {
        if (rotation.value <= 90f) {
            front()
        } else {
            Box(modifier = Modifier.graphicsLayer { rotationY = 180f }) {
                back()
            }
        }
    }
}

@Composable
fun Ayuda(modifier: Modifier =Modifier){
    var mostrarDialogo by remember { mutableStateOf(false) }

    IconButton(
        onClick = { mostrarDialogo=true },
        modifier = modifier
        ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Ayuda",
            tint = AppColors.AzulCielo
        )
    }

    if (mostrarDialogo){
        AlertDialog(
            onDismissRequest = {mostrarDialogo=false},
            title = { Text("Cómo jugar") },
            text =
                { Text(
                    "Batalla Pokémon:\n" +
                            "*Cada jugador recibe 3 cartas al azar.\n" +
                            "*Las cartas representan un Pokémon con sus estadísticas y tipos.\n" +
                            "*Al iniciar la pelea, el juego compara las cartas y te indica quién ganó según las reglas de combate (por ejemplo, daño o tipo).\n" +
                            "*El objetivo es ganar la mayor cantidad de rondas usando estrategia y un poco de suerte."
                )
                },
            confirmButton = {
                TextButton(onClick = {mostrarDialogo = false}) {
                    Text("Cerrar")
                }
            }
        )
    }
}


