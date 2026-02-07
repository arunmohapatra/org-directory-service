# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Employee Hierarchy Service - A Java application that reads employee data from CSV, builds an organizational hierarchy tree, and validates business rules around salary policies and reporting depth.

## Build & Development Commands

Maven project using Java 17. Use Maven commands directly (no wrapper present):

- **Build**: `mvn clean install`
- **Compile**: `mvn compile`
- **Run tests**: `mvn test`
- **Run single test**: `mvn test -Dtest=ClassName#methodName`
- **Run main application**: `mvn exec:java -Dexec.mainClass="com.bigcompany.hierarchy.app.EmployeeHierarchyApp"`

Note: The application expects `employees.csv` in the project root directory. The file path is currently hardcoded in EmployeeHierarchyApp (absolute Windows path). The CSV must follow this format:
```
Id,firstName,lastName,salary,managerId
123,Joe,Doe,60000,
124,Martin,Chekov,45000,123
```
Root employees (CEOs) have empty managerId field.

## Architecture

This codebase follows **Hexagonal Architecture** (Ports and Adapters pattern):

### Layer Structure

```
domain/
  model/         - Core domain entities (Employee, SalaryViolation, DepthViolation)
  port/
    out/         - Output port interfaces (EmployeeRepository)
    service/     - Domain services (EmployeeTreeBuilder)
  service/       - Policy checker services (SalaryPolicyChecker, HierarchyDepthChecker)
adapter/
  out/           - Adapters implementing output ports (FileEmployeeRepository)
app/             - Application entry point (EmployeeHierarchyApp)
```

### Key Domain Concepts

**Employee Tree Building**: Employees are loaded flat from CSV and assembled into a tree structure. Each Employee contains a list of subordinates. Root employees are identified by null managerId.

**Salary Policy Validation**: Managers must earn 20-50% more than their subordinates' average salary. Violations are calculated recursively through the tree.

**Hierarchy Depth Validation**: Reports employees whose reporting chain exceeds a maximum depth (default: 4 levels).

### Important Implementation Details

- Employee uses Lombok annotations (@Data, @AllArgsConstructor, @NoArgsConstructor)
- The subordinates list in Employee is final and initialized to an empty ArrayList
- EmployeeTreeBuilder uses a HashMap for O(1) employee lookup by ID
- Both validation services (SalaryPolicyChecker, HierarchyDepthChecker) traverse the tree recursively
- The application supports multiple root employees (multiple CEOs scenario)

### Data Flow

1. FileEmployeeRepository reads CSV → List<Employee>
2. EmployeeTreeBuilder assembles flat list into tree structure
3. Root employees are passed to validation services
4. Each service recursively traverses tree and collects violations

## Dependencies

- JUnit 5.11.0 for testing
- Lombok 1.18.32 for reducing boilerplate
- Java 17 language level

## Testing

Test directory structure exists at `src/test/java/` but currently contains only a placeholder test. Most domain logic (tree building, salary validation, depth checking) does not yet have test coverage.

## Known Issues

**HierarchyDepthChecker bug in EmployeeHierarchyApp**: The `findDeeplyNestedEmployees()` method returns a list of violations, but the main application creates an empty `violations` list that is never populated with the results. Line 61 should capture the return value:
```java
violations.addAll(hierarchyDepthChecker.findDeeplyNestedEmployees(employee, 4));
```
