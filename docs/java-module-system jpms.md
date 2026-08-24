Java 9 introduced the **Java Platform Module System (JPMS)**, which allows you to organize applications into strongly encapsulated modules.

## Rules for Creating Java 9 Modules

1. Every module must have a **`module-info.java`** file in its source root.
2. The module name should be **globally unique** and follow reverse-domain naming (e.g., `com.example.orders`).
3. A module only exposes packages declared with the `exports` statement.
4. Packages not exported are **completely hidden** from other modules.
5. To use another module, declare it with the `requires` statement.
6. A package can belong to **only one module** (avoid split packages).
7. The module descriptor (`module-info.java`) should be in the root of the module's source directory.
8. Cyclic dependencies between modules should be avoided.

## Steps to Create Java Modules

### Step 1: Create Project Structure

```
src
 ├── com.example.util
 │    ├── module-info.java
 │    └── com/example/util/
 │          Calculator.java
 │
 └── com.example.app
      ├── module-info.java
      └── com/example/app/
            Main.java
```

### Step 2: Create the Utility Module

`module-info.java`

```java
module com.example.util {
    exports com.example.util;
}
```

`Calculator.java`

```java
package com.example.util;

public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
}
```

### Step 3: Create the Application Module

`module-info.java`

```java
module com.example.app {
    requires com.example.util;
}
```

`Main.java`

```java
package com.example.app;

import com.example.util.Calculator;

public class Main {
    public static void main(String[] args) {
        Calculator c = new Calculator();
        System.out.println(c.add(10, 20));
    }
}
```

### Step 4: Compile the Modules

Compile the utility module first:

```bash
javac -d mods/com.example.util \
src/com.example.util/module-info.java \
src/com.example.util/com/example/util/Calculator.java
```

Compile the application module:

```bash
javac --module-path mods \
-d mods/com.example.app \
src/com.example.app/module-info.java \
src/com.example.app/com/example/app/Main.java
```

### Step 5: Run the Application

```bash
java --module-path mods \
-m com.example.app/com.example.app.Main
```

Output:

```
30
```

## Common Module Directives

| Directive             | Purpose                                                |
| --------------------- | ------------------------------------------------------ |
| `exports`             | Makes a package accessible to other modules            |
| `requires`            | Declares a dependency on another module                |
| `requires transitive` | Re-exports a dependency to downstream modules          |
| `requires static`     | Optional dependency, mainly for compile time           |
| `opens`               | Allows runtime reflective access (e.g., by frameworks) |
| `opens ... to`        | Opens a package only to specified modules              |
| `exports ... to`      | Exports a package only to specified modules            |
| `uses`                | Declares that the module consumes a service            |
| `provides ... with`   | Registers a service implementation                     |

## Best Practices

* Use meaningful, reverse-domain module names.
* Export only the packages that form your public API.
* Keep implementation packages unexported.
* Minimize module dependencies.
* Avoid split packages and cyclic dependencies.
* Use `opens` only when frameworks (such as Hibernate, Spring, or Jackson) require reflection.

Let's continue with the **more important JPMS concepts**, especially the ones useful for an intermediate Java learner.

## 1. `exports` — controlling the public API

Suppose the utility module contains two packages:

```text
com.example.util
com.example.util.internal
```

`Calculator`:

```java
package com.example.util;

public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
}
```

Internal implementation:

```java
package com.example.util.internal;

public class InternalLogger {
    public static void log(String message) {
        System.out.println(message);
    }
}
```

Module descriptor:

```java
module com.example.util {
    exports com.example.util;
}
```

Notice:

```text
exports com.example.util
```

but **not**:

```text
exports com.example.util.internal
```

Therefore another module can do:

```java
import com.example.util.Calculator;
```

but cannot do:

```java
import com.example.util.internal.InternalLogger;
```

even though `InternalLogger` is `public`.

### Important rule

In JPMS:

> `public` controls visibility inside the Java language, while `exports` controls visibility between modules.

This is one of the most important differences between traditional Java applications and modular Java applications.

