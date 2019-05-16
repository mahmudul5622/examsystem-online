package osa.ora.bd.impl;

import java.util.Hashtable;
import osa.ora.beans.ExamBean;
import osa.ora.beans.UserProfile;
import osa.ora.bo.BOFactory;
import osa.ora.exception.ApplicationException;
import osa.ora.spi.IExam;
/*
 * @author Osama Oransa
 */
public class ExamBD implements IExam{
    private static ExamBD examBD = new ExamBD();
    private static IExam examBO = BOFactory.getExamBO();
    public static IExam getExamBDInstance() {
        return examBD;
    }
    private ExamBD(){        
    }
    public ExamBean[] loadNewExam(UserProfile userProfile) throws ApplicationException {
        return examBO.loadNewExam(userProfile);
    }

    public void saveExamAnswers(UserProfile userProfile, Hashtable<Integer, String> answers, int totalScore) throws ApplicationException {
        examBO.saveExamAnswers(userProfile,answers,totalScore);
    }

    public void saveExamScoreOnly(UserProfile userProfile, int totalScore) throws ApplicationException {
        examBO.saveExamScoreOnly(userProfile,totalScore);
    }
}

