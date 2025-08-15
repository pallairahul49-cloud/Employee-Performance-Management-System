package com.assignment.employeemanagement.repositories;

import com.assignment.employeemanagement.entities.PerformanceReview;
import com.assignment.employeemanagement.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {
    List<PerformanceReview> findTop3ByEmployeeOrderByReviewDateDesc(Employee employee);
}
