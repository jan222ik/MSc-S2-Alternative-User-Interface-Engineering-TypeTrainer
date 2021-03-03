package textgen.generators

interface IGenerator<T : IGeneratorOptions> {
    fun create(options: T): ContinuousGenerator
}
