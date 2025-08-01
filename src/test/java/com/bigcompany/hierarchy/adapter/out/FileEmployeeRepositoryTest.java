package com.bigcompany.hierarchy.adapter.out;

import com.bigcompany.hierarchy.domain.model.Employee;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FileEmployeeRepositoryTest {

    private static Path tempCsvFile;

    @BeforeAll
    static void setup() throws IOException {
        // Create a temporary CSV file with test data
        String csvContent = """
                Id,firstName,lastName,salary,managerId
                123,Joe,Doe,60000,
                124,Martin,Chekov,45000,123
                125,Bob,Ronstad,47000,123
                300,Alice,Hasacat,50000,124
                305,Brett,Hardleaf,34000,300
                """;

        tempCsvFile = Files.createTempFile("employee", ".csv");
        Files.write(tempCsvFile, csvContent.getBytes());
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.deleteIfExists(tempCsvFile);
    }

    @Test
    void testGetAllEmployees() {
        FileEmployeeRepository repository = new FileEmployeeRepository(tempCsvFile.toString());
        List<Employee> employees = repository.getAllEmployees();

        assertEquals(5, employees.size());

        Employee joe = employees.stream().filter(e -> e.getId().equals("123")).findFirst().orElse(null);
        assertNotNull(joe);
        assertEquals("Joe", joe.getFirstName());
        assertEquals("Doe", joe.getLastName());
        assertEquals(60000.0, joe.getSalary());
        assertNull(joe.getManagerId());

        Employee martin = employees.stream().filter(e -> e.getId().equals("124")).findFirst().orElse(null);
        assertNotNull(martin);
        assertEquals("123", martin.getManagerId());
    }

    @Test
    void testInvalidLineSkipped() throws IOException {
        // Add an invalid line to test robustness
        String contentWithInvalid = """
                Id,firstName,lastName,salary,managerId
                123,Joe,Doe,60000,
                Invalid,Line
                124,Martin,Chekov,45000,123
                """;

        Path temp = Files.createTempFile("employee_invalid", ".csv");
        Files.write(temp, contentWithInvalid.getBytes());

        FileEmployeeRepository repository = new FileEmployeeRepository(temp.toString());
        List<Employee> employees = repository.getAllEmployees();

        assertEquals(2, employees.size());

        Files.deleteIfExists(temp);
    }
}
