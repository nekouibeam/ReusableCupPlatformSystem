package platformManagementProgram;

import javax.swing.JFrame;

import databaseConnection.PlatformDBConn;
import databaseConnection.SignupAndLoginExceptions;

public class PlatformManagementFrame extends JFrame{
	
	PlatformDBConn dbcConn;
	
	public PlatformManagementFrame() {
		super("PlatformManagementFrame");
	}
}
