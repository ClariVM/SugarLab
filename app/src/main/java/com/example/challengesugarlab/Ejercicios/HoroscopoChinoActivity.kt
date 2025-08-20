package com.example.challengesugarlab.Ejercicios

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.challengesugarlab.R
import com.example.challengesugarlab.assets.horoscopo.Signo
import com.example.challengesugarlab.assets.horoscopo.signosChinos
import com.example.challengesugarlab.ui.components.BarraSuperior
import com.example.challengesugarlab.ui.theme.AppColors
import com.example.challengesugarlab.ui.theme.ChallengeSugarLabTheme

class HoroscopoChinoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengeSugarLabTheme {
                MostrarHoroscopo()
            }
        }
    }
}

fun calcularSigno(anioChino: Int): Signo {
    val i = (anioChino -4) % 12
    return signosChinos[i]
}

@Composable
fun MostrarHoroscopo() {

    var anioChino by remember { mutableStateOf("") }
    var signoResult by remember { mutableStateOf<Signo?>(null) }

    if(signoResult == null){
    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Horóscopo chino",
                backgroundColor = AppColors.Rojo
            )
        }
    ) { innerPading ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPading)
                .background(color = AppColors.RojoClaro)
        ) {
            (
                    Text(
                        "El horóscopo chino es un sistema de astrología que se basa en un ciclo de 12 animales, donde cada año está asociado a uno de ellos.\n" +
                                "A diferencia del horóscopo occidental, que se guía por la posición del sol y los meses, el horóscopo chino se rige por el calendario lunar y los ciclos de 12 años.\n" +
                                "Cada signo tiene características de personalidad asociadas y también se combina con uno de los cinco elementos chinos: Madera, Fuego, Tierra, Metal y Agua, lo que crea ciclos más largos de 60 años.\n" +
                                "Se utiliza para describir rasgos de las personas, compatibilidades, y hacer predicciones para el año.",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(16.dp)
                    )
                    )
            Image(
                painter = painterResource(id = R.drawable.chinesesign),
                contentDescription = "chinese sign",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                "Conocé tu animal del horóscopo chino!",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = anioChino,
                onValueChange = { anioChino = it },
                label = { Text("Año de nacimiento") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(26.dp)
            )
            Button(
                onClick = {
                    if (anioChino.isNotEmpty()) {
                        signoResult = calcularSigno(anioChino.toInt())
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.Rojo)
            ) { Text("Ver mi signo") }

        }
    }
    }else{
        PantallaSigno(signo = signoResult!!){
            signoResult =null
            anioChino= ""
        }
    }

}

@Composable
fun PantallaSigno(signo: Signo, onVolver:() ->Unit){
    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Horóscopo chino",
                backgroundColor = AppColors.Rojo
            )
        }
            ) {padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Image(
                        painter = painterResource(id = signo.imagen),
                        contentDescription = signo.nombre,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("${signo.nombre}")
                    Text("Elemento: ${signo.elemento}", style = MaterialTheme.typography.bodyMedium)
                    Text("Características: ${signo.caracteristicas.joinToString(", ")}")
                    Text("Compatibilidad: ${signo.compatibilidad.joinToString(", ")}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(signo.descripcion)

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = onVolver) {
                        Text("Volver")
                    }
                }
            }
}


