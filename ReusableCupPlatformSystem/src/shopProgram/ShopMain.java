package shopProgram;

import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import databaseConnection.ShopDBConn;

public class ShopMain {

	public ShopMain() {
		try {
			ShopOperationsFrame shopOpFrame = new ShopOperationsFrame(new ShopDBConn());
			shopOpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			shopOpFrame.setVisible(true);
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Database Connection Error: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		/*
		 * ShopOperationsFrame sFrame = new ShopOperationsFrame(shopConn);
		 * sFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 * sFrame.setVisible(true);
		 */
	}
}
