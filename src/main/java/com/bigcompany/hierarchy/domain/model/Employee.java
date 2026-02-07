package com.bigcompany.hierarchy.domain.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Employee class contains list of subordinates that reports to the employee
 **/
@Data // includes @Getter, @Setter, @ToString, @EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private String id;
    private String firstName;
    private String lastName;
    private double salary;
    private String managerId;
    private final List<Employee> subordinates = new ArrayList<>();

    public void addSubordinate(Employee employee) {
        subordinates.add(employee);
    }

    public boolean removeSubordinateById(String employeeId) {
        return subordinates
                .removeIf(e-> e.getId().equals(employeeId));
    }

    public boolean removeSubordinate(Employee employee) {
        return subordinates
                .removeIf(e-> e.getId().equals(employee.id));
    }
}

