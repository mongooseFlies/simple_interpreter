## Dynamic Programming Language 

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
// Variable declaration
let a = 22

// Function declaration
fn add(a, b) {
  print a + b
}

// First class function
let addFunc = add

// Function call
addFunc(4, 5)

add(3, 4)

print "HI"
print 3 * 4

if 1 != 1  {
  print 1
} else {
  print "Dummy"
}

//TODO: if statement
//TODO: while statement

let sum = 0

for i = 1; i < 3; i = i + 1 {
  sum = sum + i
}

print sum

let j = 1

//NOTE: Go-like while loop
// bake while loop into for-statment
for ;j < 10; {
  print "j = " + j
  j = j + 1
}
```

### Supported Language Features
* 
* 
*
* 


## TODO list

* [x] Control flow
* [x] Functions with parameters
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

    
