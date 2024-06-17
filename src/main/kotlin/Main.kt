import lang.Lexer
import lang.Parser
import lang.model.Token
import lang.model.TokenType
import lang.runtime.Ast
import lang.runtime.Interpreter
import java.io.File

var hadError = false

fun main(args: Array<String>) {
    when (args.size) {
        0 -> repl()
        else -> source(args[0])
        // TODO: add help ?
    }
}

private fun source(filename: String) {
    try {
        val file = File(filename)
        val source = file.readText()
        run(source)
    } catch (ex: Exception) {
        println(ex)
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

private fun runLine(
    line: String,
    interpreter: Interpreter,
) {
    val lexer = Lexer(line)
    val tokens = lexer.tokens()
    val parser = Parser(tokens)
    val statements = parser.parse()
    interpreter.interpret(statements)
}

fun run(source: String) {
    val lexer = Lexer(source)
    val tokens = lexer.tokens()

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
    if (!hadError) {
        val lispVisitor = Ast()
        statements.forEach { println(it.visit(lispVisitor)) }
        val interpreter = Interpreter()
        interpreter.interpret(statements)
    }
}

fun error(
    line: Int,
    message: String,
) = report(line, "", message)

fun error(
    token: Token,
    message: String,
) {
    if (token.type === TokenType.EOF) {
        report(token.line, " at end", message)
    } else {
        report(token.line, " at ${token.text}", message)
    }
}

private fun report(
    line: Int,
    where: String,
    message: String,
) {
    System.err.println(
        "[line $line] Error$where: $message",
    )
    hadError = true
}

