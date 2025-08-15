package com.assignment.employeemanagement.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class EmployeeResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String departmentName;
    private LocalDate dateOfJoining;
    private Double salary;
    private String managerName;
    private List<String> projectNames;
    private Double lastPerformanceScore;
}
