<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>
<%@ page import="com.cesfelipesegundo.itis.model.Group" %>
<%@ page import="com.cesfelipesegundo.itis.model.TemplateExamQuestion" %>

<% 
	Group group = (Group)request.getAttribute("group");
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addStep(group.getCourse().getName()+" ("+group.getName()+")",request.getContextPath()+"/tutor/managegroup.itest?method=indexGroup");
	TemplateExamQuestion q = (TemplateExamQuestion)request.getAttribute("question");
	if (q != null) {
		// Edition of question
		breadCrumb.addBundleStep("editQuestion","");
	} else {
		// Addition of questions
		breadCrumb.addBundleStep("newQuestion","");	
	}
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="tutor"/>
	<jsp:param name="menuNewQuestion" value="tutor"/>
</jsp:include>

	<!-- Ajax for questions -->
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/QuestionMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>
	
	
	<script type='text/javascript'>
		edit=false;
		var type;
		function showPdf(){
			window.open('${pageContext.request.contextPath}/tutor/managequestion.itest?method=generateQuestionPDF&idQuestion='+document.getElementById("questionid").value, '_blank', 'width=600,height=400,scrollbars=YES,resizable=YES'); 
			return false;
		}
		answerSaved = false;
		function alert2Exit(newQuestion){
			var questionType = document.getElementById('selectQuestionType').value;
			
			if(askCreated){
				globalVar = newQuestion;
				if(document.getElementsByName('labelOkSol').length>=1 || (answerSaved && questionType==1)){
					/*Si la pregunta tiene al menos una respuesta correcta*/
					if(globalVar == 'newQuestion'){
						submitForm(document.getElementById('formAddNewQuestion'));
					}else if(globalVar == 'copyQuestion'){
						if (confirm("<fmt:message key="msgConfirmCopyQuestion"/> "+document.getElementById("questionid").value +" <fmt:message key="msgConfirmChar"/>")){
							 window.location='${pageContext.request.contextPath}/tutor/managequestion.itest?method=questionCopy';
						}else{
							return false;
						}
					}
				}else{
					/*La pregunta no tiene ninguna respuesta correcta*/
					
					if(globalVar == 'copyQuestion'){
						if (confirm("<fmt:message key="noCorrectAnswer4Copy"/>:\n<fmt:message key="msgConfirmCopyQuestion"/> "+document.getElementById("questionid").value +" <fmt:message key="msgConfirmChar"/>")){
							 window.location='${pageContext.request.contextPath}/tutor/managequestion.itest?method=questionCopy';
						}else{
							return false;
						}
					}else if(globalVar == 'newQuestion'){
						if(confirm("<fmt:message key="noCorrectAnswer"/>"))
							{
							submitForm(document.getElementById('formAddNewQuestion'));
							}else{
								return false;
							}
					}else{
						
						if(confirm("<fmt:message key="noCorrectAnswer"/>"))
						{
							return true;
						}else{
							return false;
						}
					}
				}
			}else{
				/*
					LA PREGUNTA NO SE A GUARDADO
				*/
				if(confirm("<fmt:message key="confirmExitWithoutSave"/>")){
					return true;
				}else{
					return false;
				}
			}
		}
	
		function showAlert2Exit(answers){
			var countCorrectAnsw = getCorrectAnswers(answers);
			alert(countCorrectAnsw);
			if (qAnswerListLength==countCorrectAnsw){
				return;
			}
			
			
		}
		
		function disableAnswerMM(){
			antiguoColorRespuestaMM = document.getElementById("divrespuestader").getElementsByTagName("fieldset")[0].style.backgroundColor;
			document.getElementById("divrespuestader").getElementsByTagName("fieldset")[0].style.backgroundColor="#C0C0C0";
			document.getElementById("mmediaanswer").disabled=true;
			document.getElementById("buttonAddFileA").disabled=true
			document.getElementById("divmmediaAtable").style.display="none";
			
		}

		function enableAnswerMM(){
			if(document.getElementById("divrespuestader")!=null)
				document.getElementById("divrespuestader").getElementsByTagName("fieldset")[0].style.backgroundColor='';
			if(document.getElementById("divrespuestader")!=null)
				document.getElementById("mmediaanswer").disabled=false;
			document.getElementById("buttonAddFileA").disabled=false;
			document.getElementById("divmmediaAtable").style.display="block";
		}

	
		function disableAnswerDiv(){
			document.getElementById("textrespuesta").disabled = true;
			askCreated = false;
			document.getElementById("combosolution").disabled = true;
			var inputs = document.getElementById("divrespuestaizq").getElementsByTagName("input");
			for(var i=0;i<inputs.length;i++){
				inputs[i].disabled=true;
			}
			antiguoColorRespuesta = document.getElementById("divrespuestaizq").getElementsByTagName("fieldset")[0].style.backgroundColor;
			document.getElementById("divrespuestaizq").getElementsByTagName("fieldset")[0].style.backgroundColor="#C0C0C0";
			document.getElementById('buttonAddFileQ').disabled=true;
			document.getElementById('mmediaquestion').disabled=true; 
			document.getElementById('minusYT').disabled=true;
			antiguoColorPreguntaMM = document.getElementById("divenunciadoder").getElementsByTagName("fieldset")[0].style.backgroundColor;
			document.getElementById("divenunciadoder").getElementsByTagName("fieldset")[0].style.backgroundColor="#C0C0C0";
			//CommentMM
			document.getElementById("buttonAddFileC").disabled = true;
			document.getElementById('mmediacomment').disabled=true; 
			document.getElementById("commentader").getElementsByTagName("fieldset")[0].style.backgroundColor="#C0C0C0";
		}
		/* For Ajax */
		function saveQuestion() {
		   // Maybe there are no themes in the subject
		   if (document.getElementById("idtheme") == null) {
		      alert("<fmt:message key="cannotSaveQ"/>, <fmt:message key="noAvailableThemes"/>");
		      return;
		   }
		   
		   var questionType = document.getElementById('selectQuestionType').value
		   type = questionType;
		   var id=document.getElementById("questionid").value;
		   var title = document.getElementById("questiontitle").value;
		   var idth = document.getElementById("idtheme").value;
   		   var difficulty = document.getElementById("combodificultad").value;
   		   var visibility = document.getElementById("combovisibilidad").value;
		   var enunc = document.getElementById("textenunciado").value;
		   enunc = enunc.replace(/^\s*|\s*$/g,"");
		   if(enunc.length == 0){
			   alert("<fmt:message key="cannotSaveQ"/> <fmt:message key="msgEmptyQuestionText"/>");
			   return;
			}
		   if (idth == "") {
		      alert("<fmt:message key="cannotSaveQ"/> <fmt:message key="msgNoSelectedTheme"/>");
		      return;
		   }
		   if (title.length > 60) {
		      alert("<fmt:message key="cannotSaveQ"/> <fmt:message key="msgTitleTooLong"/>");
		      return;
		   }
		   if (enunc.length > 65000) {
		      alert("<fmt:message key="cannotSaveQ"/> <fmt:message key="msgQuestionTooLong"/>");
		      return;
		   }
		   var comm = document.getElementById("textcomentario").value;
		   if (comm.length > 65000) {
		      alert("<fmt:message key="cannotSaveQ"/> <fmt:message key="msgCommentTooLong"/>");
		      return;
		   }
		   
  		   // Protecting the page
		   iTestLockPage('');
		   
		   // Save the question using Ajax, updateQuestionId is the callback
	       QuestionMgmt.saveQuestion(${group.id},idth,title,difficulty,visibility,enunc,comm,questionType,{callback:updateQuestionId,
		       timeout:callBackTimeOut,
		       errorHandler:function(message){iTestUnlockPage('error');}});
	       
		} // saveQuestion


		function confirmar(context){
			 if (confirm("<fmt:message key="msgConfirmCopyQuestion"/> "+document.getElementById("questionid").value +" <fmt:message key="msgConfirmChar"/>")){
				 window.location=context+'/tutor/managequestion.itest?method=questionCopy';
			    }
			} 

		
		// Updates id for an added question
		function updateQuestionId(id) {
			// We show the hidden divs...
		       // Question multimedia
		       
			   document.getElementById("divenunciadoder").style.display="block";
			   // Answer text
			   document.getElementById("divrespuestaizq").style.display="block";
			   document.getElementById("newquestion").style.display="block";
			   document.getElementById("newQuestionPDF").style.display="block";
			   // And the "paste to answer" button
			   document.getElementById("pasteA").style.display="block";
			   // And the "preliminar view" button
			   document.getElementById("preliminar").style.display="block";
			  // And the "Question Copy" button
			   document.getElementById("newQuestionCopy").style.display="block";
			   document.getElementById("buttonAnswerDiv").style.display="";
			  // Unlock the "textArea" answer
			  document.getElementById("textrespuesta").disabled = false;
			  document.getElementById("textRespuestaFill").disabled = false;
			  document.getElementById("buttonSaveFillAnswer").disabled = false;
			  // Change the back ground color from the fieldset
			  document.getElementById("combosolution").disabled = false;
			  if(type == 0){
			  	document.getElementById("pasteA").disabled = false;
			  }
			  
			  var inputs = document.getElementById("divrespuestaizq").getElementsByTagName("input");
				for(var i=0;i<inputs.length;i++){
					inputs[i].disabled=false;
				}
			  document.getElementById("divrespuestaizq").getElementsByTagName("fieldset")[0].style.backgroundColor = '';
			  
			  askCreated = true;
			  // Unlock button add from MMFileAsk
			  document.getElementById("divenunciadoder").getElementsByTagName("fieldset")[0].style.backgroundColor = '';
			  document.getElementById('buttonAddFileQ').disabled=false; 
			  document.getElementById('mmediaquestion').disabled=false; 
			  document.getElementById('minusYT').disabled=false;

				//CommentMM
				document.getElementById("buttonAddFileC").disabled = false;
				document.getElementById('mmediacomment').disabled=false; 
				document.getElementById("commentader").getElementsByTagName("fieldset")[0].style.backgroundColor='';
			  
			  
			   // Question not modified
			   questionModified(false);
			   
			if(id!=-1)
				document.getElementById("questionid").value=id;
			else
				alert('<fmt:message key=""/>');
			iTestUnlockPage();
		}

		// Updates a flag to let the user know that the question was modified or not
		function questionModified(value) {
		    var newlegend;
		    
			// New legend:
 		    newlegend=document.createElement('label');
			newlegend.setAttribute("id","labelQ_modified");
	   	    newlegend.innerHTML = "<fmt:message key="labelModified"/>"
			if (value) {
			   // The question was modified
			   if (!document.getElementById("labelQ_modified"))
			   		document.getElementById("labelQ").appendChild(newlegend);
			} else {
			   // The question was saved
			   if (document.getElementById("labelQ_modified"))
			   		document.getElementById("labelQ").removeChild(document.getElementById("labelQ_modified"));
			}
			
  		   // Un-protect the page
		   iTestUnlockPage();
						
		} // questionModified
		
		
		// Is a new answer?: needed to check if the saved answer is new or not...
		var isnewanswer = true;
		var idTableRow;
		function editAnswerTable(idTable,id){
			idTableRow = idTable;
			var modified;
		    modified=document.createElement('label');
	   	 	modified.setAttribute("id","answerLegendModified");
	   	 	modified.innerHTML = ": Respuesta "+id;
	   	 	if (document.getElementById("answerLegendModified")){
		   		document.getElementById("labelA").removeChild(document.getElementById("answerLegendModified"));
			}
	   	 	if (!document.getElementById("answerLegendModified"))
	   			document.getElementById("labelA").appendChild(modified);
		}

		
		/* Callback function, Ajax for the answer being inserted/deleted (receives the list of answers) */
		function updateListAnswers(answers) {
		  /*
		     This is a callback function that updates the information about the answers already saved (answerstable)
		     with the answer recently just saved.
		  */
  		  var rowelement, datatable, cellelementCount, cellelementText, cellelementSol, cellelementFiles, cellelementDel;		  			  
   		  // Answers counter: needed to reference the answer to receive images.
		  var countAnsw = 1;

	       // First we show the hidden divs...
		   // Answer text
		   document.getElementById("divanswers").style.display="block";

		   tbodyelement=document.createElement('tbody');
		   tbodyelement.setAttribute("id","answerstabletbody");
		   
		   // Fills the table (DOM scripting): headers
		   rowelementH = document.createElement('tr');
           headelement = document.createElement('th');
           headelement.innerHTML = "&nbsp;";
           rowelementH.appendChild(headelement);
           headelement = document.createElement('th');
           headelement.innerHTML = "<fmt:message key="labelAnswerList"/>";
           rowelementH.appendChild(headelement);
           headelement = document.createElement('th');
           headelement.innerHTML = "&nbsp;";
           rowelementH.appendChild(headelement);
           headelement = document.createElement('th');
           headelement.innerHTML = "&nbsp;";
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
		   var id;
		   while (countAnsw <= answers.length) {
		        answer = answers[position];
		        id = countAnsw;
				rowelement = document.createElement('tr');
				rowelement.setAttribute("id","tableAnswersRow"+id);
				// All the cells:
				cellelementCount = document.createElement('td');
				cellelementCount.innerHTML = countAnsw+".&nbsp;";
				
				// Counter:
				countAnsw++;
				rowelement.appendChild(cellelementCount);
				
				// Text of the answer
				cellelementText = document.createElement('td');
				if (typeof answer.text != 'undefined' && answer.text != null && answer.text.length > 100) {
					parse2HTML(answer.text.substring(0,99)+"...",cellelementText);
				} else if (typeof answer.text != 'undefinied' && answer.text != null){
				   parse2HTML(answer.text,cellelementText);
				}
				
				rowelement.appendChild(cellelementText);
	
				// Is solution?
				cellelementSol = document.createElement('td');
				if (answer.solution == 1)
					cellelementSol.innerHTML = "<img name='labelOkSol' src=\"${pageContext.request.contextPath}/imagenes/simok.gif\" alt=\"<fmt:message key="labelOkSol"/>\" border=\"none\">";
				else
					cellelementSol.innerHTML = "&nbsp;";
				rowelement.appendChild(cellelementSol);
	
				// Files (mmedia)
				cellelementFiles = document.createElement('td');
				// In there are associated files, the "clip" is showed
				if (answer.mmedia!=null && answer.mmedia.length > 0)
				   cellelementFiles.innerHTML = "<img src=\"${pageContext.request.contextPath}/imagenes/clip.gif\" alt=\"<fmt:message key="labelFiles"/>\" border=\"none\">";
				else
				   cellelementFiles.innerHTML = "&nbsp;";
				rowelement.appendChild(cellelementFiles);

				// Control element: edit answer
				cellelementDel = document.createElement('td');
				cellelementDel.innerHTML = "<a href=\"javascript:editAnswer('"+answer.id+"');javascript:editAnswerTable('tableAnswersRow"+id+"','"+id+"');\"><img src=\"${pageContext.request.contextPath}/imagenes/editar.gif\" alt=\"<fmt:message key="labelEditAnswer"/>\" border=\"none\"></a>";
				rowelement.appendChild(cellelementDel);
				
				// Control element: delete answer
				cellelementDel = document.createElement('td');
				// Can be deleted only if was not used in exam
				if (!answer.usedInExam) {
					cellelementDel.innerHTML = "<a href=\"javascript:deleteAnswer('"+answer.id+"','"+document.getElementById('questionId').value+"');\"><img src=\"${pageContext.request.contextPath}/imagenes/borrar.gif\" alt=\"<fmt:message key="labelDeleteAnswer"/>\" border=\"none\"></a>";
				} else {
					cellelementDel.innerHTML = "<img src=\"${pageContext.request.contextPath}/imagenes/forb_dot.gif\" alt=\"<fmt:message key="cannotDeleteAnswer"/>\" border=\"none\">";
				}
				rowelement.appendChild(cellelementDel);
				
				// Add row
				tbodyelement.appendChild(rowelement);
				
				position++;
			} // while
			
			// No answers: present message
		    if (answers.length == 0) {
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=5;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML = "<fmt:message key="noAvailableAnswers"/>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		    }
		
		    // Gets the datatable
		    datatable=document.getElementById("answerstable");
			// Replaces tbody			
			datatable.replaceChild(tbodyelement,document.getElementById("answerstabletbody"));
			
  		   // Do not un-protect the page: it is done in "updateAnswerMmediaList"

			// If task is to modify correct answers, show ButtonReCorrect
			//QuestionMgmt.getCurrentQuestionAnswers(showButtonReCorrect);
			if(edit){
				if(idTableRow){
					document.getElementById(idTableRow).style.backgroundColor = "#00ffff";
				}
	    		edit=false;
			}else{
				newAnswer();
			}
		} // updateListAnswers

		
		
		function showButtonRecorrectExam(answer){
			if(answer==true){
				document.getElementById("divSubmitRe-Correction").style.display="";
				document.getElementById("divSubmitRe-CorrectionFill").style.display="";
			}
		}
		
		
		
		/* Callback function, Ajax for the answer being selected for edition (receives the answer) */
		function updateAnswerForm(answer) {
		   // Updates the answer form
		   document.getElementById("textrespuesta").value = answer.text;
   		   document.getElementById("combosolution").value = answer.solution;
	   	    
   		   // Answer multimedia list
		   document.getElementById("divrespuestader").style.display="block";
		   updateAnswerMmediaList(answer.mmedia);
		   

		} // updateAnswerForm		
		
		
		// Updates a flag to let the user know that the answer was modified or not
		function answerModified(value) {
		    var newlegend;
		    
			// New legend:
 		    newlegend=document.createElement('label');
			newlegend.setAttribute("id","labelA_modified");
	   	    newlegend.innerHTML = "<fmt:message key="labelModified"/>"
			if (value) {
			   // The question was modified
			   if (!document.getElementById("labelA_modified"))
			   		document.getElementById("labelA").appendChild(newlegend);
			} else {
			   // The question was saved
			   if (document.getElementById("labelA_modified")){
			   		document.getElementById("labelA").removeChild(document.getElementById("labelA_modified"));
				}
			}
						
		} // answerModified
		
		
		/* Resets the current answer in the controller and erases the fields of the new answer */
		function newAnswer(idQuestion) {
		   document.getElementById("textrespuesta").value = "";
   		   document.getElementById("combosolution").value = 0;
   		   var tabla = document.getElementById("mmediaAtbody").innerHTML="";
   		   document.getElementById("mmediaanswer").value = "";
	   		if (document.getElementById("answerLegendModified")){
		   		document.getElementById("labelA").removeChild(document.getElementById("answerLegendModified"));
			}
		   

 		   // Protecting the page
		   iTestLockPage();

		   // Resets the answer using Ajax (no callback)
	       QuestionMgmt.newAnswer();
	       
	       // Flag of new answer
	       isnewanswer = true;
		   
		   // Answer not modified
		   answerModified(false);

  		   // Un-protecting the page
		   iTestUnlockPage();
		   disableAnswerMM();
		// Question multimedia hidden (no currentAnswer)
		   
		} // newAnswer

		//Calculate the new number of correct answers
		function getCorrectAnswers(answers){
			var countCorrectAnsw = 0;
			var indice=0;
			
			while (indice<answers.length){
				answer = answers[indice];
				if (answer.solution == 1){
					countCorrectAnsw++;
				}
				indice++;
			}
			return countCorrectAnsw;
		}
		
		// Number of elements of the answers list for the question
		var qAnswerListLength ='${(question.numCorrectAnswers)}';

		//Update counter correct Answers
		function updateCorrectAnswers(answers){
			qAnswerListLength=getCorrectAnswers(answers);
		}

		

		function showCountRevisions(revisions){
			alert("Han sido revisados "+revisions.length+ " examenes.");
			document.getElementById("divSubmitRe-Correction").style.display="none";
			document.getElementById("divSubmitRe-CorrectionFill").style.display="none";
			//Update the count correct answers
			QuestionMgmt.getCurrentQuestionAnswers(updateCorrectAnswers);
		}
		
		function saveAnswer(idQuestion) {
			// Si es una nueva pregunta, no se porque idQuestion es null 
			idQuestion = document.getElementById("questionid").value;
		   var texto = document.getElementById("textrespuesta").value;
		   if (texto.length > 65000) {
		      alert("<fmt:message key="cannotSaveA"/> <fmt:message key="msgAnswerTooLong"/>");
		      return;
		   }
   		   var solution = document.getElementById("combosolution").value;
  		   // Protecting the page
		   iTestLockPage();
		   // Save the answer using Ajax (updateListAnswers is the callback function and answers is the return object)
	       QuestionMgmt.saveAnswer(texto,solution,isnewanswer,updateListAnswers);
	       answerSaved = true;
	       if((document.getElementById('combosolution').value==1)||(!isnewanswer)){
	       		QuestionMgmt.isInExam(idQuestion,showButtonRecorrectExam);
	       }
	       
	       // Now is not a new answer
		   /*isnewanswer = false;
			document.getElementById("divrespuestader").getElementsByTagName("fieldset")[0].style.backgroundColor='';
			document.getElementById("mmediaanswer").disabled=false;
			document.getElementById("buttonAddFileA").disabled=false;*/
		   // Answer not modified
		   answerModified(false);
	       // Question multimedia: has to be obtained from Ajax, callback updateAnswerMmediaList
		   //QuestionMgmt.getCurrentAnswerMmedia(updateAnswerMmediaList);
		   enableAnswerMM();
		   if (document.getElementById("answerLegendModified")){
		   		document.getElementById("labelA").removeChild(document.getElementById("answerLegendModified"));
			}
		   
		} // saveAnswer


		//When the Tutor modify correct answers and press button Re-correction
		function revisionExams(){
			// Protecting the page
			iTestLockPage();
			QuestionMgmt.reviewExamsByQuestion(showCountRevisions);
			// Un-protecting the page
			iTestUnlockPage();
		}


		
		// Changes the answer being edited by the selected one
		function editAnswer(idresp) {
		   // Changes the answer being edited using Ajax. Callback updateAnswerForm updates the info about the answer
	       QuestionMgmt.editAnswer(idresp,updateAnswerForm);
	       enableAnswerMM();
	       // Is not a new answer because it was saved before
		   isnewanswer = false;
		   edit=true;
		} // editAnswer


		function deleteAnswer(idresp,idQuestion) {
          var conf = confirm ('<fmt:message key="confirmDeleteAnswer"/>\n<fmt:message key="alertDeleteAnswer"/>');
          
		  if (conf) {
			 if (document.getElementById("answerLegendModified")){
			 	document.getElementById("labelA").removeChild(document.getElementById("answerLegendModified"));
			 }
  		     // Protecting the page
		     iTestLockPage('');		  
		     // Delete the answer using Ajax
	         QuestionMgmt.deleteAnswer(idresp,{callback:updateListAnswers,
		         								timeout:callBackTimeOut,
		         								errorHandler:function(message) {alert(message); iTestUnlockPage('error');} });
	         // In order to avoid problems deleting the currently edited answer, the current answer is also reseted:
	         //QuestionMgmt.isInExam(idQuestion,showButtonRecorrectExam);
	         newAnswer();	         
	      }
		   
		} // deleteAnswer
		
		
		var oldMathString = "";
				
		/* Translation Ascii to MathML */
		function updateMathValue(val) {
			//Version 2.0.9 Nov 3, 2007, (c) Peter Jipsen http://www.chapman.edu/~jipsen
			//License: GNU General Public License (http://www.gnu.org/copyleft/gpl.html)			
			
			init();

  			var str = document.getElementById("matheditor").value;
  			// we compare with the old string for not updating if the string hasn't changed
  			if (str == oldMathString) {
  				return;
  			}
  			oldMathString = str;
			var outnode = document.getElementById("mathresult");
		    var n = outnode.childNodes.length;
			for (var i=0; i<n; i++)
			    outnode.removeChild(outnode.firstChild);
			outnode.appendChild(document.createTextNode("`"+str+"`"));
			AMprocessNode(outnode);
		} // updateMathValue
		
		
		function pasteQuestionMathValue(val) {

  			var strMath = document.getElementById("matheditor").value;
			var outnode = document.getElementById("textenunciado");
			var str = outnode.value;
			
			outnode.value = str+"`"+strMath+"`";
		} // pasteQuestionMathValue
		
		function rellenarCamposCopiados(val) {
			document.getElementById("textenunciado").value=currentQuestion.getText();
  			document.getElementById("questionid").value=currentQuestion.getText();

		} // pasteQuestionMathValue
		
		function pasteAnswerMathValue(val) {
			
  			var strMath = document.getElementById("matheditor").value;
			var outnode = document.getElementById("textrespuesta");
			var str = outnode.value;
			
			outnode.value = str+"`"+strMath+"`";
		} // pasteAnswerMathValue
		
		
		// Number of elements of the mmedia list for the question (maybe editing)
		var qMmediaListLength = '${fn:length(question.mmedia)}';
				
		
		/* Ajax for list the mmedia elements (callback function, receives the list of mmedia of the question) */		
		function updateQuestionMmediaList(mmedias) {
		
			var datatable,tbodyelement, mmedia, rowelement, cellelementCount, cellelementText, cellelementUp, cellelementDown, cellelementDel;
			
			// Create the table for the list
		   tbodyelement=document.createElement('tbody');
		   tbodyelement.setAttribute("id","mmediaqtbody");
			
			if (mmedias == null) {
				// It was impossible to attach the file to the question, because there is no question
				alert("<fmt:message key="errorUploadingQuestionMMedia"/>");
				// Updates size:
				qMmediaListLength = 0;	
			} else {
		   
			   // Fills the table (DOM scripting): mmedia data ---------------
			   var position = 0;
			   
			   while (position < mmedias.length) {
			        mmedia = mmedias[position];
			        position++;
	
				   // Fills the table (DOM scripting)
					rowelement = document.createElement('tr');
				
					// All the cells:
				
					// Order:
					cellelementCount = document.createElement('td');
					cellelementCount.innerHTML = mmedia.order+".&nbsp;";
					rowelement.appendChild(cellelementCount);
										
					// Name of the file
					cellelementText = document.createElement('td');
					if (mmedia.type == '7') {
						cellelementText.innerHTML = "<a target=\"_new\" href=\"http://www.youtube.com/watch?v="+mmedia.path+"\">"+mmedia.name+"</a>";
					} else { 
						cellelementText.innerHTML = mmedia.name;
					}
					rowelement.appendChild(cellelementText);
	
					// Control elements:
					cellelementUp = document.createElement('td');
					cellelementUp.innerHTML = "<a href=\"javascript:changeOrderQuestionMmedia("+mmedia.order+","+(mmedia.order-1)+");\"><img src=\"${pageContext.request.contextPath}/imagenes/up.gif\" alt=\"<fmt:message key="labelUpOrderFile"/>\" border=\"none\"></a>";
					rowelement.appendChild(cellelementUp);
	
					cellelementDown = document.createElement('td');
					cellelementDown.innerHTML = "<a href=\"javascript:changeOrderQuestionMmedia("+mmedia.order+","+(mmedia.order+1)+");\"><img src=\"${pageContext.request.contextPath}/imagenes/down.gif\" alt=\"<fmt:message key="labelDownOrderFile"/>\" border=\"none\"></a>";
					rowelement.appendChild(cellelementDown);
	
					cellelementConf = document.createElement('td');
					cellelementConf.innerHTML = "<a href=\"javascript:showQMmediaConfig('"+mmedia.id+"');\"><img src=\"${pageContext.request.contextPath}/imagenes/editar.gif\" alt=\"<fmt:message key="labelEditMmediaSize"/>\" title=\"<fmt:message key="labelEditMmediaSize"/>\" border=\"none\"></a>";
					rowelement.appendChild(cellelementConf);
								
					cellelementDel = document.createElement('td');
					cellelementDel.innerHTML = "<a href=\"javascript:deleteQuestionMmedia('"+mmedia.id+"');\"><img src=\"${pageContext.request.contextPath}/imagenes/borrar.gif\" alt=\"<fmt:message key="labelDeleteFile"/>\" border=\"none\"></a>";
					rowelement.appendChild(cellelementDel);
					
					// Add row
					tbodyelement.appendChild(rowelement);
				}
			
				// There is a limit of three archives:
				if (mmedias.length == 3) {
					alert("<fmt:message key="warningLimitFiles"/>");
					document.getElementById("buttonAddFileQ").style.display = "none";
					document.getElementById("buttonAddYouTubeQ").style.display = "none";
				} else {
					document.getElementById("buttonAddFileQ").style.display = "";
					document.getElementById("buttonAddYouTubeQ").style.display = "";
				}
				
				// Updates size:
				qMmediaListLength = mmedias.length;

			}
		
			// Replaces tbody
			datatable=document.getElementById("mmediaqtable");
			datatable.replaceChild(tbodyelement,document.getElementById("mmediaqtbody"));
		
			

		    // Hiding the div to avoid double-click
		    iTestUnlockPage();

		} // updateQuestionMmediaList
		
		
		function deleteQuestionMmedia(idqmm) {
		  // Deletes a Mmedia element
          var conf = confirm ('<fmt:message key="confirmDeleteFile"/>');
		  
		  if (conf) {
		     // Delete the file using Ajax
	         QuestionMgmt.deleteQuestionMmedia(idqmm,updateQuestionMmediaList);
	      }
		} // deleteQuestionMmedia
		

		function changeOrderQuestionMmedia(oldorder,neworder) {
		     // Change the order using Ajax (callback function)
	         QuestionMgmt.changeOrderQuestionMmedia(oldorder,neworder,updateQuestionMmediaList);
		} // changeOrderQuestionMmedia

		function changeOrderCommentMmedia(oldorder,neworder) {
		     // Change the order using Ajax (callback function)
	         QuestionMgmt.changeOrderCommentMmedia(oldorder,neworder,updateCommentMmediaList);
		} // changeOrderQuestionMmedia
		
		function saveCommentMmedia(file){
		   var rowelement, cellelement;
		   var files = document.getElementById('mmediaCtbody').getElementsByTagName('tr').length;
		   if(files>=2){
			   return;
			}
		   rowelement = document.createElement('tr');
		   cellelement = document.createElement('td');
		   cellelement.colSpan=5;
		   cellelement.setAttribute("align","center");
		   cellelement.innerHTML = "<fmt:message key="labelUploadingFile"/>";
		   rowelement.appendChild(cellelement);
		   document.getElementById("mmediaCtbody").appendChild(rowelement);

		   var mmediacommentfile = document.getElementById("mmediacomment").value;

		   iTestLockPage('');
			// Adds the mmedia using Ajax: callback function is updateMmediaList
	       QuestionMgmt.addCommentMmedia(mmediacommentfile,file,{callback:updateCommentMmediaList,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
		   
		}

		function updateCommentMmediaList(mmedias) {

			var datatable,tbodyelement, mmedia, rowelement, cellelementCount, cellelementText, cellelementUp, cellelementDown, cellelementDel;
			
		   // Create the table for the list
		   tbodyelement=document.createElement('tbody');
		   tbodyelement.setAttribute("id","mmediaCtbody");
		
			if (mmedias == null) {
				// It was impossible to attach the file to the answer, because there is no answer
				alert("<fmt:message key="errorUploadingQuestionMMedia"/>");
			} else {
			
			   // Fills the table (DOM scripting): mmedia data ---------------
			   var position = 0;
			   
			   while (position < mmedias.length) {
			        mmedia = mmedias[position];
			        position++;
	
				   // Fills the table (DOM scripting)
					rowelement = document.createElement('tr');
				
					// All the cells:
				
					// Order:
					cellelementCount = document.createElement('td');
					cellelementCount.innerHTML = mmedia.order+".&nbsp;";
					rowelement.appendChild(cellelementCount);
										
					// Name of the file
					cellelementText = document.createElement('td');
					cellelementText.innerHTML = mmedia.name;
					rowelement.appendChild(cellelementText);
					
					cellelementUp = document.createElement('td');
					cellelementUp.innerHTML = "<a href=\"javascript:changeOrderCommentMmedia("+mmedia.order+","+(mmedia.order-1)+");\"><img src=\"${pageContext.request.contextPath}/imagenes/up.gif\" alt=\"<fmt:message key="labelUpOrderFile"/>\" border=\"none\"></a>";
					rowelement.appendChild(cellelementUp);
	
					cellelementDown = document.createElement('td');
					cellelementDown.innerHTML = "<a href=\"javascript:changeOrderCommentMmedia("+mmedia.order+","+(mmedia.order+1)+");\"><img src=\"${pageContext.request.contextPath}/imagenes/down.gif\" alt=\"<fmt:message key="labelDownOrderFile"/>\" border=\"none\"></a>";
					rowelement.appendChild(cellelementDown);

					cellelementDel = document.createElement('td');
					cellelementDel.innerHTML = "<a href=\"javascript:deleteCommentMmedia('"+mmedia.id+"');\"><img src=\"${pageContext.request.contextPath}/imagenes/borrar.gif\" alt=\"<fmt:message key="labelDeleteFile"/>\" border=\"none\"></a>";
					rowelement.appendChild(cellelementDel);
					
					// Add row
					tbodyelement.appendChild(rowelement);
				}
			
				// There is a limit of one archives:
				if (mmedias.length >= 2) {
					document.getElementById("buttonAddFileC").style.display = "none";
					document.getElementById("buttonAddYouTubeC").style.display = "none";
				} else {
					document.getElementById("buttonAddFileC").style.display = "block";
					document.getElementById("buttonAddYouTubeC").style.display = "block";
				}
			}
		    
		    // Replaces tbody
			datatable=document.getElementById("mmediaCtable");
			datatable.replaceChild(tbodyelement,document.getElementById("mmediaCtbody"));
		    
		
		    // Hiding the div to avoid double-click
		    iTestUnlockPage();
		
		} // updateQuestionMmediaList
			
		function deleteCommentMmedia(id){
			 // Deletes a Mmedia element
	          var conf = confirm ('<fmt:message key="confirmDeleteFile"/>');
			  
			  if (conf) {
			     // Delete the file using Ajax
		         QuestionMgmt.deleteCommentMmedia(id,updateCommentMmediaList);
		      }
		}
				
		function addQuestionMmedia(file) {
			/* Saves the mmedia file associated to the question */
		   var files = document.getElementById('mmediaqtbody').getElementsByTagName('tr').length;
			if(files>=3){
				return;
			}
			// We establish a temporal message:
		   var rowelement, cellelement;
		   rowelement = document.createElement('tr');
		   cellelement = document.createElement('td');
		   cellelement.colSpan=5;
		   cellelement.setAttribute("align","center");
		   cellelement.innerHTML = "<fmt:message key="labelUploadingFile"/>";
		   rowelement.appendChild(cellelement);
		   document.getElementById("mmediaqtbody").appendChild(rowelement);
   		   var mmediaquestionfile = document.getElementById("mmediaquestion").value;

		  if(files<=3){
			  // Adds the mmedia using Ajax: callback function is updateMmediaList
			  iTestLockPage('');
		       QuestionMgmt.addQuestionMmedia(mmediaquestionfile,file,{callback:updateQuestionMmediaList,
					 timeout:callBackTimeOut,
					 errorHandler:function(message) { iTestUnlockPage('error');} });
		   }

		} // addQuestionMmedia
		

		function addQuestionMmediaYouTube() {
			/* Saves the youtube video as a mmedia file associated to the question */
			var files = document.getElementById('mmediaqtbody').getElementsByTagName('tr').length;
			if(files>=3){
				return;
			}
   		   var youTubeURL = document.getElementById("mmediaYTURLquestion").value;
   		   var youTubeTitle = document.getElementById("mmediaYTTitlequestion").value;
   		
   		   if ((youTubeURL != "") && (youTubeTitle !="")) {

   			   if (youTubeTitle.length > 40) {
  		      	    alert("<fmt:message key="cannotSaveYTvideo"/> <fmt:message key="msgTitleTooLong"/>");
  		     		return;
  		   	   }
   	   		   
	   		   // Parse the url: need to separate the key which is after the "=" symbol
	   		   var vars = youTubeURL.split("=");
	   		   var videoKey = vars[1];

			   // URL should match the correct format:
			   if ((vars[0] == "http://www.youtube.com/watch?v") && (videoKey != "")) {
				   // We establish a temporal message:
				   var rowelement, cellelement;
				   rowelement = document.createElement('tr');
				   cellelement = document.createElement('td');
				   cellelement.colSpan=5;
				   cellelement.setAttribute("align","center");
				   cellelement.innerHTML = "<fmt:message key="labelUploadingFile"/>";
				   rowelement.appendChild(cellelement);
				   document.getElementById("mmediaqtbody").appendChild(rowelement);

				   // The video title is passed as first argument, using a keyword:
				   youTubeTitle = 'YT:'+youTubeTitle;
			
				   // Adds the mmedia using Ajax: callback function is updateMmediaList
			       QuestionMgmt.addQuestionMmedia(youTubeTitle,videoKey,updateQuestionMmediaList);
			       
			   } else {
				   alert("<fmt:message key="cannotSaveYTvideo"/> <fmt:message key="msgInvalidURL"/>");
			       return;
			   }

			} else {
			   alert("<fmt:message key="cannotSaveYTvideo"/> <fmt:message key="msgEmptyURLorTitle"/>");
		       return;
		    }

		} // addQuestionMmediaYouTube

		function addCommentMmediaYouTube() {
			/* Saves the youtube video as a mmedia file associated to the comment */
			   var files = document.getElementById('mmediaCtbody').getElementsByTagName('tr').length;
			   if(files>=2){
					return;
				   }
	   		    var youTubeURL = document.getElementById("mmediaYTURLcomment").value;
	   		    var youTubeTitle = document.getElementById("mmediaYTTitlecomment").value;

				if ((youTubeURL != "") && (youTubeTitle !="")) {

		   			   if (youTubeTitle.length > 40) {
		  		      	    alert("<fmt:message key="cannotSaveYTvideo"/> <fmt:message key="msgTitleTooLong"/>");
		  		     		return;
		  		   	   }
		   	   		   
			   		   // Parse the url: need to separate the key which is after the "=" symbol
			   		   var vars = youTubeURL.split("=");
			   		   var videoKey = vars[1];

					   // URL should match the correct format:
					   if ((vars[0] == "http://www.youtube.com/watch?v") && (videoKey != "")) {
						   // We establish a temporal message:
						   var rowelement, cellelement;
						   rowelement = document.createElement('tr');
						   cellelement = document.createElement('td');
						   cellelement.colSpan=5;
						   cellelement.setAttribute("align","center");
						   cellelement.innerHTML = "<fmt:message key="labelUploadingFile"/>";
						   rowelement.appendChild(cellelement);
						   document.getElementById("mmediaCtbody").appendChild(rowelement);

						   // The video title is passed as first argument, using a keyword:
						   youTubeTitle = 'YT:'+youTubeTitle;
					
						   // Adds the mmedia using Ajax: callback function is updateMmediaList
					       QuestionMgmt.addCommentMmedia(youTubeTitle,videoKey,updateCommentMmediaList);
					       
					   } else {
						   alert("<fmt:message key="cannotSaveYTvideo"/> <fmt:message key="msgInvalidURL"/>");
					       return;
					   }

					} else {
					   alert("<fmt:message key="cannotSaveYTvideo"/> <fmt:message key="msgEmptyURLorTitle"/>");
				       return;
				    }
		   		   
		} // addCommentMmediaYouTube
		
		// Function to display/hide the symbols and divs for YouTube on question
		function changeYTcontrol(key) {
			// By default, hide
			if(askCreated){
				var mode = 'none';
				var noMode = 'block';
				if (key == 'show') {
					// Maybe show
				    mode = 'block';
				    noMode = 'none';
				}	
				// Div
				document.getElementById('youTubeQControls').style.display=mode;
				// "Minus"
				document.getElementById('minusYT').style.display=mode;		
				// "Plus"
				document.getElementById('plusYT').style.display=noMode;
			}
				
		} // changeYTcontrol
		function changeYTCommentControl(key) {
			// By default, hide
			if(askCreated){
				var mode = 'none';
				var noMode = 'block';
				if (key == 'show') {
					// Maybe show
				    mode = 'block';
				    noMode = 'none';
				}	
				// Div
				document.getElementById('youTubeCControls').style.display=mode;
				// "Minus"
				document.getElementById('minusYTComment').style.display=mode;		
				// "Plus"
				document.getElementById('plusYTComment').style.display=noMode;
			}
				
		} // changeYTcontrol
		
		
		/* Ajax for list the mmedia elements (callback function, receives the list of mmedia of the answer) */		
		function updateAnswerMmediaList(mmedias) {
			
			var datatable,tbodyelement, mmedia, rowelement, cellelementCount, cellelementText, cellelementUp, cellelementDown, cellelementDel;
			
		   // Create the table for the list
		   tbodyelement=document.createElement('tbody');
		   tbodyelement.setAttribute("id","mmediaAtbody");
		
			if (mmedias == null) {
				// It was impossible to attach the file to the answer, because there is no answer
				alert("<fmt:message key="errorUploadingQuestionMMedia"/>");
			} else {
			
			   // Fills the table (DOM scripting): mmedia data ---------------
			   var position = 0;
			   
			   while (position < mmedias.length) {
			        mmedia = mmedias[position];
			        position++;
	
				   // Fills the table (DOM scripting)
					rowelement = document.createElement('tr');
				
					// All the cells:
				
					// Order:
					cellelementCount = document.createElement('td');
					cellelementCount.innerHTML = mmedia.order+".&nbsp;";
					rowelement.appendChild(cellelementCount);
										
					// Name of the file
					cellelementText = document.createElement('td');
					cellelementText.innerHTML = mmedia.name;
					rowelement.appendChild(cellelementText);
	
					// Control elements:
					cellelementUp = document.createElement('td');
					cellelementUp.innerHTML = "<a href=\"javascript:changeOrderAnswerMmedia("+mmedia.order+","+(mmedia.order-1)+");\"><img src=\"${pageContext.request.contextPath}/imagenes/up.gif\" alt=\"<fmt:message key="labelUpOrderFile"/>\" border=\"none\"></a>";
					rowelement.appendChild(cellelementUp);
	
					cellelementDown = document.createElement('td');
					cellelementDown.innerHTML = "<a href=\"javascript:changeOrderAnswerMmedia("+mmedia.order+","+(mmedia.order+1)+");\"><img src=\"${pageContext.request.contextPath}/imagenes/down.gif\" alt=\"<fmt:message key="labelDownOrderFile"/>\" border=\"none\"></a>";
					rowelement.appendChild(cellelementDown);
				
					cellelementDel = document.createElement('td');
					cellelementDel.innerHTML = "<a href=\"javascript:deleteAnswerMmedia('"+mmedia.id+"');\"><img src=\"${pageContext.request.contextPath}/imagenes/borrar.gif\" alt=\"<fmt:message key="labelDeleteFile"/>\" border=\"none\"></a>";
					rowelement.appendChild(cellelementDel);
					
					// Add row
					tbodyelement.appendChild(rowelement);
				}
			
				// There is a limit of one archives:
				if (mmedias.length == 1) {
					alert("<fmt:message key="warningLimitFiles"/>");
					document.getElementById("buttonAddFileA").style.display = "none";
				} else {
					document.getElementById("buttonAddFileA").style.display = "block";
				}
			
				// Visibility:
				document.getElementById("divrespuestader").style.display = "block";
		
		
			    // Updates the list of answers, just to maintain the "clip" image or not:
			    // Ajax with callback updateListAnswers
			    	QuestionMgmt.getCurrentQuestionAnswers(updateListAnswers);
				
			}
		    
		    // Replaces tbody
			datatable=document.getElementById("mmediaAtable");
			datatable.replaceChild(tbodyelement,document.getElementById("mmediaAtbody"));
		    
		
		    // Hiding the div to avoid double-click
		    iTestUnlockPage();
		    
		} // updateAnswerMmediaList


		function deleteAnswerMmedia(idamm) {
		  // Deletes a Mmedia element
          var conf = confirm ('<fmt:message key="confirmDeleteFile"/>');
		  
		  if (conf) {
		     // Delete the file using Ajax
	         QuestionMgmt.deleteAnswerMmedia(idamm,updateAnswerMmediaList);
	      }
		} // deleteAnswerMmedia
		

		function changeOrderAnswerMmedia(oldorder,neworder) {
		     // Change the order using Ajax (callback function)
	         QuestionMgmt.changeOrderAnswerMmedia(oldorder,neworder,updateAnswerMmediaList);
		} // changeOrderAnswerMmedia

		
		function saveAnswerMmedia(file) {
			/* Saves the mmedia file associated to the question */
		   var mmediaanswerfile = document.getElementById("mmediaanswer").value;
		   var files = document.getElementById('mmediaAtbody').getElementsByTagName('tr').length;
		   if(files >=1){
				return;
			   }
		   // Save the mmedia using Ajax: callback function is updateMmediaList
	       QuestionMgmt.addAnswerMmedia(mmediaanswerfile,file,{callback:updateAnswerMmediaList,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });

		} // saveQuestionMmedia


		/* For File Upload */		
		function submitUploadForm() {
			/* Submit the form to upload the file */
		  	//document.getElementById("questionfilemmedia").value = document.getElementById("mmediaquestion").value;
			document.getElementById("formadjsenunciado").submit();
		}
		
		function questionUploadError() {
			alert("<fmt:message key="errorUploadingFile"/>");	
		}

		function MMSizeError(){
			alert("<fmt:message key="msgMMErrorSize"/>");
		}
		
		function setDisabledCustomizedQuestionMmediaSize(dis) {
			document.getElementById("radioQMMWidthAuto").disabled = dis;
			document.getElementById("radioQMMWidthPerc").disabled = dis;
			document.getElementById("radioQMMWidthPx").disabled = dis;
			document.getElementById("radioQMMHeightAuto").disabled = dis;
			document.getElementById("radioQMMHeightPerc").disabled = dis;
			document.getElementById("radioQMMHeightPx").disabled = dis;
			if (dis) {
				document.getElementById("inputQMMWidthPerc").disabled = true;
				document.getElementById("inputQMMWidthPx").disabled = true;
				document.getElementById("inputQMMHeightPerc").disabled = true;
				document.getElementById("inputQMMHeightPx").disabled = true;
				document.getElementById("radioQMMWidthAuto").checked = true;
				document.getElementById("radioQMMWidthPerc").checked = false;
				document.getElementById("radioQMMWidthPx").checked = false;
				document.getElementById("radioQMMHeightAuto").checked = true;
				document.getElementById("radioQMMHeightPerc").checked = false;
				document.getElementById("radioQMMHeightPx").checked = false;
				document.getElementById("inputQMMWidthPerc").value = "";
				document.getElementById("inputQMMWidthPx").value = "";
				document.getElementById("inputQMMHeightPerc").value = "";
				document.getElementById("inputQMMHeightPx").value = "";
			}
		}
		
		function setDisabledQMMWidth(perc, px) {
			document.getElementById("inputQMMWidthPerc").disabled = perc;
			if (perc) {
				document.getElementById("inputQMMWidthPerc").value = "";
			}
			document.getElementById("inputQMMWidthPx").disabled = px;
			if (px) {
				document.getElementById("inputQMMWidthPx").value = "";
			}
		}
				
		function setDisabledQMMHeight(perc,px) {
			document.getElementById("inputQMMHeightPerc").disabled = perc;
			if (perc) {
				document.getElementById("inputQMMHeightPerc").value = "";
			}
			document.getElementById("inputQMMHeightPx").disabled = px;
			if (px) {
				document.getElementById("inputQMMHeightPx").value = "";
			}
		}
		
		var configuringQMMid = 0;
		
		function showQMmediaConfig(id) {
			configuringQMMid = id;
			QuestionMgmt.getQuestionMmediaByID(id, openQMmediaConfig);
		}

		var geogebraPath = '';
		var geogebraId;
		function activeGeogebraDiv(advanced){
			document.getElementById('divMoreInfo').style.display='';
			var innerHTML = '<applet id="geogebraAppletView" code="geogebra.GeoGebraApplet" archive="http://www.geogebra.org/webstart/geogebra.jar" width="500" height="375">';
			innerHTML += '<param name="filename" value="../common/mmedia/'+geogebraPath+'"/>" />'
			if(advanced){
				innerHTML += '<param name="enableLabelDrags" value="true" />';
				innerHTML += '<param name="showToolBar" value="true" />';
				innerHTML += '<param name="showToolBarHelp" value="true" />';
				innerHTML += '<param name="showAlgebraInput" value="true" />';
				innerHTML += '<param name="allowRescaling" value="true" />';
				innerHTML += '<param name="framePossible" value="true" />';
			}
			innerHTML += '</applet>';
			document.getElementById('divGeogebra').innerHTML = innerHTML;
			QuestionMgmt.editGeogebraMM(geogebraId,advanced);
		}
		
		/* Ajax for mmedia configuration (callback function, receives the mmedia) */
		function openQMmediaConfig(mmedia) {
			if (mmedia == null) {
				alert("<fmt:message key="noMmediaByIDfound"/>");
			} else if (mmedia.type !=2) {
				if(mmedia.type == 4 && ${user.role eq 'TUTORAV'}){
					$('#divMoreInfo').show('slow',function(){
							geogebraId = mmedia.id;
							geogebraPath = mmedia.path;
							if(mmedia.geogebraType == 0){
								activeGeogebraDiv(false);
								document.getElementById('avancedOptionNo').checked = true;
								document.getElementById('avancedOptionYes').checked = false;
							}
							else{
								activeGeogebraDiv(true);
								document.getElementById('avancedOptionYes').checked = true;
								document.getElementById('avancedOptionNo').checked = false;
							}
						});
				}else{
					// Not allowed to configure size for this type of multimedia
					alert("<fmt:message key="notAllowedMmediaConfig"/>");
				}
			} else { 	
				if (mmedia.width == "big") {
					setDisabledCustomizedQuestionMmediaSize(true);
					document.getElementById("radioBigQMMSize").checked = true;
				} else if (mmedia.width == "medium") {
					setDisabledCustomizedQuestionMmediaSize(true);
					document.getElementById("radioMediumQMMSize").checked = true;
				} else if (mmedia.width == "small" || mmedia.width == "" && mmedia.height =="") {
					setDisabledCustomizedQuestionMmediaSize(true);
					document.getElementById("radioSmallQMMSize").checked = true;
				} else if (mmedia.width == "auto") {
					setDisabledCustomizedQuestionMmediaSize(true);
					document.getElementById("radioAutoQMMSize").checked = true;
				} else if (mmedia.width == null && mmedia.height == null) {
					setDisabledCustomizedQuestionMmediaSize(true);
					document.getElementById("radioBigQMMSize").checked = false;
					document.getElementById("radioMediumQMMSize").checked = false;
					document.getElementById("radioSmallQMMSize").checked = true;
					document.getElementById("radioAutoQMMSize").checked = false;
				} else {
				
					document.getElementById("radioCustomizedQMMSize").checked = true;
					setDisabledCustomizedQuestionMmediaSize(false);
					if (mmedia.width == "") {
						setDisabledQMMWidth(true, true);
						document.getElementById("radioQMMWidthAuto").checked = true;
					} else if (mmedia.width.substring(mmedia.width.length-1,mmedia.width.length) == "%") {
						setDisabledQMMWidth(false, true);
						document.getElementById("radioQMMWidthPerc").checked = true;
						document.getElementById("inputQMMWidthPerc").value = mmedia.width.substring(0,mmedia.width.length-1);
					} else {
						setDisabledQMMWidth(true, false);
						document.getElementById("radioQMMWidthPx").checked = true;
						document.getElementById("inputQMMWidthPx").value = mmedia.width;
					}
					if (mmedia.height == "") {
						setDisabledQMMHeight(true, true);
						document.getElementById("radioQMMHeightAuto").checked = true;
					} else if (mmedia.height.substring(mmedia.height.length-1,mmedia.height.length) == "%") {
						setDisabledQMMHeight(false, true);
						document.getElementById("radioQMMHeightPerc").checked = true;
						document.getElementById("inputQMMHeightPerc").value = mmedia.height.substring(0,mmedia.height.length-1);
					} else {
						setDisabledQMMHeight(true, false);
						document.getElementById("radioQMMHeightPx").checked = true;
						document.getElementById("inputQMMHeightPx").value = mmedia.height;
					}
				}
				document.getElementById("divEditQMmediaSize").style.display="block";
			}
			
		}
		
		function closeQMmediaConfig() {
			configuringQMMid = 0;
			document.getElementById("divEditQMmediaSize").style.display="none";
		}
		
		function saveQMMConfig() {
			if (configuringQMMid != 0) {
				var width = "";
				var height = "";
				if (document.getElementById("radioBigQMMSize").checked) {
					width = "big";
				} else if (document.getElementById("radioMediumQMMSize").checked) {
					width = "medium";
				} else if (document.getElementById("radioSmallQMMSize").checked) {
					width = "small";
				} else if (document.getElementById("radioAutoQMMSize").checked) {
					width = "auto";
				}  else if (document.getElementById("radioCustomizedQMMSize").checked) {
					if (document.getElementById("radioQMMWidthPerc").checked) {
						var text = document.getElementById("inputQMMWidthPerc").value;
						var number = parseInt(text);
						if (isNaN(number) || number < 0 || number > 100) {
							alert("<fmt:message key="wrongMMWidthPerc"/>");
							return;
						}
						width = number + "%";
					} else if (document.getElementById("radioQMMWidthPx").checked) {
						var text = document.getElementById("inputQMMWidthPx").value;
						var number = parseInt(text);
						if (isNaN(number) || number < 0 || number > 1024) {
							alert("<fmt:message key="wrongMMWidthPx"/>");
							return;
						}
						width = number;
					}
					if (document.getElementById("radioQMMHeightPerc").checked) {
						var text = document.getElementById("inputQMMHeightPerc").value;
						var number = parseInt(text);
						if (isNaN(number) || number < 0 || number > 100) {
							alert("<fmt:message key="wrongMMHeightPerc"/>");
							return;
						}
						height = number + "%";
						
					} else if (document.getElementById("radioQMMHeightPx").checked) {
						var text = document.getElementById("inputQMMHeightPx").value;
						var number = parseInt(text);
						if (isNaN(number) || number < 0 || number > 1024) {
							alert("<fmt:message key="wrongMMHeightPx"/>");
							return;
						}
						height = number;
					}
				}
			}
			
			// Protecting the page
		     iTestLockPage();
			//alert('Ancho: ' + width + "\nAlto: " + height + "\nID: " + configuringQMMid);
			QuestionMgmt.setQuestionMmediaSize(configuringQMMid, width, height, iTestUnlockPage);
		}
		function submitForm(form){
			form.submit();
		}

		function changeType(){
			if(answerSaved){
				if(confirm("<fmt:message key="alertChangeQuestionType"/>")){
					questionModified(true);
					var questionType = document.getElementById('selectQuestionType').value;
					iTestLockPage('');
					QuestionMgmt.removeAnswersFromQuestion(questionType,{callback:updateQuestionType,
						 timeout:callBackTimeOut,
						 errorHandler:function(message) { iTestUnlockPage('error');} });
				}else{
					if(document.getElementById('selectQuestionType').value==0){
						document.getElementById('selectQuestionType').selectedIndex = 1;
					}else{
						document.getElementById('selectQuestionType').selectedIndex = 0;
					}
				}
			}else{
				questionModified(true);
				var questionType = document.getElementById('selectQuestionType').value;
				iTestLockPage('');
				QuestionMgmt.removeAnswersFromQuestion(questionType,{callback:updateQuestionType,
					 timeout:callBackTimeOut,
					 errorHandler:function(message) { iTestUnlockPage('error');} });
			}
			if(questionType==1){
				document.getElementById('pasteA').disabled = true;
			}else{
				document.getElementById('pasteA').disabled = false;
			}
			
		}

		function updateQuestionType(type){
			if(type==1){
				document.getElementById('answerTest').style.display='none';
				document.getElementById('answerFill').style.display='';
				document.getElementById('textRespuestaFill').value='';
				document.getElementById('labelA').innerHTML="<fmt:message key="labelAnswer"/>";
				answerModified(false);
				document.getElementById('divanswers').style.display='none';
				newFillAnswer = true;
				document.getElementById('divrespuestader').style.display ="none";
				iTestUnlockPage('');
			}else if(type==0){
				updateListAnswers('');
				document.getElementById('divrespuestader').style.display ="";
				disableAnswerMM();
				document.getElementById('answerTest').style.display='';
				document.getElementById('answerFill').style.display='none';
				document.getElementById('divanswers').style.display='';
				iTestUnlockPage('');
			}else{
				iTestUnlockPage('error');
			}
		}

		<c:choose>
			<c:when test="${empty fillAnswer}">
				var newFillAnswer = true;
			</c:when>
			<c:otherwise>
				var newFillAnswer = false;
			</c:otherwise>
		</c:choose>
		<c:if test="${!empty question}">
			answerSaved = true;
		</c:if>
		
		function saveFillAnswer(){
			answerSaved = true;
			var resp = document.getElementById('textRespuestaFill').value;
			resp = resp.replace(/^\s*|\s*$/g,"");
			document.getElementById('textRespuestaFill').value = resp;
			iTestLockPage('');
			QuestionMgmt.saveFillAnswer(resp,newFillAnswer,{callback:updateFillAnswerSaved,
														timeout:callBackTimeOut,
														errorHandler:function(message){iTestUnlockPage('error');} });
			idQuestion = document.getElementById('questionid').value;
			if(!newFillAnswer){
				QuestionMgmt.isInExam(idQuestion,{callback:showButtonRecorrectExam,
					timeout:callBackTimeOut,
					errorHandler:function(message){iTestUnlockPage('error');} });
			}
		}

		function updateFillAnswerSaved(result){
			if(result != -1){
				alert("<fmt:message key="alertFillAnswerSaved"/>");
				answerModified(false);
				enableAnswerMM();
				newFillAnswer = false;
				answerSaved = true;
			}else{
				alert("<fmt:message key="alertFillAnswerNotSaved"/>")
			}
			iTestUnlockPage();
		}

	</script>
		<div id="contenido">
		<form id="formAddNewQuestion" action="${pageContext.request.contextPath}/tutor/managegroup.itest?method=newQuestion" method="POST" style="display:none">
			<input type="hidden" name="idgroup" value="${group.id}">
		</form>
		<!-- Needed for the preview of formulae -->
	    <script type="text/javascript" src="${pageContext.request.contextPath}/common/resources/ASCII_MathML_noonload.js"></script>
		
			<div class="divenunciadoizq">
			  <fieldset id="fieldsetQ" class="setespecial"><legend id="labelQ"><fmt:message key="labelQuestion"/></legend>
				<form name="formenunciado">
					<table width="100%" class="aviso">
					<c:choose>
						<c:when test="${!empty sourceActionIsCopy and sourceActionIsCopy}">
							<tr>
								<td><fmt:message key="labelIsCopy"/></td>
							</tr>
						</c:when>
						<c:otherwise></form></c:otherwise>
					</c:choose>
					</table>
					<table class="tablaformulario">
					<tr>
					  <td><fmt:message key="labelDifficulty"/></td>
					  <td>	<label>
						<select id="combodificultad" onchange="javascript:questionModified(true);" <c:if test="${!empty question and question.usedInExam}">disabled</c:if>>
						  <%-- THE VALUES ARE CONSTANTS OF THE PROJECT --%>
						  <c:choose>
							  <%-- Maybe editing --%>
							  <c:when test="${!empty question and question.difficulty eq 0}">
							  	  <option value="0" selected><fmt:message key="diffLow"/></option>
							  </c:when>
							  <c:otherwise>
							      <option value="0"><fmt:message key="diffLow"/></option>
							  </c:otherwise>
						  </c:choose>
						  <c:choose>
						  <c:when test="${!empty question and question.difficulty eq 1}">
						  	  <option value="1" selected><fmt:message key="diffMedium"/></option>
						  </c:when>
						  <c:otherwise>
						      <option value="1"><fmt:message key="diffMedium"/></option>
						  </c:otherwise>
						  </c:choose>
						  <c:choose>
							  <c:when test="${!empty question and question.difficulty eq 2}">
							  	  <option value="2" selected><fmt:message key="diffHigh"/></option>
							  </c:when>
							  <c:otherwise>
							      <option value="2"><fmt:message key="diffHigh"/></option>
							  </c:otherwise>
						  </c:choose>
						</select>
						</label></td>
					  <td><fmt:message key="labelVisib"/></td>
					  <td>	<label>
						<select id="combovisibilidad" onchange="javascript:questionModified(true);">
						  <%-- THE VALUES ARE CONSTANTS OF THE PROJECT --%>
						  <%-- Maybe editing --%>
						  <c:choose>
							  <c:when test="${!empty question and question.visibility eq 0}">
							  	  <option value="0" selected><fmt:message key="scopeGroup"/></option>
							  </c:when>
							  <c:otherwise>
							      <option value="0"><fmt:message key="scopeGroup"/></option>
							  </c:otherwise>
						  </c:choose>
						  <c:choose>
							  <c:when test="${!empty question and question.visibility eq 2}">
							  	  <option value="2" selected><fmt:message key="scopePublic"/></option>
							  </c:when>
							  <c:otherwise>display:none;
								  <option value="2"><fmt:message key="scopePublic"/></option>
							  </c:otherwise>
						  </c:choose>
						</select>
						</label></td>
					  <td style="text-align: right">
					  	<fmt:message key="labelId"/>
					  	<input type="text" id="questionid" size="6" style="text-align: right; width: auto" disabled value="${question.id}"/>
					  </td>
					 </tr>
					<tr>
					  <td><fmt:message key="labelTitleQ"/></td>
					  <td colspan="4">
						<input type="text" id="questiontitle" value="${question.title}" onkeydown="javascript:questionModified(true);"/>
					  </td>
					</tr>
					 <tr>
					  <td><fmt:message key="labelTheme"/></td>
					  <td colspan="4">
						<label>
							<c:choose>
								<c:when test="${!empty themes}">
									<select id="idtheme" size="1" onchange="javascript:questionModified(true);" <c:if test="${!empty question and question.usedInExam}">disabled</c:if>>
										<c:if test="${empty question}">
											<option value="${theme.id}" selected>--------------</option>
										</c:if>
										<c:forEach items="${themes}" var="theme">
										    <c:choose>
									    		<%-- Same theme has to be selected --%>
									    		<c:when test="${!empty question}">
										    		<c:choose>
   												    	<c:when test="${question.subject.id eq theme.id}"><option value="${theme.id}" selected><c:out value="${theme.sort}. ${theme.subject}"/></option></c:when>
													    <c:otherwise><option value="${theme.id}"><c:out value="${theme.sort}. ${theme.subject}"/></option></c:otherwise>
										    		</c:choose>
									    		</c:when>
											    <c:otherwise><option value="${theme.id}"><c:out value="${theme.sort}. ${theme.subject}"/></option></c:otherwise>
										    </c:choose>
										</c:forEach>
									</select>
								</c:when>
								<c:otherwise>
									<fmt:message key="noAvailableThemes"/>
								</c:otherwise>
							</c:choose>						
						</label>
					  </td>
					</tr>
					<tr>
						<td><fmt:message key="labelQuestionType"/>:</td>
						<td>
							<select id="selectQuestionType"<c:if test="${!empty question and question.usedInExam}">disabled</c:if> onchange="javascript:changeType();">
								<option value="0"<c:if test="${question.type eq 0}">selected</c:if>><fmt:message key="labelQuestionTest"/></option>
								<option value="1"<c:if test="${question.type eq 1}">selected</c:if>><fmt:message key="labelQuestionFill"/></option>
							</select>
						</td>
					</tr>
					<tr>
					  <td><fmt:message key="labelTextQ"/></td>
					  <td colspan="4">
					  	 <table>
					  		<tr>
					  			<td>
					  				<div style="float:left;">
					  					<img class="buttonBBCode" src="${pageContext.request.contextPath}/imagenes/bold.gif" onclick="javascript:tagBold(document.getElementById('textenunciado'),document.getElementById('questionResult')); questionModified('true');"  title="<fmt:message key="titleTagBold"/>"/>
										<img class="buttonBBCode" src="${pageContext.request.contextPath}/imagenes/italic.gif" onclick="javascript:tagU(document.getElementById('textenunciado'),document.getElementById('questionResult')); questionModified('true');" title="<fmt:message key="titleTagItalic"/>"/>
										<img class="buttonBBCode" src="${pageContext.request.contextPath}/imagenes/underline.gif" onclick="javascript:tagS(document.getElementById('textenunciado'),document.getElementById('questionResult')); questionModified('true');" title="<fmt:message key="titleTagUnderline"/>"/>
										<img class="buttonBBCode" src="${pageContext.request.contextPath}/imagenes/hipervinculo.gif" onclick="javascript:addLink(document.getElementById('textenunciado'),document.getElementById('questionResult')); questionModified('true');" title="<fmt:message key="titleTagLink"/>"/>
										<button title="<fmt:message key="titleTagColor"/>"  class="buttonBBCode"  onclick="document.getElementById('divColorQuestion').style.display='block'; return false;"><img id="bau" src="${pageContext.request.contextPath}/imagenes/font_color.gif"/></button>
					  					
					  				</div>
					  				<div id="divColorQuestion" style="float:right;background-color:#D8D8D8; width:120px; display:none;">
					  						<button class="buttonBBCode" onclick="javascript:getColor(document.getElementById('textenunciado'),'red');document.getElementById('divColorQuestion').style.display='none'; questionModified('true'); return false;"style="background-color:red;">&nbsp;</button>
					  						<button class="buttonBBCode" onclick="javascript:getColor(document.getElementById('textenunciado'),'blue');document.getElementById('divColorQuestion').style.display='none'; questionModified('true'); return false;"style="background-color:blue;">&nbsp;</button>
					  						<button class="buttonBBCode" onclick="javascript:getColor(document.getElementById('textenunciado'),'green');document.getElementById('divColorQuestion').style.display='none'; questionModified('true'); return false;"style="background-color:green;">&nbsp;</button>
					  				</div>
					  			</td>
							</tr>
							<tr>
								<td colspan="5"><textarea id="textenunciado" cols="80" rows="6" onkeydown="javascript:questionModified(true);"><c:if test="${!empty question}"><c:out value="${question.text}"/></c:if></textarea></td>
							</tr>
						</table>
						<input type="hidden" id="questionResult"/>
					  </td>
					</tr>
					<tr>
					  <td><fmt:message key="labelComm"/></td>
					  <td colspan="4">
					  	<table>
					  		<tr>
					  			<td>
						  			<div style="float:left">
						  				<img class="buttonBBCode" src="${pageContext.request.contextPath}/imagenes/bold.gif" onclick="javascript:tagBold(document.getElementById('textcomentario'),document.getElementById('commentResult'));questionModified('true');" title="<fmt:message key="titleTagBold"/>"/>
										<img class="buttonBBCode" src="${pageContext.request.contextPath}/imagenes/italic.gif" onclick="javascript:tagU(document.getElementById('textcomentario'),document.getElementById('commentResult'));questionModified('true');" title="<fmt:message key="titleTagItalic"/>"/>
										<img class="buttonBBCode" src="${pageContext.request.contextPath}/imagenes/underline.gif" onclick="javascript:tagS(document.getElementById('textcomentario'),document.getElementById('commentResult'));questionModified('true');" title="<fmt:message key="titleTagUnderline"/>"/>
										<img class="buttonBBCode" src="${pageContext.request.contextPath}/imagenes/hipervinculo.gif" onclick="javascript:addLink(document.getElementById('textcomentario'),document.getElementById('commentResult'));questionModified('true');" title="<fmt:message key="titleTagLink"/>"/>
										<button class="buttonBBCode" title="<fmt:message key="titleTagColor"/>"   onclick="document.getElementById('divColorComment').style.display='block'; return false;"><img id="bau" src="${pageContext.request.contextPath}/imagenes/font_color.gif" /></button>
					  					
						  			</div>
						  			<div id="divColorComment" style="float:right;background-color:#D8D8D8; width:120px; display:none;">
					  						<button class="buttonBBCode" onclick="javascript:getColor(document.getElementById('textcomentario'),'red');document.getElementById('divColorComment').style.display='none'; questionModified('true'); return false;"style="background-color:red;">&nbsp;</button>
					  						<button class="buttonBBCode" onclick="javascript:getColor(document.getElementById('textcomentario'),'blue');document.getElementById('divColorComment').style.display='none'; questionModified('true'); return false;"style="background-color:blue;">&nbsp;</button>
					  						<button class="buttonBBCode" onclick="javascript:getColor(document.getElementById('textcomentario'),'green');document.getElementById('divColorComment').style.display='none'; questionModified('true'); return false;"style="background-color:green;">&nbsp;</button>
					  				</div>
						  		</td>							
							</tr>
							<tr>
								<td colspan="5"><textarea id="textcomentario" cols="80" rows="6"  onkeydown="javascript:questionModified(true);" ><c:if test="${!empty question}"><c:out value="${question.comment}"/></c:if></textarea></td>
							</tr>
						</table>
						<input type="hidden" id="commentResult"/>
					  </td>
					</tr>		
				   </table>
   				   <table class="tablaformulario">								
					<tr>
					  <td>&nbsp;</td>
					  <td><label><input type="button" name="submitsave" onclick="javascript:saveQuestion();" value="<fmt:message key="buttonSave"/>"></label></td>
					  <%-- Maybe editing --%>
					  <c:choose>
						  <c:when test="${!empty question}">
						  		<td><label><input id="newquestion" type="button" name="newquestion" onclick="javascript:alert2Exit('newQuestion');" value="<fmt:message key="buttonNewQuestion"/>"></label></td>
	  	  					  <td><label><input id="preliminar" type="button" name="submitverpreg" onclick="window.open('${pageContext.request.contextPath}/tutor/managequestion.itest?method=questionPreview&role=${group.studentRole}', '_blank', 'width=600,height=400,scrollbars=YES,resizable=YES'); return false;" value="<fmt:message key="buttonPreview"/>"></label></td>
	  	  				  	  <td><label><input id="newQuestionCopy" type="button" name="newQuestionCopy" onclick="alert2Exit('copyQuestion')" value="<fmt:message key="buttonCopyQuestion"/>"></label></td>
	  	  				  	  <td><label><input id="newQuestionPDF" type="button" name="newQuestionPDF" onclick="window.open('${pageContext.request.contextPath}/tutor/managequestion.itest?method=generateQuestionPDF&idQuestion=${question.id}', '_blank', 'width=600,height=400,scrollbars=YES,resizable=YES'); return false;" value="<fmt:message key="generatePDF"/>"></label></td>
  					  		  <td>&nbsp;</td>
	  	  				  </c:when>
	  	  				  <c:otherwise>
		  					  <!-- The preview needs the question to be saved first -->
		  					  <td><label><input id="newquestion" type="button" name="newquestion" onclick="javascript:alert2Exit('newQuestion');" value="<fmt:message key="buttonNewQuestion"/>" style="display:none;"></label></td>
  	  	  					  <td><label><input id="preliminar" type="button" name="submitverpreg" onclick="window.open('${pageContext.request.contextPath}/tutor/managequestion.itest?method=questionPreview&role=${group.studentRole}', '_blank', 'width=600,height=400,scrollbars=YES,resizable=YES'); return false;" value="<fmt:message key="buttonPreview"/>" style="display:none;"></label></td>
	  	  				  	  <td><label><input id="newQuestionCopy" type="button" name="newQuestionCopy" onclick="alert2Exit('copyQuestion')" value="<fmt:message key="buttonCopyQuestion"/>" style="display:none;"></label></td>
	  	  				  	  <td><label><input id="newQuestionPDF" type="button" name="newQuestionPDF" onclick="javascript:showPdf();" value="<fmt:message key="generatePDF"/>" style="display:none;"></label></td>
  					  		  <td>&nbsp;</td>
	  	  				  </c:otherwise>
	  	  			  </c:choose>
	  	  			  
				    </tr>
				   </table>
				  </form>				   
			  </fieldset>
		</div>
		<div class="divrespuestader">
		
			  <fieldset class="setherramienta"><legend><fmt:message key="labelMathEd"/></legend>
			  <form name="formmates" method="post">
				<table class="tablaformulario">
                  <tr>
                    <td><textarea id="matheditor" name="texteditor" rows="1" onkeyup="javascript:updateMathValue();"></textarea></td>
                  </tr>
                  <tr>
                    <td>
                       <input type="button" id="pasteQ" value="<fmt:message key="buttonPasteQ"/>" onclick="javascript:pasteQuestionMathValue();questionModified(true);">&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                  </tr>
                  <tr>
                    <td>
					  <%-- Maybe editing: button must be available --%>
					  <c:choose>
						  <c:when test="${!empty question and question.type eq 0}">
                       		 <input type="button" id="pasteA" value="<fmt:message key="buttonPasteA"/>" onclick="javascript:pasteAnswerMathValue();answerModified(true);">
                       	  </c:when>
                       	  <c:otherwise>
                       		 <input type="button" id="pasteA" value="<fmt:message key="buttonPasteA"/>" onclick="javascript:pasteAnswerMathValue();answerModified(true);" disabled>
                       	  </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>
                  <tr>
                    <td><div id="mathresult" name="textlatex"></div></td>
                  </tr>
                </table>
			  </form>
			  </fieldset>
			  <br/>
		</div>
		
		<%-- Maybe editing => show multimedia --%>
		
		<div id="divenunciadoder" class="divenunciadoder">
			  <fieldset class="setespecial"><legend><fmt:message key="labelQuestionMM"/></legend>
				  <form method="post" enctype="multipart/form-data" name="formadjsenunciado" id="formadjsenunciado" action="${pageContext.request.contextPath}/tutor/fileupload.itest" target="form1_iframe">
					<table id="mmediaQuestionControl" width="100%">
					  <tr>
					     <td><input type="file" name="file" id="mmediaquestion"/></td>
						 <td>
						 <c:choose>
						 	<c:when test="${!empty question and !empty question.mmedia and question.size ge 3}">
						 		<input id="buttonAddFileQ" type="submit" style="display:none;" name="SubmitAdd" value="<fmt:message key="buttonAddMM"/>"/>						 		
						 	</c:when>
						 	<c:otherwise>
						 		<input id="buttonAddFileQ" type="submit" name="SubmitAdd" value="<fmt:message key="buttonAddMM"/>"/>
						 	</c:otherwise>
						 </c:choose>
						 	
						 	<c:choose>
						 		<c:when test="${!empty question}">
						 			<script>askCreated=true</script>
					 	    	</c:when>
						 	</c:choose>
						 	<img id="plusYT" title="<fmt:message key="buttonAddMMYouTube"/>" src="${pageContext.request.contextPath}/imagenes/mas.jpg" onclick="javascript:changeYTcontrol('show');" style="display:block; border:none; float: right;"/>
					 	    <img id="minusYT" src="${pageContext.request.contextPath}/imagenes/menos.jpg" onclick="javascript:changeYTcontrol('hide');" style="display:none; border:none; float: right;"/></td>
						 </td>
					  </tr>
					</table>
					  <input type="hidden" id="questionId" value="${question.id}"/>
					  <input type="hidden" name="successCallback" value="addQuestionMmedia"/>
					  <input type="hidden" name="errorCallback" value="questionUploadError"/>
					  <input type="hidden" name="allowedExtensions" value="jpg gif png bmp pcx jpeg wmf psd tiff swf wav mp3 midi mid ggb class sib sch"/>	 
				  </form>
				  <div id="youTubeQControls" style="display: none;">
					  <fieldset class="setespecial"><legend>YouTube</legend>
						  <form method="post" name="formAddQYouTube" id="formAddQYouTube" action="javascript:addQuestionMmediaYouTube()">
						    <table id="mmediaQuestionControl2" class="tablaformulario" width="100%" style="margin-top: 0px;">
						      <col width="0*"/>
					          <col width="*"/>
							  <tr>
							     <td><label><fmt:message key="labelURLMMYouTube"/></label></td><td><input type="text" width="100%" name="mmediaYTURLquestion" id="mmediaYTURLquestion"/></td>
							  </tr>
							  <tr>
							     <td><label><fmt:message key="labelTitleMMYouTube"/></label></td><td><input type="text" name="mmediaYTTitlequestion" id="mmediaYTTitlequestion"/></td>
							  </tr>
							  <c:choose>
							  	<c:when test="${question.size gt 3}">
							  	<tr>	
								 <td colspan="2" style="text-align:center;"><input id="buttonAddYouTubeQ" type="submit" name="SubmitAddYT" value="<fmt:message key="buttonAddMMYouTube"/>" style="display:none"/></td>
							 	 </tr>
							  </c:when>
							  <c:otherwise>
							  	<tr>	
								 <td colspan="2" style="text-align:center;"><input id="buttonAddYouTubeQ" type="submit" name="SubmitAddYT" value="<fmt:message key="buttonAddMMYouTube"/>"/></td>
							 	 </tr>
							  </c:otherwise>
							  </c:choose>
							</table>
						  </form>
					  </fieldset>
				  </div>
				<table id="mmediaqtable" class="tabladatos">
				   <col width="0*"/>
				   <col width="*"/>
				   <col width="0*"/>
				   <col width="0*"/>
				   <col width="0*"/>
				   <col width="0*"/>	
				   <tbody id="mmediaqtbody">
					<%-- Maybe editing: have multimedia associated --%>
					<c:forEach items="${question.mmedia}" var="qmm">
						<tr>
							<td>${qmm.order}.&nbsp;</td>
							<%-- Link for YouTube Videos--%>
							<c:choose>
							    <c:when test="${qmm.type eq 7}">
							        <td><a target="_new" href="http://www.youtube.com/watch?v=${qmm.path}">${qmm.name}</a></td>
							    </c:when>
							    <c:otherwise>
									<td>${qmm.name}</td>
								</c:otherwise>
							</c:choose>
							<td><a href="javascript:changeOrderQuestionMmedia(${qmm.order},${qmm.order-1});"> <img src="${pageContext.request.contextPath}/imagenes/up.gif" alt="<fmt:message key="labelUpOrderFile"/>" title="<fmt:message key="labelUpOrderFile"/>" border="none"> </a></td>
							<td><a href="javascript:changeOrderQuestionMmedia(${qmm.order},${qmm.order+1});"> <img src="${pageContext.request.contextPath}/imagenes/down.gif" alt="<fmt:message key="labelDownOrderFile"/>" title="<fmt:message key="labelDownOrderFile"/>" border="none"> </a></td>
							<td><a href="javascript:showQMmediaConfig(${qmm.id})"><img src="${pageContext.request.contextPath}/imagenes/editar.gif" alt="<fmt:message key="labelEditMmediaSize"/>" title="<fmt:message key="labelEditMmediaSize"/>" border="none"></a></td>
							<td><a href="javascript:deleteQuestionMmedia('${qmm.id}')"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="labelDeleteFile"/>" title="<fmt:message key="labelDeleteFile"/>" border="none"></a></td>
						</tr>
					</c:forEach>
				   </tbody>
				</table>
			  	<iframe name="form1_iframe" id="form1_iframe" src="blank.html" style="border: 0pt none ; padding: 0pt; height: 0pt; width: 0pt; position: absolute;"></iframe>
	  			<div id="divEditQMmediaSize" style="display:none">
	  				<form name="MmediaSizeForm" action="javascript:saveQMMConfig()">
					<hr/>
					<table width="100%" class="tablaminiformulario">
						<tr>
							<td><label>
								<input type="radio" name="size" id="radioAutoQMMSize" onClick="setDisabledCustomizedQuestionMmediaSize(true)"/>
								<fmt:message key="labelOriginalSize"/>
							</label></td>
				  			<td align="right"><label>
				  				<input type="radio" name="size" onClick="setDisabledCustomizedQuestionMmediaSize(true)"  id="radioBigQMMSize"/>
				  				<fmt:message key="labelBigSize"/>
				  			</label></td>
				  			<td align="right"><label>
				  				<input type="radio" name="size" onClick="setDisabledCustomizedQuestionMmediaSize(true)" id="radioMediumQMMSize"/>
				  				<fmt:message key="labelMediumSize"/>
				  			</label></td>
				  			<td align="right"><label>
				  				<input type="radio" name="size" onClick="setDisabledCustomizedQuestionMmediaSize(true)"  id="radioSmallQMMSize"/>
				  				<fmt:message key="labelSmallSize"/>
				  			</label></td>
				  		</tr>
				  	</table>
				  	<br/>
				  	<table width="100%" class="tablaminiformulario">
				  		<tr>
				  			<td rowspan="2"><label>
				  				<input type="radio" name="size" onClick="setDisabledCustomizedQuestionMmediaSize(false)" id="radioCustomizedQMMSize"/>
				  				<fmt:message key="labelCustomizedSize"/>
				  			</label></td>
				  			<td><fmt:message key="labelWidth"/></td>
				  			<td align="right">
				  				<input type="radio" name="width" disabled id="radioQMMWidthAuto" onclick="setDisabledQMMWidth(true,true)"/>
				  				<fmt:message key="labelAutoSize"/>
				  			</td>
				  			<td align="right">
				  				<input type="radio" name="width" disabled id="radioQMMWidthPerc" onclick="setDisabledQMMWidth(false,true)"/>
				  				<input type="text" size="2" disabled id="inputQMMWidthPerc"/>%
				  			</td>
				  			<td align="right">
				  				<input type="radio" name="width" disabled id="radioQMMWidthPx" onclick="setDisabledQMMWidth(true,false)"/>
				  				<input type="text" size="2" disabled id="inputQMMWidthPx"/>px
				  			</td>
				  		</tr>
				  		<tr>
				  			<td><fmt:message key="labelHeight"/> </td>
				  			<td align="right"><label>
				  				<input type="radio" name="height" disabled id="radioQMMHeightAuto" onclick="setDisabledQMMHeight(true,true)"/>
				  				<fmt:message key="labelAutoSize"/>
				  			</label></td>
				  			<td align="right">
				  				<input type="radio" name="height" disabled id="radioQMMHeightPerc" onclick="setDisabledQMMHeight(false,true)"/>
				  				<input type="text" size="2" disabled id="inputQMMHeightPerc"/>%
				  			</td>
				  			<td align="right">
				  				<input type="radio" name="height" disabled id="radioQMMHeightPx" onclick="setDisabledQMMHeight(true,false)"/>
				  				<input type="text" size="2" disabled id="inputQMMHeightPx"/>px
				  			</td>
				  		</tr>
				  	</table>
				  	<table width="100%" class="tablaminiformulario">
				  		<tr>
				  			<td><input type="submit" value="<fmt:message key="buttonSave"/>"/></td>
				  			<td align="right"><input type="button" value="<fmt:message key="closeButton"/>" onclick="closeQMmediaConfig()"/></td>
				  		</tr>
				  	</table>
				  	</form>
				</div>
			  </fieldset>
			  <br/>
			</div>
			
			
			<%-- Hidden even when editing because no answer was saved --%>
			<div id="commentader" class="divenunciadoder">
			  <fieldset class="setespecial"><legend><fmt:message key="commentMM"/></legend>
			  <form method="post" enctype="multipart/form-data" name="formadjscomentario" id="formadjscomentario" action="${pageContext.request.contextPath}/tutor/fileupload.itest" target="form3_iframe">
				<table id="mmediaCommentControl" width="100%">
				  <tr>
				     <td><input type="file" name="file" id="mmediacomment"/></td>
					 <td><input id="buttonAddFileC" type="submit" name="SubmitAdd" value="<fmt:message key="buttonAddMM"/>"></td>
				  </tr>
				</table>			  
				  <input type="hidden" name="successCallback" value="saveCommentMmedia"/>
				  <input type="hidden" name="errorCallback" value="questionUploadError"/>
				  <input type="hidden" name="allowedExtensions" value="jpg gif png bmp pcx jpeg wmf psd tiff swf wav mp3 midi mid ggb class sib sch"/>	 
				<table id="mmediaCtable" class="tabladatos">
					<tbody id="mmediaCtbody">
					<%-- In edit mode there is no answer selected --%>
					<c:forEach items="${question.mmediaComment}" var="qmmc">
						<tr>
							<td>${qmmc.order}.&nbsp;</td>
							<%-- Link for YouTube Videos--%>
							<td>${qmmc.name}</td>
							<td><a href="javascript:changeOrderCommentMmedia(${qmmc.order},${qmmc.order-1});"> <img src="${pageContext.request.contextPath}/imagenes/up.gif" alt="<fmt:message key="labelUpOrderFile"/>" title="<fmt:message key="labelUpOrderFile"/>" border="none"> </a></td>
							<td><a href="javascript:changeOrderCommentMmedia(${qmmc.order},${qmmc.order+1});"> <img src="${pageContext.request.contextPath}/imagenes/down.gif" alt="<fmt:message key="labelDownOrderFile"/>" title="<fmt:message key="labelDownOrderFile"/>" border="none"> </a></td>
							<td><a href="javascript:deleteCommentMmedia('${qmmc.id}')"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="labelDeleteFile"/>" title="<fmt:message key="labelDeleteFile"/>" border="none"></a></td>
						</tr>
					</c:forEach>
					

				   </tbody>
				</table>
				<script type="text/javascript">
					comentarios = document.getElementById('mmediaCtbody').getElementsByTagName('tr').length;
					if(comentarios>=2){
						document.getElementById('buttonAddFileC').style.display ='none';
					}
				</script>
				<img id="plusYTComment" title="<fmt:message key="buttonAddMMYouTube"/>" src="${pageContext.request.contextPath}/imagenes/mas.jpg" onclick="javascript:changeYTCommentControl('show');" style="display:block; border:none; float: right;"/>
				<img id="minusYTComment" src="${pageContext.request.contextPath}/imagenes/menos.jpg" onclick="javascript:changeYTCommentControl('hide');" style="display:none; border:none; float: right;"/></td>
			  </form>
			  <div id="youTubeCControls" style="display: none;">
					  <fieldset class="setespecial"><legend>YouTube</legend>
						  <form method="post" name="formAddCYouTube" id="formAddCYouTube" action="javascript:addCommentMmediaYouTube()">
						    <table id="mmediaCommentControl2" class="tablaformulario" width="100%" style="margin-top: 0px;">
						      <col width="0*"/>
					          <col width="*"/>
							  <tr>
							     <td><label><fmt:message key="labelURLMMYouTube"/></label></td><td><input type="text" width="100%" name="mmediaYTURLcomment" id="mmediaYTURLcomment"/></td>
							  </tr>
							  <tr>
							     <td><label><fmt:message key="labelTitleMMYouTube"/></label></td><td><input type="text" name="mmediaYTTitlecomment" id="mmediaYTTitlecomment"/></td>
							  </tr>
							  <c:choose>
							  	<c:when test="${question.commentSize lt 2 or empty question}">
								  	<tr>	
										<td colspan="2" style="text-align:center;"><input id="buttonAddYouTubeC" type="submit" name="SubmitAddYT" value="<fmt:message key="buttonAddMMYouTube"/>"/></td>
									</tr>
								  </c:when>
								  <c:otherwise>
								  		<tr>	
											<td colspan="2" style="text-align:center;"><input id="buttonAddYouTubeC" type="submit" name="SubmitAddYT" value="<fmt:message key="buttonAddMMYouTube"/>" style="display:none"/></td>
										</tr>
								  </c:otherwise>
							  </c:choose>
							</table>
						  </form>
					  </fieldset>
				  </div>
			  <iframe name="form3_iframe" id="form3_iframe" src="blank.html" style="border: 0pt none ; padding: 0pt; height: 0pt; width: 0pt; position: absolute;"></iframe>
			  </fieldset>
			</div>
			
		<%-- Maybe editing => show answer form --%>
		
		<div id="divrespuestaizq" class="divrespuestaizq" disabled="disabled">
			<fieldset class="setrespuesta"><legend id="labelA"><fmt:message key="labelAnswer"/></legend>
						  <form id="answerTest" name="formrespuesta" method="post" <c:if test="${question.type eq 1}">style="display:none;"</c:if>>
							<table class="tablaformulario">
							<tr>
							  <td><fmt:message key="labelAnswer"/></td>
							  <td colspan="3">
							  	<table>
							  		<tr>
							  			<td>
							  				<c:choose>
							  					<c:when test="${empty question}">
							  						<div id="buttonAnswerDiv" style="float:left; display:none;">
							  					</c:when>
							  					<c:otherwise>
							  						<div id="buttonAnswerDiv" style="float:left;">
							  					</c:otherwise>
							  				</c:choose>
							  					<img id="buttonAnswerBold" class="buttonBBCode" src="${pageContext.request.contextPath}/imagenes/bold.gif" onclick="javascript:tagBold(document.getElementById('textrespuesta'),document.getElementById('answerResult')); answerModified('true');" title="<fmt:message key="titleTagBold"/>"/>
														<img id="buttonAnswerItalic" class="buttonBBCode" src="${pageContext.request.contextPath}/imagenes/italic.gif" onclick="javascript:tagU(document.getElementById('textrespuesta'),document.getElementById('answerResult')); answerModified('true');" title="<fmt:message key="titleTagItalic"/>"/>
														<img id="buttonAnswerUnderLine" class="buttonBBCode" src="${pageContext.request.contextPath}/imagenes/underline.gif" onclick="javascript:tagS(document.getElementById('textrespuesta'),document.getElementById('answerResult')); answerModified('true');" title="<fmt:message key="titleTagUnderline"/>"/>
														<img id="buttonAnswerHipervinculo" class="buttonBBCode" src="${pageContext.request.contextPath}/imagenes/hipervinculo.gif" onclick="javascript:addLink(document.getElementById('textrespuesta'),document.getElementById('answerResult')); answerModified('true');" title="<fmt:message key="titleTagLink"/>"/>
														<button id="buttonAnswerFontColor" class="buttonBBCode" title="<fmt:message key="titleTagColor"/>"  onclick="document.getElementById('divColorAnswer').style.display='block'; return false;"><img id="bau" src="${pageContext.request.contextPath}/imagenes/font_color.gif"/></button>
							  				</div>
							  				<div id="divColorAnswer" style="float:right; background-color:#D8D8D8; width:120px; display:none;">
						  						<button class="buttonBBCode" onclick="javascript:getColor(document.getElementById('textrespuesta'),'red');document.getElementById('divColorAnswer').style.display='none'; answerModified('true'); return false;"style="background-color:red;">&nbsp;</button>
						  						<button class="buttonBBCode" onclick="javascript:getColor(document.getElementById('textrespuesta'),'blue');document.getElementById('divColorAnswer').style.display='none'; answerModified('true'); return false;"style="background-color:blue;">&nbsp;</button>
						  						<button class="buttonBBCode" onclick="javascript:getColor(document.getElementById('textrespuesta'),'green');document.getElementById('divColorAnswer').style.display='none'; answerModified('true'); return false;"style="background-color:green;">&nbsp;</button>
						  					</div>	
							  			</td>
									</tr>
									<tr>
										<td colspan="5"><textarea id="textrespuesta" cols="80" rows="4" onkeydown="javascript:answerModified(true);" ></textarea></td>
									</tr>
								</table>
								<input type="hidden" id="answerResult"/>
							  </td>
							</tr>
							
							<tr>
							  <%-- THE VALUES ARE CONSTANTS OF THE PROJECT (YES, NO) --%>
			<!--				  <td>&nbsp;</td>-->
							  <td>
							    <select id="combosolution" name="select" onchange="javascript:answerModified(true);">
							       <option selected value="0"><fmt:message key="labelNoSol"/></option>
							       <option value="1"><fmt:message key="labelOkSol"/></option>
							    </select>
							  </td>
							  <td>
							    <input type="button" name="SubmitAdd" value="<fmt:message key="buttonSave"/>" onclick="javascript:saveAnswer(${question.id});" style="width:100px">&nbsp;
							  </td>
							  <td>
							  	<div id="divSubmitRe-Correction" class="divSubmitRe-Correction" style="display:none;">
							    <input type="button" name="SubmitRe-Correction" value="<fmt:message key="buttonRe-Correction"/>" onclick="javascript:revisionExams();">&nbsp;
							    </div>
							  </td>
							</tr>
							</table>
							</form>
					
						<div id="answerFill" <c:if test="${empty question or(!empty question && question.type eq 0)}">style="display:none;"</c:if>>
							<table style="width:100%;">
								<tr>
									<td><fmt:message key="labelAnswer"/>:</td>
									<td>
										<textarea <c:if test="${empty question}">disabled</c:if> id="textRespuestaFill" style="width:100%;" rows="2" onkeydown="javascript:answerModified(true);" ><c:if test="${!empty fillAnswer}"><c:out value="${fillAnswer.text}"/></c:if></textarea>
									</td>
								</tr>
							</table>
							<table>
								<tbody>
									<tr>
										<td><button id="buttonSaveFillAnswer" onclick="javascript:saveFillAnswer();"<c:if test="${empty question}">disabled</c:if>><fmt:message key="buttonSave"/></button></td>
										<td>
											<div id="divSubmitRe-CorrectionFill" class="divSubmitRe-Correction" style="display:none;">
										    	<input type="button" name="SubmitRe-Correction" value="<fmt:message key="buttonRe-Correction"/>" onclick="javascript:revisionExams();">&nbsp;
										    </div>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
			</fieldset>	
			<br/>
		</div>
			
			
			<%-- Hidden even when editing because no answer was saved --%>
			<div id="divrespuestader" class="divrespuestader">
			  <fieldset class="setrespuesta"><legend><fmt:message key="labelAnswerMM"/></legend>
			  <form method="post" enctype="multipart/form-data" name="formadjsrespuesta" id="formadjsrespuesta" action="${pageContext.request.contextPath}/tutor/fileupload.itest" target="form2_iframe">
				<table id="mmediaAnswerControl" width="100%">
				  <tr>
				     <td><input type="file" name="file" id="mmediaanswer"/></td>
					 <td><input id="buttonAddFileA" type="submit" name="SubmitAdd" value="<fmt:message key="buttonAddMM"/>"></td>
				  </tr>
				</table>			  
				  <input type="hidden" name="successCallback" value="saveAnswerMmedia"/>
				  <input type="hidden" name="errorCallback" value="questionUploadError"/>
				  <input type="hidden" name="allowedExtensions" value="jpg gif png bmp pcx jpeg wmf psd tiff swf wav mp3 midi mid class sib sch"/>	 
				<div id="divmmediaAtable">
				<table id="mmediaAtable" class="tabladatos">
					<tbody id="mmediaAtbody">
					<%-- In edit mode there is no answer selected --%>
				   </tbody>
				</table>
				</div>
			  </form>
			  <iframe name="form2_iframe" id="form2_iframe" src="blank.html" style="border: 0pt none ; padding: 0pt; height: 0pt; width: 0pt; position: absolute;"></iframe>
			  </fieldset>
			</div>
			<script type='text/javascript'>disableAnswerMM()</script>
		<%-- Maybe editing => show answers list --%>
		<c:choose>
			<c:when test="${empty question}"><script type='text/javascript'>disableAnswerDiv()</script></c:when>
			<c:when test="${!empty question and question.type eq 1}"><script type='text/javascript'>document.getElementById('divrespuestader').style.display ="none";</script></c:when>
		</c:choose>
		
									<c:if test="${!empty fillAnswer}"><script>document.getElementById('divrespuestader').style.display ="none";</script></c:if>
		
		
		<div id="divanswers" class="divcentro" <c:if test="${question.type eq 1}">style="display:none;"</c:if>>
			  <table id="answerstable" class="tabladatos">
			     <col width="0*"/>
			     <col width="*"/>
			     <col width="0*"/>
			     <col width="0*"/>			  
			     <col width="0*"/>
			     <col width="0*"/>
			     <tbody id="answerstabletbody">
					<%-- Maybe editing: have multimedia associated --%>
						<tr>
							<th>&nbsp;</th>
							<th><fmt:message key="labelAnswerList"/></th>
							<th>&nbsp;</th>
							<th>&nbsp;</th>
							<th>&nbsp;</th>
							<th>&nbsp;</th>
						</tr>
					<c:choose>
				    	<c:when test="${empty question}">
				    	<tr>
							<td align="center" colspan="5">
								<fmt:message key="noAvailableAnswers"/>
							</td>
						</tr>
						</c:when>
				    </c:choose>
						
						<c:forEach items="${question.answers}" var="answer" varStatus="status">
							<tr>
								<td>${status.index + 1}.&nbsp;</td>
								<td id="answer${answer.id}">
									<c:choose>
					   					<c:when test="${fn:length(answer.text) gt 100}">
					   					    <c:out value="${fn:substring(answer.text,0,99)}"/>...
					   					</c:when>
					   					<c:otherwise>
					   					    <c:out value="${answer.text}"/>
					   					</c:otherwise>
									</c:choose>
								</td>
								<script>
									parse2HTML(document.getElementById('answer'+${answer.id}).innerHTML,document.getElementById('answer'+${answer.id}))
								</script>
								<c:choose>
								   <c:when test="${answer.solution eq 1}"><td><img name="labelOkSol" src="${pageContext.request.contextPath}/imagenes/simok.gif" alt="<fmt:message key="labelOkSol"/>" border="none"></td></c:when>
								   <c:otherwise><td>&nbsp;</td></c:otherwise>
								</c:choose>
								<c:choose>
								   <c:when test="${fn:length(answer.mmedia) gt 0}"><td><img src="${pageContext.request.contextPath}/imagenes/clip.gif" alt="<fmt:message key="labelFiles"/>" border="none"></td></c:when>
								   <c:otherwise><td>&nbsp;</td></c:otherwise>
								</c:choose>
								<td><a href="javascript:editAnswer('${answer.id}');javascript:editAnswerTable('tableAnswersRow${status.index+1}','${status.index+1}');"><img src="${pageContext.request.contextPath}/imagenes/editar.gif" alt="<fmt:message key="labelEditAnswer"/>" border="none"></a></td>
								<td>
								<c:choose>
								   <c:when test="${!answer.usedInExam}">
										<a href="javascript:deleteAnswer('${answer.id}','${question.id}');"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="labelDeleteAnswer"/>" border="none"></a>
								   </c:when>
								   <c:otherwise>
								   		<img src="${pageContext.request.contextPath}/imagenes/forb_dot.gif" alt="<fmt:message key="cannotDeleteQuestion"/>" border="none">
								   </c:otherwise>
								</c:choose>
								</td>
							</tr>
							<script>
								answerSaved = true;
							</script>
						</c:forEach>	
			     </tbody>
			  </table>
		    </div>
		    
			<div id="footer">
			  <br/>
			</div>
		    
		</div>
		<div id="divMoreInfo" class="floatingDiv" style="display:none;">
			<div class="floatingDivBody" style="overflow-y: scroll">
			<div align="right" style="position: fixed; background-color:white"><a href="" align="right" onclick="$('#divMoreInfo').hide('slow',function(){}); return false;"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif"></a></div>
				<div id="divGeogebra">
					
				</div>
				<br/>
				<div id="divAction" style="background-color:#FFECD9"></div>
				<br/>
				<div id="divGeogebraOptions" align="left">
					<center><h3><fmt:message key="labelAdvancedOptions"/></h3> <fmt:message key="labelYes"/><input id="avancedOptionYes" type="radio" name="optionsAvanced" onclick="javascript:activeGeogebraDiv(true);"/> <fmt:message key="labelNo"/><input id="avancedOptionNo" type="radio" name="optionsAvanced" onclick="javascript:activeGeogebraDiv(false);"/></center>
				</div>
			</div>
		</div>
	</body>
</html>