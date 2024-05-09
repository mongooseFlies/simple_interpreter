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
    println(source)
}