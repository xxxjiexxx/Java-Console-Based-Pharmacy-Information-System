package views;
import models.*;
import utils.*;
import controllers.*;

public class LoginView {

    public UserController usersController = new UserController();
    public ValidationController validator = new ValidationController();
    public MedicineManagementView medview = new MedicineManagementView();
    
    public void showLoginMenu() {
        ConsoleUtils.clearConsole();
        int totalWidth = 64;
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("WELCOME TO " + usersController.getPharmacyName().toUpperCase(), totalWidth);
        TableUtils.doubleLine(totalWidth);
        System.out.println();
        System.out.println("  Please select an option:");
        System.out.println(Color.YELLOW + "  1" + Color.RESET + ". Login"); 
        System.out.println(Color.YELLOW + "  2" + Color.RESET + ". Search Medicine"); 
        System.out.println(Color.YELLOW + "  3" + Color.RESET + ". Exit");
        System.out.println();
        System.out.print("  >>> ");

        int choice = validator.validateIntegerInput();
        
        switch (choice) {
            case 1: login(); break;
            case 2: medview.searchMedicine(); break;
            case 3: UserModel.setCurrentUsername(null);  // Set the current user to null to destroy the session 
                    TxtUtils.CenteredColoredText("Exiting system..... ", totalWidth, Color.RED);
                    ConsoleUtils.sleepAndClear();
                    System.exit(0);
                    break;
            default: TxtUtils.invalidChoice(); 
                     showLoginMenu();
                     break;    
        }
    }

    // Login 
    private void login() {
        char key = 'J';                      // Encryption key (must be the same)
        int attempts = 0, attemptLimit = 5;  // Counter for attempts , Maximum number of allowed attempts
       
        while (true) { 
            System.out.println("");
            System.out.print("Enter " + Color.YELLOW + "username: " + Color.RESET);
            String username = validator.validateStringInput();
            String inputedPassword = validator.readPassword("Enter " + Color.YELLOW + "password: " + Color.RESET); 
            String encryptedpassword = usersController.encryptDecrypt(inputedPassword, key);
            boolean validCredentials = false;  // Set Default Credentials to false
    
            // Loop through users and check if credentials match
            for (UserModel user : usersController.getUsers()) {
                if (user.getUsername().equals(username) && user.getPassword().equals(encryptedpassword)) {
                    ConsoleUtils.clearConsole(); // clear my consoles if match
                    TxtUtils.CenteredColoredText("--- Login successful ---", 64, Color.GREEN);
                    System.out.print(" Hi " + Color.YELLOW + username.toUpperCase() + Color.RESET);
                    System.out.print("                                   ");
                    System.out.print(" Role: " + Color.YELLOW + user.getRole() + Color.RESET);
                    System.out.println();
                    UserModel.setCurrentUsername(username); // Set Session testing
                    UserModel.setCurrentRole(user.getRole());

                    validCredentials = true;  // Set true if credentials match
                    handleRole(user.getRole());  // Handle the role-specific menu
                    attempts = 0;   // Reset Login Attempt para maalis sa memory
                    break;  // Exit the loop if credentials are valid
                }
            }
            
            if (validCredentials) {
                return;  // Exit the login loop if valid credentials are found
            }
             
            attempts++; // Increment the attempts counter
            // Check if the attempt limit has been reached
            if (attempts >= attemptLimit) {
               TxtUtils.CenteredColoredText("--- Maximum login attempts reached. System Exiting... ---", 64, Color.RED);
               ConsoleUtils.sleepAndClear();
               System.exit(1); // Exit the program with an error code
            } else {
                TxtUtils.CenteredColoredText("--- Invalid credentials. Please try again. ---", 64, Color.RED);
                TxtUtils.CenteredColoredText("Remaining Login Attempts: " + Color.YELLOW + (attemptLimit - attempts), 64, Color.RED); 
                
            }
        }
    }

    // Check user by Role
    private void handleRole(UserModel.Role role) {
        switch (role) {
            case SUPERADMIN:
                SuperAdminView superadminMenu = new SuperAdminView();
                superadminMenu.superAdminMenu();
                break;
            case PHARMACIST:
                alert();
                PharmacistView pharmacistMenu = new PharmacistView();
                pharmacistMenu.runPharmacistMenu();
                break;
            case ASSISTANT:
                alert();
                AssistantView assistantMenu = new AssistantView();
                assistantMenu.assistantMenu();
                break;
            default:
                Color.printInColor("Role not recognized.", Color.RED);
                break;
        }
    }

    //Alert
    public void alert(){
            MedicineController checkExpired = new MedicineController();
            AlertController.showScrollableDialog("Medicines that need Attention!", checkExpired.alertCheckExpiredMedicines().toString());
    }
}

