# Closure In Java

In Java, a **closure** is a mechanism where a function/lambda **captures variables from its surrounding scope** and can continue to use those variables even after the surrounding method has finished.

Java supports closure-like behavior through **lambda expressions**, but with an important restriction: captured local variables must be **final or effectively final**.

### 1. Simple closure example

```java
public class ClosureDemo {

    public static void main(String[] args) {

        int multiplier = 10;

        Function<Integer, Integer> multiply =
                n -> n * multiplier;

        System.out.println(multiply.apply(5));  // 50
    }
}
```

Here:

```java
int multiplier = 10;
```

belongs to the surrounding scope, while:

```java
n -> n * multiplier
```

is the lambda.

The lambda **captures `multiplier`**. This is the basic idea of a closure.

---

### 2. Why must the variable be effectively final?

This is valid:

```java
int x = 10;

Function<Integer, Integer> f =
        n -> n + x;
```

But this is **not valid**:

```java
int x = 10;

Function<Integer, Integer> f =
        n -> n + x;

x = 20;       // Compilation error
```

The compiler complains because `x` is no longer effectively final.

You can explicitly declare it final:

```java
final int x = 10;

Function<Integer, Integer> f =
        n -> n + x;
```

### 3. Why does Java impose this restriction?

Consider:

```java
public static Function<Integer, Integer> createMultiplier() {

    int multiplier = 10;

    return n -> n * multiplier;
}
```

After `createMultiplier()` returns, its local variable `multiplier` normally goes out of scope.

Yet:

```java
Function<Integer, Integer> f = createMultiplier();

System.out.println(f.apply(5));
```

still prints:

```text
50
```

The lambda has captured the value of `multiplier`.

Conceptually:

```text
createMultiplier()
       |
       | creates
       v
  multiplier = 10
       |
       | captured by
       v
   Lambda object
       |
       | survives method return
       v
 f.apply(5)
       |
       v
      50
```

---

## 4. Closure vs Lambda

These terms are related but not identical.

**Lambda** describes the function syntax:

```java
n -> n * 10
```

**Closure** describes the ability of that function to **capture variables from its surrounding lexical scope**:

```java
int multiplier = 10;

Function<Integer, Integer> f =
        n -> n * multiplier;
```

So:

> **A lambda can form a closure when it captures variables from its enclosing scope.**

A lambda that doesn't capture anything is often called a **non-capturing lambda**:

```java
Function<Integer, Integer> f =
        n -> n * 10;
```

A capturing lambda:

```java
int multiplier = 10;

Function<Integer, Integer> f =
        n -> n * multiplier;
```

---

## 5. A more interesting example: creating customized functions

Closures become very useful when creating functions dynamically.

```java
static Function<Integer, Integer> createMultiplier(int multiplier) {

    return number -> number * multiplier;
}
```

Now:

```java
Function<Integer, Integer> multiplyBy10 =
        createMultiplier(10);

Function<Integer, Integer> multiplyBy20 =
        createMultiplier(20);

System.out.println(multiplyBy10.apply(5)); // 50
System.out.println(multiplyBy20.apply(5)); // 100
```

Each returned lambda remembers the value it captured.

Conceptually:

```text
multiplyBy10
    |
    +-- multiplier = 10

multiplyBy20
    |
    +-- multiplier = 20
```

This is one of the most useful ways to understand closures.

---

## 6. Closure with a method

A closure doesn't have to capture only primitive values.

```java
static Consumer<String> createLogger(String prefix) {

    return message ->
            System.out.println(prefix + ": " + message);
}
```

Usage:

```java
Consumer<String> infoLogger =
        createLogger("INFO");

Consumer<String> errorLogger =
        createLogger("ERROR");

infoLogger.accept("Application started");
errorLogger.accept("Database unavailable");
```

Output:

```text
INFO: Application started
ERROR: Database unavailable
```

The lambda remembers `prefix`.

---

## 7. Capturing an object

Java closures can capture references to objects too.

```java
class Employee {
    String name;

    Employee(String name) {
        this.name = name;
    }
}
```

```java
Employee emp = new Employee("John");

Runnable task = () ->
        System.out.println(emp.name);

task.run();
```

The lambda captures the reference `emp`.

Importantly, **effectively final applies to the reference**, not necessarily to the object's state.

For example:

```java
Employee emp = new Employee("John");

Runnable task = () ->
        System.out.println(emp.name);

emp.name = "David";   // Valid

task.run();
```

Output:

```text
David
```

The reference `emp` wasn't reassigned; the object it refers to was modified.

---

## 8. Closure with instance variables

Instance variables don't have the same effectively-final restriction:

```java
class Counter {

    private int count = 0;

    Runnable increment = () -> {
        count++;
        System.out.println(count);
    };
}
```

Usage:

```java
Counter counter = new Counter();

counter.increment.run();
counter.increment.run();
counter.increment.run();
```

Output:

```text
1
2
3
```

Here `count` is an instance field, not a local variable captured by the lambda.

---

## 9. Common interview question

**Why can't I do this?**

```java
int count = 0;

Runnable r = () -> {
    count++;
};
```

Compilation error.

Instead, you can use a mutable holder such as:

```java
AtomicInteger count = new AtomicInteger(0);

Runnable r = () -> {
    count.incrementAndGet();
};

r.run();
r.run();

System.out.println(count.get()); // 2
```

However, don't use mutable holders merely to bypass the effectively-final rule. In concurrent programs especially, choose the appropriate thread-safe design.

---

## 10. Closure, Functional Interface and Lambda

These three concepts are often confused:

```text
Functional Interface
       |
       | provides target type
       v
Lambda Expression
       |
       | may capture variables
       v
Closure
```

Example:

```java
Function<Integer, Integer>      // Functional interface
        |
        v
n -> n * multiplier             // Lambda
        |
        v
captures multiplier            // Closure
```

### Key points to remember

| Concept                 | Meaning                                                      |
| ----------------------- | ------------------------------------------------------------ |
| Functional interface    | Interface with one abstract method                           |
| Lambda                  | Concise implementation of a functional interface             |
| Closure                 | Lambda/function together with captured surrounding variables |
| Captured local variable | Must be final/effectively final                              |
| Captured object         | Object state can still be mutable                            |
| Instance fields         | Can be modified from a lambda                                |

**The simplest definition for Java learners:**

> **A closure is a lambda expression that remembers and uses variables from the scope in which it was created.**
