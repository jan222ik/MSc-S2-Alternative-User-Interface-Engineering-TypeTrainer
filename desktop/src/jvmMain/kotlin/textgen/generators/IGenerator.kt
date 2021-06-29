package textgen.generators

import com.github.jan222ik.common.HasDoc

@HasDoc
interface IGenerator<T : AbstractGeneratorOptions> {
    fun create(options: T): ContinuousGenerator
}
