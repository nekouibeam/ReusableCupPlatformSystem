package databaseConnection;

import java.sql.*;

public class ShopDBConn extends DatabaseUpdate {

	private String shopID;

//Constructor
	public ShopDBConn() throws SQLException {
		super();
	}

//Methods for operation
	public void activateAccount(String ID, String activatePassword, String newPassword)
			throws SQLException, AccountNotExistException, PasswordAlreadyUsedException, PasswordWrongException {
		accountExistCheck(ID);
		query = String.format("SELECT `ID`, `Password` FROM `Shop_Accounts` WHERE `ID` = '%s' AND `Password` = '%s';",
				ID, activatePassword);
		rs = stat.executeQuery(query);
		if (!rs.next()) {
			throw new PasswordWrongException();
		}
		changePassword(ID, newPassword);
		setActivate(ID);
	}

	public void login(String ID, String password)
			throws SQLException, PasswordWrongException, NotActivateException, AccountNotExistException {
		accountExistCheck(ID);
		checkActivate(ID);
		query = String.format("SELECT `ID`, `Password` FROM `Shop_Accounts` WHERE `ID` = '%s' AND `Password` = '%s';",
				ID, password);
		rs = stat.executeQuery(query);
		if (!rs.next()) {
			throw new PasswordWrongException();
		}
		setID(ID);
	}

	public void changePassword(String ID, String newPassword) throws SQLException, PasswordAlreadyUsedException {
		newPasswordCheck(newPassword);
		query = String.format("UPDATE `Shop_Accounts` SET `Password` = '%s' WHERE ID = '%s';", newPassword, ID);
		stat.execute(query);
	}

	public void changeName(String ID, String newName) throws SQLException {
		query = String.format("UPDATE `Shop_Accounts` SET `Name` = '%s' WHERE ID = %s;", newName, ID);
		stat.execute(query);
	}

	public void lendCup(int cupID, String ID) throws SQLException {
		query = String.format("UPDATE `Cups` SET holder = 'Consumer', holderID = '%s', status = 'Used' WHERE ID = %d;",
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

//Setter and Methods for checking
	public void setID(String ID) {
		this.shopID = ID;
	}

	public void setActivate(String ID) throws SQLException {
		query = String.format("UPDATE `Shop_Accounts` SET `Activate` = '1' WHERE ID = '%s';", ID);
		stat.execute(query);
	}

	public void checkActivate(String ID) throws SQLException, NotActivateException {
		query = String.format("SELECT `ID` FROM `Shop_Accounts` WHERE `ID` = '%s' AND `Activate` = 1;", ID);
		rs = stat.executeQuery(query);
		if (!rs.next()) {
			throw new NotActivateException();
		}
	}

	public void newPasswordCheck(String password) throws SQLException, PasswordAlreadyUsedException {
		query = String.format("SELECT `Password` FROM `Shop_Accounts` WHERE `Password` = '%s';", password);
		rs = stat.executeQuery(query);
		if (rs.next()) {
			throw new PasswordAlreadyUsedException();
		}
	}

	public void accountExistCheck(String ID) throws SQLException, AccountNotExistException {
		query = String.format("SELECT `ID` FROM `Shop_Accounts` WHERE `ID` = '%s';", ID);
		rs = stat.executeQuery(query);
		if (!rs.next()) {
			throw new AccountNotExistException();
		}
	}
	
	public void consumerExistCheck(String ID) throws SQLException, AccountNotExistException {
		query = String.format("SELECT `ID` FROM `Consumer_Accounts` WHERE `ID` = '%s';", ID);
		rs = stat.executeQuery(query);
		if (!rs.next()) {
			throw new AccountNotExistException();
		}
	}

}
