package osa.ora.spi;

import java.util.Hashtable;
import osa.ora.beans.ExamBean;
import osa.ora.beans.UserProfile;
import osa.ora.exception.ApplicationException;
/*
 * @author Osama Oransa
 */

public interface IExam {
   public ExamBean[] loadNewExam (UserProfile userProfile) throws ApplicationException;
   public void saveExamAnswers (UserProfile userProfile,Hashtable<Integer,String> answers,int totalScore) throws ApplicationException;
   public void saveExamScoreOnly (UserProfile userProfile,int totalScore) throws ApplicationException;
}

