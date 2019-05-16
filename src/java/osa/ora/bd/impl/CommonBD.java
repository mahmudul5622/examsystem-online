package osa.ora.bd.impl;

import java.util.Hashtable;
import osa.ora.beans.Action;
import osa.ora.beans.Notification;
import osa.ora.bo.BOFactory;
import osa.ora.exception.ApplicationException;
import osa.ora.spi.ICommon;
/*
 * @author Osama Oransa
 */
public class CommonBD implements ICommon {

    private static CommonBD instance=new CommonBD();
    private static ICommon commonBO=BOFactory.getCommonBO();
    public static ICommon getCommonBDInstance() {
        return instance;
    }
    private CommonBD () {         
    }
    
    public boolean auditAction(Action action)throws ApplicationException {
       return commonBO.auditAction(action);
    }
    
    public Notification getNotification(int id)throws ApplicationException{
        return commonBO.getNotification(id);
    }

    public Hashtable<String, String> geConfigurations() throws ApplicationException {
        return commonBO.geConfigurations();
    }
    
}

