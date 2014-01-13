<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>
<%@ page import="com.cesfelipesegundo.itis.model.Group" %>

<% 
	Group group = (Group)request.getAttribute("group");
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addStep(group.getCourse().getName()+" ("+group.getName()+")",request.getContextPath()+"/tutor/managegroup.itest?method=indexGroup");
	//breadCrumb.addBundleStep("tasksMain",request.getContextPath()+"/tutor/managegroup.itest?method=indexGroup");
	breadCrumb.addBundleStep("questionList","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="tutor"/>
	<jsp:param name="menu" value="tutor"/>
	<jsp:param name="mathml" value="mathml"/>
</jsp:include>

	<!-- Ajax for question list -->
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/QuestionListMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>

    <script type="text/javascript">
    
    	$(document).ready(function(){
    		$('#limitCheckBox').attr('checked', true);
    		runFilterAndSearch('',true);
        });

    	function orderQuestionList(order){
    		iTestLockPage('');
    		QuestionListMgmt.orderQuestionList(order,reverse,{callback:updateQuestionList,
	        	 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
    		reverse = !reverse;
        }
		    
		// Updates the list of questions after deleting or sorting questions:
		function updateQuestionList(questions) {
		  /*
		     This is a callback function that updates the information about the answers already saved (answerstable)
		     with the answer recently just saved.
		  */
  		  var rowelement, tbodyelement, question, cellelement, formelement, inputelement;

		   tbodyelement=document.createElement('tbody');
		   tbodyelement.setAttribute("id","questionstabletbody");

		   // Fills the table (DOM scripting): answers data ---------------
		   var position = 0;
		   
		   while (position < questions.length) {
		        question = questions[position];
		        
				rowelement = document.createElement('tr');
				rowelement.id="row"+question.id;
				// ID
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				cellelement.innerHTML = question.id;
				rowelement.appendChild(cellelement);

				// Checkbox
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				checkboxelement = document.createElement('input');
				checkboxelement.setAttribute("type","checkbox");
				checkboxelement.setAttribute("id","check"+question.id);
				checkboxelement.setAttribute("name","questionCheckbox");
				checkboxelement.setAttribute("value",question.id);
				cellelement.appendChild(checkboxelement);
				rowelement.appendChild(cellelement);

				// Text of the question
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","justify");
				if (question.title != null && question.title.length > 0) {
					cellelement.innerHTML = question.title;
				}
				else {
					if ((typeof question.text != 'undefined' ) && question.text!= null && question.text.length > 60) {
						var divExterno = document.createElement('div');
						var divTexto = document.createElement('div');
						var divImagen = document.createElement('div');
						divTexto.style.cssFloat='left';
						divImagen.style.cssFloat='right';
							var divTextoCorto= document.createElement('div');
							divTextoCorto.innerHTML=question.text.substring(0,59)+"...";
							divTextoCorto.setAttribute("id","div2"+question.id);
							var divTextoLargo = document.createElement('div');
							divTextoLargo.innerHTML = question.text;
							divTextoLargo.setAttribute("id","div"+question.id);
							divTextoLargo.style.display="none";
							divTextoLargo.style.width="400px"
						divTexto.appendChild(divTextoCorto);
						divTexto.appendChild(divTextoLargo);
							var img = document.createElement('img');
							img.setAttribute("id","plus"+question.id);
							img.setAttribute("src","${pageContext.request.contextPath}/imagenes/mas.jpg");
							img.onclick = function(){enableDivInfo(question.id)};
							img.setAttribute("onclick","enableDivInfo("+question.id+")");
							img.style.border="medium none";
						divImagen.appendChild(img);
							img = document.createElement('img');
							img.setAttribute("id","minus"+question.id);
							img.setAttribute("src","${pageContext.request.contextPath}/imagenes/menos.jpg");
							img.onclick = function(){disableDivInfo(question.id)};
							img.setAttribute("onclick","disableDivInfo("+question.id+")");
							img.style.border="medium none";
							img.style.display="none";
						divImagen.appendChild(img);
						divExterno.appendChild(divTexto);
						divExterno.appendChild(divImagen);
						cellelement.appendChild(divExterno);

					} else {
						if((typeof question.text != 'undefined' ) && question.text!= null){
							if (question.text.length == 0) {
							   // Empty text -> shows the comment in italics
							   cellelement.innerHTML = "<i>"+question.comment+"</i>";
							} else {
							   cellelement.innerHTML = question.text;
							}
						}
					}
				}
				rowelement.appendChild(cellelement);
	
				// Files
				cellelement = document.createElement('td');
				if (question.mmedia != null) {
					if (question.mmedia.length > 0) {
						cellelement.innerHTML = "<img src=\"${pageContext.request.contextPath}/imagenes/clip.gif\" alt=\"<fmt:message key="labelFiles"/>\" title=\"<fmt:message key="labelFiles"/>\" border=\"none\">";
					} else {
					    cellelement.innerHTML = "&nbsp;";
					}
				} else {
				    cellelement.innerHTML = "&nbsp;";
				}
				rowelement.appendChild(cellelement);
					
				// Theme
				cellelement = document.createElement('td');
				if (question.subject.subject.length > 10) {
						var divLeft = document.createElement('div');
						divLeft.setAttribute("id","infoSubjectLeft"+question.id);
						divLeft.style.cssFloat='left';
							var sortDiv = document.createElement('div');
							sortDiv.setAttribute("id","sortSubjectDiv"+question.id);
							sortDiv.innerHTML = question.subject.order+".- "+question.subject.subject.substring(0,9)+"...";
							var largeDiv = document.createElement('div');
							largeDiv.setAttribute("id","largeSubjectDiv"+question.id);
							largeDiv.style.width='100px';
							largeDiv.innerHTML = question.subject.order+".- "+question.subject.subject;
							largeDiv.style.display='none';
						divLeft.appendChild(sortDiv);
						divLeft.appendChild(largeDiv);
					cellelement.appendChild(divLeft);
						var divRight = document.createElement('div');
						divRight.style.cssFloat='right';
							var img = document.createElement('img');
							img.setAttribute("id","subjectPlus"+question.id);
							img.setAttribute("src","${pageContext.request.contextPath}/imagenes/mas.jpg");
							img.setAttribute("alt","<fmt:message key="showDetails"/>");
							img.setAttribute("title","<fmt:message key="showDetails"/>");
							img.onclick = function(){enableDivInfo(question.id)};
							img.setAttribute("onclick","enableLargeSubjectDiv(true,"+question.id+")");
							img.style.border="medium none";
						divRight.appendChild(img);
							img = document.createElement('img');
							img.setAttribute("id","subjectMinus"+question.id);
							img.setAttribute("src","${pageContext.request.contextPath}/imagenes/menos.jpg");
							img.setAttribute("alt","<fmt:message key="hideDetails"/>");
							img.setAttribute("title","<fmt:message key="hideDetails"/>");
							img.onclick = function(){disableDivInfo(question.id)};
							img.setAttribute("onclick","enableLargeSubjectDiv(false,"+question.id+")");
							img.style.border="medium none";
							img.style.display="none";
						divRight.appendChild(img);
					cellelement.appendChild(divRight);
				} else {
					cellelement.innerHTML = question.subject.order+".- "+question.subject.subject;
				}
				rowelement.appendChild(cellelement);
									
				// Difficulty 
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				// -- Constants of the project:
				switch (question.difficulty) {
				   case 0:
				      cellelement.innerHTML = "<fmt:message key="diffLow"/>";
				      break;
				   case 1:
				      cellelement.innerHTML = "<fmt:message key="diffMedium"/>";
				      break;
				   case 2:
				      cellelement.innerHTML = "<fmt:message key="diffHigh"/>";
				      break;
				}  
				rowelement.appendChild(cellelement);
								
				// Visibility
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				// -- Constants of the project:
				switch (question.visibility) {
				   case 0:
				      cellelement.innerHTML = "<fmt:message key="scopeGroup"/>";
				      break;
				   case 1:
				      cellelement.innerHTML = "<fmt:message key="scopeCourse"/>";
				      break;
				   case 2:
				      cellelement.innerHTML = "<fmt:message key="scopePublic"/>";
				      break;
				}  
				rowelement.appendChild(cellelement);

				// Number of answers
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				cellelement.innerHTML = question.answers.length;
				rowelement.appendChild(cellelement);


				// Question type
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				if(question.type == 0){
					cellelement.innerHTML = '<fmt:message key="labelQuestionTest"/>';
				}else{
					cellelement.innerHTML = '<fmt:message key="labelQuestionFill"/>';
				}
				rowelement.appendChild(cellelement);
								
				// Active or not
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");				
				if (question.active == 1) {
					cellelement.innerHTML = "<a href=\"javascript:changeActivityQuestion('"+question.id+"',0);\"><img src=\"${pageContext.request.contextPath}/imagenes/visible.gif\" alt=\"<fmt:message key="active"/>\" title=\"<fmt:message key="active"/>\" border=\"none\"></a>";				
				} else {
					cellelement.innerHTML = "<a href=\"javascript:changeActivityQuestion('"+question.id+"',1);\"><img src=\"${pageContext.request.contextPath}/imagenes/invisible.gif\" alt=\"<fmt:message key="notActive"/>\" title=\"<fmt:message key="notActive"/>\" border=\"none\"></a>";	
				}
				rowelement.appendChild(cellelement);	
	
				// Control element: edit question
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				cellelement.innerHTML = "<a href=\"javascript:editQuestion("+question.id+","+question.usedInExam+")\"><img src=\"${pageContext.request.contextPath}/imagenes/editar.gif\" alt=\"<fmt:message key="labelEditQuestion"/>\" title=\"<fmt:message key="labelEditQuestion"/>\" border=\"none\"></a>";
				rowelement.appendChild(cellelement);
				
				// Control element: delete question
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");				
				// May be deleted if was not used in an exam:
				if (!question.usedInExam) {
					cellelement.innerHTML = "<a href=\"javascript:deleteQuestion('"+question.id+"');\"><img id=\"del"+question.id+"\" src=\"${pageContext.request.contextPath}/imagenes/borrar.gif\" alt=\"<fmt:message key="labelDeleteQuestion"/>\" title=\"<fmt:message key="labelDeleteQuestion"/>\" border=\"none\"></a>";
				} else {
					cellelement.innerHTML = "<a><img id=\"del"+question.id+"\" src=\"${pageContext.request.contextPath}/imagenes/forb_dot.gif\" alt=\"<fmt:message key="cannotDeleteQuestion"/>\" alt=\"<fmt:message key="cannotDeleteQuestion"/>\" title=\"<fmt:message key="cannotDeleteQuestion"/>\" border=\"none\"></a>";
				}
				rowelement.appendChild(cellelement);

				// Control element: preview
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				cellelement.innerHTML = "<input id=\"previewq"+question.id+"\" type=\"button\" name=\"previewq"+question.id+"\" onclick=\"window.open('${pageContext.request.contextPath}/tutor/managequestion.itest?method=questionPreview&role=${group.studentRole}&qId="+question.id+"', '_blank', 'width=600,height=400,scrollbars=YES,resizable=YES'); return false;\" value=\"<fmt:message key="previewQuestion"/>\">";
				rowelement.appendChild(cellelement);
				
				// Add row
				tbodyelement.appendChild(rowelement);

				// Important to add the form to the control operations:
				formelement = document.createElement('form');
				formelement.setAttribute("id","editq"+question.id);
				formelement.setAttribute("method","POST");
				formelement.setAttribute("action","${pageContext.request.contextPath}/tutor/managegroup.itest?method=editQuestion");
				inputelement = document.createElement('input');
				inputelement.setAttribute("name","idquestion");
				inputelement.setAttribute("type","hidden");
				inputelement.setAttribute("value",question.id);
				formelement.appendChild(inputelement);
				tbodyelement.appendChild(formelement);
				
				position++;
			} // while
			
			// No questions: present message
		    if (questions.length == 0) {
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=13;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML = "<fmt:message key="noAvailableQuestions"/>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		    } else {
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=13;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML ="<hr/>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=13;
		       cellelement.setAttribute("align","center");
		       var limited = document.getElementById('limitCheckBox').checked;
		       if(limited && questions.length>=100){
		       		cellelement.innerHTML ="<b><fmt:message key="totalLabel"/> "+questions.length+"</b>&nbsp;&nbsp;<b>(<fmt:message key="labelSearchLimited"/>)</b>";
		       }else{
		       		cellelement.innerHTML ="<b><fmt:message key="totalLabel"/> "+questions.length+"</b>";
		       }
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		    }
		
		    // Gets the datatable
		    datatable=document.getElementById("questionstable");
			// Replaces tbody			
			datatable.replaceChild(tbodyelement,document.getElementById("questionstabletbody"));
			
			// Unlock the page:
			iTestUnlockPage();
			
		} // updateQuestionList
		
		var reverse = false;
	    // Function that, given some criteria, asks for the questions that make them true
	    function runFilterAndSearch(orderby,limit) {
	         // Get the criteria of the filter:
	         var idq = document.getElementById("filterId").value;
	         if (isNaN(idq)) {
	            alert('<fmt:message key="msgQuestionListIdError"/>');
	            return;
	         }
	         var questionType = $("#selectQuestionType").val();
	         var idtheme = document.getElementById("filterTheme").value;
	         var text = document.getElementById("filterText").value;
	         var diff = document.getElementById("filterDiff").value;
	         var scope = document.getElementById("filterScope").value;
	         var active = document.getElementById("filterActive").value;
	         var textTheme = '';//Not used here
	         var idInstitution = '';//Not used here
			 var limite = limit;
			 if(limit == true){
				limite = document.getElementById('limitCheckBox').checked;
			 }
			// Lock the page:
			iTestLockPage('');		

		     // Obtains the list of questions that comply with the filter, sorted by orderby (callback updateQuestionList)
	         QuestionListMgmt.filterAndSearch(${group.id},idq,idtheme,text,textTheme,diff,scope,active,orderby,idInstitution,reverse,limite,questionType,{callback:updateQuestionList,
	        	 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
	 	} // runFilterAndSearch

		
	    // Function that changes the activity of the question
	    function changeActivityQuestion(idquestion,value) {
			// Lock the page:
			iTestLockPage();
		     // Change the activity using Ajax (callback updateQuestionList)
	         QuestionListMgmt.changeActivityQuestion(idquestion,value,updateQuestionList);
	    } // changeActivityQuestion
	    
	    // Function that changes the activity of all the selected questions
	    function changeActivitySelectedQuestions(value) {
	    	var questions = selectedQuestions();
	    	// Lock the page:
			iTestLockPage();
		    // Change the activity using Ajax (callback updateQuestionList)
	        QuestionListMgmt.changeActivityQuestions(questions,value,updateQuestionList);
	    }
	    
	    // Function that changes the visibility of all the selected questions
	    function changeVisibilitySelectedQuestions() {
	    	var questions = selectedQuestions();
	    	var value = document.getElementById("newScope").value;
	    	// Lock the page:
			iTestLockPage();
		    // Change the activity using Ajax (callback updateQuestionList)
	        QuestionListMgmt.changeVisibilityQuestions(questions,value,updateQuestionList);
	    }
	    
	    // Function that changes the difficulty of all the selected questions
	    function changeDifficultySelectedQuestions() {
	    	var questions = selectedQuestions();
	    	var forbidenQuestions = false;
	    	var value = document.getElementById("newDifficulty").value;
	    	for(var i=0;i<questions.length;i++){
				var tr = document.getElementById('row'+questions[i]);
				var tdImgEdit = tr.getElementsByTagName('td')[11];
				var a = tdImgEdit.getElementsByTagName('a')[0];
				var img = a.getElementsByTagName('img')[0];
				if(img.src.split('/')[img.src.split('/').length-1] == 'forb_dot.gif'){
					forbidenQuestions = true;
					questions[i]=-1;
				}
		    }
		    if(forbidenQuestions){
				alert("<fmt:message key="alertNotChangeQuestionUsedInExam"/>");
			}
	    	// Lock the page:
			iTestLockPage('');
		    // Change the activity using Ajax (callback updateQuestionList)
		    if(value.length!=0){
	        	QuestionListMgmt.changeDifficultyQuestions(questions,value,{callback:updateQuestionList,
		        	timeout:callBackTimeOut,
		        	errorHandler:function(message){iTestUnlockPage('error');} });
		    }else{
		    	iTestUnlockPage();
			}
	    }
	    
	    // Function that changes the subject of all the selected questions
	    function changeSubjectSelectedQuestions() {
	    	var questions = selectedQuestions();
	    	var forbidenQuestions = false;
	    	var value = document.getElementById("newSubject").value;
	    	for(var i=0;i<questions.length;i++){
				var tr = document.getElementById('row'+questions[i]);
				var tdImgEdit = tr.getElementsByTagName('td')[11];
				var a = tdImgEdit.getElementsByTagName('a')[0];
				var img = a.getElementsByTagName('img')[0];
				if(img.src.split('/')[img.src.split('/').length-1] == 'forb_dot.gif'){
					forbidenQuestions = true;
					questions[i]=-1;
				}
		    }
		    if(forbidenQuestions){
				alert("<fmt:message key="alertNotChangeQuestionUsedInExam"/>");
			}
	    	// Lock the page:
			iTestLockPage();
		    // Change the activity using Ajax (callback updateQuestionList)
		    if(value.length!=0){
		    	QuestionListMgmt.changeSubjectQuestions(questions,value,{callback:updateQuestionList,
		        	timeout:callBackTimeOut,
		        	errorHandler:function(message){iTestUnlockPage('error');} });
		    }else{
		    	iTestUnlockPage();
			}
	    }

		// Using Ajax, deletes the question
		function deleteQuestion(idquestion) {
		  
          var conf = confirm ('<fmt:message key="infoDeleteQuestion"/>'+idquestion+'\n\n<fmt:message key="confirmDeleteQuestion"/>\n\n<fmt:message key="alertDeleteQuestion"/>');
		  if (conf) {
			// Lock the page:
			iTestLockPage();
		     // Delete the question using Ajax (callback updateQuestionList)
	         QuestionListMgmt.deleteQuestion(idquestion,updateQuestionList);
	      }
		   
		} // deleteQuestion

		// Using Ajax, deletes the question
		function deleteQuestionForbiden(idquestion) {
		
          var conf = confirm ('<fmt:message key="confirmDeleteQuestion"/>\n\n<fmt:message key="alertDeleteQuestion"/>');
		  
		  if (conf) {
			  var conf = confirm ('<fmt:message key="confirmDeleteQuestion2"/>');
			  if(conf){
				// Lock the page:
				iTestLockPage();
			    // Delete the question using Ajax (callback updateQuestionList)
		        QuestionListMgmt.deleteQuestionForbiden(idquestion,showExamsModify);
				}
	      }
		   
		} // deleteQuestion

		function showExamsModify (respuesta){
			updateQuestionList(respuesta[0])
			alert('Han sido modificados '+respuesta[1]+' examenes');
		}
		// Deletes all checked questions using AJAX
		function deleteSelectedQuestions() {			
	    	var questions = selectedQuestions();
			  for(var i=0;i<questions.length;i++){
					var questionId= questions[i];
					var img = document.getElementById('del'+questionId).src;
					var imgSize = img.split('/').length;
					img = img.split('/')[imgSize-1];
					if(img=='forb_dot.gif'){
						alert('<fmt:message key="alertIndelibleQuestions"/>');
						break;
					}
			  }
	    	if (questions.length > 0) {
	    		var conf = confirm ('<fmt:message key="confirmDeleteQuestions"/>\n\n<fmt:message key="alertDeleteQuestions"/>');
	    		if (conf) {
			    	// Lock the page:
					iTestLockPage();
				    // Change the activity using Ajax (callback updateQuestionList)
			        QuestionListMgmt.deleteQuestions(questions,updateQuestionList);
			    }
			}
		}
		
		// Check all unchecked checkboxes and unchecks all checked ones
		function switchAllCheckboxes() { 
			var mytable = document.getElementById("questionstable");
			if(!mytable)
				return;
			var inputs = mytable.getElementsByTagName("input");
			if(!inputs)
				return;
			// switch the check value for all check boxes
			for(var i = 0; i < inputs.length; i++)
				if (inputs[i].type == 'checkbox')
					inputs[i].checked = !inputs[i].checked;
		}
		
		// Returns an array with the IDs of the checked questions
		function selectedQuestions() {
			var resultList = new Array();
			var mytable = document.getElementById("questionstable");
			if(!mytable)
				return resultList;
			var inputs = mytable.getElementsByTagName("input");
			if(!inputs)
				return resultList;
			// switch the check value for all check boxes
			for(var i = 0; i < inputs.length; i++)
				if (inputs[i].type == 'checkbox' && inputs[i].checked)
					resultList.push(new String(inputs[i].value));
					
			return resultList;
		}
		
		// Shows the div with extra options
		function showMoreOptions() {
			if (document.getElementById("extra").style.display=="none") {
				document.getElementById("extra").style.display="block";
			    document.getElementById("plus").style.display = 'none';
			    document.getElementById("minus").style.display = 'block';
			} else {
				document.getElementById("extra").style.display="none";
			    document.getElementById("plus").style.display = 'block';
			    document.getElementById("minus").style.display = 'none';
			}
		}

		function enableDivInfo(questionId){
			document.getElementById('div'+questionId).style.display="";
			document.getElementById('div2'+questionId).style.display="none";
			document.getElementById('minus'+questionId).style.display="";
			document.getElementById('plus'+questionId).style.display="none";
		}
		function disableDivInfo(questionId){
			document.getElementById('div'+questionId).style.display="none";
			document.getElementById('div2'+questionId).style.display="";
			document.getElementById('minus'+questionId).style.display="none";
			document.getElementById('plus'+questionId).style.display="";
		}
		function enableLargeSubjectDiv(enable,idQuestion){
			if(enable){
				document.getElementById('largeSubjectDiv'+idQuestion).style.display='';
				document.getElementById('sortSubjectDiv'+idQuestion).style.display='none';
				document.getElementById('subjectMinus'+idQuestion).style.display='';
				document.getElementById('subjectPlus'+idQuestion).style.display='none';
			}else{
				document.getElementById('largeSubjectDiv'+idQuestion).style.display='none';
				document.getElementById('sortSubjectDiv'+idQuestion).style.display='';
				document.getElementById('subjectMinus'+idQuestion).style.display='none';
				document.getElementById('subjectPlus'+idQuestion).style.display='';
			}
		}
		function editQuestion (idQuestion,usedInExam){
			if(usedInExam)
				showConfirmEditQuestion(idQuestion);
			else
				document.getElementById('editq'+idQuestion).submit();
		}

		function showConfirmEditQuestion(idQuestion){
			document.getElementById('selectedIdQuestion').value=idQuestion;
			document.getElementById('divConfirmEdit').style.display='';
		}

		function goEditQuestion(){
			var idQuestion = document.getElementById('selectedIdQuestion').value;
			document.getElementById('editq'+idQuestion).submit();
		}
		function copyQuestion(){
			var form = document.getElementById('formCopyUsedQuestion');
			document.getElementById('idGroup').value = ${group.id};
			form.submit();
			
		}

		function submitForm(form){
			form.submit();
		}

		function showAllQuestions(){
			document.getElementById('filterForm').reset();
			document.getElementById('limitCheckBox').checked=false;
			runFilterAndSearch('',false);
		}
		
	</script>

	<div id="contenido">
		
			<div>
				<form id="filterForm" name="formfiltro" method="post" action="javascript:runFilterAndSearch('',true);">
				  <fieldset  style="font-size:16px;">
				  <legend><fmt:message key="labelFilterTitle"/></legend>
					<table width="100%">
					  <tr>
						<td align="right"><fmt:message key="labelFilterId"/></td>
						<td align="left"><input id="filterId" name="filtroid" size="5"></input></td>
						<td align="right"><fmt:message key="labelFilterTheme"/></td>
						<td align="left">
						  <select id="filterTheme" name="filtrotema">
						  	<option selected value=""><fmt:message key="labelFilterShowAll"/></option>
							<c:if test="${!empty themes}">
								<c:forEach items="${themes}" var="theme">
									<option value="${theme.id}">
										<c:out value="${theme.sort}"/>.- <c:out value="${theme.subject}"/>
									</option>
								</c:forEach>
							</c:if>
						  </select>
						</td>
						<td align="right"><fmt:message key="labelQuestionType"/>:</td>
						<td align="left">
							<select id="selectQuestionType">
								<option value="-1">--------</option>
								<option value="0"><fmt:message key="labelQuestionTest"/></option>
								<option value="1"><fmt:message key="labelQuestionFill"/></option>
							</select>
						</td>
					  </tr>
					  
					  <tr>
						<td align="right"><fmt:message key="labelFilterText"/></td>
						<td colspan="5" align="left"><input id="filterText" name="filtroenunciado" size="100%"></input></td>
					  </tr>
					  
					  <tr>
						<td align="right"><fmt:message key="labelFilterDiff"/></td>
						<td align="left">
						   <select id="filterDiff" name="filtrodificultad">
						  		<option selected value=""><fmt:message key="labelFilterShowAll"/></option>
 								<%-- THE VALUES ARE CONSTANTS OF THE PROJECT --%>
						  		<option value="0"><fmt:message key="diffLow"/></option>
						  		<option value="1"><fmt:message key="diffMedium"/></option>
						  		<option value="2"><fmt:message key="diffHigh"/></option>
						   </select>
						</td>
						<td align="right"><fmt:message key="labelFilterScope"/></td>
						<td align="left">
						   <select id="filterScope" name="filtroscope">
						  		<option selected value=""><fmt:message key="labelFilterShowAll"/></option>
 								<%-- THE VALUES ARE CONSTANTS OF THE PROJECT --%>
						  		<option value="0"><fmt:message key="scopeGroup"/></option>
						  		<option value="2"><fmt:message key="scopePublic"/></option>
						   </select>
						</td>
						<td align="right"><fmt:message key="labelFilterActive"/></td>
						<td align="left">
						   <select id="filterActive" name="filtroactivas">
						  		<option selected value=""><fmt:message key="labelFilterShowAll"/></option>
 								<%-- THE VALUES ARE CONSTANTS OF THE PROJECT --%>
						  		<option value="1"><fmt:message key="labelYes"/></option>
						  		<option value="0"><fmt:message key="labelNo"/></option>
						  	</select>	
						</td>
					  </tr>
					  <tr>
					    <td colspan="3" align="center">
					       <input type="button" name="resetfiltrar" value="<fmt:message key="buttonFilterReset"/>" onclick="javascript:showAllQuestions();">
					    </td>	
					    <td colspan="3" align="center">
					    	<input type="reset" value="<fmt:message key="msgbuttonReset"/>" onclick="document.getElementById('questionstabletbody').innerHTML='';"/>
					        <input type="submit" name="submitfiltrar" value="<fmt:message key="buttonFilterRun"/>"/>
					    	<input type="checkBox" id="limitCheckBox" checked/><label><fmt:message key="labelLimit100"/></label>
					    </td>	
					  </tr>						  
					</table>
				  </fieldset>
				</form>
			</div>
			<form id="formAddNewQuestion" action="${pageContext.request.contextPath}/tutor/managegroup.itest?method=newQuestion" method="POST" style="display:none">
				<input type="hidden" name="idgroup" value="${group.id}">
			</form>
			<div class="botonera">
				<form>
				<fieldset>
				<table width="100%">
				<col width="15%" span="5"/>
				<col width="10%"/>
				<tr>
					<td style="text-align: left"><input type="button" value="<fmt:message key="newQuestion"/>" onclick="javascript:submitForm(document.getElementById('formAddNewQuestion'));"/></td>
					<td><input type="button" value="<fmt:message key="switchAll"/>" onclick="switchAllCheckboxes()"/></td>
					<td><input type="button" value="<fmt:message key="deleteSelectedQuestion"/>" onclick="deleteSelectedQuestions()"/></td>
					<td><input type="button" value="<fmt:message key="activeSelected"/>" onclick="changeActivitySelectedQuestions(1)"/></td>
					<td><input type="button" value="<fmt:message key="noActiveSelected"/>" onclick="changeActivitySelectedQuestions(0)"/></td>
					<td><center>
						<img id="plus" title="<fmt:message key="buttonMore"/>" src="${pageContext.request.contextPath}/imagenes/mas.jpg" onclick="javascript:showMoreOptions();" style="border:none;"/>
						<img id="minus" src="${pageContext.request.contextPath}/imagenes/menos.jpg" onclick="javascript:showMoreOptions();" style="display: none; border:none;"/>
						</center>
					</td>
				</tr>
				</table>
				<div id="extra" style="display:none">
				<table width="100%">
				<tr>
					<td style="text-align:left"><input type="button" value="<fmt:message key="changeSubject"/>" onclick="changeSubjectSelectedQuestions()"/>
					   <select id="newSubject" name="newSubject">
							<c:if test="${!empty themes}">
								<c:forEach items="${themes}" var="theme">
									<option value="${theme.id}">
										<c:out value="${theme.sort}"/>.- <c:out value="${theme.subject}"/>
									</option>
								</c:forEach>
							</c:if>
					   </select>
					</td>
					<td><input type="button" value="<fmt:message key="changeVisibility"/>" onclick="changeVisibilitySelectedQuestions()"/>
					   <select id="newScope" name="newScope">
							<%-- THE VALUES ARE CONSTANTS OF THE PROJECT --%>
					  		<option value="0"><fmt:message key="scopeGroup"/></option>
					  		<option value="2"><fmt:message key="scopePublic"/></option>
					   </select>
					</td>
					<td><input type="button" value="<fmt:message key="changeDifficulty"/>" onclick="changeDifficultySelectedQuestions()"/>
					   <select id="newDifficulty" name="newDifficulty">
							<%-- THE VALUES ARE CONSTANTS OF THE PROJECT --%>
					  		<option value="0"><fmt:message key="diffLow"/></option>
					  		<option value="1"><fmt:message key="diffMedium"/></option>
					  		<option value="2"><fmt:message key="diffHigh"/></option>
					   </select>
					</td>
				</tr>
				</table>
				</div>
				</fieldset>
				</form>
			</div>
			<div>
			<table id="questionstable" class="tabladatos">
			  <tr>
				<th><center><a href="javascript:orderQuestionList('id');"><fmt:message key="headerQlistId"/></a></center></th>
				<th>&nbsp;</th>
				<th><a href="javascript:orderQuestionList('text');"><fmt:message key="headerQlistText"/></a></th>	
				<th>&nbsp;</th>
				<th><a href="javascript:orderQuestionList('subject');"><fmt:message key="headerQlistTheme"/></a></th>
				<th><center><a href="javascript:orderQuestionList('diff');"><fmt:message key="headerQlistDiff"/></a></center></th>
				<th><center><a href="javascript:orderQuestionList('scope');"><fmt:message key="headerQlistScope"/></a></center></th>
				<th><center><a href="javascript:orderQuestionList('resp');"><fmt:message key="headerQlistNumAns"/></a></center></th>
				<th width="20px;"><center><a href="javascript:orderQuestionList('type');"><fmt:message key="headerStListRole"/></a></center></th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
			  </tr>
			 <tbody id="questionstabletbody">
			
  			 <c:forEach items="${questions}" var="question">
 			  <tr id="row${question.id}">
 			  	<td align="center"><c:out value="${question.id}"/></td>
 			  	<td align="center"><input type="checkbox" id="check${question.id}" name="questionCheckbox" value="${question.id}"/></td>
   				<c:choose>
   					<c:when test="${not empty question.title}">
   						<td align="justify"><c:out value="${question.title}"/></td>
   					</c:when>
   					<c:when test="${not empty question.text and fn:length(question.text) gt 60}">
   					    <td align="justify">
   					    	<div>
   					    		<div style="float:left">
   					    			<div id="div2${question.id}">
   					    				<c:out value="${fn:substring(question.text,0,59)}"/>...
   					    			</div>
	   					    		<div id="div${question.id}" style="display:none; width:400px;">
	   					    			<c:out value="${question.text}"></c:out>
	   					    		</div>
   					    		</div>
   					    		<div style="float:right">
   					    			<img id="plus${question.id}" onclick="enableDivInfo(${question.id})" src="${pageContext.request.contextPath}/imagenes/mas.jpg" style="border:none;"/>
	 			    				<img id="minus${question.id}" onclick="disableDivInfo(${question.id})" src="${pageContext.request.contextPath}/imagenes/menos.jpg" style="border:none; display:none"/>
   					    		</div>
   					    	</div>
   					    </td>
   					</c:when>
   					<c:when test="${not empty question.text and fn:length(question.text) eq 0}">
   					    <td align="justify"><i><c:out value="${question.comment}"/></i></td>
   					</c:when>
   					<c:otherwise>
   					    <td align="justify"><c:out value="${question.text}"/></td>
   					</c:otherwise>
				</c:choose>

				<%-- Multimedia Files associated --%>
				<td align="center">
				   <c:choose>
					  <c:when test="${!empty question.mmedia}">
					     <img src="${pageContext.request.contextPath}/imagenes/clip.gif" alt="<fmt:message key="labelFiles"/>" border="none">
					  </c:when>
					  <c:otherwise>
					     &nbsp;
					  </c:otherwise>
					</c:choose>
				</td>

 			    <td>
 			    	<c:choose>
 			    		<c:when test="${fn:length(question.subject.subject) gt 10}">
 			    			<div id="infoSubjectLeft${question.id}" style="float:left">
 			    				<div id="sortSubjectDiv${question.id}">
 			    					<c:out value="${question.subject.order}"/>.- <c:out value="${fn:substring(question.subject.subject,0,9)}"/>
 			    					<c:if test="${fn:length(question.subject.subject) gt 10}">...</c:if>
 			    				</div>
 			    				<div id="largeSubjectDiv${question.id}" style="display:none; width:100px">
 			    					<c:out value="${question.subject.order}"/>.- <c:out value="${question.subject.subject}"/>
 			    				</div>
 			    			</div>
 			    			<div style="float:right">
 			    				<img id="subjectPlus${question.id}" onclick="javascript:enableLargeSubjectDiv(true,${question.id})" src="${pageContext.request.contextPath}/imagenes/mas.jpg" style="border:none;"/>
	 			    			<img id="subjectMinus${question.id}" onclick="javascript:enableLargeSubjectDiv(false,${question.id})" src="${pageContext.request.contextPath}/imagenes/menos.jpg" style="border:none; display:none"/>
 			    			</div>
 			    		</c:when>
 			    		<c:otherwise>
 			    			<c:out value="${question.subject.order}"/>.- <c:out value="${fn:substring(question.subject.subject,0,9)}"/>
 			    		</c:otherwise>
 			    	</c:choose>
 			    	
 			    </td>
   				
   				<td align="center">
					<%-- THE VALUES ARE CONSTANTS OF THE PROJECT --%>
   					<c:choose>
						<c:when test="${question.difficulty eq 0}">
							<fmt:message key="diffLow"/>
						</c:when>
						<c:when test="${question.difficulty eq 1}">
							<fmt:message key="diffMedium"/>
						</c:when>
						<c:when test="${question.difficulty eq 2}">
							<fmt:message key="diffHigh"/>
						</c:when>
					</c:choose>
   				</td>

   				<td align="center">
					<%-- THE VALUES ARE CONSTANTS OF THE PROJECT --%>
   					<c:choose>
						<c:when test="${question.visibility eq 0}">
							<fmt:message key="scopeGroup"/>
						</c:when>
						<c:when test="${question.visibility eq 1}">
							<fmt:message key="scopeCourse"/>
						</c:when>
						<c:when test="${question.visibility eq 2}">
							<fmt:message key="scopePublic"/>
						</c:when>
					</c:choose>
   				</td>

 			    <td align="center">
 			    	<c:out value="${fn:length(question.answers)}"/>
 			    </td>
 			    
 			    <td align="center">
   					<c:choose>
   						<c:when test="${question.type eq 0}">
	   						<fmt:message key="labelQuestionTest"/>
	   					</c:when>
	   					<c:otherwise>
	   						<fmt:message key="labelQuestionFill"/>
	   					</c:otherwise>
   					</c:choose>
   				</td>

				<td align="center">
				   <c:choose>
					  <c:when test="${question.active eq 1}">
						 <a href="javascript:changeActivityQuestion('${question.id}',0);"><img src="${pageContext.request.contextPath}/imagenes/visible.gif" alt="<fmt:message key="active"/>" border="none"></a>
					  </c:when>
					  <c:otherwise>
						 <a href="javascript:changeActivityQuestion('${question.id}',1);"><img src="${pageContext.request.contextPath}/imagenes/invisible.gif" alt="<fmt:message key="notActive"/>" border="none"></a>
					  </c:otherwise>
				   </c:choose>
						
				</td>
				
				<td align="center">
				   <a href="javascript:editQuestion(${question.id},${question.usedInExam})"><img src="${pageContext.request.contextPath}/imagenes/editar.gif" alt="<fmt:message key="labelEditQuestion"/>" border="none"></a>
				</td>
				<td align="center">
				<c:choose>
					<c:when test="${!question.usedInExam}">
						<a href="javascript:deleteQuestion('${question.id}');"><img id="del${question.id}" src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="labelDeleteQuestion"/>" border="none"></a>
					</c:when>
					<c:otherwise>
						<a><img id="del${question.id}" src="${pageContext.request.contextPath}/imagenes/forb_dot.gif" alt="<fmt:message key="cannotDeleteQuestion"/>" title="<fmt:message key="cannotDeleteQuestion"/>" border="none"></a>
					</c:otherwise>
				</c:choose>
				</td>
				<td align="center">
				    <input id="previewq${question.id}" type="button" name="previewq${question.id}" onclick="window.open('${pageContext.request.contextPath}/tutor/managequestion.itest?method=questionPreview&role=${group.studentRole}&qId=${question.id}', '_blank', 'width=600,height=400,scrollbars=YES,resizable=YES'); return false;" value="<fmt:message key="previewQuestion"/>">
				</td>
			  </tr>
 			 
 			  <form id="editq${question.id}" method="POST" action="${pageContext.request.contextPath}/tutor/managegroup.itest?method=editQuestion"><input type="hidden" name="idquestion" value="${question.id}"/></form>
			  
			 </c:forEach>
			 
			 <c:choose>
				 <c:when test="${empty questions}">
				  <tr>
				    <td align="center" colspan="13"><fmt:message key="noAvailableQuestions"/></td>
				  </tr>
				 </c:when>
				 <c:otherwise>
				  <tr>
				    <td align="center" colspan="13"><hr/></td>
				  </tr>
				  <tr>
				    <td align="center" colspan="13"><b><fmt:message key="totalLabel"/> ${fn:length(questions)} </b></td>
				  </tr>				 
				 </c:otherwise>
			 </c:choose>
			</tbody>
			</table>
			<br/>
			</div>
		</div>
	</body>
	<c:if test="${!empty error}">
		<script>
			alert('error');
		</script>
	</c:if>
	<div id="divConfirmEdit" class="floatingDiv" style="display:none;">
		<div class="floatingDivBody" style="height:200px">
			<div style="background-color:#B40404;">
				<h1><font color="white"><fmt:message key="labelWarning"/></font> <img src="${pageContext.request.contextPath}/imagenes/warning.gif" border="none" height="40px" width="40px"/></h1>
			</div>
			<div style="height:40%">
				<fmt:message key="msgEditQuestionUsedInExam"/>
			</div>
			<div id="divConfirmEditContent" style="height:30%">
				<form id="formCopyUsedQuestion" method="POST" action="${pageContext.request.contextPath}/tutor/managequestion.itest?method=copyQuestionUsedInExam">
					<input type="hidden" id="selectedIdQuestion" name="selectedIdQuestion"/>
					<input type="hidden" id="idGroup" name="idGroup"/>
				</form>
				<input type="button" value="<fmt:message key="labelEditQuestion"/>" onclick="javascript:goEditQuestion();"/>
				<input type="button" value="<fmt:message key="buttonCopyQuestion"/>" onclick="javascript:copyQuestion();"/>
				<input type="button" value="<fmt:message key="buttonCancel"/>" onclick="document.getElementById('divConfirmEdit').style.display = 'none';"/>
			</div>
		</div>
	</div>
</html>