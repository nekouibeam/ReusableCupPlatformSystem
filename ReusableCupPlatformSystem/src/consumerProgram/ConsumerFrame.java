package consumerProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import databaseConnection.ConsumerDBConn;
import databaseConnection.SignupAndLoginExceptions.*;

public class ConsumerFrame extends JFrame {
    private ConsumerDBConn dbcConn;

    // GUI Components
    private JTextField idField;
    private JTextField nameField;
    private JPasswordField passwordField;

    public ConsumerFrame() {
        super("Consumer Login/Sign Up");

        // Initialize database connection
        try {
            dbcConn = new ConsumerDBConn();
        } catch (SQLException e) {
            showError("Database Connection Error", e.getMessage());
            return;
        }

        // Create GUI components
        idField = new JTextField(15);
        nameField = new JTextField(15);
        passwordField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");

        // Set up the layout
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));
        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(inputPanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignUp();
            }
        });

        // Set default behaviors
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // Center on screen
    }

    private void handleLogin() {
        String id = idField.getText();
        String password = new String(passwordField.getPassword());

        try {
            dbcConn.login(id, password);
            showMessage("Success", "Login Successful");
            openConsumerOperationsFrame();
        } catch (PasswordWrongException e) {
            showError("Login Error", "Wrong password");
        } catch (AccountNotExistException e) {
            showError("Login Error", "Account does not exist");
        } catch (SQLException e) {
            showError("Login Error", e.getMessage());
        }
    }

    private void handleSignUp() {
        String id = idField.getText();
        String name = nameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            dbcConn.consumerSignUp(id, name, password);
            showMessage("Success", "Sign Up Successful");
        } catch (IdAlreadyUsedException e) {
            showError("Sign Up Error", "ID already in use");
        } catch (PasswordAlreadyUsedException e) {
            showError("Sign Up Error", "Password already in use");
        } catch (SQLException e) {
            showError("Sign Up Error", e.getMessage());
        }
    }

    private void openConsumerOperationsFrame() {
        ConsumerOperationsFrame operationsFrame = new ConsumerOperationsFrame(dbcConn);
        operationsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        operationsFrame.setVisible(true);
        this.dispose(); // Close the login/signup window
    }

    private void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
