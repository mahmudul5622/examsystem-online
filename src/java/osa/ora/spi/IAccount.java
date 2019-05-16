package osa.ora.spi;

import osa.ora.beans.UserProfile;
import osa.ora.exception.ApplicationException;
/*
 * @author Osama Oransa
 */

public interface IAccount {
   public UserProfile login (UserProfile userProfile) throws ApplicationException;
   public UserProfile voucherAuthentication (UserProfile userProfile) throws ApplicationException;
   public void updateVoucherStatus(UserProfile userProfile) throws ApplicationException;
}

