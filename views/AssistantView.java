package views;
import utils.*;

public class AssistantView extends PharmacistView {

    int totalWidth = 64;

    public AssistantView() {
        super();  // Call the parent class's constructor
    }


    // Assitant Menu, Inhirit from Pharmacist
    public void assistantMenu() {
        
        while (true) {
            TableUtils.doubleLine(totalWidth);
            TxtUtils.CenteredTitle("--- Assistant Menu ---", totalWidth);
            TableUtils.doubleLine(totalWidth);
            System.out.println();
            System.out.println("  Plese choose on the followwing:");
            System.out.println(Color.YELLOW + "  1" + Color.RESET + ". Check Prescription");
            System.out.println(Color.YELLOW + "  2" + Color.RESET + ". Dispense Medicine");
            System.out.println(Color.YELLOW + "  3" + Color.RESET + ". View Reports");
            System.out.println(Color.YELLOW + "  4" + Color.RESET + ". View List of Medicines");
            System.out.println(Color.YELLOW + "  5" + Color.RESET + ". Search Medicine");
            System.out.println(Color.YELLOW + "  6" + Color.RESET + ". Check Expired Medicine");
            System.out.println(Color.YELLOW + "  7" + Color.RESET + ". Customer Management");
            System.out.println(Color.YELLOW + "  8" + Color.RESET + ". Logout");
            System.out.print("  >>> ");

            int choice = validator.validateIntegerInput();
            switch (choice) {
                case 1: checkPrescription(); break;
                case 2: dispenseMedicine(); break;
                case 3: viewReports(); break;
                case 4: controller.displayAllMedicinesConsole(); break;
                case 5: searchMedicine(); break;
                case 6: controller.checkExpiredMedicines(); break;
                case 7: customerManagement(); break;
                case 8: validator.logout(); break;
                default: TxtUtils.invalidChoice();
                         assistantMenu(); 
                         break;
            }
        }
    }
}
