package lang.model

data class Token (
    val type : TokenType,
    val text: String?,
    val value: Any?,
    val line: Int
)
