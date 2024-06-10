package shopProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import databaseConnection.ShopDBConn;
import databaseConnection.SignupAndLoginExceptions.*;

/**
 * The ShopOperationsFrame class provides a GUI for shop activation and login.
 * It connects to the ShopDBConn for database operations.
 */
public class ShopOperationsFrame extends JFrame {

    private ShopDBConn shopConn;
    private JTextField idField;
    private JTextField activatePasswordField;
    private JTextField passwordField;

    /**
     * Constructs a ShopOperationsFrame with the given database connection.
     * 
     * @param shopConn the database connection for shop operations
     */
    public ShopOperationsFrame(ShopDBConn shopConn) {
        super("Shop Activate/Login");

        this.shopConn = shopConn;

        idField = new JTextField(15);
        activatePasswordField = new JTextField(15);
        passwordField = new JPasswordField(15);

        JButton activateButton = new JButton("Activate");
        JButton loginButton = new JButton("Login");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));
        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Initial Password:"));
        inputPanel.add(activatePasswordField);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(activateButton);
        buttonPanel.add(loginButton);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(inputPanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);

        activateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleActivate();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Handles the activation of a shop account.
     * Checks input fields and calls the database connection to activate the account.
     */
    private void handleActivate() {
        String id = idField.getText();
        String activatePassword = activatePasswordField.getText();
        String password = passwordField.getText();

        try {
            shopConn.activateAccount(id, activatePassword, password);
            showMessage("Success", "Active Successful");
        } catch (AccountNotExistException e) {
            showError("Activate Error", "Account does not exist");
        } catch (PasswordAlreadyUsedException e) {
            showError("Activate Error", "Password already in use");
        } catch (PasswordWrongException e) {
            showError("Activate Error", "Password wrong");
        } catch (SQLException e) {
            showError("Activate Error", e.getMessage());
        } catch (IdCantEmptyException e) {
            showError("Activate Error", "ID can't be empty");
        } catch (InitialPasswordCantEmptyException e) {
            showError("Activate Error", "Initial password can't be empty");
        } catch (PasswordCantEmptyException e) {
            showError("Activate Error", "Password can't be empty");
        }
    }

    /**
     * Handles the login of a shop account.
     * Checks input fields and calls the database connection to log in to the account.
     */
    private void handleLogin() {
        String id = idField.getText();
        String password = passwordField.getText();

        try {
            shopConn.login(id, password);
            showMessage("Success", "Login Successful");
            openShopFrame();
        } catch (PasswordWrongException e) {
            showError("Login Error", "Wrong password");
        } catch (AccountNotExistException e) {
            showError("Login Error", "Account does not exist");
        } catch (SQLException e) {
            showError("Login Error", e.getMessage());
        } catch (NotActivateException e) {
            showError("Login Error", "Account not activated");
        } catch (IdCantEmptyException e) {
            showError("Login Error", "ID can't be empty");
        } catch (PasswordCantEmptyException e) {
            showError("Login Error", "Password can't be empty");
        }
    }

    /**
     * Opens the ShopFrame after successful login.
     * Disposes the current frame.
     */
    private void openShopFrame() {
        ShopFrame shFrame = new ShopFrame(shopConn);
        shFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        shFrame.setVisible(true);
        this.dispose();
    }

    /**
     * Displays an informational message dialog.
     *
     * @param title   the title of the dialog
     * @param message the message to display
     */
    private void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays an error message dialog.
     *
     * @param title   the title of the dialog
     * @param message the error message to display
     */
    private void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
