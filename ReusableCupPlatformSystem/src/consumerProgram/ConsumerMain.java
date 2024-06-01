package consumerProgram;

import java.sql.SQLException;
import javax.swing.*;

/**
 * The ConsumerMain class serves as the entry point for the consumer application.
 * It initializes and displays the ConsumerFrame for user interaction.
 */
public class ConsumerMain {

    /**
     * Constructs a ConsumerMain object, initializing and displaying the ConsumerFrame.
     */
    public ConsumerMain() {
        ConsumerFrame cframe;
		try {
			cframe = new ConsumerFrame(new databaseConnection.ConsumerDBConn());
			cframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        cframe.setVisible(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 JOptionPane.showMessageDialog(null, "Database Connection Error: " + e.getMessage(), "Error",
	                    JOptionPane.ERROR_MESSAGE);
		}
        
    }

    /**
     * The main method to run the ConsumerMain class.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        new ConsumerMain();
    }
}
