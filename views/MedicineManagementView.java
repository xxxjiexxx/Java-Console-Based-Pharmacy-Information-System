package views;
import models.*;
import utils.*;
import java.time.*;

import controllers.*;

public class MedicineManagementView{

    private ValidationController validator = new ValidationController();
    private MedicineController controller = new MedicineController();
    private ReportManager report = new ReportManager();
    int totalWidth = 64;

    // Medicine Management Menu
    public void showMenuPharma() {
        ConsoleUtils.clearConsole();
        while (true) {
            TableUtils.doubleLine(totalWidth);
            TxtUtils.CenteredTitle("--- Medicine Management ---", totalWidth);
            TableUtils.doubleLine(totalWidth);
            System.out.println("  Please select an option");
            System.out.println(Color.YELLOW + "  1" + Color.RESET + ". Add Medicine");
            System.out.println(Color.YELLOW + "  2" + Color.RESET + ". Update Medicine");
            System.out.println(Color.YELLOW + "  3" + Color.RESET + ". View List of Medicines");
            System.out.println(Color.YELLOW + "  4" + Color.RESET + ". Delete Medicine");
            System.out.println(Color.YELLOW + "  5" + Color.RESET + ". Search Medicine");
            System.out.println(Color.YELLOW + "  6" + Color.RESET + ". Check Expired Medicines");
            System.out.println(Color.YELLOW + "  7" + Color.RESET + ". Check Low-Level Stocks");
            System.out.println(Color.YELLOW + "  8" + Color.RESET + ". Re-Stocking Medicine");
            System.out.println(Color.YELLOW + "  9" + Color.RESET + ". Return to Main Menu");
            System.out.println(Color.YELLOW + "  10" + Color.RESET + ". Logout");
            System.out.print("  >>> ");
            int choice = validator.validateIntegerInput();
            
            switch (choice) {
                case 1: addNewMedicine();   break;
                case 2: editMedicine();     break;
                case 3: 
                        controller.displayAllMedicinesConsole(); 
                        // controller.displayAllMedicinesFrame();  
                        break;
                case 4: deleteMed(); break;
                case 5: searchMedicine(); break;
                case 6: expiredMedicines(); break;
                case 7: lowlevelStock(); break;
                case 8: restockStock(); break;
                case 9: ConsoleUtils.clearConsole(); return;  
                case 10: validator.logout(); break;
                default: System.out.println("Invalid choice.");
            }
        }
    }
    
    // Add Medicine
    private void addNewMedicine() {
         do { 
            ConsoleUtils.clearConsole();
            int id = controller.getNextId();  // Get the next available ID from the controller
            
            TableUtils.doubleLine(totalWidth);
            TxtUtils.CenteredTitle("--- Add New Medicine ---", totalWidth);
            TableUtils.doubleLine(totalWidth);
            System.out.println("  Please filled up the details:");
            System.out.print("\n  Enter " + Color.YELLOW + "Medicine Name: " + Color.RESET );
            String name = validator.validateStringInput(); 
            
            // System.out.print("  Enter " + Color.YELLOW + "Category : " + Color.RESET );
            String categoryInput = validator.validateCategory();
            Medicine.Category category = Medicine.Category.valueOf(categoryInput);
        
            System.out.print("  Enter " + Color.YELLOW + "Price : " + Color.RESET );
            double price = validator.validateDoubleInput();  // Assuming you have a method to validate doubles
        
            System.out.print("  Enter " + Color.YELLOW + "Stock Quantity : " + Color.RESET );
            int stockQuantity = validator.validateIntegerInput();  
            
            System.out.print("  Enter " + Color.YELLOW + "Expiry Date (YYYY-MM-DD) : " + Color.RESET );
            LocalDate expiryDate = validator.validateDateInput(); 
        
            System.out.print("  Enter " + Color.YELLOW + "Manufacturer : " + Color.RESET );
            String manufacturer = validator.validateStringInput();

            System.out.print("  Need Presciption " + Color.YELLOW + "(true/false)? : " + Color.RESET );
            boolean needPresciption = validator.validateBooleanInput();
        
            // Create new Medicine object
            Medicine newMedicine = new Medicine(id, name, category, price, stockQuantity, expiryDate, manufacturer, needPresciption);
            
            // Add new medicine to the controller
            controller.addMedicine(newMedicine, totalWidth);
            System.out.println();

        }while(controller.askAgain());
    }
     
