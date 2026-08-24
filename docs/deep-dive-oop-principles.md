# Deep Dive into OOP Principles in Java

Object-Oriented Programming (OOP) is more than simply creating classes and objects. In enterprise Java applications, OOP principles help us design software that is **maintainable, extensible, testable, and loosely coupled**.

The four major OOP concepts are:

1. **Abstraction**
2. **Encapsulation**
3. **Inheritance**
4. **Polymorphism**

These concepts are closely related. A good object-oriented design often combines all four.

---

# 1. Abstraction

### What is abstraction?

**Abstraction means exposing what an object does while hiding how it does it.**

For example, when we use:

```java
List<String> names = new ArrayList<>();
names.add("John");
```

we know that `List` supports operations such as `add()`, `remove()`, and `get()`.

We don't need to know how `ArrayList` internally stores and manages the elements.

So:

> **Abstraction focuses on WHAT an object does rather than HOW it does it.**

Java primarily provides abstraction through:

* Abstract classes
* Interfaces

---

## 1.1 Abstract Class

An abstract class is a class that can contain:

* Abstract methods
* Concrete methods
* Instance variables
* Constructors
* Static methods
* Final methods

Example:

```java
abstract class Payment {

    protected double amount;

    public Payment(double amount) {
        this.amount = amount;
    }

    // Abstract behavior
    public abstract void pay();

    // Common behavior
    public void printReceipt() {
        System.out.println("Receipt generated for: " + amount);
    }
}
```

A subclass provides the implementation:

```java
class CreditCardPayment extends Payment {

    public CreditCardPayment(double amount) {
        super(amount);
    }

    @Override
    public void pay() {
        System.out.println("Processing credit card payment");
    }
}
```

Usage:

```java
Payment payment = new CreditCardPayment(5000);

payment.pay();
payment.printReceipt();
```

### Why use an abstract class?

Use an abstract class when several related classes share:

* Common state
* Common implementation
* Common behavior

For example:

```text
              Payment
                 |
       ----------------------
       |                    |
 CreditCard              UPI
 Payment                Payment
```

The common payment-related functionality can be placed in `Payment`.

---

# 2. Interface

An interface defines a **contract** that implementing classes must satisfy.

```java
interface PaymentService {

    void pay(double amount);
}
```

Different classes can implement the contract:

```java
class CreditCardService implements PaymentService {

    @Override
    public void pay(double amount) {
        System.out.println("Paid using credit card: " + amount);
    }
}
```

```java
class UpiService implements PaymentService {

    @Override
    public void pay(double amount) {
        System.out.println("Paid using UPI: " + amount);
    }
}
```

The client depends on the abstraction:

```java
PaymentService service = new UpiService();

service.pay(2000);
```

The client does not need to know that the implementation is `UpiService`.

---

# 3. Abstract Class vs Interface

This is an important design decision.

| Feature              | Abstract Class            | Interface                                 |
| -------------------- | ------------------------- | ----------------------------------------- |
| Purpose              | Common base + abstraction | Contract/capability                       |
| Instance variables   | Yes                       | No normal instance state                  |
| Constructor          | Yes                       | No                                        |
| Abstract methods     | Yes                       | Yes                                       |
| Concrete methods     | Yes                       | Yes, through `default`/`static` methods   |
| Multiple inheritance | No                        | A class can implement multiple interfaces |
| State sharing        | Yes                       | Generally no                              |
| Best suited for      | Closely related classes   | Common capability/contract                |

For example:

```java
interface Flyable {
    void fly();
}
```

Many unrelated classes can implement it:

```java
class Bird implements Flyable {
    public void fly() {
        System.out.println("Bird flying");
    }
}
```

```java
class Drone implements Flyable {
    public void fly() {
        System.out.println("Drone flying");
    }
}
```

A bird and a drone are not necessarily related through inheritance, but both have the **capability** of flying.

### Rule of thumb

Use an **abstract class** when you want:

> "These objects are closely related and share common state/implementation."

