package models;
import utils.Color;

/**
 * Represents a user in the system, containing their details such as ID, username, password, and role.
 */
public class UserModel {
    /**
     * Enum for defining user roles within the system.
     */
    public enum Role {
        SUPERADMIN, PHARMACIST, ASSISTANT
    }

    private int id; // Unique identifier for the user
    private String username; // The username chosen by the user
    private String password; // The password associated with the user
    private Role role; // The role assigned to the user

    // Static field for username session, set to null by default
    private static String currentUsername = null;
    private static Role currentRole = null;

    /**
     * Constructor to initialize the user model with all fields.
     * 
     * @param id The unique identifier for the user.
     * @param username The username chosen by the user.
     * @param password The password associated with the user.
     * @param role The role assigned to the user.
     */
    public UserModel(int id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Default constructor 
    public UserModel() { 
        // Initialize fields with default values, if needed this.id = 0; 
        this.username = ""; 
        this.password = ""; 
        this.role = Role.ASSISTANT; // Default role or null if appropriate 
    }
    /**
     * Constructor to initialize the user model with id and username only.
     * 
     * @param id The unique identifier for the user.
     * @param username The username chosen by the user.
     */
    public UserModel(int id, String username) {
        this.id = id;
        this.username = username;
    }

    // Getter for the id
    public int getId() {
        return id;
    }

    // Setter for the id
    public void setId(int id) {
        this.id = id;
    }

    // Getter for the username
    public String getUsername() {
        return username;
    }

    // Setter for the username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for the password
    public String getPassword() {
        return password;
    }

    // Setter for the password
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter for the role
    public Role getRole() {
        return role;
    }

    // Setter for the role
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Static method to set the current username in the system.
     * 
     * @param username The username to set as the current username.
     */
    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    /**
     * Static method to get the current username in the system.
     * 
     * @return The current username.
     */
    public static String getCurrentUsername() {
        return currentUsername;
    }


    // Static method to set the current role in the system 
    public static void setCurrentRole(Role role) { 
        currentRole = role; 
    } 
    
    // Static method to get the current role in the system 
    public static Role getCurrentRole() { 
        return currentRole; 
    }

    /**
     * Provides a default string representation of the user.
     * 
     * @return A string representation of the user.
     */
    @Override 
    public String toString() {
        return String.format(
            " ID : %-3s | Username : %-15s | Role : %-15s", 
            Color.YELLOW + id + Color.RESET, 
            Color.YELLOW + username + Color.RESET, 
            Color.YELLOW + role + Color.RESET
        );
    }
}