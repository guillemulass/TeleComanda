package com.example.telecomanda.routes

sealed class Routes(val route : String) {

    object InitialScreenRoute: Routes("InitialScreen")
    object WorkerSelectionScreenRoute: Routes("WorkerSelectionScreen")
    object TableCodeInputScreenRoute: Routes("TableCodeInputScreen")
    object ClientOrderScreenRoute: Routes("ClientOrderScreen")

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
    object TableQuantityControllerScreenRoute: Routes("TableQuantityControllerScreen")

    object ExampleScreenRoute: Routes("ExampleScreen")


}