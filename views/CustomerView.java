package views;
import models.*;
import utils.*;
import controllers.*;
import java.time.LocalDate;

public class CustomerView {
    private ValidationController validator = new ValidationController();
    private CustomerController customerController = new CustomerController();
    int totalWidth = 64;

    public void manageCustomers() {
        ConsoleUtils.clearConsole();

        while (true) {
            TableUtils.doubleLine(totalWidth);
            TxtUtils.CenteredTitle("--- Customer Management ---", totalWidth);
            TableUtils.doubleLine(totalWidth);
            System.out.println("  Please select an option:");
            System.out.println(Color.YELLOW + "  1" + Color.RESET + ". View All Customers");
            System.out.println(Color.YELLOW + "  2" + Color.RESET + ". Add Customer");
            System.out.println(Color.YELLOW + "  3" + Color.RESET + ". Edit/Update Customer");
            System.out.println(Color.YELLOW + "  4" + Color.RESET + ". Delete Customer");
            System.out.println(Color.YELLOW + "  5" + Color.RESET + ". Return to Main Menu");
            System.out.println(Color.YELLOW + "  6" + Color.RESET + ". Logout");
            System.out.println();
            System.out.print("  >>> ");
            int choice = validator.validateIntegerInput();

            switch (choice) {
                case 1: customerController.viewCustomers(); break;
                case 2: createCustomerWithTitle(); break;
                case 3: updateCustomer(); break;
                case 4: deleteCustomer(); break;
                case 5: ConsoleUtils.clearConsole(); return;  // Go back to the main menu
                case 6: validator.logout(); break;  // Go back to the main menu
                default: TxtUtils.CenteredColoredText("Invalid choice. Please try again." , totalWidth, Color.RED); 
                        break;
            }
        }
    }

    // Add Customer Title
    public void createCustomerWithTitle() {
        ConsoleUtils.clearConsole();
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- Create Customer ---", totalWidth);
        TableUtils.doubleLine(totalWidth);
        System.out.println();
        createCustomer();
        TableUtils.printLine(totalWidth);
        customerController.askReturnMenu(totalWidth);
    }

    // Add Customer
    public Customer createCustomer() {
        System.out.print(" Enter " + Color.YELLOW + "Customer Name" + Color.RESET+ ": ");
        String name = validator.validateStringInput();
        System.out.print(" With Prescription (" + Color.YELLOW + "true" + Color.RESET+ "/" + Color.YELLOW + "false" + Color.RESET+ ")?: ");
        boolean withPrescription = validator.validateBooleanInput();
        LocalDate prescriptionValidity;

        // Check if meron prescription, if wala set validity good for today
        if (withPrescription) {
            System.out.print(" Prescription " + Color.YELLOW + "Validity Date (" + Color.YELLOW + "YYYY-MM-DD" + Color.RESET + "): ");
            prescriptionValidity = validator.validateDateInput();
        } else {
            prescriptionValidity = LocalDate.now(); // Default to current date if no prescription
        }
        System.out.println();
        ConsoleUtils.clearMultipleLines(5);
        ConsoleUtils.moveCursorUp(4);
        Customer createdCustomer = customerController.createCustomer(name, withPrescription, prescriptionValidity);
        System.out.println();
        return createdCustomer; // Return the success status
    }

    // Update Customer
    public void updateCustomer() {
        ConsoleUtils.clearConsole();
        
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- Update Customer ---", totalWidth);
        TableUtils.doubleLine(totalWidth);
        System.out.println();

        System.out.print("  Enter the Customer "+ Color.YELLOW +"ID"+ Color.RESET+"/"+ Color.YELLOW +"Name"+ Color.RESET+" to update: ");
        String input = validator.validateStringInput();

        Customer existingCustomer = customerController.findCustomer(input, totalWidth);
        // Check if the customer exists
        if (existingCustomer == null) {
            TxtUtils.error("  Customer with "+ Color.YELLOW +"ID"+ Color.RESET+"/"+ Color.YELLOW +"Name"+ Color.RESET+"  does not exist.");
            TableUtils.printLine(totalWidth);
            
            System.out.print("  Do you wannt to update another Customer? "+ TxtUtils.printYesorNO()+": ");
            if(validator.yesOrNo()){
                updateCustomer();
            }
            else{
                manageCustomers();
            }
            return; // Ensure no further code runs if the customer doesn't exist
        }
        System.out.println("");
        TxtUtils.CenteredTitle("***Leave it blank if you wish to retain same details.***", totalWidth);
       
        // Prompt for new details, retaining existing ones if input is blank
        System.out.print(" Enter new Name [" + Color.YELLOW + existingCustomer.getCustomerName() + Color.RESET + "]: ");
        String newName = validator.validateStringInput(existingCustomer.getCustomerName());
        
        System.out.print(" With Prescription [" + Color.YELLOW + existingCustomer.isWithPrescription() + Color.RESET + "] (true/false): "); 
        boolean withPrescription = validator.validateBooleanInput(existingCustomer.isWithPrescription());

        System.out.print(" Enter Prescription Validity Date (YYYY-MM-DD) [" + Color.YELLOW + existingCustomer.getPrescriptionValidity() + Color.RESET + "]: ");
        LocalDate prescriptionValidity = validator.validateDateInput(existingCustomer.getPrescriptionValidity());

        customerController.updateCustomer(existingCustomer.getId(), newName, withPrescription, prescriptionValidity);
       
        TableUtils.printLine(totalWidth);
        customerController.askReturnMenu(totalWidth);
    }

     // Remove Customer
    public void deleteCustomer() {
        ConsoleUtils.clearConsole();
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- Delete Customer ---", totalWidth);
        TableUtils.doubleLine(totalWidth);
        System.out.println();
        System.out.print(" Enter the Customer "+ Color.YELLOW +"ID"+ Color.RESET +" to delete: ");
        int id = validator.validateIntegerInput();
        customerController.deleteCustomer(id,totalWidth);
    }
}