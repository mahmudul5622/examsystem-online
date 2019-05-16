var xmlHttp = null ;
running=false;
currentAction=0;
pageIndex=0;
maxPages=10;
maxPerPage=5;
examIndex=-1;
total=0;
totalQuestions=0;
elabsed=0;
secondsTimer=0;
elabsedPause=0;
secondsTimerPause=0;
timerStart=false;
timerId=0;
examData=new Array();
output = {};
function ajax_check(url,action){
    if(running==true){
        alert("Please wait while connecting to server...");
        return;
    }
    currentAction=action;
    createXmlHttpRequest();
    xmlHttp.onreadystatechange = handleRequest;
    running=true;
    document.getElementById("loading").style.visibility="visible";
    xmlHttp.open("POST",url+"?action="+action,true);
    xmlHttp.send("<?XML version=\"1.0\" encoding=\"UTF-8\"?>");
}
function ajax_loadAnswers(url,action){
    currentAction=action;
    createXmlHttpRequest();
    xmlHttp.onreadystatechange = handleRequest;
    xmlHttp.open("POST",url+"?action="+action,true);
    xmlHttp.send("<?XML version=\"1.0\" encoding=\"UTF-8\"?>");
}
function ajax_loadServerTime(url,action){
    currentAction=action;
    createXmlHttpRequest();
    xmlHttp.onreadystatechange = handleRequest;
    xmlHttp.open("POST",url+"?action="+action,true);
    xmlHttp.send("<?XML version=\"1.0\" encoding=\"UTF-8\"?>");
}
function ajax_endExam(url,action){
    currentAction=action;
    createXmlHttpRequest();
    //we don't care about the response
    xmlHttp.onreadystatechange = handleRequest;
    xmlHttp.open("POST",url+"?action="+action,true);
    xmlHttp.send("<?XML version=\"1.0\" encoding=\"UTF-8\"?>");
}
function ajax_pauseExam(url,action){
    currentAction=action;
    createXmlHttpRequest();
    //we don't care about the response
    xmlHttp.onreadystatechange = handleRequest;
    xmlHttp.open("POST",url+"?action="+action,true);
    xmlHttp.send("<?XML version=\"1.0\" encoding=\"UTF-8\"?>");
}
function ajax_getQuestionsPerPage(url,action){
    currentAction=action;
    createXmlHttpRequest();
    //we don't care about the response
    xmlHttp.onreadystatechange = handleRequest;
    xmlHttp.open("POST",url+"?action="+action,true);
    xmlHttp.send("<?XML version=\"1.0\" encoding=\"UTF-8\"?>");
}
function ajax_timeCheck(url,action){
    //currentAction=action;
    createXmlHttpRequest();
    //we don't care about the response
    //xmlHttp.onreadystatechange = handleRequest;
    xmlHttp.open("POST",url+"?action="+action,true);
    xmlHttp.send("<?XML version=\"1.0\" encoding=\"UTF-8\"?>");
}
function ajax_timeUpdate(url,action){
    //currentAction=action;
    createXmlHttpRequest();
    //we don't care about the response
    //xmlHttp.onreadystatechange = handleRequest;
    xmlHttp.open("POST",url+"?action="+action,true);
    xmlHttp.send("<?XML version=\"1.0\" encoding=\"UTF-8\"?>");
}
function ajax_submitAnswer(url,action,id,value){
    //currentAction=action;
    createXmlHttpRequest();
    //we don't care about the response
    //xmlHttp.onreadystatechange = handleRequest;
    xmlHttp.open("POST",url+"?action="+action+"&id="+id+"&value="+value,true);
    xmlHttp.send("<?XML version=\"1.0\" encoding=\"UTF-8\"?>");
}
function ajax_downloadExistingExam(url,action){
    if(running==true){
        alert("Please wait while downloading the exam...");
        return;
    }
    currentAction=action;
    createXmlHttpRequest();
    xmlHttp.onreadystatechange = handleRequest;
    running=true;
    document.getElementById("loading").style.visibility="visible";
    xmlHttp.open("POST",url+"?action="+action,true);
    xmlHttp.send("<?XML version=\"1.0\" encoding=\"UTF-8\"?>");
}
function ajax_downloadNewExam(url,action){
    if(running==true){
        alert("Please wait while downloading the exam...");
        return;
    }
    currentAction=action;
    voucher=document.forms[0].voucher.value;
    createXmlHttpRequest();
    xmlHttp.onreadystatechange = handleRequest;
    running=true;
    document.getElementById("loading").style.visibility="visible";
    xmlHttp.open("POST",url+"?action="+action+"&voucher="+voucher,true);
    xmlHttp.send("<?XML version=\"1.0\" encoding=\"UTF-8\"?>");
}
function createXmlHttpRequest(){
    if(window.ActiveXObject){
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    else if(window.XMLHttpRequest){
        xmlHttp = new XMLHttpRequest();
    }
}

function handleRequest(){
    if(xmlHttp.readyState == 4){
     xmlvalue=xmlHttp.responseText;
     if(xmlvalue == null){
         xmlvalue = xmlHttp.responseXML;
     }
     //check if exam started or not yet...
     if(currentAction==1){
         if(xmlvalue=='YES'){
            running=false;
            //go to main menu
            resumeOldExam();
         }else{
            document.getElementById("start").style.visibility="visible";
         }
         //download exam (new =2 , old =3)
     }else if(currentAction==2 || currentAction==3){
         if(xmlvalue.substr(0,5)!='Error'){
             parts=xmlvalue.split('###');
             document.getElementById("welcome").innerHTML = '<i>Welcome, '+parts[0]+'</i>';
             examData= parts[1];
             examData=eval('(' + "{"+examData+"}" + ')');
             ajax_getQuestionsPerPage('ExamServlet',10);
         }else{
            alert('Invalid Exam Voucher!');
            running=false;
            document.getElementById("loading").style.visibility="hidden";
         }
     //}else if(currentAction==4 || currentAction==6){
         //4=time update, do no thing...
         //6=submit exam answer..         
     }else if(currentAction==10){
          maxPerPage=xmlvalue;
          loadExamAnswers();
          calculateTotal();
          refreshExamsMainMenu();
     }else if(currentAction==7){
         //7=exam termination ,  must return DONE or score or NO...
         if(xmlvalue!='DONE'){
             if(xmlvalue.substr(0,5)=='SCORE'){
                alert("Your Score = "+xmlvalue.substr(7));
                window.location.reload();
             }else{
                resumeExam();
             }
         }else{
            window.location.reload();
         }
     }else if(currentAction==5){
         //get server timing and update current exam time...
         if(xmlvalue!=0){
            elabsed=parseInt(xmlvalue/12);
            secondsTimer=(xmlvalue%12)*5;
         }else{
             elabsed=0;
             secondsTimer=0;
         }
         timerHTML=totalQuestions+" question(s) in "+total+" min(s) <img src='images/clock.jpg' style='vertical-align: middle'/> "+elabsed+" min(s) "+secondsTimer+" second(s)";
         document.getElementById("time").innerHTML = timerHTML;
         if(timerStart==false){
             timerId=setInterval("increaseTime();",1000);
             timerStart=true;
         }
     }else if(currentAction==8){
         //get previously submitted answers..
         answers=xmlvalue.split(',');
         for(i=0;i<answers.length;i++){
             answers[i]=answers[i].replace(/["']{1}/gi,"");
             k=answers[i].split(':');
             output[k[0]] = k[1];
         }
         loadServerTime();

    }else if(currentAction==11){
        //check if pause is allowed...
         if(xmlvalue!='NO'){
            //check on server if pause still valid..... user is allowed for pauses repeated per (no. of exams -1)
            if(timerStart==true) {
                clearInterval(timerId);
                timerStart=false;
            }
            elabsedPause=0;
            secondsTimerPause=0;
            timerId=setInterval("increaseTimePause();",1000);
            timerStart=true;
            document.getElementById("start").innerHTML ="<center><img src='images/pause_big.jpg'/><br><br>Exam Paused<br><div id='timerPause'></div><br><img src='images/mainmenu.gif' title='main menu' alt='Display' onclick='resumeExam();' style='cursor:pointer'/></center>";
            if(xmlvalue=='LAST'){
                document.getElementById("pauseLink").style.visibility="hidden";
            }
         }else{
             //disable pauses....
             document.getElementById("pauseLink").style.visibility="hidden";
         }
    }
     running=false;
     document.getElementById("loading").style.visibility="hidden";
   }
}
function loadServerTime(){
    ajax_loadServerTime('ExamServlet',5);
}
function loadExamAnswers(){
    ajax_loadAnswers('ExamServlet',8);
}
function endExamTime(){
    if(timerStart==true) {
        clearInterval(timerId);
        timerStart=false;
    }
    ajax_endExam('ExamServlet',7);
}
function resumeExam(){
    refreshExamsMainMenu();
    clearInterval(timerId);
    timerId=setInterval("increaseTime();",1000);
    timerStart=true;
}
function pauseExam(){
    //check if there is available pauses...
    ajax_pauseExam('ExamServlet',11);
}
function submitExam(){
    document.getElementById("start").innerHTML ="<center><img width=100 height-100 src='images/loading.gif'/><br><br>Submitting Exam ... </center>";
    if(timerStart==true) {
        clearInterval(timerId);
        timerStart=false;
    }
    ajax_endExam('ExamServlet',7);
}

function checkStarted(){
    ajax_check('ExamServlet',1);
}
function startNewExam(){
    ajax_downloadNewExam('ExamServlet',2);
}
function resumeOldExam(){
    ajax_downloadNewExam('ExamServlet',3);
}
function increaseTime(){
    secondsTimer+=1;
    if(secondsTimer%5==0){
        ajax_timeUpdate('ExamServlet',4);
    }
    if(secondsTimer>=60){
        elabsed++;
        secondsTimer=0;        
    }
    if(examIndex>=-1){
        timerHTML=totalQuestions+" question(s) in "+total+" min(s) <img src='images/clock.jpg' style='vertical-align: middle'/> "+elabsed+" min(s) "+secondsTimer+" second(s)";
        document.getElementById("time").innerHTML = timerHTML;        
        if(elabsed>=total){
            examIndex=-2;
            document.getElementById("back").style.visibility="hidden";
            document.getElementById("next").style.visibility="hidden";
            document.getElementById("main").style.visibility="hidden";
            document.getElementById("start").innerHTML = "Exam Time Ended! Good Luck, results will be processed soon";
            //cancel the interval
            clearInterval(timerId);
            //call server to terminate exam finally...
            endExamTime();
        }
    }
}
function increaseTimePause(){
    secondsTimerPause+=1;
    if(secondsTimerPause>=60){
        elabsedPause++;
        secondsTimerPause=0;
    }
    timerHTML="5 minutes : "+elabsedPause+" min(s) "+secondsTimerPause+" second(s)";
    document.getElementById("timerPause").innerHTML = timerHTML;
    if(elabsedPause>=5){
        //cancel the interval
        clearInterval(timerId);
        //call server to terminate exam finally...
        resumeExam();
    }
}
function calculateTotal(){
    total=0;
    totalQuestions=0;
    for(i=0;i<examData.exams.length;i++){
      total+=examData.exams[i].examDuration;
      totalQuestions+=examData.exams[i].questions.length;
    }
    timerHTML=totalQuestions+" question(s) in "+total+" min(s) <img src='images/clock.jpg' style='vertical-align: middle'/> "+elabsed+" min(s) "+secondsTimer+" second(s)";
    document.getElementById("time").innerHTML = timerHTML;
}
function refreshExamsMainMenu(){
    pageIndex=0;
    examIndex=-1;
    document.getElementById("back").style.visibility="hidden";
    document.getElementById("next").style.visibility="hidden";
    document.getElementById("main").style.visibility="hidden";
    document.getElementById("next_exam").style.visibility="hidden";
    var html="<table border='0'>";
    for(i=0;i<examData.exams.length;i++){
      html+="<tr><td>";
      n=i+1;
      html+="<B> Exam "+n+" : "+examData.exams[i].examTitle+" </B><br>"
      html+="Number of Questions: "+examData.exams[i].maxQuestions+"<br>";
      html+="Exam Duation : "+examData.exams[i].examDuration+" minutes";
      html+="</td><td>&nbsp;&nbsp;&nbsp;&nbsp;</td>"
      html+="<td><image src='images/start.jpg' onclick='selectExamDisplay("+i+")' style='cursor:pointer'/>";
      html+="</td></tr>";
    }
    html+="</tr></table>";
    document.getElementById("start").innerHTML = html;
    document.getElementById("start").style.visibility="visible";
    document.getElementById("control").style.visibility="visible";
    document.getElementById("timer").style.visibility="visible";
}
function refreshExamDisplay(){
    //alert('Navigation');
    selectExamDisplay(examIndex);
}
function selectExamDisplay(index){
     document.getElementById("control").style.visibility="hidden";
     document.getElementById("main").style.visibility="visible";
     examIndex=index;
     maxPages=examData.exams[examIndex].questions.length/maxPerPage;
     document.getElementById("next_exam").style.visibility="hidden";
     //alert(maxPages);
    if(pageIndex==0){
         document.getElementById("back").style.visibility="hidden";
    }else{
         document.getElementById("back").style.visibility="visible";
    }
    if(pageIndex>=maxPages-1){
         document.getElementById("next").style.visibility="hidden";
         if(examIndex<examData.exams.length-1){
             document.getElementById("next_exam").style.visibility="visible";
         }
    }else{
         document.getElementById("next").style.visibility="visible";
    }
    var html="<a name='top'><B> Exam : "+examData.exams[examIndex].examTitle+"</B></a><br><hr>";
    html+="<table border='0'>";
    var n=maxPerPage*pageIndex;
    //n++;
    //alert(n);
    for(i=0;i<maxPerPage;i++){
      html+="<tr><td>";
      s=n+1;
      if(examData.exams[examIndex].questions[n].type==1){
        html+="<B>Q"+s+":</B> "+examData.exams[examIndex].questions[n].questionString;
      }else{
        html+="<B>Q"+s+":</B> <img style='vertical-align: text-top' src='/ExamSystem/ExamServlet?action=9&id="+examData.exams[examIndex].questions[n].id+"'/>";
      }
      html+="<br>";
      if(examData.exams[examIndex].questions[n].multi_answer>1){
          for(x=0;x<examData.exams[examIndex].questions[n].questionOptions.length;x++){
              s=x+1;
              key=examData.exams[examIndex].questions[n].id+'|'+s;
              if(output[key]==1){
                  html+="<br>"+"<input type=checkbox onclick='storeMultiSelectedValue(this.id,"+s+")' checked id="+examData.exams[examIndex].questions[n].id+" name="+examData.exams[examIndex].questions[n].id+"></input>"+examData.exams[examIndex].questions[n].questionOptions[x].optionValue;
              }else{
                  html+="<br>"+"<input type=checkbox onclick='storeMultiSelectedValue(this.id,"+s+")' id="+examData.exams[examIndex].questions[n].id+" name="+examData.exams[examIndex].questions[n].id+"></input>"+examData.exams[examIndex].questions[n].questionOptions[x].optionValue;
              }
          }
      }else{
          for(x=0;x<examData.exams[examIndex].questions[n].questionOptions.length;x++){
              s=x+1;
              if(output[examData.exams[examIndex].questions[n].id]==s){
                html+="<br>"+"<input type=radio onclick='storeSelectedValue(this.id,"+s+")' checked id="+examData.exams[examIndex].questions[n].id+" name="+examData.exams[examIndex].questions[n].id+"></input>"+examData.exams[examIndex].questions[n].questionOptions[x].optionValue;
              }else{
                html+="<br>"+"<input type=radio onclick='storeSelectedValue(this.id,"+s+")' id="+examData.exams[examIndex].questions[n].id+" name="+examData.exams[examIndex].questions[n].id+"></input>"+examData.exams[examIndex].questions[n].questionOptions[x].optionValue;
              }
          }
      }
      html+=("<hr width='60%'/></td></tr>");
      n++;
      if(n>=examData.exams[examIndex].questions.length) break;
    }
    html+="</tr></table>";
    document.getElementById("start").innerHTML = html;
}
function storeSelectedValue(id,index){
    output[id] = index;
    //sent to the server....
    ajax_submitAnswer('ExamServlet',6,id,index);
}
function storeMultiSelectedValue(id,index){
    id=id+'|'+index;
    if(output[id]!=1){
        output[id]=1;
    }else{
        output[id]=0;
    }
    //sent to the server....
    ajax_submitAnswer('ExamServlet',6,id,output[id]);
}

function string2Bin(str) {
  var result = [];
  for (var i = 0; i < str.length; i++) {
    result.push(str.charCodeAt(i).toString(2));
  }
  return result;
}
function bin2String(array) {
  return String.fromCharCode.apply(String, array);
}

