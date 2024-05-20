//This class is just for Testing
package databaseConnection;

import java.sql.SQLException;
import databaseConnection.SignupAndLoginExceptions.*;

public class DBC_Test {
	
	PlatformDBConn Pdbc;
	ShopDBConn Sdbc;
	ConsumerDBConn Cdbc;
	
	public DBC_Test() {
		try {
			//Pdbc = new PlatformDBConn();
			//Sdbc = new StoreDBConn();
			Cdbc = new ConsumerDBConn();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void test() {
		//Pdbc.addCup("cold", "small");
		//Pdbc.lendCup(3, "s1");
		//Sdbc.setID("milktea1");
		//Sdbc.lendCup(3, "com1");
		//Sdbc.receiveCup(3);
		//Pdbc.receiveCup(3);
		try {
			//System.out.print(Sdbc.queryCupsHolding());
			//Cdbc.consumerSignUp("Allen666", "Allen", "9487943");
			Cdbc.loginCheck("Allen666", "9487942");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PasswordWrongException e) {
			e.printStackTrace();
		}
	}
}
