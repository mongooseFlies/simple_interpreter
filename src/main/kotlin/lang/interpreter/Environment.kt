package lang.interpreter

class Environment(private val values: MutableMap<String, Any?> = mutableMapOf()) {

  fun define(name: String, value: Any?) {
    values[name] = value
  }

  fun get(name: String): Any? {
    if(!values.containsKey(name)) {
      throw RuntimeException("undeclared variable $name")
    }
    return values[name]
  }
}

