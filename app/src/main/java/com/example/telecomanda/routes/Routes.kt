package com.example.telecomanda.routes

sealed class Routes(val route : String) {

    object InitialScreenRoute: Routes("InitialScreen")
    object AddToMenuScreenRoute: Routes("AddToMenu")
    object ConfigurationScreenRoute: Routes("ConfigurationScreen")
    object LogInAdministratorScreenRoute: Routes("LogInAdministrator")
    object LogInEmployeeScreenRoute: Routes("LogInEmployee")
    object LogInSelectorScreenRoute: Routes("LogInSelector")
    object RegisterScreenRoute: Routes("RegisterScreen")
    object AdminWorkScreenRoute: Routes("AdminWork")
    object EmployeeWorkScreenRoute: Routes("EmployeeWork")

    object ExampleScreenRoute: Routes("ExampleScreen")


}