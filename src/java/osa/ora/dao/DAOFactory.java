package osa.ora.dao;

import osa.ora.dao.impl.AccountDAO;
import osa.ora.dao.impl.CommonDAO;
import osa.ora.dao.impl.ExamDAO;
import osa.ora.spi.IAccount;
import osa.ora.spi.ICommon;
import osa.ora.spi.IExam;
/*
 * @author Osama Oransa
 */

public class DAOFactory{
    public static ICommon getCommonDAO(){
        return CommonDAO.getCommonDAOInstance();
    }

    public static IAccount getAccountDAO(){
        return AccountDAO.getAccountDAOInstance();
    }
    public static IExam getExamDAO(){
        return ExamDAO.getExamDAOInstance();
    }
}