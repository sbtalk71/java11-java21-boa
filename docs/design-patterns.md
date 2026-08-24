# Design Patterns in Java

Design patterns are **proven, reusable approaches to common software-design problems**. They are not ready-made libraries; rather, they provide a structure for organizing classes and objects.

This note covers four important patterns:

1. **Factory Pattern**
2. **Builder Pattern**
3. **Singleton Pattern**
4. **Dependency Injection (DI)**

---

# 1. Factory Pattern

## Problem

Suppose an application needs to create different types of notifications:

```text
EmailNotification
SmsNotification
PushNotification
```

Without a Factory, client code may become tightly coupled to concrete classes:

```java
Notification notification;

if (type.equals("email")) {
    notification = new EmailNotification();
} else if (type.equals("sms")) {
    notification = new SmsNotification();
}
```

Everywhere we create notifications, we may repeat this logic.

The **Factory pattern centralizes object creation**.

---

## Basic Example

### Product interface

```java
interface Notification {
    void send(String message);
}
```

### Concrete products

```java
class EmailNotification implements Notification {

    @Override
    public void send(String message) {
        System.out.println("Sending EMAIL: " + message);
    }
}
```

```java
class SmsNotification implements Notification {

    @Override
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}
```

```java
class PushNotification implements Notification {

    @Override
    public void send(String message) {
        System.out.println("Sending PUSH notification: " + message);
    }
}
```

### Factory

```java
class NotificationFactory {

    public static Notification create(String type) {

        return switch (type.toLowerCase()) {
            case "email" -> new EmailNotification();
            case "sms"   -> new SmsNotification();
            case "push"  -> new PushNotification();
            default -> throw new IllegalArgumentException(
                    "Unknown notification type: " + type);
        };
    }
}
```

### Client

```java
public class FactoryDemo {

    public static void main(String[] args) {

        Notification notification =
                NotificationFactory.create("email");

        notification.send("Order has been shipped");
    }
}
```

Output:

```text
Sending EMAIL: Order has been shipped
```

The client knows about:

```java
Notification
NotificationFactory
```

but does not need to know:

```java
new EmailNotification()
new SmsNotification()
new PushNotification()
```

---

## Factory Structure

```text
              Notification
                   ▲
       ┌───────────┼───────────┐
       │           │           │
    Email        SMS         Push
 Notification Notification Notification
       ▲
       │
 NotificationFactory
       │
       ▼
 creates appropriate object
```

---

## When to use Factory

Use Factory when:

* Object creation is complex.
* There are multiple implementations of an interface.
* The exact implementation depends on runtime information.
* You want to hide concrete classes from clients.
* Object creation logic is repeated in multiple places.

### Key benefit

**Factory separates object creation from object usage.**

---

# 2. Builder Pattern

The Builder pattern is useful when an object has **many optional parameters** or a complicated construction process.

Consider:

```java
class Employee {

    private String name;
    private int age;
    private String city;
    private String department;
    private double salary;
    private String phone;
}
```

A constructor could become difficult to use:

```java
new Employee(
    "John",
    35,
    "Hyderabad",
    "IT",
    75000,
    "9876543210"
);
```

What does each argument represent?

---

## The telescoping constructor problem

You might end up with:

```java
Employee(String name)

Employee(String name, int age)

Employee(String name, int age, String city)

Employee(String name, int age, String city, String department)

Employee(String name, int age, String city,
         String department, double salary)
```

This is called the **telescoping constructor problem**.

Builder provides a cleaner solution.

---

## Builder Example

```java
public class Employee {

    private final String name;
    private final int age;
    private final String city;
    private final String department;
    private final double salary;

    private Employee(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.city = builder.city;
        this.department = builder.department;
        this.salary = builder.salary;
    }

    public static class Builder {

        private String name;
        private int age;
        private String city;
        private String department;
        private double salary;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Builder salary(double salary) {
            this.salary = salary;
            return this;
        }

        public Employee build() {

            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException(
                        "Name is required");
            }

            return new Employee(this);
        }
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", city='" + city + '\'' +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                '}';
    }
}
```

### Usage

```java
Employee employee = new Employee.Builder()
        .name("John")
        .age(35)
        .city("Hyderabad")
        .department("IT")
        .salary(75000)
        .build();

System.out.println(employee);
```

This is much more readable:

```text
.name(...)
.age(...)
.city(...)
.department(...)
.salary(...)
```

---

## Builder and Immutability

