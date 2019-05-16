package osa.ora.spi;
/*
 * @author Osama Oransa
 */

public interface IConstant {
    //Data source JNDI name
    public static final String DATA_SOURCE_NAME = "jdbc/exam";
    //Possible voucher status
    public static final int VOUCHER_STATUS_USED = 1;
    //Possible examinee actions...
    public static final String CHECK_EXAM_EXIST="1";
    public static final String DOWNLOAD_NEW_EXAM="2";
    public static final String DOWNLOAD_EXISTING_EXAM="3";
    public static final String UPDATE_EXAM_TIME="4";
    public static final String GET_EXAM_TIME="5";
    public static final String SUBMIT_SINGLE_EXAM_ANSWER="6";
    public static final String SUBMIT_EXAM_OR_EXAM_TIME_END="7";
    public static final String GET_ALL_EXAM_ANSWERS="8";
    public static final String RENDER_IMAGE="9";
    public static final String GET_EXAM_NO_OF_QUESTIONS_PER_PAGE="10";
    public static final String REQUEST_EXAM_PAUSE="11";
    //configuration constants
    public static final String SEND_EXAMINER_SCORE="SEND_EXAMINER_SCORE";
    public static final String SEND_EXAMINEE_SCORE="SEND_EXAMINEE_SCORE";
    public static final String SAVE_SCORE_ONLY="SAVE_SCORE_ONLY";
    public static final String SHOW_SCORE="SHOW_SCORE";
    public static final String QUESTIONS_PER_PAGE="QUESTIONS_PER_PAGE";
    public static final String EMAIL_SERVER="EMAIL_SERVER";
    public static final String EMAIL_PORT="EMAIL_PORT";
    public static final String EMAIL_SSL="EMAIL_SSL";
    public static final String EMAIL_USER="EMAIL_USER";
    public static final String EMAIL_PASSWORD="EMAIL_PASSWORD";
    public static final String TRUE="TRUE";
    //request & session scoped variables
    public static final String EXAM_DETAILS="EXAM_DETAILS";
    public static final String PROFILE="PROFILE";
    public static final String TIME="TIME";
    public static final String ANSWERS="ANSWERS";
    public static final String PAUSES="PAUSES";
    public static final String VOUCHER="voucher";
    public static final String ID="id";
    public static final String VALUE="value";
    public static final String ACTION="action";
    //possible return values
    public static final String NO="NO";
    public static final String YES="YES";
    public static final String LAST="LAST";
    public static final String OK="OK";
    public static final String DONE="DONE";
    public static final String SCORE="SCORE: ";
    //possible audit actions
    public static final String DOWNLOAD_NEW_EXAM_ACTION="DOWNLOAD NEW EXAM";
    public static final String SUBMIT_EXAM_ACTION="SUBMIT EXAM";
    //notification templates
    public static final int EMAIL_TEMPLATE_FOR_EXAMINER=1;
    public static final int EMAIL_TEMPLATE_FOR_EXAMINEE=2;
}

