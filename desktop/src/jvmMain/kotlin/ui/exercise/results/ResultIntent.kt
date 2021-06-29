package ui.exercise.results

import textgen.error.ExerciseEvaluation

class ResultIntent(
    internal val data: ExerciseEvaluation
) {

    companion object {

        fun getAchievements(): List<List<String>> {
            return listOf(
                listOf("The Marksman", "Complete a practise with a accuracy above 97%"),
                listOf("On a roll", "https://youtu.be/dQw4w9WgXcQ"),
                listOf("Lorem ipsum", "Achieved when the developer runs out of ideas")
            )
        }
    }
}
