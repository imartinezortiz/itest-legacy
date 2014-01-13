<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>
<%@ page import="com.cesfelipesegundo.itis.model.BasicDataExam" %>

<% 
	BasicDataExam exam = (BasicDataExam)request.getAttribute("exam");
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addBundleAndTextStep("examOf"," "+exam.getGroup().getCourse().getName(),"");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="kid"/>
	<jsp:param name="mathml" value="mathml"/>
	<jsp:param name="examduration" value="${exam.duration}"/>
</jsp:include>

<script type="text/javascript">
	function saveFillAnswer(questionId,answerId){
	   iTestLockPage(true);
		var textAnswer = document.getElementById('inputFillAnswer'+answerId).value;
		document.getElementById('labelModified'+questionId).style.display="none";
		ExamMgmt.updateFillAnswer(${exam.id},${user.id},questionId,answerId,textAnswer,{callback:iTestUnlockPage(),
   	 		 timeout:callBackTimeOut,
			 errorHandler:function(message) { document.getElementById('imgSaved'+answerId).style.display="none";iTestUnlockPage('error');} });
	}
</script>
		
<div id="menu"> 
  <ul>
    <li>
		<!-- <a href="${pageContext.request.contextPath}/learner/newexam.itest?method=gradeQuestion&questionNumber=${questionNumber}"><fmt:message key="answerQuestion" /></a> -->
    </li>
  </ul>
</div>

<div id="continuar">
	<a href="${pageContext.request.contextPath}/learner/newexam.itest?method=gradeQuestion&questionNumber=${questionNumber}">
		<img src="${pageContext.request.contextPath}/imagenes/boton_siguiente.png" alt="<fmt:message key="answerQuestion" />"/>
	</a>
</div>

