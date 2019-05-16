package osa.ora.bo.impl;

import java.util.Hashtable;
import osa.ora.beans.ExamBean;
import osa.ora.beans.UserProfile;
import osa.ora.dao.DAOFactory;
import osa.ora.exception.ApplicationException;
import osa.ora.spi.IExam;
/*
 * @author Osama Oransa
 */

public class ExamBO implements IExam{
    private static ExamBO examBO = new ExamBO();
    private static IExam examDAO = DAOFactory.getExamDAO();
    public static IExam getExamBOInstance() {
        return examBO;
    }
    private ExamBO(){        
    }
    public ExamBean[] loadNewExam(UserProfile userProfile) throws ApplicationException {
        return examDAO.loadNewExam(userProfile);
    }

    public void saveExamAnswers(UserProfile userProfile, Hashtable<Integer, String> answers, int totalScore) throws ApplicationException {
        //we don't want both to be in same transactions so if exam answers failed do not roll back the total score
        examDAO.saveExamScoreOnly(userProfile,totalScore);
        examDAO.saveExamAnswers(userProfile,answers,totalScore);
    }

    public void saveExamScoreOnly(UserProfile userProfile, int totalScore) throws ApplicationException {
        examDAO.saveExamScoreOnly(userProfile,totalScore);
    }
}

