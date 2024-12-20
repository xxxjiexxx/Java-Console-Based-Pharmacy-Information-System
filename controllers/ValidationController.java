package controllers;
import models.*;
import utils.*;
import java.util.*;
import views.*;
import java.io.Console;
import java.time.*;
import java.time.format.*;

public class ValidationController {

    // Instantiate scanner once
    private Scanner scanner = new Scanner(System.in); 

    // Generate Tranasaction ID base lang sa date time
    public String tid() { 
        // Current date and time 
        LocalDateTime currentDateTime = LocalDateTime.now(); 
        // Define the formatter 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yMMddHHmmss");
        // Format the current date and time 
        String formattedDateTime = currentDateTime.format(formatter); 
        // Generate the transaction ID
        String transactionID = formattedDateTime; 
        // Return the transaction ID
        return transactionID; 
    }

    // Validation method for integers
    public int validateIntegerInput() {
        while (true) {
            // Print the prompt for user input with color
            System.out.print(Color.BOLD + Color.CYAN);
            String input = scanner.nextLine();
            System.out.print(Color.RESET);
    
            try {
                // Try to parse the input as an integer
                int number = Integer.parseInt(input);
                
                // Check if the number is positive and greater than zero
                if (number > 0) {
                    return number; // Return if the number is valid
                } else {
                    TxtUtils.error("  Invalid input. Please enter a positive integer greater than zero.");
                    System.out.print("  Re-enter >>> ");
                }
            } catch (NumberFormatException e) {
                // Print an error message if the input is not a valid integer
                TxtUtils.error("  Invalid input. Please enter a valid integer.");
                System.out.print("  Re-enter >>> ");
            }
        }
    }
    
    // Fetch Existing Details for updae
    public int validateIntegerInput(int existingValue) {
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? existingValue : Integer.parseInt(input);
    }

    // Validation method for doubles (accepts both integers and decimals)
    public double validateDoubleInput() {
        while (true) {
            System.out.print(Color.BOLD + Color.CYAN);
            String input = scanner.nextLine().trim(); // Trim spaces and handle empty input
            System.out.print(Color.RESET);

            if (input.isEmpty()) {
                return 0.0; // Default to 0.0 for null input
            }

            try {
                return Double.parseDouble(input); // Return if successful
            } catch (NumberFormatException e) {
                TxtUtils.error("  Invalid input. Please enter a valid number.");
                System.out.print("  Re-enter >>> ");
            }
        }
    }

