import TokenType.*

class Parser(
    private val tokens: List<Token>,
) {

  private var currentInd: Int = 0

  fun parse(): Expr = expression()

  private fun expression() = equality()

  private fun equality(): Expr {
    var expr = comparison()
    while (match(EQ_EQ) || match(NOT_EQ)) {
      val operator = previous().type
      val right = comparison()
      expr = Binary(expr, operator, right)
    }
    return expr
  }

  private fun comparison(): Expr {
    var expr = term()
    while (match(LT) || match(LTE) || match(GT) || match(GTE)) {
      val operator = previous().type
      val right = term()
      expr = Binary(expr, operator, right)
    }
    return expr
  }

  private fun term(): Expr {
    var expr = factor()
    while (match(PLUS) || match(MINUS)) {
      val operator = previous().type
      val right = factor()
      expr = Binary(expr, operator, right)
    }
    return expr
  }

  private fun factor(): Expr {
    var expr = primary()
    while (match(ASTERISK) || match(SLASH)) {
      val operator = previous().type
      val right = primary()
      expr = Binary(expr, operator, right)
    }
    return expr
  }

  private fun primary(): Expr =
      when {
        match(STRING) -> Literal(previous().text)
        match(NUMBER) -> Literal(previous().text!!.toDouble())
        match(NIL) -> Literal(null)
        match(LEFT_PAREN) -> {
          val expr = expression()
          expect(RIGHT_PAREN, "Expect right paren")
          Grouping(expr)
        }

        // TODO: Throw proper error with line number info
        else -> error("Compile error ...")
      }

  private fun expect(type: TokenType, message: String) {
    when (match(type)) {
      false -> error(message)
      else -> {}
    }
  }

  private fun match(type: TokenType): Boolean = if (check(type)) advance().let { true } else false

  private fun check(type: TokenType) = tokens[currentInd].type == type

  private fun previous() = tokens[currentInd - 1]

  private fun advance() = tokens[currentInd++]

  private fun isAtEnd() = currentInd >= tokens.size
}