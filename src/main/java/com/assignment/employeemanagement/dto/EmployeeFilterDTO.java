package com.assignment.employeemanagement.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class EmployeeFilterDTO {
    private List<String> departments;
    private List<String> projects;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate reviewDate;
    
    private Double minScore;
    private Double maxScore;
}
