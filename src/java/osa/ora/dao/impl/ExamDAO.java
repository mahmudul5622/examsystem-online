
package osa.ora.dao.impl;

import java.util.Hashtable;
import osa.ora.beans.ExamBean;
import osa.ora.log.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import osa.ora.beans.Options;
import osa.ora.beans.Question;
import osa.ora.beans.UserProfile;
import osa.ora.dao.impl.helper.ConnectionManager;
import osa.ora.dao.impl.helper.MySQLQueries;
import osa.ora.exception.ApplicationException;
import osa.ora.exception.TechnicalException;
import osa.ora.spi.IExam;
/*
 * @author Osama Oransa
 */
public class ExamDAO implements IExam{
    private static ExamDAO examDAO = new ExamDAO();
    private static Logger logger = Logger.getLogger("ExamDAO");
    private static ConnectionManager connectionManager=ConnectionManager.getConnectionManager();
    /**
     * Singleton class method
     * @return instance of this DAO.
     */
    public static ExamDAO getExamDAOInstance() {
        return examDAO;
    }

    private ExamDAO() {
    }
    /**
     * Method to load the exam using userprofile
     * @param userProfile
     * @return fully loaded exam
     * @throws ApplicationException
     */
    public ExamBean[] loadNewExam(UserProfile userProfile) throws ApplicationException {
        if (Logger.isAllowDebugging()) {
            logger.debug("Enter in method: loadNewExam");
        }
        Connection connection = null;
        PreparedStatement loadExamData = null;
        ArrayList<ExamBean> exams=new ArrayList<ExamBean>();
        try {
            connection = connectionManager.getConnection();
            loadExamData = connection.prepareStatement(MySQLQueries.LOAD_EXAM_DATA);
            loadExamData.setInt(1, userProfile.getLevelId());
            loadExamData.setInt(2, userProfile.getRoleId());
            ResultSet resultSet = loadExamData.executeQuery();
            while(resultSet.next()){
                ExamBean examBean=new ExamBean();
                examBean.setExamId(resultSet.getInt("ID"));
                examBean.setExamTitle(resultSet.getString("EXAM_TITLE"));
                examBean.setMaxQuestions(resultSet.getInt("NO_OF_QUESTIONS"));
                examBean.setExamType(resultSet.getInt("EXAM_TYPE_ID"));
                examBean.setExamDuration(resultSet.getInt("EXAM_DURATION"));
                //load exam questions
                examBean.setQuestions(loadExamQuestions(examBean.getExamId(),examBean.getMaxQuestions()));
                exams.add(examBean);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                    connectionManager.releaseConnection(connection);
            }
        }
        if (Logger.isAllowDebugging()) {
            logger.debug("Exit from method: loadNewExam");
        }
        return exams.toArray(new ExamBean[0]);
    }
    /**
     * load exam questions using exam Id and max questions for this exam  (in case no of questions in DB > max exam questions)
     * @param examId
     * @param maxQuestions
     * @return Question[] of that exam
     * @throws ApplicationException
     */
    private Question[] loadExamQuestions(int examId,int maxQuestions) throws ApplicationException {
        if (Logger.isAllowDebugging()) {
            logger.debug("Enter in method: loadExamQuestions for exam="+examId);
        }
        Connection connection = null;
        PreparedStatement loadExamData = null;
        ArrayList<Question> questions=new ArrayList<Question>();
        try {
            connection = connectionManager.getConnection();
            loadExamData = connection.prepareStatement(MySQLQueries.LOAD_EXAM_QUESTIONS_ALL);
            loadExamData.setInt(1, examId);
            loadExamData.setInt(2, maxQuestions);
            ResultSet resultSet = loadExamData.executeQuery();
            while(resultSet.next()){
                Question question=new Question();
                question.setId(resultSet.getInt("ID"));
                question.setType(resultSet.getInt("CONTENT_TYPE_ID"));
                if(question.getType()==1){
                    String str = new String(resultSet.getBytes("QUESTION_DATA"),"UTF8");
                    str = str.replaceAll("\n", "<br>");
                    question.setQuestionString(str);
                }else{
                    question.setQuestionData(resultSet.getBytes("QUESTION_DATA"));
                }
                question.setMulti_answer(resultSet.getInt("MULTI_ANSWER"));
                question.setAnswers(resultSet.getString("CORRECT_ANSWERS"));
                question.setType(resultSet.getInt("CONTENT_TYPE_ID"));
                int numberOfOptions=resultSet.getInt("NO_OF_OPTIONS");
                question.setQuestionOptions(loadQuestionOptions(question.getId(),numberOfOptions));
                questions.add(question);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                    connectionManager.releaseConnection(connection);
            }
        }
        if (Logger.isAllowDebugging()) {
            logger.debug("Exit from method: loadExamQuestions");
        }
        return questions.toArray(new Question[0]);
    }
    /**
     * load question available options using question id and max options for this questions (in case DB contain more than max options for this question)
     * @param questionId
     * @param maxOptions
     * @return Options[] of that question
     * @throws ApplicationException
     */
    private Options[] loadQuestionOptions(int questionId,int maxOptions) throws ApplicationException {
        if (Logger.isAllowDebugging()) {
            logger.debug("Enter in method: loadQuestionOptions for question="+questionId);
        }
        Connection connection = null;
        PreparedStatement loadQuestionOptions = null;
        ArrayList<Options> options=new ArrayList<Options>();
        try {
            connection = connectionManager.getConnection();
            loadQuestionOptions = connection.prepareStatement(MySQLQueries.LOAD_QUESTIONS_OPTIONS);
            loadQuestionOptions.setInt(1, questionId);
            loadQuestionOptions.setInt(2, maxOptions);
            ResultSet resultSet = loadQuestionOptions.executeQuery();
            while(resultSet.next()){
                Options option=new Options();
                option.setOptionId(resultSet.getInt("SEQUENCE"));
                option.setOptionValue(resultSet.getString("OPTION_VALUE"));
                options.add(option);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                    connectionManager.releaseConnection(connection);
            }
        }
        if (Logger.isAllowDebugging()) {
            logger.debug("Exit from method: loadQuestionOptions");
        }
        return options.toArray(new Options[0]);
    }

