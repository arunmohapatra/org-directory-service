package com.bigcompany.hierarchy.domain.port.out;

import com.bigcompany.hierarchy.domain.model.Employee;

import java.util.List;

public interface EmployeeRepository {
    /**
     * Retrieve all employees from the data source.
     * And return list of all employees
     */
    List<Employee> getAllEmployees();
}
