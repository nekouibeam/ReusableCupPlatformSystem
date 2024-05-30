package shopProgram;

import databaseConnection.ShopDBConn;
import databaseConnection.SignupAndLoginExceptions.*;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.*;

/**
 * This class represents the frame for shop operations, allowing shops to rent and return cups,
 * check currently held cups, change password, and logout.
 */
public class ShopFrame extends JFrame {
    private ShopDBConn shopConn;
    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 400;
    private static final int FIELD_WIDTH = 5;
    private JPanel actionPanel, cupIDPanel, customerIDPanel, operatePanel1, operatePanel2, operatePanel3, toolPanel, overallPanel;
    private JTextArea infoArea;
    private JLabel actionLabel, cupIDLabel, customerIDLabel;
    private JComboBox<String> actionCombo;
    private JTextField cupIDField, customerIDField;
    private JButton submitButton, checkHeldCupButton, changePasswordButton, logoutButton;

    /**
     * Constructs a new ShopFrame with the given ShopDBConn and initializes the UI components.
     *
     * @param shopConn the database connection for the shop operations
     */
    public ShopFrame(ShopDBConn shopConn) {
        this.shopConn = shopConn;
        this.setTitle("Store");
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        createItemComp();
        createButton();
        createInfoArea();
        createPanel();
        setLocationRelativeTo(null);
    }

    /**
     * Creates and initializes the item components (labels, combo boxes, text fields).
     */
    public void createItemComp() {
        actionLabel = new JLabel("Action:");
        actionCombo = new JComboBox<>();
        actionCombo.addItem("rent");
        actionCombo.addItem("return");

        cupIDLabel = new JLabel("Cup ID:");
        customerIDLabel = new JLabel("User ID:");

        cupIDField = new JTextField(FIELD_WIDTH);
        customerIDField = new JTextField(FIELD_WIDTH);
    }

    /**
     * Creates and initializes the buttons with their corresponding action listeners.
     */
    public void createButton() {
        submitButton = new JButton("Submit");
        checkHeldCupButton = new JButton("Cup Currently Held");
        changePasswordButton = new JButton("Change Password");
        logoutButton = new JButton("Logout");

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String op = (String) actionCombo.getSelectedItem();
                infoArea.setText("");
                if (op.equals("rent")) {
                    try {
                        shopConn.lendCup(Integer.parseInt(cupIDField.getText()), customerIDField.getText());
                        infoArea.setText("Action completed!");
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (AccountNotExistException e) {
                        JOptionPane.showMessageDialog(null, "Consumer Account Does Not Exist", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (op.equals("return")) {
                    try {
                        shopConn.receiveCup(Integer.parseInt(cupIDField.getText()));
                        infoArea.setText("Action completed!");
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        checkHeldCupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    infoArea.setText(shopConn.queryCupsHolding());
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        changePasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String userID = shopConn.getShopID();

				String oldPassword = JOptionPane.showInputDialog(null, "Enter Old Password:");
				if (oldPassword == null) {
				    return;
				} else {
				    try {
				        shopConn.login(userID, oldPassword);
				    } catch (PasswordWrongException e) {
				        JOptionPane.showMessageDialog(null, "Old Password is incorrect", "Error", JOptionPane.ERROR_MESSAGE);
				        return;
				    } catch (SQLException | AccountNotExistException | NotActivateException | IdCantEmptyException e) {
				        JOptionPane.showMessageDialog(null, "Error during password verification: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				        return;
				    } catch (PasswordCantEmptyException e) {
				        JOptionPane.showMessageDialog(null, "Old Password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
				        return;
				    }
				}

				String newPassword = JOptionPane.showInputDialog(null, "Enter New Password:");
				if (newPassword == null) {
				    return;
				} else if (newPassword.isEmpty()) {
				    JOptionPane.showMessageDialog(null, "New Password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
				    try {
				        shopConn.changePassword(userID, newPassword);
				        JOptionPane.showMessageDialog(null, "Password changed successfully");
				    } catch (SQLException e) {
				        JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				    } catch (PasswordAlreadyUsedException e) {
				        JOptionPane.showMessageDialog(null, "Password already used", "Error", JOptionPane.ERROR_MESSAGE);
				    }
				}
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    openShopOperationsFrame(shopConn);
                }
            }
        });
    }

    /**
     * Opens the ShopOperationsFrame and closes the current frame.
     *
     * @param shopConn the database connection for the shop operations
     */
    public void openShopOperationsFrame(ShopDBConn shopConn) {
        ShopOperationsFrame shOpFrame = new ShopOperationsFrame(shopConn);
        shOpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        shOpFrame.setVisible(true);
        this.dispose();
    }

    /**
     * Creates and initializes the info area where output messages are displayed.
     */
    public void createInfoArea() {
        infoArea = new JTextArea(20, 30);
        infoArea.setEditable(false);
    }

    /**
     * Creates and sets up the panel layout for the frame.
     */
    public void createPanel() {
        actionPanel = new JPanel();
        actionPanel.add(actionLabel);
        actionPanel.add(actionCombo);

        cupIDPanel = new JPanel();
        cupIDPanel.add(cupIDLabel);
        cupIDPanel.add(cupIDField);

        customerIDPanel = new JPanel();
        customerIDPanel.add(customerIDLabel);
        customerIDPanel.add(customerIDField);

        operatePanel1 = new JPanel(new GridLayout(1, 3));
        operatePanel1.add(actionPanel);
        operatePanel1.add(cupIDPanel);
        operatePanel1.add(customerIDPanel);

        operatePanel2 = new JPanel(new GridLayout(1, 2));
        operatePanel2.add(submitButton);
        operatePanel2.add(checkHeldCupButton);

        operatePanel3 = new JPanel(new GridLayout(1, 2));
        operatePanel3.add(changePasswordButton);
        operatePanel3.add(logoutButton);

        toolPanel = new JPanel(new GridLayout(3, 1));
        toolPanel.add(operatePanel1);
        toolPanel.add(operatePanel2);
        toolPanel.add(operatePanel3);

        overallPanel = new JPanel(new BorderLayout());
        overallPanel.add(toolPanel, BorderLayout.NORTH);
        overallPanel.add(new JScrollPane(infoArea), BorderLayout.CENTER);

        this.add(overallPanel);
    }
}