    // Edit/Update Medicine
    private void editMedicine() {
        ConsoleUtils.clearConsole();
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- Update Medicine ---", totalWidth);
        TableUtils.doubleLine(totalWidth);
        System.out.println("Note: Leave it blank if you wish to retain the same details.");
        System.out.println();
        int id;
        Medicine existingMedicine;

        // Ensure the user enters a valid Medicine ID
        while (true) {
            System.out.print("Enter " + Color.YELLOW + "Medicine ID" + Color.RESET + " to edit: ");
            id = validator.validateIntegerInput();  

            existingMedicine = controller.getMedicineById(id);  // Fetch the medicine details by ID

            if (existingMedicine != null) {
                
                System.out.println();
                TxtUtils.CenteredColoredText("--- Medicine Found!. ---", totalWidth, Color.GREEN);
                System.out.println(existingMedicine);
                break;  // If found, proceed to update the details
            }   
           TxtUtils.CenteredColoredText("--- No Medicine Found! Please try again. ---", totalWidth, Color.RED);
        }

        // Show the xisting details for update
        System.out.println("");
        // Prompt for new details, retaining existing ones if input is blank
        System.out.print("  Enter new Medicine Name [" + Color.YELLOW + existingMedicine.getName() + Color.RESET + "]: ");
        String name = validator.validateStringInput(existingMedicine.getName());

        System.out.print("  Enter new Category [" + Color.YELLOW + existingMedicine.getCategory() + Color.RESET + "]: ");
        String categoryInput = validator.validateCategory(existingMedicine.getCategory().name());
        
        Medicine.Category category = Medicine.Category.valueOf(categoryInput);

        System.out.print("  Enter new Price [" + Color.YELLOW  + existingMedicine.getPrice() + Color.RESET + "]: ");
        double price = validator.validateDoubleInput(existingMedicine.getPrice());

        System.out.print("  Enter new Stock Quantity [" + Color.YELLOW  + existingMedicine.getStockQuantity() + Color.RESET + "]: ");
        int stockQuantity = validator.validateIntegerInput(existingMedicine.getStockQuantity());

        System.out.print("  Enter new Expiry Date (YYYY-MM-DD) [" + Color.YELLOW  + existingMedicine.getExpiryDate() + Color.RESET + "]: ");
        LocalDate expiryDate = validator.validateDateInput(existingMedicine.getExpiryDate());

        System.out.print("  Enter new Manufacturer [" + Color.YELLOW + existingMedicine.getManufacturer() + Color.RESET + "]: ");
        String manufacturer = validator.validateStringInput(existingMedicine.getManufacturer());

        System.out.print("  Enter if need Presciption [" + Color.YELLOW + existingMedicine.getNeedPrescription() + Color.RESET + "]: ");
        boolean needPresciption = validator.validateBooleanInput(existingMedicine.getNeedPrescription());

        // Create the updated medicine object with the new or retained details
        Medicine updatedMedicine = new Medicine(id, name, category, price, stockQuantity, expiryDate, manufacturer, needPresciption);

        // Call the controller's method to update the medicine
        controller.editMedicine(id, updatedMedicine,totalWidth);
        System.out.println();

 }

    // Delete Med
    private void deleteMed() {
        ConsoleUtils.clearConsole();
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- Delete Medicine ---", totalWidth);
        TableUtils.doubleLine(totalWidth);
        System.out.println();
        System.out.print("  Enter " + Color.YELLOW + "Medicine ID" + Color.RESET + " to delete: ");
        int id = validator.validateIntegerInput(); 
        controller.deleteMedicine(id, totalWidth);
    }

