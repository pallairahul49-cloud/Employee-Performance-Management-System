package com.assignment.employeemanagement.dto;
import com.assignment.employeemanagement.entities.*;
import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
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
        this.departmentName = e.getDepartment().getName();
        this.projects = e.getProjects().stream().map(Project::getName).collect(Collectors.toList());
        this.last3Reviews = reviews;
    }
}
