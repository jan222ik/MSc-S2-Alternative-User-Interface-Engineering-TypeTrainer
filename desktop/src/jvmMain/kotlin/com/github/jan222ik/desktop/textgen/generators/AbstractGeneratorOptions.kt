package com.github.jan222ik.desktop.textgen.generators

import com.github.jan222ik.common.HasDoc
import kotlinx.serialization.Serializable

@Serializable
@HasDoc
abstract class AbstractGeneratorOptions {
    abstract val seed: Long
}
