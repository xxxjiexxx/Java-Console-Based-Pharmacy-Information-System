package controllers;
import models.*;
import utils.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import java.time.LocalDate;

public class CustomerController {
    public ValidationController validator = new ValidationController();
    private List<Customer> customers = new ArrayList<>();
    private final String filePath = "db/customers.txt";  // File path to store customer data
    private int nextId = 1;  // Variable to track the next available ID

    // Constructor to load customers from file
    public CustomerController() {
        loadCustomersFromFile();
    }

    // Load customers from a file
    private void loadCustomersFromFile() {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                // If the file does not exist, create an empty file
                Files.createFile(path);
                System.out.println("File 'customers.txt' did not exist. A new file has been created.");
            }

            // Read customers from the file
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] details = line.split(",");
                    int id = Integer.parseInt(details[0].trim());
                    String name = details[1].trim();
                    boolean withPrescription = Boolean.parseBoolean(details[2].trim());
                    LocalDate prescriptionValidity = details[3].trim().isEmpty() ? null : LocalDate.parse(details[3].trim());
                    LocalDate dateUpdated = LocalDate.parse(details[4].trim());
                    LocalDate dateCreated = LocalDate.parse(details[5].trim());
                   
                    Customer customer = new Customer(id, name, withPrescription, prescriptionValidity, dateUpdated, dateCreated);
                    customers.add(customer);
                    nextId = Math.max(nextId, id + 1);  // Update nextId
                }
            }
        } catch (IOException e) {
            TxtUtils.error("Error loading customers from file: " + e.getMessage());
        }
    }

    // Save customers to a file
    private void saveCustomersToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            for (Customer customer : customers) {
                writer.write(customer.getId() + "," 
                            + customer.getCustomerName() + "," 
                            + customer.isWithPrescription() + "," 
                            + customer.getPrescriptionValidity() + "," 
                            + customer.getDateUpdated() + "," 
                            + customer.getDateCreated()
                            );
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving customers to file: " + e.getMessage());
        }
    }

    // Get all customers
    public List<Customer> getCustomers() {
        return customers;
    }

    // View all customers
    public void viewCustomers() {
        ConsoleUtils.clearConsole();
        reloadCustomerFile();
        int totalWidth = 100;
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- List of Customers--- ", totalWidth);


        if (customers.isEmpty()) {
            System.out.println();
            TxtUtils.CenteredColoredText("--- No customers available. --- ", totalWidth, Color.RED);
            System.out.println();
        } 
        else {
            String[] headers = {"ID", "Customer Name", "Presciption?", "Validity", "Updated at", "Created at"};
            TableUtils.printTableHeader(totalWidth , headers, 4);
            for (Customer customer : customers) {
                TableUtils.printTableRow(totalWidth, customer.toStringArray(),4);
            }

            TableUtils.printLine(totalWidth);
            TxtUtils.CenteredTitle("*** Nothing Follows ***",totalWidth);
            System.out.println();

            askReturnMenu(totalWidth);
        }
        TableUtils.printLineDivider(totalWidth);
    }

    // Create a new customer void to in para retufn
    public Customer createCustomer( String name, boolean withPrescription, LocalDate prescriptionValidity) {
        Customer newCustomer = new Customer(nextId, name, withPrescription, prescriptionValidity, LocalDate.now(), LocalDate.now()); 
        customers.add(newCustomer); // Add to array list
        nextId++;  // Increment the next available ID
        saveCustomersToFile();  // Save changes to file
        System.out.println();
        TxtUtils.CenteredColoredText("--- Customer successfully created--- ",64, Color.GREEN);
        System.out.print(newCustomer);
        return newCustomer; 
    }
    
    // Create a walk-in customer
    public Customer createWalkinCustomer(boolean withPrescription){
        Customer walkinCustomer = new Customer(nextId,"Walk-in", withPrescription, LocalDate.now(), LocalDate.now(), LocalDate.now()); 
        return walkinCustomer;
    }
    
    // Read (Retrieve) a customer by ID
    public void readCustomer(int id) {
        Customer customer = findCustomerById(id);
        if (customer != null) {
            System.out.println("Customer Details: " + customer);
        } else {
           TxtUtils.error("Customer with ID " + id + " not found.");
        }
    }

    // Update an existing customer
    public void updateCustomer(int id, String newName, boolean withPrescription, LocalDate prescriptionValidity) {
        Customer customer = findCustomerById(id);
        customer.setCustomerName(newName);
        customer.setWithPrescription(withPrescription);
        customer.setPrescriptionValidity(prescriptionValidity);
        customer.setDateUpdated(LocalDate.now());
        saveCustomersToFile();  // Save changes to file
        
        TxtUtils.CenteredColoredText("--- Customer successfully updated ---", 64, Color.GREEN);
    }

    // Delete a customer by ID
    public void deleteCustomer(int id, int totalWidth) {

        Customer customer = findCustomerById(id); // search
        if (customer != null) {
            System.out.print(customer); // preview
            System.out.println();
            System.out.println();
            System.out.print("  Are you you want to delete this Customer? "+ TxtUtils.printYesorNO()+ ": ");
            if(validator.yesOrNo()){
                customers.remove(customer); // remove memory
                saveCustomersToFile();  // Save changes to file
                TxtUtils.CenteredColoredText("--- Customer successfully deleted! ---", 64, Color.GREEN);
                TableUtils.printLine(totalWidth);
                askReturnMenu(totalWidth);
            }
            else{
                ConsoleUtils.clearConsole();
                return;
            }
        } else {
            TxtUtils.CenteredColoredText("Customer with ID " + id + " not found.", 64, Color.RED);
            TableUtils.printLine(totalWidth);
            askReturnMenu(totalWidth);
        }
       
    }

    // Find a customer by ID, Do not edit
    public Customer findCustomerById(int id) {
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                return customer;
            }
        }
        return null;
    }

    // Search customer by name
    public Customer findCustomerByName(String name) {
        for (Customer customer : customers) {
            if (customer.getCustomerName().equalsIgnoreCase(name)) {
                return customer;
            }
        }
        return null;
    }

    // Search customer by either name or ID
    public Customer findCustomer(String input , int totalWidth) {
        Customer foundCustomer = null;

        // Try to parse the input as an ID and then Name
        try {
            foundCustomer = findCustomerById( Integer.parseInt(input) );   //  Parse the input to an integer and Search by ID
        } catch (NumberFormatException e) {
            foundCustomer = findCustomerByName(input); // If parsing fails, search by name
        }

        // Print and return the result
        if (foundCustomer != null) {
            TxtUtils.CenteredColoredText("--- Customer Record Found! ---", totalWidth, Color.GREEN);
            System.out.println(foundCustomer); // Print customer details
        } 
        return foundCustomer; // Return the found customer or null for checking lang
    }

    // Reload medicines from file
    private void reloadCustomerFile() {
        customers.clear();          // Clear the existing list
        loadCustomersFromFile();  // Reload medicines into the list
    }
    
    public void askReturnMenu(int totalWidth){
        totalWidth = (totalWidth <= 0) ? 64 : totalWidth;
        TxtUtils.CenteredInline("--- Press "+Color.YELLOW+"Enter"+ Color.RESET+" to return to Main Menu ---",totalWidth);
        validator.validateStringorEmptyInput();
        ConsoleUtils.clearConsole();
        return;
    }
    
    

}