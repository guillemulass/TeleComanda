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
    object MenuScreenRoute: Routes("MenuScreen")
    object AddEmployeeScreenRoute: Routes("AddEmployeeScreen")
    object AddOrderScreenRoute: Routes("AddOrderScreen")
    object TableSelecctionScreenRoute: Routes("TableSelectionScreen")

    object ExampleScreenRoute: Routes("ExampleScreen")


}