---

# 2. `requires` — declaring dependencies

Suppose:

```text
com.example.app
        |
        | requires
        ↓
com.example.util
```

The application module declares:

```java
module com.example.app {
    requires com.example.util;
}
```

Now classes inside `com.example.app` can use exported packages from `com.example.util`.

For example:

```java
package com.example.app;

import com.example.util.Calculator;

public class Main {

    public static void main(String[] args) {

        Calculator calculator = new Calculator();

        System.out.println(
            calculator.add(10, 20)
        );
    }
}
```

The dependency is explicit.

Traditional Java:

```text
Application
   |
   | classpath
   ↓
many JARs
```

JPMS:

```text
Application Module
       |
       | requires
       ↓
Utility Module
```

The module system can therefore detect missing dependencies much earlier.

---

# 3. Strong Encapsulation

Consider:

```java
module com.example.util {

    exports com.example.util;
}
```

The module contains:

```text
com.example.util
    Calculator.java

com.example.util.internal
    InternalLogger.java
```

Only this is part of the module's external API:

```text
com.example.util
```

The following remains encapsulated:

```text
com.example.util.internal
```

This is called **strong encapsulation**.

### Why is this useful?

Suppose you have:

```text
com.company.employee
com.company.employee.internal
com.company.employee.util
```

You might want external applications to see only:

```text
com.company.employee
```

while hiding:

```text
internal
util
```

Your module becomes similar to a well-designed library with a clearly defined public API.

---

# 4. `requires transitive`

Consider three modules:

```text
A → B → C
```

Suppose:

```text
A requires B
B requires C
```

Normally, A does **not automatically obtain access** to C.

But B can declare:

```java
module B {
    requires transitive C;
}
```

Now:

```text
A
|
requires B
|
B
|
requires transitive C
|
C
```

A can read C because B explicitly exposes that dependency transitively.

### Example

```java
module com.example.service {
    requires transitive com.example.model;
}
```

This means:

> Anyone who requires `com.example.service` also gets readability of `com.example.model`.

Use `requires transitive` carefully because it makes a dependency part of your module's API.

---

# 5. `opens`

This is particularly important for frameworks.

Suppose:

```java
module com.example.employee {
    exports com.example.employee;
}
```

A framework may need reflection to access private fields:

```java
class Employee {

    private int id;
    private String name;
}
```

`exports` does **not** provide unrestricted reflective access.

You can use:

```java
module com.example.employee {

    exports com.example.employee;

    opens com.example.employee;
}
```

Now deep reflection is allowed.

This is commonly relevant to frameworks such as:

* Hibernate
* Jackson
* dependency injection frameworks
* serialization frameworks

### `exports` vs `opens`

| `exports`               | `opens`                    |
| ----------------------- | -------------------------- |
| Provides normal access  | Provides reflective access |
| Applies to public types | Allows deep reflection     |
| Compile-time access     | Runtime reflection         |
| Used for API            | Used mainly for frameworks |

You can also restrict reflection:

```java
opens com.example.employee
    to com.example.persistence;
```

Only `com.example.persistence` gets reflective access.

---

# 6. `exports ... to`

You can also restrict normal access to a particular module.

```java
module com.example.employee {

    exports com.example.employee.api
        to com.example.client;
}
```

Now:

```text
com.example.client
```

can access the package, but arbitrary modules cannot.

This is called a **qualified export**.

---

# 7. Modular JAR

Once the application is working, you can package a module into a JAR.

For example:

```bash
jar --create \
    --file=mods/com.example.util.jar \
    -C mods/com.example.util .
```

You can then have:

```text
mods/
 ├── com.example.util.jar
 └── com.example.app.jar
```

Run:

```bash
java \
    --module-path mods \
    -m com.example.app/com.example.app.Main
```

The important difference is:

```text
-classpath
```

versus:

```text
--module-path
```

For modular applications, the **module path** is normally used.

---

# 8. Module Path vs Classpath

### Traditional application

```bash
java -cp lib/* com.example.Main
```

