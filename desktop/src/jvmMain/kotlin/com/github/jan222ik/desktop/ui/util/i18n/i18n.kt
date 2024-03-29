package com.github.jan222ik.desktop.ui.util.i18n


@Suppress("ClassName", "MemberVisibilityCanBePrivate")
sealed class i18n {
    fun String.i18Key() = KeyI18N("$path.$this")

    abstract val path: String

    sealed class str : i18n() {

        object app : str() {
            override val path: String = "app"

            val name: KeyI18N = "app_name".i18Key()
        }

        object debug : str() {
            override val path: String = "debug"
            val notImplementedYet: KeyI18N = "not_implemented_yet".i18Key()
        }

        object history : str() {
            override val path: String = "history"

            val today: KeyI18N = "today".i18Key()
            val yesterday: KeyI18N = "yesterday".i18Key()
            val thisWeek: KeyI18N = "thisWeek".i18Key()
            val lastWeek: KeyI18N = "lastWeek".i18Key()
            val lastMonth: KeyI18N = "lastMonth".i18Key()
            val older: KeyI18N = "older".i18Key()
        }

        sealed class dashboard : str() {
            override val path: String = "dashboard"

            object weeklyChart : dashboard() {
                override val path: String = super.path + ".weekly_chart"

                val daysOfWeek: KeyI18N = "days_of_week".i18Key()
                val chartsTitle: KeyI18N = "title".i18Key()
                val showMore: KeyI18N = "show_more".i18Key()
                val description: KeyI18N = "description".i18Key()
            }

            object goals : dashboard() {
                override val path: String = super.path + ".goals"
                val goalsTitle: KeyI18N = "title".i18Key()
                val openAll: KeyI18N = "open_all".i18Key()
                val composeNew: KeyI18N = "compose_new".i18Key()
            }

            object streak : dashboard() {
                override val path: String = super.path + ".streak"
                val streakTitle: KeyI18N = "title".i18Key()
                val days: KeyI18N = "days".i18Key()
            }
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
                val exerciseSelection: KeyI18N = "exercise_selection".i18Key()
            }
        }

        sealed class camera : str() {
            override val path = "camera"

            object setup : camera() {
                override val path = super.path + ".setup"
                val instruction_header = "instruction_header".i18Key()
                val first_point = "first_point".i18Key()
                val second_point = "second_point".i18Key()
                val third_point = "third_point".i18Key()
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
                fun getAll() = listOf(eng, ger)
            }
        }

        sealed class exercise : str() {
            override val path: String = "exercise"

            sealed class selection : exercise() {
                override val path: String = super.path + ".selection"

                object controls : selection() {
                    override val path: String = super.path + ".controls"
                    val useHandTracking = "use_hand_tracking".i18Key()
                    val startBtn = "start_exercise".i18Key()
                    val startingHint = "starting_hint".i18Key()
                }

                object textMode : selection() {
                    override val path: String = super.path + ".textMode"
                    val textMode = "text_mode".i18Key()
                    val literature = "literature".i18Key()
                    val literatureDescription = "literature_description".i18Key()
                    val randomWords = "rng_words".i18Key()
                    val randomWordsDescription = "rng_words_description".i18Key()
                    val randomChars = "rng_chars".i18Key()
                    val randomCharsDescription = "rng_chars_description".i18Key()
                    fun getAll() = listOf(literature, randomWords, randomChars)
                }

                object exerciseMode : selection() {
                    override val path: String = super.path + ".exerciseMode"

                    val exerciseMode = "exercise_mode".i18Key()
                    val duration = "duration".i18Key()
                    val oneMin = "one_min".i18Key()
                    val twoMin = "two_min".i18Key()
                    val customDuration = "custom_duration".i18Key()
                    val enterDuration = "enter_duration".i18Key()
                    val customDurationError = "custom_duration_error".i18Key()
                    val timelimit = "timelimit".i18Key()
                    val timelimitDescription = "timelimit_description".i18Key()
                    val accuracy = "accuracy".i18Key()
                    val accuracyDescription = "accuracy_description".i18Key()
                    val noTimeLimit = "no_time_limit".i18Key()
                    val noTimeLimitDescription = "no_time_limit_description".i18Key()

                    fun getAll() = listOf(timelimit, noTimeLimit)
                    fun getDurations() = listOf(oneMin, twoMin, customDuration)
                }

                object typingType : selection() {
                    override val path: String = super.path + ".typingType"

