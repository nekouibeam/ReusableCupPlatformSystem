package platformManagementProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import databaseConnection.PlatformDBConn;
import databaseConnection.SignupAndLoginExceptions.*;

/**
 * This class represents the main frame for platform management, providing 
 * various functionalities such as managing cups, consumers, shops, and transactions.
 */
public class PlatformManagementFrame extends JFrame {

    private PlatformDBConn dbcConn;

    private final int frame_width = 600, frame_height = 400;
    private JButton cupButton, consumerButton, shopButton, transRecordButton, addCupButton, shopSignUpButton,
            lendCupButton, receiveCupButton, queryCupsHoldingButton;
    private JTextArea textArea;
    private JPanel buttonPanel;
    private JScrollPane scrollPane;

    /**
     * Constructs a new PlatformManagementFrame and initializes the database connection and UI components.
     */
    public PlatformManagementFrame() {
        super("PlatformManagementFrame");

        try {
            dbcConn = new PlatformDBConn();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        consumerButton = new JButton("Consumer");
        shopButton = new JButton("Shop");
        transRecordButton = new JButton("TransRecord");
        cupButton = new JButton("Cup");
        addCupButton = new JButton("addCup");
        shopSignUpButton = new JButton("shopSignUp");
        lendCupButton = new JButton("lendCup");
        receiveCupButton = new JButton("receiveCup");
        queryCupsHoldingButton = new JButton("CupsHolding");

        textArea = new JTextArea();
        textArea.setEditable(false); // Make textArea read-only

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 3));
        buttonPanel.add(consumerButton);
        buttonPanel.add(shopButton);
        buttonPanel.add(transRecordButton);
        buttonPanel.add(cupButton);
        buttonPanel.add(addCupButton);
        buttonPanel.add(shopSignUpButton);
        buttonPanel.add(lendCupButton);
        buttonPanel.add(receiveCupButton);
        buttonPanel.add(queryCupsHoldingButton);

        scrollPane = new JScrollPane(textArea);

        setLayout(new GridLayout(2, 1));
        add(buttonPanel);
        add(scrollPane);

        setSize(frame_width, frame_height); // Set size after adding components
        addButtonsFunction();
        setLocationRelativeTo(null);
    }

    /**
     * Adds functionality to the buttons by associating them with corresponding database operations.
     */
    public void addButtonsFunction() {
        consumerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    textArea.setText(dbcConn.consumerInfo());
                } catch (SQLException ee) {
                    ee.printStackTrace();
                    textArea.setText("error: " + ee.getMessage());
                }
            }
        });

        shopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    textArea.setText(dbcConn.shopInfo());
                } catch (SQLException ee) {
                    ee.printStackTrace();
                    textArea.setText("error: " + ee.getMessage());
                }
            }
        });

        transRecordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    textArea.setText(dbcConn.transInfo());
                } catch (SQLException ee) {
                    ee.printStackTrace();
                    textArea.setText("error: " + ee.getMessage());
                }
            }
        });

        cupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    textArea.setText(dbcConn.cupInfo());
                } catch (SQLException ee) {
                    ee.printStackTrace();
                    textArea.setText("error: " + ee.getMessage());
                }
            }
        });
        
        addCupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
                String[] types = {"hot", "cold"};
                String[] sizes = {"large", "small"};

                JComboBox<String> typeComboBox = new JComboBox<>(types);
                int typeOption = JOptionPane.showConfirmDialog(
                    null,
                    typeComboBox,
                    "Please select cup type:",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );

                if (typeOption != JOptionPane.OK_OPTION) {
                    return;
                }
                String type = (String) typeComboBox.getSelectedItem();

                JComboBox<String> sizeComboBox = new JComboBox<>(sizes);
                int sizeOption = JOptionPane.showConfirmDialog(
                    null,
                    sizeComboBox,
                    "Please select cup size:",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );

                if (sizeOption != JOptionPane.OK_OPTION) {
                    return;
                }
                String size = (String) sizeComboBox.getSelectedItem();

                try {
                    dbcConn.addCup(type, size);
                    JOptionPane.showMessageDialog(null, "Cup added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    textArea.setText("error: " + ex.getMessage());
                }
            }
        });

        shopSignUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ID = JOptionPane.showInputDialog("Please enter shop ID:");
                if (ID == null ) {
                    return;
                }else if(ID.trim().isEmpty()) {
                	JOptionPane.showMessageDialog(null, "Input cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String name = JOptionPane.showInputDialog("Please enter shop name:");
                if (name == null ) {
                    return;
                }else if( name.trim().isEmpty()) {
                	JOptionPane.showMessageDialog(null, "Input cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String activatePassword = JOptionPane.showInputDialog("Please enter password:");
                if (activatePassword == null ) {
                    return;
                }else if(activatePassword.trim().isEmpty()) {
                	JOptionPane.showMessageDialog(null, "Input cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    dbcConn.shopSignUp(ID, name, activatePassword);
                    JOptionPane.showMessageDialog(null, "Shop signed up successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    textArea.setText("error: " + ex.getMessage());
                } catch (IdAlreadyUsedException ee) {
                    ee.printStackTrace();
                    textArea.setText("error: " + ee.getMessage());
                }
            }
        });

        lendCupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cupIDInput = JOptionPane.showInputDialog("Please enter cup ID:");
                if (cupIDInput == null) {
                    return;
                }else if(cupIDInput.trim().isEmpty()) {
                	JOptionPane.showMessageDialog(null, "Input cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int cupID;
                try {
                    cupID = Integer.parseInt(cupIDInput);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid cup ID", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String shopID = JOptionPane.showInputDialog("Please enter shop ID:");
                if (shopID == null) {
                    return;
                }else if(shopID.trim().isEmpty()) {
                	JOptionPane.showMessageDialog(null, "Input cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // 使用 signUpIDCheck 方法檢查 shopID 是否存在
                    if (!dbcConn.signUpIDCheck(shopID)) {
                        JOptionPane.showMessageDialog(null, "Shop ID does not exist", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    dbcConn.lendCup(cupID, shopID);
                    JOptionPane.showMessageDialog(null, "Cup lent successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    textArea.setText("error: " + ex.getMessage());
                }
            }
        });

        receiveCupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cupIDInput = JOptionPane.showInputDialog("Please enter cup ID:");
                if (cupIDInput == null) {
                    return;
                }else if(cupIDInput.trim().isEmpty()) {
                	JOptionPane.showMessageDialog(null, "Input cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int cupID;
                try {
                    cupID = Integer.parseInt(cupIDInput);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid cup ID", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    dbcConn.receiveCup(cupID);
                    JOptionPane.showMessageDialog(null, "Cup received successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    textArea.setText("error: " + ex.getMessage());
                }
            }
        });

        queryCupsHoldingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    textArea.setText(dbcConn.queryCupsHolding());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    textArea.setText("error: " + ex.getMessage());
                }
            }
        });
    }
}
