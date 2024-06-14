package lang.runtime

import lang.model.Fn

class Function(
    private val declaration: Fn,
    private val environment: Environment
) : Callable {
    override fun call(
        interpreter: Interpreter,
        arguments: List<Any?>,
    ) {
        for (i in arguments.indices) {
            environment.define(declaration.params[i].text, arguments[i])
        }
        interpreter.executeBlockStmt(declaration.body, environment)
    }
}
