package com.assignment.employeemanagement.repositories;
import com.assignment.employeemanagement.entities.Employee;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    @Query("SELECT DISTINCT e FROM Employee e " +
            "JOIN e.department d " +
            "LEFT JOIN e.employeeProjects ep " +
            "LEFT JOIN ep.project p " +
            "WHERE (:departmentNames IS NULL OR d.name IN :departmentNames) " +
            "AND (:projectNames IS NULL OR p.name IN :projectNames)")
    List<Employee> findAllWithFilters(
            @Param("departmentNames") List<String> departmentNames,
            @Param("projectNames") List<String> projectNames
    );

    @Query("SELECT DISTINCT e FROM Employee e " +
            "JOIN e.department d " +
            "LEFT JOIN e.employeeProjects ep " +
            "LEFT JOIN ep.project p " +
            "JOIN e.performanceReviews pr " +
            "WHERE (:departmentNames IS NULL OR d.name IN :departmentNames) " +
            "AND (:projectNames IS NULL OR p.name IN :projectNames) " +
            "AND pr.score = :score")
    List<Employee> findAllWithScoreFilter(
            @Param("departmentNames") List<String> departmentNames,
            @Param("projectNames") List<String> projectNames,
            @Param("score") Double score
    );

    @Query("SELECT DISTINCT e FROM Employee e " +
            "JOIN e.department d " +
            "LEFT JOIN e.employeeProjects ep " +
            "LEFT JOIN ep.project p " +
            "JOIN e.performanceReviews pr " +
            "WHERE (:departmentNames IS NULL OR d.name IN :departmentNames) " +
            "AND (:projectNames IS NULL OR p.name IN :projectNames) " +
            "AND pr.reviewDate = :reviewDate " +
            "AND pr.score = :score")
    List<Employee> findAllWithScoreAndDate(
            @Param("departmentNames") List<String> departmentNames,
            @Param("projectNames") List<String> projectNames,
            @Param("reviewDate") LocalDate reviewDate,
            @Param("score") Double score
    );

    @Query("SELECT DISTINCT e FROM Employee e " +
            "JOIN e.department d " +
            "LEFT JOIN e.employeeProjects ep " +
            "LEFT JOIN ep.project p " +
            "JOIN e.performanceReviews pr " +
            "WHERE (:departmentNames IS NULL OR d.name IN :departmentNames) " +
            "AND (:projectNames IS NULL OR p.name IN :projectNames) " +
            "AND pr.reviewDate = :reviewDate")
    List<Employee> findAllWithDateOnly(
            @Param("departmentNames") List<String> departmentNames,
            @Param("projectNames") List<String> projectNames,
            @Param("reviewDate") LocalDate reviewDate
    );

    @Query("SELECT DISTINCT e FROM Employee e " +
            "JOIN e.performanceReviews pr " +
            "WHERE pr.reviewDate = :reviewDate " +
            "AND pr.score >= :minScore " +
            "AND pr.score <= :maxScore")
    List<Employee> findByPerformanceScoreAndDate(
            @Param("reviewDate") LocalDate reviewDate,
            @Param("minScore") Double minScore,
            @Param("maxScore") Double maxScore
    );

    @Query("SELECT DISTINCT e FROM Employee e " +
            "JOIN e.department d " +
            "LEFT JOIN e.employeeProjects ep " +
            "LEFT JOIN ep.project p " +
            "JOIN e.performanceReviews pr " +
            "WHERE (:departmentNames IS NULL OR d.name IN :departmentNames) " +
            "AND (:projectNames IS NULL OR p.name IN :projectNames) " +
            "AND pr.reviewDate = :reviewDate " +
            "AND pr.score >= :minScore " +
            "AND pr.score <= :maxScore")
    List<Employee> findAllWithAllFilters(
            @Param("departmentNames") List<String> departmentNames,
            @Param("projectNames") List<String> projectNames,
            @Param("reviewDate") LocalDate reviewDate,
            @Param("minScore") Double minScore,
            @Param("maxScore") Double maxScore
    );
}
