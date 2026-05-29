# 🚀 Selenium Java Automation Framework

A comprehensive Java-based Automation Testing Framework built using Selenium WebDriver, TestNG, Maven, Rest Assured, Apache POI, MySQL, and Extent Reports.

This project demonstrates end-to-end automation concepts including:

- Web UI Automation
- API Testing
- Database Validation
- Excel Data Handling
- Reporting & Logging
- TestNG Framework Design

It is designed for automation engineers, QA professionals, and interview preparation.

---

## 📌 Features

### 🌐 Web UI Automation
- Selenium WebDriver 4
- Browser Automation
- Alert Handling
- Wait Strategies
- Reusable Base Framework

### 🔌 API Testing
- REST API Automation using Rest Assured
- GET, POST, PUT, DELETE validations
- Response Assertions
- JSON Parsing

### 🗄 Database Testing
- MySQL Database Connectivity
- Query Execution
- Data Validation

### 📊 Excel Data Handling
- Apache POI Integration
- Read Excel Files
- Data-Driven Testing Support

### 📈 Reporting
- Extent Reports Integration
- Detailed Test Execution Reports
- Pass/Fail Status Tracking

### ⚙ Framework Utilities
- Configuration Management
- Test Listeners
- Centralized Driver Management
- Reusable Utility Classes

---

## 🏗 Framework Structure

```text
src
├── main
│   └── java
│       ├── base
│       │   └── BaseClass
│       └── utils
│           ├── ConfigManager
│           └── ExtentReportsManager
│
└── test
    └── java
        ├── listeners
        ├── resources
        │   ├── config.properties
        │   ├── testng.xml
        │   └── test_data
        │
        └── tests
            ├── web_ui_tests
            ├── api_tests
            ├── db_tests
            └── excel_tests
```


## 🛠 Technologies Used
| Technology         | Purpose              |
| ------------------ | -------------------- |
| Java 21            | Programming Language |
| Selenium WebDriver | UI Automation        |
| TestNG             | Test Framework       |
| Maven              | Build Management     |
| WebDriverManager   | Driver Management    |
| Rest Assured       | API Testing          |
| MySQL Connector    | Database Testing     |
| Apache POI         | Excel Operations     |
| Extent Reports     | Reporting            |
| Log4j              | Logging              |


## 📂 Test Modules
Web UI Tests
  - Examples include:
  - Alert Handling
  - Explicit Waits
  - Browser Interaction
  - UI Validation
  - API Tests

## Examples include:
  - REST API Requests
  - Response Validation
  - JSON Data Verification
  - Database Tests

## Examples include:
  - Database Connectivity
  - Query Execution
  - Result Validation
  - Excel Tests

## Examples include:
  - Reading Excel Data
  - Data-Driven Testing Concepts


## ▶️ Getting Started
  - Prerequisites
  - Java 21+
  - Maven
  - IntelliJ IDEA / Eclipse
  - Chrome Browser
  - Clone Repository
  - git clone https://github.com/abhishekchinta1997/Selenium_Java.git
  - Install Dependencies
  - mvn clean install
  - Execute Tests
  - mvn test

    Or execute through TestNG Suite:
      - testng.xml
      - 📊 Reporting

After execution, Extent Reports are generated containing:
  - Test Summary
  - Pass/Fail Results
  - Execution Time
  - Detailed Logs


## 🎯 Learning Objectives
  1. This project helps in understanding:
  2. Selenium Automation Framework Design
  3. Page Object Model Concepts
  4. TestNG Annotations & Execution Flow
  5. API Automation with Rest Assured
  6. Database Testing
  7. Excel Data Handling
  8. Reporting & Logging
  9. Maven Project Structure

## 👨‍💻 Suitable For
  1. Automation Test Engineers
  2. Selenium Learners
  3. QA Professionals
  4. Software Testing Students
  5. Interview Preparation


## 🤝 Contributions
Contributions are welcome.
Feel free to fork the repository, enhance the framework, and submit pull requests.


## ⭐ Support
If you find this repository useful, please consider giving it a ⭐ Star.
It helps others discover the project and encourages future improvements.


## 👤 Author
Abhishek Chinta
GitHub: https://github.com/abhishekchinta1997

## Happy Testing! 🚀
