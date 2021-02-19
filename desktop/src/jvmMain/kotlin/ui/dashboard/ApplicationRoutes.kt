package ui.dashboard

import ui.util.i18n.RequiresTranslationI18N
import ui.util.i18n.KeyI18N
import ui.util.i18n.i18n

sealed class ApplicationRoutes(val title: KeyI18N) {
    object Dashboard : ApplicationRoutes(title = RequiresTranslationI18N("Dashboard"))
    object Compose : ApplicationRoutes(title = RequiresTranslationI18N("Create new Dive Entry"))
    object Settings : ApplicationRoutes(title = i18n.str.navigation.self.settings)
    object Pictures : ApplicationRoutes(title = RequiresTranslationI18N("Pictures"))
    object Logbook : ApplicationRoutes(title = RequiresTranslationI18N("Logbook"))
    object ExerciseSelection : ApplicationRoutes(title = RequiresTranslationI18N("Exercise Selection"))
}
