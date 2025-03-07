package com.java_selenium.tests.db_tests;

import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.java_selenium.base.BaseClass_Emp_DB;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class DB_Test_01 extends BaseClass_Emp_DB
{
    // Fetch All Employee Details and print in console
    @Test
    public void Fetch_Emp_Details_01() throws SQLException
    {
        assert connection != null;   // Create a Statement object to execute queries
        Statement stmt = connection.createStatement();
        String query = "SELECT * FROM employees";  // SQL query to fetch employee data
        ResultSet rs = stmt.executeQuery(query);  // Execute the query
        while (rs.next())  // Process the result
        {
            // Retrieve by column name
            int employeeId = rs.getInt("employee_id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String position = rs.getString("position");
            double salary = rs.getDouble("salary");
            String hireDate = rs.getString("hire_date");

            // Print employee details
            System.out.println("Employee ID: " + employeeId);
            System.out.println("First Name: " + firstName);
            System.out.println("Last Name: " + lastName);
            System.out.println("Position: " + position);
            System.out.println("Salary: " + salary);
            System.out.println("Hire Date: " + hireDate);
            System.out.println("------------");
        }

        rs.close();  // Close the connection
        stmt.close();
    }



    // Fetch All Employee Details and print in extent reports
    @Test
    public void Fetch_Emp_Details_02() throws SQLException
    {
        assert connection != null;
        Statement stmt = connection.createStatement();  // Create a Statement object to execute queries
        String query = "SELECT * FROM employees";   // SQL query to fetch employee data
        ResultSet rs = stmt.executeQuery(query);   // Execute the query
        while (rs.next())   // Process the result
        {
            // Retrieve by column name
            int employeeId = rs.getInt("employee_id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String position = rs.getString("position");
            double salary = rs.getDouble("salary");
            String hireDate = rs.getString("hire_date");

            // Print employee details
            LogInfo("Employee ID: " + employeeId);
            LogInfo("First Name: " + firstName);
            LogInfo("Last Name: " + lastName);
            LogInfo("Position: " + position);
            LogInfo("Salary: " + salary);
            LogInfo("Hire Date: " + hireDate);
            LogInfo("------------");
        }

        rs.close();  // Close the connection
        stmt.close();
    }



    // Fetch All Employee Details and print in extent reports in table format using StringBuilder
    @Test
    public void Fetch_Emp_Details_03() throws SQLException
    {
        List<String> employeeData = FetchEmployeeData(); // Fetch data from the database
        StringBuilder tableHTML = new StringBuilder();  // Convert employee data to HTML table format for Extent Reports
        tableHTML.append("<table border='1'>" +
                "<tr>" +
                "<th>Employee ID</th>" +
                "<th>First Name</th>" +
                "<th>Last Name</th>" +
                "<th>Name</th>" +
                "<th>Position</th>" +
                "<th>Salary</th>" +
                "<th>Hire Date</th>" +
                "<th>Department ID</th>" +
                "</tr>");

        for (String data : employeeData) {
            // Split the data by tab '\t' to get individual fields
            String[] employeeDetails = data.split("\t");

            // Start a new row in the HTML table
            tableHTML.append("<tr>");

            // Loop through each employee detail and create a table cell <td> for each one
            for (String detail : employeeDetails)
            {
                tableHTML.append("<td>").append(detail.trim()).append("</td>");
            }

            tableHTML.append("</tr>");
        }
        tableHTML.append("</table>");

        // Log the HTML table into the report
        LogInfo("Employee Data: " + tableHTML);
    }



    // Fetch All Employee Details and print in extent reports
    @Test
    public void Fetch_Emp_Details_04() throws SQLException
    {
        List<String> employees = FetchEmployeeData();  // Fetch employee data from the database
        String[][] tableData = new String[employees.size() + 1][8];  // Create a header for the table, Add 1 for header row

        tableData[0] = new String[]{"employee_id", "first_name", "last_name", "name", "position", "salary", "hire_date", "department_id"};
        for (int i = 0; i < employees.size(); i++)   // Populate the table with employee data
        {
            String[] empDetails = employees.get(i).split("\t");  // Split each employee's details by the tab character
            if (empDetails.length == 8)  // Check if the split array has the correct length (8 columns)
            {
                for (int j = 0; j < empDetails.length; j++)  // Fill each cell in the row with employee data
                {
                    tableData[i + 1][j] = empDetails[j];  // Directly assign the data without splitting by ": "
                }
            }
            else
            {
                LogFail("Data for employee " + (i + 1) + " is malformed: " + employees.get(i)); // Log error if the data is malformed or not as expected
            }
        }

        Markup tableMarkup = MarkupHelper.createTable(tableData);  // Create an HTML table using MarkupHelper
        LogInfo("Employee Data:");  // Log the table in the Extent Report
        LogInfo(tableMarkup.getMarkup());
        LogPass("Employee data fetched and logged in table format");  // Additional checks or assertions can go here if needed
    }







}



