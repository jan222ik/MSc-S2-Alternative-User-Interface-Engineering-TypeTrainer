package com.github.jan222ik.desktop.ui.dashboard.goal_preview

import com.github.jan222ik.desktop.ui.util.i18n.KeyI18N

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
