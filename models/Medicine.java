package models;
import java.time.LocalDate;
import utils.Color;

/**
 * Represents a medicine in the system, containing its details such as name, category, price, stock quantity, expiry date, manufacturer, and prescription requirement.
 */
public class Medicine {
    /**
     * Enum for categorizing medicines.
     */
    public enum Category {
        ANTIBIOTICS, PAINKILLERS, VITAMINS
    }

    private int id; // Unique identifier for the medicine
    private String name; // The name of the medicine
    private Category category; // The category of the medicine
    private double price; // The price of the medicine
    private int stockQuantity; // The quantity of the medicine in stock
    private LocalDate expiryDate; // The date the medicine expires
    private String manufacturer; // The manufacturer of the medicine
    private boolean needPresciption; // Indicates if a prescription is needed for the medicine

    /**
     * Constructor to initialize a new Medicine object.
     * 
     * @param id The unique identifier for the medicine.
     * @param name The name of the medicine.
     * @param category The category of the medicine.
     * @param price The price of the medicine.
     * @param stockQuantity The quantity of the medicine in stock.
     * @param expiryDate The date the medicine expires.
     * @param manufacturer The manufacturer of the medicine.
     * @param needPresciption Indicates if a prescription is needed for the medicine.
     */
    public Medicine(int id, String name, Category category, double price, int stockQuantity, LocalDate expiryDate, String manufacturer , boolean needPresciption) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.expiryDate = expiryDate;
        this.manufacturer = manufacturer;
        this.needPresciption = needPresciption;
    }

    // Getters for medicine properties
    public int getId() { return id; }
    public String getName() { return name; }
    public Category getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public String getManufacturer() { return manufacturer; }
    public boolean getNeedPrescription() { return needPresciption; }

    // Setter for Stock Quantity
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    // Static method to get the table header for medicine display
    public static String getTableHeader() {
        return String.format(
            
            "ID   Name          Category       Price   Stock   Expiry Date   Manufacturer   Prescription\n" +
            "-------------------------------------------------------------------------------------------"
        );
    }

    // This method overrides the default toString method to provide a custom string representation of the Medicine object.
    // It includes all the properties of the medicine, such as ID, name, category, price, stock quantity, expiry date, manufacturer, and prescription requirement.
    // The properties are formatted with colors to make them visually distinct.
    @Override
    public String toString() {
        return "ID: " + Color.YELLOW + id + Color.RESET +
               " | Name: " + Color.YELLOW + name + Color.RESET +
               " | Category: " + Color.YELLOW + category + Color.RESET +
               " | Price: " + Color.YELLOW + price + " Php" + Color.RESET +
               " | Stock: " + Color.YELLOW + stockQuantity + Color.RESET +
               " | Expiry: " + Color.YELLOW + expiryDate + Color.RESET +
               " | Manufacturer: " + Color.YELLOW + manufacturer + Color.RESET +
               " | Prescription: " + Color.YELLOW + needPresciption + Color.RESET;
    }

    // This method generates a static string that represents the footer of a table displaying medicine information.
    // It includes a horizontal rule to visually separate the footer from the rest of the table content.
    public static String getTableFooter() {
        return String.format(
            "-------------------------------------------------------------------------------------------\n" +
            "                             ------ nothing follows -----"
        );
    }
    
    // This method generates a multi-line string representation of the Medicine object, formatted for display in a table.
    // It includes all the properties of the medicine, formatted to fit within specific column widths to maintain table alignment.
    public String medMultipleLine() {
        return String.format(
            "ID: "+Color.YELLOW +"%-2d"+Color.RESET+" | Name: "+Color.YELLOW +"%-11.11s"+Color.RESET+" | Category: "+Color.YELLOW +"%-11.11s"+Color.RESET+" | Price: "+Color.YELLOW +"%.2f Php"+Color.RESET+" | Stock: "+Color.YELLOW +"%3d"+Color.RESET+" | Expiry: "+Color.YELLOW +"%-12.12s"+Color.RESET+" | Manufacturer: "+Color.YELLOW +"%-11.11s"+Color.RESET+" | Prescription: "+Color.YELLOW +"%-5s"+Color.RESET+"",
            id,                                   // ID (left-aligned, 4 characters)
            name.length() > 11 ? name.substring(0, 11) : name, // Name (left-aligned, 13 characters)
            category.toString().length() > 11 ? category.toString().substring(0, 11) : category.toString(), // Category (left-aligned, 12 characters)
            price,                                // Price (right-aligned, 8 characters, 2 decimals)
            stockQuantity,                        // Stock (right-aligned, 6 characters)
            expiryDate,                           // Expiry Date (left-aligned, 12 characters)
            manufacturer.length() > 13 ? manufacturer.substring(0, 13) : manufacturer, // Manufacturer (left-aligned, 15 characters)
            needPresciption                       // Prescription (left-aligned, 5 characters)
        );
    }

    // This method generates an array of strings, each representing a property of the Medicine object.
    // The properties included are ID, name, category, price, stock quantity, expiry date, manufacturer, and prescription requirement.
    public String[] toStringArray() {
        return new String[] {
            String.valueOf(id),
            name,
            category.toString(),
            String.valueOf(price),
            String.valueOf(stockQuantity),
            expiryDate.toString(),
            manufacturer,
            String.valueOf(needPresciption)
        };
    }
}
