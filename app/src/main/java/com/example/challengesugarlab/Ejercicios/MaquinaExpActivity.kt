package com.example.challengesugarlab.Ejercicios

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.challengesugarlab.R
import com.example.challengesugarlab.ui.components.BarraSuperior
import com.example.challengesugarlab.ui.theme.AppColors
import com.example.challengesugarlab.ui.theme.ChallengeSugarLabTheme

data class Producto(
    val nombre: String,
    val precio: Int, // en pesos
    val imagenRes: Int // drawable o recurso de imagen
)

data class ResultadoCompra(
    val mensaje: String,
    val vuelto: Int
)


class MaquinaExpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengeSugarLabTheme {
                MostrarMaquina()
            }
        }
    }
}
@Composable
fun MostrarMaquina() {
    var monedasIngresadas by remember { mutableStateOf(0) } // dinero que ingresó el usuario
    var productosComprados by remember { mutableStateOf(listOf<String>()) }
    var mensaje by remember { mutableStateOf("") }
    var vuelto by remember { mutableStateOf(0) }
    var totalGastado by remember { mutableStateOf(0) } // suma de precios de productos comprados
    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Máquina expendedora",
                backgroundColor = AppColors.Coral
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            var monedasIngresadas by remember { mutableStateOf(0) }
            var mensaje by remember { mutableStateOf("") }
            var vuelto by remember { mutableStateOf(0) }

            Text(
                text = "Máquina expendedora",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height(500.dp)
            ) {
                items(listaProductos.size) { index ->
                    val producto = listaProductos[index]
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(6.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth().align(Alignment.CenterHorizontally)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .background(Color.White)
                        ) {
                            Image(
                                painter = painterResource(id = producto.imagenRes),
                                contentDescription = producto.nombre,
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(8.dp)
                            )
                            Text(producto.nombre, style = MaterialTheme.typography.bodyMedium)
                            Text("$${producto.precio}.00", style = MaterialTheme.typography.bodySmall)
                            Button(
                                onClick = {
                                    val precioTotal = totalGastado + producto.precio
                                    if (precioTotal <= monedasIngresadas) {
                                        // Compra permitida
                                        totalGastado += producto.precio
                                        productosComprados = productosComprados + producto.nombre
                                        mensaje = "Has comprado: ${productosComprados.joinToString(", ")}"
                                        vuelto = monedasIngresadas - totalGastado
                                    } else {
                                        mensaje = "Dinero insuficiente"
                                    }
                                },
                                modifier = Modifier.padding(top = 8.dp),
                                colors = ButtonDefaults.buttonColors(AppColors.Coral)
                            ) {
                                Text("Comprar")
                            }

                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Saldo ingresado: $$monedasIngresadas")
            Text("$mensaje")
            if (vuelto > 0) Text("Vuelto: $$vuelto")

            Spacer(modifier = Modifier.height(16.dp))

            val monedas = listOf(5, 10, 50, 100, 200)

            Column {
                Row(modifier = Modifier.padding(bottom = 8.dp)) {
                    monedas.take(4).forEach { moneda ->
                        MonedaBoton(moneda) { monedasIngresadas += moneda }
                    }
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                monedasIngresadas = 0
                mensaje = ""
                vuelto = 0
            }, colors = ButtonDefaults.buttonColors(AppColors.Coral)) {
                Text("Resetear")
            }
        }
    }
}

fun comprarProducto(monedasIngresadas: Int, precio: Int, nombre: String): ResultadoCompra {
    return if (monedasIngresadas < precio) {
        ResultadoCompra("Dinero insuficiente", 0)
    } else {
        ResultadoCompra("Has comprado: $nombre", monedasIngresadas - precio)
    }
}



val listaProductos = listOf(
    Producto("Agua", 100, R.drawable.agua),
    Producto("Gaseosa", 150, R.drawable.gaseosa),
    Producto("Snack", 200, R.drawable.snack),
    Producto("Jugo", 120, R.drawable.jugo)
)

@Composable
fun MonedaBoton(moneda: Int, onClick: () -> Unit) {

    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(end = 8.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = when (moneda) {
                5 -> Color(0xFFC0C0C0)
                10 -> Color(0xFFD4AF37)
                50 -> Color(0xFFC0C0C0)
                100 -> Color(0xFFD4AF37)

                else -> Color.Gray
            }
        ),
        elevation = ButtonDefaults.buttonElevation(8.dp)
    ) {
        Text(
            text = "$$moneda",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

    }
}
