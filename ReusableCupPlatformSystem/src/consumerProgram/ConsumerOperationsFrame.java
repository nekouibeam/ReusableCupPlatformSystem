package consumerProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import databaseConnection.ConsumerDBConn;

public class ConsumerOperationsFrame extends JFrame {
    private ConsumerDBConn dbcConn;
    private JTextArea outputArea;

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

    private void handleQueryCups() {
        try {
            String result = dbcConn.queryCupsHolding();
            outputArea.setText(result);
        } catch (SQLException e) {
            showError("Query Error", e.getMessage());
        }
    }

    private void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
}

