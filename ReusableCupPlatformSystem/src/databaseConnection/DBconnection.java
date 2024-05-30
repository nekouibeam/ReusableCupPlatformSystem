package databaseConnection;

import java.sql.SQLException;

/**
 * This interface defines the methods required for database connection operations.
 */
public interface DBconnection {
    /**
     * The server URL for the database connection.
     */
    String server = "jdbc:mysql://140.119.19.73:3315/";
    
    /**
     * The name of the database.
     */
    String database = "112306069";
    
    /**
     * The full URL for the database connection.
     */
    String url = server + database + "?useSSL=false";
    
    /**
     * The username for the database connection.
     */
    String username = "112306069"; 
    
    /**
     * The password for the database connection.
     */
    String password = "06bz8"; 
    
    /**
     * Executes a query to retrieve information about cups holding.
     *
     * @return a {@code String} containing the result of the query.
     * @throws SQLException if a database access error occurs.
     */
    String queryCupsHolding() throws SQLException;
}
