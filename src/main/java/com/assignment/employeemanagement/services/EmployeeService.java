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
        
        // If reviewDate is provided, use date-based filtering (more specific)
        if (filterDTO.getReviewDate() != null) {
            if (filterDTO.getScore() != null) {
                // Use exact score on specific date
                employees = employeeRepository.findAllWithScoreAndDate(
                    filterDTO.getDepartments(),
                    filterDTO.getProjects(),
                    filterDTO.getReviewDate(),
                    filterDTO.getScore()
                );
            } else if (filterDTO.getMinScore() != null && filterDTO.getMaxScore() != null) {
                // Use score range on specific date
                employees = employeeRepository.findAllWithAllFilters(
                    filterDTO.getDepartments(),
                    filterDTO.getProjects(),
                    filterDTO.getReviewDate(),
                    filterDTO.getMinScore(),
                    filterDTO.getMaxScore()
                );
            } else {
                // Just filter by date without score
                employees = employeeRepository.findAllWithDateOnly(
                    filterDTO.getDepartments(),
                    filterDTO.getProjects(),
                    filterDTO.getReviewDate()
                );
            }
        }
        // If only score is provided (without date)
        else if (filterDTO.getScore() != null) {
            employees = employeeRepository.findAllWithScoreFilter(
                filterDTO.getDepartments(),
                filterDTO.getProjects(),
                filterDTO.getScore()
            );
        } else {
            // Basic filtering without performance score or date
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

    public EmployeeDetailDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        
        // Get last 3 performance reviews
        List<PerformanceReview> reviews = reviewRepository.findTop3ByEmployeeOrderByReviewDateDesc(employee);
        
        // Map to DTO to avoid circular references
        EmployeeDetailDTO dto = new EmployeeDetailDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setDateOfJoining(employee.getDateOfJoining());
        dto.setSalary(employee.getSalary());
        
        // Set department info
        if (employee.getDepartment() != null) {
            dto.setDepartmentName(employee.getDepartment().getName());
            dto.setDepartmentBudget(employee.getDepartment().getBudget());
        }
        
        // Set manager info
        if (employee.getManager() != null) {
            dto.setManagerName(employee.getManager().getName());
        }
        
        // Set projects
        dto.setProjects(employee.getProjectNames());
        
        // Map performance reviews
        List<EmployeeDetailDTO.PerformanceReviewDTO> reviewDtos = reviews.stream()
                .map(review -> {
                    EmployeeDetailDTO.PerformanceReviewDTO reviewDto = new EmployeeDetailDTO.PerformanceReviewDTO();
                    reviewDto.setId(review.getId());
                    reviewDto.setReviewDate(review.getReviewDate());
                    reviewDto.setScore(review.getScore());
                    reviewDto.setReviewComments(review.getReviewComments());
                    return reviewDto;
                })
                .collect(Collectors.toList());
        
        dto.setLast3Reviews(reviewDtos);
        
        return dto;
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
