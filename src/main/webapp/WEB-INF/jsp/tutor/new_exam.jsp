<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fmt_rt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>
<%@ page import="com.cesfelipesegundo.itis.model.Group" %>
<%@ page import="com.cesfelipesegundo.itis.model.ConfigExam" %>

<% 
	Group group = (Group)request.getAttribute("group");
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addStep(group.getCourse().getName()+" ("+group.getName()+")",request.getContextPath()+"/tutor/managegroup.itest?method=indexGroup");
	ConfigExam e = (ConfigExam)request.getAttribute("exam");
	if (e != null) {
		// Edition of question
		breadCrumb.addBundleStep("editExam","");
		// Edit question for show the minquestionGrade in positive value.
		e.setMinQuestionGrade(Math.abs(e.getMinQuestionGrade()));
		request.setAttribute("exam",e);
	} else {
		// Addition of questions
		breadCrumb.addBundleStep("newExam","");	
	}
	request.setAttribute("breadCrumb",breadCrumb);
%>


<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="tutor"/>
	<jsp:param name="menu" value="tutor"/>
</jsp:include>

	<!-- JavaScript Calendar -->
  	<script type='text/javascript' src='${pageContext.request.contextPath}/common/resources/scw.js'></script>
  	
	<!-- Ajax for exams -->
	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/ThemeListMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/TutorExamMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>
	
	<script type='text/javascript'>
	modified=false;
	failed=false;
	visibilidad = 1;
	
	function checkSavedExam(){
		if(modified){
			var conf = confirm ("<fmt:message key="msgConfirmSaveExam"/>");
			if(conf){
				saveExam();
				if(failed)
					return false;
				else
					return true;
			}else{
				return true;
			}
		}
		return true;
	}
	
	function disableDivMitadDer(){
		document.getElementById("fieldsetmitadder").style.backgroundColor = '#C0C0C0';
		document.getElementById("subject").disabled = true;
		document.getElementById("questionsNumber").disabled = true;
		document.getElementById("difficulty").disabled = true;
		document.getElementById("questionType").disabled =true;
		document.getElementById("answersPerQuestion").disabled = true;
		document.getElementById("submit").disabled = true;
		
		
	}
	function enableDivMitadDer(){
		document.getElementById("fieldsetmitadder").style.backgroundColor = '';
		document.getElementById("subject").disabled = false;
		document.getElementById("questionsNumber").disabled = false;
		document.getElementById("difficulty").disabled = false;
		document.getElementById("questionType").disabled =false;
		document.getElementById("answersPerQuestion").disabled = false;
		document.getElementById("submit").disabled = false;
	}
	function enableExamButtons(){
		document.getElementById("validateButton").disabled = false;
		document.getElementById("previewButton").disabled = false;
		document.getElementById("copyExamButton").disabled = false;
	}

	function showButtonRecorrect(value){
		if(value){
			document.getElementById('examRecorrectButton').style.display = "";
		}else{
			document.getElementById('examRecorrectButton').style.display = "none";
		}
		
	}

	function recorrectExam(idExam,idGroup){
		showButtonRecorrect(false);
		TutorExamMgmt.recorrectExam(idExam,showExamRecorrect);
		
		
	}


	function showExamRecorrect(list){
		if(list!=null){
			alert("<fmt:message key="alertExamReview"/> "+list.length);
		}else{
			alert("<fmt:message key="alertNoExamReview"/>");
		}
	}
	
			/* For Ajax */
		function saveExam() {
		   failed = false;
		   var examTitle = document.getElementById("examTitle").value;
		   examTitle = examTitle.replace(/^\s*|\s*$/g,"");   
		   if (examTitle == "") {
		   		alert ("<fmt:message key="msgExamTitleEmpty"/>");
		   		failed = true;
		   		return;
		   }
		   if (examTitle.length > 30) {
		   		alert ("<fmt:message key="msgExamTitleTooLong"/>");
		   		failed = true;
		   		return;
		   }		   
		   var duration = parseInt(document.getElementById("duration").value);
		   if (isNaN(duration) || duration <= 0) {
		   		alert ("<fmt:message key="msgExamDurError"/>");
		   		failed = true;
		   		return;
		   }
		   // Always "group" visibility
		   
		   var startDate = document.getElementById("startDate").value;
			var visible = document.getElementById("publishYes").checked;
			var customized;
			if(document.getElementById("customizedExam")!=null)
				customized = document.getElementById("customizedExam").checked;
			else
				customized = false;
		   if(visible)
		   if (startDate == "") {
		   		alert ("<fmt:message key="msgExamStartDateEmpty"/>");
		   		failed = true;
		   		return;
		   }
		   var startTimeH = document.getElementById("startTimeH").value;
		   var startTimeM = document.getElementById("startTimeM").value;

		   var startDateRev = document.getElementById("startDateRev").value;
		   if (startDateRev == "" && revisionActivada) {
		   		alert ("<fmt:message key="msgExamStartRevisionDateEmpty"/>");
		   		failed = true;
		   		return;
		   }
		   var startTimeRevH = document.getElementById("startTimeRevH").value;
		   var startTimeRevM = document.getElementById("startTimeRevM").value;
		   
		   var endDate = document.getElementById("endDate").value;
		   if(visible)
		   if (endDate == "") {
		   		alert ("<fmt:message key="msgExamEndDateEmpty"/>");
		   		failed = true;
		   		return;
		   }
		   var endTimeH = document.getElementById("endTimeH").value;
		   var endTimeM = document.getElementById("endTimeM").value;

		   var endDateRev = document.getElementById("endDateRev").value;
		   if (endDateRev == "" && revisionActivada) {
		   		alert ("<fmt:message key="msgExamEndDateRevsisionEmpty"/>");
		   		failed = true;
		   		return;
		   }
		   var endTimeRevH = document.getElementById("endTimeRevH").value;
		   var endTimeRevM = document.getElementById("endTimeRevM").value;
		   
		   var max = document.getElementById("max").value;
		   if ((max == "") || isNaN(max) || (max < 0) || (max >99)) {
		   		alert ("<fmt:message key="msgExamMaxGradeEmpty"/>");
		   		failed = true;
		   		return;
		   }

			//Recoger los valores de las nuevos atributos de un examen
			var correccionParcialBoolean= document.getElementById("correccionParcialYes").checked;
<%--
			/* Partial correction and penalization currently disabled and hidden to the user */
			var penalizeIncorrectValue=document.getElementById("inputPenalizeIncorrectValue").value;//meter en la llamada a TutorExamMgmt.saveConfigExam(....	
			var	minQuestionGradeValue=document.getElementById("inputMinQuestionGradeValue").value;//meter en la llamada a TutorExamMgmt.saveConfigExam(....
			if (((minQuestionGradeValue < 0) || (minQuestionGradeValue == "")) || isNaN(minQuestionGradeValue)) {
		   		alert ('<fmt:message key="msgExamMinGradeEmpty"/>');
		   		return;
		    }
		    minQuestionGradeValue=0-minQuestionGradeValue;
--%>
			var penalizeIncorrectValue=0;
			var	minQuestionGradeValue=0;

			var penalizeIncorrectQuestionValue=document.getElementById("inputPenalizeIncorrectQuestionValue").value;//meter en la llamada a TutorExamMgmt.saveConfigExam(....
			if (((penalizeIncorrectQuestionValue < 0) || (penalizeIncorrectQuestionValue > 100) || (penalizeIncorrectQuestionValue == "")) || isNaN(penalizeIncorrectQuestionValue)) {
		   		alert ("<fmt:message key="msgExamIncorrectPenaliz"/>");
		   		failed = true;
		   		return;
		    }
		    // Normalized to 1:
		    penalizeIncorrectQuestionValue = penalizeIncorrectQuestionValue / 100;
		    
			var penalizeNoRespondedValue=document.getElementById("inputPenalizeNoRespondedValue").value;//meter en la llamada a TutorExamMgmt.saveConfigExam(....
			if (((penalizeNoRespondedValue < 0) || (penalizeNoRespondedValue > 100) || (penalizeNoRespondedValue == "")) || isNaN(penalizeNoRespondedValue)) {
		   		alert ("<fmt:message key="msgExamIncorrectPenaliz"/>");
		   		failed = true;
		   		return;
		    }
		    // Normalized to 1:
		    penalizeNoRespondedValue = penalizeNoRespondedValue / 100;
		    
		    var showAnswerCorrectsValue=document.getElementById("inputShowNumAnswerCorrects").checked;//meter en la llamada a TutorExamMgmt.saveConfigExam(....
		   // Save the exam using Ajax
		   
		   

			var confidencePenalization = document.getElementById('PenalizeConfidenceLevel').value;
			if(((confidencePenalization < 0) || (confidencePenalization > 100) || (confidencePenalization == "")) || isNaN(confidencePenalization)){
				alert("<fmt:message key="msgExamIncorrectPenaliz"/>");
				failed = true;
				return;
			}
			confidencePenalization /= 100; 

			var confidenceReward = document.getElementById('RewardConfidenceLevel').value;
			if(((confidenceReward < 0) || (confidenceReward > 100) || (confidenceReward == "")) || isNaN(confidenceReward)){
				alert("<fmt:message key="msgExamIncorrectPenaliz"/>");
				failed = true;
				return;
			}
			confidenceReward /= 100; 
			var d1 = new Date(20+startDate.split("/")[2],startDate.split("/")[1]-1,startDate.split("/")[0],startTimeH,startTimeM,0);
			var d2 = new Date(20+endDate.split("/")[2],endDate.split("/")[1]-1,endDate.split("/")[0],endTimeH,endTimeM,0);
			var today = new Date();
			var guardar = true;
			var published = document.getElementById('radioPublishedTrue').checked;
			if(d1>d2 || d2<today){
				guardar = confirm("<fmt:message key="labelExamDates"/>\n<fmt:message key="confirmSaveConfigExam"/>")
			}
			revisionActivada = document.getElementById('revYes').checked;
			if(guardar){
				<c:if test="${user.role eq 'TUTORAV' or empty exam}">
					iTestLockPage('');
					TutorExamMgmt.saveConfigExam(${group.id},examTitle,duration,visibilidad,startDate,startTimeH,startTimeM,endDate,endTimeH,endTimeM,startDateRev,startTimeRevH,startTimeRevM,endDateRev,endTimeRevH,endTimeRevM,max,0,revisionActivada,correccionParcialBoolean,showAnswerCorrectsValue,penalizeIncorrectQuestionValue,penalizeNoRespondedValue,penalizeIncorrectValue,minQuestionGradeValue,confidencePenalization,confidenceReward,customized,published,{callback:checkSavedConfigExam,
			       		timeout:callBackTimeOut,
			       		errorHandler:function(message) { iTestUnlockPage('error');} });
				</c:if>
				<c:if test="${!(user.role eq 'TUTORAV' or empty exam)}">
					iTestLockPage('');
					TutorExamMgmt.saveConfigExam(${group.id},examTitle,duration,visibilidad,startDate,startTimeH,startTimeM,endDate,endTimeH,endTimeM,startDateRev,startTimeRevH,startTimeRevM,endDateRev,endTimeRevH,endTimeRevM,max,0,revisionActivada,correccionParcialBoolean,showAnswerCorrectsValue,penalizeIncorrectQuestionValue,penalizeNoRespondedValue,penalizeIncorrectValue,minQuestionGradeValue,${exam.penConfidenceLevel},${exam.rewardConfidenceLevel},${exam.customized},published,{callback:checkSavedConfigExam,
                        timeout:callBackTimeOut,
                        errorHandler:function(message) { iTestUnlockPage('error');} });
                </c:if>
			}
			if(!customized){
				document.getElementById('customUserTableBody').innerHTML='';
				iTestLockPage('');
				TutorExamMgmt.getUsersNotInCustomExam(${group.id},{callback:updateCustomSelect,
					timeout:callBackTimeOut,
					 errorHandler:function(message) { iTestUnlockPage('error');} });
			}
		   
		} // saveExam

		revisionActivada = false;
		
		function checkSavedConfigExam(saved){
			if(saved){
				 // We show the hidden divs...
			       // Theme including
				   enableDivMitadDer();
				   // Themes list
				   document.getElementById("divthemeslist").style.backgroundColor="";
				   
				   enableExamButtons();
				   unLockDivUserCustom();
				   // Exam not modified
				   examModified(false);
				   failed = false;
				alert("<fmt:message key="msgSavedExam"/>");
			}else{
				alert("<fmt:message key="msgNoSavedExam"/>");
			}
			iTestUnlockPage();
		}
			
				
		// Updates a flag to let the user know that the exam was modified or not
		function examModified(value) {
		    var newlegend;
		    modified = value;
			// New legend:
 		    newlegend=document.createElement('label');
			newlegend.setAttribute("id","labelE_modified");
	   	    newlegend.innerHTML = "<fmt:message key="labelModified"/>"
			if (value) {
			   // The question was modified
			   if (!document.getElementById("labelE_modified"))
			   		document.getElementById("labelE").appendChild(newlegend);
			} else {
			   // The question was saved
			   if (document.getElementById("labelE_modified"))
			   		document.getElementById("labelE").removeChild(document.getElementById("labelE_modified"));
			}
						
		} // examModified
		
		/* Callback function, Ajax for the theme being inserted/deleted (receives the list of themes) */
		function updateListThemes(themes) {
		  /*
		     This is a callback function that updates the information about the themes included
		     in the exam configuration
		  */
  		  var datatable, rowelement, datatable, cellelementCount, cellelementText, cellelementDel;		  			  
   		  // Themes counter: needed to ?
		  var countThemes = 1;

	       // First we show the hidden divs...
		   document.getElementById("divthemeslist").style.display="";

		   tbodyelement=document.createElement('tbody');
		   tbodyelement.setAttribute("id","themestabletbody");
		  	   
		   // Fills the table (DOM scripting): headers
		   rowelementH = document.createElement('tr');
           headelement = document.createElement('th');
           headelement.innerHTML = "&nbsp;";
           rowelementH.appendChild(headelement);
           headelement = document.createElement('th');
           headelement.innerHTML = "<fmt:message key="theme"/>";
           rowelementH.appendChild(headelement);
           headelement = document.createElement('th');
           headelement.innerHTML = "<fmt:message key="difficulty"/>";
           rowelementH.appendChild(headelement);
           headelement = document.createElement('th');
           headelement.innerHTML = "<fmt:message key="questionsNumber"/>";
           rowelementH.appendChild(headelement);
           headelement = document.createElement('th');
           headelement.innerHTML = "<fmt:message key="answersNumber"/>";
           rowelementH.appendChild(headelement);
           headelement = document.createElement('th');
           headelement.innerHTML = "&nbsp;";
           rowelementH.appendChild(headelement);
           headelement = document.createElement('th');
           headelement.innerHTML = "&nbsp;";
           rowelementH.appendChild(headelement);
  
  		   // Add headers
		   tbodyelement.appendChild(rowelementH);

		   // Fills the table (DOM scripting): answers data ---------------
		   var position = 0;
		   
		   while (countThemes <= themes.length) {
		        theme = themes[position];
		        
				rowelement = document.createElement('tr');
				
				// All the cells:
				cellelementCount = document.createElement('td');
				cellelementCount.setAttribute("id","tema"+theme.id);
				//cellelementCount.innerHTML = countThemes+".&nbsp;";
				cellelementCount.innerHTML = (position+1)+".&nbsp;";
				
				// Counter:
				countThemes++;
				rowelement.appendChild(cellelementCount);
				
				// Title of the theme
				cellelementText = document.createElement('td');
				cellelementText.innerHTML = theme.subject.subject;
				rowelement.appendChild(cellelementText);
				
				// Difficulty of the questions for this theme
				cellelementText = document.createElement('td');
				switch (theme.maxDifficulty) {
					case 0: 
						cellelementText.innerHTML = "<fmt:message key="diffLow"/>";
						break;
					case 1: 
						cellelementText.innerHTML = "<fmt:message key="diffMedium"/>";
						break;
					case 2: 
						cellelementText.innerHTML = "<fmt:message key="diffHigh"/>";
						break;
					default:
						cellelementText.innerHTML = "ERROR";
						break;
				}
				rowelement.appendChild(cellelementText);

				// Number of questions for this theme
				cellelementText = document.createElement('td');
				cellelementText.innerHTML = theme.questionsNumber;
				rowelement.appendChild(cellelementText);

				// Correct answers per question for this theme
				cellelementText = document.createElement('td');
				cellelementText.innerHTML = theme.answersxQuestionNumber;
				rowelement.appendChild(cellelementText);

				cellelementText = document.createElement('td');
				if(theme.questionType == 0)
					cellelementText.innerHTML = "<fmt:message key="labelQuestionTest"/>";
				else
					cellelementText.innerHTML = "<fmt:message key="labelQuestionFill"/>";
				rowelement.appendChild(cellelementText);
						
				// Control element: delete theme
				cellelementDel = document.createElement('td');
				cellelementDel.innerHTML = "<a href=\"javascript:deleteTheme('"+theme.id+"');\"><img src=\"${pageContext.request.contextPath}/imagenes/borrar.gif\" alt=\"<fmt:message key="delete"/>\" border=\"none\"></a>";
				rowelement.appendChild(cellelementDel);
				
				// Add row
				tbodyelement.appendChild(rowelement);
				
				position++;
			} // while
			
			// Replaces tbody
			datatable=document.getElementById("themestable");
			datatable.replaceChild(tbodyelement,document.getElementById("themestabletbody"));
			
			// Hides the div to avoid double-click
		    iTestUnlockPage();
			
		}
		
		
		function includeTheme() {
			var subject = document.getElementById("subject").value;
			var difficulty = document.getElementById("difficulty").value;
			var answersPerQuestion = document.getElementById("answersPerQuestion").value;
			var questionsNumber = document.getElementById("questionsNumber").value;
			var questionType = document.getElementById('questionType').value
			var select = document.getElementById("subject");
			var tema = select[select.selectedIndex].innerHTML;
			tema = tema.substring(tema.search(" "),tema.length);
			tema = tema.replace(/^\s+|\s+$/g,"");
			var select2 = document.getElementById("difficulty");
			var select3 = document.getElementById("questionType");
			var dificultad = select2[select2.selectedIndex].innerHTML;
			var tipoPregunta = select3[select3.selectedIndex].innerHTML;
			dificultad = dificultad.replace(/^\s+|\s+$/g,"");
			tipoPregunta = tipoPregunta.replace(/^\s+|\s+$/g,"");
			// Validating data
			if (subject == "") {
		   		alert ("<fmt:message key="msgExamSubjectError"/>");
		   		return;
			}
			
			if ((isNaN(questionsNumber)) || (questionsNumber <= 0)) {
		   		alert ("<fmt:message key="msgExamQuestionNumError"/>");
		   		return;
		    }	
		    
			if ((isNaN(answersPerQuestion)) || (answersPerQuestion <= 0)) {
		   		alert ("<fmt:message key="msgExamAnswerNumError"/>");
		   		return;
		    }
			var noAniadir = false;
		    var tableBody = document.getElementById('themestabletbody');
			var rows = tableBody.getElementsByTagName('tr');
			for(var i=1;i<rows.length;i++){
				var columns = rows[i].getElementsByTagName('td');
				var temaAux = columns[1].innerHTML;
				temaAux = temaAux.replace(/^\s+|\s+$/g,"");
				var difAux = columns[2].innerHTML; 
				difAux = difAux.replace(/^\s+|\s+$/g,"");
				var tipoPreguntaAux = columns[5].innerHTML; 
				tipoPreguntaAux = tipoPreguntaAux.replace(/^\s+|\s+$/g,"");
				if(temaAux == tema && difAux == dificultad && tipoPregunta == tipoPreguntaAux)
					noAniadir = true;
			}
			if(!noAniadir){
				// Puts a div to avoid double-click
		        iTestLockPage();
		        
				TutorExamMgmt.addConfigExamTheme(subject,answersPerQuestion,difficulty,difficulty,questionsNumber,questionType,updateListThemes);
			}else{
				alert("<fmt:message key="alertSameThemeAndDifficulty"/>")
			}
		} // addConfigExamTheme
		
		function deleteTheme(id) {
		    // Puts a div to avoid double-click
		    var pos = document.getElementById('tema'+id).innerHTML.split('.')[0];
		    var conf = confirm ("<fmt:message key="msgConfirmDeleteTheme"/>"+pos+"?\n<fmt:message key="msgConfirmDeleteTheme2"/>");
	        if(conf){
	        	iTestLockPage();
		        showButtonRecorrect(true);
				// Delete
				TutorExamMgmt.deleteConfigExamTheme(id,${group.id},updateListThemes);
		    }
		} // deleteConfigExamTheme
			
	    function validateExam() {
	    	window.open("${pageContext.request.contextPath}/tutor/managegroup.itest?method=validateExam", "itest_validate", "width="+(screen.availWidth*6/7)+",height="+(screen.availHeight*2/3)+",top="+(screen.availHeight/6)+",left="+(screen.availWidth/14)+", scrollbars=yes");	  		
	    } // validateExam

	    function previewExam() {
	    	window.open("${pageContext.request.contextPath}/tutor/managegroup.itest?method=previewExam", "itest_preview", "width="+(screen.availWidth)+",height="+(screen.availHeight*2/3)+",top="+(screen.availHeight/6)+",left=0,scrollbars=yes");
	    } // previewExam

	    function showCustomizeAdv(dis) {
		    //Por defecto aparece desactivada la parte de configuracion de penalizaciones
	
		    if (dis){
		    	document.getElementById("ShowCustomizeAdvanced").style.display="none";
		    	document.getElementById("NoShowCustomizeAdvanced").style.display="";
		    	document.getElementById("CustomizeAdvanced").style.display="";
		    	document.getElementById("ShowNumAnswerCorrect").style.display="";
		    	var correccionParcialBoolean= document.getElementById("correccionParcialYes").checked;
		    	if (correccionParcialBoolean) {
		    		setDisabledCustomizedPartialCorrection(true);
		    	}
		    	else{
		    		setDisabledCustomizedPartialCorrection(false);	
		    	}
		    }
		    else{
		    document.getElementById("CustomizeAdvanced").style.display="none";
		    document.getElementById("ShowCustomizeAdvanced").style.display="";
	    	document.getElementById("NoShowCustomizeAdvanced").style.display="none";
	    	document.getElementById("CustomizePenalizePartialCorrectionYes").style.display="none";    
	    	document.getElementById("CustomizePenalizePartialCorrectionNo").style.display="none"; 
	    	document.getElementById("ShowNumAnswerCorrect").style.display="none"; 
	    	
	    	}
		}
	    
	    function setDisabledCustomizedPartialCorrection(dis) {
		    if (dis){
		    	document.getElementById("CustomizePenalizePartialCorrectionYes").style.display="";
		    	document.getElementById("CustomizePenalizePartialCorrectionNo").style.display="none";
		    	document.getElementById("inputShowNumAnswerCorrects").disabled = true;
		    	document.getElementById("inputShowNumAnswerCorrects").checked = true;
		    }
		    else{
		    		document.getElementById("CustomizePenalizePartialCorrectionYes").style.display="none";
			    	document.getElementById("CustomizePenalizePartialCorrectionNo").style.display="";
			    	document.getElementById("inputShowNumAnswerCorrects").disabled = false;
	    	}
		}
		
		//This function show a message for to confirm you want to copy the current Exam
		function copyExam(context){
			if (confirm("<fmt:message key="msgConfirmCopyExam"/> "+document.getElementById("examTitle").value +" <fmt:message key="msgConfirmChar"/>")){
				 window.location=context+'/tutor/manageconfigexam.itest?method=configExamCopy';
			    }
		}

		//This funcion use Ajax to show number of questions aviables for each theme
		function showAsks(){
			var idTheme = document.getElementById("subject").value;
			var questionType = document.getElementById("questionType").value;
			if(questionType==0){
				$("#answersPerQuestion").val('3');
				$("#answerPerQuestionRow").show('slow',function(){});
			}else{
				$("#answerPerQuestionRow").hide(0,function(){});
				$("#answersPerQuestion").val('1');
			}
			if(idTheme!=""){
				iTestLockPage('');
				ThemeListMgmt.showDetailsTheme(idTheme,questionType,${group.id},{callback:showDetails,
									timeout:callBackTimeOut,
									errorHandler:function(message) { iTestUnlockPage('error');} });
			}else{
				document.getElementById("fieldsetTheme").style.display="none";
			}
			//
		}

		function showDetails(detallesTema){
			var subject = document.getElementById("subject");
			var array = subject.options[subject.selectedIndex].innerHTML.split('. ');
			var subjectValue ='';
			if(array.length>2){
				for(var i=1;i<array.length;i++){
					subjectValue+=(array[i]+'. ');
				}
			}else{
				subjectValue+=array[1];
			}
			document.getElementById("fieldsetTheme").style.display="";
			document.getElementById("legendTheme").innerHTML = subjectValue;
			document.getElementById("detailsTableBody").innerHTML='';

			rowelement = document.createElement('tr');
			cellelementCount = document.createElement('td');
			cellelementCount.innerHTML = "<fmt:message key="difficultyLow"/>"
			cellelementCount.setAttribute("width","5%");
			cellelementCount.setAttribute("align","center");
			rowelement.appendChild(cellelementCount);	

			cellelementCount = document.createElement('td');
			cellelementCount.innerHTML = detallesTema.difficultyLow;
			cellelementCount.setAttribute("width","5%");
			cellelementCount.setAttribute("align","center");
			rowelement.appendChild(cellelementCount);
			
			cellelementCount = document.createElement('td');
			cellelementCount.innerHTML = detallesTema.answerDifficultyLow;
			cellelementCount.setAttribute("width","5%");
			cellelementCount.setAttribute("align","center");
			rowelement.appendChild(cellelementCount);

			cellelementCount = document.createElement('td');
			cellelementCount.innerHTML = detallesTema.totalQuestionLow;
			cellelementCount.setAttribute("width","5%");
			cellelementCount.setAttribute("align","center");
			rowelement.appendChild(cellelementCount);
			
			document.getElementById("detailsTableBody").appendChild(rowelement);

			rowelement = document.createElement('tr');
			cellelementCount = document.createElement('td');
			cellelementCount.innerHTML = "<fmt:message key="difficultyMedium"/>"
			cellelementCount.setAttribute("width","5%");
			cellelementCount.setAttribute("align","center");
			rowelement.appendChild(cellelementCount);	

			cellelementCount = document.createElement('td');
			cellelementCount.innerHTML = detallesTema.difficultyMedium;
			cellelementCount.setAttribute("width","5%");
			cellelementCount.setAttribute("align","center");
			rowelement.appendChild(cellelementCount);
			
			cellelementCount = document.createElement('td');
			cellelementCount.innerHTML = detallesTema.answerDifficultyMedium;
			cellelementCount.setAttribute("width","5%");
			cellelementCount.setAttribute("align","center");
			rowelement.appendChild(cellelementCount);

			cellelementCount = document.createElement('td');
			cellelementCount.innerHTML = detallesTema.totalQuestionMedium;
			cellelementCount.setAttribute("width","5%");
			cellelementCount.setAttribute("align","center");
			rowelement.appendChild(cellelementCount);

			document.getElementById("detailsTableBody").appendChild(rowelement);

			rowelement = document.createElement('tr');
			cellelementCount = document.createElement('td');
			cellelementCount.innerHTML = "<fmt:message key="difficultyHigh"/>"
			cellelementCount.setAttribute("width","5%");
			cellelementCount.setAttribute("align","center");
			rowelement.appendChild(cellelementCount);	

			cellelementCount = document.createElement('td');
			cellelementCount.innerHTML = detallesTema.difficultyHigh;
			cellelementCount.setAttribute("width","5%");
			cellelementCount.setAttribute("align","center");
			rowelement.appendChild(cellelementCount);
			
			cellelementCount = document.createElement('td');
			cellelementCount.innerHTML = detallesTema.answerDifficultyHigh;
			cellelementCount.setAttribute("width","5%");
			cellelementCount.setAttribute("align","center");
			rowelement.appendChild(cellelementCount);

			cellelementCount = document.createElement('td');
			cellelementCount.innerHTML = detallesTema.totalQuestionHigh;
			cellelementCount.setAttribute("width","5%");
			cellelementCount.setAttribute("align","center");
			rowelement.appendChild(cellelementCount);

			document.getElementById("detailsTableBody").appendChild(rowelement);
			iTestUnlockPage();
		}

		function showConfidenceLevelDiv(show){
			if(show){
				document.getElementById('showConfidenceLevel').style.display='none';
				document.getElementById('hideConfidenceLevel').style.display='';
				document.getElementById('ConfidenceLevelDiv').style.display='';
			}
			else{
				document.getElementById('showConfidenceLevel').style.display='';
				document.getElementById('hideConfidenceLevel').style.display='none';
				document.getElementById('ConfidenceLevelDiv').style.display='none';
			}
			return false;
		}

		function checkReview(mostrar){
			if(mostrar){
				document.getElementById('columnEndDateRev').style.display='';
				document.getElementById('columnStartDateRev').style.display='';
			}else{
				document.getElementById('columnEndDateRev').style.display='none';
				document.getElementById('columnStartDateRev').style.display='none';
			}
			examModified(true);
			revisionActivada = mostrar;
		}

		function showFeaturesExam(mostrar){
			if(mostrar){
				visibilidad = 1;
				document.getElementById('startDateTr').style.display='';
				document.getElementById('endDateTr').style.display='';
			}else{
				visibilidad = 0;
				document.getElementById('startDateTr').style.display='none';
				document.getElementById('endDateTr').style.display='none';
			}
		}

		function setPublished(publicar){
			if(publicar){
				document.getElementById('noPublished').style.display='none';
				document.getElementById('published').style.display='';
			}else{
				document.getElementById('noPublished').style.display='';
				document.getElementById('published').style.display='none';
			}
			TutorExamMgmt.setPublished(publicar);
		}

		function activeConfidenceLevel(activar){
			examModified(true);
			if(activar){
				document.getElementById('divConfidenceLevel').style.display="";
			}else{
				document.getElementById('divConfidenceLevel').style.display="none";
			}
			TutorExamMgmt.activeConfidenceLevel(activar);
			examModified(true);
		}

		function setCustomized(customized){
			var conf = true;
			if(!customized){
				conf = confirm("<fmt:message key="confirmSetCustomized"/>");
			}
			if(conf){
				iTestLockPage('');
				document.getElementById('customUserTableBody').innerHTML = '';
				var usersDiv = document.getElementById('divUserCustom');
				var themesDiv = document.getElementById('divthemeslist');
				if(customized){
					usersDiv.style.display='';
					themesDiv.style.width='50%';
				}else{
					usersDiv.style.display='none';
					themesDiv.style.width='100%'
				}
				TutorExamMgmt.setCurrentExamCustomized(${group.id},customized,{callback:updateCustomSelect,
					timeout:callBackTimeOut,
					 errorHandler:function(message) { iTestUnlockPage('error');} });
			}else{
				document.getElementById('customizedExam').checked = true;
			}
		}

		function addUser2CustomUserList(){
			iTestLockPage('');
			var select = document.getElementById('customUserSelect');
			var idUser = select.value;
			select.remove(select.selectedIndex);
			TutorExamMgmt.addUser2CustomExam(idUser,{callback:updateCustomUserTable,
				timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
		}

		function updateCustomUserTable(users){
			if(users!=null){
				var body = document.getElementById('customUserTableBody');
				body.innerHTML="";
				for(var i=0;i<users.length;i++){
					var user = users[i];
					var tr = document.createElement('tr');
					tr.id ="customUserColumn"+user.id;
					var td = document.createElement('td');
					td.innerHTML=user.persId;
					tr.appendChild(td);

					td = document.createElement('td');
					td.innerHTML=user.surname;
					tr.appendChild(td);

					td = document.createElement('td');
					td.innerHTML=user.name;
					tr.appendChild(td);

					td = document.createElement('td');
					td.innerHTML=user.userName;
					tr.appendChild(td);

					td = document.createElement('td');
					if(user.inExam){
						td.innerHTML="<img src=\"${pageContext.request.contextPath}/imagenes/forb_dot.gif \" border=\"none\"/>";
					}else{
						td.innerHTML="<a href=\"javascript:removeUserFromCustomExam('"+user.id+"')\"><img src=\"${pageContext.request.contextPath}/imagenes/borrar.gif \" border=\"none\"/></a>";
					}
					tr.appendChild(td);

					body.appendChild(tr);
				}
			}
			iTestUnlockPage();
		}

		function removeUserFromCustomExam(userId){
			iTestLockPage('');
			document.getElementById('customUserTableBody').removeChild(document.getElementById("customUserColumn"+userId));
			TutorExamMgmt.removeUserFromCustomExam(userId,${group.id},{callback:updateCustomSelect,
				timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
		}

		function updateCustomSelect(usuarios){
			if(usuarios!=null){
				var select = document.getElementById('customUserSelect');
				select.innerHTML="";
				for(var i=0;i<usuarios.length;i++){
					var option = document.createElement('option');
					var user = usuarios[i];
					option.value=user.id
					option.innerHTML=user.surname+", "+user.name;
					select.appendChild(option);
				}
			}
			iTestUnlockPage();
		}

		function unLockDivUserCustom(){
			var select = document.getElementById('customUserSelect');
			var button = document.getElementById('buttonCustomUser');
			document.getElementById('divUserCustom').style.backgroundColor='';
			select.disabled='';
			button.disabled='';
		}
	</script>
	<c:if test="${!empty exam}">
		<script type='text/javascript'>
			visibilidad = ${exam.visibility};
		</script>
	</c:if>
		<div id="contenido">
		
			<div style="width: 100%; position: relative;">
				<div class="divmitadizq">
				  <form name="formconfigexam" method="post" action="javascript:saveExam();">
					<fieldset class="setespecial"><legend id="labelE"><fmt:message key="examFeatures"/></legend>
						<table id="featuresExam" class="listado">
						<col width="40%">
						<col width="40%">
						<col width="20%">
						<tbody>
							<tr>
								<td><label><fmt:message key="courseAndGroup"/></label><hr></td>
								<td colspan="2"><label><c:out value="${group.course.name}"/> (<c:out value="${group.name}"/>)</label><hr></td>
							</tr>
							<tr>
								<td><fmt:message key="examTitle"/>:</td>
								<td colspan="2">
									<c:choose>
										<c:when test="${!empty exam}">
											<input name="tituloexamen" type="text" size="30" id="examTitle" onkeydown="javascript:examModified(true);" value="${exam.title}"/>
										</c:when>
										<c:otherwise>
											<input name="tituloexamen" type="text" size="30" id="examTitle" onkeydown="javascript:examModified(true);"/>
										</c:otherwise>
									</c:choose>
								</td>
						</tr>
						<tr>
							<td><label><fmt:message key="duration"/>:</label></td>
							<td colspan="2">
							  	<c:choose>
							  		<c:when test="${!empty exam}">
							  			<input name="selectduration" type="text" maxlength="3" size="4" id="duration" onkeydown="javascript:examModified(true);" value="${exam.duration}"/>
							  		</c:when>
							  		<c:otherwise>
							  			<input name="selectduration" type="text" maxlength="3" size="4" id="duration" onkeydown="javascript:examModified(true);" value="0" />
							  		</c:otherwise>
							  	</c:choose>
							<!-- 
								<select name="selectduracion" id="duration" onchange="javascript:examModified(true);">
								  <option selected="selected">5</option>
								  <option>10</option>
								  <option>15</option>
								  <option>20</option>
								  <option>25</option>
								  <option>30</option>
								  <option>40</option>
								  <option>50</option>
								  <option>60</option>
							   </select> --> 
								<fmt:message key="minutes"/>
							</td>
						</tr>
						<tr>
							<td><fmt:message key="labelActiveExam"/></td>
							<td colspan="2">
								<c:choose>
									<c:when test="${empty exam or !empty exam && exam.visibility eq 1}">
										<fmt:message key="partialCorrectionYes"/><input type="radio" id="publishYes" name="radioPublish" onclick="javascript:showFeaturesExam(true);" checked/>
										<fmt:message key="partialCorrectionNo"/><input type="radio" id="publishNo" name="radioPublish" onclick="javascript:showFeaturesExam(false);"/>
									</c:when>
									<c:otherwise>
										<fmt:message key="partialCorrectionYes"/><input type="radio" id="publishYes" name="radioPublish" onclick="javascript:showFeaturesExam(true);"/>
										<fmt:message key="partialCorrectionNo"/><input type="radio" id="publishNo" name="radioPublish" onclick="javascript:showFeaturesExam(false);" checked/>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<c:choose>
							<c:when test="${empty exam or !empty exam && exam.visibility eq 1}">
								<tr id="startDateTr">
							</c:when>
							<c:otherwise>
								<tr id="startDateTr" style="display:none">
							</c:otherwise>
						</c:choose>
							<td><label><fmt:message key="startDate"/>:</label></td>
							<td colspan="2">
							  	<c:choose>
							  		<c:when test="${!empty exam}">
							  			<input id="startDate" name="textfechaini" size="8" readonly maxlength="8" type="text" value="<fmt_rt:formatDate value='${exam.startDate}' type='date' dateStyle='short' />"/>
							  		</c:when>
							  		<c:otherwise>
							  			<input id="startDate" name="textfechaini" value="" size="8" readonly maxlength="8" type="text"/>
							  		</c:otherwise>
							  	</c:choose>
							  	&nbsp;<img src="${pageContext.request.contextPath}/imagenes/scw.gif" onclick="javascript:scwShow(getElementById('startDate'),this);examModified(true);"/>&nbsp;&nbsp;&nbsp;
							  <select id="startTimeH" onchange="javascript:examModified(true);">
							  	<c:forEach var="i" begin="0" end="23">
							  		<c:choose>
							  			<c:when test="${!empty exam and exam.startDate.hours eq i}">
							  				<option selected="selected" value="<c:out value="${i}"/>"><c:out value="${i}"/></option>
							  			</c:when>
							  			<c:otherwise>
							  				<option value="<c:out value="${i}"/>"><c:out value="${i}"/></option>
							  			</c:otherwise>
							  		</c:choose>
							  	</c:forEach>
							   </select>:
							  <select id="startTimeM" onchange="javascript:examModified(true);">
							  	<c:forEach var="i" begin="0" end="59" step="5">
							  		<c:choose>
							  			<c:when test="${!empty exam and exam.startDate.minutes eq i}">
							  				<option selected="selected" value="<c:out value="${i}"/>"><c:if test="${i lt 10}">0</c:if><c:out value="${i}"/></option>
							  			</c:when>
							  			<c:otherwise>
							  				<option value="<c:out value="${i}"/>"><c:if test="${i lt 10}">0</c:if><c:out value="${i}"/></option>
							  			</c:otherwise>
							  		</c:choose>
							  	</c:forEach>
							   </select>
							</td>
						</tr>
						<c:choose>
							<c:when test="${empty exam or !empty exam && exam.visibility eq 1}">
								<tr id="endDateTr">
							</c:when>
							<c:otherwise>
								<tr id="endDateTr" style="display:none">
							</c:otherwise>
						</c:choose>
							<td><label><fmt:message key="endDate"/>:</label></td>
							<td colspan="2">
							  	<c:choose>
							  		<c:when test="${!empty exam}">
							  			<input id="endDate" name="textfechafin" size="8" readonly maxlength="8" type="text" value="<fmt_rt:formatDate value='${exam.endDate}' type='date' dateStyle='short'/>"/>
							  		</c:when>
							  		<c:otherwise>
							  			<input id="endDate" name="textfechafin" value="" size="8" readonly maxlength="8" type="text"/>
							  		</c:otherwise>
							  	</c:choose>
							  	&nbsp;<img src="${pageContext.request.contextPath}/imagenes/scw.gif" onclick="javascript:scwShow(getElementById('endDate'),this);examModified(true);"/>&nbsp;&nbsp;&nbsp;
							  <select id="endTimeH" onchange="javascript:examModified(true);">
							  	<c:forEach var="i" begin="0" end="23">
							  		<c:choose>
							  			<c:when test="${!empty exam and exam.endDate.hours eq i}">
							  				<option selected="selected" value="<c:out value="${i}"/>"><c:out value="${i}"/></option>
							  			</c:when>
							  			<c:otherwise>
							  				<option value="<c:out value="${i}"/>"><c:out value="${i}"/></option>
							  			</c:otherwise>
							  		</c:choose>
							  	</c:forEach>
							   </select>:
							  <select id="endTimeM" onchange="javascript:examModified(true);">
							  	<c:forEach var="i" begin="0" end="59" step="5">
							  		<c:choose>
							  			<c:when test="${!empty exam and exam.endDate.minutes eq i}">
							  				<option selected="selected" value="<c:out value="${i}"/>"><c:if test="${i lt 10}">0</c:if><c:out value="${i}"/></option>
							  			</c:when>
							  			<c:otherwise>
							  				<option value="<c:out value="${i}"/>"><c:if test="${i lt 10}">0</c:if><c:out value="${i}"/></option>
							  			</c:otherwise>
							  		</c:choose>
							  	</c:forEach>
							   </select>
							</td>
						</tr>
						<tr>
							<td><fmt:message key="labelPostExam"/>:</td>
							<td>
								<c:choose>
									<c:when test="${empty exam or !empty exam && exam.published eq true}">
										<fmt:message key="partialCorrectionYes"/><input id="radioPublishedTrue" type="radio" name="radioVisibility" onclick="javascript:setPublished(true);" checked/>
										<fmt:message key="partialCorrectionNo"/><input type="radio" name="radioVisibility" onclick="javascript:setPublished(false);"/>
									</c:when>
									<c:otherwise>
										<fmt:message key="partialCorrectionYes"/><input id="radioPublishedTrue" type="radio" name="radioVisibility" onclick="javascript:setPublished(true);"/>
										<fmt:message key="partialCorrectionNo"/><input type="radio" name="radioVisibility" onclick="javascript:setPublished(false);" checked/>
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${empty exam or !empty exam && exam.published eq true}">
										<a id="published"><img src="${pageContext.request.contextPath}/imagenes/visible.gif" border="none"></a>
										<a id="noPublished" style="display:none"><img src="${pageContext.request.contextPath}/imagenes/invisible.gif" border="none"></a>
									</c:when>
									<c:otherwise>
										<a id="published" style="display:none"><img src="${pageContext.request.contextPath}/imagenes/visible.gif" border="none"></a>
										<a id="noPublished"><img src="${pageContext.request.contextPath}/imagenes/invisible.gif" border="none"></a>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<c:if test="${user.role eq 'TUTORAV'}">
							<tr>
								<td><fmt:message key="labelCustomizedExam"/></td>
								<td>
									<c:choose>
										<c:when test="${!empty exam && exam.customized eq true}">
											<fmt:message key="labelYes"/><input id="customizedExam" type="radio" name="radioCustomized" onclick="javascript:setCustomized(true);" checked/>
											<fmt:message key="labelNo"/><input type="radio" name="radioCustomized" onclick="javascript:setCustomized(false);"/>
										</c:when>
										<c:otherwise>
											<fmt:message key="labelYes"/><input id="customizedExam" type="radio" name="radioCustomized" onclick="javascript:setCustomized(true);"/>
											<fmt:message key="labelNo"/><input type="radio" name="radioCustomized" onclick="javascript:setCustomized(false);" checked/>
										</c:otherwise>
									</c:choose>
								</td>
								<td>
								
								</td>
							</tr>
						</c:if>
						<tr>
							<c:choose>
								<c:when test="${empty exam}">
									<td><label><fmt:message key="labelActiveReview"/>:</label></td>
									<td colspan="2">
										<fmt:message key="partialCorrectionYes"/><input type="radio" name="radioReview" id="revYes" onclick="javascript:checkReview(true);"/>
										<fmt:message key="partialCorrectionNo"/><input type="radio" name="radioReview" onclick="javascript:checkReview(false);" checked/>
									</td>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${exam.activeReview eq true}">
											<td><label><fmt:message key="labelActiveReview"/>:</label></td>
											<td colspan="2">
												<fmt:message key="partialCorrectionYes"/><input type="radio" id="revYes"  name="radioReview" onclick="javascript:checkReview(true);" checked/>
												<fmt:message key="partialCorrectionNo"/><input type="radio" name="radioReview" onclick="javascript:checkReview(false);"/>
											</td>
										</c:when>
										<c:otherwise>
											<td><label><fmt:message key="labelActiveReview"/>:</label></td>
											<td colspan="2">
												<fmt:message key="partialCorrectionYes"/><input type="radio" id="revYes"  name="radioReview" onclick="javascript:checkReview(true);"/>
												<fmt:message key="partialCorrectionNo"/><input type="radio" name="radioReview" onclick="javascript:checkReview(false);" checked/>
											</td>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</tr>
						<c:choose>
					  		<c:when test="${empty exam or exam.activeReview eq false}">
					  			<tr id="columnStartDateRev" style="display:none">
					  		</c:when>
					  		<c:otherwise>
					  			<tr id="columnStartDateRev">
					  		</c:otherwise>
					  	</c:choose>
						
							<td><label><fmt:message key="startDateRev"/>:</label></td>
							<td colspan="2">
							  	<c:choose>
							  		<c:when test="${!empty exam}">
							  			<input id="startDateRev" name="textfechaini" size="8" readonly maxlength="8" type="text" value="<fmt_rt:formatDate value='${exam.startDateRevision}' type='date' dateStyle='short' />"/>
							  		</c:when>
							  		<c:otherwise>
							  			<input id="startDateRev" name="textfechaini" value="" size="8" readonly maxlength="8" type="text"/>
							  		</c:otherwise>
							  	</c:choose>
							  	&nbsp;<img src="${pageContext.request.contextPath}/imagenes/scw.gif" onclick="javascript:scwShow(getElementById('startDateRev'),this);examModified(true);"/>&nbsp;&nbsp;&nbsp;
							  <select id="startTimeRevH" onchange="javascript:examModified(true);">
							  	<c:forEach var="i" begin="0" end="23">
							  		<c:choose>
							  			<c:when test="${!empty exam and exam.startDateRevision.hours eq i}">
							  				<option selected="selected" value="<c:out value="${i}"/>"><c:out value="${i}"/></option>
							  			</c:when>
							  			<c:otherwise>
							  				<option value="<c:out value="${i}"/>"><c:out value="${i}"/></option>
							  			</c:otherwise>
							  		</c:choose>
							  	</c:forEach>
							   </select>:
							  <select id="startTimeRevM" onchange="javascript:examModified(true);">
							  	<c:forEach var="i" begin="0" end="59" step="5">
							  		<c:choose>
							  			<c:when test="${!empty exam and exam.startDateRevision.minutes eq i}">
							  				<option selected="selected" value="<c:out value="${i}"/>"><c:if test="${i lt 10}">0</c:if><c:out value="${i}"/></option>
							  			</c:when>
							  			<c:otherwise>
							  				<option value="<c:out value="${i}"/>"><c:if test="${i lt 10}">0</c:if><c:out value="${i}"/></option>
							  			</c:otherwise>
							  		</c:choose>
							  	</c:forEach>
							   </select>
							</td>
						</tr>
						<c:choose>
					  		<c:when test="${empty exam or exam.activeReview eq false}">
					  			<tr id="columnEndDateRev" style="display:none">
					  		</c:when>
					  		<c:otherwise>
					  			<tr id="columnEndDateRev">
					  		</c:otherwise>
					  	</c:choose>
							<td><label><fmt:message key="endDateRev"/>:</label></td>
							<td colspan="2">
							  	<c:choose>
							  		<c:when test="${!empty exam}">
							  			<input id="endDateRev" name="textfechaini" size="8" readonly maxlength="8" type="text" value="<fmt_rt:formatDate value='${exam.endDateRevision}' type='date' dateStyle='short' />"/>
							  		</c:when>
							  		<c:otherwise>
							  			<input id="endDateRev" name="textfechaini" value="" size="8" readonly maxlength="8" type="text"/>
							  		</c:otherwise>
							  	</c:choose>
							  	&nbsp;<img src="${pageContext.request.contextPath}/imagenes/scw.gif" onclick="javascript:scwShow(getElementById('endDateRev'),this);examModified(true);"/>&nbsp;&nbsp;&nbsp;
							  <select id="endTimeRevH" onchange="javascript:examModified(true);">
							  	<c:forEach var="i" begin="0" end="23">
							  		<c:choose>
							  			<c:when test="${!empty exam and exam.endDateRevision.hours eq i}">
							  				<option selected="selected" value="<c:out value="${i}"/>"><c:out value="${i}"/></option>
							  			</c:when>
							  			<c:otherwise>
							  				<option value="<c:out value="${i}"/>"><c:out value="${i}"/></option>
							  			</c:otherwise>
							  		</c:choose>
							  	</c:forEach>
							   </select>:
							  <select id="endTimeRevM" onchange="javascript:examModified(true);">
							  	<c:forEach var="i" begin="0" end="59" step="5">
							  		<c:choose>
							  			<c:when test="${!empty exam and exam.endDateRevision.minutes eq i}">
							  				<option selected="selected" value="<c:out value="${i}"/>"><c:if test="${i lt 10}">0</c:if><c:out value="${i}"/></option>
							  			</c:when>
							  			<c:otherwise>
							  				<option value="<c:out value="${i}"/>"><c:if test="${i lt 10}">0</c:if><c:out value="${i}"/></option>
							  			</c:otherwise>
							  		</c:choose>
							  	</c:forEach>
							   </select>
							</td>
						</tr>
						<tr>
							<td><label><fmt:message key="maxCalification"/>:</label></td>
							<td>
							  	<c:choose>
							  		<c:when test="${!empty exam}">
							  			<input name="textcalificacion" type="text" size="4" maxlength="4" id="max" onkeydown="javascript:examModified(true);" value="${exam.maxGrade}"/>
							  		</c:when>
							  		<c:otherwise>
							  			<input name="textcalificacion" type="text" size="4" maxlength="4" id="max" onkeydown="javascript:examModified(true);" value="10"/>
							  		</c:otherwise>
							  	</c:choose>
							</td>
							<td>
								<fmt:message key="labeltextExample"/> <c:out value="7.5"/>
							</td>
						</tr>
						<hr/>
						
						<c:choose>
							<c:when test="${user.role eq 'TUTORAV'}">
								<tr>
									<td><label><input id="ShowCustomizeAdvanced" name="ShowCustomizeAdvanced" type="button" onclick="javascript:showCustomizeAdv(true);" value="<fmt:message key="textShowCustomizeAdvanced"/>" /></label><label><input id="NoShowCustomizeAdvanced" name="NoShowCustomizeAdvanced" type="button" onclick="javascript:showCustomizeAdv(false);" style="display:none;" value="<fmt:message key="textNoShowCustomizeAdvanced"/>" /></label></td>
									<td><input value="<fmt:message key="showConfidenceLevel"/>" type="button" id="showConfidenceLevel" onclick="javascript:showConfidenceLevelDiv(true);"/><input value="<fmt:message key="hideConfidenceLevel"/>" type="button" id="hideConfidenceLevel" onclick="javascript:showConfidenceLevelDiv(false);" style="display:none" /></td>
									<td></td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td><label><input id="ShowCustomizeAdvanced" name="ShowCustomizeAdvanced" type="button" onclick="javascript:showCustomizeAdv(true);" value="<fmt:message key="textShowCustomizeAdvanced"/>" /></label><label><input id="NoShowCustomizeAdvanced" name="NoShowCustomizeAdvanced" type="button" onclick="javascript:showCustomizeAdv(false);" style="display:none;" value="<fmt:message key="textNoShowCustomizeAdvanced"/>" /></label></td>
									<td></td>
									<td></td>
								</tr>
							</c:otherwise>
						</c:choose>
						
							
					</tbody>
					</table>
					<div style="width: 45%; float: left;">
						<table class="listado">
							<tr>
								<td>
									<table width="100%" id="ShowNumAnswerCorrect" class="ShowNumAnswerCorrect" style="display:none;">
										<tr>
											<td><label><fmt:message key="textShowNumAnswerCorrect"/>:</label></td>
											<td>
												<c:choose>
											  		<c:when test="${!empty exam}">
											  			<c:choose>
											  				<c:when test="${exam.showNumCorrectAnswers eq true}">
											  					<input name="inputShowNumAnswerCorrectsName" type="checkbox" size="3" maxlength="3" id="inputShowNumAnswerCorrects" onClick="examModified(true);" onkeydown="javascript:examModified(true);" checked="checked" disabled/><br/>
											  				</c:when>
											  				<c:otherwise>
											  					<input name="inputShowNumAnswerCorrectsName" type="checkbox" size="3" maxlength="3" id="inputShowNumAnswerCorrects" onClick="examModified(true);" onkeydown="javascript:examModified(true);" disabled/><br/>
											  				</c:otherwise>
											  			</c:choose>
											  		</c:when>
											  		<c:otherwise>
											  			<input name="inputShowNumAnswerCorrectsName" type="checkbox" size="4" maxlength="4" id="inputShowNumAnswerCorrects" onClick="examModified(true);" onkeydown="javascript:examModified(true);" disabled checked="checked"/><br/>
											  		</c:otherwise>
											  	</c:choose>			
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table width="100%" id="CustomizeAdvanced" class="CustomizeAdvanced" style="display:none;">
										<tr>
											<td><label><fmt:message key="textPartialCorrection"/>:</label></td>
											<td width="40%">
											  	<c:choose>
											  		<c:when test="${!empty exam and exam.partialCorrection}">
											  			<input name="correccionParcial" type="radio" onClick="setDisabledCustomizedPartialCorrection(true); examModified(true);" id="correccionParcialYes" onkeydown="javascript:examModified(true);" value="Yes" checked><label><fmt:message key="partialCorrectionYes"/></label></input>
											  			<input name="correccionParcial" type="radio" onClick="setDisabledCustomizedPartialCorrection(false); examModified(true);" id="correccionParcialNo" onkeydown="javascript:examModified(true);" value="No"><label><fmt:message key="partialCorrectionNo"/></label></input>
											  		</c:when>
											  		<c:when test="${!empty exam and !exam.partialCorrection}">
											  			<input name="correccionParcial" type="radio" onClick="setDisabledCustomizedPartialCorrection(true); examModified(true);" id="correccionParcialYes" onkeydown="javascript:examModified(true);" value="Yes"><label><fmt:message key="partialCorrectionYes"/></label></input>
											  			<input name="correccionParcial" type="radio" onClick="setDisabledCustomizedPartialCorrection(false); examModified(true);" id="correccionParcialNo" onkeydown="javascript:examModified(true);" value="No" checked><label><fmt:message key="partialCorrectionNo"/></label></input>
											  		</c:when>
											  		<c:otherwise>
											  			<input name="correccionParcial" type="radio" onClick="setDisabledCustomizedPartialCorrection(true); examModified(true);" id="correccionParcialYes" onkeydown="javascript:examModified(true);" value="Yes" checked><label><fmt:message key="partialCorrectionYes"/></label></input>
											  			<input name="correccionParcial" type="radio" onClick="setDisabledCustomizedPartialCorrection(false); examModified(true);" id="correccionParcialNo" onkeydown="javascript:examModified(true);" value="No"><label><fmt:message key="partialCorrectionNo"/></label></input>
											  		</c:otherwise>
											  	</c:choose>
											</td>
										</tr>
									</table>
								</td>
							</tr>	
							<tr>
								<td>
								<c:choose>
								<c:when test="${!empty exam and exam.partialCorrection}">
									<table width="100%" id="CustomizePenalizePartialCorrectionYes" class="CustomizePenalizePartialCorrectionYes" style="display:none;">
										<tr>
									     	<td><label><fmt:message key="textPenalizationNotAllowed"/></label></td>
								<!-- 
	   								/* Partial correction and penalization currently disabled and hidden to the user */
										<tr>
											<td><label><fmt:message key="textPenalizeIncorrect"/>:</label></td>
											<td><input name="inputPenalizeIncorrectValueName" type="text" size="4" maxlength="4" id="inputPenalizeIncorrectValue" onkeydown="javascript:examModified(true);" value="${exam.penAnswerFailed}"/><br/></td>
										</tr>
										<tr>
											<td><label><fmt:message key="textMinQuestionGrade"/>:</label></td>
								  			<td><input name="inputMinQuestionGradeValue" type="text" size="4" maxlength="4" id="inputMinQuestionGradeValue" onkeydown="javascript:examModified(true);" value="${exam.minQuestionGrade}"/><br/></td>
										</tr>
								-->
										</tr>
									</table>							
									<table width="100%" id="CustomizePenalizePartialCorrectionNo" class="CustomizePenalizePartialCorrectionNo" style="display:none;">
										<tr>
											<td colspan="3"><label><fmt:message key="textPenalizePartialCorrectionNo"/></label></td>
										</tr>
										<tr>
											<td width="5%">&nbsp;</td>
											<td><label><fmt:message key="textPenalizeIncorrectQuestion"/>:</label></td>
											<td><input name="inputPenalizeIncorrectQuestionValue" type="text" size="3" maxlength="3" id="inputPenalizeIncorrectQuestionValue" onkeydown="javascript:examModified(true);" value="0"/></td>
											<td>%</td>
										</tr>
										<tr>
											<td width="10%">&nbsp;</td>	
											<td><label><fmt:message key="textPenalizeNoResponded"/>:</label>
											<td><input name="inputPenalizeNoRespondedValue" type="text" size="3" maxlength="3" id="inputPenalizeNoRespondedValue" onkeydown="javascript:examModified(true);" value="0"/></td>
											<td>%</td>
										</tr>
									</table>							
								</c:when>
								<c:when test="${(!empty exam and !exam.partialCorrection)}">
									<table width="100%" id="CustomizePenalizePartialCorrectionNo" class="CustomizePenalizePartialCorrectionNo" style="display:none;">
										<tr>
											<td colspan="3"><label><fmt:message key="textPenalizePartialCorrectionNo"/></label></td>
										</tr>
										<%-- Incoming values are normalized to 1. Shall be normalized to 100. --%>
										<tr>
											<td width="5%">&nbsp;</td>
											<td><label><fmt:message key="textPenalizeIncorrectQuestion"/>:</label></td>
											<td><input name="inputPenalizeIncorrectQuestionValue" type="text" size="3" maxlength="3" id="inputPenalizeIncorrectQuestionValue" onkeydown="javascript:examModified(true);" value="<fmt:formatNumber type="number" maxFractionDigits="0"><c:out value="${exam.penQuestionFailed*100}"/></fmt:formatNumber>"/></td>	
											<td>%</td>
										</tr>
										<tr>
											<td width="5%">&nbsp;</td>
											<td><label><fmt:message key="textPenalizeNoResponded"/>:</label></td>
											<td><input name="inputPenalizeNoRespondedValue" type="text" size="3" maxlength="3" id="inputPenalizeNoRespondedValue" onkeydown="javascript:examModified(true);" value="<fmt:formatNumber type="number" maxFractionDigits="0"><c:out value="${exam.penQuestionNotAnswered*100}"/></fmt:formatNumber>"/></td>
											<td>%</td>
										</tr>
									</table>
									<table width="100%" id="CustomizePenalizePartialCorrectionYes" class="CustomizePenalizePartialCorrectionYes" style="display:none;">
										<tr>
									     	<td><label><fmt:message key="textPenalizationNotAllowed"/></label></td>
								<!-- 
	   								/* Partial correction and penalization currently disabled and hidden to the user */
										<tr>
											<td><label><fmt:message key="textPenalizeIncorrect"/>:</label></td>
											<td><input name="inputPenalizeIncorrectValueName" type="text" size="4" maxlength="4" id="inputPenalizeIncorrectValue" onkeydown="javascript:examModified(true);" value="0"/><br/></td>
										</tr>
										<tr>
											<td><label><fmt:message key="textMinQuestionGrade"/>:</label></td>
									  		<td><input name="inputMinQuestionGradeValue" type="text" size="4" maxlength="4" id="inputMinQuestionGradeValue" onkeydown="javascript:examModified(true);" value="0"/><br/></td>
										</tr>
								-->
										</tr>
									</table>
								</c:when>
								<c:when test="${empty exam}">
									<tr>
									<table width="100%" id="CustomizePenalizePartialCorrectionYes" class="CustomizePenalizePartialCorrectionYes" style="display:none;">
										<tr>
									     	<td><label><fmt:message key="textPenalizationNotAllowed"/></label></td>
								<!-- 
	   								/* Partial correction and penalization currently disabled and hidden to the user */
										<tr>
											<td><label><fmt:message key="textPenalizeIncorrect"/>:</label></td>
											<td><input name="inputPenalizeIncorrectValueName" type="text" size="4" maxlength="4" id="inputPenalizeIncorrectValue" onkeydown="javascript:examModified(true);" value="0"/><br/></td>
										</tr>
										<tr>	
											<td><label><fmt:message key="textMinQuestionGrade"/>:</label></td>
									  		<td><input name="inputMinQuestionGradeValue" type="text" size="4" maxlength="4" id="inputMinQuestionGradeValue" onkeydown="javascript:examModified(true);" value="0"/><br/></td>
										</tr>
								-->
										</tr>
									</table>
									<table width="100%" id="CustomizePenalizePartialCorrectionNo" class="CustomizePenalizePartialCorrectionNo" style="display:none;">
										<tr>
											<td colspan="3"><label><fmt:message key="textPenalizePartialCorrectionNo"/></label></td>
										</tr>
										<tr>
											<td width="5%">&nbsp;</td>
											<td><label><fmt:message key="textPenalizeIncorrectQuestion"/>:</label></td>
											<td><input name="inputPenalizeIncorrectQuestionValue" type="text" size="3" maxlength="3" id="inputPenalizeIncorrectQuestionValue" onkeydown="javascript:examModified(true);" value="0"/></td>
											<td>%</td>	
										</tr>
										<tr>
											<td width="5%">&nbsp;</td>
											<td><label><fmt:message key="textPenalizeNoResponded"/>:</label></td>
											<td><input name="inputPenalizeNoRespondedValue" type="text" size="3" maxlength="3" id="inputPenalizeNoRespondedValue" onkeydown="javascript:examModified(true);" value="0"/></td>
											<td>%</td>
										</tr>
									</table>
									</tr>	
								</c:when>
								</c:choose>
								</td>
							</tr>
						</table>
					</div>
					<div id="ConfidenceLevelDiv" style="display:none; margin-right:5%; width: 45%; float: right; clear: right;">
						<div align="left">
							<table>
								<tr>
									<td>
										<fmt:message key="activeConfidenceLevel"/>
									</td>
								</tr>
								<tr>
									<c:choose>
										<c:when test="${!empty exam && exam.confidenceLevel eq true}">
											<td>
												<fmt:message key="labelYes"/>:<input type="radio" onclick="javascript:activeConfidenceLevel(true)" name="confidence" checked/>
												<fmt:message key="labelNo"/>:<input type="radio" onclick="javascript:activeConfidenceLevel(false)" name="confidence" />
											</td>
										</c:when>
										<c:otherwise>
											<fmt:message key="labelYes"/>:<input type="radio" onclick="javascript:activeConfidenceLevel(true)" name="confidence"/>
											<fmt:message key="labelNo"/>:<input type="radio" onclick="javascript:activeConfidenceLevel(false)" name="confidence" checked/>
										</c:otherwise>
									</c:choose>
								</tr>
							</table>
						</div>
						<c:choose>
							<c:when test="${!empty exam && exam.confidenceLevel eq true}">
								<div id="divConfidenceLevel" align="right">
							</c:when>
							<c:otherwise>
								<div id="divConfidenceLevel" align="right" style="display:none">
							</c:otherwise>
						</c:choose>
							<c:choose>
								<c:when test="${!empty exam}">
								<table>
									<tbody>
										<tr>
											<td><label><fmt:message key="labelPenalizationConfidence"/>: </label></td>
											<td><input size="4" maxlength="4" type="text" id="PenalizeConfidenceLevel" onkeydown="javascript:examModified(true);" value="<fmt:formatNumber type="number" maxFractionDigits="0"><c:out value="${exam.penConfidenceLevel*100}"/></fmt:formatNumber>"/> %</td>
										</tr>
										<tr>
											<td><label><fmt:message key="labelRewardConfidence"/>: </label></td>
											<td><input size="4" maxlength="4" type="text" id="RewardConfidenceLevel" onkeydown="javascript:examModified(true);" value="<fmt:formatNumber type="number" maxFractionDigits="0"><c:out value="${exam.rewardConfidenceLevel*100}"/></fmt:formatNumber>"/> %</td>
										</tr>
									</tbody>
								</table>
								</c:when>
								<c:otherwise>
									<table>
										<tbody>
											<tr>
												<td><label><fmt:message key="labelPenalizationConfidence"/>: </label></td>
												<td><input size="4" maxlength="4" type="text" id="PenalizeConfidenceLevel" onkeydown="javascript:examModified(true);" value="0"/> %</td>
											</tr>
											<tr>
												<td><label><fmt:message key="labelRewardConfidence"/>: </label></td>
												<td><input size="4" maxlength="4" type="text" id="RewardConfidenceLevel" onkeydown="javascript:examModified(true);" value="0"/> %</td>
											</tr>
										</tbody>
									</table>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					
					
					<table class="tablaformulario">								
						<tr>
						  <td>&nbsp;</td>
						  <td><label><input type="submit" name="submitsave" value="<fmt:message key="buttonSave"/>"></label></td>
					      <td><label><input type="button" name="newconfigexam" onclick="if(checkSavedExam())javascript:window.location='${pageContext.request.contextPath}/tutor/managegroup.itest?method=newExam';" value="<fmt:message key="buttonNewConfigExam"/>"></label></td>
						  <td>&nbsp;</td>
						</tr>
						<tr>
						  <td>&nbsp;</td>
					       <c:choose>
								<c:when test="${!empty exam}">
	     				  			<td><label><input id="validateButton" name="send" type="button" onclick="javascript:validateExam();" value="<fmt:message key="buttonValidateExam"/>" /></label></td>
	     				  		</c:when>
	     				  		<c:otherwise>
		     				  		<td><label><input id="validateButton" name="send" type="button" onclick="javascript:validateExam();" value="<fmt:message key="buttonValidateExam"/>" disabled /></label></td>
	     				  		</c:otherwise>
	     				   </c:choose>
					       <c:choose>
								<c:when test="${!empty exam}">
	     				  			<td><label><input id="previewButton" name="send" type="button" onclick="javascript:previewExam();" value="<fmt:message key="buttonPreviewExam"/>" /></label></td>
	     				  		</c:when>
	     				  		<c:otherwise>
		     				  		<td><label><input id="previewButton" name="send" type="button" onclick="javascript:previewExam();" value="<fmt:message key="buttonPreviewExam"/>" disabled /></label></td>
	     				  		</c:otherwise>
	     				   </c:choose>	 
						  <td>&nbsp;</td>    				  
						</tr>
						<tr>
						  <td>&nbsp;</td>
					       <c:choose>
								<c:when test="${!empty exam}">
	     				  			<td><label><input id="copyExamButton" name="send" type="button" onclick="javascript:copyExam('${pageContext.request.contextPath}');" value="<fmt:message key="buttonCopyExam"/>" /></label></td>
	     				  		</c:when>
	     				  		<c:otherwise>
		     				  		<td><label><input id="copyExamButton" name="send" type="button" onclick="javascript:copyExam('${pageContext.request.contextPath}');" value="<fmt:message key="buttonCopyExam"/>" disabled /></label></td>
	     				  		</c:otherwise>
	     				   </c:choose>
						  <td><label><input id="examRecorrectButton" type="button" style="display:none" onclick="javascript:recorrectExam('${exam.id}','${group.id}')" value="<fmt:message key="buttonRe-Correction"/>"/></label></td>
						  <td>&nbsp;</td>    				  
						</tr>
					</table>
					</fieldset>
			  </form>	
				</div>				
				<!-- maybe editing -->	
				<c:choose>
					<c:when test="${!empty exam}">
						<div class="divmitadder" id="divincludetheme">
					</c:when>
					<c:otherwise>
						<div class="divmitadder" id="divincludetheme">
					</c:otherwise>
				</c:choose>
				  <form name="formconfigpreg" method="post" action="javascript:includeTheme();">
			    <fieldset id="fieldsetmitadder"><legend><fmt:message key="includeNewTheme"/></legend>
						<table class="listado">
							<tbody><tr><td><label><fmt:message key="theme"/>:</label></td>
								<td><select name="subject" id="subject" onchange="javascript:showAsks()">
								    <option value="">--------------</option>
									<c:forEach items="${themes}" var="theme">
									    <option value="${theme.id}"><c:out value="${theme.sort}. ${theme.subject}"/></option>
								  	</c:forEach>
								  </select></td></tr>
							<tr>
								<td><fmt:message key="labelQuestionType"/>:</td>
								<td>
									<select id="questionType" onchange="javascript:showAsks()">
										<option value="0"><fmt:message key="labelQuestionTest"/></option>
										<option value="1"><fmt:message key="labelQuestionFill"/></option>
									</select>
								</td>
							</tr>
							<tr><td><label><fmt:message key="questionsNumber"/>:</label></td>
								<td><input name="questionsNumber" id="questionsNumber" size="2" maxlength="2" type="text"></td></tr>
							<tr><td><label><fmt:message key="difficulty"/>:</label></td>
								<td><select name="difficulty" id="difficulty">
								  <!-- Should have one correct value selected -->
								  <option selected value="0"><fmt:message key="diffLow"/></option>
								  <option value="1"><fmt:message key="diffMedium"/></option>
								  <option value="2"><fmt:message key="diffHigh"/></option>
								</select></td></tr>
							<tr id="answerPerQuestionRow"><td><label><fmt:message key="answersPerQuestion"/></label></td>
								<td><input name="answersPerQuestion" id="answersPerQuestion" size="2" maxlength="2" type="text"></td></tr>						
				    </tbody></table>
					  <p><input name="Submit" id="submit" value="<fmt:message key="buttonInclude"/>" type="submit"/></p>
  	  				</form>
				  </fieldset>
				  <fieldset id="fieldsetTheme" style="display:none">
				  		<legend id="legendTheme">Detalles del tema</legend>
				  		<table class="tabladatos" id="detailsTable">
				  		<th><center><fmt:message key="headerQlistDiff"/></center></th>
				  		<th><center><fmt:message key="numberActiveQuestions"/></center></th>
				  		<th><center><fmt:message key="numberMinimunAnswer"/></center></th>
				  		<th><center><fmt:message key="numberTotalQuestions"/></center></th>
				  		<tbody id="detailsTableBody">
				  		
				  		</tbody>
				    	</table>
				  </fieldset>

				</div>			
			</div>
			<div class="divcentro"></div>
			<c:choose>
				<c:when test="${!empty exam and exam.customized eq true and user.role eq 'TUTORAV'}">
					<div class="divmitadizq" id="divUserCustom">
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${empty exam}">
							<div class="divmitadizq" id="divUserCustom" style="background-color:#C0C0C0; display:none;">
						</c:when>
						<c:otherwise>
							<div class="divmitadizq" id="divUserCustom" style="display:none;">
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
				<fieldset>
					<legend><fmt:message key="customUserList"/></legend>
					<c:choose>
						<c:when test="${empty exam}">
							<input id="buttonCustomUser" type="button" value="<fmt:message key="buttonAddCustomUser"/>" onclick="javascript:addUser2CustomUserList();" disabled/>
							<select id="customUserSelect" disabled>
						</c:when>
						<c:otherwise>
							<input id="buttonCustomUser" type="button" value="<fmt:message key="buttonAddCustomUser"/>" onclick="javascript:addUser2CustomUserList();"/>
							<select id="customUserSelect">
						</c:otherwise>
					</c:choose>
					
						<c:forEach items="${usersNotInCustomExam}" var="user">
							<option value="${user.id}">${user.surname}, ${user.name}</option>
						</c:forEach>
					</select>
					<br/><br/>
					<table class="tabladatos">
						<thead>
							<tr>
								<th><fmt:message key="personalID"/></th>
								<th><fmt:message key="surname"/></th>
								<th><fmt:message key="name"/></th>
								<th><fmt:message key="user"/></th>
								<th></th>
							</tr>
						</thead>
						<tbody id="customUserTableBody">
							<c:forEach items="${usersInCustomExam}" var="user">
								<tr id="customUserColumn${user.id}">
									<td>${user.persId}</td>
									<td>${user.surname}</td>
									<td>${user.name}</td>
									<td>${user.userName}</td>
									<td>
										<c:choose>
											<c:when test="${user.inExam}">
												<img src="${pageContext.request.contextPath}/imagenes/forb_dot.gif" border="none">
											</c:when>
											<c:otherwise>
												<a href="javascript:removeUserFromCustomExam('${user.id}')"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif" border="none"/></a>
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</fieldset>
			
			</div>
			
			<c:choose>
				<c:when test="${!empty exam and exam.customized eq true and user.role eq 'TUTORAV'}">
					<div class="divmitadder" id="divthemeslist">
				</c:when>
				<c:when test="${!empty exam and exam.customized eq false}">
					<div class="divmitadder" style="width:100%;" id="divthemeslist">
				</c:when>
				<c:when test="${empty exam}">
					<div class="divmitadder" style="background-color:#C0C0C0; width:100%;" id="divthemeslist">
					<script type='text/javascript'> disableDivMitadDer(); </script>
				</c:when>
				<c:when test="${user.role eq 'TUTOR'}">
					<div class="divmitadder" style="width:100%;" id="divthemeslist">
				</c:when>
				<c:otherwise>
					<div class="divmitadder" id="divthemeslist">
s				</c:otherwise>
			</c:choose>
				<fieldset><legend><fmt:message key="includedThemes"/></legend>
				<br/><br/><br/>
				<table class="tabladatos" id="themestable">															
				<tbody id="themestabletbody"><tr>
				  <th></th>
				  <th><fmt:message key="theme"/></th>
				  <th><fmt:message key="difficulty"/></th>
				  <th><fmt:message key="questionsNumber"/></th>
				  <th><fmt:message key="answersNumber"/></th>
				  <th><fmt:message key="labelQuestionType"/></th>
				  <th></th>
				  <c:choose>
					  <c:when test="${!empty cesubjects}">
						  <c:forEach items="${cesubjects}" var="sub" varStatus="status">
							<tr>
								<td id="tema${sub.id}"></td>
								<td>${sub.subject.subject}</td>
								<td>
								</option>
									<c:choose>
										<c:when test="${sub.maxDifficulty eq 0}"><fmt:message key="diffLow"/></c:when>
										<c:when test="${sub.maxDifficulty eq 1}"><fmt:message key="diffMedium"/></c:when>
										<c:when test="${sub.maxDifficulty eq 2}"><fmt:message key="diffHigh"/></c:when>
										<c:otherwise>Â¡Â¡ERROR!!</c:otherwise>
									</c:choose>
								</td>
								<td>${sub.questionsNumber}</td>
								<td>${sub.answersxQuestionNumber}</td>
								<c:choose>
									<c:when test="${sub.questionType eq 0}">
										<td><fmt:message key="labelQuestionTest"/></td>
									</c:when>
									<c:when test="${sub.questionType eq 1}">
										<td><fmt:message key="labelQuestionFill"/></td>
									</c:when>
								</c:choose>
								<td><a href="javascript:deleteTheme('${sub.id}');"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="delete"/>" border="none"></a></td>
						    </tr>
						    <script>document.getElementById('tema'+${sub.id}).innerHTML = document.getElementById('themestable').rows.length-1+'.';</script>
						  </c:forEach>
					  </c:when>
				  </c:choose>
				</tr>
			  </tbody></table>
			  </fieldset>
			</div>		    
		</div>
		
	</body>
</html>