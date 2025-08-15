package com.assignment.employeemanagement.entities;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "performance_review")
@EqualsAndHashCode(exclude = {"employee"})
@ToString(exclude = {"employee"})
public class PerformanceReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "review_date", nullable = false)
    private LocalDate reviewDate;
    
    @Column(nullable = false)
    private Double score;
    
    @Column(name = "review_comments", columnDefinition = "TEXT")
    private String reviewComments;
}
