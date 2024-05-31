package databaseConnection;

import java.sql.*;

/**
 * This class provides methods for shop database operations such as activating accounts, logging in,
 * changing passwords and names, lending and receiving cups, and querying cup holdings.
 */
public class ShopDBConn extends DatabaseUpdate {

    private String shopID;

    /**
     * Constructs a ShopDBConn object and establishes a connection to the database.
     *
     * @throws SQLException if a database access error occurs.
     */
    public ShopDBConn() throws SQLException {
        super();
    }

    /**
     * Activates a shop account with the given ID and activation password, then sets a new password.
     *
     * @param ID the shop ID.
     * @param activatePassword the activation password.
     * @param newPassword the new password to set.
     * @throws SQLException if a database access error occurs.
     * @throws AccountNotExistException if the account does not exist.
     * @throws PasswordAlreadyUsedException if the new password is already used.
     * @throws PasswordWrongException if the activation password is incorrect.
     * @throws IdCantEmptyException if the ID is empty.
     * @throws InitialPasswordCantEmptyException if the activation password is empty.
     * @throws PasswordCantEmptyException if the new password is empty.
     */
    public void activateAccount(String ID, String activatePassword, String newPassword)
            throws SQLException, AccountNotExistException, PasswordAlreadyUsedException, PasswordWrongException, IdCantEmptyException, InitialPasswordCantEmptyException, PasswordCantEmptyException {
        if (ID == null || ID.trim().isEmpty()) {
            throw new IdCantEmptyException();
        } else if (activatePassword == null || activatePassword.trim().isEmpty()) {
            throw new InitialPasswordCantEmptyException();
        } else if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new PasswordCantEmptyException();
        }
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

