@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.telecomanda.screens.addToMenu

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.telecomanda.enumClass.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun AddToMenu(
    navController: NavHostController,
    addToMenuViewModel: AddToMenuViewModel
) {
    val isDrink: Boolean by addToMenuViewModel.isDrink.observeAsState(initial = false)
    val drinkOrDishText: String by addToMenuViewModel.dishOrDrinkString.observeAsState(initial = "Plato")
    val stateText: String by addToMenuViewModel.stateText.observeAsState(initial = "")

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

            Button(
                onClick = {
                    addToMenuViewModel.drinkOrDishAlternator()
                },
                modifier = Modifier
            ) {
                Text(text = drinkOrDishText )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isDrink){
                textFieldsDrink(
                    navController,
                    addToMenuViewModel
                )
            } else
                textFieldsDish(
                    navController,
                    addToMenuViewModel
                )

            Spacer(modifier = Modifier.height(16.dp))

            if (stateText.isNotEmpty()) {
                Text(text = stateText, color = Color.Red)
            }
        }
    }

}

@Composable
fun textFieldsDrink(
    navController: NavHostController,
    addToMenuViewModel: AddToMenuViewModel
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var lastSelectedDrinkType by remember { mutableStateOf(DrinkTypes.Otro) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val permissionState = rememberPermissionState(
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    TextField(
        value = name,
        onValueChange = { name = it },
        label = { Text("Nombre") },
    )

    Spacer(modifier = Modifier.height(16.dp))

    TextField(
        value = price,
        onValueChange = { price = it },
        label = { Text("Precio") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    Spacer(modifier = Modifier.height(16.dp))

    LazyRow {
        items(addToMenuViewModel.drinkTypesArray) { drinkType ->
            Button(
                onClick = {
                    lastSelectedDrinkType = drinkType
                },
                colors = ButtonDefaults.buttonColors(if (drinkType == lastSelectedDrinkType) Color.Green else Color.Gray)
            ) {
                Text(text = drinkType.toString())
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    val context = LocalContext.current
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Button(
        onClick = {
            if (permissionState.status.isGranted) {
                pickImageLauncher.launch("image/*")
            } else {
                permissionState.launchPermissionRequest()
            }
        },
        modifier = Modifier
    ) {
        Text(text = "Seleccionar Imagen")
    }

    imageUri?.let {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Imagen seleccionada: ${it.path}")
    }

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = {
            addToMenuViewModel.saveDrink(name, price, lastSelectedDrinkType.toString(), imageUri)
            name = ""
            price = ""
            imageUri = null
        },
        modifier = Modifier
    ) {
        Text(text = "Añadir Bebida")
    }
}


@Composable
fun textFieldsDish(
    navController: NavHostController,
    addToMenuViewModel: AddToMenuViewModel
){
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var lastSelectedDishType by remember { mutableStateOf(DishTypes.Segundo) }
    var ingredientsNumber by remember { mutableStateOf("0") }
    var ingredientTexts by remember { mutableStateOf(List(0) { "" }) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    TextField(
        value = name,
        onValueChange = { name = it},
        label = { Text("Nombre") },
    )

    Spacer(modifier = Modifier.height(16.dp))

    TextField(
        value = price,
        onValueChange = { price = it },
        label = { Text("Precio") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    Spacer(modifier = Modifier.height(16.dp))

    TextField(
        value = ingredientsNumber,
        onValueChange = { newValue ->
            ingredientsNumber = if (newValue.isEmpty() || newValue.toIntOrNull() == null) {
                "0"
            } else {
                newValue
            }
            ingredientTexts = List(ingredientsNumber.toInt()) { "" }
        },
        label = { Text("Numero de Ingredientes") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    Spacer(modifier = Modifier.height(16.dp))

    for (i in ingredientTexts.indices) {
        TextField(
            value = ingredientTexts[i],
            onValueChange = { newValue ->
                ingredientTexts = ingredientTexts.toMutableList().also {
                    it[i] = newValue
                }
            },
            label = { Text("Ingrediente ${i + 1}") },
        )

        Spacer(modifier = Modifier.height(16.dp))
    }

    Spacer(modifier = Modifier.height(16.dp))

    LazyRow {
        items(addToMenuViewModel.dishTypesArray) { dishType ->
            Button(
                onClick = {
                    lastSelectedDishType = dishType
                },
                colors = ButtonDefaults.buttonColors(if (dishType == lastSelectedDishType) Color.Green else Color.Gray)
            ) {
                Text(text = dishType.toString())
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Implement image picker button
    val context = LocalContext.current
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Button(
        onClick = {
            pickImageLauncher.launch("image/*")
        },
        modifier = Modifier
    ) {
        Text(text = "Seleccionar Imagen")
    }

    imageUri?.let {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Imagen seleccionada: ${it.path}")
    }

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = {
            addToMenuViewModel.saveDish(name, price, lastSelectedDishType.toString(), ingredientTexts, imageUri)
            name = ""
            price = ""
            ingredientsNumber = "0"
            ingredientTexts = List(0) { "" }
            imageUri = null
        },
        modifier = Modifier
    ) {
        Text(text = "Añadir Plato")
    }
}

