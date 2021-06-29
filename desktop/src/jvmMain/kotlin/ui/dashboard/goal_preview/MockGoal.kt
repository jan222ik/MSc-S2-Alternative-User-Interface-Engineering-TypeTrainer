package ui.dashboard.goal_preview

import ui.util.i18n.KeyI18N

data class MockGoal(
    val current: Float,
    val max: Float,
    val name: KeyI18N,
    val timeframe: KeyI18N,
    val centerText: KeyI18N
) {
    val progress: Float
        get() = current / max
}
