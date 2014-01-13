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
	<jsp:param name="userRole" value="learner"/>
	<jsp:param name="mathml" value="mathml"/>
	<jsp:param name="examduration" value="${exam.duration}"/>
</jsp:include>
		
<div id="menu"> 
  <ul>
    <li><fmt:message key="questions" />
        <select id="goQuestion" onchange="javascript:location.href=this.value;">
		    <c:forEach items="${exam.questions}" varStatus="numQuestion">
		    	<option value="#pregunta<c:out value="${numQuestion.count}"/>"><c:out value="${numQuestion.count}"/></option>
		    </c:forEach>
	    </select>
	</li>
    <li>
    	<form name="myform" action="${pageContext.request.contextPath}/learner/newexam.itest?method=endExam" onsubmit="document.getElementById('pressedEndButton').value='pressed'" method="post">
	    	<a id="endButton" href="javascript:submitForm()" style="display: none; border: ridge; background-color:#CCCCCC; color:#000000;"><fmt:message key="endExam" /></a> 
	    	<input id="pressedEndButton" name="pressedEndButton" type="hidden" value="no pressed"/>
    	</form>
    </li>
  </ul>
</div>

 <script type="text/javascript" src="${pageContext.request.contextPath}/common/resources/ASCII_MathML.js"></script>
	
	<!-- Ajax for questions -->
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/ExamMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>
	


