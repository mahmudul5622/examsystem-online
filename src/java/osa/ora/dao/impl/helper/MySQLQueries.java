/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package osa.ora.dao.impl.helper;

/**
 *
 * @author ooransa
 */
public interface MySQLQueries {
    public static final String LOAD_USERS_DATA = "SELECT ID,EMAIL,NAME FROM exam.account where password=? and ";
    public static final String LOGIN_WHERE_NAME = " name=?";
    public static final String LOGIN_WHERE_EMAIL = " email=?";
    public static final String LOAD_EXAM_DATA="SELECT ID,EXAM_TITLE,NO_OF_QUESTIONS, EXAM_TYPE_ID,EXAM_DURATION FROM exam.exam "+
                                    "where ID in (select a.exam_id from exam_to_collection a ,exam_collections b "+
                                    "where a.collection_id=b.id and years_of_exp_id=? and exam_role_id=?)";
    public static final String LOAD_EXAM_QUESTIONS_NO_IMAGES="SELECT id,IF(content_type_id=1,question_data,null),no_of_options,correct_answers,content_type_id,multi_answer "+
                                    "FROM questions q,exam_questions eq where q.id=eq.question_id and exam_id=? limit ?";
    public static final String LOAD_EXAM_QUESTIONS_ALL="SELECT id,question_data,no_of_options,correct_answers,content_type_id,multi_answer "+
                                    "FROM questions q,exam_questions eq where q.id=eq.question_id and exam_id=? limit ?";
    public static final String LOAD_QUESTIONS_OPTIONS="SELECT SEQUENCE,OPTION_VALUE FROM question_options q where question_id=? order by sequence limit ?";
    public static final String LOAD_EXAM_TAKER_DATA_USING_VOUCHER = "SELECT id,name,last, email , level_id,role_id, voucher, voucher_status,resultEmail FROM exam.exam_taker where voucher=?";
    public static final String UPDATE_VOUCHER_STATUS = "UPDATE exam_taker set voucher_status=? where voucher=?";
    public static final String CONFIGURATION="SELECT CONFIG_NAME,CONFIG_VALUE FROM CONFIG";
    public static final String NOTIFICATION="SELECT SUBJECT, BODY FROM EMAIL_NOTIFICATIONS WHERE inactive=0 and id=? ";
    public static final String AUDIT_TABLE="INSERT INTO AUDIT_TRAIL (action,subaction,at_date,BY_USER_ID,justification) VALUES (?,?,SYSDATE(),?,?)";
    public static final String SAVE_EXAM_SCORE="update exam_taker set score=? where id=?";
    public static final String SAVE_EXAM_ANSWERS="insert into exam_taker_answer (user_id,question_id,answer,date) values (?,?,?,SYSDATE())";
}
