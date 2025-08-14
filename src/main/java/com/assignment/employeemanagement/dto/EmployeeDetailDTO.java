package com.assignment.employeemanagement.dto;

import com.assignment.employeemanagement.entities.Employee;
import com.assignment.employeemanagement.entities.PerformanceReview;
import lombok.Data;
import java.util.List;

@Data
public class EmployeeDetailDTO {
    private Employee employee;
    private List<PerformanceReview> performanceReviews;

    public EmployeeDetailDTO(Employee employee, List<PerformanceReview> performanceReviews) {
        this.employee = employee;
        this.performanceReviews = performanceReviews;
    }
}