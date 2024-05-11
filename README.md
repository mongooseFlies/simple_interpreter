## Dynamic Programming Language Interpreter <-> WIP

Very small dynamically-typed programming language 

### Features

* Dynamic Typing: Variables do not require explicit type declaration.
* Interactive Shell: Execute code line-by-line for testing and exploration.
* Execute a source file.

### Getting Started

1. **Prerequisites:** 

    * Kotlin ([https://kotlinlang.org/](https://kotlinlang.org/))
    * Go ([https://go.dev/](https://go.dev/)) 

2. **Running the Interpreter:**

    * Execute the script:
      ```bash
      $ kotlinc ...
      ```
    * This will launch the interactive shell where you can try out

### Usage

* Use the Repl
* Run an entire source file

### **Example:**
```

```

### Supported Language Features
* 
* 
*
* 


## TODO list

* [ ] Control flow
* [ ] Functions with parameters
* [ ] Closures
* [ ] Classes
* [ ] Methods
* [ ] Inheritance

## Grammar

    expression |> literal    |
                  binary     |
                  unary      |
                  grouping   |

    literal   -> NUMBER | STRING | 'false' | 'true' | 'nil'
    binary    -> expression operator expression
    unary     -> operator expression
    grouping  -> '('  expression ')'
    operator  -> '==' | '>=' | '<' | '<=' | '+' | '-' | '*' | '/' | '!=' |

    expression => equality
    equality   => comparison ('==' | '!=') comparison
    comparison => term ('+' | '-') term
    term       => factor ('*' | '/') factor
    factor     => primary
    primary    => STRING | NUMBER | 'false' | 'true' | grouping
    grouping     => '(' expression ')'

    