<script type='text/javascript'>

	// unable right mouse button
	/*
	var message="";
	function clickIE() {if (document.all) {(message);return false;}}
	function clickNS(e) {if
	(document.layers||(document.getElementById&&!document.all)) {
	if (e.which==2||e.which==3) {(message);return false;}}}
	if (document.layers)
	{document.captureEvents(Event.MOUSEDOWN);document.onmousedown=clickNS;}
	else{document.onmouseup=clickNS;document.oncontextmenu=clickIE;}
	document.oncontextmenu=new Function("return false")
	*/

	// Variables that store the features of the last answer marked, for if we need to repeat the call
	var lastQuestionID = null;
	var lastAnswerID = null;
	var lastUserID = null;
	var lastValue;
	// Timer for repeating calls
	var timerID;
	var attempt = 0;
	var attemptFill = 0;
	// Array for storing the number of answers marked for each question
	var questionHits = new Array();
	// Array for storing the max number of answers marked for each question
	var questionMaxHits = new Array();

	/*$(document).ready(function(){
		var testQuestionAnswerd = true;
		  var idPreg;
		  var aux = false;
			$('.preguntaExamen > .preguntaCuerpo > .respuestasTipoTest > p > input[type="checkbox"]').each(function(index) {
				var id = $(this).parent().parent().parent().parent().attr('id');
				alert(id);
			});
			 
		});
	*/
	function checkAllQuestionAnswered(){
		var fillQuestionAnswerd = true;
		$('.preguntaExamen > .preguntaCuerpo > .respuestasTipoTest > div > input[type="text"]').each(function(index) {
			var id = $(this).parent().parent().parent().parent().attr('id');
			id = id.replace("question","");
			var display = $("#labelModified"+id).css('display');
			var val = $(this).val();
			val = val.replace(/^\s*|\s*$/g,"");
			if(val==null || val == "" || display != "none"){
				fillQuestionAnswerd = false;
			}
		  });

		  var testQuestionAnswerd = true;
		  var idPreg =-1;
		  var aux = false;
		  $('.preguntaExamen > .preguntaCuerpo > .respuestasTipoTest > p > input[type="checkbox"]').each(function(index) {
				var id = $(this).parent().parent().parent().parent().attr('id');
				if(id==idPreg){
					if(!aux){
						aux = $(this).attr('checked');
					}
					if(!aux && index == $('.preguntaExamen > .preguntaCuerpo > .respuestasTipoTest > p > input[type="checkbox"]').length -1){
						testQuestionAnswerd = false;
					}
				}else{
					if(!aux && idPreg!=-1){
						testQuestionAnswerd = false;
					}else{
						idPreg = id;
						aux = $(this).attr('checked');
					}
				}
			  });
		return testQuestionAnswerd && fillQuestionAnswerd;
	}
	
	function submitForm(){
		if(!checkAllQuestionAnswered()){
			if(confirm("<fmt:message key="alertNotAllQuestionsAnswered"/>")){
				document.myform.submit();
			}
		}else{
			document.myform.submit();
		}
			
	}
	
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
		    //timerID = setTimeout(repeatLastMark, 1500);
		    attempt = 0;
           ExamMgmt.updateQuestion(${exam.id},userid,questionid,answerid,value,{callback:remarkAnswer,
	            timeout:callBackTimeOut,
					 errorHandler:function(message) {iTestUnlockPage('error');} });
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
		  	     //timerID = setTimeout(repeatLastMark, 1500);
		  	     attempt = 0;
		         ExamMgmt.updateQuestion(${exam.id},userid,questionid,answerid,value,{callback:unremarkAnswer,
		        	 timeout:callBackTimeOut,
					 errorHandler:function(message) { iTestUnlockPage('error');} });
		    }
    	   else{
    		// If too much questions were answered, we capture the answer:
    		      if (value) {
    		         // Answer checked
    		         var inputs = document.getElementById('answer'+questionid).getElementsByTagName('input');
    		         questionHits[questionnumber]=-1;
					 for(var i=0;i<inputs.length;i++){
						if(inputs[i].checked){
							questionHits[questionnumber]++;
						}
					 }
    		         if (questionHits[questionnumber] >= questionMaxHits[questionnumber]) {
    		            alertFunction("<fmt:message key="toomuchansw" />");
    		            checkelem.checked = false;
    		         } else {    
    				    questionHits[questionnumber]++;
    				    // Lock page
    					iTestLockPage(true);
    				    // Updating server info: callback is the unlock of the page...
    				    lastQuestionID = questionid;
    				    lastAnswerID = answerid;
    				    lastUserID = userid;
    				    lastValue = true;
    				    //timerID = setTimeout(repeatLastMark, 1500);
    				    attempt = 0;
    		            ExamMgmt.updateQuestion(${exam.id},userid,questionid,answerid,value,{callback:remarkAnswer,
    			            timeout:callBackTimeOut,
    						 errorHandler:function(message) {iTestUnlockPage('error');} });
    				 }
    		      } else {
    		         // Answer unchecked
    		         questionHits[questionnumber]--;
    				 // Lock page
    				 iTestLockPage(true);
    			     // Updating server info: callback is the unlock of the page...
    			     lastQuestionID = questionid;
    				 lastAnswerID = answerid;
    				 lastUserID = userid;
    			     lastValue = false;
    			     //timerID = setTimeout(repeatLastMark, 1500);
    			     attempt = 0;
    		         ExamMgmt.updateQuestion(${exam.id},userid,questionid,answerid,value,{callback:unremarkAnswer,
    		        	 timeout:callBackTimeOut,
						 errorHandler:function(message) { iTestUnlockPage('error');} });
    		      }
    	   }
       }
      
      
   } // checkNumAnswers
   
   // remarks the last selected answer	
   function remarkAnswer(value) {
//   		var answer = document.getElementById('check'+lastAnswer);
//   		answer.className = "respuestaSeleccionada";
	   	if(!value){
			var answer = document.getElementById('check'+lastAnswerID);
			answer.checked=false;
			repeatLastMark();
		}else{
			//clearTimeout(timerID);
	   		iTestUnlockPage(true);;
   		}
   } // remarkAnswer

   // unremarks the last selected answer
   function unremarkAnswer(value) {
//   		var answer = document.getElementById('check'+lastAnswer);
//   		answer.className = "";
		if(!value){
			var answer = document.getElementById('check'+lastAnswer);
			answer.checked=true;
			repeatLastMark();
		}else{
			//clearTimeout(timerID);
	   		iTestUnlockPage(true);
   		}
   } // unremarkAnswer
   
   function repeatLastMark() {
   		//clearTimeout(timerID);
   		attempt++;
   		if(attempt<=3){
   	   		//timerID = setTimeout(repeatLastMark, 1500);
   	   		if (lastValue) {
   	   			ExamMgmt.reUpdateQuestion(lastUserID,lastQuestionID,lastAnswerID,lastValue,attempt,{callback:remarkAnswer,
   		            timeout:callBackTimeOut,
   					 errorHandler:function(message) {iTestUnlockPage('error');} });
   	   		}
   	   		else {
   	   			ExamMgmt.reUpdateQuestion(lastUserID,lastQuestionID,lastAnswerID,lastValue,attempt,{callback:unremarkAnswer,
   		        	 timeout:callBackTimeOut,
   					 errorHandler:function(message) { iTestUnlockPage('error');} });
   	   		}
   	   	}else{
			attempt = 0;
			alertFunction("<fmt:message key="transactionError"/>");
			iTestUnlockPage(true);
   	   	}
   }

   function updateConfidenceLevel(elem,userid,questionid){
	   var attemptConfidenceLevel = 0;
	   function updatedConfidenceLevel(value){
			if(!value){
				attemptConfidenceLevel++;
				if(attemptConfidenceLevel<=3){
					ExamMgmt.updateConfidenceLevel(elem.checked,userid,questionid,{callback:updatedConfidenceLevel,
				      	 timeout:callBackTimeOut,
							 errorHandler:function(message) { iTestUnlockPage('error');} });
			   }else{
				   elem.checked = !elem.checked;
				   attemptConfidenceLevel=0;
				   alertFunction("<fmt:message key="transactionError"/>");
				   iTestUnlockPage(true);
				}
			}else{
				iTestUnlockPage(true);
			}
	   }
	   iTestLockPage(true);
	   ExamMgmt.updateConfidenceLevel(elem.checked,userid,questionid,{callback:updatedConfidenceLevel,
      	 timeout:callBackTimeOut,
			 errorHandler:function(message) { iTestUnlockPage('error');} });
	}

   function alertFunction(message){
	   document.getElementById('alertMessage').innerHTML = message;
	   document.getElementById('divEmptyAlert').style.display = '';
	   document.getElementById('divFloatingAlert').style.display = '';
	}

   function hideAlertDivs(){
	   document.getElementById('divEmptyAlert').style.display = 'none';
	   document.getElementById('divFloatingAlert').style.display = 'none';
	}

   function saveFillAnswer(questionId,answerId){
	   function updateFillAnswer(value){
		   var textAnswer = document.getElementById('inputFillAnswer'+answerId).value;
		   if(!value){
			   attemptFill++;
			   if(attemptFill<=3){
				   ExamMgmt.updateFillAnswer(${exam.id},${user.id},questionId,answerId,textAnswer,{callback:updateFillAnswer,
				      	 timeout:callBackTimeOut,
						 errorHandler:function(message) {iTestUnlockPage('error');} });
			   }else{
				   document.getElementById('labelModified'+questionId).style.display="";
				   attemptFill=0;
				   alertFunction("<fmt:message key="transactionError"/>");
				   iTestUnlockPage(true);
				}
			}else{
		   		iTestUnlockPage(true);
	   		}
	   }
	   iTestLockPage(true);
		var textAnswer = document.getElementById('inputFillAnswer'+answerId).value;
		document.getElementById('labelModified'+questionId).style.display="none";
		ExamMgmt.updateFillAnswer(${exam.id},${user.id},questionId,answerId,textAnswer,{callback:updateFillAnswer,
      	 timeout:callBackTimeOut,
			 errorHandler:function(message) {iTestUnlockPage('error');} });
	}

   
