package utils;
import java.text.DecimalFormat;
import controllers.ValidationController;

public class TxtUtils {

    ValidationController validator = new ValidationController();
    
    // Print Whole Text as Green
    public static void success(String message) {
        Color.printInColor(message,Color.GREEN);
    }
    
    // Print Whole Text as Red
    public static void error(String message) {
        Color.printInColor(message,Color.RED);
    }
    // skip the color strings when displaying the output in a JFrame
    public static String stripColorCodes(String input) {
            return input.replaceAll("\\x1B\\[[;\\d]*m", "");
    }

    public static String money(double amount) { 
        DecimalFormat formatter = new DecimalFormat("#,##0.00"); 
        return formatter.format(amount); 
    }

    public static void CenteredTitle(String text, int width) {
        int padding = (width - text.length()) / 2;
        String paddedText = String.format("%" + padding + "s%s%" + padding + "s", "", text, "");

        if (paddedText.length() < width) {
            paddedText += " ";
        }
        System.out.println(paddedText);
    }

    public static void CenteredInline(String text, int width) {
        int padding = (width - text.length()) / 2;
        String paddedText = String.format("%" + padding + "s%s%" + padding + "s", "", text, "");

        if (paddedText.length() < width) {
            paddedText += " ";
        }
        System.out.print(paddedText);
    }
    public static void CenteredColoredText(String text, int width, String color) {
        int padding = (width - text.length()) / 2;
        String paddedText = String.format("%" + padding + "s%s%" + padding + "s", "", text, "");

        if (paddedText.length() < width) {
            paddedText += " ";
        }
        System.out.println(color + paddedText + Color.RESET);
    }


    // INvalid Choice
    public static void invalidChoice(){
        CenteredColoredText("Invalid choice. Please try again.", 64, Color.RED);
        ConsoleUtils.sleepAndClear();
    }

    public static String printYesorNO() {
        String output = "("+ Color.YELLOW + "yes" + Color.RESET + "/" + Color.YELLOW + "no" + Color.RESET+")";
        return output;
    }
    public static String printIdName() {
        String output = Color.YELLOW + "ID" + Color.RESET + "/" + Color.YELLOW + "Name" + Color.RESET;
        return output;
    }
}
