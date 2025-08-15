USE employee;

-- Department data
INSERT INTO department (id, name, budget) VALUES
(1, 'Engineering', 1000000),
(2, 'Human Resources', 300000),
(3, 'Sales', 700000),
(4, 'Marketing', 500000),
(5, 'Finance', 600000),
(6, 'Customer Support', 400000),
(7, 'IT', 450000),
(8, 'Legal', 350000),
(9, 'R&D', 900000),
(10, 'Operations', 550000);

-- Project data
INSERT INTO project (id, name, start_date, end_date, department_id) VALUES
(1, 'Project Apollo', '2024-01-01', '2024-06-30', 1),
(2, 'Recruitment Drive 2024', '2024-02-01', '2024-04-30', 2),
(3, 'Sales Expansion', '2024-03-01', '2024-12-31', 3),
(4, 'Brand Awareness Campaign', '2024-01-15', '2024-07-15', 4),
(5, 'Budget Audit Q1', '2024-01-01', '2024-03-31', 5),
(6, 'Customer Feedback System', '2024-04-01', '2024-09-30', 6),
(7, 'Network Infrastructure Upgrade', '2024-02-15', '2024-11-30', 7),
(8, 'Legal Compliance Review', '2024-03-01', '2024-05-31', 8),
(9, 'New Product Development', '2024-01-01', '2024-12-31', 9),
(10, 'Operational Efficiency', '2024-05-01', '2024-10-31', 10);

-- Employee data
INSERT INTO employee (id, name, email, department_id, date_of_joining, salary, manager_id) VALUES
(1, 'Alice Johnson', 'alice.johnson@example.com', 1, '2020-01-10', 90000, NULL),
(2, 'Bob Smith', 'bob.smith@example.com', 1, '2021-03-15', 75000, 1),
(3, 'Carol White', 'carol.white@example.com', 2, '2019-06-01', 65000, NULL),
(4, 'David Brown', 'david.brown@example.com', 3, '2018-08-20', 72000, NULL),
(5, 'Eva Green', 'eva.green@example.com', 4, '2022-01-05', 68000, NULL),
(6, 'Frank Black', 'frank.black@example.com', 5, '2017-11-11', 80000, NULL),
(7, 'Grace Lee', 'grace.lee@example.com', 6, '2020-07-23', 64000, NULL),
(8, 'Hank Miller', 'hank.miller@example.com', 7, '2023-04-15', 70000, 1),
(9, 'Ivy Wilson', 'ivy.wilson@example.com', 9, '2016-09-30', 95000, NULL),
(10, 'Jack Davis', 'jack.davis@example.com', 10, '2015-12-01', 72000, NULL);

-- Employee project assignments (with assigned_date and role)
INSERT INTO employee_project (employee_id, project_id, assigned_date, role) VALUES
(1, 1, '2024-01-01', 'Lead Engineer'),
(2, 1, '2024-02-01', 'Software Developer'),
(3, 2, '2024-02-10', 'HR Specialist'),
(4, 3, '2024-03-15', 'Sales Manager'),
(5, 4, '2024-01-20', 'Marketing Analyst'),
(6, 5, '2024-01-10', 'Financial Analyst'),
(7, 6, '2024-04-05', 'Customer Support Agent'),
(8, 7, '2024-02-20', 'Network Engineer'),
(9, 9, '2024-01-01', 'Product Manager'),
(10, 10, '2024-05-01', 'Operations Analyst');

-- Performance reviews (no duplicates)
INSERT INTO performance_review (employee_id, review_date, score, review_comments) VALUES
(1, '2024-01-15', 4.5, 'Excellent leadership on Project Apollo'),
(1, '2023-07-15', 4.2, 'Consistent good performance'),
(1, '2022-12-10', 4.3, 'Strong contribution to multiple Engineering projects'),
(1, '2021-08-05', 4.4, 'Delivered critical module ahead of deadline'),
(2, '2024-01-10', 4.0, 'Strong technical skills'),
(3, '2023-12-01', 3.8, 'Reliable and punctual'),
(4, '2023-11-20', 4.1, 'Good sales results'),
(5, '2024-02-25', 3.9, 'Creates creative marketing strategies'),
(6, '2024-01-30', 4.3, 'Handles finances carefully'),
(7, '2023-11-15', 3.7, 'Excellent customer support'),
(8, '2024-03-05', 4.0, 'Key contributor to network upgrade'),
(9, '2024-03-15', 4.6, 'Exceptional product development');
