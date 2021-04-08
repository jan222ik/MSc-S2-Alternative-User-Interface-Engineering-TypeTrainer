package ui.exercise.results

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import util.RandomUtil


data class KeyPoint(
    val name: String,
    val value: String
) {
    companion object {
        private val images = listOf(Icons.Filled.North, Icons.Filled.NorthEast, Icons.Filled.East, Icons.Filled.SouthEast, Icons.Filled.South)
        private val random = RandomUtil.nextIntInRemBoundAsClosure(1L, images.size)
    }

    val icon: ImageVector by lazy {
        images[random()]
    }
}


