package osa.ora.bd.impl;

import osa.ora.beans.UserProfile;
import osa.ora.bo.BOFactory;
import osa.ora.exception.ApplicationException;
import osa.ora.spi.IAccount;
/*
 * @author Osama Oransa
 */
public class AccountBD implements IAccount{
    private static AccountBD instance = new AccountBD();
    private static IAccount accountBO = BOFactory.getAccountBO();
    public static IAccount getAccountBDInstance() {
        return instance;
    }
    private AccountBD(){        
    }
    public UserProfile login(UserProfile userProfile) throws ApplicationException{
        return accountBO.login(userProfile);
    }

    public UserProfile voucherAuthentication(UserProfile userProfile) throws ApplicationException {
        return accountBO.voucherAuthentication(userProfile);
    }

    public void updateVoucherStatus(UserProfile userProfile) throws ApplicationException {
        accountBO.updateVoucherStatus(userProfile);
    }
}

