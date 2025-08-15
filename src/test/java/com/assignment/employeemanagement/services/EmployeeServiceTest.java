package com.assignment.employeemanagement.services;

import com.assignment.employeemanagement.dto.EmployeeDetailDTO;
import com.assignment.employeemanagement.dto.EmployeeFilterDTO;
import com.assignment.employeemanagement.dto.EmployeeResponseDTO;
import com.assignment.employeemanagement.entities.*;
import com.assignment.employeemanagement.exceptions.ResourceNotFoundException;
import com.assignment.employeemanagement.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PerformanceReviewRepository reviewRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee testEmployee;
    private Department testDepartment;
    private Project testProject;
    private PerformanceReview testReview;
    private EmployeeProject testEmployeeProject;

    @BeforeEach
    void setUp() {
        testDepartment = new Department();
        testDepartment.setId(1L);
        testDepartment.setName("Engineering");
        testDepartment.setBudget(1000000.0);

        testProject = new Project();
        testProject.setId(1L);
        testProject.setName("Project Apollo");
        testProject.setStartDate(LocalDate.of(2024, 1, 1));
        testProject.setEndDate(LocalDate.of(2024, 6, 30));
        testProject.setDepartment(testDepartment);

        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setName("John Doe");
        testEmployee.setEmail("john.doe@example.com");
        testEmployee.setDepartment(testDepartment);
        testEmployee.setDateOfJoining(LocalDate.of(2020, 1, 10));
        testEmployee.setSalary(90000.0);

        testReview = new PerformanceReview();
        testReview.setId(1L);
        testReview.setEmployee(testEmployee);
        testReview.setReviewDate(LocalDate.of(2024, 1, 15));
        testReview.setScore(4.5);
        testReview.setReviewComments("Excellent performance");

        testEmployeeProject = new EmployeeProject();
        testEmployeeProject.setId(1L);
        testEmployeeProject.setEmployee(testEmployee);
        testEmployeeProject.setProject(testProject);
        testEmployeeProject.setAssignedDate(LocalDate.of(2024, 1, 1));
        testEmployeeProject.setRole("Lead Engineer");

        testEmployee.setEmployeeProjects(Arrays.asList(testEmployeeProject));
        testEmployee.setPerformanceReviews(Arrays.asList(testReview));
    }

    @Test
    void testGetEmployeesWithFilters_WithPerformanceScore() {
        EmployeeFilterDTO filterDTO = new EmployeeFilterDTO();
        filterDTO.setDepartments(Arrays.asList("Engineering"));
        filterDTO.setProjects(Arrays.asList("Project Apollo"));
        filterDTO.setReviewDate(LocalDate.of(2024, 1, 15));
        filterDTO.setMinScore(4.0);
        filterDTO.setMaxScore(5.0);

        when(employeeRepository.findAllWithAllFilters(
                eq(Arrays.asList("Engineering")),
                eq(Arrays.asList("Project Apollo")),
                eq(LocalDate.of(2024, 1, 15)),
                eq(4.0),
                eq(5.0)
        )).thenReturn(Arrays.asList(testEmployee));

        List<EmployeeResponseDTO> result = employeeService.getEmployeesWithFilters(filterDTO);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Engineering", result.get(0).getDepartmentName());
        assertEquals(4.5, result.get(0).getLastPerformanceScore());
    }

    @Test
    void testGetEmployeesWithFilters_WithoutPerformanceScore() {
        EmployeeFilterDTO filterDTO = new EmployeeFilterDTO();
        filterDTO.setDepartments(Arrays.asList("Engineering"));
        filterDTO.setProjects(Arrays.asList("Project Apollo"));

        when(employeeRepository.findAllWithFilters(
                eq(Arrays.asList("Engineering")),
                eq(Arrays.asList("Project Apollo"))
        )).thenReturn(Arrays.asList(testEmployee));

        List<EmployeeResponseDTO> result = employeeService.getEmployeesWithFilters(filterDTO);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testGetEmployeeDetail_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(reviewRepository.findTop3ByEmployeeIdOrderByReviewDateDesc(1L))
                .thenReturn(Arrays.asList(testReview));

        EmployeeDetailDTO result = employeeService.getEmployeeDetail(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("Engineering", result.getDepartmentName());
        assertEquals(1, result.getLast3Reviews().size());
    }

    @Test
    void testGetEmployeeDetail_EmployeeNotFound() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeDetail(999L);
        });
    }

    @Test
    void testGetEmployees() {
        when(employeeRepository.findAllWithFilters(
                eq(Arrays.asList("Engineering")),
                eq(Arrays.asList("Project Apollo"))
        )).thenReturn(Arrays.asList(testEmployee));

        List<Employee> result = employeeService.getEmployees(
                Arrays.asList("Engineering"),
                Arrays.asList("Project Apollo")
        );

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }
}