<script type='text/javascript'>

	// unable right mouse button
	var message="";
	function clickIE() {if (document.all) {(message);return false;}}
	function clickNS(e) {if
	(document.layers||(document.getElementById&&!document.all)) {
	if (e.which==2||e.which==3) {(message);return false;}}}
	if (document.layers)
	{document.captureEvents(Event.MOUSEDOWN);document.onmousedown=clickNS;}
	else{document.onmouseup=clickNS;document.oncontextmenu=clickIE;}
	document.oncontextmenu=new Function("return false")

	// Variables that store the features of the last answer marked, for if we need to repeat the call
	var lastQuestionID = null;
	var lastAnswerID = null;
	var lastUserID = null;
	var lastValue;
	// Timer for repeating calls
	var timerID;
	var attempt;
	// Array for storing the number of answers marked for each question
	var questionHits = 0;
	// Array for storing the max number of answers marked for each question
	var questionMaxHits = ${question.numCorrectAnswers};

   // Check the number of answers checked:
   function checkNumAnswers(formid,value,checkelem,userid,questionid,answerid,questionnumber,toCheck) {   
       if (!toCheck && value){   
    	   // Lock page
			iTestLockPage(true);
		    // Updating server info: callback is the unlock of the page...
		    lastQuestionID = questionid;
		    lastAnswerID = answerid;
		    lastUserID = userid;
		    lastValue = true;
		    timerID = setTimeout(repeatLastMark, 1500);
		    attempt = 0;
           ExamMgmt.updateQuestion(${exam.id},userid,questionid,answerid,value,{callback:remarkAnswer,
        	     timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
           
		 }
       else{
    	   if (!toCheck && !value){
    		 // Lock page
  			 iTestLockPage(true);
  		     // Updating server info: callback is the unlock of the page...
  		     lastQuestionID = questionid;
  			 lastAnswerID = answerid;
  			 lastUserID = userid;
  		     lastValue = false;
  		     timerID = setTimeout(repeatLastMark, 1500);
  		     attempt = 0;
  	         ExamMgmt.updateQuestion(${exam.id},userid,questionid,answerid,value,{callback:unremarkAnswer,
  	        	timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
  	      }
    	   else{
    		   // If too much questions were answered, we capture the answer:
    		      if (value) {
    		    	  var inputs = document.getElementById('answer'+questionid).getElementsByTagName('input');
    		    	  questionHits=-1;
     		         	for(var i=0;i<inputs.length;i++){
    						if(inputs[i].checked){
    							questionHits++;
    						}
    					 }
    		         if (questionHits >= questionMaxHits) {
    		            alert('<fmt:message key="toomuchansw" />');
    		            checkelem.checked = false;
    		         } else {    
    				    questionHits++;
    				    // Lock page
    					iTestLockPage(true);
    				    // Updating server info: callback is the unlock of the page...
    				    lastQuestionID = questionid;
    				    lastAnswerID = answerid;
    				    lastUserID = userid;
    				    lastValue = true;
    				    timerID = setTimeout(repeatLastMark, 1500);
    				    attempt = 0;
    		            ExamMgmt.updateQuestion(${exam.id},userid,questionid,answerid,value,{callback:remarkAnswer,
    		            timeout:callBackTimeOut,
   						 errorHandler:function(message) {iTestUnlockPage('error');} });
    				 }
    		      } else {
    		         // Answer unchecked
    		         questionHits--;
    				 // Lock page
    				 iTestLockPage(true);
    			     // Updating server info: callback is the unlock of the page...
    			     lastQuestionID = questionid;
    				 lastAnswerID = answerid;
    				 lastUserID = userid;
    			     lastValue = false;
    			     timerID = setTimeout(repeatLastMark, 1500);
    			     attempt = 0;
    		         ExamMgmt.updateQuestion(${exam.id},userid,questionid,answerid,value,{callback:unremarkAnswer,
    		        	 timeout:callBackTimeOut,
						 errorHandler:function(message) { iTestUnlockPage('error');} });
    		      }
    	   }
       }         
      
   } // checkNumAnswers
   
   // remarks the last selected answer	
   function remarkAnswer() {
//   		var answer = document.getElementById('check'+lastAnswer);
//   		answer.className = "respuestaSeleccionada";
		clearTimeout(timerID);
   		iTestUnlockPage(true);
   } // remarkAnswer

   // unremarks the last selected answer
   function unremarkAnswer() {
//   		var answer = document.getElementById('check'+lastAnswer);
//   		answer.className = "";
		clearTimeout(timerID);
   		iTestUnlockPage(true);
   } // unremarkAnswer
   
   function repeatLastMark() {
   		clearTimeout(timerID);
   		attempt++;
   		if (attempt % 3 == 0) {
   			alert('<fmt:message key="repeatingMark" />');
   		}
   		timerID = setTimeout(repeatLastMark, 1500);
   		if (lastValue) {
   			ExamMgmt.reUpdateQuestion(lastUserID,lastQuestionID,lastAnswerID,lastValue,attempt,{callback:remarkAnswer,
   				timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
   		}
   		else {
   			ExamMgmt.reUpdateQuestion(lastUserID,lastQuestionID,lastAnswerID,lastValue,attempt,{callback:unremarkAnswer,
   				timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
   		}
   }

</script>

<div id="contenido" style="width: 90%; left: 0">

    <script type="text/javascript" src="${pageContext.request.contextPath}/common/resources/ASCII_MathML.js"></script>
	
	<!-- Ajax for questions -->
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/ExamMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>
	
	<center>
	
    <!-- <p class="tituloExamen"> <u><fmt:message key="course"/> <c:out value="${exam.group.course.name}"/> (<c:out value="${exam.group.name}"/>)</u> <br/><br/>
    <c:out value="${exam.title}"/></p>  -->
	

	<jsp:include page="/WEB-INF/jsp/common/question.jsp" flush="true">
		<jsp:param name="view" value="exam"/>
	    <jsp:param name="numQuestion" value="${questionNumber}"/>
	    <jsp:param name="questionsNumber" value="${exam.questionsNumber}"/>
	    <jsp:param name="showCorrectAnswers" value="${exam.showNumCorrectAnswers}"/>
	    <jsp:param name="role" value="KID"/>
	</jsp:include>

		
	</center>	
</div>

<div id="reloj">
	<form name="cd">
		<center>
		<span style="font-size:8pt; background-color:#FFFFFF; font-family: Verdana, Arial, Helvetica, sans-serif; font-weight:bold"><fmt:message key="remainingTime" /></span></br>
	    <table id="slideReloj" cellspacing="0" cellpadding="0"><tr><td id="tiempoGastado"></td><td id="tiempoRestante"></td></tr></table>
	    </center>
	</form>
</div>

<script type="text/javascript">
<!--
   	tiempoExamen(<c:out value="${timeRemaining}"/>,
   		${60 * exam.duration},
		'<c:out value="${pageContext.request.contextPath}/learner/newexam.itest?method=endExam"/>',
		'<c:out value="${pageContext.request.contextPath}/imagenes"/>',
		'<fmt:message key="showClock" />',
		'<fmt:message key="hideClock" />',
		'<fmt:message key="examEnded" />');
//-->
</script>

</body>

</html>
