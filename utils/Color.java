package utils;

public final class Color {
    // Color codes for console output
    public static final String RESET = "\u001B[0m";     // Reset to default
    public static final String RED = "\u001B[31m";      // Error or failed
    public static final String GREEN = "\u001B[32m";    // Success or selection
    public static final String YELLOW = "\u001B[33m";   // Options
    public static final String BLUE = "\u001B[34m";     // Information
    public static final String CYAN = "\u001B[36m";     // Additional emphasis
    public static final String MAGENTA = "\u001B[35m";  // Highlighted text
    public static final String BOLD = "\u001B[1m";      // Bold
    public static final String UNDERLINE = "\u001B[4m"; // Underline Text
    // Prevent instantiation of the utility class
    private Color() {}

    // Utility method to print a message in the desired color
    public static void printInColor(String message, String colorCode) {
        System.out.println(colorCode + message + RESET);
    }

    // Overloaded method to print inline
    public static void printInColor(String message, String colorCode, boolean inline) {
        if (inline) {
            System.out.print(colorCode + message + RESET);
        } else {
            printInColor(message, colorCode);
        }
    }

    //Color.printWithStyle("This is bold magenta text.", Color.MAGENTA, Color.BOLD);
    public static void printWithStyle(String message, String colorCode, String styleCode) {
        System.out.println(styleCode + colorCode + message + RESET);
    }
}
