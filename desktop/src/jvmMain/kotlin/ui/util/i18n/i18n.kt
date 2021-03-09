package ui.util.i18n


@Suppress("ClassName", "MemberVisibilityCanBePrivate")
sealed class i18n {
    fun String.i18Key() = KeyI18N("$path.$this")

    abstract val path: String

    sealed class str : i18n() {

        object app : str() {
            override val path: String = "app"

            val name: KeyI18N = "app_name".i18Key()
        }

        sealed class navigation : str() {
            override val path: String = "navigation"

            object self : navigation() {
                val dashboard: KeyI18N = "dashboard".i18Key()
                val settings: KeyI18N = "settings".i18Key()
                val practice: KeyI18N = "practice".i18Key()
                val competition: KeyI18N = "competition".i18Key()
                val history: KeyI18N = "history".i18Key()
                val achievements: KeyI18N = "achievements".i18Key()
                val app_benefits: KeyI18N = "app_benefits".i18Key()
                val camera_setup: KeyI18N = "camera_setup".i18Key()
            }
        }

        sealed class settings : str() {
            override val path = "settings"

            object quickies : settings() {
                override val path = super.path + ".quickies"
                val action_open_settings = "action_open_settings".i18Key()
                val title = "quick_settings".i18Key()
            }

            /**
             * Known Languages to the program
             */
            object languages : settings() {
                override val path = super.path + ".languages"
                val language = "language_label".i18Key()
                val eng = "english".i18Key()
                val ger = "german".i18Key()

            }
        }

        sealed class exercise : str() {
            override val path: String = "exercise"

            sealed class selection : exercise() {
                override val path: String = super.path + ".selection"

                object textMode : selection() {
                    override val path: String = super.path + ".textMode"
                    val literature = "literature".i18Key()
                    val literatureDescription = "literature_description".i18Key()
                    val randomWords = "rng_words".i18Key()
                    val randomChars = "rng_chars".i18Key()
                    fun getAll() = listOf(literature, randomWords, randomChars)
                }

                object exerciseMode : selection() {
                    override val path: String = super.path + ".exerciseMode"
                    val speed = "speed".i18Key()
                    val accuracy = "accuracy".i18Key()
                    val noTimeLimit = "no_time_limit".i18Key()
                    fun getAll() = listOf(speed, accuracy, noTimeLimit)
                }
            }
        }

    }
}
