package lang.runtime

import kotlin.text.buildString
import lang.model.*

class AstVisitor : Expr.Visitor, Stmt.Visitor {
  override fun visitBinaryExpr(binary: Binary): String {
    val left = binary.left.visit(this)
    val right = binary.right.visit(this)
    return buildString { append("(${binary.operator} $left $right)") }
  }

  override fun visitUnaryExpr(unary: Unary): String {
    val right = unary.right.visit(this)
    return buildString { append("(${unary.operator.type} $right)") }
  }

  override fun visitGroupingExpr(grouping: Grouping): String {
    val expr = grouping.expr.visit(this)
    return buildString { append("(GROUP $expr)") }
  }

  override fun visitLiteralExpr(literal: Literal): String = buildString { append(literal.value) }

  override fun visitVarExpr(expr: Var) = buildString { append(expr.token.text) }

  override fun visitCallExpr(expr: Call) = buildString {
    append("(CALL ${expr.callee.visit(this@AstVisitor)} ")
    if (expr.arguments.isNotEmpty()) {
      append("(PARAMS (")
      val argumentsLen = expr.arguments.size
      for (index in expr.arguments.indices) {
        append("${expr.arguments[index]}")
        if (index < argumentsLen - 1)
          append(", ")
        else
          append(")")
      }
      append(")")
    }
    append(")")
  }

  override fun visitExpressionStmt(stmt: Expression) {
    stmt.expr.visit(this)
  }

  override fun visitPrintStmt(stmt: Print) = buildString {
    append("(PRINT ${stmt.expr.visit(this@AstVisitor)})")
  }

  override fun visitVarStmt(stmt: Variable) = buildString {
    append("(VAR ${stmt.name.text} ${stmt.initializer})})")
  }

  override fun visitBlockStmt(block: Block) = buildString {
    append(("(BLOCK "))
    for (stmt in block.statements) append(stmt.visit(this@AstVisitor))
    append(")")
  }

  override fun visitFnStmt(fn: Fn) = buildString {
    append("(FN -> ${fn.name.text}(PARAMS ")
    for (index in fn.params.indices) {
      append(fn.params[index].text)
      if (index < fn.params.size - 1)
        append(", ")
      else
        append(")")
    }
    append(")")
    for (stmt in fn.body) stmt.visit(this@AstVisitor)
    append(")")
  }
}
