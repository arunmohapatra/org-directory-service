package com.bigcompany.hierarchy.domain.port.service;

import com.bigcompany.hierarchy.domain.model.Employee;

import java.util.*;
import java.util.stream.Collectors;

public class EmployeeTreeBuilder {

    /*
     * Store all employee in a (Hash)Map for faster retrival base upon
     * the Employee id
     */
    private Map<String, Employee> employeeMap(List<Employee> allEmployees) {
        Map<String, Employee> employeeMap =
                new HashMap<>();

        allEmployees.forEach(employee -> {
            employeeMap.put(employee.getId(), employee);
        });

        return employeeMap;
    }

    public List<Employee> buildEmployeeTree(List<Employee> allEmployees) {

        Map<String, Employee> employeeMap
                = employeeMap(allEmployees);

        // Get all employees
        Collection<Employee> employees = employeeMap.values();

        // Find out root of the employee Hierachy
        // Employee who does no have manager be the root , might be CEO of the company
        // There might be a chance , there will be multiple CEO

        List<Employee> rootEmployee = employees.stream().filter(employee -> {
            if(employee.getManagerId() == null)
                return true;
            else
                return false;
        }).collect(Collectors.toList());

        //Now construct the employee Hierachy

        employees.forEach(employee -> {
            String managerIdOfemployee =
                    employee.getManagerId();
            Employee manager = employeeMap.get(managerIdOfemployee);
            // Add employee as subOridinate of that employee
            // Do the same for all employees except the root i.e. CEO
            // It does not have manager
            if(manager != null)
                manager.addSubordinate(employee);
        });

        return rootEmployee;
    }
}
