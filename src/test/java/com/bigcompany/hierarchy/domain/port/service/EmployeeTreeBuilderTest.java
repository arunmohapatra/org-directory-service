package com.bigcompany.hierarchy.domain.port.service;

import com.bigcompany.hierarchy.domain.model.Employee;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTreeBuilderTest {

    private final EmployeeTreeBuilder treeBuilder = new EmployeeTreeBuilder();

    @Test
    void testSingleRootHierarchy() {
        List<Employee> input = List.of(
                new Employee("1", "Alice", "CEO", 100000.0, null),
                new Employee("2", "Bob", "Manager", 80000.0, "1"),
                new Employee("3", "Charlie", "Engineer", 60000.0, "2"),
                new Employee("4", "David", "Engineer", 60000.0, "2")
        );

        List<Employee> roots = treeBuilder.buildEmployeeTree(input);

        assertEquals(1, roots.size());
        Employee root = roots.get(0);
        assertEquals("1", root.getId());
        assertEquals(1, root.getSubordinates().size());

        Employee bob = root.getSubordinates().get(0);
        assertEquals("2", bob.getId());
        assertEquals(2, bob.getSubordinates().size());
    }

    @Test
    void testMultipleRoots() {
        List<Employee> input = List.of(
                new Employee("1", "Alice", "CEO", 100000.0, null),
                new Employee("5", "Eve", "Another CEO", 95000.0, null),
                new Employee("2", "Bob", "Manager", 80000.0, "1"),
                new Employee("6", "Frank", "Engineer", 60000.0, "5")
        );

        List<Employee> roots = treeBuilder.buildEmployeeTree(input);
        assertEquals(2, roots.size());

        Set<String> rootIds = new HashSet<>();
        roots.forEach(emp -> rootIds.add(emp.getId()));
        assertTrue(rootIds.contains("1"));
        assertTrue(rootIds.contains("5"));
    }

    @Test
    void testOrphanEmployeeWithInvalidManager() {
        List<Employee> input = List.of(
                new Employee("1", "Alice", "CEO", 100000.0, null),
                new Employee("2", "Bob", "Manager", 80000.0, "1"),
                new Employee("3", "Charlie", "Engineer", 60000.0, "999")  // invalid managerId
        );

        List<Employee> roots = treeBuilder.buildEmployeeTree(input);
        assertEquals(1, roots.size());

        Employee root = roots.get(0);
        assertEquals("1", root.getId());
        assertEquals(1, root.getSubordinates().size());

        Employee bob = root.getSubordinates().get(0);
        assertEquals("2", bob.getId());
        assertTrue(bob.getSubordinates().isEmpty());

        // Charlie should not be part of the tree
        boolean foundCharlie = root.getSubordinates().stream()
                .anyMatch(emp -> emp.getId().equals("3"));
        assertFalse(foundCharlie);
    }

    @Test
    void testEmptyInput() {
        List<Employee> input = new ArrayList<>();
        List<Employee> roots = treeBuilder.buildEmployeeTree(input);
        assertTrue(roots.isEmpty());
    }
}
