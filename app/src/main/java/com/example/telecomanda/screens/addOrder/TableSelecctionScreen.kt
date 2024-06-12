package com.example.telecomanda.screens.addOrder

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.telecomanda.botonbig32sp.BotonBig32sp
import com.example.telecomanda.footer.Footer
import com.example.telecomanda.header.Header
import com.example.telecomanda.logo.Logo

@Composable
fun TableSelectionScreen(
    navController: NavHostController,
    addOrderViewModel: AddOrderViewModel
) {
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
                .background(Color(0xFF161618))
            ,
            onClick = { navController.popBackStack() }
        )
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp)
        ) {

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Logo(
                    modifier = Modifier
                        .width(199.dp)
                        .height(232.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

            }

            items(tables) { table ->
                Spacer(modifier = Modifier.height(8.dp))

                BotonBig32sp(
                    onClick = {
                        navController.navigate("addOrder/${table.number}")
                    },
                    text = "Mesa ${table.number}"
                )
            }

            item {
                Spacer(modifier = Modifier.height(70.dp))
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
            Spacer(modifier = Modifier.height(16.dp).fillMaxWidth().background(Color(0xFF161618)))
        }
    }
}