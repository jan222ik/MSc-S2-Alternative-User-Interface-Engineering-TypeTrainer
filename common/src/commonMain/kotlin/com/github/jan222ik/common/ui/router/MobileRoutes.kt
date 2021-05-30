package com.github.jan222ik.common.ui.router

sealed class MobileRoutes {
    object Menu : MobileRoutes()
    object Scanner : MobileRoutes()
    object CameraSetup : MobileRoutes()
    object AppBenefits : MobileRoutes()
    data class Exercise(val connection: Connection) : MobileRoutes()
}

data class Connection(val canConnect: Boolean = false)
