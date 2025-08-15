package com.assignment.employeemanagement.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_project")
@EqualsAndHashCode(exclude = {"employee", "project"})
@ToString(exclude = {"employee", "project"})
public class EmployeeProject {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "assigned_date", nullable = false)
    private LocalDate assignedDate;
    
    @Column(nullable = false)
    private String role;
}

