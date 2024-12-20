package views;
import models.*;
import utils.*;
import controllers.*;
import java.time.LocalDate;

public class PharmacistView {
    public MedicineController controller = new MedicineController();
    public MedicineManagementView medview = new MedicineManagementView();
    public CustomerController customer = new CustomerController();
    public CustomerView customerView = new CustomerView();
    public ValidationController validator = new ValidationController();
    public ReportManager report = new ReportManager();
    int totalWidth = 64; // Set Width

    // Pharmacist Main Menu
    public void runPharmacistMenu() {
        while (true) {
            TableUtils.doubleLine(totalWidth);
            TxtUtils.CenteredTitle("--- Pharmacist Menu ---", totalWidth);
            TableUtils.doubleLine(totalWidth);
            System.out.println("  Please select an option:");
            System.out.println(Color.YELLOW + "  1" + Color.RESET + ". Manage Medicine");
            System.out.println(Color.YELLOW + "  2" + Color.RESET + ". Check Prescription");
            System.out.println(Color.YELLOW + "  3" + Color.RESET + ". Dispense Medicine");
            System.out.println(Color.YELLOW + "  4" + Color.RESET + ". View Reports");
            System.out.println(Color.YELLOW + "  5" + Color.RESET + ". Customer Management");
            System.out.println(Color.YELLOW + "  6" + Color.RESET + ". Logout");
            System.out.print("  >>> ");
            int choice = validator.validateIntegerInput();
            switch (choice) {
                case 1: medview.showMenuPharma(); break;
                case 2: checkPrescription(); break;
                case 3: dispenseMedicine(); break;
                case 4: viewReports(); break;
                case 5: customerManagement(); break;
                case 6: validator.logout(); break;
                default: ConsoleUtils.clearConsole(); TxtUtils.invalidChoice(); break;
            }
        }
    }

    // Search Medicine - to use in child class
    public void searchMedicine() {
        medview.searchMedicine();
    } 

    // Check Precription - to use in child class
    public void checkPrescription() {
        ConsoleUtils.clearConsole();
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- Prescription Checker ---", totalWidth);
        TableUtils.doubleLine(totalWidth);
        System.out.println();
        System.out.print("  Enter the Customer "+ Color.YELLOW +"ID"+ Color.RESET+"/"+ Color.YELLOW +"Name"+ Color.RESET+" to check: ");
        String input = validator.validateStringInput();

        Customer existingCustomer = customer.findCustomer(input, totalWidth);
        
        String message;
        if (existingCustomer != null) {
           
            if (existingCustomer.isWithPrescription()) {
                LocalDate validity = existingCustomer.getPrescriptionValidity();
                if (validity == null) {
                    message = "--- Prescription validity date is missing! ---";
                } else if (!validity.isBefore(LocalDate.now())) {
                    message = Color.GREEN + "--- The prescription is valid! ---" + Color.RESET;
                } else {
                    message = Color.RED + "--- The prescription has expired! ---"+ Color.RESET;
                }
            } else {
                message = Color.RED + "--- Customer does not hava a prescription. ---"+ Color.RESET;
            }
        } 
        else {
            message = Color.RED + "--- Customer with ID/Name does not exist. ---"+ Color.RESET;
        }
        System.err.println();
        TxtUtils.CenteredTitle(message, totalWidth);
        TableUtils.printLine(totalWidth);
        recheckPrescription();  // check again prescription
    }

    // Prescritiptio Loop again Helper
    public void recheckPrescription(){
        // check again prescription
        System.out.print("  Do you want to check again? "+TxtUtils.printYesorNO()+": ");
        if(validator.yesOrNo()){
            checkPrescription();
        }
        else{
            ConsoleUtils.clearConsole();
            return;
        }
    }

    // Dispence/Sell Medicine - to use in child class
    public void dispenseMedicine() {
        DispenseMedicine dispenser = new DispenseMedicine();
        dispenser.startDispensingProcess();
    }

    // Report Views - to use in child class
    public void viewReports() {
        ConsoleUtils.clearConsole();
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- Choose a report type ---", totalWidth);
        TableUtils.doubleLine(totalWidth);
        System.out.println("  Please select an option:");
        System.out.println(Color.YELLOW + "  1" + Color.RESET + ". Sales Report");
        System.out.println(Color.YELLOW + "  2" + Color.RESET + ". Inventory Report");
        System.out.println(Color.YELLOW + "  3" + Color.RESET + ". Customer History");
        System.out.println(Color.YELLOW + "  4" + Color.RESET + ". Re-stocking History");
        System.out.println(Color.YELLOW + "  5" + Color.RESET + ". Return to Main Menu");
        System.out.println(Color.YELLOW + "  6" + Color.RESET + ". Logout");
        System.out.print("  >>> ");
        int reportChoice = validator.validateIntegerInput();

        switch (reportChoice) {
            case 1: salesReport(); break;
            case 2: inventoryReport(); break;
            case 3: customerPurchaseHistory(); break;
            case 4: report.reStockingHistory();  break;
            case 5: ConsoleUtils.clearConsole(); return;  
            case 6: validator.logout(); break;
            default:TxtUtils.CenteredColoredText("--- Invalid Input, Please try again. ---", totalWidth, Color.RED);
                    ConsoleUtils.sleep();
                    viewReports();
                    break;
                     
        }
    }