Use an **interface** when you want:

> "These objects should follow this contract/capability."

---

# 4. Encapsulation

Encapsulation means:

> **Bundling data and behavior together while controlling access to the internal state.**

Consider a bank account.

Bad design:

```java
class BankAccount {
    public double balance;
}
```

Anyone can modify the balance:

```java
account.balance = -50000;
```

This violates the object's rules.

A better design:

```java
class BankAccount {

    private double balance;

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        balance += amount;
    }

    public double getBalance() {
        return balance;
    }
}
```

Now:

```java
BankAccount account = new BankAccount();

account.deposit(5000);

System.out.println(account.getBalance());
```

The caller cannot directly manipulate `balance`.

---

## 4.1 Encapsulation is More Than Getters and Setters

A common misconception is:

> Encapsulation = private fields + getters/setters.

Not necessarily.

This:

```java
class Employee {

    private double salary;

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
```

does not provide much protection because the caller can set any value.

A better design protects the object's invariants:

```java
class Employee {

    private double salary;

    public void increaseSalary(double percentage) {

        if (percentage <= 0) {
            throw new IllegalArgumentException(
                "Percentage must be positive");
        }

        salary += salary * percentage / 100;
    }

    public double getSalary() {
        return salary;
    }
}
```

The object controls **how its state can change**.

---

# 5. Immutability

Immutability is closely related to encapsulation.

An immutable object is an object whose state **cannot change after it is created**.

For example:

```java
final class Employee {

    private final int id;
    private final String name;

    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
```

Once created:

```java
Employee e = new Employee(101, "John");
```

there is no method that can change `id` or `name`.

---

## 5.1 Characteristics of an Immutable Class

A typical immutable class:

1. Is `final`
2. Has `private final` fields
3. Initializes fields through the constructor
4. Does not provide setters
5. Does not expose mutable internal objects directly

For example:

```java
final class Address {

    private final String city;

    public Address(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }
}
```

---

## 5.2 Defensive Copying

Consider:

```java
class Employee {

    private final List<String> skills;

    public Employee(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getSkills() {
        return skills;
    }
}
```

This is **not truly immutable**.

The caller can do:

```java
List<String> skills = new ArrayList<>();

skills.add("Java");

Employee e = new Employee(skills);

skills.add("Spring");
```

The employee's internal state has changed indirectly.

A better approach:

```java
final class Employee {

    private final List<String> skills;

    public Employee(List<String> skills) {
        this.skills = List.copyOf(skills);
    }

    public List<String> getSkills() {
        return skills;
    }
}
```

Now:

```java
Employee employee =
        new Employee(List.of("Java", "Spring"));
```

The internal list cannot be modified.

### Why immutability is useful

Immutable objects are particularly useful in:

* Multithreaded applications
* Caching
* Concurrent programming
* Value objects
* DTOs
* Configuration objects

Because their state cannot change, they are much easier to reason about safely.

---

# 6. Inheritance

Inheritance allows one class to acquire behavior and properties from another class.

```java
class Vehicle {

    public void start() {
        System.out.println("Vehicle started");
    }
}
```

A subclass:

```java
class Car extends Vehicle {

    public void drive() {
        System.out.println("Car driving");
    }
}
```

Usage:

```java
Car car = new Car();

car.start();
car.drive();
```

`Car` inherits `start()` from `Vehicle`.

---

# 7. The "is-a" Relationship

Inheritance should normally represent an **is-a relationship**.

```text
Car is a Vehicle
Dog is an Animal
Manager is an Employee
```

Therefore:

```java
class Car extends Vehicle
```

makes conceptual sense.

But:

```java
class Engine extends Car
```

doesn't make sense because an engine is not a car.

---

# 8. Inheritance vs Composition

This is one of the most important OOP design decisions.

### Inheritance

Inheritance expresses:

> **is-a**

Composition expresses:

> **has-a**

Consider:

