package osa.ora.bd;

import osa.ora.bd.impl.AccountBD;
import osa.ora.bd.impl.CommonBD;
import osa.ora.bd.impl.ExamBD;
import osa.ora.spi.IAccount;
import osa.ora.spi.ICommon;
import osa.ora.spi.IExam;
/*
 * @author Osama Oransa
 */
public class BDFactory{
    public static ICommon getCommonBD() {
        return CommonBD.getCommonBDInstance();
    } 
    public static IAccount getAccountBD(){
        return AccountBD.getAccountBDInstance();
    }    
    public static IExam getExamBD(){
        return ExamBD.getExamBDInstance();
    }
}