package com.bigcompany.hierarchy.domain.service;

import com.bigcompany.hierarchy.domain.model.Employee;
import com.bigcompany.hierarchy.domain.model.SalaryViolation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;

class SalaryPolicyCheckerTest {

    @Test
    void checkSalaryPolicy_shouldReturnNoViolationsWhenWithinLimits() {
        // Subordinates with average salary of 1000
        Employee subordinate1 = new Employee("2", "John", "Doe", 1000.0, "1");
        Employee subordinate2 = new Employee("3", "Jane", "Smith", 1000.0, "1");

        // Manager with salary within 1.20x to 1.50x of 1000 = 1200 to 1500
        Employee manager = new Employee("1", "Manager", "Boss", 1300.0, null);
        manager.addSubordinate(subordinate1);
        manager.addSubordinate(subordinate2);

        SalaryPolicyChecker checker = new SalaryPolicyChecker();
        List<SalaryViolation> violations = checker.checkSalaryPolicy(manager);

        assertThat(violations).isEmpty();
    }

}
