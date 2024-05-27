package databaseConnection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import databaseConnection.SignupAndLoginExceptions.AccountNotExistException;

public class PlatformDBConn extends DatabaseUpdate {
//Constructor
	public PlatformDBConn() throws SQLException {
		super();
	}

//Methods for operation
	public void addCup(String type, String size) throws SQLException {
		query = String.format(
				"INSERT INTO `Cups` (type, size, holder, holderID, status) VALUES ('%s', '%s', DEFAULT, DEFAULT, DEFAULT);",
				type, size);
		stat.execute(query);
	}

	public void shopSignUp(String ID, String name, String activatePassword)
			throws SQLException, IdAlreadyUsedException {
		signUpIDCheck(ID);
		query = String.format("INSERT INTO `Shop_Accounts` (ID, Name, Password) VALUES ('%s', '%s', '%s');", ID, name,
				activatePassword);
		stat.execute(query);

	}

	public void lendCup(int cupID, String ID) throws SQLException {
		query = String.format("UPDATE `Cups` SET holder = 'Shop', holderID = '%s' WHERE ID = %d;", ID, cupID);
		stat.execute(query);
		updateTransactionRecord(cupID, "Platform", "Null", "Shop", ID);
	}

	public void receiveCup(int cupID) throws SQLException {
		query = String.format("SELECT `holderID` FROM `Cups` WHERE `ID` = %d", cupID);
		rs = stat.executeQuery(query);
		rs.next();
		String holderID = rs.getString("holderID");

		query = String.format("UPDATE `Cups` SET holder = 'Platform', holderID = DEFAULT WHERE ID = %d;", cupID);
		stat.execute(query);

		updateTransactionRecord(cupID, "Shop", holderID, "Platform", "Null");
	}

	public String queryCupsHolding() throws SQLException {
		String list = "";
		query = String.format("SELECT * FROM `Cups` WHERE `holderID` = '%s';", "Null");
		rs = stat.executeQuery(query);

		ResultSetMetaData metadata = rs.getMetaData();
		int columnCount = metadata.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
				list += String.format("%-10s", metadata.getColumnName(i));
		}
		while (rs.next()) {
			list += String.format("\n%s\n", "-".repeat(70));
			String row = "";
			for (int i = 1; i <= columnCount; i++) {
				if (i == 1) {
					row += String.format("%-10d", rs.getInt(i));
				} else {
					row += String.format("%-10s", rs.getString(i));
				}
			}
			list += row;
		}
		return list;
	}

	public void changeStatus(int cupID) throws SQLException {
		query = String.format("UPDATE `Cups` SET `status` = 'No' WHERE ID = '%d';", cupID);
		stat.execute(query);
	}

	// woody
	public String consumerInfo() throws SQLException {

		String list = "";
		query = String.format("SELECT * FROM Consumer_Accounts");
		rs = stat.executeQuery(query);

		ResultSetMetaData metadata = rs.getMetaData();
		int columnCount = metadata.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			list += String.format("%-10s", metadata.getColumnName(i));
		}
		while (rs.next()) {
			list += String.format("\n%s\n", "-".repeat(70));
			String row = "";
			for (int i = 1; i <= columnCount; i++) {
					row += String.format("%-10s", rs.getString(i));
			}
			list += row;
		}
		return list;
	}

	public String shopInfo() throws SQLException {

		String list = "";
		query = String.format("SELECT * FROM Shop_Accounts");
		rs = stat.executeQuery(query);

		ResultSetMetaData metadata = rs.getMetaData();
		int columnCount = metadata.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
				list += String.format("%-10s", metadata.getColumnName(i));
		}
		while (rs.next()) {
			list += String.format("\n%s\n", "-".repeat(70));
			String row = "";
			for (int i = 1; i <= columnCount; i++) {
				if (i != columnCount) {
					row += String.format("%-10s", rs.getString(i));
				} else {
					row += String.format("%-10d", rs.getInt(i));
				}
			}
			list += row;
		}
		return list;
	}

	public String transInfo() throws SQLException {

		String list = "";
		query = String.format("SELECT * FROM Transaction_Records");
		rs = stat.executeQuery(query);

		ResultSetMetaData metadata = rs.getMetaData();
		int columnCount = metadata.getColumnCount();
		for (int i = 1; i < columnCount; i++) {

				list += String.format("%-10s", metadata.getColumnName(i));
		}
		while (rs.next()) {
			list += String.format("\n%s\n", "-".repeat(70));
			String row = "";
			for (int i = 1; i < columnCount; i++) {
				if (i == 1 || i == 2) {
					row += String.format("%-10d", rs.getInt(i));
				} else {
					row += String.format("%-10s", rs.getString(i));
				}
			}
			list += row;
		}
		return list;
	}

	public String cupInfo() throws SQLException {

		String list = "";
		query = String.format("SELECT * FROM Cups");
		rs = stat.executeQuery(query);

		ResultSetMetaData metadata = rs.getMetaData();
		int columnCount = metadata.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
				list += String.format("%-10s", metadata.getColumnName(i));
		}
		while (rs.next()) {
			list += String.format("\n%s\n", "-".repeat(70));
			String row = "";
			for (int i = 1; i <= columnCount; i++) {
				if (i == 1) {
					row += String.format("%-10d", rs.getInt(i));
				} else {
					row += String.format("%-10s", rs.getString(i));
				}
			}
			list += row;
		}
		return list;
	}

//Methods for checking
	public void signUpIDCheck(String ID) throws SQLException, IdAlreadyUsedException {
		query = String.format("SELECT `ID` FROM `Shop_Accounts` WHERE `ID` = '%s';", ID);
		rs = stat.executeQuery(query);
		if (rs.next()) {
			throw new IdAlreadyUsedException();
		}
	}
	
	public void shopExistCheck(String ID) throws SQLException, AccountNotExistException {
		query = String.format("SELECT `ID` FROM `Shop_Accounts` WHERE `ID` = '%s';", ID);
		rs = stat.executeQuery(query);
		if (!rs.next()) {
			throw new AccountNotExistException();
		}
	}
}
