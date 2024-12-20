package models;
import java.time.LocalDate;
import utils.Color;

/**
 * Represents a customer in the system, containing their details and prescription information.
 */
public class Customer {

    private int id; // Unique identifier for the customer
    private String customerName; // The customer's name
    private boolean withPrescription; // Indicates if the customer has a prescription
    private LocalDate prescriptionValidity; // The date until which the prescription is valid
    private LocalDate dateCreated; // The date the customer's record was created
    private LocalDate dateUpdated; // The date the customer's record was last updated

    private static Customer currentCustomer; // Static variable to hold the current customer session

    /**
     * Constructor to initialize a new Customer object.
     * 
     * @param id The customer's unique identifier.
     * @param customerName The customer's name.
     * @param withPrescription Indicates if the customer has a prescription.
     * @param prescriptionValidity The date until which the prescription is valid.
     * @param dateUpdated The date the customer's record was last updated.
     * @param dateCreated The date the customer's record was created.
     */
    public Customer(int id, String customerName, boolean withPrescription, LocalDate prescriptionValidity, LocalDate dateUpdated, LocalDate dateCreated) {
        this.id = id; 
        this.customerName = customerName; 
        this.withPrescription = withPrescription; 
        this.prescriptionValidity = prescriptionValidity;
        this.dateCreated = dateCreated; 
        this.dateUpdated = dateUpdated; 
    }

    // Getter for ID
    public int getId() {
        return id;
    }

    // Setter for ID (optional if needed)
    public void setId(int id) {
        this.id = id;
    }

    // Getter for customerName
    public String getCustomerName() {
        return customerName;
    }

    // Setter for customerName
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    // Setter for dateCreated
    public void setDateCreated(LocalDate dateCreated) { 
        this.dateCreated = dateCreated; 
    }

    // getter for dateCreated
    public LocalDate getDateCreated() { 
        return dateCreated; 
    }

    // Getter for dateUpdated 
    public LocalDate getDateUpdated() { 
        return dateUpdated; 
    }
    // Setter for dateUpdated 
    public void setDateUpdated(LocalDate dateUpdated) { 
        this.dateUpdated = dateUpdated; 
    }

    // Getter for withPrescription 
    public boolean isWithPrescription() { 
        return withPrescription; 
    }

    // Setter for withPrescription
    public void setWithPrescription(boolean withPrescription) { 
        this.withPrescription = withPrescription; 
    }

    // Getter for prescriptionValidity 
    public LocalDate getPrescriptionValidity() { 
        return prescriptionValidity; 
    }

    // Setter for prescriptionValidity
    public void setPrescriptionValidity(LocalDate prescriptionValidity) { 
    this.prescriptionValidity = prescriptionValidity; 
    }

    /**
     * Sets the current customer in the system.
     * 
     * @param customer The customer to set as the current customer.
     */
    public static void setCurrentCustomer(Customer customer) {
        currentCustomer = customer;
    }

    /**
     * Returns the current customer in the system.
     * 
     * @return The current customer.
     */
    public static Customer getCurrentCustomer() {
        return currentCustomer; // Return the current customer
    }
    
    /**
     * Provides a default string representation of the customer.
     * 
     * @return A string representation of the customer.
     */
    @Override 
    public String toString() { 
        return String.format( "ID: %-3s | Customer Name: %-15s | With Prescription: %-5s | Prescription Validity: %-10s | Date Updated: %-10s | Date Created: %-10s", 
        Color.YELLOW + id + Color.RESET, 
        Color.YELLOW + customerName + Color.RESET, 
        Color.YELLOW + withPrescription + Color.RESET, 
        Color.YELLOW + prescriptionValidity + Color.RESET, 
        Color.YELLOW + dateUpdated + Color.RESET, 
        Color.YELLOW + dateCreated + Color.RESET
        ); 
    }

    /**
     * Converts the customer's details into an array of strings.
     * 
     * @return An array of strings representing the customer's details.
     */
    public String[] toStringArray() {
        return new String[] {
            String.valueOf(id),
            customerName,
            String.valueOf(withPrescription),
            prescriptionValidity != null ? prescriptionValidity.toString() : "",
            dateUpdated.toString(),
            dateCreated.toString()
        };
    }
}
