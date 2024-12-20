package controllers;
import models.*;
import utils.*;

import java.io.*;
import java.util.*;

public class UserController {
    private List<UserModel> users = new ArrayList<>();
    private String pharmacyName = "Pharmacy Information System";  // Default name
    private static final String PHARMACY_NAME_FILE = "db/pharmacy_name.txt";  // File to store pharmacy name
    private static final String USERS_FILE = "db/users.txt";  // File to store USERS
    private int idCounter;  // ID counter, starting from the highest ID
   
    public ValidationController validator = new ValidationController();


    public static void main(String[] args) {
       
    }

    public UserController() {
        loadPharmacyName();
        loadAllUsers();
        if (!users.isEmpty()) {
            idCounter = users.get(users.size() - 1).getId() + 1;
        }
    }

    // Method to return the list of users
    public List<UserModel> getUsers() {
        return users;
    }

    // All users
    public void viewUsers() {
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            for (UserModel user : users) {
                System.out.println(user);
            }
        }
    }

    // Create User with auto-incremented ID
    public void createUser(String username, String password, UserModel.Role role) {
        
        char key = 'J';         // Simple key for XOR operation
        String encryptedPassword= encryptDecrypt(password, key); // Encrypt the Password
        UserModel newUser = new UserModel(idCounter, username, encryptedPassword, role);
        
        users.add(newUser);     // Add to the in-memory list
        addUser(newUser);       // Add user to File
        idCounter++;            // Increment the ID for the next user
        System.out.println();
        TxtUtils.CenteredColoredText("--- User created successfully. ---", 64, Color.GREEN);
    }

    // Update User
    public void updateUser(String query,String username, String newPassword, UserModel.Role newRole) {

        for (UserModel user : users) {
            if (user.getUsername().equals(query) || user.getId() == Integer.parseInt(query)) {
               // Update the password and role
               char key = 'J';
               user.setUsername(username);
               user.setPassword(encryptDecrypt(newPassword, key));
               user.setRole(newRole);
               saveAllUsers();     // Save updated user list to the file
               return;
            }
        }
    }

    //Helper/Checker
    public UserModel searchIdOrUsername(String query) {
        // for (UserModel user : users) {
        //     if (user.getUsername().equals(query) || user.getId() == Integer.parseInt(query)) {
        //         System.out.println(user);
        //         return user;
        //     }
        // }

        for (UserModel user : users) {
            if (user.getUsername().equals(query)) {
                System.out.println(user);
                return user;
            } 
            
            try {
                int id = Integer.parseInt(query);
                if (user.getId() == id) {
                    System.out.println(user);
                    return user;
                }
            } catch (NumberFormatException e) {
                // Skip to the next iteration if the query is not a valid integer
            }
        }
        return null;
    }

    // Delete User
    public void deleteUser(int userId) {
        UserModel userToDelete = null;
    
        // Find the user by ID
        for (UserModel user : users) {
            if (user.getId() == userId) {
                userToDelete = user;
                break;
            }
        }

        // If user is found, print their info and remove them
        if (userToDelete != null) {
            System.out.println(userToDelete); // display the user to delete
            System.err.println();
            System.out.print("  Are you sure you want to delete this user (" + TxtUtils.printYesorNO() + "): ");
            if(validator.yesOrNo()){  // Yes or No
                users.removeIf(user -> user.getId() == userId); // Remove User
                saveAllUsers(); // Save the updated list of users to the file
                TxtUtils.CenteredColoredText(" --- User deleted successfully. ---", 64, Color.GREEN);
            }
            else{
                TxtUtils.CenteredColoredText(" ---Transaction Cancelled ---", 64, Color.RED);
            }
                
        } else {
            TxtUtils.CenteredColoredText(" ---User with ID " + userId + " not found. ---", 64, Color.RED);
        }
    }

    // Set the pharmacy name from user input
    public void setPharmacyName(String newPharmacyName) {
        this.pharmacyName = newPharmacyName;  // Update the pharmacy name
        savePharmacyName();  // Save the updated pharmacy name to a file
        System.out.println();
        System.out.println("  Pharmacy name set to: " + Color.GREEN + pharmacyName + Color.RESET);
    }

    // Get the pharmacy name
    public String getPharmacyName() {
        return pharmacyName;
    }

    // Save the pharmacy name to a file
    private void savePharmacyName() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PHARMACY_NAME_FILE))) {
            writer.write(pharmacyName);
        } catch (IOException e) {
            Color.printInColor("Error saving pharmacy name to file.", Color.RED);
        }
    }

    // Load the pharmacy name from the file
    private void loadPharmacyName() {
        File file = new File(PHARMACY_NAME_FILE);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    pharmacyName = reader.readLine();
                } catch (IOException e) {
                    Color.printInColor("Error loading pharmacy name from file.", Color.RED);
                }
            }
    }

    // Save all users to the file (overwrite the existing file)
    public void saveAllUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (UserModel user : users) {
                // Write each user in CSV format: id,username,password,role
                writer.write(user.getId() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getRole());
                writer.newLine();
            }
        } catch (IOException e) {
            Color.printInColor("Error saving users to file.", Color.RED);
        }
    }

    // Save a single user to the file (append mode)
    private void addUser(UserModel user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            // Write the user in CSV format: id,username,password,role
            writer.newLine();
            writer.write(user.getId() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getRole());
        } catch (IOException e) {
            Color.printInColor("Error saving user to file.", Color.RED);
        }
    }
   
    // Load all users from file
    private void loadAllUsers() {
        File file = new File(USERS_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(",");
                    if (fields.length >= 4) {
                        int id = Integer.parseInt(fields[0]);
                        String username = fields[1];
                        String password = fields[2];
                        UserModel.Role role = UserModel.Role.valueOf(fields[3]);
                        users.add(new UserModel(id, username, password, role));
                    }
                }
            } catch (IOException e) {
                Color.printInColor("Error loading users from file.", Color.RED);
            } catch (Exception e) {
                Color.printInColor("Error parsing user data from file.", Color.RED);
            }
        }
    }

    // Encrypt or decrypt a given string with a specified key
    public String encryptDecrypt(String input, char key) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            result.append((char) (c ^ key));
        }
        return result.toString();
    }
}
