package controllers;
import models.*;
import utils.*;
import views.MedicineManagementView;

import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.time.LocalDate;

public class MedicineController {
   
    private List<Medicine> medicines = new ArrayList<>();
    private final String filePath = "db/medicines.txt";  // File path to store medicine data
    private int nextId;  // Variable to track the next available ID
    ValidationController validator = new ValidationController();
    private static MedicineController instance; // Singleton instance
  
    // Constructor to load medicines from file
    public MedicineController() {
        loadMedicinesFromFile();
    }

    // Method to get the singleton instance
    public static MedicineController getInstance() {
        if (instance == null) {
            instance = new MedicineController();
        }
        return instance;
    }
    
    // Load medicines from a file
    private void loadMedicinesFromFile() {
        try {
            // Check if the file exists, if not, it will be created.
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                System.out.println("File not found. A new file will be created.");
                Files.createFile(path);  // Create the file if it doesn't exist
            }
            
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] details = line.split(",");
                    
                    // int id = Integer.parseInt(details[0].trim());
                    // Ensure that details[0] is not empty or null before parsing
                    if (details[0] != null && !details[0].trim().isEmpty()) {
                        int id = Integer.parseInt(details[0].trim());
                        String name = details[1].trim();
                        Medicine.Category category = Medicine.Category.valueOf(details[2].trim());
                        double price = Double.parseDouble(details[3].trim());
                        int stockQuantity = Integer.parseInt(details[4].trim());
                        LocalDate expiryDate = LocalDate.parse(details[5].trim());
                        String manufacturer = details[6].trim();
                        boolean needPrescription = Boolean.parseBoolean(details[7].trim());

                        Medicine medicine = new Medicine(id, name, category, price, stockQuantity, expiryDate, manufacturer, needPrescription);
                        medicines.add(medicine);
                        nextId = Math.max(nextId, id + 1);  // Update nextId to be 1 greater than the highest existing ID
                    } 
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading medicines from file: " + e.getMessage());
        }
    }

    // Save medicines to file
    private void saveMedicinesToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            for (Medicine medicine : medicines) {
                writer.write(medicine.getId() + "," +
                        medicine.getName() + "," +
                        medicine.getCategory() + "," +
                        medicine.getPrice() + "," +
                        medicine.getStockQuantity() + "," +
                        medicine.getExpiryDate() + "," +
                        medicine.getManufacturer() + "," +
                        medicine.getNeedPrescription()
                        );
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving medicines to file: " + e.getMessage());
        }
    }

    // Get the next available ID
    public int getNextId() {
        return nextId;  // Return the current nextId value
    }

    // Add or Create Medicine
    public void addMedicine(Medicine medicine, int totalWidth) {
        medicine = new Medicine(nextId, medicine.getName(), medicine.getCategory(), medicine.getPrice(),
                medicine.getStockQuantity(), medicine.getExpiryDate(), medicine.getManufacturer(), medicine.getNeedPrescription());
        medicines.add(medicine);
        nextId++;  // Increment the next available ID
        saveMedicinesToFile(); // Save to file
        TxtUtils.CenteredColoredText("--- Medicine Added Successfully ---", totalWidth, Color.GREEN);
        TableUtils.printLine(totalWidth);
    }

    // Edit Medicine
    public void editMedicine(int id, Medicine updatedMedicine, int totalWidth) {
        boolean checkUpdated = false;

        for (int i = 0; i < medicines.size(); i++) {
            if (medicines.get(i).getId() == id) {
                medicines.set(i, updatedMedicine);
                saveMedicinesToFile();
                checkUpdated = true;
            }
        }
        if(checkUpdated){
            System.out.println();
            TxtUtils.CenteredColoredText("--- Medicine with ID " + id + " has been successfully updated.--- ", 64, Color.GREEN);
            TableUtils.printLine(totalWidth);
            askReturnMenu(totalWidth);
        }
    }

    // Delete Medicine
    public void deleteMedicine(int id, int totalWidth) {
        
        Medicine medicine = searchMedByIDandName(String.valueOf(id));
        System.out.println();

        if (medicine != null) {
            // verify to delete
            System.out.println();
            System.out.print("  Are you sure you want to delete this Medicine? ("+ TxtUtils.printYesorNO() +"): ");
            if(validator.yesOrNo()){
                medicines.removeIf(med -> med.getId() == id);
                saveMedicinesToFile();
                System.out.println();
                TxtUtils.CenteredColoredText("--- This medicine is Successfully Deleted! ---", totalWidth, Color.GREEN);
                TableUtils.printLine(totalWidth);
                askReturnMenu(totalWidth);
            }
            else{
                System.out.println();
                TxtUtils.CenteredColoredText("--- Delete Transaction Cancelled! ---", totalWidth, Color.RED);
                TableUtils.printLine(totalWidth);
                askReturnMenu(totalWidth);
            }

        } else {
            TxtUtils.CenteredColoredText("--- Medicine to delete not found! ---", totalWidth, Color.RED);
            TableUtils.printLine(totalWidth);
            askReturnMenu(totalWidth);
        }
        
    }

    // Display all medicines
    public void displayAllMedicinesConsole() {
        ConsoleUtils.clearConsole(); // clear sa taas para makita list of medicine
        reloadMedicinesFromFile(); // Ensure medicines list is up-to-date
        int totalWidth = 120;

        TableUtils.doubleLine(totalWidth );
        TxtUtils.CenteredTitle("---- List of Medicines ----", totalWidth);
        String[] headers = {"ID", "Name", "Category", "Price", "Stock" , "Expiry Date" , "Manufacturer", "Prescription"};
        TableUtils.printTableHeader(totalWidth, headers ,4);
        
        for (Medicine medicine : medicines) {
            TableUtils.printTableRow(totalWidth, medicine.toStringArray(), 4);
        }
        TableUtils.printLineDivider(totalWidth);
        TxtUtils.CenteredInline("*** Nothing Follows ***", totalWidth);
        System.out.println(); 
        askReturnMenu(totalWidth);
    }

    // Search Medicine and return full details with return true of false
    public boolean searchMedicine(String query) {
        boolean checkResult = false;
        for (Medicine medicine : medicines) {
            if (String.valueOf(medicine.getId()).equals(query) || 
                medicine.getName().equalsIgnoreCase(query) || 
                medicine.getCategory().toString().equalsIgnoreCase(query) || 
                medicine.getManufacturer().equalsIgnoreCase(query)) {
                System.out.println(medicine.medMultipleLine());
                checkResult = true;
            }
        }
        return checkResult;
    }
    
    // Search Medicine by name/id and return it
    public Medicine searchMedByIDandName(String input) {
        for (Medicine medicine : medicines) {
            // Check if input matches name or ID
            if (medicine.getName().equalsIgnoreCase(input) || 
                String.valueOf(medicine.getId()).equals(input)) {
                System.out.println(medicine);
                return medicine;  // Return the matched medicine
            }
        }
        return null;  // Return null if no match is found
    }

    // Helper ko
    public Medicine getMedicineById(int id) {
        for (Medicine medicine : medicines) {
            if (medicine.getId() == id) {
                return medicine; // Return the medicine if found
            }
        }
        return null; // Return null if no medicine is found with the given ID
    }
    
    //Get Specific Medicine Stock for Checker and Return current Stock
    public Integer getMedStock(String input) {
        for (Medicine medicine : medicines) {
            // Check if input matches name or ID
            if (medicine.getName().equalsIgnoreCase(input) || 
                String.valueOf(medicine.getId()).equals(input)) {
                return medicine.getStockQuantity();  // Return the stock quantity
            }
        }
        return null;  // Return null if no match is found
    }

    // Check Expire Medicines
    public void checkExpiredMedicines() {
        int totalWidth = 100;
        boolean hasExpiredMedicines = false;
        double grandTotal = 0.0;
        String[] headers = {"ID", "Name", "Category", "Brand", "Date","Price","Qty", "Total"};
        TableUtils.printTableHeader(totalWidth, headers ,4);
        
        for (Medicine medicine : medicines) {
            if (medicine.getExpiryDate().isBefore(LocalDate.now())) {

                double total = medicine.getPrice() * medicine.getStockQuantity(); // Calculate total
                grandTotal += total;

                String[] expiredMedRow = {
                    String.valueOf(medicine.getId()),
                    medicine.getName(),
                    medicine.getCategory().toString(),
                    medicine.getManufacturer(),
                    medicine.getExpiryDate().toString(),
                    String.format("%.2f", medicine.getPrice()),
                    String.valueOf(medicine.getStockQuantity()),
                    String.format("%.2f", total)
                };

                TableUtils.printTableRow(totalWidth, expiredMedRow, 4); // Use totalWidth for the table
                hasExpiredMedicines = true;
            }
        }

        if (!hasExpiredMedicines) {
            TxtUtils.CenteredColoredText("---- No medicines are expired. ---- ", totalWidth, Color.GREEN);
            System.out.println();
        }
        
        System.out.printf("\nGrand Total: %s%s Php%s", Color.YELLOW, TxtUtils.money(grandTotal), Color.RESET);
        System.out.println("\nDate generated: "+ Color.YELLOW + LocalDate.now()  + Color.RESET);
        System.out.println("Generated By: " + Color.YELLOW + UserModel.getCurrentUsername() + Color.RESET);
        TableUtils.printLineDivider(totalWidth); // Ending line divider
        System.out.println();
        askReturnMenu(totalWidth);
    }

     // Alert - Expired Medicines
    public String alertCheckExpiredMedicines() {
        StringBuilder expiredMedicines = new StringBuilder(" ------------------------------- Expired Medicine/s ------------------------------- \n");
        expiredMedicines.append(String.format("%-5s%-20s%-20s%-20s%-20s\n", "ID", "Name", "Stocks", "Expiry Date", "Manufacturer"));
        expiredMedicines.append("-----------------------------------------------------------------------------------\n");
        for (Medicine medicine : medicines) {
            if (medicine.getExpiryDate().isBefore(LocalDate.now())) {
               
                expiredMedicines.append(String.format("%-5d%-20.20s%-20d%-20.10s%-20.20s\n", 
                        medicine.getId(), 
                        medicine.getName(), 
                        medicine.getStockQuantity(),
                        medicine.getExpiryDate(), 
                        medicine.getManufacturer()
                        ));
            }
        }

        return expiredMedicines.toString(); // Ensure a String is always returned
    }

    // Deduck Stocks after payment
    public void deductStock(Medicine medicine, int quantity) {
        if (medicine.getStockQuantity() >= quantity) {
            medicine.setStockQuantity(medicine.getStockQuantity() - quantity);
            saveMedicinesToFile();  // Save updated data to the file
        } 
    }

    // Restock stock after receiving new stock
    public boolean restockStock(Medicine medicine, int quantity, int totalWidth) {
        boolean checkRestock = true;
        if (quantity > 0) {
            medicine.setStockQuantity(medicine.getStockQuantity() + quantity);
            saveMedicinesToFile();  // Save updated data to the file
            

        } else {
            TxtUtils.error("Invalid quantity for restocking. Quantity must be greater than 0.");
        }

        return checkRestock;
    }
    
    // Low Level Stock with assign Threshhold
    public void displayLowStockMedicines(int lowStockThreshold, int totalWidth) {
        boolean foundLowStocks = false; // Flag to check if any low stock medicines are found
       
        // Loop through the medicines list
        for (Medicine medicine : medicines) {
            // Check if the stock is below the specified threshold
            if (medicine.getStockQuantity() < lowStockThreshold) {
                System.out.println(medicine.medMultipleLine());
                foundLowStocks = true;
            }
        }
        
        TableUtils.printLineDivider(totalWidth);

        if(foundLowStocks){
            System.out.println("\n\n");
            System.out.print("  Do you want to proceed for "+Color.UNDERLINE+"Re-Stocking"+Color.RESET+"? ("+ TxtUtils.printYesorNO() +"): ");
            if(validator.yesOrNo()){
                MedicineManagementView medView = new MedicineManagementView();
                medView.restockStock();
            }
            else{
                ConsoleUtils.clearConsole();
                return;
            }
        }   
        else {
            TxtUtils.CenteredColoredText("No medicines with stock below the threshold.", lowStockThreshold, Color.GREEN);
            System.out.println();
        }
    }
    
    // Reload medicines from file
    public void reloadMedicinesFromFile() {
        medicines.clear();          // Clear the existing list
        loadMedicinesFromFile();    // Reload medicines into the list
    }
    
    // Method to get the list of medicines
    public List<Medicine> getMedicines() {
        return medicines; // Return the list of medicines
    }
    
    // Ask Another medicine to input in cart
    public boolean askAgain(){
        System.out.print("Do you want to add another medicine? (" + TxtUtils.printYesorNO() + "): ");
        if(validator.yesOrNo()){
            ConsoleUtils.clearConsole();
            return true;
        }
        else{
            ConsoleUtils.clearConsole();
            return false;
        }
    }

    // Press Enter to Exit
    public void askReturnMenu(int totalWidth){
        System.out.println();
        TxtUtils.CenteredInline("--- Press "+Color.YELLOW+"Enter"+ Color.RESET+" to return to Main Menu ---",totalWidth);
        validator.validateStringorEmptyInput();
        ConsoleUtils.clearConsole();
        return;
    }

}
