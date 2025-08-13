package com.assignment.employeemanagement.controllers;

import com.assignment.employeemanagement.dto.EmployeeDetailDTO;
import com.assignment.employeemanagement.entities.Employee;
import com.assignment.employeemanagement.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getEmployees(
            @RequestParam(required = false) List<String> departments,
            @RequestParam(required = false) List<String> projects,
            @RequestParam(required = false) Double score,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reviewDate
    ) {
        return employeeService.getEmployees(departments, projects, score, reviewDate);
    }

    // 2. Get detailed info for one employee
    @GetMapping("/{id}")
    public EmployeeDetailDTO getEmployeeDetails(@PathVariable Long id) {
        return employeeService.getEmployeeDetail(id);
    }
}
