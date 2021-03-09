package ui.util.debug

object Debug {
    private const val debugPropName = "debug"
    private const val debugPropTrueValue = "true"
    val isDebug by lazy { System.getProperty(debugPropName) == debugPropTrueValue }
}

fun ifDebug(content: () -> Unit) {
    if (Debug.isDebug) {
        content.invoke()
    }
}
