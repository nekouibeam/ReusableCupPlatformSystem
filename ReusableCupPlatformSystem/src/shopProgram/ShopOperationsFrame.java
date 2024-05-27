package shopProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import databaseConnection.ShopDBConn;
import databaseConnection.SignupAndLoginExceptions.*;

public class ShopOperationsFrame extends JFrame {
    private ShopDBConn shopConn;

    // GUI Components
    private JTextField idField;
    private JTextField activatePasswordField;
    private JTextField passwordField;

    public ShopOperationsFrame() {
        super("shop Activate/Login");

        try {
        	shopConn = new ShopDBConn();
        } catch (SQLException e) {
            showError("Database Connection Error", e.getMessage());
            return;
        }

        idField = new JTextField(15);
        activatePasswordField = new JTextField(15);
        passwordField = new JPasswordField(15);
        
        JButton ActivateButton = new JButton("Activate");
        JButton loginButton = new JButton("Login");
        

        // Set up the layout
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));
        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Initial Password:"));
        inputPanel.add(activatePasswordField);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(ActivateButton);
        buttonPanel.add(loginButton);
        
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(inputPanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);

        
        ActivateButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
            	handleActivate();            
            }
        });

        loginButton.addActionListener(new ActionListener() {
           
            public void actionPerformed(ActionEvent e) {
            	handleLogin();
            }
        });

        // Set default behaviors
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // Center on screen
    }

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
        }catch (NotActivateException e) {
            showError("Login Error", "Not Activate");
        }
    }

    private void handleActivate() {
        String id = idField.getText();
        String activatePassword = activatePasswordField.getText();
        String password = passwordField.getText();

        try {
        	shopConn.activateAccount(id, activatePassword, password);
            showMessage("Success", "Sign Up Successful");
        } catch (AccountNotExistException e) {
            showError("Sign Up Error", "Account does not exist");
        } catch (PasswordAlreadyUsedException e) {
            showError("Sign Up Error", "Password already in use");
        } catch (SQLException e) {
            showError("Sign Up Error", e.getMessage());
        }catch (PasswordWrongException e) {
            showError("Sign Up Error", "Wrong password");
        }
        
    }

    private void openShopFrame() {
    	ShopFrame shFrame = new ShopFrame(shopConn);
    	shFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	shFrame.setVisible(true);
        this.dispose(); 
    }

    private void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
