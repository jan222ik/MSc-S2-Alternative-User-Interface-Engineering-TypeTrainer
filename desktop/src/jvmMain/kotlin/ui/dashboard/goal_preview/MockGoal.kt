package ui.dashboard.goal_preview

data class MockGoal(
    override val current: Float,
    override val max: Float,
    override val name: String
) : IGoal
