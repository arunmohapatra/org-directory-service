package com.bigcompany.hierarchy.app;

import com.bigcompany.hierarchy.adapter.out.FileEmployeeRepository;
import com.bigcompany.hierarchy.domain.model.DepthViolation;
import com.bigcompany.hierarchy.domain.model.Employee;
import com.bigcompany.hierarchy.domain.model.SalaryViolation;
import com.bigcompany.hierarchy.domain.port.out.EmployeeRepository;
import com.bigcompany.hierarchy.domain.port.service.EmployeeTreeBuilder;
import com.bigcompany.hierarchy.domain.service.HierarchyDepthChecker;
import com.bigcompany.hierarchy.domain.service.SalaryPolicyChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 */
public class EmployeeHierarchyApp {
    public static void main(String[] args) {

        // Read employee data from CSV file and create list of employees
        EmployeeRepository repo
                = new FileEmployeeRepository("C:\\work\\org-directory-service\\employees.csv");
        List<Employee> employees = repo.getAllEmployees();


        //Build employee tree
        EmployeeTreeBuilder employeeTreeBuilder =
                new EmployeeTreeBuilder();

        List<Employee> rootEmployee =
                employeeTreeBuilder.buildEmployeeTree(employees);

        rootEmployee.forEach(employee -> {
            System.out.println(employee.getFirstName()+" "+ employee.getLastName()+" <--- is top in the hierachy");
        });

        // Check the average salary of employee based upon the criteria given
        // I.e. Salary of the employee should not less than 20% of average  salary of subordinates
        // And Salary of the employee should not more than 50% of average salary of subordinates

        SalaryPolicyChecker salaryPolicyChecker =
                new SalaryPolicyChecker();
        List<SalaryViolation> employeesViolatingSalary
                = new ArrayList<>();
        rootEmployee.forEach(employee -> {
            employeesViolatingSalary.addAll(salaryPolicyChecker.checkSalaryPolicy(employee));
        });

        employeesViolatingSalary.forEach(empSalaryViolation->{
            System.out.println(employeesViolatingSalary.toString());
        });

        // Check the depth of reporting hierachy
        HierarchyDepthChecker hierarchyDepthChecker
                = new HierarchyDepthChecker();
        List<DepthViolation> violations =
                new ArrayList<>();
        rootEmployee.forEach( employee -> {
            hierarchyDepthChecker.findDeeplyNestedEmployees(employee, 4);
        });

        for (DepthViolation violation : violations) {
            System.out.println(violation);
        }
    }
}
