package com.example.telecomanda.screens.logIn

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.telecomanda.routes.Routes

@Composable
fun LogInSelector(
    navController: NavHostController,
) {

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
                text = "Iniciar Sesion",
                style = TextStyle(
                    fontWeight = Bold,
                    fontSize = 30.sp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    navController.navigate(Routes.LogInAdministratorScreenRoute.route)
                },
                modifier = Modifier
            ) {
                Text(text = "Administrador")
            }
            Button(
                onClick = {
                    navController.navigate(Routes.AdminWorkScreenRoute.route)
                },
                modifier = Modifier
            ) {
                Text(text = "Administrador2")
            }

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = {
                    navController.navigate(Routes.LogInEmployeeScreenRoute.route)
                },
                modifier = Modifier
            ) {
                Text(text = "Empleado")
            }
            Button(
                onClick = {
                    navController.navigate(Routes.EmployeeWorkScreenRoute.route)
                },
                modifier = Modifier
            ) {
                Text(text = "Administrador2")
            }

        }
    }

}