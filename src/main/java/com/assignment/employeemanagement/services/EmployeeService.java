package com.assignment.employeemanagement.services;
import com.assignment.employeemanagement.entities.*;
import com.assignment.employeemanagement.repositories.*;
import com.assignment.employeemanagement.dto.*;
import com.assignment.employeemanagement.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private PerformanceReviewRepository reviewRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private ProjectRepository projectRepository;

    public List<EmployeeResponseDTO> getEmployeesWithFilters(EmployeeFilterDTO filterDTO) {
        List<Employee> employees;
        
        // If performance score filter is applied
        if (filterDTO.getReviewDate() != null && filterDTO.getMinScore() != null && filterDTO.getMaxScore() != null) {
            employees = employeeRepository.findAllWithAllFilters(
                filterDTO.getDepartments(),
                filterDTO.getProjects(),
                filterDTO.getReviewDate(),
                filterDTO.getMinScore(),
                filterDTO.getMaxScore()
            );
        } else {
            // Basic filtering without performance score
            employees = employeeRepository.findAllWithFilters(
                filterDTO.getDepartments(),
                filterDTO.getProjects()
            );
        }
        
        return employees.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<Employee> getEmployees(List<String> departments, List<String> projects) {
        return employeeRepository.findAllWithFilters(departments, projects);
    }

    public EmployeeDetailDTO getEmployeeDetail(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        
        List<PerformanceReview> last3Reviews = reviewRepository.findTop3ByEmployeeIdOrderByReviewDateDesc(employeeId);
        
        return new EmployeeDetailDTO(employee, last3Reviews);
    }

    private EmployeeResponseDTO convertToResponseDTO(Employee employee) {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setDateOfJoining(employee.getDateOfJoining());
        dto.setSalary(employee.getSalary());
        
        if (employee.getDepartment() != null) {
            dto.setDepartmentName(employee.getDepartment().getName());
        }
        
        if (employee.getManager() != null) {
            dto.setManagerName(employee.getManager().getName());
        }
        
        dto.setProjectNames(employee.getProjectNames());
        
        // Get the latest performance score
        if (!employee.getPerformanceReviews().isEmpty()) {
            PerformanceReview latestReview = employee.getPerformanceReviews().stream()
                    .sorted((r1, r2) -> r2.getReviewDate().compareTo(r1.getReviewDate()))
                    .findFirst()
                    .orElse(null);
            if (latestReview != null) {
                dto.setLastPerformanceScore(latestReview.getScore());
            }
        }
        
        return dto;
    }
}