Builder is often combined with **immutable objects**.

Notice:

```java
private final String name;
private final int age;
```

There are:

* no setters
* fields are `final`
* object is completely initialized during construction

Therefore, after construction:

```java
Employee employee = ...;
```

the object's state cannot be changed through setters.

This is particularly useful for:

* configuration objects
* request objects
* DTOs
* domain objects
* objects with many optional properties

---

# 3. Singleton Pattern

The Singleton pattern ensures that a class has **one shared instance** and provides a way to access it.

Typical examples include objects representing:

* application configuration
* shared caches
* certain resource managers

However, Singleton should be used carefully because excessive use introduces global state and makes testing harder.

---

## Basic Singleton

```java
public class Configuration {

    private static Configuration instance;

    private Configuration() {
        // Prevent external instantiation
    }

    public static Configuration getInstance() {

        if (instance == null) {
            instance = new Configuration();
        }

        return instance;
    }
}
```

Usage:

```java
Configuration c1 = Configuration.getInstance();
Configuration c2 = Configuration.getInstance();

System.out.println(c1 == c2);
```

Output:

```text
true
```

---

# Singleton and Multithreading

The previous implementation is **not thread-safe**.

Two threads could execute:

```java
if (instance == null)
```

at the same time.

Both could create an instance.

---

## Thread-safe Singleton using `synchronized`

```java
public class Configuration {

    private static Configuration instance;

    private Configuration() {
    }

    public static synchronized Configuration getInstance() {

        if (instance == null) {
            instance = new Configuration();
        }

        return instance;
    }
}
```

This is thread-safe, but every call acquires the lock.

---

## Double-checked locking

```java
public class Configuration {

    private static volatile Configuration instance;

    private Configuration() {
    }

    public static Configuration getInstance() {

        if (instance == null) {

            synchronized (Configuration.class) {

                if (instance == null) {
                    instance = new Configuration();
                }
            }
        }

        return instance;
    }
}
```

The `volatile` keyword is important here because of Java's memory-visibility and instruction-reordering rules.

---

# Best Simple Singleton: Enum

Java provides an elegant way to implement a Singleton:

```java
public enum AppConfig {

    INSTANCE;

    public void printConfig() {
        System.out.println("Application configuration");
    }
}
```

Usage:

```java
AppConfig.INSTANCE.printConfig();
```

The enum approach provides strong protection against common problems involving:

* serialization
* reflection
* multiple instances

For many traditional Singleton use cases, **enum Singleton is preferable to manually implementing the pattern**.

---

# 4. Dependency Injection

Dependency Injection is slightly different from the previous three patterns.

It is primarily a **dependency-management technique/principle**, rather than a classic GoF design pattern.

---

## What is a dependency?

Consider:

```java
class OrderService {

    private EmailNotification notification =
            new EmailNotification();

    public void placeOrder() {
        notification.send("Order placed");
    }
}
```

`OrderService` depends on:

```java
EmailNotification
```

But it also **creates** the dependency itself.

This creates tight coupling.

---

# Dependency Injection

Instead of:

```java
class OrderService {

    private EmailNotification notification =
            new EmailNotification();
}
```

we provide the dependency from outside:

```java
class OrderService {

    private final Notification notification;

    public OrderService(Notification notification) {
        this.notification = notification;
    }

    public void placeOrder() {
        notification.send("Order placed");
    }
}
```

Now:

```java
Notification notification =
        new EmailNotification();

OrderService service =
        new OrderService(notification);
```

The dependency is **injected** through the constructor.

---

# Constructor Injection

This is generally the preferred form of DI.

```java
interface PaymentService {
    void pay(double amount);
}
```

Implementation:

```java
class CreditCardPayment implements PaymentService {

    @Override
    public void pay(double amount) {
        System.out.println(
                "Paid using credit card: " + amount);
    }
}
```

Service:

```java
class OrderService {

    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void placeOrder(double amount) {
        paymentService.pay(amount);
    }
}
```

Client:

```java
public class DIDemo {

    public static void main(String[] args) {

        PaymentService payment =
                new CreditCardPayment();

        OrderService orderService =
                new OrderService(payment);

        orderService.placeOrder(5000);
    }
}
```

Notice that `OrderService` does not know:

```java
new CreditCardPayment()
```

It only knows:

```java
PaymentService
```

This is **programming to an abstraction**.

---

# DI Makes Testing Easier

Suppose we want to test:

```java
OrderService
```

