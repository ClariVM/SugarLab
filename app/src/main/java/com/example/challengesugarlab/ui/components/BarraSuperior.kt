package com.example.challengesugarlab.ui.components

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(titulo: String, backgroundColor: Color, showback: Boolean = true){

    val flechaVolver = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher //esto es para volver atras me da acceso al dispatcher del sistema android

    CenterAlignedTopAppBar(
        title = {
            Text(text = titulo, color = Color.White)
        },
        navigationIcon = {
            if (showback) {
                IconButton(onClick = {flechaVolver?.onBackPressed()}) { //aca llamo al dispatcher para volver
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = backgroundColor
        )
    )
}