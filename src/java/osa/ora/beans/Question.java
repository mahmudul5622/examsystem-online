package osa.ora.beans;

import java.io.Serializable;

/*
 * @author Osama Oransa
 */
public class Question implements Serializable {
    private int id;
    private int type;
    private int multi_answer;
    private String answers;
    private String questionString;
    private byte[] questionData;
    private Options[] questionOptions;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the multi_answer
     */
    public int getMulti_answer() {
        return multi_answer;
    }

    /**
     * @param multi_answer the multi_answer to set
     */
    public void setMulti_answer(int multi_answer) {
        this.multi_answer = multi_answer;
    }

    /**
     * @return the answers
     */
    public String getAnswers() {
        return answers;
    }

    /**
     * @param answers the answers to set
     */
    public void setAnswers(String answers) {
        this.answers = answers;
    }

    /**
     * @return the questionData
     */
    public byte[] getQuestionData() {
        return questionData;
    }

    /**
     * @param questionData the questionData to set
     */
    public void setQuestionData(byte[] questionData) {
        this.questionData = questionData;
    }

    /**
     * @return the questionOptions
     */
    public Options[] getQuestionOptions() {
        return questionOptions;
    }

    /**
     * @param questionOptions the questionOptions to set
     */
    public void setQuestionOptions(Options[] questionOptions) {
        this.questionOptions = questionOptions;
    }

    /**
     * @return the questionString
     */
    public String getQuestionString() {
        return questionString;
    }

    /**
     * @param questionString the questionString to set
     */
    public void setQuestionString(String questionString) {
        this.questionString = questionString;
    }
    
}
