package databaseConnection;

import java.sql.*;

public class PlatformDBConn extends DatabaseUpdate {

	public PlatformDBConn() throws SQLException {
		super();
	}

	public void addCup(String type, String size) throws SQLException {
		query = String.format(
				"INSERT INTO `Cups` (type, size, holder, holderID, status) VALUES ('%s', '%s', DEFAULT, DEFAULT, DEFAULT);",
				type, size);
		stat.execute(query);
	}

	public void storeSignUp(String ID, String name) throws SQLException {
		query = String.format("INSERT INTO `Shop_Accounts` (ID, Name) VALUES ('%s', '%s');", ID, name);
		stat.execute(query);

	}

	public void signUpIDCheck(String ID) throws SQLException, IdAlreadyUsedException {
		query = String.format("SELECT `ID` FROM `Shop_Accounts` WHERE `ID` = '%s';", ID);
		rs = stat.executeQuery(query);
		if (rs.next()) {
			throw new IdAlreadyUsedException();
		}
	}
	
	public void lendCup(int number, String type, String size, int shopID) throws SQLException {

	}

	public void lendCup(int cupID, String ID) throws SQLException {
		query = String.format("UPDATE `Cups` SET holder = 'Shop', holderID = '%s' WHERE ID = %d;", ID, cupID);
		stat.execute(query);
		updateTransactionRecord(cupID, "Platform", "NULL", "Shop", ID);
	}

	public void receiveCup(int cupID) throws SQLException {
		query = String.format("SELECT `holderID` FROM `Cups` WHERE `ID` = %d", cupID);
		rs = stat.executeQuery(query);
		rs.next();
		String holderID = rs.getString("holderID");

		query = String.format("UPDATE `Cups` SET holder = 'Platform', holderID = DEFAULT WHERE ID = %d;", cupID);
		stat.execute(query);

		updateTransactionRecord(cupID, "Shop", holderID, "Platform", "null");
	}

	public String queryCupsHolding() throws SQLException {
		String list = "";
		query = String.format("SELECT * FROM `Cups` WHERE `holderID` = '%s';", "NULL");
		rs = stat.executeQuery(query);

		ResultSetMetaData metadata = rs.getMetaData();
		int columnCount = metadata.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			if (i == 1) {
				list += String.format("%-10s", metadata.getColumnName(i));
			} else {
				list += String.format("|%-10s", metadata.getColumnName(i));
			}
		}
		while (rs.next()) {
			list += String.format("\n%s\n", "-".repeat(70));
			String row = "";
			for (int i = 1; i <= columnCount; i++) {
				if (i == 1) {
					row += String.format("%-10d", rs.getInt(i));
				} else {
					row += String.format("|%-10s", rs.getString(i));
				}
			}
			list += row;
		}
		return list;
	}
}
