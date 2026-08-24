#Lambda Chaining and Composition
Lambda **chaining and composition** means combining multiple lambda expressions/functions so that the output of one function becomes the input of another.

Java provides this mainly through the default methods of functional interfaces such as:

* `Function` → `andThen()`, `compose()`
* `Predicate` → `and()`, `or()`, `negate()`
* `Consumer` → `andThen()`
* `UnaryOperator` → same composition methods as `Function`

### 1. Function chaining with `andThen()`

```java
Function<Integer, Integer> doubleIt =
        x -> x * 2;

Function<Integer, Integer> addTen =
        x -> x + 10;

Function<Integer, Integer> combined =
        doubleIt.andThen(addTen);

System.out.println(combined.apply(5));
```

Execution:

```text
5
 ↓
doubleIt       → 10
 ↓
addTen         → 20
```

Output:

```text
20
```

So:

```java
doubleIt.andThen(addTen)
```

means:

```text
addTen(doubleIt(x))
```

---

### 2. `compose()` — reverse order

```java
Function<Integer, Integer> doubleIt =
        x -> x * 2;

Function<Integer, Integer> addTen =
        x -> x + 10;

Function<Integer, Integer> combined =
        doubleIt.compose(addTen);

System.out.println(combined.apply(5));
```

Execution:

```text
5
 ↓
addTen         → 15
 ↓
doubleIt       → 30
```

Output:

```text
30
```

Therefore:

```java
doubleIt.compose(addTen)
```

means:

```text
doubleIt(addTen(x))
```

### Easy way to remember

```text
f.andThen(g)
        ↓
g(f(x))

f.compose(g)
        ↓
f(g(x))
```

---

## 3. Chaining more than two functions

You can chain functions repeatedly:

```java
Function<String, String> trim =
        String::trim;

Function<String, String> upper =
        String::toUpperCase;

Function<String, String> addBrackets =
        s -> "[" + s + "]";

Function<String, String> process =
        trim
            .andThen(upper)
            .andThen(addBrackets);

System.out.println(process.apply("  java  "));
```

Execution:

```text
"  java  "
     ↓
trim
     ↓
"java"
     ↓
toUpperCase
     ↓
"JAVA"
     ↓
addBrackets
     ↓
"[JAVA]"
```

---

# 4. Predicate composition

Predicates return `boolean`.

```java
Predicate<Integer> positive =
        n -> n > 0;

Predicate<Integer> even =
        n -> n % 2 == 0;
```

Combine them:

```java
Predicate<Integer> positiveAndEven =
        positive.and(even);

System.out.println(positiveAndEven.test(10));  // true
System.out.println(positiveAndEven.test(-10)); // false
System.out.println(positiveAndEven.test(7));   // false
```

### OR

```java
Predicate<Integer> positiveOrEven =
        positive.or(even);
```

### NOT

```java
Predicate<Integer> notPositive =
        positive.negate();
```

This is particularly useful in Stream filtering:

```java
numbers.stream()
       .filter(positive.and(even))
       .forEach(System.out::println);
```

---

# 5. Consumer chaining

`Consumer<T>` performs an action and returns nothing.

```java
Consumer<String> print =
        s -> System.out.println("Value: " + s);

Consumer<String> log =
        s -> System.out.println("Logging: " + s);
```

Chain them:

```java
Consumer<String> process =
        print.andThen(log);

process.accept("Java");
```

Output:

```text
Value: Java
Logging: Java
```

The important distinction is:

```text
Function   → produces a result
Predicate  → produces boolean
Consumer   → performs an action
```

---

# 6. Real-world example: processing an employee

Suppose:

```java
record Employee(String name, double salary) {}
```

Create functions:

```java
Function<Employee, Double> getSalary =
        Employee::salary;

Function<Double, Double> addBonus =
        salary -> salary * 1.10;

Function<Double, Double> tax =
        salary -> salary * 0.90;
```

Compose them:

```java
Function<Employee, Double> finalSalary =
        getSalary
            .andThen(addBonus)
            .andThen(tax);
```

Then:

```java
Employee emp = new Employee("John", 100000);

System.out.println(finalSalary.apply(emp));
```

Processing:

```text
Employee
   ↓
getSalary
   ↓
100000
   ↓
addBonus (10%)
   ↓
110000
   ↓
tax (10%)
   ↓
99000
```

This is **functional composition**: building a larger function from smaller functions.

---

## 7. Chaining vs composition

A useful distinction:

**Chaining** emphasizes the sequence:

```java
f.andThen(g).andThen(h)
```

```text
x → f → g → h → result
```

**Composition** emphasizes creating a new function:

```java
Function<Integer, Integer> pipeline =
        f.andThen(g).andThen(h);
```

The resulting `pipeline` itself is a function that can be reused:

```java
pipeline.apply(10);
pipeline.apply(20);
pipeline.apply(30);
```

This idea becomes extremely important when using **Java Streams**, because a Stream pipeline is essentially a sequence of transformations:

```java
employees.stream()
         .filter(...)
         .map(...)
         .sorted(...)
         .collect(...);
```

Each operation contributes to a larger functional processing pipeline.
