package com.example.telecomanda

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.telecomanda.routes.Routes
import com.example.telecomanda.screens.addEmployee.AddEmployee
import com.example.telecomanda.screens.addEmployee.AddEmployeeViewModel
import com.example.telecomanda.screens.addOrder.AddOrder
import com.example.telecomanda.screens.addOrder.AddOrderViewModel
import com.example.telecomanda.screens.addOrder.TableSelectionScreen
import com.example.telecomanda.screens.addToMenu.AddToMenu
import com.example.telecomanda.screens.addToMenu.AddToMenuViewModel
import com.example.telecomanda.screens.clientScreens.ClientOrderScreen
import com.example.telecomanda.screens.clientScreens.TableCodeInputScreen
import com.example.telecomanda.screens.clientScreens.TableCodeInputViewModel
import com.example.telecomanda.screens.configurationScreen.ConfigurationScreen
import com.example.telecomanda.screens.initialScreens.InitialScreen
import com.example.telecomanda.screens.initialScreens.WorkerSelectionScreen
import com.example.telecomanda.screens.logIn.LogInAdministrator
import com.example.telecomanda.screens.logIn.LogInAdministratorViewModel
import com.example.telecomanda.screens.logIn.LogInEmployee
import com.example.telecomanda.screens.logIn.LogInEmployeeViewModel
import com.example.telecomanda.screens.logIn.LogInSelector
import com.example.telecomanda.screens.menuScreen.MenuScreen
import com.example.telecomanda.screens.menuScreen.MenuScreenViewModel
import com.example.telecomanda.screens.registerScreen.RegisterScreen
import com.example.telecomanda.screens.registerScreen.RegisterScreenViewModel
import com.example.telecomanda.screens.tableQuantityController.TableQuantityControllerScreen
import com.example.telecomanda.screens.tableQuantityController.TableQuantityControllerViewModel
import com.example.telecomanda.screens.workScreens.AdminWork
import com.example.telecomanda.screens.workScreens.EmployeeWork
import com.example.telecomanda.ui.theme.TeleComandaTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // VIEWMODELS
        val addToMenuViewModel = AddToMenuViewModel()
        val tableCodeInputViewModel = TableCodeInputViewModel()
        val addEmployeeViewModel = AddEmployeeViewModel()
        val registerScreenViewModel = RegisterScreenViewModel()
        val logInAdministratorViewModel = LogInAdministratorViewModel()
        val menuScreenViewModel = MenuScreenViewModel()
        val logInEmployeeViewModel = LogInEmployeeViewModel()
        val addOrderViewModel = AddOrderViewModel()
        val tableQuantityControllerViewModel = TableQuantityControllerViewModel()

        //navController.popBackStack()

        setContent {
            TeleComandaTheme {

                val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

                LaunchedEffect(Unit) {
                    permissionState.launchPermissionRequest()
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Routes.InitialScreenRoute.route
                    ) {



                        composable(Routes.InitialScreenRoute.route) {
                            InitialScreen(
                                navController
                            )
                        }
                        composable(Routes.WorkerSelectionScreenRoute.route) {
                            WorkerSelectionScreen(
                                navController
                            )
                        }
                        composable(Routes.TableCodeInputScreenRoute.route) {
                            TableCodeInputScreen(
                                navController,
                                tableCodeInputViewModel
                            )
                        }

                        composable("clientOrderScreen/{tableCode}/{restaurantEmail}") { backStackEntry ->
                            val tableCode = backStackEntry.arguments?.getString("tableCode") ?: ""
                            val restaurantEmail = backStackEntry.arguments?.getString("restaurantEmail") ?: ""
                            ClientOrderScreen(tableCode, restaurantEmail)
                        }

                        composable(Routes.RegisterScreenRoute.route) {
                            RegisterScreen(
                                navController,
                                registerScreenViewModel
                            )
                        }

                        composable(Routes.LogInSelectorScreenRoute.route) {
                            LogInSelector(
                                navController
                            )
                        }

                        composable(Routes.LogInEmployeeScreenRoute.route) {
                            LogInEmployee(
                                navController,
                                logInEmployeeViewModel
                            )
                        }

                        composable(Routes.LogInAdministratorScreenRoute.route) {
                            LogInAdministrator(
                                navController,
                                logInAdministratorViewModel
                            )
                        }

                        composable(Routes.AdminWorkScreenRoute.route) {
                            AdminWork(
                                navController
                            )
                        }

                        composable(Routes.EmployeeWorkScreenRoute.route) {
                            EmployeeWork(
                                navController
                            )
                        }

                        composable(Routes.ConfigurationScreenRoute.route) {
                            ConfigurationScreen(
                                navController
                            )
                        }

                        composable(Routes.AddToMenuScreenRoute.route) {
                            AddToMenu(
                                navController,
                                addToMenuViewModel
                            )
                        }

                        composable(Routes.AddEmployeeScreenRoute.route) {
                            AddEmployee(
                                navController,
                                addEmployeeViewModel
                            )
                        }

                        composable(Routes.MenuScreenRoute.route) {
                            MenuScreen(
                                navController,
                                menuScreenViewModel
                            )
                        }

                        composable(Routes.TableSelecctionScreenRoute.route) {
                            TableSelectionScreen(
                                navController,
                                addOrderViewModel
                            )
                        }

                        composable("addOrder/{tableNumber}") { backStackEntry ->
                            val tableNumber = backStackEntry.arguments?.getString("tableNumber")?.toInt() ?: 1
                            AddOrder(tableNumber, navController, addOrderViewModel)
                        }

                        composable(Routes.TableQuantityControllerScreenRoute.route) {
                            TableQuantityControllerScreen(
                                navController,
                                tableQuantityControllerViewModel
                            )
                        }

                    }

                }
            }
        }
    }
}
