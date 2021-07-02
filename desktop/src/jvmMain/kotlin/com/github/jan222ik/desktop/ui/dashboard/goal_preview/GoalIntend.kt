package com.github.jan222ik.desktop.ui.dashboard.goal_preview

import com.github.jan222ik.desktop.ui.util.i18n.LocalTranslationI18N

class GoalIntend {
    fun getPreviewGoals(): List<MockGoal> {
        return listOf(
            MockGoal(
                current = 3f / 5f,
                max = 1f,
                name = LocalTranslationI18N(
                    eng = "Increase Typingspeed",
                    ger = "Geschwindigkeit erhöhen"
                ),
                timeframe = LocalTranslationI18N(
                    eng = "(5 Days to go)",
                    ger = "(Noch 5 Tage)"
                ),
                centerText = LocalTranslationI18N(
                    eng = "+3 / 5\nWPM",
                    ger = "+3 / 5\nWPM"
                )
            ),
            MockGoal(
                current = 0.87f,
                max = 1f,
                name = LocalTranslationI18N(
                    eng = "Finish 20 Exercises",
                    ger = "Beende 20 Übungen"
                ),
                timeframe = LocalTranslationI18N(
                    eng = "(1 week to go)",
                    ger = "(Noch 1 Woche)"
                ),
                centerText = LocalTranslationI18N(
                    eng = "87%",
                    ger = "87%"
                )
            )
        )
    }
}