Everything is primarily treated as classpath entries.

### Modular application

```bash
java --module-path mods \
     -m com.example.app/com.example.app.Main
```

The JVM knows:

```text
Module name
     ↓
com.example.app

Required modules
     ↓
com.example.util
```

The module system can construct a **module graph**.

---

# 9. Service Provider Architecture

One of the most powerful JPMS features is the service mechanism.

Suppose we want:

```text
Application
    |
    ↓
Payment Service Interface
    |
    ↓
Payment Provider
```

Create an API module:

```java
module com.example.payment.api {

    exports com.example.payment;
}
```

Interface:

```java
package com.example.payment;

public interface PaymentService {

    void pay(double amount);
}
```

The application declares:

```java
module com.example.app {

    requires com.example.payment.api;

    uses com.example.payment.PaymentService;
}
```

Notice:

```java
uses com.example.payment.PaymentService;
```

This says:

> This module wants to discover implementations of this service.

---

## Provider Module

Implementation:

```java
package com.example.payment.impl;

import com.example.payment.PaymentService;

public class CreditCardPayment
        implements PaymentService {

    @Override
    public void pay(double amount) {
        System.out.println(
            "Paid ₹" + amount + " using credit card"
        );
    }
}
```

Provider module:

```java
module com.example.payment.creditcard {

    requires com.example.payment.api;

    provides com.example.payment.PaymentService
        with com.example.payment.impl.CreditCardPayment;
}
```

The provider does **not** need to export its implementation package.

That's an important feature.

---

# 10. Discovering the Service

The application can use:

```java
ServiceLoader<PaymentService> loader =
        ServiceLoader.load(PaymentService.class);

for (PaymentService service : loader) {

    service.pay(1000);
}
```

Architecture:

```text
             PaymentService
                   ↑
          ┌────────┴────────┐
          │                 │
 CreditCardPayment     UpiPayment
          │                 │
          └────────┬────────┘
                   │
              ServiceLoader
                   │
                   ↓
              Application
```

This provides a loosely coupled architecture where the application does not directly instantiate the implementation.

---

# 11. Complete JPMS Example

A good classroom project would look like:

```text
modular-app/
│
├── src/
│   │
│   ├── com.example.payment.api/
│   │   ├── module-info.java
│   │   └── com/example/payment/
│   │       └── PaymentService.java
│   │
│   ├── com.example.payment.creditcard/
│   │   ├── module-info.java
│   │   └── com/example/payment/impl/
│   │       └── CreditCardPayment.java
│   │
│   └── com.example.app/
│       ├── module-info.java
│       └── com/example/app/
│           └── Main.java
```

The dependency graph becomes:

```text
                  ┌─────────────────────┐
                  │ com.example.app     │
                  │                     │
                  │ uses PaymentService │
                  └──────────┬──────────┘
                             │
                         requires
                             ↓
                  ┌─────────────────────┐
                  │ payment.api         │
                  │                     │
                  │ PaymentService      │
                  └──────────┬──────────┘
                             ↑
                         provides
                             │
                  ┌──────────┴──────────┐
                  │ payment.creditcard  │
                  │                     │
                  │ CreditCardPayment   │
                  └─────────────────────┘
```

This example demonstrates the most important JPMS concepts:

```text
module-info.java
       ↓
requires
       ↓
exports
       ↓
uses
       ↓
provides ... with
       ↓
ServiceLoader
```

### A useful way to remember JPMS

Think of a module as a **Java component with a contract**:

```text
┌──────────────────────────────────────┐
│              MODULE                  │
│                                      │
│  Public API                          │
│  ──────────                          │
│  exports com.example.api             │
│                                      │
│  Dependencies                        │
│  ────────────                        │
│  requires other.module               │
│                                      │
│  Internal implementation             │
│  ─────────────────────               │
│  NOT exported                        │
│                                      │
│  Reflection                          │
│  ──────────                          │
│  opens package                       │
│                                      │
└──────────────────────────────────────┘
```

This is the essence of **Java 9 JPMS: explicit dependencies + strong encapsulation + modular deployment**.

