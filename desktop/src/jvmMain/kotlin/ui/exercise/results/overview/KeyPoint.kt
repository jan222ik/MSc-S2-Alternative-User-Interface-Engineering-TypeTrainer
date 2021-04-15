package ui.exercise.results.overview

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.East
import androidx.compose.material.icons.filled.North
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.South
import androidx.compose.material.icons.filled.SouthEast
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