```java
class Engine {

    public void start() {
        System.out.println("Engine started");
    }
}
```

Instead of:

```java
class Car extends Engine {
}
```

use composition:

```java
class Car {

    private Engine engine;

    public Car(Engine engine) {
        this.engine = engine;
    }

    public void start() {
        engine.start();
    }
}
```

Now:

```text
Car
 |
 +---- has an ----> Engine
```

This is a **has-a relationship**.

---

# 9. Why Composition Is Often Preferred

Inheritance creates a strong coupling between parent and child.

Consider:

```java
class Report {

    public void generate() {
        // ...
    }
}

class PdfReport extends Report {
}
```

Later, suppose we need:

* PDF report
* Excel report
* HTML report

Inheritance can start producing a large hierarchy.

Composition can provide greater flexibility:

```java
interface ReportFormatter {

    void format();
}
```

```java
class PdfFormatter implements ReportFormatter {

    public void format() {
        System.out.println("Formatting PDF");
    }
}
```

```java
class ExcelFormatter implements ReportFormatter {

    public void format() {
        System.out.println("Formatting Excel");
    }
}
```

The report uses a formatter:

```java
class Report {

    private final ReportFormatter formatter;

    public Report(ReportFormatter formatter) {
        this.formatter = formatter;
    }

    public void generate() {
        formatter.format();
    }
}
```

Now:

```java
Report report =
        new Report(new PdfFormatter());

report.generate();
```

or:

```java
Report report =
        new Report(new ExcelFormatter());

report.generate();
```

This design is much easier to extend.

---

# 10. Composition in Spring Applications

This principle is everywhere in Spring.

For example:

```java
@Service
class OrderService {

    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

`OrderService` does not extend `PaymentService`.

Instead:

```text
OrderService
      |
      | has-a
      v
PaymentService
```

This is composition combined with abstraction and dependency injection.

It results in:

* Loose coupling
* Easy testing
* Easy replacement of implementations
* Better maintainability

---

# 11. Polymorphism

Polymorphism means:

> **One interface/reference can represent multiple forms of an object.**

For example:

```java
PaymentService service;
```

The same reference can point to different implementations:

```java
service = new CreditCardService();
```

or:

```java
service = new UpiService();
```

The caller uses the same contract:

```java
service.pay(1000);
```

but different implementations execute.

There are two important forms of polymorphism in Java:

1. Compile-time polymorphism
2. Runtime polymorphism

---

# 12. Compile-Time Polymorphism

Compile-time polymorphism is primarily achieved through **method overloading**.

The compiler determines which method to call based on the method signature.

```java
class Calculator {

    int add(int a, int b) {
        return a + b;
    }

    double add(double a, double b) {
        return a + b;
    }

    int add(int a, int b, int c) {
        return a + b + c;
    }
}
```

Usage:

```java
Calculator calculator = new Calculator();

calculator.add(10, 20);

calculator.add(10.5, 20.5);

calculator.add(10, 20, 30);
```

The compiler resolves the appropriate overloaded method.

### Important

Changing only the return type does **not** constitute overloading:

```java
int calculate(int x) {
    return x;
}
```

and:

```java
double calculate(int x) {
    return x;
}
```

cannot coexist.

The method signature must differ in its parameter list.

---

# 13. Runtime Polymorphism

Runtime polymorphism is achieved through **method overriding**.

Example:

```java
class Animal {

    public void sound() {
        System.out.println("Animal sound");
    }
}
```

```java
class Dog extends Animal {

    @Override
    public void sound() {
        System.out.println("Dog barks");
    }
}
```

```java
class Cat extends Animal {

    @Override
    public void sound() {
        System.out.println("Cat meows");
    }
}
```

Now:

```java
Animal animal;

