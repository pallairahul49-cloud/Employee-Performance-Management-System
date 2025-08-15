package com.assignment.employeemanagement.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class EmployeeDetailDTO {
    private Long id;
    private String name;
    private String email;
    private String departmentName;
    private Double departmentBudget;
    private LocalDate dateOfJoining;
    private Double salary;
    private String managerName;
    private List<String> projects;
    private List<PerformanceReviewDTO> last3Reviews;
    
    @Data
    public static class PerformanceReviewDTO {
        private Long id;
        private LocalDate reviewDate;
        private Double score;
        private String reviewComments;
    }
}
