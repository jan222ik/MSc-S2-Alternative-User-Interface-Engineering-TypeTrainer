package ui.dashboard.goal_preview

import kotlin.random.Random

class GoalIntend {
    fun getPreviewGoals(): List<IGoal> {
        val rng = { Random.nextDouble(0.0, 1.0) }
        return listOf(
            MockGoal(
                current = rng.invoke().toFloat(),
                max = 1f,
                name = "Mock Goal 1"
            ),
            MockGoal(
                current = rng.invoke().toFloat(),
                max = 1f,
                name = "Mock Goal 2"
            )
        )
    }
}
