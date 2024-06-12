package com.example.telecomanda.screens.tableQuantityController

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.telecomanda.botonbig24sp.BotonBig24sp
import com.example.telecomanda.footer.Footer
import com.example.telecomanda.header.Header
import com.example.telecomanda.logo.Logo
import com.example.telecomanda.tablenumbertext.TableNumberText

@Composable
fun TableQuantityControllerScreen(
    navController: NavHostController,
    tableQuantityControllerViewModel: TableQuantityControllerViewModel = viewModel()
) {
    val tableQuantity by tableQuantityControllerViewModel.tableQuantity.observeAsState(0)
    var tableNumberToDelete by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        tableQuantityControllerViewModel.getTableQuantity { quantity ->
            tableQuantityControllerViewModel.tableQuantity.value = quantity
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

            TableNumberText(
                modifier = Modifier
                .width(262.dp)
                .height(84.dp),
                tableQuantityText = "$tableQuantity"
            )

            Spacer(modifier = Modifier.height(16.dp))

            BotonBig24sp(
                onClick = {
                    tableQuantityControllerViewModel.editTableQuantity { newQuantity ->
                        tableQuantityControllerViewModel.tableQuantity.value = newQuantity
                    }
                },
                text = "Agregar Mesa",
                modifier = Modifier.width(266.dp).height(52.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            BotonBig24sp(
                onClick = {
                    tableQuantityControllerViewModel.deleteLastTable { newQuantity ->
                        tableQuantityControllerViewModel.tableQuantity.value = newQuantity
                    }
                },
                text = "Eliminar Última Mesa",
                modifier = Modifier.width(266.dp).height(52.dp)
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
