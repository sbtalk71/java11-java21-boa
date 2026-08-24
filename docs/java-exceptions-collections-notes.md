# Classroom Notes: Exception Handling & Collections Framework

---

## Part 1: Exception Handling and Best Practices

### 1.1 What is an Exception?

An exception is an event that disrupts the normal flow of a program's instructions during execution. Java uses an object-oriented approach to handle these events — when something goes wrong, an **exception object** is created and "thrown."

### 1.2 The Exception Hierarchy

```
                    Throwable
                   /          \
              Error            Exception
           (JVM/system         /          \
            level, not      Checked      RuntimeException
            recoverable)   Exceptions      (Unchecked)
                                            
```

- **`Throwable`** — root of the entire hierarchy.
- **`Error`** — serious problems an application shouldn't try to catch (`OutOfMemoryError`, `StackOverflowError`). These indicate JVM-level issues.
- **`Exception`** — problems a program may want to catch and handle.
  - **Checked Exceptions** — subclasses of `Exception` (excluding `RuntimeException`). Checked at **compile time**. The compiler forces you to either handle them (`try-catch`) or declare them (`throws`). Examples: `IOException`, `SQLException`, `ClassNotFoundException`.
  - **Unchecked Exceptions (Runtime Exceptions)** — subclasses of `RuntimeException`. Not checked at compile time; they usually represent programming bugs. Examples: `NullPointerException`, `ArrayIndexOutOfBoundsException`, `ArithmeticException`, `ClassCastException`, `IllegalArgumentException`.

### 1.3 Core Keywords

| Keyword | Purpose |
|---|---|
| `try` | Wraps code that might throw an exception |
| `catch` | Handles a specific exception type |
| `finally` | Always executes, whether an exception occurred or not (cleanup code) |
| `throw` | Used to explicitly throw an exception instance |
| `throws` | Declares that a method may throw an exception (used in method signature) |

**Basic Syntax**

```java
try {
    int result = 10 / 0; // ArithmeticException
} catch (ArithmeticException e) {
    System.out.println("Cannot divide by zero: " + e.getMessage());
} finally {
    System.out.println("This always runs");
}
```

### 1.4 Multi-Catch and Catch Ordering

```java
try {
    // risky code
} catch (ArithmeticException | NullPointerException e) {
    // handle multiple exception types together (Java 7+)
} catch (Exception e) {
    // generic fallback — must come AFTER specific exceptions
}
```

> **Rule:** Subclasses must be caught before their superclasses. Catching `Exception` first would make subsequent, more specific `catch` blocks unreachable (compile error).

### 1.5 try-with-resources (Java 7+)

Automatically closes resources (anything implementing `AutoCloseable`), removing the need for manual `finally` cleanup.

```java
try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
    System.out.println(br.readLine());
} catch (IOException e) {
    e.printStackTrace();
}
// br.close() is called automatically
```

### 1.6 Custom Exceptions

Create domain-specific exceptions by extending `Exception` (checked) or `RuntimeException` (unchecked).

```java
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}

// usage
if (balance < amount) {
    throw new InsufficientBalanceException("Balance too low for withdrawal");
}
```

### 1.7 Exception Chaining

Preserve the original cause when wrapping one exception in another — critical for debugging in layered/microservices architectures.

```java
try {
    // low-level DB call
} catch (SQLException e) {
    throw new ServiceException("Failed to fetch user", e); // original cause preserved
}
```

### 1.8 Best Practices

1. **Catch specific exceptions, not `Exception` or `Throwable`.** Broad catches hide bugs and swallow errors you didn't intend to handle.
2. **Never leave a catch block empty.** At minimum, log it. Silent failures are extremely hard to debug.
3. **Don't use exceptions for normal control flow.** Exceptions are costly (stack trace generation) and meant for exceptional conditions, not routine logic like loop termination.
4. **Always clean up resources** — prefer try-with-resources over manual `finally` blocks.
5. **Preserve the stack trace** when wrapping exceptions — pass the original as the `cause` argument instead of discarding it.
6. **Throw early, catch late.** Validate inputs and fail fast at the source; handle/log exceptions at a layer that has enough context to act (e.g., a global exception handler in a web layer).
7. **Use unchecked exceptions for programming errors** (bad state, invalid arguments) and **checked exceptions for recoverable conditions** the caller can reasonably act on.
8. **Don't use exceptions to signal expected outcomes** (e.g., "record not found" is often better modeled with `Optional` than an exception).
9. **Include meaningful messages** — `"User ID must not be null"` is far more useful than a bare `NullPointerException`.
10. **In Spring Boot**, prefer centralized handling via `@ControllerAdvice` / `@ExceptionHandler` instead of scattering try-catch blocks across every controller.

---

## Part 2: Collections Framework (Quick Coverage)

### 2.1 Why Collections?

Arrays have a fixed size and limited functionality. The **Collections Framework** (`java.util`) provides a unified architecture for storing, retrieving, and manipulating groups of objects — dynamically resizable, with built-in algorithms for sorting, searching, and more.

### 2.2 Collection Interfaces — The Big Picture

```
                    Iterable
                       |
                   Collection
              /        |        \
           List       Set      Queue
                        |
                   SortedSet
                        |
                  NavigableSet

              Map (separate hierarchy — not a Collection)
               |
           SortedMap
               |
          NavigableMap
```

