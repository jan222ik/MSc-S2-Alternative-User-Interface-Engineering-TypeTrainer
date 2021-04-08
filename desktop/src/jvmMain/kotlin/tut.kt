@file:Suppress(
    "unused", "UNUSED_VARIABLE", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER",
    "CanBeParameter"
)

typealias ReturnValue = String
typealias ReturnValueOptional = String

/**
 * Functions
 */

fun nameOfFunction0(arg0: String, arg1: Int) {

}

fun name(arg0: String, arg1: Int = 0) {

}

fun callName() {
    // Normal Call (Positional Argument)
    name("arg0")

    // Named Argument
    name(arg0 = "arg0")

    // Both with positional argument
    name("arg0", 123)

    // Both named args -> Order may be changed
    name(arg1 = 123, arg0 = "arg0")


    //  .. in theory you can mix positional and named args
}

fun nameOfFunction1(): ReturnValue {
    return ReturnValue()
}

// With return value
fun nameOfFunction2(): ReturnValueOptional = ReturnValueOptional()

// Without return value
fun nameOfFunction3() = ReturnValueOptional()

/**
 * Variables
 */

fun variables() {
    var mutableVariable: String = ""
    mutableVariable = "test"

    val immutableVariable: String = ""
    //immutableVariable = "test"
}

/**
 * Classes
 */

// Interface
interface Test {
    fun fromInterface(arg0: Int)
}

abstract class AbstractTestClass {
    abstract fun formAbstractClass(arg0: String)
}

class TestClassImpl : AbstractTestClass(), Test {
    override fun fromInterface(arg0: Int) {
        TODO("Not yet implemented")
    }

    override fun formAbstractClass(arg0: String) {
        TODO("Not yet implemented")
    }
}

data class EntityClass(val someValue: Int)

enum class TestEnum {
    TEST, ENTITY, UNIT
}

/**
 * Constructors
 */

data class EntityClass2(
    val someValue: Int
)

class Test2 private constructor(val arg0: String, val arg1: Int)

// Constructor extention with init

class Test3(
    private val arg0: String = "test"
) {

    @Suppress("JoinDeclarationAndAssignment")
    var knownToInit: String

    init {
        knownToInit = arg0
        // val len = unknownToTopInit.length
    }

    val unknownToTopInit: String = "unknownToTop"

    init {
        val len = unknownToTopInit.length
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

        }
    }
}

fun callConstructor() {
    val entityClass = EntityClass(someValue = 13)
    val test3 = Test3() // Has default thus it does not need to be defined here
}


/**
 * Special Classes
 */
// Objects - aka. Singeltons in JVM static contexts
object OnlyExistsOnce {
    const val name = "name of application"

    fun test()  {}
}

fun callObject() {
    val a: OnlyExistsOnce = OnlyExistsOnce
    a.test()
    OnlyExistsOnce.name
    OnlyExistsOnce.test()
}

// Sealed Classes
typealias ArticleID = Long

sealed class Navigation {

    object Menu : Navigation()
    object Settings : Navigation()

    data class Article(val id: ArticleID) : Navigation()

    object AdditionalDestination: Navigation()

}



@Suppress("MoveVariableDeclarationIntoWhen")
fun `UI - Router Example`() {
    val currentRouting: Navigation = Navigation.Menu
    when (currentRouting) {
        Navigation.Menu -> TODO("Show menu")
        Navigation.Settings -> TODO("Show Settings")
        is Navigation.Article -> {
            val articleId = currentRouting.id // Gets smart casted, only possible with sealed class
            TODO("Show article with id")
        }
        Navigation.AdditionalDestination -> TODO()
    }
}

/**
 * Types and Null
 */
data class Wrapper(val value: Int)

@Suppress("RedundantExplicitType")
fun types() {
    // Any is Java Object
    val notNull: Any = Any()
    val nullable: Any? = null

    // Handle Null
    val wrapped: Wrapper? = null

    val unwrappedValue: Int? = wrapped?.value
    // What type is above ?

    // If you want a default value for missing use operator ?:
    val unwrappedValueOrDefault: Int = wrapped?.value ?: 0

    // You know it cant be null but compiler does not, if wrong NPE thrown
    val unwrappedValueThrowsNPE = wrapped!!.value
}

typealias Element = String

fun javaTypesTranslated() {
    // Object
    val objekt = Any()
    // Text
    val char = Char
    val string = String
    // Numbers
    val int = Int
    val float = Float
    val double = Double

    // Exception
    class E0 : Error("msg")
    class E1 : RuntimeException("msg")

    try {
        throw E0()
    } catch (e: E0) {
        // Caught
    }

    // Exception Control Flow
    val nothing = Nothing::class
    // Allows this
    run {
        val parsedOrDefault: Int = try {
            "2".toInt()
        } catch (nfe: NumberFormatException) {
            0
        }

        val v: Int = if (true) {0} else {1}

        // Takes when true
        val v0 = 0.takeIf { 0 == it } ?: 1
        // Takes when false
        val v1 = 0.takeUnless { 0 == it } ?: 1
    }

    // Array, always immutable
    val array: Array<Element> = arrayOf(Element(), Element(), Element())

    // Linked List
    //      immutable
    val list0 = listOf<Element>(Element(), Element(), Element())
    //      mutable
    val list1 = mutableListOf<Element>(Element(), Element(), Element())
    list1.add(Element())

    // ArrayList use arrayListOf

    // Map
    val mapOf = mutableMapOf<String, Element>(
        "key" to Element()
    )
    mapOf.put(key = "test", value = Element())
    mapOf["test"] = Element()

    // ... see pattern for all other JVM known types
}



/**
 * Lambdas
 */
fun functionName() : Boolean {
    return true
}

class L {
    fun functionName() : Boolean = true
}

fun labdas(
    predicate: () -> Boolean
) {
    predicate()
    predicate.invoke()
}

fun test() {
    labdas(
        predicate = ::functionName
    )
    val objectName = L()
    labdas(
        predicate = objectName::functionName
    )

    labdas {
        true
    }
}

/**
 * Some Functions to know
 */
fun someFunctions() {
    val wrapper = WrapperWrapper(Wrapper(110))
    wrapper.wrapper.value
    wrapper.wrapper.let { wrapper ->
        val value = wrapper.value
    }
}

data class WrapperWrapper(val wrapper: Wrapper)
