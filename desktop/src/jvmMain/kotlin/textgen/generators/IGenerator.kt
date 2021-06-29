package textgen.generators

interface IGenerator<T : AbstractGeneratorOptions> {
    fun create(options: T): ContinuousGenerator
}