    // Search Medicine
    public void searchMedicine() {
        ConsoleUtils.clearConsole();
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- Search Medicine ---", totalWidth);
        TableUtils.doubleLine(totalWidth);
        System.out.println();
       
        System.out.print("  Search (" + Color.YELLOW + "ID, " + Color.YELLOW + "Name" + Color.RESET + ", " + Color.YELLOW + "Category" + Color.RESET + ", " + Color.YELLOW + "Manufacturer" + Color.RESET + "): ");
        String query = validator.validateStringInput();
        System.out.println();
        boolean found = controller.searchMedicine(query);

        if (!found) {
            TxtUtils.CenteredColoredText("--- No Medicine Found --- ", totalWidth, Color.RED);     
        }
        System.out.println();
        System.out.print("Do you want to search again? "+TxtUtils.printYesorNO()+": ");
        if (validator.yesOrNo()) {
            ConsoleUtils.clearConsole();
            searchMedicine();
        } else {
             // if login
            if (UserModel.getCurrentUsername() != null) {
                validator.toReturn();
            }
            // if not/Guest
            else{ 
                ConsoleUtils.clearConsole();
                LoginView loginView = new LoginView(); // Create an instance of LoginView
                loginView.showLoginMenu(); // Call the method on the instance
            }
           
        }
    }

    // Get all Expire Medicine
    public void expiredMedicines(){
        ConsoleUtils.clearConsole();
        int totalWidth = 100;
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- Expired Medicine/s ---", totalWidth);
        controller.checkExpiredMedicines();
        System.out.println();
    }
    
    //Check low level stock
    public void lowlevelStock(){
        ConsoleUtils.clearConsole();
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- Low Level Stock --", totalWidth);
        TableUtils.doubleLine(totalWidth);;
        System.out.println();
        controller.displayLowStockMedicines(10, totalWidth);
    }
    
    //Restock Medicine -Display current stock information
    public void restockStock() {
        ConsoleUtils.clearConsole();
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- Re-Stocking Medicine ---", totalWidth);
        TableUtils.doubleLine(totalWidth);
        System.out.println();

        System.out.print("  Enter Medicine " + Color.YELLOW + "ID"+ Color.RESET +"/" + Color.YELLOW + "Name" +  Color.RESET + ": ");
        String search = validator.validateStringInput();
        
        Medicine medicine = controller.searchMedByIDandName((search));
        
        if( medicine != null ){
            System.out.println();
            System.out.print("  Enter " + Color.YELLOW + "quantity to re-stock ["+Color.YELLOW+ + medicine.getStockQuantity() + Color.RESET +"]: ");
            int quantity = validator.validateIntegerInput(); 

            // Perform restocking
            boolean checkRestock = controller.restockStock(medicine, quantity, totalWidth);
            if(checkRestock){
                report.writeRestockingHistory(medicine, quantity); // Save it to Restocking History

                System.out.println();
                TxtUtils.CenteredColoredText("--- Stock updated successfully ---", totalWidth, Color.GREEN);
                TableUtils.printLineDivider(totalWidth);
                System.out.println();

                System.out.print("Do you want to Add Another Stock? (" + TxtUtils.printYesorNO() + "): ");
                if(validator.yesOrNo()){
                    restockStock();
                }
                else{
                    ConsoleUtils.clearConsole();
                    return;
                }
            }
            
        }
        else{
            System.out.println();
            TxtUtils.CenteredInline(Color.RED+ "--- Medicine ID/Name not Found! ---" + Color.RESET, totalWidth); 
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.print("  Do you want to check again ("+ TxtUtils.printYesorNO() +")? ");
            if(validator.yesOrNo()){
                restockStock();
            }
            else{
                showMenuPharma();
            }
        }
        System.out.println();
    }
}
