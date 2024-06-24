//NOTE:  Variable declaration
let a = 22

//NOTE: Function declaration
fn add(a, b) {
  print a + b
}

//NOTE:  First class function
let addFunc = add

//NOTE: Function call
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

//NOTE: Go-like while loop - bake while loop into for-statment
for ;j < 10; {
  print "j = " + j
  j = j + 1
}

