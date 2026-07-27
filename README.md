# 📚 Library Management System

Welcome to the **Library Management System**! This is a simple Java-based backend application designed to manage books, users, and borrowing transactions (loans) safely and efficiently.

---

## 📖 System Overview

The system allows a library administrator to:
1. Register new **Books** and **Users**.
2. Track active **Loans** (who borrowed which book and when it is due).
3. Prevent duplicate borrowing of the same book.
4. Clean up **expired loans** automatically.

### Key Features & Design Principles
* **Data Safety (Immutability):** Core domain objects cannot be altered once created.
* **Defensive Copying:** Prevents external code from corrupting internal date or list values.
* **Type-Safe Storage:** Uses generic repositories to store data consistently without code duplication.
* **Custom Error Handling:** Throws clear exceptions when an entity is missing or invalid.

---

## 📂 Project Structure & File Guide

Below is the breakdown of all files in this repository and what each of them does:

```text
library-management/
├── src/main/java/com/library/
│   ├── model/
│   │   ├── Book.java                 # Represents a book entity
│   │   ├── User.java                 # Represents a library member
│   │   └── Loan.java                 # Represents a borrowing transaction
│   ├── repository/
│   │   └── Repository.java           # Generic storage system (CRUD)
│   ├── service/
│   │   └── Library.java              # Central business logic manager
│   ├── exception/
│   │   ├── BookNotFoundException.java # Error thrown when a book isn't found
│   │   └── UserNotFoundException.java # Error thrown when a user isn't found
│   └── app/
│       └── Main.java                 # Manual test runner with visual outputs
└── src/test/java/com/library/
    └── LibraryTest.java              # Automated JUnit test suite


### 1. Main Classes (`com.library.model`)
* **`Book.java`**: Stores information about a book (ISBN, title, author, release year). It is immutable (cannot be changed after creation) and uses the ISBN for comparison.
* **`User.java`**: Stores user information (User ID, name, email). Uses the User ID for identification.
* **`Loan.java`**: Links a `Book` and a `User` together with a `borrowDate` and `dueDate`. It uses defensive copies for `Date` objects so outside code cannot manipulate the timeline.

### 2. Data Files (`com.library.repository`)
* **`Repository.java`**: A generic in-memory database wrapper powered by a `HashMap`. It provides type-safe methods to add, retrieve (`Optional<T>`), list, and remove items by their unique key.

### 3. Working Logic (`com.library.service`)
* **`Library.java`**: The main controller of the system. It connects the repositories and the loans list. It handles checking out books, validating entity existence, preventing double-borrowing, and safely removing expired loans using an `Iterator`.

### 4. Exceptions (`com.library.exception`)
* **`BookNotFoundException.java` & `UserNotFoundException.java`**: Unchecked runtime exceptions triggered whenever an operation searches for a missing book or user.

### 5. Application & Tests
* **`Main.java`**: A console application that runs scenario tests and prints detailed step-by-step logs.
* **`LibraryTest.java`**: Unit tests written in JUnit to automatically verify system rules.
### 1. Domain Models (`com.library.model`)
* **`Book.java`**: Stores information about a book (ISBN, title, author, release year). It is immutable (cannot be changed after creation) and uses the ISBN for comparison.
* **`User.java`**: Stores user information (User ID, name, email). Uses the User ID for identification.
* **`Loan.java`**: Links a `Book` and a `User` together with a `borrowDate` and `dueDate`. It uses defensive copies for `Date` objects so outside code cannot manipulate the timeline.

### 2. Data Storage (`com.library.repository`)
* **`Repository.java`**: A generic in-memory database wrapper powered by a `HashMap`. It provides type-safe methods to add, retrieve (`Optional<T>`), list, and remove items by their unique key.

### 3. Business Logic (`com.library.service`)
* **`Library.java`**: The main controller of the system. It connects the repositories and the loans list. It handles checking out books, validating entity existence, preventing double-borrowing, and safely removing expired loans using an `Iterator`.

### 4. Custom Exceptions (`com.library.exception`)
* **`BookNotFoundException.java` & `UserNotFoundException.java`**: Unchecked runtime exceptions triggered whenever an operation searches for a missing book or user.

### 5. Application & Tests
* **`Main.java`**: A console application that runs scenario tests and prints detailed step-by-step logs.
* **`LibraryTest.java`**: Unit tests written in JUnit to automatically verify system rules.

---

## ⚙️ How the System Works (Workflow)

```text
[User Request] ──> [Library Service] ──> Checks Repositories & Loan List
                         │
                         ├── Validates input rules (repOk)
                         ├── Prevents duplicate loans
                         └── Returns safe copies of data
