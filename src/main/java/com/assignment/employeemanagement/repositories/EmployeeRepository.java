package com.assignment.employeemanagement.repositories;

import com.assignment.employeemanagement.entities.Employee;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT DISTINCT e FROM Employee e " +
            "JOIN e.department d " +
            "LEFT JOIN e.projects p " +
            "LEFT JOIN e.performanceReviews pr " +
            "WHERE (:departmentNames IS NULL OR d.name IN :departmentNames) " +
            "AND (:projectNames IS NULL OR p.name IN :projectNames) " +
            "AND (:score IS NULL OR pr.score = :score) " +
            "AND (:reviewDate IS NULL OR pr.reviewDate = :reviewDate)")
    List<Employee> findAllWithFilters(
            @Param("departmentNames") List<String> departmentNames,
            @Param("projectNames") List<String> projectNames,
            @Param("score") Double score,
            @Param("reviewDate") LocalDate reviewDate
    );
}
