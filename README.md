# Back-end for Banking Application

A robust banking system built with Spring Boot that provides secure account management and comprehensive transaction services.

## 🚀 Features

### Account Management
- User Registration
- Secure Authentication
- Account Deletion
- Profile Management

### Transaction Services
- Money Withdrawal
- Deposits
- Inter-account Transfers
- Loan Processing
- Bank Deposits

### Security
- Basic Authentication
- JWT Token Implementation
- Encrypted Data Transfer
- Secure Session Management

## 🛠 Tech Stack

- **Java 17**
- **Spring Boot 3.x**
- **Spring Security**
- **MySQL Database**
- **Maven**
- **JWT**

## 📋 Prerequisites

- JDK 17 or later
- Maven 3.6+
- MySQL 8.0+

## 🔧 Installation

1. Clone the repository
```bash
git clone https://github.com/yourusername/banking-app.git

README.md
Configure MySQL database
spring.datasource.url=jdbc:mysql://localhost:3306/banking_app
spring.datasource.username=your_username
spring.datasource.password=your_password

Build the project
mvn clean install

Run the application
mvn spring-boot:run

🔒 Security Configuration
The application implements a dual-layer security approach:

Basic Authentication for initial access
JWT tokens for maintaining secure sessions
📚 API Documentation
Account Endpoints
POST /api/accounts/createAccount - Create new account
POST /api/accounts/login - User authentication
DELETE /api/accounts/{id} - Delete account
Transaction Endpoints
POST /api/accounts/{id}/withdraw- Withdraw funds
POST /api/accounts/{id}/deposit - Deposit funds
POST /api/accounts/{sender_id}/transfer/(receiver_id) - Transfer between accounts
POST /loans/{id}/takeLoan - Process loan request
POST /deposits/{id}/createDeposit - Make bank deposit
🔄 Database Schema
The application uses a relational database with the following key tables:

Account
Deposit
Loans
Transactions

📄 License
This project is licensed under the MIT License - see the LICENSE.md file for details


