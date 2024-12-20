package views;
import controllers.*;
import models.*;
import utils.*;
import java.util.*;
import java.time.*;

public class DispenseMedicine {

    private MedicineController medicineController = new MedicineController();
    private ValidationController validator = new ValidationController();
    private CustomerController customerController = new CustomerController();
    private CustomerView customerView = new CustomerView();
    private ReportManager reportManager = new ReportManager();
    PharmacistView pharmacistView = new PharmacistView(); // Create an instance of PharmacistView
    AssistantView assistantView = new AssistantView(); // Create an instance of PharmacistView
    private List<CartItem> cart = new ArrayList<>(); // storage for Medicine Cart
    
    public String transactionID = validator.tid(); // Transaction ID for Invoice
    public int totalWidth= 64;
    
    // Start Dispensing/Selling Medicine
    public void startDispensingProcess() {
        ConsoleUtils.clearConsole();
        clearCart();   // Try to clear the cart first muna if may laman
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- Dispense Medicine --- ", totalWidth);
        TableUtils.doubleLine(totalWidth);
        System.out.println();
    
        Customer customer = determineCustomer(); // call the Method to detemine customer
        Customer.setCurrentCustomer(customer); // Set session customer name
    
        addMedicinesToCart(); // Add medicines to the cart
       
        // After Checkout, Print Billing Summary
        if (printCartSummary(customer.getCustomerName())) {
            double grandTotal = computeGrandTotal();
            Color.printInColor("\n Grand Total: " + grandTotal + " Php", Color.YELLOW);
            handlePayment(grandTotal, cart);
            //call and pass the data to the method for savings
            reportManager.recordPurchaseHistory(customer, cart, transactionID, UserModel.getCurrentUsername());  // Add to history
            reportManager.generateSalesReport(cart);    // Add to Sales Report
        }
        
        clearCart();   // Clear cart and end transaction
    }
   
    // Customer Interaction to check type of customer and presciption
    private Customer determineCustomer() {
        System.out.print("With Prescription " + TxtUtils.printYesorNO() + ": ");

        if (validator.yesOrNo()) {
            System.out.print("Enter customer " + Color.YELLOW + "name" + Color.RESET + " or " + Color.YELLOW + "ID: " + Color.RESET);
            String customerInput = validator.validateStringInput();
            Customer existingCustomer = customerController.findCustomer(customerInput, totalWidth);

            if (existingCustomer != null) {
                System.out.println();
                return existingCustomer;
            } else {
                System.out.println();
                TxtUtils.CenteredColoredText("--- Customer not found! ---", totalWidth, Color.RED);
                System.out.println();
                System.out.print(" Do you want to add a " + Color.UNDERLINE + "new customer?" + Color.RESET + " " + TxtUtils.printYesorNO() + ": ");
                if (validator.yesOrNo()) {
                    ConsoleUtils.clearMultipleLines(7);
                    ConsoleUtils.moveCursorUp(6);
                    return customerView.createCustomer(); // Create the new customer
                } else {
                    ConsoleUtils.clearMultipleLines(7);
                    ConsoleUtils.moveCursorUp(6);
                    System.out.println("Customer: " + Color.YELLOW + "Walk-in" + Color.RESET);
                    return customerController.createWalkinCustomer(true);
                }
            }
        } else {
            System.out.println("Customer: " + Color.GREEN + "Walk-in" + Color.RESET);
            return customerController.createWalkinCustomer(false);
        }
    }

    // Method to Add Medicine to cart
    private void addMedicinesToCart() {
        int itemCounter = 0; // Set counter
        Customer currentCustomer = Customer.getCurrentCustomer(); // get current customer session
    
        while (true) {
            // I want to print the condition result directly
            System.out.print(
                (itemCounter == 0 ? "Enter medicine " : "Add another medicine ") +
                Color.YELLOW + "Name" + Color.RESET + "/" + Color.YELLOW + "ID" + Color.RESET + " : "
            );
    
            String medicineName = validator.validateStringInput();
            
            // Search for medicine by name or ID
            Medicine medicine = medicineController.searchMedByIDandName(medicineName);
            if (medicine == null) {
                TxtUtils.CenteredColoredText("--- Medicine not found ---", totalWidth, Color.RED);
                if (!askToAddAnotherItem()) break; // Exit if the user doesn't want to add another item
                continue;
            }
            
            // Check medicine need prescription
            if (medicine.getNeedPrescription()) {
                boolean noValidPrescription = !currentCustomer.isWithPrescription() || 
                                               currentCustomer.getPrescriptionValidity().isBefore(LocalDate.now());

                if (noValidPrescription) {
                    // I use ternary to direct print
                    TxtUtils.error(currentCustomer.isWithPrescription() ? 
                                    " --- Prescription is expired --- " : 
                                    "This medicine requires a doctor's prescription.");
                    
                    if (!askToAddAnotherItem()) break;
                    continue;
                }
            }
            
            // Check if the medicine is expired
            if (medicine.getExpiryDate().isBefore(LocalDate.now())) {
                TxtUtils.CenteredColoredText("--- Medicine is expired --- ", totalWidth, Color.RED);
                if (!askToAddAnotherItem()) break;
                continue;
            }

            // Prompt for quantity
            System.out.println();
            System.out.print("Enter quantity: ");
            int quantity = validator.validateIntegerInput();
    
            // Check stock availability
            if (medicine.getStockQuantity() < quantity) {
                Color.printInColor(" Not enough stock Available!, Remaining Stock: " + medicine.getStockQuantity(), Color.RED);
                if (!askToAddAnotherItem()) break;
            } 
            else {
                addToCart(medicine, quantity);
                itemCounter++; // Increment the counter for each successful addition
                
                System.out.printf(Color.GREEN + "Successfully added to your cart" + Color.RESET + ": " +
                    Color.YELLOW + "%s" + Color.RESET + " at " +
                    Color.YELLOW + "%.2f" + Color.RESET + " each * " +
                    Color.YELLOW + "%d" + Color.RESET + " = " +
                    Color.YELLOW + Color.BOLD + "Php %.2f" + Color.RESET + "%n",
                    medicine.getName(),
                    medicine.getPrice(),
                    quantity,
                    medicine.getPrice() * quantity);
    
                // Ask if the user wants to add another item
                if (!askToAddAnotherItem()) break;
            }
        }
    
        // Proceed to checkout
        System.out.println();
        System.out.print("Proceeding to checkout...........");
        ConsoleUtils.sleep();
    }
    
