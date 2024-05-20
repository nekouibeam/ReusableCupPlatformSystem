package databaseConnection;

import java.sql.*;

public class ShopDBConn extends DatabaseUpdate{

	private String shopID;

	public ShopDBConn() throws SQLException {
		super();
	}

	public void setID(String ID) {
		this.shopID = ID;
	}

	public void activateAccount(String ID, String password) throws SQLException {
		query = String.format("UPDATE `Shop_Accounts` SET `Password` = '%s' WHERE ID = %s;", password, ID);
		stat.execute(query);
	}

	public void changePassword(String ID, String newPassword) throws SQLException {
		query = String.format("UPDATE `Shop_Accounts` SET `Password` = '%s' WHERE ID = %s;", newPassword, ID);
		stat.execute(query);
	}

	public void changeName(String ID, String newName) throws SQLException {
		query = String.format("UPDATE `Shop_Accounts` SET `Name` = '%s' WHERE ID = %s;", newName, ID);
		stat.execute(query);
	}

	public void loginCheck(String ID, String password) throws SQLException, PasswordWrongException{
		query = String.format("SELECT `ID`, `Password` FROM `Shop_Accounts` WHERE `ID` = '%s' AND `Password` = '%s';",
				ID, password);
		rs = stat.executeQuery(query);
		if (!rs.next()) {
			throw new PasswordWrongException();
		}
	}

	public void accountExistCheck(String ID) throws SQLException, AccountNotExistException{
		query = String.format(
				"SELECT `ID` FROM `Shop_Accounts` WHERE `ID` = '%s';", ID);
		rs = stat.executeQuery(query);
		if (!rs.next()) {
			throw new AccountNotExistException();
		}
	}
	
	public void lendCup(int cupID, String ID) throws SQLException {
		query = String.format("UPDATE `Cups` SET holder = 'Consumer', holderID = '%s', status = 'Yes' WHERE ID = %d;",
				ID, cupID);
		stat.execute(query);
		updateTransactionRecord(cupID, "Store", shopID, "Comsumer", ID);
	}

	public void receiveCup(int cupID) throws SQLException {
		query = String.format("SELECT `holderID` FROM `Cups` WHERE `ID` = %d", cupID);
		rs = stat.executeQuery(query);
		rs.next();
		String holderID = rs.getString("holderID");

		query = String.format("UPDATE `Cups` SET holder = 'Shop', holderID = '%s' WHERE ID = %d;", shopID, cupID);
		stat.execute(query);

		updateTransactionRecord(cupID, "Comsumer", holderID, "Store", shopID);
	}

	public String queryCupsHolding() throws SQLException {
		String list = "";
		query = String.format("SELECT * FROM `Cups` WHERE `holderID` = '%s';", shopID);
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
