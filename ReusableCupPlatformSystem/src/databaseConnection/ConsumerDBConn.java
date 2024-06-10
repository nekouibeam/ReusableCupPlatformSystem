package databaseConnection;

import java.sql.*;

/**
 * This class provides methods for consumer database operations such as signing up, logging in, 
 * querying held cups, and changing account details.
 */
public class ConsumerDBConn extends SignupAndLoginExceptions implements DBconnection {
    /**
     * Connection to the database.
     */
    private Connection conn;
    
    /**
     * Statement for executing SQL queries.
     */
    private Statement stat;
    
    /**
     * ResultSet for storing query results.
     */
    private ResultSet rs;
    
    /**
     * SQL query string.
     */
    private String query;
    
    /**
     * ID of the consumer.
     */
    private String comsumerID;

    /**
     * Constructs a ConsumerDBConn object and establishes a connection to the database.
     *
     * @throws SQLException if a database access error occurs.
     */
    public ConsumerDBConn() throws SQLException {
        conn = DriverManager.getConnection(url, username, password);
        stat = conn.createStatement();
    }

    /**
     * Signs up a new consumer with the given ID, name, and password.
     *
     * @param ID the consumer's ID.
     * @param name the consumer's name.
     * @param password the consumer's password.
     * @throws SQLException if a database access error occurs.
     * @throws IdAlreadyUsedException if the ID is already used.
     * @throws PasswordAlreadyUsedException if the password is already used.
     * @throws IdCantEmptyException if the ID is empty.
     * @throws PasswordCantEmptyException if the password is empty.
     * @throws NameCantEmptyException if the password is empty.
     */
    public void consumerSignUp(String ID, String name, String password)
            throws SQLException, IdAlreadyUsedException, PasswordAlreadyUsedException,  IdCantEmptyException, PasswordCantEmptyException, NameCantEmptyException{
    	if (ID == null || ID.trim().isEmpty()) {
            throw new IdCantEmptyException();
        } else if (password == null || password.trim().isEmpty()) {
            throw new PasswordCantEmptyException();
        } else if(name == null || name.trim().isEmpty()) {
        	throw new NameCantEmptyException();
        }
    	singUpIDCheck(ID);
        newPasswordCheck(password);
        query = String.format("INSERT INTO `Consumer_Accounts` (ID, Name, Password) VALUES ('%s', '%s', '%s');", ID,
                name, password);
        stat.execute(query);
    }

    /**
     * Logs in a consumer with the given ID and password.
     *
     * @param ID the consumer's ID.
     * @param password the consumer's password.
     * @throws SQLException if a database access error occurs.
     * @throws PasswordWrongException if the password is incorrect.
     * @throws AccountNotExistException if the account does not exist.
     * @throws IdCantEmptyException if the ID is empty.
     * @throws PasswordCantEmptyException if the password is empty.
     */
    public void login(String ID, String password) throws SQLException, PasswordWrongException, AccountNotExistException, IdCantEmptyException, PasswordCantEmptyException {
    	  if (ID == null || ID.trim().isEmpty()) {
              throw new IdCantEmptyException();
          } else if (password == null || password.trim().isEmpty()) {
              throw new PasswordCantEmptyException();
          }
    	accountExistCheck(ID);
        query = String.format(
                "SELECT `ID`, `Password` FROM `Consumer_Accounts` WHERE `ID` = '%s' AND `Password` = '%s';", ID,
                password);
        rs = stat.executeQuery(query);
        if (!rs.next()) {
            throw new PasswordWrongException();
        }
        this.setID(ID);
    }