                    val typingType: KeyI18N = "typing_type".i18Key()
                    val movingCursor: KeyI18N = "moving_cursor".i18Key()
                    val movingCursorDescription: KeyI18N = "moving_cursor_description".i18Key()
                    val movingText: KeyI18N = "moving_text".i18Key()
                    val movingTextDescription: KeyI18N = "moving_text_description".i18Key()

                    fun getAll() = listOf(movingCursor, movingText)
                }
            }

            sealed class results : exercise() {
                override val path: String = super.path + ".results"

                object base : results() {
                    override val path: String = super.path + ".base"
                    val returnToDashboard = "returnToDashboard".i18Key()
                    val overview = "overview".i18Key()
                    val analysis = "analysis".i18Key()
                    val errorHeatmap = "error_heatmap".i18Key()
                    val timeline = "timeline".i18Key()
                }

                object overview : results() {
                    override val path: String = super.path + ".overview"
                    val keyPoints: KeyI18N = LocalTranslationI18N("KeyPoints:", "Eckpunkte:")
                    val goals: KeyI18N = LocalTranslationI18N("Goals:", "Ziele:")
                    val achievements: KeyI18N = LocalTranslationI18N("Achievements:", "Errungenschaften:")
                }

                object overview_keypoints : results() {
                    override val path: String = super.path + ".overview_keypoints"
                    val wordsTyped: KeyI18N = LocalTranslationI18N("Words Typed:", "Getippte Wörter:")
                    val charsTyped: KeyI18N = LocalTranslationI18N("Chars Typed:", "Getippte Buchstaben:")
                    val wpm: KeyI18N = LocalTranslationI18N("Words Per Minute (WPM):", "Wörter pro Minute (WPM):")
                    val cpm: KeyI18N = LocalTranslationI18N("Chars Per Minute (CPM):", "Buchstaben pro Minute (BPM):")
                    val totalErrors: KeyI18N = LocalTranslationI18N("Total Errors:", "Gesamtfehler:")
                    val accuracy: KeyI18N = LocalTranslationI18N("Accuracy:", "Genauigkeit")
                    val typingErrors: KeyI18N = LocalTranslationI18N("Typing Errors:", "Tippfehler:")

                    val typingErrorsPercentage: KeyI18N = LocalTranslationI18N("Typing Errors in %:", "Tippfehler in %:")
                    val typingErrorsCase: KeyI18N = LocalTranslationI18N("Char Case:", "Groß-/Kleinschreibung:")
                    val typingErrorsCasePercentatge: KeyI18N = LocalTranslationI18N("Char Case in %:", "Groß-/Kleinschreibung in %:")
                    val typingErrorsWhitespace: KeyI18N = LocalTranslationI18N("Whitespace:", "Leerraum:")
                    val typingErrorsWhitespacePercentatge: KeyI18N = LocalTranslationI18N("Whitespace in %:", "Leerraum in %:")
                    val fingerErrors: KeyI18N = LocalTranslationI18N("Finger Errors:", "Fingerfehler:")
                    val fingerErrorsPercentage: KeyI18N = LocalTranslationI18N("Finger Errors in %:", "Fingerfehler in %:")
                }
            }

            object connection : exercise() {
                override val path: String = super.path + ".connection"

                val ndsStarting: KeyI18N = "nds_starting".i18Key()
                val ndsStarted: KeyI18N = "nds_started".i18Key()
                val continueWithoutHandtracking: KeyI18N = "continue_without_handtracking".i18Key()

            }

            object keyboard_sync : exercise() {
                override val path: String = super.path + ".keyboard_synchronisation"

                val step1: KeyI18N = LocalTranslationI18N(
                    "1. Place your camera above the keyboard", "1. Plaziere die Kamera überhalb der Tastatur")
                val step2: KeyI18N =
                    LocalTranslationI18N("2. Press the <span>'ESC'<\\span> key with your <span>left pinky finger<\\span>",
                        "2. Drücke die <span>'ESC'<\\span> Taste mit deinem <span>linken kleinen Finger<\\span>")
                val step3: KeyI18N =
                    LocalTranslationI18N("3. Press the <span>right 'CTRL'<\\span> key with your <span>right pinky finger<\\span>",
                        "3. Drücke die <span>rechte 'STRG'<\\span> Taste mit deinem <span>rechten kleinen Finger<\\span>")
            }
        }

    }
}
