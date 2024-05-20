import lang.Lexer
import lang.Parser
import lang.interpreter.AstVisitor
import lang.interpreter.Interpreter
import java.io.File

fun main(args: Array<String>) {
  when (args.size) {
    0 -> repl()
    else -> source(args[1])
  }
}

private fun source(filename: String) {
  try {
    val file = File(filename)
    val source = file.readText()
    run(source)
  } catch (_: Exception) {
  }
}

fun repl() {
  val interpreter = Interpreter()
  while (true) {
    print("-> ")
    val userStmt = readlnOrNull() ?: return
    runLine(userStmt, interpreter)
  }
}

private fun runLine(line: String, interpreter: Interpreter) {
  val lexer = Lexer(line)
  val tokens = lexer.tokens()
  val parser = Parser(tokens)
  val statements = parser.parse()
  interpreter.interpret(statements)
}

fun run(source: String) {
  val lexer = Lexer(source)
  val tokens = lexer.tokens()
  tokens.forEach(::println)
  val parser = Parser(tokens)
  val statements = parser.parse()
  /*
   * NOTE: example of lisp like visitor
   * expr => 1 * 2
   * result => (ASTERISK 1 2)
   *
   * expr => 1 * (9 - 4)
   * result -> (ASTERISK 1 (GROUP (MINUS 9 4)))
   */

  val lispVisitor = AstVisitor()
  statements.forEach { println(it.visit(lispVisitor)) }
  val interpreter = Interpreter()
  interpreter.interpret(statements)
}
