package com.example.telecomanda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.telecomanda.routes.Routes
import com.example.telecomanda.screens.addToMenu.AddToMenu
import com.example.telecomanda.screens.addToMenu.AddToMenuViewModel
import com.example.telecomanda.screens.configurationScreen.ConfigurationScreen
import com.example.telecomanda.screens.initialScreen.InitialScreen
import com.example.telecomanda.screens.logIn.LogInAdministrator
import com.example.telecomanda.screens.logIn.LogInEmployee
import com.example.telecomanda.screens.logIn.LogInSelector
import com.example.telecomanda.screens.registerScreen.RegisterScreen
import com.example.telecomanda.screens.registerScreen.RegisterScreenViewModel
import com.example.telecomanda.screens.workScreens.AdminWork
import com.example.telecomanda.screens.workScreens.EmployeeWork
import com.example.telecomanda.ui.theme.TeleComandaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // VIEWMODELS
        val addToMenuViewModel = AddToMenuViewModel()
        val registerScreenViewModel = RegisterScreenViewModel()

        setContent {
            TeleComandaTheme {
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
                                navController
                            )
                        }

                        composable(Routes.LogInAdministratorScreenRoute.route) {
                            LogInAdministrator(
                                navController
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

                    }

                }
            }
        }
    }
}