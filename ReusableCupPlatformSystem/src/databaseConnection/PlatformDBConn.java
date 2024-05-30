package databaseConnection;

import java.sql.*;

/**
 * This class provides methods for platform database operations such as adding cups, signing up shops,
 * lending and receiving cups, querying cup holdings, changing cup statuses, and retrieving information about
 * consumers, shops, transactions, and cups.
 */
public class PlatformDBConn extends DatabaseUpdate {
    /**
     * Constructs a PlatformDBConn object and establishes a connection to the database.
     *
     * @throws SQLException if a database access error occurs.
     */
    public PlatformDBConn() throws SQLException {
        super();
    }

    /**
     * Adds a new cup to the database with the specified type and size.
     *
     * @param type the type of the cup.
     * @param size the size of the cup.
     * @throws SQLException if a database access error occurs.
     */
    public void addCup(String type, String size) throws SQLException {
        query = String.format(
                "INSERT INTO `Cups` (type, size, holder, holderID, status) VALUES ('%s', '%s', DEFAULT, DEFAULT, DEFAULT);",
                type, size);
        stat.execute(query);
    }

    /**
     * Signs up a new shop with the given ID, name, and activation password.
     *
     * @param ID the shop's ID.
     * @param name the shop's name.
     * @param activatePassword the shop's activation password.
     * @throws SQLException if a database access error occurs.
     * @throws IdAlreadyUsedException if the ID is already used.
     */
    public void shopSignUp(String ID, String name, String activatePassword)
            throws SQLException, IdAlreadyUsedException {
        signUpIDCheck(ID);
        query = String.format("INSERT INTO `Shop_Accounts` (ID, Name, Password) VALUES ('%s', '%s', '%s');", ID, name,
                activatePassword);
        stat.execute(query);
    }

    /**
     * Lends a cup to a shop with the specified cup ID and shop ID.
     *
     * @param cupID the ID of the cup.
     * @param ID the ID of the shop.
     * @throws SQLException if a database access error occurs.
     */
    public void lendCup(int cupID, String ID) throws SQLException {
        query = String.format("UPDATE `Cups` SET holder = 'Shop', holderID = '%s' WHERE ID = %d;", ID, cupID);
        stat.execute(query);
        updateTransactionRecord(cupID, "Platform", "Null", "Shop", ID);
    }

    /**
     * Receives a cup that was lent out, with the specified cup ID.
     *
     * @param cupID the ID of the cup.
     * @throws SQLException if a database access error occurs.
     */
    public void receiveCup(int cupID) throws SQLException {
        query = String.format("SELECT `holderID` FROM `Cups` WHERE `ID` = %d", cupID);
        rs = stat.executeQuery(query);
        rs.next();
        String holderID = rs.getString("holderID");

        query = String.format("UPDATE `Cups` SET holder = 'Platform', holderID = DEFAULT WHERE ID = %d;", cupID);
        stat.execute(query);

        changeStatus(cupID);
        updateTransactionRecord(cupID, "Shop", holderID, "Platform", "Null");
    }

    /**
     * Queries the cups currently held by the platform.
     *
     * @return a {@code String} listing the details of the cups held by the platform.
     * @throws SQLException if a database access error occurs.
     */
    public String queryCupsHolding() throws SQLException {
        String list = "";
        query = String.format("SELECT * FROM `Cups` WHERE `holderID` = '%s';", "Null");
        rs = stat.executeQuery(query);

        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            list += String.format("%-20s", metadata.getColumnName(i));
        }
        while (rs.next()) {
            list += String.format("\n%s\n", "-".repeat(140));
            String row = "";
            for (int i = 1; i <= columnCount; i++) {
                if (i == 1) {
                    row += String.format("%-20d", rs.getInt(i));
                } else {
                    row += String.format("%-20s", rs.getString(i));
                }
            }
            list += row;
        }
        return list;
    }

    /**
     * Changes the status of a cup to 'No' with the specified cup ID.
     *
     * @param cupID the ID of the cup.
     * @throws SQLException if a database access error occurs.
     */
    public void changeStatus(int cupID) throws SQLException {
        query = String.format("UPDATE `Cups` SET `status` = 'No' WHERE ID = '%d';", cupID);
        stat.execute(query);
    }

    /**
     * Retrieves information about all consumer accounts.
     *
     * @return a {@code String} listing the details of all consumer accounts.
     * @throws SQLException if a database access error occurs.
     */
    public String consumerInfo() throws SQLException {
        String list = "";
        query = String.format("SELECT * FROM Consumer_Accounts");
        rs = stat.executeQuery(query);

        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            list += String.format("%-20s", metadata.getColumnName(i));
        }
        while (rs.next()) {
            list += String.format("\n%s\n", "-".repeat(140));
            String row = "";
            for (int i = 1; i <= columnCount; i++) {
                row += String.format("%-20s", rs.getString(i));
            }
            list += row;
        }
        return list;
    }

    /**
     * Retrieves information about all shop accounts.
     *
     * @return a {@code String} listing the details of all shop accounts.
     * @throws SQLException if a database access error occurs.
     */
    public String shopInfo() throws SQLException {
        String list = "";
        query = String.format("SELECT * FROM Shop_Accounts");
        rs = stat.executeQuery(query);

        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            list += String.format("%-20s", metadata.getColumnName(i));
        }
        while (rs.next()) {
            list += String.format("\n%s\n", "-".repeat(140));
            String row = "";
            for (int i = 1; i <= columnCount; i++) {
                if (i != columnCount) {
                    row += String.format("%-20s", rs.getString(i));
                } else {
                    row += String.format("%-20d", rs.getInt(i));
                }
            }
            list += row;
        }
        return list;
    }

    /**
     * Retrieves information about all transaction records.
     *
     * @return a {@code String} listing the details of all transaction records.
     * @throws SQLException if a database access error occurs.
     */
    public String transInfo() throws SQLException {
        String list = "";
        query = String.format("SELECT * FROM Transaction_Records");
        rs = stat.executeQuery(query);

        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        for (int i = 1; i < columnCount; i++) {
            list += String.format("%-20s", metadata.getColumnName(i));
        }
        while (rs.next()) {
            list += String.format("\n%s\n", "-".repeat(140));
            String row = "";
            for (int i = 1; i < columnCount; i++) {
                if (i == 1 || i == 2) {
                    row += String.format("%-20d", rs.getInt(i));
                } else {
                    row += String.format("%-20s", rs.getString(i));
                }
            }
            list += row;
        }
        return list;
    }

    /**
     * Retrieves information about all cups.
     *
     * @return a {@code String} listing the details of all cups.
     * @throws SQLException if a database access error occurs.
     */
    public String cupInfo() throws SQLException {
        String list = "";
        query = String.format("SELECT * FROM Cups");
        rs = stat.executeQuery(query);

        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            list += String.format("%-20s", metadata.getColumnName(i));
        }
        while (rs.next()) {
            list += String.format("\n%s\n", "-".repeat(140));
            String row = "";
            for (int i = 1; i <= columnCount; i++) {
                if (i == 1) {
                    row += String.format("%-20d", rs.getInt(i));
                } else {
                    row += String.format("%-20s", rs.getString(i));
                }
            }
            list += row;
        }
        return list;
    }
    
    /**
     * Checks if the given ID is already used in the Shop_Accounts table.
     *
     * @param ID the ID to check.
     * @return true if the ID is already used, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean signUpIDCheck(String ID) throws SQLException {
        query = "SELECT ID FROM Shop_Accounts WHERE ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, ID);
            rs = pstmt.executeQuery();
            return rs.next(); //If the query result exists, return true, otherwise return false
        }
    }

}