    // Method Handle payment
    private void handlePayment(double grandTotal, List<CartItem> cart) {
        while (true) {
            System.out.print(" Enter payment amount: ");
            double payment = validator.validateDoubleInput();

            if (payment >= grandTotal) { // check ko payment kung pasuk
                System.out.println(" Change: " + Color.YELLOW + String.format("%.2f", (payment - grandTotal))+ " Php" + Color.RESET);
                
                // Deduct stock from all items in the cart
                for (CartItem cartItem : cart) {
                    Medicine medicine = cartItem.getMedicine();
                    int quantity = cartItem.getQuantity();
                    medicineController.deductStock(medicine, quantity);  // Deduct stock for each cart item
                }

                System.out.println();
                TxtUtils.CenteredColoredText("--- Payment successful, Thank you! ---", totalWidth , Color.GREEN);
                TableUtils.printLine(64);
                
                //ask again to Dispense another Medicine for next customer
                System.out.print("Do you want Another Transaction? (" + Color.YELLOW + "yes" + Color.RESET + "/" + Color.YELLOW + "no" + Color.RESET + "): ");
                if(validator.yesOrNo()){
                    ConsoleUtils.clearConsole();
                    startDispensingProcess();
                }
                else {
                    ConsoleUtils.clearConsole();
                    clearCart(); // clear the cart upon exit

                    // Assuming 'user' is an instance of UserModel
                    if (UserModel.getCurrentRole() == UserModel.Role.PHARMACIST) {
                        pharmacistView.runPharmacistMenu(); // Back to pharmacist Menu
                    } else {
                        assistantView.assistantMenu(); // Back to assistant Menu
                    }
                    break;
                }
            } else {
                System.out.println();
                TxtUtils.CenteredColoredText("Insufficient payment. Please try again.", totalWidth , Color.RED);
                System.out.println();
            }
        }
    }
    
    // Method to add a medicine to the cart
    public void addToCart(Medicine medicine, int quantity) {
        cart.add(new CartItem(medicine, quantity));
    }

    // Method to compute the grand total of the cart
    public double computeGrandTotal() {
        return cart.stream()
                   .mapToDouble(CartItem::getTotalPrice) // Map each cart item to its total price as a double
                   .sum();  // Sum all the total prices to get the grand total
    }

    // Method to print the list of items in the cart with return boolean
    public boolean printCartSummary(String CustomerName) {
        
        if (cart.isEmpty()) {
            System.out.println();
            TxtUtils.CenteredColoredText("--- The cart is empty. ---", 64, Color.RED);
            TableUtils.printLine(64);
            System.out.print("Do you want to add another Transaction? "+ TxtUtils.printYesorNO() +": ");
                if (!validator.yesOrNo()) {
                    ConsoleUtils.clearConsole();
                    pharmacistView.runPharmacistMenu();
                }
                else{
                    startDispensingProcess();
                }
            return false; // Indicates the cart is empty

        } else {
            ConsoleUtils.clearConsole();
            TableUtils.doubleLine(totalWidth);
            TxtUtils.CenteredTitle(" --- Summary of Billing ---", totalWidth);
            TableUtils.doubleLine(totalWidth);
            System.out.println();
            System.out.println(" Customer : " + Color.YELLOW + CustomerName.toUpperCase() + Color.RESET);
            System.out.println(" Date     : " + Color.YELLOW + LocalDate.now() + Color.RESET);
            System.out.println(" Inv. No. : " + Color.YELLOW + transactionID + Color.RESET);
            System.out.println(" Cashier. : " + Color.YELLOW + UserModel.getCurrentUsername() + Color.RESET);
            System.out.println();
  
            int itemNumber = 1;
            for (CartItem cartItem : cart) {
                Medicine medicine = cartItem.getMedicine();
                int quantity = cartItem.getQuantity();
                double totalPrice = cartItem.getTotalPrice();
                System.out.printf(Color.YELLOW + "  %-3d" + Color.RESET + " %-13s    @   %d x %.2f    Php %.2f%n", 
                                itemNumber++, 
                                medicine.getName(), 
                                quantity, 
                                medicine.getPrice(), 
                                totalPrice);
            }
            System.out.println();
            TxtUtils.CenteredTitle("x------------------nothing follows----------------------x", totalWidth);
            System.out.println();
            return true; // Indicates the cart is not empty
        }
    }

    // Method to clear the cart
    public void clearCart() {
        cart.clear();
    }


    // Method to ask again
    private boolean askToAddAnotherItem() {
        System.out.println();
        System.out.print("Do you want to add another item? " + TxtUtils.printYesorNO() + ": ");
        return validator.yesOrNo();
    }
}
