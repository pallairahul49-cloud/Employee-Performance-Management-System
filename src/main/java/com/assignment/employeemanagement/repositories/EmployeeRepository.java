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
            "LEFT JOIN PerformanceReview pr ON pr.employee = e " +
            "WHERE (:departmentNames IS NULL OR " +
            "   EXISTS (SELECT 1 FROM Department dep WHERE dep = e.department AND " +
            "       LOWER(dep.name) LIKE ANY (SELECT CONCAT('%', LOWER(name), '%') FROM java.util.List name IN :departmentNames))) " +
            "AND (:projectNames IS NULL OR " +
            "   EXISTS (SELECT 1 FROM Project proj WHERE proj MEMBER OF e.projects AND " +
            "       LOWER(proj.name) LIKE ANY (SELECT CONCAT('%', LOWER(name), '%') FROM java.util.List name IN :projectNames))) " +
            "AND (:score IS NULL OR pr.score = :score) " +
            "AND (:reviewDate IS NULL OR pr.reviewDate = :reviewDate)")
    List<Employee> findEmployeesWithFilters(
            @Param("departmentNames") List<String> departmentNames,
            @Param("projectNames") List<String> projectNames,
            @Param("score") Double score,
            @Param("reviewDate") LocalDate reviewDate
    );

}