animal = new Dog();
animal.sound();
```

Output:

```text
Dog barks
```

Change the object:

```java
animal = new Cat();
animal.sound();
```

Output:

```text
Cat meows
```

The reference type is:

```java
Animal
```

but the actual object determines which overridden method executes.

This is **runtime polymorphism**.

---

# 14. The Key Idea: Reference Type vs Object Type

This distinction is fundamental.

```java
Animal animal = new Dog();
```

There are two types involved:

```text
Reference type        Object type
-------------        -----------
Animal        --->    Dog
```

The compiler primarily uses the **reference type** to determine what operations are available.

The JVM uses the **actual object type** to determine the overridden implementation.

Therefore:

```java
animal.sound();
```

calls:

```java
Dog.sound()
```

because the actual object is a `Dog`.

---

# 15. Compile-Time vs Runtime Polymorphism

| Feature              | Compile-Time                          | Runtime                              |
| -------------------- | ------------------------------------- | ------------------------------------ |
| Common mechanism     | Overloading                           | Overriding                           |
| Resolution           | Compiler                              | Runtime                              |
| Inheritance required | No                                    | Usually yes/interface implementation |
| Same method name     | Yes                                   | Yes                                  |
| Parameter list       | Must differ                           | Must remain compatible               |
| Example              | `add(int,int)` / `add(double,double)` | `Dog.sound()` / `Cat.sound()`        |

---

# 16. Polymorphism Through Interfaces

Runtime polymorphism becomes especially powerful with interfaces.

```java
interface NotificationService {

    void send(String message);
}
```

Implementations:

```java
class EmailNotification implements NotificationService {

    @Override
    public void send(String message) {
        System.out.println("Sending email: " + message);
    }
}
```

```java
class SmsNotification implements NotificationService {

    @Override
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}
```

Client:

```java
class NotificationManager {

    private final NotificationService service;

    public NotificationManager(NotificationService service) {
        this.service = service;
    }

    public void notifyUser(String message) {
        service.send(message);
    }
}
```

Now:

```java
NotificationManager manager =
        new NotificationManager(
                new EmailNotification());

manager.notifyUser("Order shipped");
```

We can change the implementation:

```java
NotificationManager manager =
        new NotificationManager(
                new SmsNotification());

manager.notifyUser("Order shipped");
```

The `NotificationManager` itself does not change.

This is one of the most important applications of polymorphism in enterprise Java.

---

# 17. Putting All Four Principles Together

Consider an e-commerce payment system.

### Abstraction

```java
interface PaymentService {

    void pay(double amount);
}
```

The interface defines **what** a payment service does.

### Encapsulation

```java
class Payment {

    private final double amount;

    public Payment(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }

        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
```

The payment amount is protected from uncontrolled modification.

### Polymorphism

```java
class UpiPayment implements PaymentService {

    public void pay(double amount) {
        System.out.println("UPI payment: " + amount);
    }
}
```

```java
class CardPayment implements PaymentService {

