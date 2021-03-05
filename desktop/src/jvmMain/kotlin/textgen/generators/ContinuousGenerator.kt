package textgen.generators

class ContinuousGenerator(
    private val generatorClosure: () -> String
) {
    fun generateSegment(): String = generatorClosure.invoke()
}
