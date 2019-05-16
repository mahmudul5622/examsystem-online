package osa.ora.bo.impl;

import java.util.Hashtable;
import osa.ora.beans.Action;
import osa.ora.beans.Notification;
import osa.ora.dao.DAOFactory;
import osa.ora.exception.ApplicationException;
import osa.ora.spi.ICommon;
/*
 * @author Osama Oransa
 */

public class CommonBO implements ICommon{
    private static CommonBO instance=new CommonBO();
    private static ICommon commonDAO=DAOFactory.getCommonDAO();
	
    private CommonBO(){       
    }
    
    public static CommonBO getCommonBOInstance(){
        return instance;
    }
    
    public boolean auditAction(Action action)throws ApplicationException {
       return commonDAO.auditAction(action);
    }
    
    public Notification getNotification(int id)throws ApplicationException{
        return commonDAO.getNotification(id);
    }

    public Hashtable<String, String> geConfigurations() throws ApplicationException {
        return commonDAO.geConfigurations();
    }
}