    public void pay(double amount) {
        System.out.println("Card payment: " + amount);
    }
}
```

The same interface supports multiple implementations.

### Composition

```java
class OrderService {

    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void placeOrder(double amount) {
        paymentService.pay(amount);
        System.out.println("Order placed");
    }
}
```

Now:

```java
OrderService service =
        new OrderService(new UpiPayment());

service.placeOrder(5000);
```

We have combined:

```text
                OOP Design
                    |
       +------------+------------+
       |            |            |
  Abstraction  Encapsulation  Polymorphism
       |            |            |
   Interface    private state   multiple
       |                         implementations
       +------------+------------+
                    |
              Composition
                    |
             OrderService
                    |
                    v
            PaymentService
```

---

# 18. Practical Design Guidelines

When designing Java classes, ask these questions.

### Abstraction

**What should the client know?**

Hide implementation details behind:

```java
interface
```

or:

```java
abstract class
```

### Encapsulation

**Who should be allowed to change this state?**

Keep state private:

```java
private
```

and expose meaningful operations instead of unrestricted setters.

### Immutability

**Does this object really need to change after creation?**

If not, consider:

```java
final class
private final fields
no setters
```

### Inheritance

**Is there a genuine "is-a" relationship?**

If not, don't use inheritance simply to reuse code.

### Composition

**Can I build this object using other objects instead?**

Prefer:

```java
class A {
    private B b;
}
```

when the relationship is **has-a**.

### Polymorphism

**Can the client depend on an abstraction instead of a concrete implementation?**

Prefer:

```java
PaymentService service;
```

over:

```java
UpiPayment service;
```

when the client doesn't need UPI-specific behavior.

---

# 19. The Big Picture

The four principles are not independent features. They work together:

```text
              ABSTRACTION
                   |
       Define what the object does
                   |
                   v
             INTERFACE
                   |
                   v
            POLYMORPHISM
                   |
       Multiple implementations
                   |
                   v
            COMPOSITION
                   |
       Assemble objects together
                   |
                   v
            ENCAPSULATION
                   |
       Protect internal state
                   |
                   v
            IMMUTABILITY
                   |
       Prevent unwanted state changes
```

A modern Java application therefore tends to favor:

```text
Program to interfaces
        +
Encapsulate state
        +
Prefer composition
        +
Use inheritance selectively
        +
Use polymorphism for variation
        +
Use immutability where practical
```

### A useful mental model

> **Abstraction decides what is exposed.**
> **Encapsulation controls how state is accessed.**
> **Inheritance represents an "is-a" relationship.**
> **Composition represents a "has-a" relationship.**
> **Polymorphism allows one abstraction to have multiple implementations.**
> **Immutability prevents an object's state from changing after construction.**

These principles become particularly important when designing **Spring services, microservices, domain models, strategy implementations, repositories, notification systems, payment systems, and concurrent applications**.

Absolutely. These topics fit naturally after the core OOP principles because they show how Java's `Object` model, interfaces, and functional programming build on OOP.

# Additional OOP Concepts in Java

## 1. The `Object` Class

In Java, every class ultimately inherits from `java.lang.Object`.

Even this:

```java
class Employee {
}
```

is conceptually:

```java
class Employee extends Object {
}
```

Therefore, every Java object has methods inherited from `Object`.

Some of the most important methods are:

```java
public boolean equals(Object obj)
public int hashCode()
public String toString()
protected Object clone()
public final Class<?> getClass()
protected void finalize()
```

> Note: `finalize()` is deprecated and should not be used in modern Java.

Other important methods include:

```java
public final void wait()
public final void wait(long timeout)
public final void notify()
public final void notifyAll()
```

These are related to thread coordination.

---

# 2. `toString()`

The `toString()` method provides a textual representation of an object.

Consider:

```java
class Employee {