Below are four progressively advanced **JPMS hands-on examples**. All examples can be compiled with **JDK 9+**; the commands work particularly well with modern JDKs.

# 1. Simple Two-Module Application — Utility → App

This demonstrates:

* `module-info.java`
* `requires`
* `exports`
* module path
* compiling and running modules

## Project structure

```text
two-module-app/
└── src/
    ├── com.example.utility/
    │   ├── module-info.java
    │   └── com/example/utility/
    │       └── Calculator.java
    │
    └── com.example.app/
        ├── module-info.java
        └── com/example/app/
            └── Main.java
```

### Module 1: Utility

`Calculator.java`

```java
package com.example.utility;

public class Calculator {

    public int add(int a, int b) {
        return a + b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }
}
```

`module-info.java`

```java
module com.example.utility {

    exports com.example.utility;
}
```

The important part is:

```java
exports com.example.utility;
```

It makes the package available to modules that read this module.

---

### Module 2: App

`module-info.java`

```java
module com.example.app {

    requires com.example.utility;
}
```

`Main.java`

```java
package com.example.app;

import com.example.utility.Calculator;

public class Main {

    public static void main(String[] args) {

        Calculator calculator = new Calculator();

        System.out.println(
            "Addition = " + calculator.add(10, 20)
        );

        System.out.println(
            "Multiplication = " + calculator.multiply(10, 20)
        );
    }
}
```

## Compile

Create a `mods` directory:

```bash
mkdir mods
```

Compile Utility:

```bash
javac \
  -d mods/com.example.utility \
  src/com.example.utility/module-info.java \
  src/com.example.utility/com/example/utility/Calculator.java
```

Compile App:

```bash
javac \
  --module-path mods \
  -d mods/com.example.app \
  src/com.example.app/module-info.java \
  src/com.example.app/com/example/app/Main.java
```

## Run

```bash
java \
  --module-path mods \
  -m com.example.app/com.example.app.Main
```

Output:

```text
Addition = 30
Multiplication = 200
```

### What happened?

The module graph is:

```text
com.example.app
       |
       | requires
       ↓
com.example.utility
       |
       | exports
       ↓
com.example.utility
```

The App module **cannot use** packages from Utility that are not exported.

---

# 2. Three-Module Application — API → Service → Client

Now let's introduce a more realistic architecture:

```text
Client
   |
   | requires
   ↓
Service
   |
   | requires
   ↓
API
```

This is useful for demonstrating **layering and separation of responsibilities**.

## Project structure

```text
three-module-app/
└── src/
    ├── com.example.api/
    │   ├── module-info.java
    │   └── com/example/api/
    │       └── UserService.java
    │
    ├── com.example.service/
    │   ├── module-info.java
    │   └── com/example/service/
    │       └── UserServiceImpl.java
    │
    └── com.example.client/
        ├── module-info.java
        └── com/example/client/
            └── Main.java
```

## Module 1 — API

`UserService.java`

```java
package com.example.api;

public interface UserService {

    String getUserName(int id);
}
```

`module-info.java`

```java
module com.example.api {

    exports com.example.api;
}
```

The API module contains only the contract.

---

## Module 2 — Service

`UserServiceImpl.java`

```java
package com.example.service;

import com.example.api.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public String getUserName(int id) {

        return switch (id) {
            case 1 -> "John";
            case 2 -> "Mary";
            default -> "Unknown";
        };
    }
}
```

`module-info.java`

```java
module com.example.service {

    requires com.example.api;

    exports com.example.service;
}
```

The dependency is:

```text
Service
   |
   └── requires API
```

---

## Module 3 — Client

`Main.java`

```java
package com.example.client;

import com.example.service.UserServiceImpl;

public class Main {

    public static void main(String[] args) {

        UserServiceImpl service =
                new UserServiceImpl();

        System.out.println(service.getUserName(1));
        System.out.println(service.getUserName(2));
        System.out.println(service.getUserName(100));
    }
}
```

`module-info.java`

