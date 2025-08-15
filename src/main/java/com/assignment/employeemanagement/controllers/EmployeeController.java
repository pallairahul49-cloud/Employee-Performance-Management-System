package com.assignment.employeemanagement.controllers;

import com.assignment.employeemanagement.dto.*;
import com.assignment.employeemanagement.entities.Employee;
import com.assignment.employeemanagement.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesWithFilters(
            @RequestParam(required = false) List<String> departments,
            @RequestParam(required = false) List<String> projects,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reviewDate,
            @RequestParam(required = false) Double minScore,
            @RequestParam(required = false) Double maxScore) {
        
        EmployeeFilterDTO filterDTO = new EmployeeFilterDTO();
        filterDTO.setDepartments(departments);
        filterDTO.setProjects(projects);
        filterDTO.setReviewDate(reviewDate);
        filterDTO.setMinScore(minScore);
        filterDTO.setMaxScore(maxScore);
        
        List<EmployeeResponseDTO> employees = employeeService.getEmployeesWithFilters(filterDTO);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDetailDTO> getEmployeeDetails(@PathVariable Long id) {
        EmployeeDetailDTO employeeDetail = employeeService.getEmployeeDetail(id);
        return ResponseEntity.ok(employeeDetail);
    }

    // Legacy endpoint for backward compatibility
    @GetMapping("/legacy")
    public ResponseEntity<List<Employee>> getEmployees(
            @RequestParam(required = false) List<String> departments,
            @RequestParam(required = false) List<String> projects) {
        List<Employee> employees = employeeService.getEmployees(departments, projects);
        return ResponseEntity.ok(employees);
    }
}
