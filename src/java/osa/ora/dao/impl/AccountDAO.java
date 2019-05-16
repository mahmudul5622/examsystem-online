
package osa.ora.dao.impl;

import osa.ora.log.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import osa.ora.beans.UserProfile;
import osa.ora.dao.impl.helper.ConnectionManager;
import osa.ora.dao.impl.helper.MySQLQueries;
import osa.ora.exception.ApplicationException;
import osa.ora.exception.BusinessException;
import osa.ora.exception.TechnicalException;
import osa.ora.spi.IAccount;
import osa.ora.spi.IConstant;
/*
 * @author Osama Oransa
 */
public class AccountDAO implements IAccount{
    private static AccountDAO accountDAO = new AccountDAO();
    private static Logger logger = Logger.getLogger("AccountDAO");
    private static ConnectionManager connectionManager=ConnectionManager.getConnectionManager();
    /**
     * Singleton class method
     * @return instance of this DAO.
     */
    public static AccountDAO getAccountDAOInstance() {
        return accountDAO;
    }

    private AccountDAO() {
    }
    /**
     * To login a user using voucher (password) and name (could be login name or email address)
     * @param userProfile
     * @return Loaded UserProfile
     * @throws ApplicationException
     */
    public UserProfile login(UserProfile userProfile) throws ApplicationException {
        if (Logger.isAllowDebugging()) {
            logger.debug("Enter in method: login");
        }
        Connection connection = null;
        PreparedStatement checkUserStatement = null;
        try {
            connection = connectionManager.getConnection();
            if(userProfile!=null && userProfile.getName()!=null && userProfile.getName().indexOf('@')==-1){
                checkUserStatement = connection.prepareStatement(MySQLQueries.LOAD_USERS_DATA+MySQLQueries.LOGIN_WHERE_NAME);
            }else{
                checkUserStatement = connection.prepareStatement(MySQLQueries.LOAD_USERS_DATA+MySQLQueries.LOGIN_WHERE_EMAIL);
            }
            checkUserStatement.setString(1, userProfile.getVoucher());
            checkUserStatement.setString(2, userProfile.getName());
            ResultSet resultSet = checkUserStatement.executeQuery();
            if(resultSet.next()){
                userProfile.setId(resultSet.getInt("ID"));
                userProfile.setEmail(resultSet.getString("EMAIL"));
                userProfile.setName(resultSet.getString("NAME"));
            }else{
                throw new BusinessException("Invalid Login!");
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        } finally {
            if (connection != null) {
                    connectionManager.releaseConnection(connection);
            }
        }
        if (Logger.isAllowDebugging()) {
            logger.debug("Exit from method: login");
        }
        return userProfile;
    }
    /**
     * Load user profile using a valid voucher
     * @param userProfile
     * @return UserProfile that match this voucher
     * @throws ApplicationException
     */
    public UserProfile voucherAuthentication(UserProfile userProfile) throws ApplicationException {
        if (Logger.isAllowDebugging()) {
            logger.debug("Enter in method: voucherAuthentication");
        }
        Connection connection = null;
        PreparedStatement loadUserDataStatement = null;
        try {
            connection = connectionManager.getConnection();
            loadUserDataStatement = connection.prepareStatement(MySQLQueries.LOAD_EXAM_TAKER_DATA_USING_VOUCHER);
            loadUserDataStatement.setString(1, userProfile.getVoucher());
            ResultSet resultSet = loadUserDataStatement.executeQuery();
            if(resultSet.next()){
                int status=resultSet.getInt("VOUCHER_STATUS");
                if(status>0){
                    throw new BusinessException("Invalid Voucher Status!");
                }
                userProfile.setId(resultSet.getInt("ID"));
                userProfile.setEmail(resultSet.getString("EMAIL"));
                userProfile.setName(resultSet.getString("NAME")+" "+resultSet.getString("LAST"));
                userProfile.setRoleId(resultSet.getInt("ROLE_ID"));
                userProfile.setLevelId(resultSet.getInt("LEVEL_ID"));
                userProfile.setResultsEmail(resultSet.getString("RESULTEMAIL"));
            }else{
                throw new BusinessException("Invalid Login!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex);
        } finally {
            if (connection != null) {
                    connectionManager.releaseConnection(connection);
            }
        }
        if (Logger.isAllowDebugging()) {
            logger.debug("Exit from method: voucherAuthentication");
        }
        return userProfile;
    }
    /**
     * Update voucher to be expired one after using it to download the exam
     * @param userProfile
     * @throws ApplicationException
     */
    public void updateVoucherStatus(UserProfile userProfile) throws ApplicationException {
        if (Logger.isAllowDebugging()) {
            logger.debug("Enter in method: updateVoucherStatus");
        }
        Connection connection = null;
        PreparedStatement updateVoucherStatusStatement = null;
        try {
            connection = connectionManager.getConnection();
            updateVoucherStatusStatement = connection.prepareStatement(MySQLQueries.UPDATE_VOUCHER_STATUS);
            updateVoucherStatusStatement.setInt(1, IConstant.VOUCHER_STATUS_USED);
            updateVoucherStatusStatement.setString(2, userProfile.getVoucher());
            int status= updateVoucherStatusStatement.executeUpdate();
            if(status==0){
                throw new BusinessException("Can't update voucher!");
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        } finally {
            if (connection != null) {
                    connectionManager.releaseConnection(connection);
            }
        }
        if (Logger.isAllowDebugging()) {
            logger.debug("Exit from method: updateVoucherStatus");
        }
    }

}
