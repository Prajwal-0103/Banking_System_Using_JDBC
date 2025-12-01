# Banking System (JDBC)

Simple console banking application built with Java and JDBC. It lets users register, open an account, deposit/withdraw, transfer funds, and check balances against a MySQL database.

## Prerequisites
- `Java` (JDK 8+)
- `MySQL` server (local or accessible remotely)
- MySQL Connector/J driver (`mysql-connector-j-9.5.0.jar` or compatible)

## Database Configuration
The app expects this default connection (see `src/Main.java`):

```
jdbc:mysql://localhost:3306/banking_management_system
user: root
password: <your-password>
```

Update the credentials in `src/Main.java` if yours differ. Avoid committing real passwords when sharing the project.

### Create Schema & Tables
Run the following SQL in your MySQL instance:

```sql
CREATE DATABASE IF NOT EXISTS banking_management_system;
USE banking_management_system;

-- User login table
CREATE TABLE IF NOT EXISTS USER (
  full_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL
);

-- Bank accounts table
CREATE TABLE IF NOT EXISTS accounts (
  acc_num BIGINT PRIMARY KEY,
  full_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  balance DOUBLE NOT NULL,
  pin VARCHAR(32) NOT NULL
);
```

## Build
From the project root on Windows PowerShell:

```powershell
# 1) Download MySQL Connector/J (or copy an existing one into lib/)
New-Item -ItemType Directory -Path "lib" -Force | Out-Null
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/9.5.0/mysql-connector-j-9.5.0.jar" `
  -Headers @{ 'User-Agent' = 'Mozilla/5.0' } -OutFile "lib/mysql-connector-j-9.5.0.jar" -UseBasicParsing

# 2) Create output directory
New-Item -ItemType Directory -Path "out\production\Banking System" -Force | Out-Null

# 3) Compile sources
javac -d "out\production\Banking System" src\*.java
```

## Run
Run with the compiled classes and the driver on the classpath:

```powershell
java -cp "out\production\Banking System;lib\mysql-connector-j-9.5.0.jar" Main
```

## Optional: Package as JAR
This project can run directly from class files. If you prefer a JAR, you can create one without the driver bundled and run it with an explicit classpath:

```powershell
# Create the JAR (no manifest required for classpath-based run)
jar cf "banking-system.jar" -C "out\production\Banking System" .

# Run using explicit classpath (JAR + driver)
java -cp "banking-system.jar;lib\mysql-connector-j-9.5.0.jar" Main
```

To make `java -jar banking-system.jar` work without specifying the driver on the command line, build a fat JAR (bundling the driver) using a build tool (e.g., Maven/Gradle) or a third-party packager.

## Usage Notes
- Email must be a `@gmail.com` address (see validation in `src/User.java`).
- Password must include upper/lowercase, digit, and special character (min length 8).
- Account numbers auto-increment from the last existing account; first account defaults to `10000100`.
- PIN is required for credit/debit/transfer/balance operations.

## Troubleshooting
- "No suitable driver" error: ensure the MySQL Connector/J JAR is on the classpath when running.
- Connection errors: verify MySQL is running and credentials/URL match your setup.
- Schema issues: confirm tables exist exactly as defined above.

## Project Structure
```
src/
  Main.java
  User.java
  Accounts.java
  Account_manager.java
out/production/Banking System/   # compiled classes (created after build)
lib/                             # MySQL driver jar (you create this)
```