```java
module com.example.client {

    requires com.example.service;
}
```

## Compile

Compile API:

```bash
javac \
  -d mods/com.example.api \
  src/com.example.api/module-info.java \
  src/com.example.api/com/example/api/UserService.java
```

Compile Service:

```bash
javac \
  --module-path mods \
  -d mods/com.example.service \
  src/com.example.service/module-info.java \
  src/com.example.service/com/example/service/UserServiceImpl.java
```

Compile Client:

```bash
javac \
  --module-path mods \
  -d mods/com.example.client \
  src/com.example.client/module-info.java \
  src/com.example.client/com/example/client/Main.java
```

Run:

```bash
java \
  --module-path mods \
  -m com.example.client/com.example.client.Main
```

Output:

```text
John
Mary
Unknown
```

### Dependency graph

```text
┌───────────────────┐
│ com.example.client│
└─────────┬─────────┘
          │
       requires
          ↓
┌───────────────────┐
│ com.example.service│
└─────────┬─────────┘
          │
       requires
          ↓
┌───────────────────┐
│ com.example.api   │
└───────────────────┘
```

### Architectural problem

Although this works, the Client is directly coupled to:

```java
UserServiceImpl
```

That's not ideal.

A better design is:

```text
Client → API ← Service
```

The Client depends only on the interface.

That's where **SPI/service loading** becomes useful.

---

# 3. Service Provider Interface — `uses` and `provides`

This is one of the most important JPMS examples.

We will build:

```text
             ┌─────────────────────┐
             │     API Module      │
             │                     │
             │ PaymentService      │
             └──────────┬──────────┘
                        ↑
                  implements
                        │
             ┌──────────┴──────────┐
             │ Provider Module     │
             │                     │
             │ UpiPaymentService   │
             └─────────────────────┘

                        ↑
                        │ ServiceLoader
                        │
             ┌──────────┴──────────┐
             │ Client/Application   │
             └─────────────────────┘
```

The Client doesn't know the implementation class.

## Project structure

```text
spi-example/
└── src/
    ├── com.example.payment.api/
    │   ├── module-info.java
    │   └── com/example/payment/
    │       └── PaymentService.java
    │
    ├── com.example.payment.upi/
    │   ├── module-info.java
    │   └── com/example/payment/upi/
    │       └── UpiPaymentService.java
    │
    └── com.example.payment.client/
        ├── module-info.java
        └── com/example/client/
            └── Main.java
```

## API module

`PaymentService.java`

```java
package com.example.payment;

public interface PaymentService {

    void pay(double amount);
}
```

`module-info.java`

```java
module com.example.payment.api {

    exports com.example.payment;
}
```

---

## Provider module

`UpiPaymentService.java`

```java
package com.example.payment.upi;

import com.example.payment.PaymentService;

public class UpiPaymentService
        implements PaymentService {

    @Override
    public void pay(double amount) {

        System.out.println(
            "Paid ₹" + amount + " using UPI"
        );
    }
}
```

Now comes the important part.

`module-info.java`

```java
module com.example.payment.upi {

    requires com.example.payment.api;

    provides com.example.payment.PaymentService
        with com.example.payment.upi.UpiPaymentService;
}
```

This means:

```text
provides
    PaymentService

with
    UpiPaymentService
```

In other words:

> This module provides an implementation of `PaymentService`.

Notice that we didn't write:

```java
exports com.example.payment.upi;
```

The implementation package doesn't need to be exposed.

---

## Client module

`module-info.java`

```java
module com.example.payment.client {

    requires com.example.payment.api;

    uses com.example.payment.PaymentService;
}
```

The important directive is:

```java
uses com.example.payment.PaymentService;
```

This means:

> This module wants to consume implementations of `PaymentService`.

---

## Client code

`Main.java`

```java
package com.example.client;

import com.example.payment.PaymentService;

import java.util.ServiceLoader;

public class Main {

    public static void main(String[] args) {

        ServiceLoader<PaymentService> loader =
                ServiceLoader.load(PaymentService.class);

        for (PaymentService service : loader) {

            service.pay(1500);
        }
    }
}
```

