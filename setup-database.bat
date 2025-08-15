@echo off
echo Setting up Employee Performance Management System Database...
echo.

REM Check if MySQL is running
echo Checking MySQL connection...
mysql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: MySQL is not installed or not in PATH
    echo Please install MySQL and ensure it's running
    pause
    exit /b 1
)

REM Create database and tables
echo Creating database and tables...
mysql -u root -p < src\main\resources\schema.sql
if %errorlevel% neq 0 (
    echo ERROR: Failed to create database schema
    echo Please check your MySQL credentials and ensure MySQL is running
    pause
    exit /b 1
)

REM Insert sample data
echo Inserting sample data...
mysql -u root -p employee < "SQL Scripts\INSERT_SCRIPTS.sql"
if %errorlevel% neq 0 (
    echo WARNING: Failed to insert sample data
    echo You can manually insert data later
) else (
    echo Sample data inserted successfully
)

echo.
echo Database setup completed successfully!
echo You can now run the application with: mvn spring-boot:run
echo.
pause