    private int id;
    private String name;

    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
```

If we do:

```java
Employee e = new Employee(101, "John");

System.out.println(e);
```

Java effectively calls:

```java
System.out.println(e.toString());
```

Without overriding `toString()`, the output will look something like:

```text
Employee@5e91993f
```

This is generally not useful to application developers.

Override it:

```java
@Override
public String toString() {
    return "Employee{id=" + id +
           ", name='" + name + "'}";
}
```

Now:

```java
System.out.println(e);
```

might produce:

```text
Employee{id=101, name='John'}
```

### Where is `toString()` useful?

It is especially useful for:

* Logging
* Debugging
* Monitoring
* Exception messages
* Displaying object state during development

For example:

```java
logger.info("Processing employee {}", employee);
```

The logging framework will typically invoke `toString()`.

---

# 3. `equals()`

`equals()` determines whether two objects should be considered logically equal.

Consider:

```java
Employee e1 = new Employee(101, "John");
Employee e2 = new Employee(101, "John");
```

Without overriding `equals()`:

```java
System.out.println(e1.equals(e2));
```

will normally produce:

```text
false
```

because the default implementation in `Object` compares object identity.

If employees should be considered equal based on their ID, override it.

```java
@Override
public boolean equals(Object obj) {

    if (this == obj)
        return true;

    if (!(obj instanceof Employee other))
        return false;

    return id == other.id;
}
```

Now:

```java
Employee e1 = new Employee(101, "John");
Employee e2 = new Employee(101, "John");

System.out.println(e1.equals(e2));
```

produces:

```text
true
```

---

# 4. The `equals()` Contract

The `equals()` method has an important contract.

It should be:

### Reflexive

An object must equal itself.

```java
x.equals(x) == true
```

### Symmetric

If:

```java
x.equals(y)
```

is true, then:

```java
y.equals(x)
```

should also be true.

### Transitive

If:

```text
x.equals(y)
y.equals(z)
```

then:

```text
x.equals(z)
```

should also be true.

### Consistent

Repeated calls should produce the same result as long as the objects have not changed in a way relevant to equality.

### Non-null

For a non-null object:

```java
x.equals(null)
```

should return:

```text
false
```

---

# 5. `hashCode()`

`hashCode()` returns an integer representation of an object's logical state.

Example:

```java
@Override
public int hashCode() {
    return Integer.hashCode(id);
}
```

The important rule is:

> **If two objects are equal according to `equals()`, they must have the same `hashCode()`.**

Therefore:

```java
if (e1.equals(e2)) {
    // Must be true
    e1.hashCode() == e2.hashCode();
}
```

The reverse is **not** required.

Two unequal objects can have the same hash code.

This is called a **hash collision**.

---

# 6. Why `equals()` and `hashCode()` Must Be Implemented Together

This is particularly important when objects are used in:

* `HashSet`
* `HashMap`
* `HashTable`

Consider:

```java
Set<Employee> employees = new HashSet<>();

Employee e1 = new Employee(101, "John");
Employee e2 = new Employee(101, "John");

employees.add(e1);
employees.add(e2);

System.out.println(employees.size());
```

If `equals()` and `hashCode()` are correctly implemented based on ID:

```text
1
```

The set recognizes the two objects as logically equal.

If you override `equals()` but not `hashCode()`, you can get unexpected behavior because the two equal objects may be placed in different hash buckets.

### Rule

Whenever you override:

```java
equals()
```

you should normally override:

```java
hashCode()
```

using the **same fields that determine equality**.

---

# 7. Complete Example

```java
import java.util.Objects;

class Employee {

    private final int id;
    private final String name;

    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (!(obj instanceof Employee other))
            return false;

        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "Employee{id=" + id +
               ", name='" + name + "'}";
    }
}
```

Usage:

```java
Employee e1 = new Employee(101, "John");
Employee e2 = new Employee(101, "John");

System.out.println(e1.equals(e2));
System.out.println(e1.hashCode());
System.out.println(e1);
```

Output:

```text
true
101
Employee{id=101, name='John'}
```

---

# 8. `Object` Methods and Their Purpose

| Method        | Purpose                                                   |
| ------------- | --------------------------------------------------------- |
| `equals()`    | Logical equality                                          |
| `hashCode()`  | Hash-based representation                                 |
| `toString()`  | Textual representation                                    |
| `getClass()`  | Runtime class information                                 |
| `clone()`     | Object copying mechanism; rarely preferred in modern Java |
| `wait()`      | Thread coordination                                       |
| `notify()`    | Wake a waiting thread                                     |
| `notifyAll()` | Wake all waiting threads                                  |

### `getClass()`

```java
Employee employee = new Employee(101, "John");

System.out.println(employee.getClass());
```

Output:

```text
class Employee
```

You can also obtain the class name:

```java
System.out.println(employee.getClass().getName());
```

---

# 9. Marker Interfaces

A **marker interface** is an interface with no methods.

Example:

```java
interface Auditable {
}
```

A class can implement it:

```java
class Employee implements Auditable {
}
```

The interface doesn't provide behavior.

Instead, it communicates:

> "This class belongs to a particular category or has a particular capability."

---

## 9.1 Famous Java Marker Interfaces

Examples include:

```java
Serializable
Cloneable
RandomAccess
```

For example:

```java
class Employee implements Serializable {
}
```

`Serializable` doesn't define methods that `Employee` must implement.

Instead, it tells Java's serialization mechanism that the object is eligible for serialization.

---

# 10. Why Marker Interfaces Exist

Suppose we define:

```java
interface Auditable {
}
```

Then:

```java
class Employee implements Auditable {
}

class Product {
}
```

We can determine whether an object is auditable:

```java
Object obj = new Employee();

if (obj instanceof Auditable) {
    System.out.println("Object supports auditing");
}
```

The interface provides **type information**.

This is sometimes called a **tagging** or **marker** mechanism.

---

# 11. Marker Interface vs Annotation

Modern Java applications frequently use annotations instead of marker interfaces.

For example:

```java
@Auditable
class Employee {
}
```

An annotation can carry additional metadata:

```java
@Auditable(level = "HIGH")
class Employee {
}
```

A marker interface, however, participates directly in the Java type system.

For example:

```java
if (employee instanceof Auditable)
```

So the two mechanisms solve related but different problems.

---

# 12. Functional Interfaces

A functional interface is an interface with **exactly one abstract method**.

For example:

```java
@FunctionalInterface
interface Calculator {

