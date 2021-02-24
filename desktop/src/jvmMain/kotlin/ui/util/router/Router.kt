package ui.util.router

interface Router<T> {
    val current: T
    val previous: Router<T>?

    fun navTo(dest: T)
    fun hasBackDestination(): Boolean
    fun back(): Boolean
}

internal data class RouterImpl<T>(
    override val current: T,
    override val previous: Router<T>? = null
) : Router<T> {
    var setRouter: (Router<T>) -> Unit = {}

    override fun navTo(dest: T) {
        val nextRouter = RouterImpl(
            current = dest,
            previous = this
        )
        nextRouter.setRouter = setRouter
        setRouter.invoke(nextRouter)
    }

    override fun hasBackDestination() = previous != null

    override fun back(): Boolean {
        return if (previous != null) {
            setRouter(previous)
            true
        } else false
    }
}
