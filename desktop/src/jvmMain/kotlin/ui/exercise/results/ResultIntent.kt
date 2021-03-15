package ui.exercise.results

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

class ResultIntent {

    companion object {
        fun getKeyPoints(): List<KeyPoint> {
            return listOf(
                KeyPoint("Words typed", "95", Icons.Filled.NorthEast),
                KeyPoint("Characters typed", "476", Icons.Filled.NorthEast),
                KeyPoint("Accuracy", "97.5%", Icons.Filled.North),
                KeyPoint("Errors", "12", Icons.Filled.SouthEast),
                KeyPoint("Error %", "2.5%", Icons.Filled.NorthEast),
                KeyPoint("Typing Errors", "8", Icons.Filled.Remove),
                KeyPoint("Typing Errors %", "1.6%", Icons.Filled.Remove),
                KeyPoint("Finger Errors", "4", Icons.Filled.South),
                KeyPoint("Finger Errors %", "0.8%", Icons.Filled.South)
            )
        }

        fun getAchievements(): List<List<String>> {
            return listOf(
                listOf("The Marksman", "Complete a practise with a accuracy above 97%"),
                listOf("On a roll", "https://youtu.be/dQw4w9WgXcQ"),
                listOf("Lorem ipsum", "Achieved when the developer runs out of ideas")
            )
        }
    }
}