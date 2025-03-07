package com.java_selenium.tests.excel_tests;

import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.java_selenium.base.BaseClass_Excel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Excel_Tests_01 extends BaseClass_Excel
{
    @Test
    public void Get_Excel_Data_Test() throws IOException
    {
        // Specify the path of the Excel file
        String excelPath = "src/test/java/com/java_selenium/resources/SampleExcelData.xlsx";
        FileInputStream file = new FileInputStream(new File(excelPath));

        // Create a Workbook instance from the file
        Workbook workbook = new XSSFWorkbook(file);

        // Get the first sheet of the Excel file, 0 is the index of the first sheet
        Sheet sheet = workbook.getSheetAt(0);

        // Create a DataFormatter to automatically format cell values into a string
        DataFormatter dataFormatter = new DataFormatter();

        // the Row and Cell objects represent the structure of an Excel spreadsheet
        // Loop through the rows of the sheet
        for (Row row : sheet)
        {
            // Loop through each cell in the row
            for (Cell cell : row)
            {
                // Use DataFormatter to get the cell's value as a string
                String cellValue = dataFormatter.formatCellValue(cell);
                System.out.print(cellValue + "\t"); // Print the formatted value
            }
            System.out.println();
        }

        // Close the workbook and file input stream
        workbook.close();
        file.close();
    }


    // print in extent reports
    @Test
    public void Get_Excel_Data_Test_1() throws IOException
    {
        // Specify the path of the Excel file
        String excelPath = "src/test/java/com/java_selenium/resources/SampleExcelData.xlsx";
        FileInputStream file = new FileInputStream(new File(excelPath));

        // Create a Workbook instance from the file
        Workbook workbook = new XSSFWorkbook(file);

        // Get the first sheet of the Excel file, 0 is the index of the first sheet
        Sheet sheet = workbook.getSheetAt(0);

        // Create a DataFormatter to automatically format cell values into a string
        DataFormatter dataFormatter = new DataFormatter();

        // Prepare the 2D array for table data (excluding headers for simplicity here)
        int rowCount = sheet.getPhysicalNumberOfRows();
        int colCount = sheet.getRow(0).getPhysicalNumberOfCells();
        String[][] tableData = new String[rowCount][colCount];

        // the Row and Cell objects represent the structure of an Excel spreadsheet
        // Loop through the rows of the sheet
        for (int i = 0; i < rowCount; i++)
        {
            Row row = sheet.getRow(i);
            // Loop through each cell in the row
            for (int j = 0; j < colCount; j++)
            {
                Cell cell = row.getCell(j);
                tableData[i][j] = dataFormatter.formatCellValue(cell); // Format cell value
            }
        }

        LogInfo(MarkupHelper.createTable(tableData).getMarkup());

        // Close the workbook and file input stream
        workbook.close();
        file.close();

    }
}
