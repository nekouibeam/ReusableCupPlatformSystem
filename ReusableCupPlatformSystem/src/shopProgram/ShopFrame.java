package shopProgram;

import javax.swing.JFrame;

import databaseConnection.ShopDBConn;
import databaseConnection.SignupAndLoginExceptions;

public class ShopFrame extends JFrame{
	
	ShopDBConn dbcConn;

	public ShopFrame() {
		super("StoreFrame");
	}
}
