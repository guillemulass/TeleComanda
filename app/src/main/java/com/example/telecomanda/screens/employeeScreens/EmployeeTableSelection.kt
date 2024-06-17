package com.example.telecomanda.screens.employeeScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.telecomanda.footer.Footer
import com.example.telecomanda.header.Header
import com.example.telecomanda.logo.Logo
import com.example.telecomanda.tableiconbutton.TableIconButton

@Composable
fun EmployeeTableSelectionScreen(
    navController: NavHostController,
) {
    val addOrderViewModel: EmployeeAddOrderViewModel = viewModel()
    val tables by addOrderViewModel.tableList.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        addOrderViewModel.fetchTables()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF161618))
            .padding(top = 35.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Header(
            modifier = Modifier
                .width(430.dp)
                .height(60.dp)
                .background(Color(0xFF161618)),
            onClick = { navController.popBackStack() }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(top = 70.dp)
        ) {

            Logo(
                modifier = Modifier
                    .width(199.dp)
                    .height(232.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
            ) {

                items(tables) { table ->
                    TableIconButton(
                        onClick = {
                            navController.navigate("employeeAddOrder/${table.number}")
                        },
                        tableNumber = table.number.toString()
                    )
                }

                item(span = { GridItemSpan(2) }) {
                    Spacer(modifier = Modifier.height(70.dp))
                }

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
                    .background(Color(0xFF161618))
            )
            Spacer(modifier = Modifier
                .height(16.dp)
                .fillMaxWidth()
                .background(Color(0xFF161618)))
        }
    }
}
