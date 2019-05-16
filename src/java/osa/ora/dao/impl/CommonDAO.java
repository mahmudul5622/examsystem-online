
package osa.ora.dao.impl;

import osa.ora.log.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import osa.ora.beans.Action;
import osa.ora.beans.Notification;
import osa.ora.dao.impl.helper.ConnectionManager;
import osa.ora.dao.impl.helper.MySQLQueries;
import osa.ora.exception.ApplicationException;
import osa.ora.exception.TechnicalException;
import osa.ora.spi.ICommon;
/*
 * @author Osama Oransa
 */
public class CommonDAO implements ICommon{

    private static CommonDAO commonDAO = new CommonDAO();
    private static Logger logger = Logger.getLogger("CommonDAO");
    private static ConnectionManager connectionManager=ConnectionManager.getConnectionManager();;
    /**
     * Singleton class method
     * @return instance of this DAO.
     */
    public static CommonDAO getCommonDAOInstance() {
        return commonDAO;
    }

    private CommonDAO() {                
    }
    /**
     * Add audit record in the auditing table for later reference like start exam , download exam , submit exam ,etc..
     * @param action : the action performed by the user
     * @return boolean if audit is added successfully or not.
     * @throws ApplicationException
     */
    public boolean auditAction(Action action) throws ApplicationException {
        boolean status = false;
        //should be remove , only to handle bad code...
        if (action == null) {
            status = false;
        }
        Connection connection = null;
        PreparedStatement auditActionPreparedStatement = null;
        try {
            connection = connectionManager.getConnection();
            auditActionPreparedStatement = connection.prepareStatement(MySQLQueries.AUDIT_TABLE);
            auditActionPreparedStatement.setString(1, action.getAction());
            auditActionPreparedStatement.setString(2, action.getSub_action());
            auditActionPreparedStatement.setInt(3, action.getPerformedBy());
            auditActionPreparedStatement.setString(4, action.getJustification());
            status = auditActionPreparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex);
        } finally {
            if (connection != null) {
                connectionManager.releaseConnection(connection);
            }
        }
        return status;
    }
    /**
     * Get email notification using notification ID
     * @param id : notification id
     * @return Notification object of that Id.
     * @throws ApplicationException
     */
    public Notification getNotification(int id) throws ApplicationException {
        if (Logger.isAllowDebugging()) {
            logger.debug("Enter in method: getNotification");
        }
        Connection connection = null;
        Notification notification = null;
        try {
            connection = connectionManager.getConnection();
            PreparedStatement getNotificationStatement = connection.prepareStatement(MySQLQueries.NOTIFICATION);
            getNotificationStatement.setInt(1, id);
            ResultSet resultSet = getNotificationStatement.executeQuery();
            if (resultSet.next()) {
                notification = new Notification();
                notification.setSubject(resultSet.getString("SUBJECT"));
                notification.setMessageBody(resultSet.getString("BODY"));
                notification.setNotificationId(id);
            }
            resultSet.close();
        } catch (SQLException ex) {
            throw new TechnicalException(ex);
        } finally {
            if (connection != null) {
                connectionManager.releaseConnection(connection);
            }
        }
        if (Logger.isAllowDebugging()) {
            logger.debug("Exit from method: getNotification");
        }
        return notification;
    }
    /**
     * Get configuration table values into single hashtable to be used in the application
     * @return configuration Hashtable
     * @throws ApplicationException
     */
    public Hashtable<String,String> geConfigurations() throws ApplicationException {
        if (Logger.isAllowDebugging()) {
            logger.debug("Enter in method: geConfigurations");
        }
        Connection connection = null;
        Hashtable<String,String> configurations = new Hashtable<String,String>();
        try {
            connection = connectionManager.getConnection();
            PreparedStatement configurationLoadStatement = connection.prepareStatement(MySQLQueries.CONFIGURATION);
            ResultSet resultSet = configurationLoadStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("CONFIG_NAME");
                String value = resultSet.getString("CONFIG_VALUE");
                configurations.put(name, value);
            }
            resultSet.close();
        } catch (SQLException ex) {
            throw new TechnicalException(ex);
        } finally {
            if (connection != null) {
                connectionManager.releaseConnection(connection);
            }
        }
        if (Logger.isAllowDebugging()) {
            logger.debug("Exit from method: geConfigurations");
        }
        return configurations;
    }

}
