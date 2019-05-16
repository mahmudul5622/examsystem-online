package osa.ora.dao.impl.helper;

import java.sql.Connection;
import java.sql.SQLException;

import osa.ora.exception.TechnicalException;
import osa.ora.log.Logger;
/**
 * @author Osama Oransa
 */
public class DBSession {

    private static Logger logger = Logger.getLogger("DBSession");
    private Connection connection = null;
    private ConnectionManager connectionManager;

    public DBSession() throws TechnicalException {
        connectionManager = ConnectionManager.getConnectionManager();

        if (connection == null) {
            connection = connectionManager.getConnection();
            try {
                connection.setAutoCommit(false);
            } catch (SQLException ex) {
                rollBack();
                throw new TechnicalException("Error While Set Autocommit = false:  " + ex.getMessage(), ex);
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void commitTransaction() throws TechnicalException {

        try {
            if (connection != null && !connection.isClosed()) {
                connection.commit();
            }
        } catch (SQLException ex) {
            logger.error("Error occured while commiting transaction: " + ex.getMessage());
            rollBack();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connectionManager.releaseConnection(connection);
                }
            } catch (SQLException ex) {
                throw new TechnicalException("Error While Commiting " + ex.getMessage(), ex);
            }

        }

    }

    public void rollBack() throws TechnicalException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
            }
        } catch (SQLException ex) {

            throw new TechnicalException("Error While Roll back Transaction:  " + ex.getMessage(), ex);
        } finally {

            if (connection != null) {
                connectionManager.releaseConnection(connection);
            }

        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (connection != null && !connection.isClosed()) {
            connectionManager.releaseConnection(connection);
        }
    }
}