    int calculate(int a, int b);
}
```

It can be implemented using a lambda:

```java
Calculator addition =
        (a, b) -> a + b;

System.out.println(
        addition.calculate(10, 20)
);
```

Output:

```text
30
```

The interface provides the contract, while the lambda provides the implementation.

---

# 13. Why `@FunctionalInterface`?

This annotation tells the compiler:

> "This interface is intended to be a functional interface."

For example:

```java
@FunctionalInterface
interface Calculator {

    int calculate(int a, int b);

    void anotherMethod();
}
```

This produces a compilation error because there are two abstract methods.

The annotation therefore protects the design.

---

# 14. Functional Interface Can Have Default and Static Methods

A functional interface can have:

* One abstract method
* Multiple `default` methods
* Multiple `static` methods

For example:

```java
@FunctionalInterface
interface Calculator {

    int calculate(int a, int b);

    default void printMessage() {
        System.out.println("Calculator");
    }

    static void info() {
        System.out.println("Utility method");
    }
}
```

It is still a functional interface because there is only **one abstract method**.

---

# 15. Built-in Functional Interfaces

Java provides many functional interfaces in:

```java
java.util.function
```

Some important ones are:

| Interface           | Abstract method   | Purpose          |
| ------------------- | ----------------- | ---------------- |
| `Predicate<T>`      | `boolean test(T)` | Test a condition |
| `Function<T,R>`     | `R apply(T)`      | Transform data   |
| `Consumer<T>`       | `void accept(T)`  | Consume data     |
| `Supplier<T>`       | `T get()`         | Supply data      |
| `UnaryOperator<T>`  | `T apply(T)`      | T → T            |
| `BinaryOperator<T>` | `T apply(T,T)`    | T,T → T          |

---

# 16. `Predicate`

Used when we want to test something.

```java
Predicate<Integer> isEven =
        n -> n % 2 == 0;

System.out.println(isEven.test(10));
```

Output:

```text
true
```

A practical example:

```java
List<Integer> numbers =
        List.of(10, 15, 20, 25, 30);

numbers.stream()
       .filter(n -> n > 20)
       .forEach(System.out::println);
```

The lambda supplied to `filter()` behaves as a `Predicate`.

---

# 17. `Function`

A `Function<T,R>` transforms one value into another.

```java
Function<String, Integer> length =
        text -> text.length();

System.out.println(length.apply("Java"));
```

Output:

```text
4
```

Another example:

```java
Function<Employee, String> employeeName =
        Employee::getName;
```

---

# 18. `Consumer`

A `Consumer` accepts a value and does something with it.

```java
Consumer<String> printer =
        text -> System.out.println(text);

printer.accept("Hello Java");
```

There is no return value.

This is commonly used with streams:

```java
employees.forEach(
        employee -> System.out.println(employee)
);
```

---

# 19. `Supplier`

A `Supplier` produces a value without receiving an argument.

```java
Supplier<Double> randomNumber =
        () -> Math.random();

System.out.println(randomNumber.get());
```

It is useful when value creation should be deferred.

For example:

```java
Supplier<List<String>> listSupplier =
        () -> new ArrayList<>();
```

Each call can create a new list:

```java
List<String> list1 = listSupplier.get();
List<String> list2 = listSupplier.get();
```

---

# 20. Functional Interface and Polymorphism

Functional interfaces are actually another powerful application of **polymorphism**.

Consider:

```java
interface PaymentProcessor {

