import TokenType.*

data class Lexer(private val source: String) {

  private val tokens = mutableListOf<Token>()
  private var startInd = 0
  private var currentInd = 0
  private var line = 0
  private val keywords =
      mapOf(
          "if" to IF,
          "else" to ELSE,
          "for" to FOR,
          "while" to WHILE,
          "true" to TRUE,
          "false" to FALSE,
          "let" to LET,
          "nil" to NIL,
          "print" to PRINT,
      )

  fun tokens(): List<Token> {
    while (!isAtEnd()) {
      startInd = currentInd
      processToken()
    }
    tokens.add(Token(type = EOF, text = "", value = null, line))
    return tokens
  }

  private fun processToken() {
    when (advance()) {
      '(' -> addToken(LEFT_PAREN)
      ')' -> addToken(RIGHT_PAREN)
      '{' -> addToken(LEFT_BRACE)
      '}' -> addToken(RIGHT_BRACE)
      '+' -> addToken(PLUS)
      '-' -> addToken(MINUS)
      '/' -> addToken(SLASH)
      '*' -> addToken(ASTERISK)
      '"' -> string()
      '=' -> {
        val type = if (match('=')) EQ_EQ else EQ
        addToken(type)
      }
      '\n', '\r', ';' -> {
        addToken(LINE)
        line++
      }
      '\t', ' ' -> {}
      in '0'..'9' -> number()
      in 'a'..'z', in 'A'..'Z' -> identifier()
    }
  }

  private fun identifier() {
    while (isAlphaNumeric(peek())) advance()
    val text = source.slice(startInd..currentInd)
    val keyword = keywords[text]
    val type = keyword ?: IDENTIFIER
    addToken(type)
  }

  private fun match(expected: Char): Boolean {
    if (isAtEnd()) return false
    val char = peek()
    return when (char) {
      expected -> {
        advance()
        true
      }
      else -> false
    }
  }

  private fun isAlphaNumeric(char: Char) = isAlpha(char) || isDigit(char)

  private fun isAlpha(char: Char) = char in 'a'..'z' || char in 'A'..'Z'

  private fun isDigit(char: Char) = char in '0'..'9'

  private fun string() {
    while (peek() != '"' && !isAtEnd()) {
      if (peek() == '\n') line++
      advance()
    }
    addToken(STRING)
  }

  private fun peek() =
      when {
        isAtEnd() -> Char.MIN_VALUE
        else -> source[currentInd]
      }

  private fun number() {
    while (isDigit(peek())) advance()
    // NOTE: handle double values
    // TODO: Introduce int and double instead of number
    if (match('.')) while (isDigit(peek())) advance()
    val value = source.slice(startInd ..< currentInd)
    addToken(NUMBER, value)
  }

  private fun addToken(type: TokenType) {
    val text = source.slice(startInd ..< currentInd)
    val token = Token(type, text, null, line)
    tokens += token
  }

  private fun addToken(type: TokenType, value: Any?) {
    val text = source.slice(startInd ..< currentInd)
    val token = Token(type, text, null, line)
    tokens += token
  }
  private fun advance(): Char = source[currentInd++]

  private fun isAtEnd() = currentInd >= source.length
}
