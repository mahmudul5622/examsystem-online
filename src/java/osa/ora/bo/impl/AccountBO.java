package osa.ora.bo.impl;

import osa.ora.beans.UserProfile;
import osa.ora.dao.DAOFactory;
import osa.ora.exception.ApplicationException;
import osa.ora.spi.IAccount;
/*
 * @author Osama Oransa
 */

public class AccountBO implements IAccount{
    private static AccountBO accountBO = new AccountBO();
    private static IAccount accountDAO = DAOFactory.getAccountDAO();
    public static IAccount getAccountBOInstance() {
        return accountBO;
    }
    private AccountBO(){        
    }
    public UserProfile login(UserProfile userProfile) throws ApplicationException {
        return accountDAO.login(userProfile);
    }

    public UserProfile voucherAuthentication(UserProfile userProfile) throws ApplicationException {
        return accountDAO.voucherAuthentication(userProfile);
    }

    public void updateVoucherStatus(UserProfile userProfile) throws ApplicationException {
        accountDAO.updateVoucherStatus(userProfile);
    }
}

