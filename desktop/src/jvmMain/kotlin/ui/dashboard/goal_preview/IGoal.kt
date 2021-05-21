package ui.dashboard.goal_preview

interface IGoal {
    val current: Float
    val max: Float
    val name: String

    val progress: Float
        get() = current / max
}
