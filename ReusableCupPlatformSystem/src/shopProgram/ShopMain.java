package shopProgram;

import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import databaseConnection.ShopDBConn;

/**
 * The main class for the Shop application.
 * Initializes the shop operation frame and handles database connection.
 */
public class ShopMain {

    /**
     * Constructs the ShopMain class.
     * Attempts to create a new ShopOperationsFrame with a database connection.
     * If the database connection fails, an error message is displayed.
     */
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
    }

    /**
     * The main method to run the ShopMain class.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        new ShopMain();
    }
}
