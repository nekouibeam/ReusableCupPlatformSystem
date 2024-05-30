package consumerProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import databaseConnection.ConsumerDBConn;

/**
 * ConsumerOperationsFrame class represents the GUI frame for consumer operations.
 * It allows consumers to query the cups they are holding.
 */
public class ConsumerOperationsFrame extends JFrame {
    private ConsumerDBConn dbcConn;
    private JTextArea outputArea;

    /**
     * Constructs a ConsumerOperationsFrame with the specified database connection.
     *
     * @param dbcConn the database connection for consumer operations
     */
    public ConsumerOperationsFrame(ConsumerDBConn dbcConn) {
        super("Consumer Operations");
        this.dbcConn = dbcConn;

        // Create GUI components
        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);
        JButton queryCupsButton = new JButton("Query Cups");

        // Set up the layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(queryCupsButton);

        JPanel outputPanel = new JPanel();
        outputPanel.add(new JScrollPane(outputArea));

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(buttonPanel, BorderLayout.NORTH);
        container.add(outputPanel, BorderLayout.CENTER);

        // Add action listeners
        queryCupsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleQueryCups();
            }
        });

        // Set default behaviors
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // Center on screen
    }

    /**
     * Handles the action of querying cups held by the consumer.
     * Retrieves the information from the database and displays it in the output area.
     */
    private void handleQueryCups() {
        try {
            String result = dbcConn.queryCupsHolding();
            outputArea.setText(result);
        } catch (SQLException e) {
            showError("Query Error", e.getMessage());
        }
    }

    /**
     * Displays an error message dialog.
     *
     * @param title   the title of the error dialog
     * @param message the error message to display
     */
    private void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