    // Validation method for doubles with parameter
    public double validateDoubleInput(double existingValue) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return existingValue;
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                TxtUtils.error("  Invalid input. Please enter a valid number.");
            }
        }
    }
    
    // Validation method for non-empty strings
    public String validateStringInput() {
        while (true) {
            System.out.print(Color.BOLD + Color.CYAN);
            String input = scanner.nextLine();
            System.out.print(Color.RESET);

            if (!input.trim().isEmpty()) {
                return input;  // Return if input is not empty
            } else {
                TxtUtils.error("  Invalid input. Please enter a non-empty string.");
                System.out.print("  Re-enter >>> ");
            }
        }
    }
    
    // For Existing Data 
    public String validateStringInput(String existingValue) {
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? existingValue : input;
    }

    // Validate String Accept Empthy for specific use only
    public String validateStringorEmptyInput() {
        String input = scanner.nextLine();
        return input;  // Return if input is not empty
    }

    // Validate Yes or No
    public boolean yesOrNo() {
        while (true) {
            System.out.print(Color.BOLD + Color.CYAN);
            String input = scanner.nextLine().trim().toLowerCase(); // Read and normalize input
            System.out.print(Color.RESET);

            if (input.equals("yes")) {
                return true; // Return true for "yes"
            } else if (input.equals("no")) {
                return false; // Return false for "no"
            } else {
                TxtUtils.error("  Invalid input. Please enter 'yes' or 'no'.");
                System.out.print("  Re-enter >>> ");
            }
        }
    }

    // Validation method for dates (in YYYY-MM-DD format)
    public LocalDate validateDateInput() {
        while (true) {
            System.out.print(Color.BOLD + Color.CYAN);
            String input = scanner.nextLine();
            System.out.print(Color.RESET);
            try {
                return LocalDate.parse(input, DateTimeFormatter.ISO_LOCAL_DATE);  // Return if successful
            } catch (DateTimeParseException e) {
                TxtUtils.error("  Invalid date format. Please enter the date in YYYY-MM-DD format.");
                System.out.print("  Re-enter >>> ");
            }
        }
    }

    // for existing date(Method with parameters)
    public LocalDate validateDateInput(LocalDate existingValue) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return existingValue;
            try {
                return LocalDate.parse(input, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                TxtUtils.error("  Invalid date format. Please enter the date in YYYY-MM-DD format.");
            }
        }
    }

    // Validate Date range (start and end) - Use for Filtering
    public boolean validateDateRangeInput(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            TxtUtils.error("Start date and end date cannot be null.");
            return false;
        }
    
        if (startDate.isAfter(endDate)) {
            TxtUtils.error("Start date cannot be after the end date. Please enter a valid date range.");
            return false;
        }
        return true; // Return true if the date range is valid
    }

    // validate Boolean Input (true or false)
    public boolean validateBooleanInput() { 
        while (true) { 
            System.out.print(Color.BOLD + Color.CYAN);
            String input = scanner.nextLine().trim().toLowerCase(); 
            System.out.print(Color.RESET);
            if (input.equals("true") || input.equals("false")) { 
                return Boolean.parseBoolean(input); 
            } else { 
                TxtUtils.error("  Invalid input. Please enter 'true' or 'false'."); 
                System.out.print("  Re-enter >>> ");
            } 
        } 
    }

    // validate Boolean Input (true or false), return existing data  
    public boolean validateBooleanInput(boolean existingValue ) { 
        
        String input = scanner.nextLine().trim();
        // Return the existing value if input is empty
        if (input.isEmpty()) {
            return existingValue;
        }
         // Check if the input is valid and parse it
         if (input.equalsIgnoreCase("true")) {
            return true;
        } else if (input.equalsIgnoreCase("false")) {
            return false;
        } else {
            TxtUtils.error("  Invalid input. Please enter 'true' or 'false'.");
            return validateBooleanInput(existingValue); // Recursive call for reattempt
        }
    }

    // Validate Medicine Category
    public String validateCategory() {
        Medicine.Category category = null;
        String categoryInput = "";

        while (category == null) {
            try {
                System.out.print("  Enter new Category (" + Color.YELLOW + "ANTIBIOTICS" + Color.RESET+ ", " + Color.YELLOW + "PAINKILLERS" + Color.RESET+ ", " + Color.YELLOW + "VITAMINS " + Color.RESET + "): ");
                System.out.print(Color.BOLD + Color.CYAN);
                categoryInput = scanner.nextLine().toUpperCase();  // Capture input
                System.out.println(Color.RESET);
                category = Medicine.Category.valueOf(categoryInput);  // Validate category
            } catch (IllegalArgumentException e) {
                Color.printInColor("  Invalid category. Please enter one of the following: ANTIBIOTICS, PAINKILLERS, VITAMINS.", Color.RED);
            }
        }
        return categoryInput;  // Return the validated category input
    }

    // Validate Category with Parameter for existing
    public String validateCategory(String category) {
        while (true) {
            System.out.print("(" + Color.YELLOW + "ANTIBIOTICS" + Color.RESET + ", " 
                            + Color.YELLOW + "PAINKILLERS" + Color.RESET + ", " 
                            + Color.YELLOW + "VITAMINS" + Color.RESET + "): ");
            String input = scanner.nextLine().trim().toUpperCase();
    
            // Check if input is empty, return default category
            if (input.isEmpty()) {
                return category; // Use the provided default category
            }
    
            try {
                Medicine.Category.valueOf(input);  // Validate if the category exists
                return input;  // Return the valid input
            } catch (IllegalArgumentException e) {
                TxtUtils.error("Invalid category. Please try again.");
            }
        }
    }
    
    // Logout centralized
    public void logout() {
        // Set the current user to null to destroy the session 
        UserModel.setCurrentUsername(null);
        System.out.println();
        TxtUtils.CenteredColoredText("Logging out... Redirecting to the login menu...", 64, Color.GREEN);
        ConsoleUtils.sleepAndClear(); // add delay effect
        LoginView app = new LoginView();
        app.showLoginMenu(); // Redirect to login screen
    }

    // Still recheck if need to use
    public void toReturn(){
        ConsoleUtils.clearConsole();
        return;
    }

    // Read Password
    public String readPassword(String prompt) { 
        Console console = System.console(); 
        if (console != null) { 
            char[] passwordArray = console.readPassword(prompt); 
            return new String(passwordArray); 
        } // Fallback for environments without Console support (e.g., IDEs) 
        
        return readPasswordFromFallback(prompt); 
    }

    // Method to Mask the password
    private String readPasswordFromFallback(String prompt) { 
        System.out.print(prompt); StringBuilder password = new StringBuilder(); 
        try { 
            while (true) { 
                char ch = (char) System.in.read(); 
                if (ch == '\n' || ch == '\r') {
                    break; 
                } 
                System.out.print('*'); password.append(ch); 
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
        
        System.out.println(); 
        return password.toString(); 
    }
  
}