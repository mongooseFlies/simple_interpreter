package lang.model

enum class TokenType {
  // Operators
  SLASH,
  PLUS,
  MINUS,
  ASTERISK,

  // Comparison
  GT,
  GTE,
  LT,
  LTE,
  EQ,
  EQ_EQ,
  NOT_EQ,

  // Grouping
  LEFT_PAREN,
  RIGHT_PAREN,
  LEFT_BRACE,
  RIGHT_BRACE,

  // Punctuation
  DOT,
  COMMA,
  BANG,

  // Primary types
  BOOL,
  STRING,
  NUMBER,
  IDENTIFIER,

  // Keywords
  IF,
  ELSE,
  FALSE,
  TRUE,
  FOR,
  FN,
  NIL,
  PRINT,
  LET,
  WHILE,
  EOF,
  LINE,
}
