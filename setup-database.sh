#!/bin/bash

echo "Setting up Employee Performance Management System Database..."
echo

# Check if MySQL is running
echo "Checking MySQL connection..."
if ! command -v mysql &> /dev/null; then
    echo "ERROR: MySQL is not installed or not in PATH"
    echo "Please install MySQL and ensure it's running"
    exit 1
fi

# Create database and tables
echo "Creating database and tables..."
mysql -u root -p < src/main/resources/schema.sql
if [ $? -ne 0 ]; then
    echo "ERROR: Failed to create database schema"
    echo "Please check your MySQL credentials and ensure MySQL is running"
    exit 1
fi

# Insert sample data
echo "Inserting sample data..."
mysql -u root -p employee < "SQL Scripts/INSERT_SCRIPTS.sql"
if [ $? -ne 0 ]; then
    echo "WARNING: Failed to insert sample data"
    echo "You can manually insert data later"
else
    echo "Sample data inserted successfully"
fi

echo
echo "Database setup completed successfully!"
echo "You can now run the application with: mvn spring-boot:run"
echo
