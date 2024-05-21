package com.example.telecomanda.screens.addEmployee

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun AddEmployee(
    navController: NavHostController,
    addEmployeeViewModel: AddEmployeeViewModel
) {

    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }
    val stateText: String by addEmployeeViewModel.stateText.observeAsState(initial = "")


    Box (
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    ScrollState(10000),
                    enabled = true,
                    reverseScrolling = true
                )
                .padding(
                    top = 35.dp
                )
        ) {

            Text(
                text = "TeleComanda",
                style = TextStyle(
                    fontWeight = Bold,
                    fontSize = 40.sp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "NombreRestaurante",
                style = TextStyle(
                    fontWeight = Bold,
                    fontSize = 30.sp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = name,
                onValueChange = { name = it},
                label = { Text("Nombre") },
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it},
                label = { Text("Contraseña") },
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = passwordConfirmation,
                onValueChange = { passwordConfirmation = it},
                label = { Text("Confirmar contraseña") },
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (password == passwordConfirmation){
                        addEmployeeViewModel.addEmployee(name, password)
                        addEmployeeViewModel.updateStateText("")
                        name = ""
                        password = ""
                        passwordConfirmation = ""
                    } else {
                        addEmployeeViewModel.updateStateText("Las contraseñas no coinciden")
                    }
                },
                modifier = Modifier
            ) {
                Text(text = "Añadir empleado")
            }

            Text(
                text = stateText,
                style = TextStyle(
                    fontWeight = Bold,
                    fontSize = 16.sp)
            )

        }
    }
}