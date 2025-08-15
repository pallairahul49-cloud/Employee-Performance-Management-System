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
import java.util.Collections;
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
    private Employee testManager;
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

        testManager = new Employee();
        testManager.setId(2L);
        testManager.setName("Jane Manager");
        testManager.setEmail("jane.manager@example.com");
        testManager.setDepartment(testDepartment);
        testManager.setDateOfJoining(LocalDate.of(2019, 1, 1));
        testManager.setSalary(120000.0);

        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setName("John Doe");
        testEmployee.setEmail("john.doe@example.com");
        testEmployee.setDepartment(testDepartment);
        testEmployee.setDateOfJoining(LocalDate.of(2020, 1, 10));
        testEmployee.setSalary(90000.0);
        testEmployee.setManager(testManager);

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
        assertEquals("Jane Manager", result.get(0).getManagerName());
        assertEquals(Arrays.asList("Project Apollo"), result.get(0).getProjectNames());
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
    void testGetEmployeesWithFilters_WithPartialPerformanceScore() {
        EmployeeFilterDTO filterDTO = new EmployeeFilterDTO();
        filterDTO.setDepartments(Arrays.asList("Engineering"));
        filterDTO.setProjects(Arrays.asList("Project Apollo"));
        filterDTO.setReviewDate(LocalDate.of(2024, 1, 15));
        // Missing minScore and maxScore

        when(employeeRepository.findAllWithFilters(
                eq(Arrays.asList("Engineering")),
                eq(Arrays.asList("Project Apollo"))
        )).thenReturn(Arrays.asList(testEmployee));

        List<EmployeeResponseDTO> result = employeeService.getEmployeesWithFilters(filterDTO);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetEmployeesWithFilters_WithNullValues() {
        EmployeeFilterDTO filterDTO = new EmployeeFilterDTO();
        filterDTO.setDepartments(null);
        filterDTO.setProjects(null);

        when(employeeRepository.findAllWithFilters(
                eq(null),
                eq(null)
        )).thenReturn(Arrays.asList(testEmployee));

        List<EmployeeResponseDTO> result = employeeService.getEmployeesWithFilters(filterDTO);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetEmployeesWithFilters_EmptyResult() {
        EmployeeFilterDTO filterDTO = new EmployeeFilterDTO();
        filterDTO.setDepartments(Arrays.asList("Engineering"));

        when(employeeRepository.findAllWithFilters(
                eq(Arrays.asList("Engineering")),
                eq(null)
        )).thenReturn(Collections.emptyList());

        List<EmployeeResponseDTO> result = employeeService.getEmployeesWithFilters(filterDTO);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetEmployeesWithFilters_AllFiltersNull() {
        EmployeeFilterDTO filterDTO = new EmployeeFilterDTO();

        when(employeeRepository.findAllWithFilters(
                eq(null),
                eq(null)
        )).thenReturn(Arrays.asList(testEmployee));

        List<EmployeeResponseDTO> result = employeeService.getEmployeesWithFilters(filterDTO);

        assertNotNull(result);
        assertEquals(1, result.size());
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
        assertEquals(Arrays.asList("Project Apollo"), result.getProjects());
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

    @Test
    void testGetEmployees_WithNullParameters() {
        when(employeeRepository.findAllWithFilters(
                eq(null),
                eq(null)
        )).thenReturn(Arrays.asList(testEmployee));

        List<Employee> result = employeeService.getEmployees(null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testConvertToResponseDTO_EmployeeWithNullDepartment() {
        Employee employeeWithoutDept = new Employee();
        employeeWithoutDept.setId(3L);
        employeeWithoutDept.setName("No Dept Employee");
        employeeWithoutDept.setEmail("no.dept@example.com");
        employeeWithoutDept.setDateOfJoining(LocalDate.of(2020, 1, 1));
        employeeWithoutDept.setSalary(80000.0);
        employeeWithoutDept.setDepartment(null);
        employeeWithoutDept.setManager(null);
        employeeWithoutDept.setEmployeeProjects(Collections.emptyList());
        employeeWithoutDept.setPerformanceReviews(Collections.emptyList());

        when(employeeRepository.findAllWithFilters(
                eq(null),
                eq(null)
        )).thenReturn(Arrays.asList(employeeWithoutDept));

        List<EmployeeResponseDTO> result = employeeService.getEmployeesWithFilters(new EmployeeFilterDTO());
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.get(0).getDepartmentName());
        assertNull(result.get(0).getManagerName());
    }

    @Test
    void testConvertToResponseDTO_EmployeeWithNullManager() {
        Employee employeeWithoutManager = new Employee();
        employeeWithoutManager.setId(4L);
        employeeWithoutManager.setName("No Manager Employee");
        employeeWithoutManager.setEmail("no.manager@example.com");
        employeeWithoutManager.setDateOfJoining(LocalDate.of(2020, 1, 1));
        employeeWithoutManager.setSalary(80000.0);
        employeeWithoutManager.setDepartment(testDepartment);
        employeeWithoutManager.setManager(null);
        employeeWithoutManager.setEmployeeProjects(Collections.emptyList());
        employeeWithoutManager.setPerformanceReviews(Collections.emptyList());

        when(employeeRepository.findAllWithFilters(
                eq(null),
                eq(null)
        )).thenReturn(Arrays.asList(employeeWithoutManager));

        List<EmployeeResponseDTO> result = employeeService.getEmployeesWithFilters(new EmployeeFilterDTO());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.get(0).getManagerName());
    }

    @Test
    void testConvertToResponseDTO_EmployeeWithEmptyPerformanceReviews() {
        Employee employeeWithoutReviews = new Employee();
        employeeWithoutReviews.setId(5L);
        employeeWithoutReviews.setName("No Reviews Employee");
        employeeWithoutReviews.setEmail("no.reviews@example.com");
        employeeWithoutReviews.setDateOfJoining(LocalDate.of(2020, 1, 1));
        employeeWithoutReviews.setSalary(80000.0);
        employeeWithoutReviews.setDepartment(testDepartment);
        employeeWithoutReviews.setManager(testManager);
        employeeWithoutReviews.setEmployeeProjects(Arrays.asList(testEmployeeProject));
        employeeWithoutReviews.setPerformanceReviews(Collections.emptyList());

        when(employeeRepository.findAllWithFilters(
                eq(null),
                eq(null)
        )).thenReturn(Arrays.asList(employeeWithoutReviews));

        List<EmployeeResponseDTO> result = employeeService.getEmployeesWithFilters(new EmployeeFilterDTO());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.get(0).getLastPerformanceScore());
    }

    @Test
    void testConvertToResponseDTO_EmployeeWithMultiplePerformanceReviews() {
        PerformanceReview oldReview = new PerformanceReview();
        oldReview.setId(2L);
        oldReview.setEmployee(testEmployee);
        oldReview.setReviewDate(LocalDate.of(2023, 12, 1));
        oldReview.setScore(3.5);
        oldReview.setReviewComments("Good performance");

        testEmployee.setPerformanceReviews(Arrays.asList(testReview, oldReview));

        when(employeeRepository.findAllWithFilters(
                eq(null),
                eq(null)
        )).thenReturn(Arrays.asList(testEmployee));

        List<EmployeeResponseDTO> result = employeeService.getEmployeesWithFilters(new EmployeeFilterDTO());

        assertNotNull(result);
        assertEquals(1, result.size());
        // Should get the latest review score (4.5 from 2024-01-15)
        assertEquals(4.5, result.get(0).getLastPerformanceScore());
    }

    @Test
    void testConvertToResponseDTO_EmployeeWithEmptyProjects() {
        Employee employeeWithoutProjects = new Employee();
        employeeWithoutProjects.setId(6L);
        employeeWithoutProjects.setName("No Projects Employee");
        employeeWithoutProjects.setEmail("no.projects@example.com");
        employeeWithoutProjects.setDateOfJoining(LocalDate.of(2020, 1, 1));
        employeeWithoutProjects.setSalary(80000.0);
        employeeWithoutProjects.setDepartment(testDepartment);
        employeeWithoutProjects.setManager(testManager);
        employeeWithoutProjects.setEmployeeProjects(Collections.emptyList());
        employeeWithoutProjects.setPerformanceReviews(Collections.emptyList());

        when(employeeRepository.findAllWithFilters(
                eq(null),
                eq(null)
        )).thenReturn(Arrays.asList(employeeWithoutProjects));

        List<EmployeeResponseDTO> result = employeeService.getEmployeesWithFilters(new EmployeeFilterDTO());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getProjectNames().isEmpty());
    }

    @Test
    void testGetEmployeesWithFilters_WithEmptyLists() {
        EmployeeFilterDTO filterDTO = new EmployeeFilterDTO();
        filterDTO.setDepartments(Collections.emptyList());
        filterDTO.setProjects(Collections.emptyList());

        when(employeeRepository.findAllWithFilters(
                eq(Collections.emptyList()),
                eq(Collections.emptyList())
        )).thenReturn(Arrays.asList(testEmployee));

        List<EmployeeResponseDTO> result = employeeService.getEmployeesWithFilters(filterDTO);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetEmployeeDetail_WithEmptyReviews() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(reviewRepository.findTop3ByEmployeeIdOrderByReviewDateDesc(1L))
                .thenReturn(Collections.emptyList());

        EmployeeDetailDTO result = employeeService.getEmployeeDetail(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertTrue(result.getLast3Reviews().isEmpty());
    }

    @Test
    void testGetEmployeeDetail_WithNullDepartment() {
        Employee employeeWithoutDept = new Employee();
        employeeWithoutDept.setId(7L);
        employeeWithoutDept.setName("No Dept Employee");
        employeeWithoutDept.setEmail("no.dept@example.com");
        employeeWithoutDept.setDepartment(null);
        employeeWithoutDept.setEmployeeProjects(Collections.emptyList());
        employeeWithoutDept.setPerformanceReviews(Collections.emptyList());

        when(employeeRepository.findById(7L)).thenReturn(Optional.of(employeeWithoutDept));
        when(reviewRepository.findTop3ByEmployeeIdOrderByReviewDateDesc(7L))
                .thenReturn(Collections.emptyList());

        EmployeeDetailDTO result = employeeService.getEmployeeDetail(7L);

        assertNotNull(result);
        assertNull(result.getDepartmentName());
    }
}
