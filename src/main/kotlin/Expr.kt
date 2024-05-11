interface Expr

data class Binary(
    val left: Expr,
    val operator: TokenType,
    val right: Expr,
) : Expr

data class Unary(
    val operator: TokenType,
    val right: Expr,
) : Expr

data class Literal(val value: Any?) : Expr

data class Grouping(val expr: Expr) : Expr
