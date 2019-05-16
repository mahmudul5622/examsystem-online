
package osa.ora.servlet;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Hashtable;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import osa.ora.bd.BDFactory;
import osa.ora.beans.Action;
import osa.ora.beans.ExamBean;
import osa.ora.beans.Notification;
import osa.ora.beans.Question;
import osa.ora.beans.UserProfile;
import osa.ora.exception.ApplicationException;
import osa.ora.log.Logger;
import osa.ora.spi.IAccount;
import osa.ora.spi.ICommon;
import osa.ora.spi.IConstant;
import osa.ora.spi.IExam;
import osa.ora.utils.EmailSender;

/*
 * @author Osama Oransa
 */
@WebServlet(name="ExamServlet", urlPatterns={"/ExamServlet"})
public class ExamServlet extends HttpServlet {
    private static IAccount accountBD=BDFactory.getAccountBD();
    private static IExam examBD=BDFactory.getExamBD();
    private static ICommon commonBD=BDFactory.getCommonBD();
    private static Logger logger = Logger.getLogger("ExamServlet");
    private static EmailSender emailSender=null;
    private static Hashtable<String,String> configurations=null;
    private static Hashtable<Integer,Notification> notifications=new Hashtable<Integer,Notification>();
    //static initializer block to adjust certain components...
    static {
        //load configurations
        try {
            configurations = commonBD.geConfigurations();
        } catch (ApplicationException ex) {
            logger.error(ex.getMessage());
        }
        //initialize email
        emailSender=new EmailSender(configurations);
        //load notifications
        Notification notification=null;
        try {
            notification = commonBD.getNotification(IConstant.EMAIL_TEMPLATE_FOR_EXAMINER);
            notifications.put(IConstant.EMAIL_TEMPLATE_FOR_EXAMINER,notification);
            notification=commonBD.getNotification(IConstant.EMAIL_TEMPLATE_FOR_EXAMINEE);
            notifications.put(IConstant.EMAIL_TEMPLATE_FOR_EXAMINEE,notification);
        } catch (ApplicationException ex) {
            logger.error(ex.getMessage());
        }
    }
    
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String action=request.getParameter(IConstant.ACTION);
        logger.debug("Action="+action);
        try {
            if(action!=null){
                //check exam already started ??
                if(IConstant.CHECK_EXAM_EXIST.equals(action)){
                    response.setContentType("text/html;charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    if(request.getSession().getAttribute(IConstant.EXAM_DETAILS)==null){
                        out.print(IConstant.NO);
                    }else{
                        out.print(IConstant.YES);
                    }
                    out.close();
                //download new exam using exam voucher
                }else if(IConstant.DOWNLOAD_NEW_EXAM.equals(action)){
                    String voucher=request.getParameter(IConstant.VOUCHER);
                    if(voucher==null || voucher.equals("")){
                        logger.debug("Error: During exam loading");
                        response.setContentType("text/html;charset=UTF-8");
                        PrintWriter out = response.getWriter();
                        out.print("Error: Missing Exma Voucher!");
                        return;
                    }
                    UserProfile userProfile=new UserProfile();
                    userProfile.setVoucher(voucher);
                    logger.debug("loading matching the selected role and experience by active voucher ="+voucher);
                    try {
                        userProfile = accountBD.voucherAuthentication(userProfile);
                    } catch (Exception ex) {
                        logger.debug("Error: During exam loading");
                        response.setContentType("text/html;charset=UTF-8");
                        PrintWriter out = response.getWriter();
                        out.print("Error: During exam loading");
                        return;
                    }
                    if(userProfile==null){
                        response.setContentType("text/html;charset=UTF-8");
                        PrintWriter out = response.getWriter();
                        out.print("Error: During exam loading");
                        return;
                    }
                    logger.debug("Loading Exams matching the eselected role and experience ........");
                    ExamBean[] exams=null;
                    try {
                        exams = examBD.loadNewExam(userProfile);
                    } catch (ApplicationException ex) {
                        logger.error(ex.getMessage());
                    }
                    if(exams==null){
                        response.setContentType("text/html;charset=UTF-8");
                        PrintWriter out = response.getWriter();
                        out.print("Error: During exam loading");
                        return;
                    }
                    //add audit action
                    Action auditAction=new Action();
                    auditAction.setAction(IConstant.DOWNLOAD_NEW_EXAM_ACTION);
                    auditAction.setJustification("Have voucher="+voucher);
                    auditAction.setPerformedBy(userProfile.getId());
                    auditAction.setSub_action("");
                    try {
                        commonBD.auditAction(auditAction);
                    } catch (ApplicationException ex) {
                        logger.error(ex.getMessage());
                    }
                    try {
                        accountBD.updateVoucherStatus(userProfile);
                    } catch (ApplicationException ex) {
                        logger.error(ex.getMessage());
                        //don't do any thing....but the voucher can be re-used!
                    }
                    HashMap answers=new HashMap();
                    request.getSession().setAttribute(IConstant.EXAM_DETAILS,exams);
                    request.getSession().setAttribute(IConstant.PROFILE,userProfile);
                    request.getSession().setAttribute(IConstant.TIME,new Integer(0));
                    request.getSession().setAttribute(IConstant.ANSWERS,answers);
                    request.getSession().setAttribute(IConstant.PAUSES,new Integer(0));
                    convertExamsToJSON(exams,userProfile,response);
                //download existing exam
                }else if(IConstant.DOWNLOAD_EXISTING_EXAM.equals(action)){
                    logger.debug("Resume existing Exam........");
                    ExamBean[] exams=(ExamBean[])request.getSession().getAttribute(IConstant.EXAM_DETAILS);
                    UserProfile userProfile=(UserProfile)request.getSession().getAttribute(IConstant.PROFILE);
                    convertExamsToJSON(exams,userProfile,response);
                //update exam timing
                }else if(IConstant.UPDATE_EXAM_TIME.equals(action)){
                    //increase 1 min for each call :)
                    Integer a=(Integer)request.getSession().getAttribute(IConstant.TIME);
                    if(a==null) {
                        request.getSession().invalidate();
                        return;
                    }
                    a++;
                    logger.debug("Update Exam Timing........");
                    request.getSession().setAttribute(IConstant.TIME,a);
                    return;
                //get exam timing
                }else if(IConstant.GET_EXAM_TIME.equals(action)){
                    logger.debug("Get Exam Timing........");
                    ExamBean[] exams=(ExamBean[])request.getSession().getAttribute(IConstant.EXAM_DETAILS);
                    if(exams==null) return;
                    response.setContentType("text/html;charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    Integer time=(Integer)request.getSession().getAttribute(IConstant.TIME);
                    out.print(time);
                    out.close();
                //get no. of exam questions per page
                }else if(IConstant.GET_EXAM_NO_OF_QUESTIONS_PER_PAGE.equals(action)){
                    logger.debug("Get Exam Questions per Page........"+configurations.get(IConstant.QUESTIONS_PER_PAGE));
                    response.setContentType("text/html;charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.print(configurations.get(IConstant.QUESTIONS_PER_PAGE));
                    out.close();
                //request exam pause
                }else if(IConstant.REQUEST_EXAM_PAUSE.equals(action)){
                    logger.debug("Request Exam Pause........");
                    ExamBean[] exams=(ExamBean[])request.getSession().getAttribute(IConstant.EXAM_DETAILS);
                    if(exams==null) return;
                    response.setContentType("text/html;charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    Integer pauses=(Integer)request.getSession().getAttribute(IConstant.PAUSES);
                    pauses++;
                    if(pauses<exams.length){
                        if(pauses==exams.length-1){
                            out.print(IConstant.LAST);
                        }else{
                            out.print(IConstant.OK);
                        }
                        request.getSession().setAttribute(IConstant.PAUSES,pauses);
                    }else{
                        out.print(IConstant.NO);
                    }
                    out.close();
                //submit single exam answer
                }else if(IConstant.SUBMIT_SINGLE_EXAM_ANSWER.equals(action)){
                    String id=request.getParameter(IConstant.ID);
                    String value=request.getParameter(IConstant.VALUE);
                    if(id!=null && value!=null){
                        HashMap answers=(HashMap)request.getSession().getAttribute(IConstant.ANSWERS);
                        answers.put(id, value);
                        logger.debug("Stored Answer="+id+" value="+value);
                        request.getSession().setAttribute(IConstant.ANSWERS,answers);
                    }
                    return;
                //end exam time or submit exam
                }else if(IConstant.SUBMIT_EXAM_OR_EXAM_TIME_END.equals(action)){
                    ExamBean[] exams=(ExamBean[])request.getSession().getAttribute(IConstant.EXAM_DETAILS);
                    HashMap answers=(HashMap)request.getSession().getAttribute(IConstant.ANSWERS);
                    if(exams==null){
                        response.setContentType("text/html;charset=UTF-8");
                        PrintWriter out = response.getWriter();
                        out.print(IConstant.DONE);
                        return;
                    }
                    logger.debug("Exam ENDED UP!");
                    Hashtable<Integer,String> userAnswersToSave=new Hashtable<Integer,String>();
                    //analyze the exam answers,score and save them to DB
                    //assumption = no negative degrees
                    //assumption = all questions have the same weight
                    int totalScore=0;
                    int totalQuestions=0;
                    for(int i=0;i<exams.length;i++){
                        Question[] questions=exams[i].getQuestions();
                        logger.debug(new Gson().toJson(answers));
                        for(int n=0;n<questions.length;n++){
                            totalQuestions++;
                            logger.debug("Question Id="+questions[n].getId());
                            if(questions[n].getMulti_answer()==1){
                                //single answer
                                logger.debug("Single answer="+questions[n].getAnswers());
                                String userAnswerString=""+answers.get(""+questions[n].getId());
                                logger.debug("User answer="+userAnswerString);
                                userAnswersToSave.put(questions[n].getId(), userAnswerString);
                                if(questions[n].getAnswers().equals(userAnswerString)){
                                    totalScore++;
                                    logger.debug("Single Answer Question is correct no# "+questions[n].getId());
                                }
                            }else{
                                //multiple answers
                                logger.debug("Multiple answer="+questions[n].getAnswers());
                                String userAnswersString="";
                                for(int s=0;s<questions[n].getQuestionOptions().length;s++){
                                    int index=s+1;
                                    String key=questions[n].getId()+"|"+index;
                                    logger.debug("User answer="+answers.get(key));
                                    if("1".equals(""+answers.get(key))){
                                        userAnswersString+=","+index;
                                    }
                                }
                                if(userAnswersString.length()>2) userAnswersString=userAnswersString.substring(1);
                                logger.debug("Final User answer="+userAnswersString);
                                userAnswersToSave.put(questions[n].getId(), userAnswersString);
                                if(userAnswersString.equals(questions[n].getAnswers())){
                                    totalScore++;
                                    logger.debug("Multi Answer Question is correct no# "+questions[n].getId());
                                }
                            }
                        }
                    }
                    logger.debug("Total score="+totalScore+" out of "+totalQuestions+" question(s).");
                    //send email to the admin user
                    UserProfile userProfile=(UserProfile)request.getSession().getAttribute(IConstant.PROFILE);
                    String[] sendEmailTo=new String[1];                    
                    int examScore=(totalScore*100)/totalQuestions;
                    String[] params=new String[3];
                    params[0]=userProfile.getName();
                    params[1]=""+examScore;
                    params[2]=""+totalQuestions;
                    if(IConstant.TRUE.equalsIgnoreCase(configurations.get(IConstant.SEND_EXAMINER_SCORE))){
                        sendEmailTo[0]=userProfile.getEmail();
                        Notification notificationToExaminer=new Notification();
                        notificationToExaminer.setSubject(EmailSender.replaceParams(params, notifications.get(IConstant.EMAIL_TEMPLATE_FOR_EXAMINER).getSubject()));
                        notificationToExaminer.setMessageBody(EmailSender.replaceParams(params, notifications.get(IConstant.EMAIL_TEMPLATE_FOR_EXAMINER).getMessageBody()));
                        emailSender.sendMail(sendEmailTo,notificationToExaminer.getSubject(),notificationToExaminer.getMessageBody(), null, null, true);
                    }
                    //here is the examiner email..
                    if(IConstant.TRUE.equalsIgnoreCase(configurations.get(IConstant.SEND_EXAMINEE_SCORE))){
                        sendEmailTo[0]=userProfile.getResultsEmail();
                        Notification notificationToExaminee=new Notification();
                        notificationToExaminee.setSubject(EmailSender.replaceParams(params, notifications.get(IConstant.EMAIL_TEMPLATE_FOR_EXAMINEE).getSubject()));
                        notificationToExaminee.setMessageBody(EmailSender.replaceParams(params, notifications.get(IConstant.EMAIL_TEMPLATE_FOR_EXAMINEE).getMessageBody()));
                        emailSender.sendMail(sendEmailTo,notificationToExaminee.getSubject(),notificationToExaminee.getMessageBody(), null, null, true);
                    }
                    //save user exam answers and total score or only total score according to configurations.
                    if(IConstant.TRUE.equalsIgnoreCase(configurations.get(IConstant.SAVE_SCORE_ONLY))){
                        try {
                            examBD.saveExamScoreOnly(userProfile, examScore);
                        } catch (ApplicationException ex) {
                            logger.error(ex.getMessage());
                        }
                    }else{
                        try {
                            examBD.saveExamAnswers(userProfile, userAnswersToSave, examScore);
                        } catch (ApplicationException ex) {
                            logger.error(ex.getMessage());
                        }
                    }
                    //add audit action
                    Action auditAction=new Action();
                    auditAction.setAction(IConstant.SUBMIT_EXAM_ACTION);
                    auditAction.setJustification("Exam ended with score="+examScore+"%");
                    auditAction.setPerformedBy(userProfile.getId());
                    auditAction.setSub_action("");
                    try {
                        commonBD.auditAction(auditAction);
                    } catch (ApplicationException ex) {
                        logger.error(ex.getMessage());
                    }
                    //invalidate the session....
                    request.getSession().invalidate();
                    response.setContentType("text/html;charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    //show to the user his/her score ??
                    if(IConstant.TRUE.equalsIgnoreCase(configurations.get(IConstant.SHOW_SCORE))){
                        out.print(IConstant.SCORE+examScore+" %");
                    }else{
                        out.print(IConstant.DONE);
                    }
                    return;
                //get all current exam answers
                }else if(IConstant.GET_ALL_EXAM_ANSWERS.equals(action)){
                    logger.debug("Get all current Exam Answers....");
                    HashMap answers=(HashMap)request.getSession().getAttribute(IConstant.ANSWERS);
                    convertAnswersToJSON(answers,response);
                }else if(IConstant.RENDER_IMAGE.equals(action)){
                    int id=Integer.parseInt(request.getParameter(IConstant.ID));
                    ExamBean[] exams=(ExamBean[])request.getSession().getAttribute(IConstant.EXAM_DETAILS);
                    if(exams==null) return;
                    for(int i=0;i<exams.length;i++){
                        for(int n=0;n<exams[i].getQuestions().length;n++){
                            if(id==exams[i].getQuestions()[n].getId()){
                                //get content per image type...
                                //response.setContentType("image/jpeg");
                                ServletOutputStream outputStream=response.getOutputStream();
                                outputStream.write(exams[i].getQuestions()[n].getQuestionData());
                                outputStream.close();
                                return;
                            }
                        }
                    }
                    return;
                }
            }
        } finally {
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void convertExamsToJSON(ExamBean[] exams, UserProfile user,HttpServletResponse response) throws IOException {
        logger.debug("Convert into JSON ........");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(user.getName()+" ("+user.getEmail()+")###");
        logger.debug("Profile written!");
        out.flush();
        out.print("\"exams\": [");
        for(int i=0;i<exams.length;i++){
            out.print(new Gson().toJson(exams[i]));
            if(i<exams.length-1){
                out.print(",");
            }
            //logger.debug(new Gson().toJson(exams[i]));
        }
        out.print("]");
        out.close();
        logger.debug("Success!");
    }
    private void convertAnswersToJSON(HashMap answers, HttpServletResponse response) throws IOException {
        logger.debug("Convert into JSON ........");
        String temp=new Gson().toJson(answers);
        if(temp!=null && temp.length()>2){
            temp=temp.substring(1,temp.length()-2);
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(temp);
            out.close();
            //logger.debug(temp);
        }
        logger.debug("Success!");
    }
}
