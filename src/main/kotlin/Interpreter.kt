import TokenType.*

class Interpreter : Expr.Visitor {

  fun eval(expr: Expr) = expr.visit(this)

  override fun visitBinaryExpr(binary: Binary): Any? {
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
      PLUS -> {
        assertNumbers(left, right)
        (left as Double) + (right as Double)
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
      // TODO: throw an error
      else -> {}
    }
  }

  private fun isEquals(left: Any?, right: Any?): Boolean {
    if (left == null && right == null) return true
    return left?.equals(right) ?: false
  }

  override fun visitUnaryExpr(unary: Unary): Any? {
    val expression = eval(unary.right)
    return when (unary.operator.type) {
      BANG -> truthy(expression)
      MINUS -> {
        if (expression is Double) return -1 * expression
        else error("Expect a number after ${unary.operator}")
      }
      else -> {}
    }
  }

  override fun visitGroupingExpr(grouping: Grouping): Any? = eval(grouping.expr)

  override fun visitLiteralExpr(literal: Literal) = literal.value

  private fun assertNumbers(left: Any?, right: Any?) {
    if (left !is Double || right !is Double) {
      throw RuntimeException("Expect number")
    }
  }

  private fun truthy(obj: Any?) =
      when (obj) {
        is Boolean -> obj
        null -> false
        // NOTE: anything else is true if not null
        else -> true
      }
}