We don't necessarily want to make a real payment.

We can provide a fake implementation:

```java
class FakePaymentService implements PaymentService {

    @Override
    public void pay(double amount) {
        System.out.println(
                "Fake payment: " + amount);
    }
}
```

Test:

```java
PaymentService fake =
        new FakePaymentService();

OrderService service =
        new OrderService(fake);

service.placeOrder(5000);
```

The production implementation can be replaced without modifying `OrderService`.

This is one of the biggest advantages of Dependency Injection.

---

# Types of Dependency Injection

There are three commonly discussed forms.

## 1. Constructor Injection

```java
class OrderService {

    private final PaymentService paymentService;

    OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

**Preferred for required dependencies.**

---

## 2. Setter Injection

```java
class OrderService {

    private PaymentService paymentService;

    public void setPaymentService(
            PaymentService paymentService) {

        this.paymentService = paymentService;
    }
}
```

Useful when the dependency is optional or can be changed.

---

## 3. Field Injection

Common in dependency-injection frameworks:

```java
class OrderService {

    @Autowired
    private PaymentService paymentService;
}
```

Although convenient, constructor injection is generally preferred because dependencies are:

* explicit
* immutable
* easier to test
* available when the object is constructed

---

# DI in Spring

Spring provides a Dependency Injection container.

For example:

```java
public interface PaymentService {
    void pay(double amount);
}
```

```java
@Service
public class CreditCardPayment
        implements PaymentService {

    @Override
    public void pay(double amount) {
        System.out.println(
                "Credit card payment: " + amount);
    }
}
```

Then:

```java
@Service
public class OrderService {

    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void placeOrder(double amount) {
        paymentService.pay(amount);
    }
}
```

Spring essentially performs:

```text
Application
    |
    v
Spring Container
    |
    +---- CreditCardPayment
    |
    +---- OrderService
              |
              +---- PaymentService
```

Spring creates the objects and injects the required dependency.

---

# Factory vs Dependency Injection

These two concepts are often confused.

### Factory

The application explicitly asks:

```java
Notification notification =
        NotificationFactory.create("email");
```

The Factory decides **which object to create**.

### Dependency Injection

The application says:

```java
OrderService service =
        new OrderService(paymentService);
```

or a DI container does it automatically.

DI decides **how dependencies are supplied**, rather than putting creation logic inside the dependent class.

A useful distinction is:

```text
Factory
   |
   +-- "Give me an object of this type."

Dependency Injection
   |
   +-- "Here is the dependency you need."
```

---

# Combining Factory + Builder + DI

In real applications, patterns are frequently combined.

For example:

```text
Controller
    |
    v
OrderService
    |
    +---- PaymentService
    |          |
    |          +---- CreditCardPayment
    |
    +---- NotificationService
               |
               +---- NotificationFactory
                          |
                          +---- EmailNotification
```

Builder may be used to construct the order:

```java
Order order = new Order.Builder()
        .customerId(101)
        .productId(500)
        .quantity(2)
        .build();
```

DI supplies the services:

```java
OrderService(
    PaymentService paymentService,
    NotificationService notificationService
)
```

Factory chooses an implementation:

```java
NotificationFactory.create("email");
```

So patterns are **not isolated techniques**. Good application design often combines them.

---

# Quick Comparison

| Pattern                  | Main Problem Solved              | Main Idea                      |
| ------------------------ | -------------------------------- | ------------------------------ |
| **Factory**              | Complex/variable object creation | Centralize object creation     |
| **Builder**              | Complex constructors             | Build objects step-by-step     |
| **Singleton**            | One shared instance              | Restrict instance creation     |
| **Dependency Injection** | Tight coupling                   | Supply dependencies externally |

## Easy way to remember

```text
Factory   → Who should I create?
Builder   → How should I construct this object?
Singleton → How many instances should exist?
DI        → Where does my dependency come from?
```

### Important design principle behind all four

The deeper goal is **loose coupling and maintainability**.

Instead of writing code such as:

```java
class OrderService {

    private EmailNotification notification =
            new EmailNotification();

    private PaymentService payment =
            new CreditCardPayment();
}
```

prefer designs where the class depends on abstractions:

```java
class OrderService {

    private final Notification notification;
    private final PaymentService paymentService;

    OrderService(
            Notification notification,
            PaymentService paymentService) {

        this.notification = notification;
        this.paymentService = paymentService;
    }
}
```

That allows implementations to change without changing the business logic.
