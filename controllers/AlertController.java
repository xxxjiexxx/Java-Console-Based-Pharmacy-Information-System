package controllers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;

public class AlertController {

    /**
     * Displays a dynamic and styled dialog with monospaced content in a scrollable pane using JFrame.
     *
     * @param title   The title of the dialog.
     * @param content The content to display (must be preformatted for alignment).
     */
    public static void showScrollableDialog(String title, String content) {
        // Create a new JFrame
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Sample Logo
        // frame.add(new JLabel(new ImageIcon("utils/logo.jpg")));

        // Set custom frame properties
        frame.setUndecorated(false); // Keeps the default window controls
        frame.getContentPane().setBackground(new Color(230, 240, 250)); // Light blue background
        frame.setLayout(new BorderLayout());

        // Create a JTextArea for content
        JTextArea textArea = new JTextArea(content);
        textArea.setFont(new Font("Courier New", Font.PLAIN, 12)); // Monospaced font for alignment
        textArea.setEditable(false);
        textArea.setBackground(new Color(245, 245, 245)); // Light gray background
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding around text

        // Wrap the JTextArea in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 200, 220), 1)); // Border for scrollPane

        // Add a custom title label
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(50, 70, 100)); // Dark blue text
        titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add a close button
        JButton closeButton = new JButton("Close Preview");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 12));
        closeButton.setBackground(new Color(200, 220, 240)); // Light blue background
        closeButton.setForeground(Color.BLACK);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> frame.dispose());

        // Add a print button
        JButton printButton = new JButton("Print");
        printButton.setFont(new Font("Arial", Font.PLAIN, 12));
        printButton.setBackground(new Color(200, 220, 240)); // Light blue background
        printButton.setForeground(Color.BLACK);
        printButton.setFocusPainted(false);
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printComponent(textArea);
            }
        });

        // Create a button panel and add the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(230, 240, 250)); // Same as frame background
        // buttonPanel.add(closeButton);
        buttonPanel.add(printButton);

        // Add components to the frame
        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Final frame properties
        frame.pack();
        frame.setLocationRelativeTo(null); // Center on the screen
        frame.setResizable(false); // Disable resizing
        frame.setAlwaysOnTop(true); // Stay on top
        frame.setVisible(true); // Make the frame visible
    }

    /**
     * Prints the specified component.
     *
     * @param component The component to print.
     */
    public static void printComponent(JComponent component) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
                if (page > 0) {
                    return NO_SUCH_PAGE;
                }

                Graphics2D g2d = (Graphics2D) g;
                g2d.translate(pf.getImageableX(), pf.getImageableY());
                component.printAll(g);

                return PAGE_EXISTS;
            }
        });

        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }
}
