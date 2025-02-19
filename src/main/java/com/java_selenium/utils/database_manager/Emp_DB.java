package com.java_selenium.utils.database_manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Emp_DB
{
    public static Connection ConnectDB()
    {
        try
        {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            String url = "jdbc:mysql://localhost:3306/employee_db"; // Change your host and database name
            String user = "root"; // MySQL username
            String password = "root"; // MySQL password

            // Get the connection
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database!");
            return connection;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> FetchEmployeeData()
    {
        List<String> employees = new ArrayList<>();
        try
        {
            Connection connection = Emp_DB.ConnectDB();
            assert connection != null;
            Statement stmt = connection.createStatement();
            String query = "SELECT * FROM employees";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next())
            {
                String employeeDetails = "Employee ID: " + rs.getInt("employee_id") +
                        ", Name: " + rs.getString("first_name") + " " + rs.getString("last_name") +
                        ", Position: " + rs.getString("position") +
                        ", Salary: " + rs.getDouble("salary") +
                        ", Hire Date: " + rs.getString("hire_date");
                employees.add(employeeDetails);
            }
            rs.close();
            stmt.close();
            connection.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return employees;
    }


}

