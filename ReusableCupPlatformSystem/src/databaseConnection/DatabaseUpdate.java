package databaseConnection;

import java.sql.*;

/**
 * This abstract class provides the implementation for common database update operations 
 * and defines methods to be implemented for specific actions such as lending and receiving cups.
 */
public abstract class DatabaseUpdate extends SignupAndLoginExceptions implements DBconnection {
    /**
     * Connection to the database.
     */
    protected Connection conn;
    
    /**
     * Statement for executing SQL queries.
     */
    protected Statement stat;
    
    /**
     * ResultSet for storing query results.
     */
    protected ResultSet rs;
    
    /**
     * SQL query string.
     */
    protected String query;

    /**
     * Constructs a DatabaseUpdate object and establishes a connection to the database.
     *
     * @throws SQLException if a database access error occurs.
     */
    public DatabaseUpdate() throws SQLException {
        conn = DriverManager.getConnection(url, username, password);
        stat = conn.createStatement();
    }

    /**
     * Lends a cup to a user.
     *
     * @param cupID the ID of the cup to lend.
     * @param ID the ID of the user borrowing the cup.
     * @throws SQLException if a database access error occurs.
     * @throws AccountNotExistException if the account does not exist.
     */
    abstract public void lendCup(int cupID, String ID) throws SQLException, AccountNotExistException;

    /**
     * Receives a cup that was lent out.
     *
     * @param cupID the ID of the cup being returned.
     * @throws SQLException if a database access error occurs.
     */
    abstract public void receiveCup(int cupID) throws SQLException;

    /**
     * Updates the transaction record with the details of a cup transaction.
     *
     * @param cupID the ID of the cup involved in the transaction.
     * @param from the source of the transaction.
     * @param fromID the ID of the source.
     * @param to the destination of the transaction.
     * @param toID the ID of the destination.
     * @throws SQLException if a database access error occurs.
     */
    public void updateTransactionRecord(int cupID, String from, String fromID, String to, String toID)
            throws SQLException {
        query = String.format(
                "INSERT INTO `Transaction_Records` (cupID, From_, FromID, To_, ToID, Time) VALUES (%d,'%s', '%s', '%s', '%s', DEFAULT);",
                cupID, from, fromID, to, toID);
        stat.execute(query);
    }
}
