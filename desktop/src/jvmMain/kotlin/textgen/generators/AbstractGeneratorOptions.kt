package textgen.generators

import kotlinx.serialization.Serializable

@Serializable
abstract class AbstractGeneratorOptions {
    abstract val seed: Long
}
