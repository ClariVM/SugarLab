package com.example.challengesugarlab.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager


import com.example.challengesugarlab.ui.theme.AppColors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdawnPoligono(
    opciones: List<String>,
    seleccion: String,
    onSeleccionChange: (String)->Unit,
    label: String,
){
    var desplegar by remember { mutableStateOf(false) }
    val focus = LocalFocusManager.current

    val isActivo = desplegar

    val labelColor = if(isActivo) Color.White else AppColors.Durazno
    val backg = if (isActivo) AppColors.Durazno else Color.White

    val rotation by animateFloatAsState(if (desplegar) 180f else 0f, label = "")
    val iconColor = if (backg == AppColors.Durazno) Color.White else AppColors.Durazno


    ExposedDropdownMenuBox(
        expanded = desplegar,
        onExpandedChange = {
            if(desplegar) {focus.clearFocus(force =true) }
            desplegar = !desplegar
        }
    ) {
        TextField(
            value = seleccion,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.rotate(rotation),
                tint = iconColor
            )},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = backg,
                focusedContainerColor = backg,
                unfocusedLabelColor = labelColor,
                focusedLabelColor =labelColor,
                focusedIndicatorColor = AppColors.Durazno,
                unfocusedIndicatorColor =  Color.White,
            )
        )
        ExposedDropdownMenu(
            expanded = desplegar,
            onDismissRequest = {desplegar=false
            focus.clearFocus()}
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = {Text(opcion)},
                    modifier = Modifier
                        .background(Color.White),
                    onClick = {
                        onSeleccionChange(opcion)
                        desplegar = false
                        focus.clearFocus()
                    }
                )
            }
        }
    }

}