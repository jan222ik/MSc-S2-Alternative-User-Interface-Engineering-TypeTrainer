package ui.temp_mobile.router

sealed class MobileRoutes {
    object Menu : MobileRoutes()
    object Scanner : MobileRoutes()
    object CameraSetup : MobileRoutes()
    object AppBenefits : MobileRoutes()
    data class Exercise(val connection: Connection) : MobileRoutes()
}

data class Connection(val stuff: Any = Any())
