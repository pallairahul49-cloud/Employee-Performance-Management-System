package com.assignment.employeemanagement.controllers;

import com.assignment.employeemanagement.dto.EmployeeDetailDTO;
import com.assignment.employeemanagement.dto.EmployeeResponseDTO;
import com.assignment.employeemanagement.entities.Employee;
import com.assignment.employeemanagement.services.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee testEmployee;
    private EmployeeResponseDTO testEmployeeResponse;
    private EmployeeDetailDTO testEmployeeDetail;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setName("John Doe");
        testEmployee.setEmail("john.doe@example.com");

        testEmployeeResponse = new EmployeeResponseDTO();
        testEmployeeResponse.setId(1L);
        testEmployeeResponse.setName("John Doe");
        testEmployeeResponse.setEmail("john.doe@example.com");

        testEmployeeDetail = new EmployeeDetailDTO();
        testEmployeeDetail.setId(1L);
        testEmployeeDetail.setName("John Doe");
        testEmployeeDetail.setEmail("john.doe@example.com");
    }

    @Test
    void testGetEmployeesWithFilters() throws Exception {
        List<EmployeeResponseDTO> employees = Arrays.asList(testEmployeeResponse);
        when(employeeService.getEmployeesWithFilters(any())).thenReturn(employees);

        mockMvc.perform(get("/api/employees")
                .param("departments", "Engineering")
                .param("projects", "Project Apollo")
                .param("reviewDate", "2024-01-15")
                .param("minScore", "4.0")
                .param("maxScore", "5.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void testGetEmployeesWithFiltersNoParams() throws Exception {
        List<EmployeeResponseDTO> employees = Arrays.asList(testEmployeeResponse);
        when(employeeService.getEmployeesWithFilters(any())).thenReturn(employees);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testGetEmployeeDetails() throws Exception {
        when(employeeService.getEmployeeDetail(1L)).thenReturn(testEmployeeDetail);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testGetEmployeesLegacy() throws Exception {
        List<Employee> employees = Arrays.asList(testEmployee);
        when(employeeService.getEmployees(any(), any())).thenReturn(employees);

        mockMvc.perform(get("/api/employees/legacy")
                .param("departments", "Engineering")
                .param("projects", "Project Apollo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
