package lang

import kotlin.collections.mutableListOf
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
        match(FN) -> function()
        else -> statement()
      }

  private fun function(): Stmt {
    val name = consume("expect function name", IDENTIFIER)
    expect("expect '(' after function name", LEFT_PAREN)
    val params = mutableListOf<Token>()
    if (!check(RIGHT_PAREN)) {
      do {
        params += consume("expect param", IDENTIFIER)
      } while (match(COMMA))
    }
    expect("expect ')' after params", RIGHT_PAREN)
    expect("expect '{'", RIGHT_BRACE)

    val funBody = block()
    return Fn(name, params, funBody)
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
        match(LEFT_BRACE) -> Block(block())
        else -> Expression(expression())
      }

  private fun block(): List<Stmt> {
    val statements = mutableListOf<Stmt>()
    while (!check(RIGHT_BRACE)) {
      statements += declaration()
    }
    expect("Expect '}'", RIGHT_BRACE)
    return statements
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
        else -> throw ParseException("Compile error ...", tokens[currentInd])
      }

  private fun expect(message: String, vararg types: TokenType) {
    when (match(*types)) {
      false -> error(message)
      else -> {}
    }
  }

  private fun consume(message: String, vararg types: TokenType): Token {
    val value = tokens[currentInd]
    expect(message, *types)
    return value
  }

  private fun match(vararg types: TokenType): Boolean = types.any { check(it) }.ifTrue { advance() }

  private fun check(type: TokenType) = tokens[currentInd].type == type

  private fun previous() = tokens[currentInd - 1]

  private fun advance() = tokens[currentInd++]

  private fun isAtEnd() = currentInd >= tokens.size
}

data class ParseException(override val message: String, val token: Token) : RuntimeException()
