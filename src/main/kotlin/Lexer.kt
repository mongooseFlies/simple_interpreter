import TokenType.*

data class Lexer(private val source: String) {
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
      '\n' -> line++
      '\t', '\r', ' ' -> advance()
      in '0'..'9' -> number()
    }
  }

  private fun string() {
    while (peek() != '"' && !isAtEnd()) {
      if (peek() == '\n') line++
      advance()
    }
  }

  private fun peek() = source[currentInd]

  private fun number() {
    // TODO: do this next to be able to parse math expression
  }

  private fun addToken(type: TokenType) {
    val text = source.slice(startInd..currentInd)
    val token = Token(type, text, null, line)
    tokens += token
  }

  private fun addToken(type: TokenType, value: Any?) {
    val text = source.slice(startInd..currentInd)
    val token = Token(type, text, null, line)
    tokens += token
  }
  private fun advance() = source[currentInd++]

  private fun isAtEnd() = currentInd >= source.length

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
}
