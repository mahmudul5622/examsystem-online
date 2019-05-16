package osa.ora.spi;

import java.util.Hashtable;
import osa.ora.beans.Action;
import osa.ora.beans.Notification;
import osa.ora.exception.ApplicationException;
/*
 * @author Osama Oransa
 */

public interface ICommon {
    public boolean auditAction(Action action) throws ApplicationException;
    public Notification getNotification(int id) throws ApplicationException;
    public Hashtable<String,String> geConfigurations() throws ApplicationException;
}

