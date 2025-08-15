# Employee Performance Management System

A comprehensive Spring Boot application for managing employee performance, departments, projects, and performance reviews.

## Features

### Core Functionality
- **Employee Management**: CRUD operations for employees with department and manager relationships
- **Department Management**: Department information with budget tracking
- **Project Management**: Project tracking with start/end dates and department assignment
- **Performance Reviews**: Employee performance scoring and review comments
- **Employee-Project Assignment**: Many-to-many relationship with role and assignment date

### REST API Endpoints

#### 1. Get Employees with Filters
```
GET /api/employees
```

**Query Parameters:**
- `departments` (optional): List of department names to filter by
- `projects` (optional): List of project names to filter by
- `reviewDate` (optional): Specific review date for performance filtering
- `minScore` (optional): Minimum performance score (0.0 - 5.0)
- `maxScore` (optional): Maximum performance score (0.0 - 5.0)

**Example Requests:**
```bash
# Get all employees
GET /api/employees

# Filter by department
GET /api/employees?departments=Engineering&departments=Sales

# Filter by projects
GET /api/employees?projects=Project Apollo&projects=Sales Expansion

# Filter by performance score on specific date
GET /api/employees?reviewDate=2024-01-15&minScore=4.0&maxScore=5.0

# Combined filtering
GET /api/employees?departments=Engineering&projects=Project Apollo&reviewDate=2024-01-15&minScore=4.0&maxScore=5.0
```

#### 2. Get Employee Details
```
GET /api/employees/{id}
```

Returns detailed employee information including:
- Basic employee details
- Department information
- Project assignments
- Last 3 performance reviews

#### 3. Legacy Endpoint (Backward Compatibility)
```
GET /api/employees/legacy?departments=Engineering&projects=Project Apollo
```

## Database Schema

### Tables
1. **department**: id, name, budget
2. **employee**: id, name, email, department_id, date_of_joining, salary, manager_id
3. **project**: id, name, start_date, end_date, department_id
4. **employee_project**: id, employee_id, project_id, assigned_date, role
5. **performance_review**: id, employee_id, review_date, score, review_comments

### Relationships
- Employee → Department (Many-to-One)
- Employee → Employee (Manager relationship)
- Employee ↔ Project (Many-to-Many through EmployeeProject)
- Employee → PerformanceReview (One-to-Many)
- Department → Project (One-to-Many)

## Technology Stack

- **Java 17**
- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **MySQL 8.0**
- **Maven**
- **Lombok**
- **JUnit 5** (Testing)

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Git

## Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd Employee-Performance-Management-System
```

### 2. Database Setup
```bash
# Create database and tables
mysql -u root -p < src/main/resources/schema.sql

# Insert sample data (optional)
mysql -u root -p employee < SQL\ Scripts/INSERT_SCRIPTS.sql
```

### 3. Configuration
Update `src/main/resources/application.properties` with your database credentials:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 4. Build and Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Testing

### Using cURL
```bash
# Get all employees
curl -X GET "http://localhost:8080/api/employees"

# Filter by department
curl -X GET "http://localhost:8080/api/employees?departments=Engineering"

# Filter by performance score
curl -X GET "http://localhost:8080/api/employees?reviewDate=2024-01-15&minScore=4.0&maxScore=5.0"

# Get employee details
curl -X GET "http://localhost:8080/api/employees/1"
```

### Using Postman
Import the following collection:
```json
{
  "info": {
    "name": "Employee Performance Management System",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Get Employees with Filters",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/api/employees?departments=Engineering&projects=Project Apollo&reviewDate=2024-01-15&minScore=4.0&maxScore=5.0"
        }
      }
    },
    {
      "name": "Get Employee Details",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/api/employees/1"
        }
      }
    }
  ]
}
```

## Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=EmployeeControllerTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=EmployeeControllerTest#testGetEmployeesWithFilters
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/assignment/employeemanagement/
│   │       ├── controllers/          # REST API controllers
│   │       ├── services/             # Business logic
│   │       ├── repositories/         # Data access layer
│   │       ├── entities/             # JPA entities
│   │       ├── dto/                  # Data Transfer Objects
│   │       ├── exceptions/           # Custom exceptions
│   │       └── EmployeePerformanceManagementSystemApplication.java
│   └── resources/
│       ├── application.properties    # Application configuration
│       └── schema.sql               # Database schema
├── test/
│   └── java/
│       └── com/assignment/employeemanagement/
│           ├── controllers/          # Controller tests
│           └── services/             # Service tests
└── SQL Scripts/
    └── INSERT_SCRIPTS.sql           # Sample data
```

## Code Quality Features

- **Lombok**: Reduces boilerplate code
- **Proper Exception Handling**: Global exception handler with meaningful error messages
- **Input Validation**: Parameter validation and type checking
- **Comprehensive Testing**: Unit tests for controllers and services
- **Clean Architecture**: Separation of concerns with proper layering
- **JPA Best Practices**: Proper entity relationships and lazy loading
- **REST API Standards**: Proper HTTP status codes and response formats

## Performance Considerations

- **Lazy Loading**: Used for entity relationships to avoid N+1 queries
- **Indexed Queries**: Database indexes on frequently queried fields
- **Efficient Filtering**: Optimized JPQL queries with proper joins
- **DTO Pattern**: Separate DTOs for API responses to avoid circular references

## Security Considerations

- **Input Validation**: All input parameters are validated
- **SQL Injection Prevention**: Using parameterized queries
- **CORS Configuration**: Configured for cross-origin requests
- **Error Handling**: No sensitive information exposed in error messages

## Future Enhancements

- **Authentication & Authorization**: JWT-based security
- **Audit Logging**: Track changes to employee data
- **File Upload**: Support for document attachments
- **Reporting**: Advanced analytics and reporting features
- **Email Notifications**: Automated performance review reminders
- **Mobile API**: REST API optimized for mobile applications

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For questions or issues, please create an issue in the repository or contact the development team.
