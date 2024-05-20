package lang

import lang.Utils.ifTrue
import lang.model.*
import lang.model.TokenType.*

class Parser(
    private val tokens: List<Token>,
) {

  private var currentInd: Int = 0

  fun parse(): List<Stmt> {
    val statements = mutableListOf<Stmt>()
    while (!isAtEnd()) {
      statements += declaration()
    }
    return statements
  }

  private fun declaration(): Stmt =
      when {
        match(LET) -> varDeclaration()
        else -> statement()
      }

  private fun varDeclaration(): Stmt {
    val name = advance()
    var expr: Expr? = null
    if (match(EQ)) {
      expr = expression()
    }
    expect("Expect new line", LINE, EOF)
    return Variable(name, expr)
  }

  private fun statement(): Stmt =
      when {
        match(PRINT) -> {
          val expression = expression()
          val printStmt = Print(expression)
          expect("Expect new line", LINE, EOF)
          printStmt
        }
        else -> Expression(expression())
      }

  private fun expression() = equality()

  private fun equality(): Expr {
    var expr = comparison()
    while (match(EQ_EQ, NOT_EQ)) {
      val operator = previous().type
      val right = comparison()
      expr = Binary(expr, operator, right)
    }
    return expr
  }

  private fun comparison(): Expr {
    var expr = term()
    while (match(LT, LTE, GT, GTE)) {
      val operator = previous().type
      val right = term()
      expr = Binary(expr, operator, right)
    }
    return expr
  }

  private fun term(): Expr {
    var expr = factor()
    while (match(PLUS, MINUS)) {
      val operator = previous().type
      val right = factor()
      expr = Binary(expr, operator, right)
    }
    return expr
  }

  private fun factor(): Expr {
    var expr = unary()
    while (match(ASTERISK, SLASH)) {
      val operator = previous().type
      val right = unary()
      expr = Binary(expr, operator, right)
    }
    return expr
  }

  private fun unary(): Expr =
      if (match(MINUS, BANG)) {
        val operator = previous()
        val right = unary()
        Unary(operator, right)
      } else primary()

  private fun primary(): Expr =
      when {
        match(STRING) -> Literal(previous().text)
        match(NUMBER) -> Literal(previous().text.toDouble())
        match(NIL) -> Literal(null)
        match(LEFT_PAREN) -> {
          val expr = expression()
          expect("Expect right paren", RIGHT_PAREN)
          Grouping(expr)
        }
        match(IDENTIFIER) -> Var(previous())
        // TODO: Throw proper error with line number info
        else -> error("Compile error ...")
      }

  private fun expect(message: String, vararg types: TokenType) {
    when (match(*types)) {
      false -> error(message)
      else -> {}
    }
  }

  private fun match(vararg types: TokenType): Boolean = types.any { check(it) }.ifTrue { advance() }

  private fun check(type: TokenType) = tokens[currentInd].type == type

  private fun previous() = tokens[currentInd - 1]

  private fun advance() = tokens[currentInd++]

  private fun isAtEnd() = currentInd >= tokens.size
}
