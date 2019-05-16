package osa.ora.beans;

import java.io.Serializable;

/*
 * @author Osama Oransa
 */
public class ExamBean implements Serializable{
    private int examId;
    private String examTitle;
    private int maxQuestions;
    private int score;
    private int examType;
    private Question[] questions;
    private int examDuration;

    /**
     * @return the examId
     */
    public int getExamId() {
        return examId;
    }

    /**
     * @param examId the examId to set
     */
    public void setExamId(int examId) {
        this.examId = examId;
    }

    /**
     * @return the examTitle
     */
    public String getExamTitle() {
        return examTitle;
    }

    /**
     * @param examTitle the examTitle to set
     */
    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    /**
     * @return the maxQuestions
     */
    public int getMaxQuestions() {
        return maxQuestions;
    }

    /**
     * @param maxQuestions the maxQuestions to set
     */
    public void setMaxQuestions(int maxQuestions) {
        this.maxQuestions = maxQuestions;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @return the questions
     */
    public Question[] getQuestions() {
        return questions;
    }

    /**
     * @param questions the questions to set
     */
    public void setQuestions(Question[] questions) {
        this.questions = questions;
    }

    /**
     * @return the examDuration
     */
    public int getExamDuration() {
        return examDuration;
    }

    /**
     * @param examDuration the examDuration to set
     */
    public void setExamDuration(int examDuration) {
        this.examDuration = examDuration;
    }

    /**
     * @return the examType
     */
    public int getExamType() {
        return examType;
    }

    /**
     * @param examType the examType to set
     */
    public void setExamType(int examType) {
        this.examType = examType;
    }

}
