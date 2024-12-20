package controllers;
import models.*;
import utils.*;
import views.*;
import java.io.*;
import java.time.*;
import java.util.*;
import java.nio.file.*;
import java.time.format.*;

public class ReportManager {
    private static final String PURCHASE_HISTORY_FILE = "db/customer_purchase_history.txt";
    private static final String SALES_REPORT_FILE = "db/sales_report.txt";
    private static final String RESTOCKING_REPORT_FILE = "db/restocking.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final LocalDateTime currentDateTime = LocalDateTime.now(); // Define the formatter 
    private ValidationController validator = new ValidationController();
    private static PharmacistView viewPharmacist = new PharmacistView();
    List<Medicine> medicines = MedicineController.getInstance().getMedicines();
   
    public ReportManager() {
        ensureFileExists(PURCHASE_HISTORY_FILE);
        ensureFileExists(SALES_REPORT_FILE);
        ensureFileExists(RESTOCKING_REPORT_FILE);
    }

    // Ensure the files exists, if not, create the file
    private static void ensureFileExists(String filePath) {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
                System.out.println("File created: " + filePath);
            } catch (IOException e) {
                System.err.println("Error creating file: " + filePath + " - " + e.getMessage());
            }
        }
    }

    // Load Stock History File and add to List
    public List<String[]> loadStockingHistory() {
        List<String[]> stockingHistory = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RESTOCKING_REPORT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stockingHistory.add(line.split(",")); // Store the fields in the ArrayList
            }
        } catch (IOException e) {
            System.err.println("Error reading stocking history: " + e.getMessage());
        }
        return stockingHistory; // Return the stocking history list
    }

    // Load Purchase History File and add to List
    public List<String[]> loadPurchaseHistory() {
        List<String[]> purchaseHistory = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PURCHASE_HISTORY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                purchaseHistory.add(line.split(",")); // Store the fields in the ArrayList
            }
        } catch (IOException e) {
            System.err.println("Error reading purchase history: " + e.getMessage());
        }
        return purchaseHistory; // Return the purchase history list
    }

    // Load Sales Report File and add to List
    public List<String[]> loadSalesReport() {
        List<String[]> salesReport = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SALES_REPORT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                salesReport.add(line.split(",")); // Store the fields in the ArrayList
            }
        } catch (IOException e) {
            System.err.println("Error reading sales report: " + e.getMessage());
        }
        return salesReport; // Return the sales report list
    }
    
    // Write - Get Cart itel list and record customer for history
    public void recordPurchaseHistory(Customer customer, List<CartItem> cart, String transactionID, String cashierName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PURCHASE_HISTORY_FILE, true))) {
            LocalDate purchaseDate = LocalDate.now();
            for (CartItem item : cart) {
                String record = transactionID + "," +
                        customer.getId() + "," +
                        customer.getCustomerName() + "," +
                        item.getMedicine().getName() + "," +
                        item.getMedicine().getPrice() + "," +
                        item.getQuantity() + "," +
                        item.getTotalPrice() + "," +
                        purchaseDate+ "," +
                        cashierName;
                writer.write(record);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing purchase history: " + e.getMessage());
        }
    }

    // Write -Get Cart Item List and write to the file for sales report
    public void generateSalesReport(List<CartItem> cart) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SALES_REPORT_FILE, true))) {
            LocalDate purchaseDate = LocalDate.now();
            for (CartItem item : cart) {
                String record = item.getMedicine().getId() + "," +
                        item.getMedicine().getName() + "," +
                        item.getMedicine().getPrice() + "," +
                        item.getQuantity() + "," +
                        item.getTotalPrice() + "," +
                        purchaseDate;
                writer.write(record);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing sales report: " + e.getMessage());
        }
    }

    // Filter sales report by date or date range
    public void filterSalesReport(LocalDate startDate, LocalDate endDate) {
        ConsoleUtils.clearConsole(); // Clear top
        try (BufferedReader reader = new BufferedReader(new FileReader(SALES_REPORT_FILE))) {
            String line;
            boolean recordsFound = false;
            int totalWidth = 80;
            double grandTotal = 0.0;
            List<String> salesReport = new ArrayList<>();
           
            TableUtils.doubleLine(totalWidth); 
            if (startDate != null && endDate != null) {
                TxtUtils.CenteredTitle("---- Sales Report from "+ Color.YELLOW + startDate.toString() + Color.RESET + " to "+ Color.YELLOW + endDate.toString() + Color.RESET +" ---", totalWidth);
            } else if (startDate != null) {
                TxtUtils.CenteredTitle("--- Sales Report for "+ Color.YELLOW + startDate.toString() + Color.RESET+ " ---", totalWidth);
            } else {
                TxtUtils.CenteredColoredText("Invalid filter parameters. Please provide at least a startDate.", totalWidth, Color.RED);
                return;
            }

            String[] headers = {"ID", "Name", "Price", "Quantity", "Total", "Date"};
            TableUtils.printTableHeader(totalWidth, headers, 4);

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                LocalDate recordDate = LocalDate.parse(fields[5], DATE_FORMATTER); //

                boolean matches;
                if (startDate != null && endDate != null) {
                    matches = !recordDate.isBefore(startDate) && !recordDate.isAfter(endDate);
                } else {
                    matches = recordDate.equals(startDate);
                }

                if (matches) {
                    String salesReportRecord = 
                            fields[0]+ "," +
                            fields[1]+ "," +
                            fields[2] + "," +
                            fields[3] + "," +
                            fields[4] + "," +
                            fields[5]; // Concatenate all details into a single String

                    salesReport.add(salesReportRecord); // Add the concatenated string to the list
                    grandTotal += Double.parseDouble(fields[4]); // Add the TotalPrice to the grand total
                    recordsFound = true;
                }
            }
            
            for (String row : salesReport) {
                TableUtils.printTableRow(totalWidth, row.split(",") ,4); // Print all the row
            }
            
            if (!recordsFound) {
                TxtUtils.CenteredColoredText("--- No sales records found ---", totalWidth, Color.RED);
            }
            else{
                System.out.printf("\nGrand Total Sales Value: %s%s Php%s", Color.YELLOW, TxtUtils.money(grandTotal), Color.RESET);
                System.out.println("\nDate generated: "+ Color.YELLOW + currentDateTime.format(DATETIME_FORMATTER)  + Color.RESET);
                System.out.println("Generated By: " + Color.YELLOW + UserModel.getCurrentUsername() + Color.RESET);
            }
            TableUtils.printLineDivider(totalWidth); // Ending line divider
            TxtUtils.CenteredTitle("*** Nothing Follows ***",totalWidth);

            askReturnMenu(totalWidth);

        } catch (IOException e) {
            System.out.println("Error reading sales report: " + e.getMessage());
        }
    }

    // Filter customer purchase history by customer ID, date range, or both
    public void filterCustomerHistory(String query, LocalDate startDate, LocalDate endDate) {
        ConsoleUtils.clearConsole(); //clear top
        boolean recordsFound = false;
        int totalWidth = 120;

        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- All Customer History Report ---", totalWidth);

        try( BufferedReader reader = new BufferedReader(new FileReader(PURCHASE_HISTORY_FILE))) {
            String line;
            double grandTotal = 0.0;

            String[] headers = {"Invoice no.", "ID", "Name", "Medicine", "Price", "Qty", "Total", "Date", "Cashier"};
            TableUtils.printTableHeader(totalWidth, headers ,14);

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length > 8) {
                    
                    String recordCustomerId = fields[1]; // ID
                    String recordCustomerName = fields[2]; // Name
                    LocalDate recordDate = LocalDate.parse(fields[7], DATE_FORMATTER); // Date
                    boolean matches = true;
                    
                    // Check customer ID or customer name match
                    if (query != null && !recordCustomerId.equals(query) && !recordCustomerName.equalsIgnoreCase(query) && !query.equalsIgnoreCase("all")) {
                        matches = false;
                    }
    
                    // Check date range match
                    if (startDate != null && endDate != null) {
                        if (recordDate.isBefore(startDate) || recordDate.isAfter(endDate)) {
                            matches = false;
                        }
                    }
    
                    if (matches) {

                        recordsFound = true;
                        grandTotal+= Double.parseDouble(fields[6]); 

                        String customerDetails =    
                                fields[0] + "," +                       // Invoice number
                                Integer.parseInt(fields[1])+ "," +      // Customer ID
                                fields[2] + "," +                       // Customer name
                                fields[3] + "," +                       // Medicine name
                                fields[4] + "," +                       // Price
                                fields[5] + "," +                       // Quantity
                                fields[6] + "," +                       // Total     
                                fields[7] + "," +                       // Purchase date
                                fields[8];                              // Cashier name
                                          
                        TableUtils.printTableRow(totalWidth, customerDetails.split(","), 14); // Split the string into an array
                    }
                }
            }

            if (!recordsFound) {
                TxtUtils.CenteredColoredText("No records found for the given criteria.", totalWidth, Color.RED);
            } 
            else {
                System.out.printf("\nGrand Total: %s%s Php%s", Color.YELLOW, TxtUtils.money(grandTotal), Color.RESET);
            }

            System.out.println("\nDate generated: " + Color.YELLOW + currentDateTime.format(DATETIME_FORMATTER) + Color.RESET);
            System.out.println("Generated By: " + Color.YELLOW + UserModel.getCurrentUsername() + Color.RESET);

            if (query != null && query != "all") {
                System.out.println("Generated for Customer ID/Name : " + Color.YELLOW + query + Color.RESET);
            }
            else if (startDate != null && endDate != null) {
                System.out.println("Generated by date range:  " + Color.YELLOW + startDate + Color.RESET + " to " + Color.YELLOW + endDate + Color.RESET);
            }
            else {
                System.out.println("Generated all Record");
            }
    
            TableUtils.printLineDivider(totalWidth);
        } 
        catch (IOException e) {
            TxtUtils.CenteredColoredText("Error reading purchase history" + e.getMessage(), totalWidth, Color.RED);
            TableUtils.printLineDivider(totalWidth);
        }
    
        TxtUtils.CenteredTitle("*** Nothing Follows ***",totalWidth);
        askReturnMenu(totalWidth);
    }

    // Read Stocking file
    public void reStockingHistory() {
        ConsoleUtils.clearConsole(); // clear top
        List<String[]> stockingHistory = loadStockingHistory(); // Load stocking history
        // double grandTotal = 0.0;
        int totalWidth = 70;
        // Print filtering criteria
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("----- Pharmacy Re-stocking History ---", totalWidth);
        
        String[] headers = {"ID", "Name", "Qty", "Expiry Date", "Cashier"};
        TableUtils.printTableHeader(totalWidth, headers ,4);
        
        // TableUtils.singleLine(totalWidth);
        for (String[] row : stockingHistory) {
            // Ensure each field in the row is valid 
            if (row.length == headers.length && rowValid(row)) { 
                TableUtils.printTableRow(totalWidth, row, 4); 
            }
        }
        // System.out.printf("%nGrand Total: "+ Color.YELLOW +"%.2f Php " + Color.RESET , grandTotal);
        System.out.println("\nDate generated: "+ Color.YELLOW + currentDateTime.format(DATETIME_FORMATTER)  + Color.RESET);
        System.out.println("Generated By: " + Color.YELLOW + UserModel.getCurrentUsername() + Color.RESET);
        TableUtils.printLineDivider(totalWidth); // Ending line divider
        TxtUtils.CenteredTitle("*** Nothing Follows ***",totalWidth);
        System.out.println();
        askReturnMenu(totalWidth);
    }
    
    // Helper method to check if a row is valid
    private boolean rowValid(String[] row) {
        for (String field : row) {
            if (field == null || field.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    // Method to write restocking history
    public void writeRestockingHistory(Medicine medicine, int quantity) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RESTOCKING_REPORT_FILE, true))) {
            // Write, ID, Name, Restocking Quantity, Date now, Current Session Cashier
            writer.write(String.format("%d,%s,%d,%s,%s%n", medicine.getId(), medicine.getName(), quantity, LocalDate.now(), UserModel.getCurrentUsername()));
            writer.flush();
        } catch (IOException e) {
            TxtUtils.error("Error writing to restocking history: " + e.getMessage());
        }
    }

    // Display all medicines Current Stocks and Value
    public void inventoryToday() {
        ConsoleUtils.clearConsole(); // Clear the console for clear visibility
        double grandTotal = 0.0;
        int totalWidth = 120;

        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- Actual Inventory Report ---", totalWidth);
        int[] columnWidths = {6, 19, 19, 19, 19, 19, 19}; // Adjust widths Manuallly, should match totalWidth
        String[] headers = {"ID", "Name", "Category", "Price", "Expiry Date", "Ending Stock", "Stock Value"};
        TableUtils.printLineDivider(totalWidth);
        TableUtils.printTableRowAdjustable(totalWidth, headers, columnWidths); // Print header row
        TableUtils.printLineDivider(totalWidth);

        for (Medicine med : medicines) {
            String inventoryDetails = 
                med.getId() + "," +
                med.getName() + "," +
                med.getCategory() + "," +
                med.getPrice() + "," +
                med.getExpiryDate() + "," +
                med.getStockQuantity() + "," +
                med.getStockQuantity() * med.getPrice(); // Concatenate all details into a single String
           
            grandTotal += med.getStockQuantity() * med.getPrice(); // Calculate for Grand Total
            TableUtils.printTableRowAdjustable(totalWidth, inventoryDetails.split(","), columnWidths); // Print each data row
        }

        System.out.printf("\nGrand Total Stock Value: %s%s Php%s", Color.YELLOW, TxtUtils.money(grandTotal), Color.RESET);
        System.out.println("\nDate generated: "+ Color.YELLOW + currentDateTime.format(DATETIME_FORMATTER)  + Color.RESET);
        System.out.println("Generated By: " + Color.YELLOW + UserModel.getCurrentUsername() + Color.RESET);
        TableUtils.printLineDivider(totalWidth); // Ending line divider
        TxtUtils.CenteredTitle("*** Nothing Follows ***",totalWidth);
        askReturnInventoryMenu(totalWidth);
    }

    // Display Detailed Inventory "Beginning", "Restocking", "Sold", "Ending Stock", "Stock Value"
    public void detailedInventory(LocalDate startDate, LocalDate endDate) {
        ConsoleUtils.clearConsole(); // Clear the console for clear visibility
        double grandTotal = 0.0;
        int totalWidth = 120;
        List<String> inventoryDetailedList = new ArrayList<>();

        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- Detailed Inventory of Medicines --", totalWidth);
        String[] headers = {"ID", "Name", "Beginning", "Restocking", "Sold", "Ending Stock", "Stock Value"};
        TableUtils.printTableHeader(totalWidth, headers, 4);
        
        // Print each medicine's details
        for (Medicine medicine : medicines) {
            int stockingQuantity = getConsolidatedStockingQuantityById(medicine.getId(), startDate, endDate);
            int purchaseQuantity = getConsolidatedPurchaseQuantityById(medicine.getId(), startDate, endDate);
            int beginning = medicine.getStockQuantity() + purchaseQuantity - stockingQuantity;  // Force muna, wala muna variance
            int ending = medicine.getStockQuantity(); // Calculate ending stock
            double stockValue = ending * medicine.getPrice(); // Calculate stock value
            grandTotal += stockValue;

            String inventoryDetails = 
                String.valueOf(medicine.getId()) + "," +
                medicine.getName()+ "," +
                beginning + "," +
                stockingQuantity + "," +
                purchaseQuantity + "," +
                ending + "," +
                stockValue; 

            inventoryDetailedList.add(inventoryDetails); // Add the concatenated string to the list
        }
        for (String row : inventoryDetailedList) {
            TableUtils.printTableRow(totalWidth, row.split(","), 4); // Print all the row
        }
        
        System.out.printf("\nGrand Total Stock Value: %s%s Php%s", Color.YELLOW, TxtUtils.money(grandTotal), Color.RESET);
        System.out.println("\nDate generated: "+ Color.YELLOW + currentDateTime.format(DATETIME_FORMATTER)  + Color.RESET);
        System.out.println("Generated By: " + Color.YELLOW + UserModel.getCurrentUsername() + Color.RESET);
        System.out.println("Generated by date range:  " + Color.YELLOW + startDate + Color.RESET + " to " + Color.YELLOW + endDate + Color.RESET);
        TableUtils.printLineDivider(totalWidth); // Ending line divider
        TxtUtils.CenteredTitle("*** Nothing Follows ***",totalWidth);
        askReturnInventoryMenu(totalWidth);
    }

    // Method to get the consolidated quantity for a specific ID with date range from stocking history
    // public int getConsolidatedStockingQuantityById(int medicineId, LocalDate startDate, LocalDate endDate) {
    //     List<String[]> stockingHistory = loadStockingHistory();
    //     int totalQuantity = 0;

    //     for (String[] fields : stockingHistory) {
    //         try {
    //             int id = Integer.parseInt(fields[0]);
    //             int qty = Integer.parseInt(fields[2]);
    //             LocalDate date = LocalDate.parse(fields[3]);

    //             if (id == medicineId && (date.isEqual(startDate) || date.isAfter(startDate)) && (date.isEqual(endDate) || date.isBefore(endDate))) {
    //                 totalQuantity += qty;
    //             }
    //         } catch (NumberFormatException | DateTimeParseException e) {
    //             System.err.println("Error parsing stocking history record: " + e.getMessage());
    //         }
    //     }
    //     return totalQuantity;
    // }

    public int getConsolidatedStockingQuantityById(int medicineId, LocalDate startDate, LocalDate endDate) {
        List<String[]> stockingHistory = loadStockingHistory();
        int totalQuantity = 0;
    
        for (String[] fields : stockingHistory) {
            try {
                // Ensure fields are not empty or null before parsing
                if (fields[0] != null && !fields[0].trim().isEmpty() &&
                    fields[2] != null && !fields[2].trim().isEmpty() &&
                    fields[3] != null && !fields[3].trim().isEmpty()) 
                    {
    
                    int id = Integer.parseInt(fields[0].trim());
                    int qty = Integer.parseInt(fields[2].trim());
                    LocalDate date = LocalDate.parse(fields[3].trim());
    
                    if (id == medicineId && (date.isEqual(startDate) || date.isAfter(startDate)) && (date.isEqual(endDate) || date.isBefore(endDate))) {
                        totalQuantity += qty;
                    }
                }
                // else {
                //     System.err.println("Invalid data in stocking history record: " + String.join(",", fields));
                // }
            } catch (NumberFormatException | DateTimeParseException e) {
                System.err.println("Error parsing stocking history record: " + e.getMessage());
            }
        }
        return totalQuantity;
    }
    

    // Method to get the consolidated quantity for a specific ID with date range from purchase history
    public int getConsolidatedPurchaseQuantityById(int medicineId, LocalDate startDate, LocalDate endDate) {
        List<String[]> purchaseHistory = loadPurchaseHistory();
        int totalQuantity = 0;

        for (String[] fields : purchaseHistory) {
            if(fields.length >= 8) {
                int id = Integer.parseInt(fields[1]);
                int qty = Integer.parseInt(fields[5]);
                LocalDate date = LocalDate.parse(fields[7]);
    
                if (id == medicineId && (date.isEqual(startDate) || date.isAfter(startDate)) && (date.isEqual(endDate) || date.isBefore(endDate))) {
                    totalQuantity += qty;
                }
            }
        }
        return totalQuantity;
    }

    // Press Enter to Exit
    public void askReturnMenu(int totalWidth){
        totalWidth = (totalWidth <= 0) ? 64 : totalWidth;
        returnHelder(totalWidth);
        return;
    }
    public void askReturnInventoryMenu(int totalWidth){
        totalWidth = (totalWidth <= 0) ? 64 : totalWidth;
        returnHelder(totalWidth);
        viewPharmacist.inventoryReport();
    }
    public void returnHelder(int totalWidth){
        TxtUtils.CenteredInline("--- Press "+Color.YELLOW+"Enter"+ Color.RESET+" to return to Main Menu ---",totalWidth);
        validator.validateStringorEmptyInput();
        ConsoleUtils.clearConsole();
    }
    
}
