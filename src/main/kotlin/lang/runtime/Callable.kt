package lang.runtime

interface Callable {
    fun call(
        interpreter: Interpreter,
        arguments: List<Any?>,
    ): Any?
}
