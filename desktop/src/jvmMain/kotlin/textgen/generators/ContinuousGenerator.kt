package textgen.generators

import com.github.jan222ik.common.HasDoc

@HasDoc
class ContinuousGenerator(
    private val generatorClosure: () -> String
) {
    fun generateSegment(): String = generatorClosure.invoke()
}
