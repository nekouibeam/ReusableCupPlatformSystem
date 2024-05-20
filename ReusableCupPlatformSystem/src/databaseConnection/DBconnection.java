package databaseConnection;

import java.sql.SQLException;

public interface DBconnection {
	String server = "jdbc:mysql://140.119.19.73:3315/";
	String database = "112306069";
	String url = server + database + "?useSSL=false";
	String username = "112306069"; 
	String password = "06bz8"; 
	
	String queryCupsHolding() throws SQLException;
}
