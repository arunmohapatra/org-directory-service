package com.bigcompany.hierarchy.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryViolation {
    private Employee manager;
    private double avgSubordinateSalary;
    private double deviation;

    public String toString() {
        return String.format("%s %s] as manager earns [%.2f] away from allowed range [%.2f–%.2f",
                manager.getFirstName(), manager.getLastName(),
                deviation,
                avgSubordinateSalary * 1.20,
                avgSubordinateSalary * 1.50);
    }
}
