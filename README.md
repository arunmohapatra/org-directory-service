How to Run and Test
-------------------
1. Prerequisites:

- Java 17+
- Maven 3.6+
- Git

2. Clone the Repository:

- git clone https://github.com/arunmohapatra/org-directory-service.git
- cd org-directory-service

3. Build the Project
- mvn clean install
- This will compile the source code and run all unit tests.
4. Run Tests Only:
- mvn test

You can find unit tests inside the src/test/java directory. These tests validate:

- Salary policy violations (SalaryPolicyChecker)
- Reporting line length checks (ReportingLineAnalyzer)
- Employee hierarchy parsing (EmployeeHierarchyParser)


Note: Main is https://github.com/arunmohapatra/org-directory-service/blob/main/src/main/java/com/bigcompany/hierarchy/app/EmployeeHierarchyApp.java
