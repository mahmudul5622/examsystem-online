<%-- 
    Document   : index2
    Created on : Sep 27, 2011, 10:38:35 AM
    Author     : fouadabd
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>Interview Exam System</title>
  <link href="css/styles.css" rel="stylesheet" type="text/css" />
</head>
<script language="JavaScript" src="js/ajax_download.js"></script>
<body onload="checkStarted();">
    <div class="main-container" align="center" id="all">
    <div class="fullwidth">
      <div class="top-menu">
         <a href="help.jsp" title="Help" target="_new"><img alt="img" src="images/help-2.gif" width="20" height="20" style="cursor:pointer;vertical-align: middle"/> Help</a>
      </div>
    </div>
    <div class="logo-container floatleft clearboth">
        <img alt="logo" src="images/ExamIcon.jpg" class="examLogo" width="80" height="80" style="vertical-align: middle"/> <big><b> Online Exam Tool</b></big>
        <div id="welcome"><i>Welcome, Guest</i></div>
    </div>
    <div class="content-container">
<form  method="post" action="ExamServlet">
      <div class="maincontent-container" >
        <div class="topborder"></div>
        <div class="border-pxl">
        <div id="control" class="form-container" align="center" style="visibility: hidden">
            <a onclick="submitExam();" title="Pause Exam" style="cursor:pointer"><img alt="submit" src="images/submit.gif"/> Submit Exam</a> &nbsp;&nbsp;
            <a onclick="pauseExam();" title="Pause Exam" id="pauseLink" name="pauseLink" style="cursor:pointer"> <img alt="pause" src="images/button-pause.png"/> Pause Exam</a> &nbsp;&nbsp;
        </div>
        <div id="timer" class="form-container" align="center" style="visibility: hidden">
            <div id="time">00 <img src='images/clock.jpg' style='vertical-align: middle'/> 00</div>
        </div>
            <div id="start" class="form-container" align="left" style="visibility: hidden">
              <div class="form-row">
                  <img alt="Voucher" src="images/login.jpg" style="vertical-align: middle" width="60" height="40" /> <label>Exam Voucher:</label>
                <input class="textstyle" type="text" id="voucher" name="voucher" value=""/>
              </div>
              <div style="vertical-align: middle" align="center">
                <img id="loading" name="loading" src="images/loading.gif" alt="Loading" style="visibility: hidden">
                <br>
                <img src="images/start.jpg" title="Start Exam" alt="Start Exam" onclick="startNewExam();" style="vertical-align: middle;cursor:pointer"/>
              </div>
          </div>
          <div style="vertical-align: middle" align="center">
              <a id="back" name="back" href="#top" onclick="pageIndex--;refreshExamDisplay();" style="visibility: hidden;cursor:pointer"><img  src="images/bullet81.jpg" alt="Back"></a>
              <a id="next" name="next" href="#top" onclick="pageIndex++;refreshExamDisplay();" style="visibility: hidden;cursor:pointer"><img  src="images/bullet8.jpg" alt="Next"></a>
              <br>
              <a id="main" name="main" onclick="refreshExamsMainMenu();" style="visibility: hidden;cursor:pointer"><img src="images/mainmenu.gif" title="Main Exam Menu" alt="Main Exam Menu"/></a>
              <a id="next_exam" name="next_exam" href="#top" onclick="pageIndex=0;examIndex++;refreshExamDisplay();" style="visibility: hidden;cursor:pointer"><img src="images/next.gif" title="Next Exam" alt="Next Exam"/></a>
          </div>
        </div>
        <div class="bottomborder"></div>
        <div class="footer">
          Copyrights 2012 - All Rights Reserved - Osama Oransa
          <br>
          <img alt="logo" src="images/online-exam-icon.jpg" class="examLogo" width="200" height="150" style="vertical-align: middle"/>          
        </div>
      </div>
</form>
    </div>
  </div>
</body>
</html>