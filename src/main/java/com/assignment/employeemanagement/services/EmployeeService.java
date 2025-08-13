package com.assignment.employeemanagement.services;

import com.assignment.employeemanagement.dto.EmployeeDetailDTO;
import com.assignment.employeemanagement.entities.Employee;
import com.assignment.employeemanagement.entities.PerformanceReview;
import com.assignment.employeemanagement.exceptions.ResourceNotFoundException;
import com.assignment.employeemanagement.repositories.EmployeeRepository;
import com.assignment.employeemanagement.repositories.PerformanceReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PerformanceReviewRepository reviewRepository;

    public List<Employee> getEmployees(List<String> departments,
                                       List<String> projects,
                                       Double score,
                                       LocalDate reviewDate) {
        return employeeRepository.findAllWithFilters(
                (departments != null && !departments.isEmpty()) ? departments : null,
                (projects != null && !projects.isEmpty()) ? projects : null,
                score != null ? score : null,
                reviewDate != null ? reviewDate : null
        );
    }

    public EmployeeDetailDTO getEmployeeDetail(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        List<PerformanceReview> last3Reviews =
                reviewRepository.findTop3ByEmployeeIdOrderByReviewDateDesc(employeeId);

        return new EmployeeDetailDTO(employee, last3Reviews);
    }
}
