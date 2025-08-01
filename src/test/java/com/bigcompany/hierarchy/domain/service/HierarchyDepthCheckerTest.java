package com.bigcompany.hierarchy.domain.service;

import com.bigcompany.hierarchy.domain.model.Employee;
import com.bigcompany.hierarchy.domain.model.DepthViolation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HierarchyDepthCheckerTest {

    private final HierarchyDepthChecker checker = new HierarchyDepthChecker();

    @Test
    void testNoViolationWhenDepthWithinLimit() {
        Employee ceo = new Employee("1", "Alice", "CEO", 100000.0, null);
        Employee manager = new Employee("2", "Bob", "Manager", 80000.0, "1");
        Employee engineer = new Employee("3", "Charlie", "Engineer", 60000.0, "2");

        ceo.addSubordinate(manager);
        manager.addSubordinate(engineer);

        int maxDepth = 3;

        List<DepthViolation> violations = checker.findDeeplyNestedEmployees(ceo, maxDepth);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testViolationDetectedBeyondMaxDepth() {
        // Create hierarchy: CEO -> M1 -> M2 -> M3 -> E1 (depth = 4)
        Employee ceo = new Employee("1", "Alice", "CEO", 100000.0, null);
        Employee m1 = new Employee("2", "Bob", "Manager", 90000.0, "1");
        Employee m2 = new Employee("3", "Carol", "Manager", 80000.0, "2");
        Employee m3 = new Employee("4", "Dan", "Manager", 75000.0, "3");
        Employee e1 = new Employee("5", "Eve", "Engineer", 60000.0, "4");

        ceo.addSubordinate(m1);
        m1.addSubordinate(m2);
        m2.addSubordinate(m3);
        m3.addSubordinate(e1);

        int maxDepth = 3;

        List<DepthViolation> violations = checker.findDeeplyNestedEmployees(ceo, maxDepth);

        assertEquals(1, violations.size());

        assertFalse(violations.stream().anyMatch(v -> v.getEmployee().getId().equals("4") && v.getExtraDepth() == 1));
        assertFalse(violations.stream().anyMatch(v -> v.getEmployee().getId().equals("5") && v.getExtraDepth() == 2));
    }

    @Test
    void testExactMaxDepthIsNotViolation() {
        Employee ceo = new Employee("1", "Alice", "CEO", 100000.0, null);
        Employee m1 = new Employee("2", "Bob", "Manager", 90000.0, "1");
        Employee m2 = new Employee("3", "Carol", "Manager", 80000.0, "2");

        ceo.addSubordinate(m1);
        m1.addSubordinate(m2);

        int maxDepth = 2;

        List<DepthViolation> violations = checker.findDeeplyNestedEmployees(ceo, maxDepth);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testRootOnly() {
        Employee ceo = new Employee("1", "Alice", "CEO", 100000.0, null);
        int maxDepth = 0;

        List<DepthViolation> violations = checker.findDeeplyNestedEmployees(ceo, maxDepth);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testNullSubordinatesHandled() {
        Employee ceo = new Employee("1", "Alice", "CEO", 100000.0, null);
        ceo.addSubordinate(null); // explicitly set null list

        int maxDepth = 2;
        List<DepthViolation> violations = checker.findDeeplyNestedEmployees(ceo, maxDepth);
        assertTrue(violations.isEmpty());
    }
}
