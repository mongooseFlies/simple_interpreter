package lang

object Utils {
    fun Boolean.ifTrue(block: () -> Unit): Boolean {
        if (this) {
            block()
        }
        return this
    }
}