Output:

```text
Paid ₹1500.0 using UPI
```

### The important relationship

Provider:

```java
provides PaymentService
    with UpiPaymentService;
```

Consumer:

```java
uses PaymentService;
```

Runtime discovery:

```java
ServiceLoader.load(PaymentService.class);
```

This gives you **loose coupling**.

The Client knows:

```text
PaymentService
```

but does not know:

```text
UpiPaymentService
```

---

# 4. Creating Modular JARs

Now let's package the first example into actual JAR files.

Our compiled modules currently look like:

```text
mods/
├── com.example.utility/
│   ├── module-info.class
│   └── com/example/utility/
│       └── Calculator.class
│
└── com.example.app/
    ├── module-info.class
    └── com/example/app/
        └── Main.class
```

We can convert them to modular JARs.

## Create Utility JAR

```bash
jar \
  --create \
  --file=mods/com.example.utility.jar \
  -C mods/com.example.utility .
```

## Create App JAR

```bash
jar \
  --create \
  --file=mods/com.example.app.jar \
  -C mods/com.example.app .
```

Now:

```text
mods/
├── com.example.utility.jar
└── com.example.app.jar
```

Each JAR is a **modular JAR** because it contains:

```text
module-info.class
```

---

## Inspect the module

Use:

```bash
jar --describe-module \
    --file=mods/com.example.utility.jar
```

You should see something similar to:

```text
com.example.utility
exports com.example.utility
requires java.base mandated
```

For the App:

```bash
jar --describe-module \
    --file=mods/com.example.app.jar
```

You should see:

```text
com.example.app
requires com.example.utility
requires java.base mandated
```

This is a useful command when teaching JPMS because it lets students **inspect the module descriptor inside the JAR**.

---

# Running Modular JARs

Run the application using:

```bash
java \
  --module-path mods \
  -m com.example.app/com.example.app.Main
```

Here:

```text
--module-path mods
```

means:

> Search for modules in the `mods` directory.

And:

```text
-m com.example.app/com.example.app.Main
```

means:

```text
module       /       main class
   ↓                      ↓
com.example.app / com.example.app.Main
```

---

# Complete JPMS Command Flow

For your training/demo, this is a good sequence to demonstrate on the command line:

```bash
# 1. Compile utility module
javac -d mods/com.example.utility \
    src/com.example.utility/module-info.java \
    src/com.example.utility/com/example/utility/Calculator.java

# 2. Compile application module
javac --module-path mods \
    -d mods/com.example.app \
    src/com.example.app/module-info.java \
    src/com.example.app/com/example/app/Main.java

# 3. Create modular JAR
jar --create \
    --file=mods/com.example.utility.jar \
    -C mods/com.example.utility .

jar --create \
    --file=mods/com.example.app.jar \
    -C mods/com.example.app .

# 4. Inspect module
jar --describe-module \
    --file=mods/com.example.utility.jar

# 5. Run
java --module-path mods \
    -m com.example.app/com.example.app.Main
```

## The four examples demonstrate four levels of JPMS

| Example                    | Main concepts                                |
| -------------------------- | -------------------------------------------- |
| **Utility → App**          | `requires`, `exports`, module path           |
| **API → Service → Client** | Layered modules and dependencies             |
| **SPI**                    | `uses`, `provides`, `ServiceLoader`          |
| **Modular JAR**            | Packaging, inspection, module-path execution |

### One particularly important teaching point

Traditional Java dependency:

```text
Client ────────→ Implementation
```

Better modular/SPI architecture:

```text
             ┌─────────────┐
             │     API     │
             │  Interface  │
             └──────┬──────┘
                    ↑
             implements
                    │
             ┌──────┴──────┐
             │   Service   │
             │ Provider    │
             └─────────────┘

Client ─────→ API
   │
   └── ServiceLoader ──→ Provider
```

That separation is the real architectural value of JPMS—not merely putting `module-info.java` into a project.



