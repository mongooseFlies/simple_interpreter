package lang.model

interface Stmt {
    interface Visitor {
        fun visitExpressionStmt(stmt: Expression): Any?

        fun visitPrintStmt(stmt: Print): Any?

        fun visitVarStmt(stmt: Variable): Any?

        fun visitBlockStmt(block: Block): Any?

        fun visitFnStmt(fn: Fn): Any?

        fun visitIfStmt(ifStmt: If): Any?

        fun visitForStmt(forStmt: For): Any?
    }

    fun visit(visitor: Visitor): Any?
}

data class Expression(
    val expr: Expr,
) : Stmt {
    override fun visit(visitor: Stmt.Visitor) = visitor.visitExpressionStmt(this)
}

data class Print(
    val expr: Expr,
) : Stmt {
    override fun visit(visitor: Stmt.Visitor) = visitor.visitPrintStmt(this)
}

data class Variable(
    val name: Token,
    val initializer: Expr?,
) : Stmt {
    override fun visit(visitor: Stmt.Visitor) = visitor.visitVarStmt(this)
}

data class Block(
    val statements: List<Stmt>,
) : Stmt {
    override fun visit(visitor: Stmt.Visitor): Any? = visitor.visitBlockStmt(this)
}

data class Fn(
    val name: Token,
    val params: List<Token>,
    val body: List<Stmt>,
) : Stmt {
    override fun visit(visitor: Stmt.Visitor) = visitor.visitFnStmt(this)
}

data class If(
    val condition: Expr,
    val then: List<Stmt>,
    val elseBranch: List<Stmt>?,
) : Stmt {
    override fun visit(visitor: Stmt.Visitor) = visitor.visitIfStmt(this)
}

data class For(
    val initializer: Expr?,
    val condition: Expr,
    val increment: Expr?,
    val body: List<Stmt>
) : Stmt {
    override fun visit(visitor: Stmt.Visitor) = visitor.visitForStmt(this)
}