- **`Iterable`** — root interface; anything implementing it can be used in a for-each loop.
- **`Collection`** — root of the List/Set/Queue branch; defines `add()`, `remove()`, `size()`, `contains()`, etc.
- **`Map`** — stores key-value pairs; deliberately **not** a subtype of `Collection` since it operates on pairs, not single elements.

### 2.3 List — Ordered, Allows Duplicates

| Implementation | Backing Structure | Key Traits |
|---|---|---|
| `ArrayList` | Resizable array | Fast random access (`O(1)` get), slower inserts/removes in the middle (`O(n)`). Best default choice. |
| `LinkedList` | Doubly linked list | Fast insert/remove at ends (`O(1)`), slow random access (`O(n)`). Also implements `Deque`. |
| `Vector` | Resizable array (synchronized) | Legacy, thread-safe but slower — generally avoid; use `CopyOnWriteArrayList` or external synchronization instead. |

```java
List<String> names = new ArrayList<>();
names.add("Ravi");
names.add("Anita");
names.get(0); // "Ravi"
```

### 2.4 Set — No Duplicates

| Implementation | Backing Structure | Key Traits |
|---|---|---|
| `HashSet` | Hash table | No ordering guarantee, `O(1)` average add/contains. Most common choice. |
| `LinkedHashSet` | Hash table + linked list | Maintains **insertion order**. |
| `TreeSet` | Red-black tree | Maintains **sorted order** (natural or via `Comparator`); implements `SortedSet`/`NavigableSet`. |

```java
Set<String> tags = new HashSet<>();
tags.add("java");
tags.add("java"); // ignored, no duplicates
```

### 2.5 SortedSet / NavigableSet

`SortedSet` guarantees elements are stored in ascending order. `TreeSet` is the standard implementation and additionally implements `NavigableSet`, which adds navigation methods:

```java
TreeSet<Integer> scores = new TreeSet<>(Set.of(45, 10, 90, 30));
scores.first();      // 10
scores.last();       // 90
scores.higher(30);   // 45  (smallest element strictly greater than 30)
scores.lower(30);    // 10  (largest element strictly less than 30)
scores.headSet(30);  // {10}      — elements < 30
scores.tailSet(30);  // {30, 45, 90} — elements >= 30
```

### 2.6 Map — Key-Value Pairs

| Implementation | Backing Structure | Key Traits |
|---|---|---|
| `HashMap` | Hash table | No ordering, `O(1)` average get/put, allows one `null` key. Default choice. |
| `LinkedHashMap` | Hash table + linked list | Maintains insertion order (or access order, configurable). |
| `TreeMap` | Red-black tree | Sorted by key (natural or `Comparator`); implements `SortedMap`/`NavigableMap`. |
| `Hashtable` | Hash table (synchronized) | Legacy, thread-safe but no `null` keys/values — generally avoid; use `ConcurrentHashMap` instead. |

```java
Map<String, Integer> ages = new HashMap<>();
ages.put("Ravi", 28);
ages.put("Anita", 32);
ages.get("Ravi");            // 28
ages.getOrDefault("Sam", 0);  // 0
ages.containsKey("Anita");    // true
```

### 2.7 Iterator — Explicit Traversal

`Iterator` is the standard way to traverse any `Collection`, and it's the **only safe way to remove elements while iterating**.

```java
Iterator<String> it = names.iterator();
while (it.hasNext()) {
    String name = it.next();
    if (name.equals("Anita")) {
        it.remove(); // safe removal during iteration
    }
}
```

For `Map`, iterate over `entrySet()`:

```java
Iterator<Map.Entry<String, Integer>> it = ages.entrySet().iterator();
while (it.hasNext()) {
    Map.Entry<String, Integer> entry = it.next();
    System.out.println(entry.getKey() + " -> " + entry.getValue());
}
```

> **Important:** Modifying a collection directly (e.g., calling `names.remove()`) while iterating with a for-each loop or a "raw" iterator throws a `ConcurrentModificationException`. Always use `Iterator.remove()` for safe removal during traversal.

### 2.8 Enhanced For Loop (for-each)

Simpler syntax built on top of `Iterable`/`Iterator`, used when you only need to **read** elements (no removal, no index needed).

```java
for (String name : names) {
    System.out.println(name);
}

for (Map.Entry<String, Integer> entry : ages.entrySet()) {
    System.out.println(entry.getKey() + " = " + entry.getValue());
}
```

**Iterator vs Enhanced For Loop — Quick Comparison**

| Aspect | `Iterator` | Enhanced For Loop |
|---|---|---|
| Removal during traversal | Safe (`it.remove()`) | Not safe — throws `ConcurrentModificationException` |
| Access to index/position | No (unless `ListIterator`) | No |
| Code verbosity | More verbose | Cleaner, more readable |
| Best used when | You need to modify the collection while iterating | You only need to read elements |

### 2.9 Quick Decision Guide

- Need ordered, duplicate-allowing data with fast index access → **`ArrayList`**
- Frequent insertions/deletions at the ends → **`LinkedList`**
- Need uniqueness, don't care about order → **`HashSet`**
- Need uniqueness + insertion order → **`LinkedHashSet`**
- Need uniqueness + sorted order → **`TreeSet`**
- Need key-value lookup, don't care about order → **`HashMap`**
- Need key-value lookup + insertion order → **`LinkedHashMap`**
- Need key-value lookup + sorted by key → **`TreeMap`**
