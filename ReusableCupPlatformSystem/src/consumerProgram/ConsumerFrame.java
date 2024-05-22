package consumerProgram;

import javax.swing.JFrame;
import databaseConnection.ConsumerDBConn;
import databaseConnection.SignupAndLoginExceptions;

public class ConsumerFrame extends JFrame{
	
	ConsumerDBConn dbcConn;
	
	public ConsumerFrame() {
		super("ConsumerFrame");
	}
}
