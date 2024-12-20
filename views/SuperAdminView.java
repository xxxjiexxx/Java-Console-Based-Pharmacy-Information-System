package views;
import models.*;
import utils.*;
import controllers.*;

public class SuperAdminView {

    private ValidationController validator = new ValidationController();
    private UserController controller = new UserController();
    public int totalWidth = 64;

    public void superAdminMenu() {
        while (true) {
            showSuperAdminMenuOptions();
            int choice = validator.validateIntegerInput();
            handleSuperAdminMenuChoice(choice);
        }
    }

    //Super Admin Menu
    private void showSuperAdminMenuOptions() {
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- SuperAdmin Menu ---", totalWidth);
        TableUtils.doubleLine(totalWidth);
        System.out.println("  Please select on the option:");
        System.out.println(Color.YELLOW + "  1" + Color.RESET + ". Create User");
        System.out.println(Color.YELLOW + "  2" + Color.RESET + ". View Users");
        System.out.println(Color.YELLOW + "  3" + Color.RESET + ". Update User");
        System.out.println(Color.YELLOW + "  4" + Color.RESET + ". Delete User");
        System.out.println(Color.YELLOW + "  5" + Color.RESET + ". Set Pharmacy Name");
        System.out.println(Color.YELLOW + "  6" + Color.RESET + ". Logout");
        System.out.println();
        System.out.print("  >>> ");
    }

    //SUper Admin Choice
    private void handleSuperAdminMenuChoice(int choice) {
        switch (choice) {
            case 1: createUser(); break;
            case 2: viewListofUsers(); break;
            case 3: updateUser(); break;
            case 4: deleteUser();;break;
            case 5: setPharmacyName(); break;
            case 6: validator.logout(); break;
            default:
                System.err.println();
                TxtUtils.CenteredColoredText("--- Invalid choice. Please try again. ---", 64, Color.RED);
                ConsoleUtils.sleep();
                ConsoleUtils.clearConsole();
                break;
        }
    }

    // Create User
    private void createUser() {
        ConsoleUtils.clearConsole();
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- Create User ---", totalWidth);
        TableUtils.doubleLine(totalWidth);
        System.out.println();
        System.out.print("  Enter new user " + Color.YELLOW + "username: " + Color.RESET);
        String username = validator.validateStringInput();
        System.out.print("  Enter new user " + Color.YELLOW + "password: " + Color.RESET);
        String password = validator.validateStringInput();
        System.out.print("  Enter user role (" + Color.YELLOW + "SUPERADMIN" + Color.RESET + ", " + Color.YELLOW + "PHARMACIST" + Color.RESET + ", " + Color.YELLOW + "ASSISTANT): " + Color.RESET);
        String roleInput = validator.validateStringInput().toUpperCase();
        try {
            UserModel.Role role = UserModel.Role.valueOf(roleInput);
            controller.createUser(username, password, role);
        } catch (IllegalArgumentException e) {
            TxtUtils.CenteredColoredText("---Invalid role. User creation failed. ---", totalWidth, Color.RED);
        }

        System.out.println();
        TableUtils.printLineDivider(totalWidth);
        askReturnMenu();
    }
    
    // View List of Users
    private void viewListofUsers(){
        ConsoleUtils.clearConsole();
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- List of all Registed Users ---", totalWidth);
        TableUtils.doubleLine(totalWidth);
        System.out.println();
        controller.viewUsers();
        TableUtils.printLineDivider(totalWidth);
        TxtUtils.CenteredTitle("--- Nothing follows ---", totalWidth);
        System.out.println();

        askReturnMenu();
    }

    // Update Users
    private void updateUser() {
        ConsoleUtils.clearConsole();
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- Update User ---", totalWidth);
        TableUtils.doubleLine(totalWidth);
        System.out.println();
        System.out.print("  Enter " + Color.YELLOW + "username/ID" + Color.RESET +": ");
        String checkInput = validator.validateStringInput();

        if(controller.searchIdOrUsername(checkInput)!=null){
            System.out.println(); 
            System.out.print("  Enter " + Color.YELLOW + "new username" + Color.RESET + ": ");
            String newUsername = validator.validateStringInput();
            System.out.print("  Enter " + Color.YELLOW + "new password" + Color.RESET+ ": ");
            String newPassword = validator.validateStringInput();
            System.out.print("  Enter user role (" + Color.YELLOW + "SUPERADMIN" + Color.RESET + ", " + Color.YELLOW + "PHARMACIST" + Color.RESET + ", " + Color.YELLOW + "ASSISTANT): " + Color.RESET);
            String newRoleInput = validator.validateStringInput().toUpperCase();
            try {
                UserModel.Role role = UserModel.Role.valueOf(newRoleInput);
                controller.updateUser(checkInput,newUsername, newPassword, role);
                System.out.println();
                TxtUtils.CenteredColoredText("---User updated successfully. ---", totalWidth, Color.GREEN);
            } catch (IllegalArgumentException e) {
                TxtUtils.CenteredColoredText("--- Invalid role. User update failed. ---", totalWidth, Color.RED);
            }
        }
        else{
            TxtUtils.CenteredColoredText("--- User not Found! ---", totalWidth, Color.RED);
        }

        System.out.println();
        TableUtils.printLineDivider(totalWidth);
        askReturnMenu();
    }

    // Delete User
    private void deleteUser() {
        ConsoleUtils.clearConsole();
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- Deletee User ---", totalWidth);
        TableUtils.doubleLine(totalWidth);
        System.out.println();
        System.out.print("  Enter the "+Color.YELLOW+"user ID"+Color.RESET+" to delete: ");
        int userId = validator.validateIntegerInput();  // Get user ID input
        controller.deleteUser(userId);
        System.out.println();
        TableUtils.printLineDivider(totalWidth);
        askReturnMenu();
    }

    // Set Pharmacy Name(soon whole pharmacy Info) 
    private void setPharmacyName(){
        ConsoleUtils.clearConsole();
        TableUtils.doubleLine(totalWidth);
        TxtUtils.CenteredTitle("--- Update User ---", totalWidth);
        TableUtils.doubleLine(totalWidth);
        System.out.println();

        System.out.print("  Enter the new "+Color.YELLOW+"pharmacy name"+Color.RESET+": ");
        String newPharmacyName = validator.validateStringInput();  // Validate and get the new name
        controller.setPharmacyName(newPharmacyName);
        TableUtils.printLineDivider(totalWidth);
        askReturnMenu();
            
    }
    
    // Press Enter to Exit
    public void askReturnMenu(){
        TxtUtils.CenteredInline("--- Press "+Color.YELLOW+"Enter"+ Color.RESET+" to return to Main Menu ---",totalWidth);
        validator.validateStringorEmptyInput();
        ConsoleUtils.clearConsole();
        return;
    }
    
}
