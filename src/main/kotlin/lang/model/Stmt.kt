package lang.model

interface Stmt {
    interface Visitor {
        fun visitExpressionStmt(stmt: Expression): Any?
        fun visitPrintStmt(stmt: Print): Any?
        fun visitVarStmt(stmt: Variable): Any?
    }
    fun visit(visitor: Visitor): Any?
}

data class Expression(val expr: Expr): Stmt {
    override fun visit(visitor: Stmt.Visitor) = visitor.visitExpressionStmt(this)
}
data class Print(val expr: Expr): Stmt {
    override fun visit(visitor: Stmt.Visitor) = visitor.visitPrintStmt(this)
}

data class Variable(val name: Token, val initializer: Expr?): Stmt {
    override fun visit(visitor: Stmt.Visitor) = visitor.visitVarStmt(this)
}
