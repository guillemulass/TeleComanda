package com.example.telecomanda.screens.initialScreens

import android.Manifest
import android.os.Build
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.telecomanda.routes.Routes
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun InitialScreen(
    navController: NavHostController,
) {


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(ScrollState(0))
                .padding(top = 35.dp)
        ) {
            Text(
                text = "TeleComanda",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    navController.navigate(Routes.WorkerSelectionScreenRoute.route)
                },
                modifier = Modifier
            ) {
                Text(text = "Trabajador")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate(Routes.TableCodeInputScreenRoute.route)
                },
                modifier = Modifier
            ) {
                Text(text = "Cliente")
            }
        }
    }
}
