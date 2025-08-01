package com.bigcompany.hierarchy.domain.model;

import com.bigcompany.hierarchy.domain.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepthViolation {
    private Employee employee;
    private int extraDepth;

    @Override
    public String toString() {
        return "DepthViolation{" +
                "employee=" + employee.getFirstName() + " " + employee.getLastName() +
                ", extraDepth=" + extraDepth +
                '}';
    }
}
