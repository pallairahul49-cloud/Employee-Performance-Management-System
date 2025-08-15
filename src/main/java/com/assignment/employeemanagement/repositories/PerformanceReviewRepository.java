package com.assignment.employeemanagement.repositories;
import com.assignment.employeemanagement.entities.PerformanceReview;
import org.springframework.data.jpa.repository.*;
import java.util.List;

public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {
    List<PerformanceReview> findTop3ByEmployeeIdOrderByReviewDateDesc(Long employeeId);
}
