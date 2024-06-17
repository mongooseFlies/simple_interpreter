package lang.runtime

import lang.model.*
import lang.model.TokenType.*

class Interpreter(private var environment: Environment = Environment()) :
    Expr.Visitor, Stmt.Visitor {

  fun interpret(statements: List<Stmt>) = statements.forEach { it.visit(this) }

  private fun eval(expr: Expr) = expr.visit(this)

  override fun visitBinaryExpr(binary: Binary): Any {
    val left = eval(binary.left)
    val right = eval(binary.right)
    val operator = binary.operator
    return when (operator) {
      ASTERISK -> {
        assertNumbers(left, right)
        (left as Double) * (right as Double)
      }
      SLASH -> {
        assertNumbers(left, right)
        (left as Double) / (right as Double)
      }
      MINUS -> {
        assertNumbers(left, right)
        (left as Double) - (right as Double)
      }
      PLUS ->
          when {
            left is Double && right is Double -> left + right
            left is String && right is String -> "$left$right"
            else -> error("can add only strings or numbers")
          }
      GTE -> {
        assertNumbers(left, right)
        (left as Double) >= (right as Double)
      }
      GT -> {
        assertNumbers(left, right)
        (left as Double) > (right as Double)
      }
      LT -> {
        assertNumbers(left, right)
        (left as Double) < (right as Double)
      }
      LTE -> {
        assertNumbers(left, right)
        (left as Double) <= (right as Double)
      }
      EQ_EQ -> isEquals(left, right)
      NOT_EQ -> !isEquals(left, right)
      else -> {}
    }
  }

  private fun isEquals(left: Any?, right: Any?): Boolean {
    if (left == null && right == null) return true
    return left?.equals(right) ?: false
  }

  override fun visitUnaryExpr(unary: Unary): Any {
    val expression = eval(unary.right)
    return when (unary.operator.type) {
      BANG -> isTruthy(expression)
      MINUS -> {
        if (expression is Double) return -1 * expression
        else error("Expect a number after ${unary.operator.type}")
      }
      else -> {}
    }
  }

  override fun visitGroupingExpr(grouping: Grouping): Any? = eval(grouping.expr)

  override fun visitLiteralExpr(literal: Literal): Any? = literal.value

  override fun visitVarExpr(expr: Var) = environment.get(expr.token.text)

  override fun visitCallExpr(expr: Call): Any? {
    val callee = eval(expr.callee)
    if (callee !is Callable) {
      throw RuntimeError("expr is not callable")
    }
    val arguments = mutableListOf<Any?>()
    for (argument in expr.arguments) arguments += eval(argument)
    return callee.call(this, arguments)
  }

  private fun assertNumbers(left: Any?, right: Any?) {
    if (left !is Double || right !is Double) throw RuntimeError("Expect number")
  }

  private fun isTruthy(obj: Any?) =
      when (obj) {
        is Boolean -> obj
        null -> false
        // NOTE: anything else is true if not null
        else -> true
      }

  override fun visitExpressionStmt(stmt: Expression) = eval(stmt.expr)

  override fun visitPrintStmt(stmt: Print) = println(eval(stmt.expr))

  override fun visitVarStmt(stmt: Variable) {
    var initializer: Any? = null
    if (stmt.initializer != null) {
      initializer = eval(stmt.initializer)
    }
    environment.define(stmt.name.text, initializer)
  }

  override fun visitBlockStmt(block: Block) {
    executeBlockStmt(block.statements, environment)
  }

  fun executeBlockStmt(body: List<Stmt>, environment: Environment) {
    // NOTE: take env snapshot
    val previous = this.environment
    try {
      for (stmt in body) stmt.visit(this)
    } finally {
      // NOTE: restore env after function call exits
      this.environment = previous
    }
  }

  override fun visitFnStmt(fn: Fn) {
    val function = Function(fn, environment)
    environment.define(fn.name.text, function)
  }

  override fun visitIfStmt(ifStmt: If) {
    val condition = eval(ifStmt.condition)
    if (isTruthy(condition)) {
      executeBlockStmt(ifStmt.then, environment)
    } else if (ifStmt.elseBranch != null) executeBlockStmt(ifStmt.elseBranch, environment)
  }
}

data class RuntimeError(override val message: String) : RuntimeException(message)