    /**
     * Queries the cups currently held by the logged-in consumer.
     *
     * @return a {@code String} listing the details of the cups held by the consumer.
     * @throws SQLException if a database access error occurs.
     */
    public String queryCupsHolding() throws SQLException {
        String list = "";
        query = String.format("SELECT * FROM `Cups` WHERE `holderID` = '%s';", comsumerID);
        rs = stat.executeQuery(query);

        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            list += String.format("%s\t", metadata.getColumnName(i));
        }
        while (rs.next()) {
            list += String.format("\n%s\n", "-".repeat(140));
            String row = "";
            for (int i = 1; i <= columnCount; i++) {
                if (i == 1) {
                    row += String.format("%d\t", rs.getInt(i));
                } else {
                    row += String.format("%s\t", rs.getString(i));
                }
            }
            list += row;
        }
        return list;
    }

    /**
     * Changes the password of the consumer with the given ID to a new password.
     *
     * @param ID the consumer's ID.
     * @param newPassword the new password.
     * @throws SQLException if a database access error occurs.
     * @throws PasswordAlreadyUsedException if the new password is already used.
     */
    public void changePassword(String ID, String newPassword) throws SQLException, PasswordAlreadyUsedException {
        this.newPasswordCheck(newPassword);
        query = String.format("UPDATE `Consumer_Accounts` SET `Password` = '%s' WHERE ID = '%s';", newPassword, ID);
        stat.execute(query);
    }

    /**
     * Changes the name of the consumer with the given ID to a new name.
     *
     * @param ID the consumer's ID.
     * @param newName the new name.
     * @throws SQLException if a database access error occurs.
     */
    public void changeName(String ID, String newName) throws SQLException {
        query = String.format("UPDATE `Consumer_Accounts` SET `Name` = '%s' WHERE ID = '%s';", newName, ID);
        stat.execute(query);
    }

    /**
     * Sets the ID of the logged-in consumer.
     *
     * @param ID the consumer's ID.
     */
    public void setID(String ID) {
        this.comsumerID = ID;
    }

    /**
     * Checks if the given ID is already used during sign-up.
     *
     * @param ID the ID to check.
     * @throws SQLException if a database access error occurs.
     * @throws IdAlreadyUsedException if the ID is already used.
     */
    public void singUpIDCheck(String ID) throws SQLException, IdAlreadyUsedException {
        query = String.format("SELECT `ID` FROM `Consumer_Accounts` WHERE `ID` = '%s';", ID);
        rs = stat.executeQuery(query);
        if (rs.next()) {
            throw new IdAlreadyUsedException();
        }
    }

    /**
     * Checks if the given password is already used.
     *
     * @param password the password to check.
     * @throws SQLException if a database access error occurs.
     * @throws PasswordAlreadyUsedException if the password is already used.
     */
    public void newPasswordCheck(String password) throws SQLException, PasswordAlreadyUsedException {
        query = String.format("SELECT `Password` FROM `Consumer_Accounts` WHERE `Password` = '%s';", password);
        rs = stat.executeQuery(query);
        if (rs.next()) {
            throw new PasswordAlreadyUsedException();
        }
    }

    /**
     * Checks if an account with the given ID exists.
     *
     * @param ID the ID to check.
     * @throws SQLException if a database access error occurs.
     * @throws AccountNotExistException if the account does not exist.
     */
    public void accountExistCheck(String ID) throws SQLException, AccountNotExistException {
        query = String.format("SELECT `ID` FROM `Consumer_Accounts` WHERE `ID` = '%s';", ID);
        rs = stat.executeQuery(query);
        if (!rs.next()) {
            throw new AccountNotExistException();
        }
    }
    
    /**
     * Retrieves the Username associated with the specified user ID from the database.
     *
     * @param ID the user ID for which the Username is to be retrieved
     * @return the Username associated with the specified user ID
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     */
    public String getUserName(String ID) throws SQLException {
        query = String.format("SELECT `Name` FROM `Consumer_Accounts` WHERE `ID` = '%s';", ID);
        rs = stat.executeQuery(query);
        rs.next();
        String name = rs.getString("Name");
        return name;
    }

    /**
     * Returns the ID for this connection.
     *
     * @return the ID.
     */
    public String getID() {
        return comsumerID;
    }
}
