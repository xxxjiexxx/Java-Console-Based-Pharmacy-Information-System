import views.LoginView;

/**
 * A final project required by Armada Logics.
 *  https://www.linkedin.com/company/armada-logics
 *  https://www.facebook.com/armadalogics
 *  https://www.armadalogics.com/
 * 
 * Created by Jeorgie C. Estrada, on December 19, 2024.
 * This is part of the Java Console Pharmacy Information System,
 * implementing the MVC pattern.
 */

public class PharmacyIS {
    public static void main(String[] args) {
        LoginView app = new LoginView();  // Use the default constructor
        app.showLoginMenu();
    }
}