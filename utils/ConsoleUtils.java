package utils;

/**
 * This class provides utility methods for manipulating the console output, such as moving the cursor, clearing lines, and clearing the entire console.
 */
public final class ConsoleUtils {

    /**
     * Moves the cursor up by 'n' lines in the console.
     * 
     * @param n The number of lines to move the cursor up.
     */
    public static void moveCursorUp(int n) {
        System.out.print("\033[" + n + "A");
    }

    /**
     * Moves the cursor down by 'n' lines in the console.
     * 
     * @param n The number of lines to move the cursor down.
     */
    public static void moveCursorDown(int n) {
        System.out.print("\033[" + n + "B");
    }

    /**
     * Moves the cursor to the beginning of the current line in the console.
     */
    public static void moveCursorToLineStart() {
        System.out.print("\033[0G");
    }

    /**
     * Clears the current line in the console.
     */
    public static void clearLine() {
        System.out.print("\033[2K");
    }

    /**
     * Clears 'n' lines starting from the current line up in the console.
     * 
     * @param n The number of lines to clear.
     */
    public static void clearMultipleLines(int n) {
        for (int i = 0; i < n; i++) {
            clearLine();
            if (i < n - 1) {
                moveCursorUp(1);
            }
        }
        // Move cursor back down to the initial position after clearing lines
        moveCursorDown(n - 1);
    }

    // Sleep and Clear
    public static void sleepAndClear(){
        sleep();
        clearConsole();
    }


    /**
     * Clears the entire console screen.
     */
    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            TxtUtils.error("  Error clearing console.");
        }
    }

    /**
     * Adds a sleep time effect to the program, pausing execution for 1500 milliseconds.
     */
    public static void sleep(){
        try {
            Thread.sleep(1500); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    

}
