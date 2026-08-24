#Method And Constructor Reference
In Java, **method references** and **constructor references** are shorthand for lambda expressions. They are mainly used with **functional interfaces** and Stream APIs.

## 1. Method Reference

General syntax:

```java
ClassName::methodName
```

### Example 1 — Static method

Suppose:

```java
class Calculator {
    static int square(int n) {
        return n * n;
    }
}
```

Using lambda:

```java
Function<Integer, Integer> f = n -> Calculator.square(n);
```

Using method reference:

```java
Function<Integer, Integer> f = Calculator::square;

System.out.println(f.apply(5));  // 25
```

So:

```text
n -> Calculator.square(n)
              ↓
Calculator::square
```

---

## 2. Instance Method Reference

```java
class Printer {
    void print(String message) {
        System.out.println(message);
    }
}
```

Create an object:

```java
Printer printer = new Printer();
```

Lambda:

```java
Consumer<String> c = message -> printer.print(message);
```

Method reference:

```java
Consumer<String> c = printer::print;

c.accept("Hello");
```

Here:

```text
message -> printer.print(message)
                 ↓
          printer::print
```

---

## 3. Instance Method of an Arbitrary Object

This is particularly useful with Streams.

Consider:

```java
List<String> names = List.of("John", "Alice", "Bob");
```

Lambda:

```java
names.stream()
     .map(name -> name.toUpperCase())
     .forEach(name -> System.out.println(name));
```

Method references:

```java
names.stream()
     .map(String::toUpperCase)
     .forEach(System.out::println);
```

Here:

```java
String::toUpperCase
System.out::println
```

are both method references.

---

# 4. Constructor Reference

A constructor reference uses:

```java
ClassName::new
```

Suppose:

```java
class Employee {
    private String name;

    Employee(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

Using lambda:

```java
Function<String, Employee> creator =
        name -> new Employee(name);
```

Using constructor reference:

```java
Function<String, Employee> creator =
        Employee::new;
```

Then:

```java
Employee e = creator.apply("John");

System.out.println(e.getName());
```

The following are equivalent:

```java
name -> new Employee(name)
```

and

```java
Employee::new
```

---

# 5. Constructor Reference with Supplier

For a no-argument constructor:

```java
class Employee {
    Employee() {
        System.out.println("Employee created");
    }
}
```

Lambda:

```java
Supplier<Employee> supplier =
        () -> new Employee();
```

Constructor reference:

```java
Supplier<Employee> supplier =
        Employee::new;
```

Then:

```java
Employee e = supplier.get();
```

---

# 6. Constructor Reference with Multiple Arguments

Suppose:

```java
class Employee {
    private int id;
    private String name;

    Employee(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
```

Define a functional interface:

```java
@FunctionalInterface
interface EmployeeCreator {
    Employee create(int id, String name);
}
```

Lambda:

```java
EmployeeCreator creator =
        (id, name) -> new Employee(id, name);
```

Constructor reference:

```java
EmployeeCreator creator =
        Employee::new;
```

Usage:

```java
Employee e = creator.create(101, "John");
```

---

## 7. Method Reference with Streams

This is where method references become especially useful.

```java
List<String> names =
        List.of("John", "Alice", "Bob", "David");
```

### Lambda version

```java
names.stream()
     .filter(name -> name.length() > 4)
     .map(name -> name.toUpperCase())
     .forEach(name -> System.out.println(name));
```

### Method-reference version

```java
names.stream()
     .filter(name -> name.length() > 4)
     .map(String::toUpperCase)
     .forEach(System.out::println);
```

Notice that `filter` still uses a lambda because the condition doesn't directly correspond to an existing method.

---

## 8. Sorting with Method Reference

```java
List<String> names =
        new ArrayList<>(List.of("John", "Alice", "Bob"));

names.sort(String::compareTo);

System.out.println(names);
```

Equivalent lambda:

```java
names.sort((a, b) -> a.compareTo(b));
```

---

# 9. Four Forms of Method References

Java essentially provides four important forms:

| Form                                 | Example               | Meaning                 |
| ------------------------------------ | --------------------- | ----------------------- |
| Static method                        | `Math::max`           | `x -> Math.max(x, ...)` |
| Instance method of particular object | `obj::method`         | `x -> obj.method(x)`    |
| Instance method of arbitrary object  | `String::toUpperCase` | `x -> x.toUpperCase()`  |
| Constructor                          | `Employee::new`       | `x -> new Employee(x)`  |

### Easy way to remember

```text
Static method
    ClassName::staticMethod

Object's instance method
    object::instanceMethod

Any object's instance method
    ClassName::instanceMethod

Constructor
    ClassName::new
```

The key point is: **a method reference doesn't execute the method immediately; it provides a reference to a method that a functional interface can invoke later.**
