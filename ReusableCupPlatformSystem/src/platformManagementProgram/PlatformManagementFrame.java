package platformManagementProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import databaseConnection.PlatformDBConn;
import databaseConnection.SignupAndLoginExceptions.*;

public class PlatformManagementFrame extends JFrame {

    PlatformDBConn dbcConn;

    private final int frame_width = 400, frame_height = 300;
    private JButton cupButton, consumerButton, shopButton, transRecordButton, addCupButton, shopSignUpButton,
            lendCupButton, receiveCupButton, queryCupsHoldingButton;
    private JTextArea textArea;
    private JPanel buttonPanel;
    private JScrollPane scrollPane;

    public PlatformManagementFrame() {

        super("PlatformManagementFrame");

        try {
            dbcConn = new PlatformDBConn();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        consumerButton = new JButton("Consumer information");
        shopButton = new JButton("Shop information");
        transRecordButton = new JButton("TransactionRecord information");
        cupButton = new JButton("Cup information");
        addCupButton = new JButton("addCup");
        shopSignUpButton = new JButton("shopSignUp");
        lendCupButton = new JButton("lendCup");
        receiveCupButton = new JButton("receiveCup");
        queryCupsHoldingButton = new JButton("queryCupsHolding");
        textArea = new JTextArea();
        textArea.setEditable(false); // Make textArea read-only

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
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
    }

    public void addButtonsFunction() {
        consumerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    textArea.setText(dbcConn.consumerInfo());
                } catch (SQLException ee) {
                    ee.printStackTrace();
                    textArea.setText("錯誤: " + ee.getMessage());
                } 
            }
        });

        shopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                	textArea.setText(dbcConn.shopInfo());
                } catch (SQLException ee) {
                    ee.printStackTrace();
                    textArea.setText("錯誤: " + ee.getMessage());
                } 
            }
        });

        transRecordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                	textArea.setText(dbcConn.transInfo());
                } catch (SQLException ee) {
                    ee.printStackTrace();
                    textArea.setText("錯誤: " + ee.getMessage());
                } 
            }
        });

        cupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                	textArea.setText(dbcConn.cupInfo());
                } catch (SQLException ee) {
                    ee.printStackTrace();
                    textArea.setText("錯誤: " + ee.getMessage());
                } 
            }
        });

        addCupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String type = JOptionPane.showInputDialog("請輸入杯子類型:");
                String size = JOptionPane.showInputDialog("請輸入杯子大小:");
                try {
                    dbcConn.addCup(type, size);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    textArea.setText("錯誤: " + ex.getMessage());
                }
            }
        });

        shopSignUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ID = JOptionPane.showInputDialog("請輸入商店ID:");
                String name = JOptionPane.showInputDialog("請輸入商店名稱:");
                String activatePassword = JOptionPane.showInputDialog("請輸入啟動密碼:");
                try {
                    dbcConn.shopSignUp(ID, name, activatePassword);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    textArea.setText("錯誤: " + ex.getMessage());
                } catch (IdAlreadyUsedException ee) {
                    ee.printStackTrace();
                    textArea.setText("錯誤: " + ee.getMessage());
                }
            }
        });

        lendCupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cupIDInput = JOptionPane.showInputDialog("請輸入杯子ID:");
                int cupID = Integer.parseInt(cupIDInput);
                String shopID = JOptionPane.showInputDialog("請輸入商店ID:");
                try {
                    dbcConn.lendCup(cupID, shopID);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    textArea.setText("錯誤: " + ex.getMessage());
                }
            }
        });

        receiveCupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cupIDInput = JOptionPane.showInputDialog("請輸入杯子ID:");
                int cupID = Integer.parseInt(cupIDInput);
                try {
                    dbcConn.receiveCup(cupID);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    textArea.setText("錯誤: " + ex.getMessage());
                }
            }
        });

        queryCupsHoldingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    textArea.setText(dbcConn.queryCupsHolding());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    textArea.setText("錯誤: " + ex.getMessage());
                }
            }
        });
    }
}