    public void saveExamAnswers(UserProfile userProfile, Hashtable<Integer, String> answers, int totalScore) throws ApplicationException {
        Connection connection = null;
        PreparedStatement saveExamScoreStatement = null;
        try {
            connection = connectionManager.getConnection();
            Enumeration<Integer> keys=answers.keys();
            saveExamScoreStatement = connection.prepareStatement(MySQLQueries.SAVE_EXAM_ANSWERS);
            while(keys.hasMoreElements()){
                Integer current=keys.nextElement();
                String answer=answers.get(current);
                saveExamScoreStatement.setInt(1, userProfile.getId());
                saveExamScoreStatement.setInt(2, current);
                saveExamScoreStatement.setString(3, answer);
                saveExamScoreStatement.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex);
        } finally {
            if (connection != null) {
                connectionManager.releaseConnection(connection);
            }
        }
    }

    public void saveExamScoreOnly(UserProfile userProfile, int totalScore) throws ApplicationException {
        Connection connection = null;
        PreparedStatement saveExamScoreStatement = null;
        try {
            connection = connectionManager.getConnection();
            saveExamScoreStatement = connection.prepareStatement(MySQLQueries.SAVE_EXAM_SCORE);
            saveExamScoreStatement.setInt(1, totalScore);
            saveExamScoreStatement.setInt(2, userProfile.getId());
            if(saveExamScoreStatement.executeUpdate() > 0){
                logger.debug("Score saved successfully");
            }else{
                logger.warn("Unable to save user score!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex);
        } finally {
            if (connection != null) {
                connectionManager.releaseConnection(connection);
            }
        }
    }

}