    // Customer Managment Menu
    public void customerManagement() {
        customerView.manageCustomers();
    }

    // Customer Purchase History
    public void customerPurchaseHistory(){
        ConsoleUtils.clearConsole();
        while (true) {
            TableUtils.doubleLine(totalWidth);
            TxtUtils.CenteredTitle("--- Customer History Report ---", totalWidth);
            TableUtils.doubleLine(totalWidth);
            System.out.println("  Please select an option:");
            System.out.println(Color.YELLOW + "  1" + Color.RESET + ". All Customer History");
            System.out.println(Color.YELLOW + "  2" + Color.RESET + ". All Customer History (by Date Range)");
            System.out.println(Color.YELLOW + "  3" + Color.RESET + ". Specific Customer History");
            System.out.println(Color.YELLOW + "  4" + Color.RESET + ". Specific Customer History (by Date Range)");
            System.out.println(Color.YELLOW + "  5" + Color.RESET + ". Return to Main Menu");
            System.out.println(Color.YELLOW + "  6" + Color.RESET + ". Logout");
            System.out.print("  >>> ");
            int choice = validator.validateIntegerInput();

            switch (choice) {
                case 1: report.filterCustomerHistory("all", null, null); break;
                case 2:
                    System.out.println("");
                    System.out.print("  Enter Start Date ("+Color.YELLOW+"YYYY-MM-DD"+Color.RESET+"): ");
                    LocalDate startDate = validator.validateDateInput();

                    System.out.print("  Enter End Date ("+Color.YELLOW+"YYYY-MM-DD"+Color.RESET+"): ");
                    LocalDate endDate = validator.validateDateInput();

                    report.filterCustomerHistory("all", startDate, endDate);
                    break;
                case 3:
                    while (true) {
                        System.out.println();
                        System.out.print("  Enter Customer " + Color.YELLOW + "ID" + Color.RESET + "/" + Color.YELLOW + "Name" + Color.RESET + ": ");
                        String getCustomer = validator.validateStringInput();
                        
                        if (customer.findCustomer(getCustomer, totalWidth) != null) {
                            report.filterCustomerHistory(getCustomer, null, null);
                            break; // Exit the loop after a successful search
                        } else {
                            searchAgin();
                            if (!validator.yesOrNo()) {
                                ConsoleUtils.clearConsole();
                                break; // Exit the loop if the user does not want to search again
                            }
                        }
                    }
                    break;
                case 4:
                    while (true) {
                        System.out.println();
                        System.out.print("  Enter Customer "+Color.YELLOW+"ID"+Color.RESET+"/"+ Color.YELLOW +"Name"+ Color.RESET+": ");
                        String getCustomerbyDaterange = validator.validateStringInput();

                        System.out.print("  Enter Start Date ("+Color.YELLOW+"YYYY-MM-DD"+Color.RESET+"): ");
                        LocalDate startDatebyDaterange = validator.validateDateInput();

                        System.out.print("  Enter End Date ("+Color.YELLOW+"YYYY-MM-DD"+Color.RESET+"): ");
                        LocalDate endDatebyDaterange = validator.validateDateInput();
                        System.out.println();
                        
                        if(!validator.validateDateRangeInput(startDatebyDaterange, endDatebyDaterange)){
                            TableUtils.printLine(totalWidth);
                            searchAgin();
                            if (!validator.yesOrNo()) {
                                ConsoleUtils.clearConsole();
                                break; // Exit the loop if the user does not want to search again
                            }
                        }

                        if (customer.findCustomer(getCustomerbyDaterange, totalWidth) != null){
                            report.filterCustomerHistory(getCustomerbyDaterange, startDatebyDaterange, endDatebyDaterange);
                        }
                        else{
                            searchAgin();
                            if (!validator.yesOrNo()) {
                                ConsoleUtils.clearConsole();
                                break; // Exit the loop if the user does not want to search again
                            }
                        }
                    }
                    break;
                case 5: ConsoleUtils.clearConsole(); return;  
                case 6: validator.logout(); break;    
                default:
                        TxtUtils.CenteredColoredText("--- Invalid Input, Please try again. ---", totalWidth, Color.RED);
                        ConsoleUtils.sleep();
                        customerPurchaseHistory();
                        break;
            }
        }   
        
    }

