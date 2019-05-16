package osa.ora.dao.impl.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import osa.ora.exception.TechnicalException;

/**
 * @author Osama
 * Class contain methods that related to tasks deal with data base
 */
public class DBSequenceHelper {

    private static final int ACCOUNT_TABLE=1;
    /**
     * 
     * @param tableId: selected from static final integers
     * @return
     * @throws TechnicalException
     */
    private static int nextId(int tableId) throws TechnicalException {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnectionManager().getConnection();
            ResultSet resultSet = null;
            PreparedStatement read = connection.prepareStatement("SELECT SEQ_VALUE FROM AUTOGEN WHERE ID=?");
            PreparedStatement update = connection.prepareStatement("UPDATE AUTOGEN SET SEQ_VALUE=? WHERE ID=?");
            int result = 0;
            read.setInt(1, tableId);
            resultSet = read.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
            update.setInt(1, result + 1);
            update.setInt(2, tableId);
            update.executeUpdate();
            return result;
        } catch (SQLException ex) {
            throw new TechnicalException("Sorry, get next ID DB exception: " + ex.getMessage(), ex);
        } finally {
            if (connection != null) {
                    ConnectionManager.getConnectionManager().releaseConnection(connection);
            }
        }
    }
    /**
     * Get the
     * @param connection
     * @return
     * @throws java.sql.SQLException
     * @auther VooDoo
     */
    public static int getNextAccountID() throws TechnicalException {
        Object synObject = new Object();
        int result = 0;
        synchronized (synObject) {
            result = nextId(ACCOUNT_TABLE);
        }
        return result;
    }        
}
