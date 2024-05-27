package shopProgram;

import databaseConnection.ConsumerDBConn;
import databaseConnection.ShopDBConn;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ShopFrame extends JFrame {
	private ShopDBConn shopConn;
	private static final int FRAME_WIDTH = 400;
	private static final int FRAME_HEIGHT = 300;
	private static final int FIELD_WIDTH = 5;
	private JPanel actionPanel, cupIDPanel, customerIDPanel, operatePanel1, operatePanel2, toolPanel, overallPanel;
	private JTextArea infoArea;
	private JLabel actionLabel, cupIDLabel, customerIDLabel;
	private JComboBox<String> actionCombo;
	private JTextField cupIDField, customerIDField;
	private JButton submitButton, historyButton;
	private ShopDBConn shopconn;

	public ShopFrame(ShopDBConn shopConn) {
		this.shopConn = shopConn;
		this.setTitle("Store");
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		createItemComp();
		createButton();
		createInfoArea();
		createPanel();
	}

	public void createItemComp() {
		actionLabel = new JLabel("action");
		actionCombo = new JComboBox<String>();
		actionCombo.addItem("rent");
		actionCombo.addItem("rerturn");

		cupIDLabel = new JLabel("cupID");
		customerIDLabel = new JLabel("userID");

		cupIDField = new JTextField(FIELD_WIDTH);
		customerIDField = new JTextField(FIELD_WIDTH);

	}

	public void createButton() {
		submitButton = new JButton("Submit");
		historyButton = new JButton("List cups held");

		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String op = (String) actionCombo.getSelectedItem();

				if (op.equals("rent")) {
					try {
						shopConn.lendCup(Integer.parseInt(cupIDField.getText()), customerIDField.getText());
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null, "something wrong", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else if (op.equals("rerturn")) {
					try {
						shopConn.receiveCup(Integer.parseInt(cupIDField.getText()));
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null, "something wrong", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}

				infoArea.setText("Action completed!");
			}
		});

		historyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					infoArea.setText(shopConn.queryCupsHolding());
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "something wrong", "Error", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
	}

	public void createInfoArea() {
		infoArea = new JTextArea(20, 30);
	}

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
		operatePanel2.add(historyButton);

		toolPanel = new JPanel(new GridLayout(2, 1));
		toolPanel.add(operatePanel1);
		toolPanel.add(operatePanel2);

		overallPanel = new JPanel(new BorderLayout());
		overallPanel.add(toolPanel, BorderLayout.NORTH);

		overallPanel.add(infoArea, BorderLayout.CENTER);

		this.add(overallPanel);
	}
}
