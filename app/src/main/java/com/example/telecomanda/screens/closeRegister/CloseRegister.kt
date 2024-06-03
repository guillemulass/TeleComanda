package com.example.telecomanda.screens.closeRegister

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@Composable
fun CloseRegisterScreen(
    navController: NavHostController,
    closeRegisterViewModel: CloseRegisterViewModel = viewModel()
) {
    var todaysTotal by remember { mutableStateOf(0.0) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        todaysTotal = closeRegisterViewModel.getTodaysTotal()
    }

    Box(
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
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Total ingresado: €$todaysTotal",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        closeRegisterViewModel.closeRegister(todaysTotal)
                        todaysTotal = 0.0
                    }
                },
                modifier = Modifier
            ) {
                Text(text = "Cerrar Caja")
            }
        }
    }
}
