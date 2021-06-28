package ui.dashboard

import textgen.error.ExerciseEvaluation
import ui.exercise.AbstractTypingOptions
import ui.exercise.results.ResultsRoutes
import ui.util.i18n.KeyI18N
import ui.util.i18n.LocalTranslationI18N
import ui.util.i18n.RequiresTranslationI18N
import ui.util.i18n.i18n
import util.FingerMatcher

sealed class ApplicationRoutes(val title: KeyI18N) {

    object Dashboard : ApplicationRoutes(title = i18n.str.navigation.self.dashboard)

    object Settings : ApplicationRoutes(title = i18n.str.navigation.self.settings)

    sealed class User(title: KeyI18N) : ApplicationRoutes(title) {
        object Login : User(title = LocalTranslationI18N("Login", "Login"))

        data class AccountManagement(val user: Any) : User(title = LocalTranslationI18N("Account Management", "Kontoverwaltung"))
    }

    sealed class Exercise(title: KeyI18N) : ApplicationRoutes(title) {

        data class ExerciseSelection(val initData: AbstractTypingOptions? = null) : Exercise(title = i18n.str.navigation.self.exerciseSelection)


        sealed class Connection(title: KeyI18N) : Exercise(title) {
            data class SetupConnection(val trainingOptions: AbstractTypingOptions) : Connection(title = LocalTranslationI18N("Connection with Companion", "Verbindung mit Begleitungsapp"))
            data class SetupInstructions(val trainingOptions: AbstractTypingOptions?) : Connection(title = LocalTranslationI18N("Exercise - Setup Camera", "Übung - Kamera einrichten"))
            data class KeyboardSynchronisation(val trainingOptions: AbstractTypingOptions) : Connection(title = LocalTranslationI18N("Exercise - Synchronisation of Camera and Keyboard", "Übung - Synchronisation von Kamera und Tastatur"))
        }

        data class Training(
            val trainingOptions: AbstractTypingOptions,
            val fingerMatcher: FingerMatcher? = null
        ) : Exercise(title = LocalTranslationI18N("Exercise - Training", "Übung - Training"))

        data class ExerciseResults(
            val exerciseResults: ExerciseEvaluation,
            val initialPage: ResultsRoutes = ResultsRoutes.OVERVIEW
        ) : Exercise(title = LocalTranslationI18N("Exercise - Results", "Übung - Ergebnisse"))
    }

    sealed class Goals(title: KeyI18N) : ApplicationRoutes(title) {
        object Overview : Goals(title = LocalTranslationI18N("Your Goals", "Deine Ziele"))
        object Compose : Goals(title = LocalTranslationI18N("Compose a new Goal", "Ein neues Ziel anlegen"))
    }

    object Achievements : ApplicationRoutes(LocalTranslationI18N("Achievements", "Errungenschaften"))

    sealed class Competitions(title: KeyI18N) : ApplicationRoutes(title) {
        object Overview : Competitions(title = LocalTranslationI18N("Competitions - Overview", "Wettkämpfe - Übersicht"))
    }

    object History : ApplicationRoutes(title = i18n.str.navigation.self.history)

    object AppBenefits : ApplicationRoutes(title = i18n.str.navigation.self.app_benefits)

    object Debug : ApplicationRoutes(title = LocalTranslationI18N("Debug", "Debug"))
}
