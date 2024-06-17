package com.example.telecomanda.screens.workScreens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.telecomanda.botonbig32sp.BotonBig32sp
import com.example.telecomanda.footer.Footer
import com.example.telecomanda.header.Header
import com.example.telecomanda.logo.Logo
import com.example.telecomanda.routes.Routes
import kotlinx.coroutines.launch

@Composable
fun EmployeeWork(
    navController: NavHostController,
) {
    val employeeWorkViewModel: EmployeeWorkViewModel = viewModel()
    val notifications by employeeWorkViewModel.notifications.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var isRestaurantOpen by remember { mutableStateOf(false) }
    var restaurantNameSet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        employeeWorkViewModel.getRestaurantName(
            onSuccess = { restaurantName ->
                employeeWorkViewModel.restaurantName = restaurantName
                employeeWorkViewModel.listenToNotifications(restaurantName)
                restaurantNameSet = true
            },
            onFailure = { /* Handle the error here */ }
        )
    }

    LaunchedEffect(restaurantNameSet) {
        if (restaurantNameSet) {
            coroutineScope.launch {
                isRestaurantOpen = employeeWorkViewModel.getRestaurantState()
            }
        }
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

            Spacer(modifier = Modifier.height(32.dp))

            if (isRestaurantOpen) {
                BotonBig32sp(
                    onClick = {
                        navController.navigate(Routes.EmployeeTableSelectionScreenRoute.route)
                    },
                    text = "Ver Mesas",
                )

                Spacer(modifier = Modifier.height(16.dp))

                BotonBig32sp(
                    onClick = {
                        navController.navigate(Routes.EmployeeMenuScreenRoute.route)
                    },
                    text = "Ver Carta"
                )
            } else {
                Text(
                    text = "Caja Cerrada",
                    style = TextStyle(color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                )
            }

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
