package com.java_selenium.tests.db_tests;

import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.java_selenium.base.BaseClass;
import com.java_selenium.utils.database_manager.Emp_DB;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class DB_Test_01 extends BaseClass
{
    @Test  // Fetch All Employee Details and print in console
    public void Fetch_Emp_Details_01()
    {
        try
        {
            // Establish the connection
            Connection connection = Emp_DB.ConnectDB();

            // Create a Statement object to execute queries
            assert connection != null;
            Statement stmt = connection.createStatement();

            // SQL query to fetch employee data
            String query = "SELECT * FROM employees";

            // Execute the query
            ResultSet rs = stmt.executeQuery(query);

            // Process the result
            while (rs.next())
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

            // Close the connection
            rs.close();
            stmt.close();
            connection.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test  // Fetch All Employee Details and print in extent reports
    public void Fetch_Emp_Details_02()
    {
        try
        {
            Connection connection = Emp_DB.ConnectDB();  // Establish the connection
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
            connection.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Test  // Fetch All Employee Details and print in extent reports in table format using StringBuilder
    public void Fetch_Emp_Details_03()
    {
        List<String> employeeData = Emp_DB.FetchEmployeeData(); // Fetch data from the database
        StringBuilder tableHTML = new StringBuilder();  // Convert employee data to HTML table format for Extent Reports
        tableHTML.append("<table border='1'>" +
                            "<tr>" +
                                "<th>Employee ID</th>" +
                                "<th>Name</th>" +
                                "<th>Position</th>" +
                                "<th>Salary</th>" +
                                "<th>Hire Date</th>" +
                            "</tr>");

        for (String data : employeeData)
        {
            // Here we're just showing the data in a single row
            tableHTML.append("<tr><td>")
                    .append(data.replaceAll(", ", "</td><td>")
                            .replace("Employee ID:", "")
                            .replace("Name:", "")
                            .replace("Position:", "")
                            .replace("Salary:", "")
                            .replace("Hire Date:", ""))
                    .append("</td></tr>");
        }
        tableHTML.append("</table>");

        // Log the HTML table into the report
        LogInfo("Employee Data: " + tableHTML);
    }

    @Test  // Fetch All Employee Details and print in extent reports in table format using MarkupHelper
    public void Fetch_Emp_Details_04()
    {
        List<String> employeeData = Emp_DB.FetchEmployeeData();   // Fetch data from the database
        Markup m = MarkupHelper.toTable(employeeData);   // Use MarkupHelper.createTable() to create the table
        LogInfo(m.getMarkup());   // Log the HTML table into the report
    }


    @Test  // Fetch All Employee Details and print in extent reports
    public void Fetch_Emp_Details_02_Test()
    {
        ArrayList<String> name_list = new ArrayList<>();
        try
        {
            Connection connection = Emp_DB.ConnectDB();  // Establish the connection
            assert connection != null;
            Statement stmt = connection.createStatement();  // Create a Statement object to execute queries
            String query = "SELECT * FROM employees";   // SQL query to fetch employee data
            ResultSet rs = stmt.executeQuery(query);   // Execute the query
            while (rs.next())   // Process the result
            {
                String name = rs.getString("name");
                name_list.add(name);
            }

            rs.close();  // Close the connection
            stmt.close();
            connection.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        LogInfo(MarkupHelper.createOrderedList(name_list).getMarkup());
    }



}



