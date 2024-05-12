fun main(args: Array<String>) {
  when (args.size) {
    0 -> repl()
    else -> {
      // TODO -> Run from source file
    }
  }
}

fun repl() {
  while (true) {
    print("-> ")
    val userStmt = readln()
    run(userStmt)
  }
}

fun run(source: String) {
  val lexer = Lexer(source)
  val tokens = lexer.tokens()
  val parser = Parser(tokens)
  val ast = parser.parse()
  /*
   * NOTE: example of lisp like visitor
   * expr => 1 * 2
   * result => (ASTERISK 1 2)
   *
   * expr => 1 * (9 - 4)
   * result -> (ASTERISK 1 (group (MINUS 9 4)))
   * TODO: result => (* 1 (group (- 9 4))
   */

  val lispVisitor = AstVisitor()
  val result = ast.visit(lispVisitor)
  println(result)
}
