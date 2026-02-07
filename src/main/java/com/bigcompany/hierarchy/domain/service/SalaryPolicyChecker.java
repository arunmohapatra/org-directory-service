package com.bigcompany.hierarchy.domain.service;

import com.bigcompany.hierarchy.domain.model.Employee;
import com.bigcompany.hierarchy.domain.model.SalaryViolation;

import java.util.ArrayList;
import java.util.List;

public class SalaryPolicyChecker {

    public List<SalaryViolation> checkSalaryPolicy(Employee root) {
        List<SalaryViolation> violations = new ArrayList<>();
        checkRecursive(root, violations);
        return violations;
    }

    /*
     * 1. Calculate the Average Salary of the SubOridinates belongs to the employee
     * 2. Calclate which is the minium allow salary for employee which is  "avgSubSalary * 1.20"
     * 3. Similary which is the maximum allow salary for employee which is "avgSubSalary * 1.5"
     * 4. Return the list of emloyee who is kind of violating this
     */
    private void checkRecursive(Employee manager, List<SalaryViolation> violations) {
        List<Employee> subs = manager.getSubordinates();

        if (!subs.isEmpty()) {
            double avgSubSalary = subs.stream()
                    .mapToDouble(Employee::getSalary)
                    .average()
                    .orElse(0);

            double minAllowed = avgSubSalary * 1.20;
            double maxAllowed = avgSubSalary * 1.50;

            if (manager.getSalary() < minAllowed || manager.getSalary() > maxAllowed) {
                double diff = manager.getSalary() < minAllowed
                        ? minAllowed - manager.getSalary()
                        : manager.getSalary() - maxAllowed;

                violations.add(new SalaryViolation(manager, avgSubSalary, diff));
            }

            for (Employee e : subs) {
                checkRecursive(e, violations);
            }
        }
    }
}
