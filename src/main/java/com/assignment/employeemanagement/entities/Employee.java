package com.assignment.employeemanagement.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique=true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    private LocalDate dateOfJoining;

    private Double salary;

    @ManyToOne
    @JoinColumn(name = "manager_id")

    private Employee manager;

    @ManyToMany
    @JoinTable(
            name = "employee_project",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
    private Set<Project> projects = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    private List<PerformanceReview> performanceReviews;

}
