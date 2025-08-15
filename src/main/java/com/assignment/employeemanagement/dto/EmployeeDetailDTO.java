package com.assignment.employeemanagement.dto;
import com.assignment.employeemanagement.entities.*;
import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetailDTO {
    private Long id;
    private String name;
    private String email;
    private String departmentName;
    private List<String> projects;
    private List<PerformanceReview> last3Reviews;

    public EmployeeDetailDTO(Employee e, List<PerformanceReview> reviews) {
        this.id = e.getId();
        this.name = e.getName();
        this.email = e.getEmail();
        this.departmentName = e.getDepartment() != null ? e.getDepartment().getName() : null;
        this.projects = e.getProjectNames();
        this.last3Reviews = reviews;
    }
}
