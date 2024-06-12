package com.example.telecomanda.screens.closeRegister

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.telecomanda.botonbig32sp.BotonBig32sp
import com.example.telecomanda.footer.Footer
import com.example.telecomanda.header.Header
import com.example.telecomanda.logo.Logo
import com.example.telecomanda.todaystotaltext.TodaysTotalText
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
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF161618))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(ScrollState(0))
                .padding(top = 35.dp)
        ) {

            Box {
                Header(
                    modifier = Modifier
                        .width(450.dp)
                        .height(60.dp),
                    onClick = { navController.popBackStack() }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Logo(
                modifier = Modifier
                    .width(199.dp)
                    .height(232.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            TodaysTotalText(
                moneyText = "$todaysTotal€",
            )

            Spacer(modifier = Modifier.height(32.dp))

            BotonBig32sp(
                onClick = {
                    coroutineScope.launch {
                        closeRegisterViewModel.closeRegister(todaysTotal)
                        todaysTotal = 0.0
                    }
                },
                text = "Cerrar Caja"
            )

        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Footer(
                modifier = Modifier
                    .width(430.dp)
                    .height(54.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}