    // To be transfer in specific file/class
    public void salesReport(){
        ConsoleUtils.clearConsole();
        while (true) {
            TableUtils.doubleLine(totalWidth);
            TxtUtils.CenteredTitle("--- Sales Report ---", totalWidth);
            TableUtils.doubleLine(totalWidth);
            System.out.println("  Please select an option:");
            System.out.println(Color.YELLOW + "  1" + Color.RESET + ". Today");
            System.out.println(Color.YELLOW + "  2" + Color.RESET + ". Specific Date");
            System.out.println(Color.YELLOW + "  3" + Color.RESET + ". Date Range");
            System.out.println(Color.YELLOW + "  4" + Color.RESET + ". Return to Report Menu");
            System.out.println(Color.YELLOW + "  5" + Color.RESET + ". Return to Main Menu");
            System.out.println(Color.YELLOW + "  6" + Color.RESET + ". Logout");
            System.out.print("  >>> ");
            int choice = validator.validateIntegerInput();
        
            switch (choice) {
                case 1: report.filterSalesReport(LocalDate.now(), null); break;  // Sales Report Today
                case 2:
                    // Filter for a specific date
                    System.out.print("  Enter the date (" + Color.YELLOW + "YYYY-MM-DD" + Color.RESET + "): ");
                    LocalDate specificDate = validator.validateDateInput();
                    report.filterSalesReport(specificDate, null);
                    break;
                case 3:
                    // Filter for a date range
                    System.out.print("Enter the start date (" + Color.YELLOW + "YYYY-MM-DD" + Color.RESET + "): ");
                    LocalDate startDate = validator.validateDateInput();

                    System.out.print("Enter the end date (" + Color.YELLOW + "YYYY-MM-DD" + Color.RESET + "): ");
                    LocalDate endDate = validator.validateDateInput();

                    if (startDate != null && endDate != null && !endDate.isBefore(startDate)) {
                        report.filterSalesReport(startDate, endDate);
                    } 
                    else if (endDate.isBefore(startDate)) {
                        TxtUtils.error("End date must be on or after the start date.");
                    }
                    break;
                case 4: viewReports(); break;
                case 5: ConsoleUtils.clearConsole(); 
                case 6: validator.logout(); break;
                default:
                    TxtUtils.CenteredColoredText("--- Invalid choice. Please enter 1 to 6. ---", totalWidth, Color.RED);
                    ConsoleUtils.sleep();
                    salesReport();
                    break;
            }
        }
    }

    public void inventoryReport(){
        ConsoleUtils.clearConsole();
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- Filter Inventory Report ---", totalWidth);
        TableUtils.doubleLine(totalWidth);
        System.out.println("  Please select an option:");
        System.out.println(Color.YELLOW + "  1" + Color.RESET + ". Today");
        System.out.println(Color.YELLOW + "  2" + Color.RESET + ". Detailed Inventory (by Date Range)");
        System.out.println(Color.YELLOW + "  3" + Color.RESET + ". Return to Report Menu");
        System.out.println(Color.YELLOW + "  4" + Color.RESET + ". Logout");
        System.out.print("  >>> ");
        int choice = validator.validateIntegerInput();
        switch (choice) {
            case 1: report.inventoryToday(); break;
            case 2: // Filter by Date Range
                System.out.println();
                System.out.print("  Enter the start date ("+Color.YELLOW+"YYYY-MM-DD"+Color.RESET+"): "); 
                LocalDate startDate = validator.validateDateInput();
                  
                System.out.print("  Enter the end date ("+Color.YELLOW+"YYYY-MM-DD"+Color.RESET+"): ");  
                LocalDate endDate = validator.validateDateInput();

                report.detailedInventory(startDate, endDate);
                break;
            case 3: viewReports(); break;
            case 4: validator.logout();
            default:
                TxtUtils.CenteredColoredText("--- Invalid choice. Please enter 1 to 4. ---", totalWidth, Color.RED);
                ConsoleUtils.sleep();
                inventoryReport();
                break;
        }
    }

    public void searchAgin(){
        TxtUtils.CenteredColoredText("--- No Customer Record Found! ---", totalWidth, Color.RED);
        TableUtils.printLine(totalWidth);
        System.out.print("  Do you want to search again? (" + TxtUtils.printYesorNO() + "): ");
           
    }
}
