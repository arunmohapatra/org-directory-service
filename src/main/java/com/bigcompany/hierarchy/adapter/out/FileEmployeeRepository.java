package com.bigcompany.hierarchy.adapter.out;

import com.bigcompany.hierarchy.domain.model.Employee;
import com.bigcompany.hierarchy.domain.port.out.EmployeeRepository;
import com.sun.java.accessibility.util.EventQueueMonitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileEmployeeRepository implements EmployeeRepository {

    private final String csvFilePath;

    public FileEmployeeRepository(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(csvFilePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // skip the header
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length < 4) {
                    continue; // skip invalid lines
                }

                String id = parts[0].trim();
                String firstName = parts[1].trim();
                String lastName = parts[2].trim();
                double salary = Double.parseDouble(parts[3].trim());
                String managerId = (parts.length >= 5 && !parts[4].trim().isEmpty()) ? parts[4].trim() : null;

                Employee emp = new Employee(id, firstName, lastName, salary, managerId);
                employees.add(emp);
            }

        } catch (IOException e) {
            System.err.println("Error reading employee CSV file: " + e.getMessage());
        }

        return employees;
    }

}