    void process(double amount);
}
```

We can provide different implementations:

```java
PaymentProcessor p1 =
        amount -> System.out.println(
                "Processing card: " + amount);

PaymentProcessor p2 =
        amount -> System.out.println(
                "Processing UPI: " + amount);
```

Both conform to the same abstraction:

```text
PaymentProcessor
       |
       +---- Lambda 1
       |
       +---- Lambda 2
```

The lambda therefore provides a lightweight implementation of an interface.

---

# 21. Method References

Functional interfaces also allow method references.

Instead of:

```java
Consumer<String> printer =
        text -> System.out.println(text);
```

we can write:

```java
Consumer<String> printer =
        System.out::println;
```

Similarly:

```java
Function<Employee, String> getName =
        Employee::getName;
```

This is especially common in Streams.

---

# 22. How These Concepts Connect

These concepts form a larger OOP picture:

```text
                    Object
                      |
          +-----------+-----------+
          |           |           |
       equals()   hashCode()  toString()
          |
          v
   Object equality
          |
          v
   HashMap / HashSet


                  Interfaces
                      |
          +-----------+-----------+
          |                       |
    Marker Interface       Functional Interface
          |                       |
    Type/capability          One abstract method
                                  |
                                  v
                               Lambda
                                  |
                                  v
                            Polymorphism
```

---

# 23. Important Interview/Design Rules

### Rule 1 — `equals()` and `hashCode()`

If:

```java
a.equals(b)
```

is `true`, then:

```java
a.hashCode() == b.hashCode()
```

**must** be true.

---

### Rule 2 — `hashCode()` does not prove equality

This is possible:

```text
a.hashCode() == b.hashCode()
```

while:

```text
a.equals(b) == false
```

because hash collisions are allowed.

---

### Rule 3 — Override `toString()` for useful diagnostics

Especially for domain objects and DTOs.

---

### Rule 4 — Marker interfaces have no abstract methods

Example:

```java
interface Auditable {
}
```

They communicate a type-level property.

---

### Rule 5 — Functional interfaces have one abstract method

```java
@FunctionalInterface
interface Processor {
    void process();
}
```

`default` and `static` methods do not count toward the single abstract-method requirement.

---

### Rule 6 — Prefer interfaces for contracts

```java
PaymentService
NotificationService
Repository
```

rather than exposing concrete implementations everywhere.

---

### Rule 7 — Functional interfaces make behavior a value

Instead of passing only data:

```java
process(Employee employee)
```

we can pass behavior:

```java
process(Employee employee,
        Predicate<Employee> condition)
```

This is a major bridge between traditional OOP and Java's functional programming model.

---

## Final OOP Mental Model

A strong Java developer should see these concepts as interconnected rather than isolated features:

```text
                     OOP
                      |
       +--------------+--------------+
       |              |              |
   Abstraction   Encapsulation   Polymorphism
       |              |              |
   Interfaces     private state   Overriding
   Abstract       Immutability     Overloading
   classes
       |                              |
       +--------------+---------------+
                      |
                 Composition
                      |
              Flexible object design
                      |
       +--------------+--------------+
       |                             |
 Object contract              Functional interfaces
       |                             |
 equals()                     Lambda expressions
 hashCode()                   Method references
 toString()                   Streams
       |
 HashMap / HashSet
```


