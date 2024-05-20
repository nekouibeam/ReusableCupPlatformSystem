package databaseConnection;

import java.sql.*;

public class ConsumerDBConn extends SignupAndLoginExceptions implements DBconnection {
	private Connection conn;
	private Statement stat;
	private ResultSet rs;
	private String query;
	private String comsumerID;

	public ConsumerDBConn() throws SQLException {
		conn = DriverManager.getConnection(url, username, password);
		stat = conn.createStatement();
	}

	public void setID(String ID) {
		this.comsumerID = ID;
	}

	public void consumerSignUp(String ID, String name, String password) throws SQLException {
		query = String.format("INSERT INTO `Consumer_Accounts` (ID, Name, Password) VALUES ('%s', '%s', '%s');", ID,
				name, password);
		stat.execute(query);
	}

	public void singUpIDCheck(String ID) throws SQLException, IdAlreadyUsedException{
		query = String.format("SELECT `ID` FROM `Consumer_Accounts` WHERE `ID` = '%s';", ID);
		rs = stat.executeQuery(query);
		if (rs.next()) {
			throw new IdAlreadyUsedException();
		}
	}

	public void signUpPasswordCheck(String password) throws SQLException, PasswordAlreadyUsedException{
		query = String.format("SELECT `Password` FROM `Consumer_Accounts` WHERE `Password` = '%s';", password);
		rs = stat.executeQuery(query);
		if (rs.next()) {
			throw new PasswordAlreadyUsedException();
		}
	}

	public void loginCheck(String ID, String password) throws SQLException, PasswordWrongException{
		query = String.format(
				"SELECT `ID`, `Password` FROM `Consumer_Accounts` WHERE `ID` = '%s' AND `Password` = '%s';", ID,
				password);
		rs = stat.executeQuery(query);
		if (!rs.next()) {
			throw new PasswordWrongException();
		}
	}
	
	public void accountExistCheck(String ID) throws SQLException, AccountNotExistException{
		query = String.format(
				"SELECT `ID` FROM `Consumer_Accounts` WHERE `ID` = '%s';", ID);
		rs = stat.executeQuery(query);
		if (!rs.next()) {
			throw new AccountNotExistException();
		}
	}

	public void changePassword(String ID, String newPassword) throws SQLException {
		query = String.format("UPDATE `Consumer_Accounts` SET `Password` = '%s' WHERE ID = %s;", newPassword, ID);
		stat.execute(query);
	}

	public void changeName(String ID, String newName) throws SQLException {
		query = String.format("UPDATE `Consumer_Accounts` SET `Name` = '%s' WHERE ID = %s;", newName, ID);
		stat.execute(query);
	}

	public String queryCupsHolding() throws SQLException {
		String list = "";
		query = String.format("SELECT * FROM `Cups` WHERE `holderID` = '%s';", comsumerID);
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