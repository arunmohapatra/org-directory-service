package com.bigcompany.hierarchy.domain.service;

import com.bigcompany.hierarchy.domain.model.DepthViolation;
import com.bigcompany.hierarchy.domain.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class HierarchyDepthChecker {

    public List<DepthViolation> findDeeplyNestedEmployees(Employee root, int maxDepth) {
        List<DepthViolation> violations = new ArrayList<>();
        traverse(root, 0, maxDepth, violations);
        return violations;
    }

    // Employee reporting Hierarchy if more than 4 report it
    // How much it is Violating need to identify i.e. Current Depth - Max permissible depth
    private void traverse(Employee employee, int depth, int maxDepth, List<DepthViolation> violations) {
        if (depth > maxDepth) {
            violations.add(new DepthViolation(employee, depth - maxDepth));
        }

        if(employee != null) {
            for (Employee subordinate : employee.getSubordinates()) {
                traverse(subordinate, depth + 1, maxDepth, violations);
            }
        }
    }
}
