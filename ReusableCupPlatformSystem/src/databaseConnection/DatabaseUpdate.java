package databaseConnection;

import java.sql.*;

public abstract class DatabaseUpdate extends SignupAndLoginExceptions implements DBconnection {
	protected Connection conn;
	protected Statement stat;
	protected ResultSet rs;
	protected String query;

	abstract public void lendCup(int cupID, String ID) throws SQLException;

	abstract public void receiveCup(int cupID) throws SQLException;

	public DatabaseUpdate() throws SQLException {
		conn = DriverManager.getConnection(url, username, password);
		stat = conn.createStatement();
	}

	public void updateTransactionRecord(int cupID, String from, String fromID, String to, String toID)
			throws SQLException {
		query = String.format(
				"INSERT INTO `Transaction_Records` (cupID, From_, FromID, To_, ToID, Time) VALUES (%d,'%s', '%s', '%s', '%s', DEFAULT);",
				cupID, from, fromID, to, toID);
		stat.execute(query);
	}
}
