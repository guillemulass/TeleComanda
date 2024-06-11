package com.example.telecomanda.screens.initialScreens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.telecomanda.botonbig32sp.BotonBig32sp
import com.example.telecomanda.logo.Logo
import com.example.telecomanda.mainheader.MainHeader
import com.example.telecomanda.routes.Routes

@Composable
fun InitialScreen(
    navController: NavHostController,
) {

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

            Box (
                Modifier
            ){
                MainHeader(
                    modifier = Modifier
                        .width(430.dp)
                        .height(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Logo(
                modifier = Modifier
                .width(199.dp)
                .height(232.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            BotonBig32sp(
                onClick = {
                    navController.navigate(Routes.WorkerSelectionScreenRoute.route)
                },
                text = "Trabajador"
            )

            Spacer(modifier = Modifier.height(16.dp))

            BotonBig32sp(
                onClick = {
                    navController.navigate(Routes.TableCodeInputScreenRoute.route)
                },
                text = "Cliente"
            )
        }
    }
}
