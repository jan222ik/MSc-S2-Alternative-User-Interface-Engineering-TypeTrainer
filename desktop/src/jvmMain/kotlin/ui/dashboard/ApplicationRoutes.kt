package ui.dashboard

import textgen.error.ExerciseEvaluation
import ui.exercise.results.ResultsRoutes
import ui.exercise.ITypingOptions
import ui.util.i18n.KeyI18N
import ui.util.i18n.RequiresTranslationI18N
import ui.util.i18n.i18n

sealed class ApplicationRoutes(val title: KeyI18N) {

    object Dashboard : ApplicationRoutes(title = i18n.str.navigation.self.dashboard)

    object Settings : ApplicationRoutes(title = i18n.str.navigation.self.settings)

    sealed class User(title: KeyI18N) : ApplicationRoutes(title) {
        object Login : User(title = RequiresTranslationI18N("Login"))

        data class AccountManagement(val user: Any) : User(title = RequiresTranslationI18N("Account Management"))
    }

    sealed class Exercise(title: KeyI18N) : ApplicationRoutes(title) {

        object ExerciseSelection : Exercise(title = RequiresTranslationI18N("Exercise - Selection"))


        sealed class Connection(title: KeyI18N) : Exercise(title) {
            object QRCode : Connection(title = RequiresTranslationI18N("Connection with Companion"))
            object SetupInstructions : Connection(title = RequiresTranslationI18N("Exercise - Setup Camera"))
        }

        data class Training(
            val trainingOptions: ITypingOptions
        ) : Exercise(title = RequiresTranslationI18N("Exercise - Training"))

        data class ExerciseResults(
            val exerciseResults: ExerciseEvaluation,
            val initialPage: ResultsRoutes = ResultsRoutes.OVERVIEW
        ) : Exercise(title = RequiresTranslationI18N("Exercise - Results"))
    }

    sealed class Goals(title: KeyI18N) : ApplicationRoutes(title) {
        object Overview : Goals(title = RequiresTranslationI18N("Your Goals"))
        object Compose : Goals(title = RequiresTranslationI18N("Create a Goal"))
    }

    object Achievements : ApplicationRoutes(RequiresTranslationI18N("Achievements"))

    sealed class Competitions(title: KeyI18N) : ApplicationRoutes(title) {
        object Overview : Competitions(title = RequiresTranslationI18N("Competitions - Overview"))
    }

    object History : ApplicationRoutes(title = RequiresTranslationI18N("History"))

    object AppBenefits : ApplicationRoutes(title = i18n.str.navigation.self.app_benefits)

    object Debug : ApplicationRoutes(title = RequiresTranslationI18N("Debug"))
}
