package org.dei.Sprint1.LAPRUS03;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dei._Train.LocomotiveType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelParserService {


    // Main method that accepts Excel file path
    public RailwayNetwork parseExcelFile(String filePath) {
        RailwayNetwork network = new RailwayNetwork();
        List<String> addedLines = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(file)) {

            printBox("Loading Excel File", filePath);

            // Parse each sheet in the correct order
            int operators = parseOperators(workbook.getSheet("Operator"), network);
            int facilities = parseFacilities(workbook.getSheet("Facility"), network);
            int lines = parseLines(workbook.getSheet("Line"), network, addedLines);
            int segments = parseSegments(workbook.getSheet("Segment"), network);
            int locomotives = parseLocomotives(workbook.getSheet("Locomotive"), network);

            // Display consolidated parsing results
            displayParsingResults(operators, facilities, lines, segments, locomotives, addedLines);

        } catch (IOException e) {
            printErrorBox("File Read Error", e.getMessage());
        } catch (Exception e) {
            printErrorBox("Parsing Error", e.getMessage());
            e.printStackTrace();
        }

        return network;
    }

    private int parseOperators(Sheet sheet, RailwayNetwork network) {
        if (sheet == null) {
            return 0;
        }

        Iterator<Row> rowIterator = sheet.iterator();
        int lineNumber = 0;
        int successCount = 0;

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            lineNumber++;

            // Skip header row
            if (lineNumber == 1) continue;

            try {
                String name = getStringCellValue(row.getCell(0));
                String shortName = getStringCellValue(row.getCell(1));
                String vatNumber = getStringCellValue(row.getCell(2));

                if (name.isEmpty() || shortName.isEmpty()) {
                    continue;
                }

                Operator operator = new Operator(name, shortName, vatNumber);
                network.addOperator(operator);
                successCount++;

            } catch (Exception e) {
                // Silent error handling
            }
        }
        return successCount;
    }

    private int parseFacilities(Sheet sheet, RailwayNetwork network) {
        if (sheet == null) {
            return 0;
        }

        Iterator<Row> rowIterator = sheet.iterator();
        int lineNumber = 0;
        int successCount = 0;

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            lineNumber++;

            // Skip header row
            if (lineNumber == 1) continue;

            try {
                int id = (int) getNumericCellValue(row.getCell(0));
                String name = getStringCellValue(row.getCell(1));

                if (name.isEmpty()) {
                    continue;
                }

                // Check for duplicate ID
                if (network.getFacility(id) != null) {
                    continue;
                }

                Facility facility = new Facility(id, name);
                network.addFacility(facility);
                successCount++;

            } catch (Exception e) {
                // Silent error handling
            }
        }
        return successCount;
    }

    private int parseLines(Sheet sheet, RailwayNetwork network, List<String> addedLines) {
        if (sheet == null) {
            return 0;
        }

        Iterator<Row> rowIterator = sheet.iterator();
        int lineNumber = 0;
        int successCount = 0;

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            lineNumber++;

            // Skip header row
            if (lineNumber == 1) continue;

            try {
                int id = (int) getNumericCellValue(row.getCell(0));
                String name = getStringCellValue(row.getCell(1));
                String owner = getStringCellValue(row.getCell(2));
                int startFacilityId = (int) getNumericCellValue(row.getCell(3));
                int endFacilityId = (int) getNumericCellValue(row.getCell(5));
                int gauge = (int) getNumericCellValue(row.getCell(7));

                // Skip empty rowsExcel File
                if (startFacilityId == 0 || endFacilityId == 0) {
                    continue;
                }

                // Validate facility references
                if (network.getFacility(startFacilityId) == null || network.getFacility(endFacilityId) == null) {
                    continue;
                }

                // Check for duplicate line ID
                if (network.getLine(id) != null) {
                    continue;
                }

                Line lineObj = new Line(id, name, owner, startFacilityId, endFacilityId, gauge);
                network.addLine(lineObj);
                successCount++;
                addedLines.add(name + " (" + startFacilityId + " → " + endFacilityId + ")");

            } catch (Exception e) {
                // Silent error handling
            }
        }
        return successCount;
    }

    private int parseSegments(Sheet sheet, RailwayNetwork network) {
        if (sheet == null) {
            return 0;
        }

        Iterator<Row> rowIterator = sheet.iterator();
        int lineNumber = 0;
        int successCount = 0;

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            lineNumber++;

            // Skip header row
            if (lineNumber == 1) continue;

            try {
                int id = (int) getNumericCellValue(row.getCell(0));
                int lineId = (int) getNumericCellValue(row.getCell(1));
                int order = (int) getNumericCellValue(row.getCell(2));
                boolean electrified = "Yes".equalsIgnoreCase(getStringCellValue(row.getCell(3)));
                double maxWeight = getNumericCellValue(row.getCell(4));
                double length = getNumericCellValue(row.getCell(5));
                int numberTracks = (int) getNumericCellValue(row.getCell(6));

                // Validate line reference
                if (network.getLine(lineId) == null) {
                    continue;
                }

                // Check for duplicate segment ID
                if (network.getSegment(id) != null) {
                    continue;
                }

                // Validate positive values
                if (maxWeight <= 0 || length <= 0 || numberTracks <= 0) {
                    continue;
                }

                Segment segment = new Segment(id, lineId, order, electrified, maxWeight, length, numberTracks);
                network.addSegment(segment);
                successCount++;

            } catch (Exception e) {
                // Silent error handling
            }
        }
        return successCount;
    }

    private int parseLocomotives(Sheet sheet, RailwayNetwork network) {
        if (sheet == null) {
            return 0;
        }

        Iterator<Row> rowIterator = sheet.iterator();
        int lineNumber = 0;
        int successCount = 0;

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            lineNumber++;

            // Skip header row
            if (lineNumber == 1) continue;

            try {
                String number = getStringCellValue(row.getCell(0));
                String name = getStringCellValue(row.getCell(1));
                String make = getStringCellValue(row.getCell(2));
                String model = getStringCellValue(row.getCell(3));
                int serviceYear = (int) getNumericCellValue(row.getCell(4));
                int numberBogies = (int) getNumericCellValue(row.getCell(5));
                String bogies = getStringCellValue(row.getCell(6));
                double power = getNumericCellValue(row.getCell(7));
                double length = getNumericCellValue(row.getCell(8));
                double width = getNumericCellValue(row.getCell(9));
                double height = getNumericCellValue(row.getCell(10));
                double weight = getNumericCellValue(row.getCell(11));
                double maxSpeed = getNumericCellValue(row.getCell(12));
                double operationalSpeed = getNumericCellValue(row.getCell(13));
                double traction = getNumericCellValue(row.getCell(14));
                String type = getStringCellValue(row.getCell(15));
                String voltage = getStringCellValue(row.getCell(16));
                String frequency = getStringCellValue(row.getCell(17));
                String operator = getStringCellValue(row.getCell(18));
                int gauge = (int) getNumericCellValue(row.getCell(19));
                double fuelCapacity = getNumericCellValue(row.getCell(20));

                LocomotiveType locomotiveType = LocomotiveType.fromString(type);
                // Validate required fields
                if (number.isEmpty() || name.isEmpty() || maxSpeed <= 0) {
                    continue;
                }

                // Check for duplicate locomotive number
                if (network.getLocomotive(number) != null) {
                    continue;
                }

                // Locomotive locomotive = new Locomotive(
                //         number, name, make, model, serviceYear, numberBogies, bogies, power,
                //         length, width, height, weight, maxSpeed, operationalSpeed, traction,
                //         locomotiveType, voltage, frequency, operator, gauge, fuelCapacity
                // );
                // network.addLocomotive(locomotive);
                successCount++;

            } catch (Exception e) {
                // Silent error handling
            }
        }
        return successCount;
    }

    private void displayParsingResults(int operators, int facilities, int lines, int segments, int locomotives, List<String> addedLines) {
        StringBuilder sb = new StringBuilder();
        String header = "Excel Parsing Results";
        int width = 59;

        sb.append("┌").append("─".repeat(width)).append("┐\n");
        sb.append("│").append(centerText(header, width)).append("│\n");
        sb.append("├").append("─".repeat(width)).append("┤\n");
        sb.append("│ ").append(String.format("%-25s", "Operators:")).append(String.format("%-33s", operators + " loaded")).append("│\n");
        sb.append("│ ").append(String.format("%-25s", "Facilities:")).append(String.format("%-33s", facilities + " loaded")).append("│\n");
        sb.append("│ ").append(String.format("%-25s", "Lines:")).append(String.format("%-33s", lines + " loaded")).append("│\n");
        sb.append("│ ").append(String.format("%-25s", "Segments:")).append(String.format("%-33s", segments + " loaded")).append("│\n");
        sb.append("│ ").append(String.format("%-25s", "Locomotives:")).append(String.format("%-33s", locomotives + " loaded")).append("│\n");
        sb.append("├").append("─".repeat(width)).append("┤\n");

        // Add the added lines information
        if (!addedLines.isEmpty()) {
            sb.append("│").append(centerText("Railway Lines Added", width)).append("│\n");
            sb.append("├").append("─".repeat(width)).append("┤\n");
            for (String line : addedLines) {
                String formattedLine = "  • " + line;
                if (formattedLine.length() > width - 2) {
                    formattedLine = formattedLine.substring(0, width - 5) + "...";
                }
                sb.append("│ ").append(String.format("%-" + (width - 1) + "s", formattedLine)).append("│\n");
            }
            sb.append("├").append("─".repeat(width)).append("┤\n");
        }

        sb.append("│").append(centerText("Total: " + (operators + facilities + lines + segments + locomotives) + " entities loaded successfully", width)).append("│\n");
        sb.append("└").append("─".repeat(width)).append("┘");

        System.out.println(sb.toString());
        System.out.println();
    }

    // Box printing methods
    private void printBox(String title, String content) {
        int width = Math.max(title.length(), content.length()) + 4;
        width = Math.max(width, 59); // Minimum width for better appearance

        System.out.println("┌" + "─".repeat(width) + "┐");
        System.out.println("│" + centerText(title, width) + "│");
        System.out.println("├" + "─".repeat(width) + "┤");
        System.out.println("│" + centerText(content, width) + "│");
        System.out.println("└" + "─".repeat(width) + "┘");
        System.out.println();
    }

    private void printErrorBox(String title, String content) {
        int width = Math.max(title.length(), content.length()) + 4;
        width = Math.max(width, 50);

        System.out.println("╔" + "─".repeat(width) + "╗");
        System.out.println("║" + centerText(title, width) + "║");
        System.out.println("╠" + "─".repeat(width) + "╣");
        System.out.println("║" + centerText(content, width) + "║");
        System.out.println("╚" + "─".repeat(width) + "╝");
        System.out.println();
    }

    private String centerText(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int padding = width - text.length();
        int leftPadding = padding / 2;
        int rightPadding = padding - leftPadding;
        return " ".repeat(leftPadding) + text + " ".repeat(rightPadding);
    }

    // Helper methods for cell value extraction (unchanged)
    private String getStringCellValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double value = cell.getNumericCellValue();
                    if (value == (int) value) {
                        return String.valueOf((int) value);
                    } else {
                        return String.valueOf(value);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue().trim();
                } catch (Exception e) {
                    try {
                        return String.valueOf(cell.getNumericCellValue());
                    } catch (Exception e2) {
                        return cell.getCellFormula();
                    }
                }
            default:
                return "";
        }
    }

    private double getNumericCellValue(Cell cell) {
        if (cell == null) return 0.0;

        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            case BOOLEAN:
                return cell.getBooleanCellValue() ? 1.0 : 0.0;
            case FORMULA:
                try {
                    return cell.getNumericCellValue();
                } catch (Exception e) {
                    try {
                        return Double.parseDouble(cell.getStringCellValue().trim());
                    } catch (NumberFormatException e2) {
                        return 0.0;
                    }
                }
            default:
                return 0.0;
        }
    }

}