    /**
     * Logs in a shop account with the given ID and password.
     *
     * @param ID the shop ID.
     * @param password the shop password.
     * @throws SQLException if a database access error occurs.
     * @throws PasswordWrongException if the password is incorrect.
     * @throws NotActivateException if the account is not activated.
     * @throws AccountNotExistException if the account does not exist.
     * @throws IdCantEmptyException if the ID is empty.
     * @throws PasswordCantEmptyException if the password is empty.
     */
    public void login(String ID, String password)
            throws SQLException, PasswordWrongException, NotActivateException, AccountNotExistException, IdCantEmptyException, PasswordCantEmptyException {
        if (ID == null || ID.trim().isEmpty()) {
            throw new IdCantEmptyException();
        } else if (password == null || password.trim().isEmpty()) {
            throw new PasswordCantEmptyException();
        }
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

    /**
     * Changes the password for a shop account with the given ID.
     *
     * @param ID the shop ID.
     * @param newPassword the new password to set.
     * @throws SQLException if a database access error occurs.
     * @throws PasswordAlreadyUsedException if the new password is already used.
     */
    public void changePassword(String ID, String newPassword) throws SQLException, PasswordAlreadyUsedException {
        newPasswordCheck(newPassword);
        query = String.format("UPDATE `Shop_Accounts` SET `Password` = '%s' WHERE ID = '%s';", newPassword, ID);
        stat.execute(query);
    }

    /**
     * Changes the name for a shop account with the given ID.
     *
     * @param ID the shop ID.
     * @param newName the new name to set.
     * @throws SQLException if a database access error occurs.
     */
    public void changeName(String ID, String newName) throws SQLException {
        query = String.format("UPDATE `Shop_Accounts` SET `Name` = '%s' WHERE ID = %s;", newName, ID);
        stat.execute(query);
    }

    /**
     * Lends a cup to a consumer with the given cup ID and consumer ID.
     *
     * @param cupID the ID of the cup.
     * @param ID the ID of the consumer.
     * @throws SQLException if a database access error occurs.
     * @throws AccountNotExistException if the consumer account does not exist.
     */
    public void lendCup(int cupID, String ID) throws SQLException, AccountNotExistException {
        consumerExistCheck(ID);
        query = String.format("UPDATE `Cups` SET holder = 'Consumer', holderID = '%s', status = 'Yes' WHERE ID = %d;",
                ID, cupID);
        stat.execute(query);
        updateTransactionRecord(cupID, "Store", shopID, "Consumer", ID);
    }

    /**
     * Receives a cup from a consumer with the given cup ID.
     *
     * @param cupID the ID of the cup.
     * @throws SQLException if a database access error occurs.
     */
    public void receiveCup(int cupID) throws SQLException {
        query = String.format("SELECT `holderID` FROM `Cups` WHERE `ID` = %d", cupID);
        rs = stat.executeQuery(query);
        rs.next();
        String holderID = rs.getString("holderID");

        query = String.format("UPDATE `Cups` SET holder = 'Shop', holderID = '%s' WHERE ID = %d;", shopID, cupID);
        stat.execute(query);

        updateTransactionRecord(cupID, "Consumer", holderID, "Store", shopID);
    }

    /**
     * Queries the cups currently held by the shop.
     *
     * @return a {@code String} listing the details of the cups held by the shop.
     * @throws SQLException if a database access error occurs.
     */
    public String queryCupsHolding() throws SQLException {
        String list = "";
        query = String.format("SELECT * FROM `Cups` WHERE `holderID` = '%s';", shopID);
        rs = stat.executeQuery(query);

        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            list += String.format("%-20s", metadata.getColumnName(i));
        }
        while (rs.next()) {
            list += String.format("\n%s\n", "-".repeat(70));
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
     * Sets the shop ID for this connection.
     *
     * @param ID the shop ID.
     */
    public void setID(String ID) {
        this.shopID = ID;
    }

    /**
     * Sets the activation status of a shop account to activated.
     *
     * @param ID the shop ID.
     * @throws SQLException if a database access error occurs.
     */
    public void setActivate(String ID) throws SQLException {
        query = String.format("UPDATE `Shop_Accounts` SET `Activate` = '1' WHERE ID = '%s';", ID);
        stat.execute(query);
    }

    /**
     * Checks if a shop account with the given ID is activated.
     *
     * @param ID the shop ID.
     * @throws SQLException if a database access error occurs.
     * @throws NotActivateException if the account is not activated.
     */
    public void checkActivate(String ID) throws SQLException, NotActivateException {
        query = String.format("SELECT `ID` FROM `Shop_Accounts` WHERE `ID` = '%s' AND `Activate` = 1;", ID);
        rs = stat.executeQuery(query);
        if (!rs.next()) {
            throw new NotActivateException();
        }
    }

    /**
     * Checks if the new password is already used by another account.
     *
     * @param password the new password to check.
     * @throws SQLException if a database access error occurs.
     * @throws PasswordAlreadyUsedException if the password is already used.
     */
    public void newPasswordCheck(String password) throws SQLException, PasswordAlreadyUsedException {
        query = String.format("SELECT `Password` FROM `Shop_Accounts` WHERE `Password` = '%s';", password);
        rs = stat.executeQuery(query);
        if (rs.next()) {
            throw new PasswordAlreadyUsedException();
        }
    }

    /**
     * Checks if a shop account with the given ID exists.
     *
     * @param ID the shop ID to check.
     * @throws SQLException if a database access error occurs.
     * @throws AccountNotExistException if the account does not exist.
     */
    public void accountExistCheck(String ID) throws SQLException, AccountNotExistException {
        query = String.format("SELECT `ID` FROM `Shop_Accounts` WHERE `ID` = '%s';", ID);
        rs = stat.executeQuery(query);
        if (!rs.next()) {
            throw new AccountNotExistException();
        }
    }

    /**
     * Checks if a consumer account with the given ID exists.
     *
     * @param ID the consumer ID to check.
     * @throws SQLException if a database access error occurs.
     * @throws AccountNotExistException if the account does not exist.
     */
    public void consumerExistCheck(String ID) throws SQLException, AccountNotExistException {
        query = String.format("SELECT `ID` FROM `Consumer_Accounts` WHERE `ID` = '%s';", ID);
        rs = stat.executeQuery(query);
        if (!rs.next()) {
            throw new AccountNotExistException();
        }
    }
    
    /**
     * Returns the shop ID for this connection.
     *
     * @return the shop ID.
     */
    public String getShopID() {
        return shopID;
    }
}
