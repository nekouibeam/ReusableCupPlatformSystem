package consumerProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import databaseConnection.ConsumerDBConn;
import databaseConnection.SignupAndLoginExceptions.AccountNotExistException;
import databaseConnection.SignupAndLoginExceptions.IdCantEmptyException;
import databaseConnection.SignupAndLoginExceptions.PasswordAlreadyUsedException;
import databaseConnection.SignupAndLoginExceptions.PasswordCantEmptyException;
import databaseConnection.SignupAndLoginExceptions.PasswordWrongException;

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
        outputArea = new JTextArea(10, 60);
        outputArea.setEditable(false);
        JButton queryCupsButton = new JButton("CupsHolding");
        JButton logoutButton = new JButton("Logout");
        JButton changePasswordButton = new JButton("Change Password");
        JButton changeNameButton = new JButton("Change Name");

        // Set up the layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(queryCupsButton);
        buttonPanel.add(changePasswordButton);
        buttonPanel.add(changeNameButton);
        buttonPanel.add(logoutButton);

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
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	 int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                 if (response == JOptionPane.YES_OPTION) {
                     openConsumerFrame(dbcConn);
                 }
            }
        });
        
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	String userID = dbcConn.getID();

				String oldPassword = JOptionPane.showInputDialog(null, "Enter Old Password:");
				if (oldPassword == null) {
				    return;
				} else {
				    try {
				    	dbcConn.login(userID, oldPassword);
				    } catch (PasswordWrongException e) {
				        JOptionPane.showMessageDialog(null, "Old Password is incorrect", "Error", JOptionPane.ERROR_MESSAGE);
				        return;
				    } catch (SQLException | AccountNotExistException | IdCantEmptyException e) {
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
				    	dbcConn.changePassword(userID, newPassword);
				        JOptionPane.showMessageDialog(null, "Password changed successfully");
				    } catch (SQLException e) {
				        JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				    } catch (PasswordAlreadyUsedException e) {
				        JOptionPane.showMessageDialog(null, "Password already used", "Error", JOptionPane.ERROR_MESSAGE);
				    }
				}
            }
        });
        
        changeNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	String userID = dbcConn.getID();

				String newName = JOptionPane.showInputDialog(null, "Enter new name:");
				if(newName == null) {
					return;
				}else if(newName.isEmpty()) {
					JOptionPane.showMessageDialog(null, "New name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
				}else {
					try {
						dbcConn.changeName(userID, newName);
						JOptionPane.showMessageDialog(null, "Name changed successfully");
					}catch(SQLException e) {
						JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
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
    
    /**
     * Opens the openConsumerFrame and closes the current frame.
     *
     * @param dbcConn the database connection for the ConsumerFrame.
     */
    public void openConsumerFrame(ConsumerDBConn dbcConn) {
        ConsumerFrame consumerFrame = new ConsumerFrame(dbcConn);
        consumerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        consumerFrame.setVisible(true);
        this.dispose();
    }
}
