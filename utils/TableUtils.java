package utils;

public class TableUtils {

    public static void printTableHeader(int totalWidth, String[] headers, int firstColumnWidth) {
        String format = createTableFormat(totalWidth, headers.length ,firstColumnWidth);
        printLineDivider(totalWidth);
        System.out.printf(format, (Object[]) headers);
        printLineDivider(totalWidth);
    }

    public static void printTableRow(int totalWidth, String[] rowContent, int firstColumnWidth) {
        String format = createTableFormat(totalWidth, rowContent.length, firstColumnWidth);
        String coloredFirstColumn = Color.YELLOW + format.split(" ")[0] + Color.RESET + " ";
        String restOfFormat = format.substring(format.indexOf(" ") + 1);
        System.out.printf(coloredFirstColumn + restOfFormat, (Object[]) rowContent);
    }

    
    private static String createTableFormat(int totalWidth, int columnCount, int firstColumnWidth) {
        int remainingWidth = totalWidth - firstColumnWidth;
        int otherColumnCount = columnCount - 1;
        int otherColumnWidth = remainingWidth / otherColumnCount;

        StringBuilder formatBuilder = new StringBuilder();
        formatBuilder.append("%-").append(firstColumnWidth).append("s ");   // Left-align the first column
        for (int i = 1; i < columnCount; i++) {
            formatBuilder.append("%-").append(otherColumnWidth).append("s "); // Left-align the other columns
        }
        formatBuilder.append("%n");

        return formatBuilder.toString();
    }

    public static void printTableRowAdjustable(int totalWidth, String[] rowContent, int[] columnWidths) {
        StringBuilder formatBuilder = new StringBuilder();
        for (int width : columnWidths) {
            formatBuilder.append("%-").append(width).append("s ");
        }
        formatBuilder.append("%n");
    
        String format = formatBuilder.toString();
        System.out.printf(format, (Object[]) rowContent);
    }


    public static void printLineDivider(int totalWidth) {
        System.out.println("-".repeat(totalWidth));
    }

    public static void doubleLine(int totalWidth) {
        if (totalWidth < 0) {
            throw new IllegalArgumentException("Length must be non-negative");
        }
        System.out.println("=".repeat(totalWidth));
    }

    // with Extra Space before and after
    public static void printLine(int totalWidth) {
        System.out.println();
        System.out.println("-".repeat(totalWidth));
        System.out.println();
    }

}