</script>

<div id="contenido">

	<center>
	
    <p class="tituloExamen"> <u><fmt:message key="course"/> <c:out value="${exam.group.course.name}"/> (<c:out value="${exam.group.name}"/>)</u> <br/><br/>
    <c:out value="${exam.title}"/></p>
	
	<c:forEach items="${exam.questions}" var="question" varStatus="numQuestion">
		<script type='text/javascript'>
			questionMaxHits[${numQuestion.count}] = ${question.numCorrectAnswers};
			questionHits[${numQuestion.count}] = 0;
		</script>
		<c:set var="question" value="${question}" scope="request"/>
		<a name="pregunta${numQuestion.count}"></a>
		<jsp:include page="/WEB-INF/jsp/common/question.jsp" flush="true">
			<jsp:param name="view" value="exam"/>
		    <jsp:param name="numQuestion" value="${numQuestion.count}"/>
		    <jsp:param name="showCorrectAnswers" value="${exam.showNumCorrectAnswers}"/>
			<jsp:param name="ConfidenceLevel" value="${exam.confidenceLevel eq true}"/>
		</jsp:include>
	</c:forEach>
		
	</center>	
</div>

<div id="reloj">
	<form name="cd">
		<span style="font-size:8pt; background-color:#FFFFFF; font-family: Verdana, Arial, Helvetica, sans-serif; font-weight:bold"><fmt:message key="remainingTime" /></span></br>
	    <input id="aspectoReloj" readonly="true" type="text" value="" border="0" name="disp">
	    <img src="${pageContext.request.contextPath}/imagenes/visible.gif" alt="<fmt:message key="hideClock" />" title="<fmt:message key="hideClock" />" onclick="switchClock();" id="imgvisible"/>
	</form>
</div>

<script type="text/javascript">
<!--
   	tiempoExamen(<c:out value="${timeRemaining}"/>,
		'<c:out value="${pageContext.request.contextPath}/learner/newexam.itest?method=endExam"/>',
		'<c:out value="${pageContext.request.contextPath}/imagenes"/>',
		"<fmt:message key="showClock" />",
		"<fmt:message key="hideClock" />",
		"<fmt:message key="examEnded" />");
//-->
</script>
	

<div id="divEmptyAlert" class="floatingDiv" style="display:none; border-style: outset;">
	<div id="divFloatingAlert" class="floatingDivBody" style="display:none; height:200px; background-color:#A4A4A4;">
		<div align="left" style="top:0; background-color:#0B0B61; height:30px; border-style: outset;">
			<b><font align="left" color="white"><fmt:message key="labelWarning"/></font></b>
		</div>
		<div id="divAlertMessage" style="height:40%">
			<span style="width:25%;" align="center">
				<img src="${pageContext.request.contextPath}/imagenes/warning.gif" border="none" height="40px" width="40px"/>
			</span>
			<span style="width:70%;">
				<b id="alertMessage"></b>
			</span>
		</div>
		<div id="divAlertButton" style="height:30%; botom:0px; align-botom">
			<input type="button" value="<fmt:message key="labelAcept"/>" onclick="javascript:hideAlertDivs();"/>
		</div>
	</div>
</div>	
</body>
</html>
