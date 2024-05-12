import kotlin.text.buildString

// NOTE: Lisp like printer
class AstVisitor : Expr.Visitor {

  override fun visitBinaryExpr(binary: Binary): String {
    val left = binary.left.visit(this)
    val right = binary.right.visit(this)
    return buildString { append("(${binary.operator} $left $right)") }
  }

  override fun visitUnaryExpr(unary: Unary): String {
    val right = unary.right
    return buildString { append("(${unary.operator} $right)") }
  }

  override fun visitGroupingExpr(grouping: Grouping): String {
    val expr = grouping.expr.visit(this)
    return buildString { append("(group $expr)") }
  }

  override fun visitLiteralExpr(literal: Literal): String = buildString { append(literal.value) }
}
