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
	breadCrumb.addBundleStep("questionImports","");
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
		    
		// Id of the destination theme on importing
		var importingQuestionsTo = -1;
		var preImportedQuestions = 0;

		function reverseSelection(){
			a = document.getElementsByName('checkboxList');
			for(var i=0;i<a.length;i++){
				a[i].checked = !a[i].checked;
				preImportQuestion(a[i].id,a[i].checked);	
			}
		}

		function selectAll(){
			a = document.getElementsByName('checkboxList');
			for(var i=0;i<a.length;i++){
				a[i].checked = document.getElementById('selectAll').checked;
				preImportQuestion(a[i].id,a[i].checked);	
			}
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
				
				// ID
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				cellelement.innerHTML = question.id;
				rowelement.appendChild(cellelement);
				
				// Text of the question
				cellelement = document.createElement('td');
				if (question.title != null && question.title.length > 0) {
					cellelement.innerHTML = question.title;
				}
				else if ((typeof question.text != 'undefined' ) && question.text.length > 60) {
					cellelement.innerHTML = question.text.substring(0,59)+"...";
				} else {
					if ((typeof question.text != 'undefined' ) &&question.text.length == 0) {
					   // Empty text -> shows the comment in italics
					   cellelement.innerHTML = "<i>"+question.comment+"</i>";
					} else {
					   cellelement.innerHTML = question.text;
					}
				}
				rowelement.appendChild(cellelement);
	
				// Files
				cellelement = document.createElement('td');
				if (question.mmedia != null) {
					if (question.mmedia.length > 0) {
						cellelement.innerHTML = "<img src=\"${pageContext.request.contextPath}/imagenes/clip.gif\" alt=\"<fmt:message key="labelFiles"/>\" border=\"none\">";
					} else {
					    cellelement.innerHTML = "&nbsp;";
					}
				} else {
				    cellelement.innerHTML = "&nbsp;";
				}
				rowelement.appendChild(cellelement);

				// Course
				cellelement = document.createElement('td');
				if (question.group.course.name.length > 15) {
					cellelement.innerHTML = question.group.course.name.substring(0,14)+"... "+"("+question.group.name+")";
				} else {
					   cellelement.innerHTML = question.group.course.name+" ("+question.group.name+")";
				}
				rowelement.appendChild(cellelement);
					
				// Theme
				cellelement = document.createElement('td');
				cellelement.innerHTML = question.subject.order+".- "
				if (question.subject.subject.length > 10) {
					cellelement.innerHTML += question.subject.subject.substring(0,9)+"...";
				} else {
					cellelement.innerHTML += question.subject.subject;
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
								
				// Control element: preview
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				cellelement.innerHTML = "<input type=\"button\" onclick=\"javascript:previewQuestion('"+question.id+"');\" value=\"<fmt:message key="previewQuestion"/>\" />";
				rowelement.appendChild(cellelement);
				
				// Control element: pre-import
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");				
				cellelement.innerHTML = "<input id=\""+question.id+"\" name=\"checkboxList\" type=\"checkbox\" onclick=\"javascript:preImportQuestion('"+question.id+"',getElementById('"+question.id+"').checked);\" />";
				rowelement.appendChild(cellelement);
				
				// Add row
				tbodyelement.appendChild(rowelement);
			
				position++;
			} // while
			
			// No questions: present message
		    
			if (questions.length == 0) {
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=9;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML = "<fmt:message key="noAvailableQuestions"/>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		    } else {
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=9;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML ="<hr/>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=9;
		       cellelement.setAttribute("align","center");
		       var limited = document.getElementById('limitCheckBox').checked;
		       if(limited && questions.length>=100){
		       		cellelement.innerHTML ="<b><fmt:message key="totalLabel"/> "+questions.length+"</b>&nbsp;&nbsp;<b>(<fmt:message key="labelSearchLimited"/>)</b>";
		       }else{
		       		cellelement.innerHTML ="<b><fmt:message key="totalLabel"/> "+questions.length+"</b>";
		       }
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
			   if(questions.length == 200 && buscar){
					alert("<fmt:message key="msgLimitQuestion"/>");
					buscar = false;
				}
		    }
			
		
		    // Gets the datatable
		    datatable=document.getElementById("questionstable");
			// Replaces tbody			
			datatable.replaceChild(tbodyelement,document.getElementById("questionstabletbody"));

			 // Hides the div to avoid double-click
			 iTestUnlockPage();
			 
			 // If there was an import and no errors, it was OK
			 if (importingQuestionsTo != -1) {
			 	alert("<fmt:message key="msgImportQuestionsOK"/>\n\n<fmt:message key="msgImportQuestionsTo"/> "+document.getElementById('destinationTheme_'+importingQuestionsTo).text);
			 	importingQuestionsTo = -1;
			 }
			 
			 // No preimported questions
			 preImportedQuestions = 0;
			 reverse = !reverse;
		} // updateQuestionList
		
		
		// Function to enable the screen
		function updatePreImportedQuestionList(qlist) {
			 		
			 // Hides the div to avoid double-click
			 iTestUnlockPage();

		} // updatePreImportedQuestionList		


	    // Function that, given some criteria, asks for the questions that make them true
	    function runFilterAndSearch(orderby) {
	         // Get the criteria of the filter:
	         var idq = document.getElementById("filterId").value;
	         if (isNaN(idq)) {
	            alert("<fmt:message key="msgQuestionListIdError"/>");
	            return;
	         }
	         var idtheme = '';  // Not used here
	         var selectThemes = document.getElementById('selectThemes');
	         if(selectThemes!=null && selectThemes.value!=0){
				idtheme = selectThemes.value;
		     }
	         var text = document.getElementById("filterText").value;
	         var textTheme = document.getElementById("filterTextTheme").value;
	         var diff = document.getElementById("filterDiff").value;
	         var active = '';  // Not used here
	         var idgroup = document.getElementById("filterGroup").value;
	         var idInstitution = document.getElementById("filterInstitution").value;
	     	 var idGroupOfSpecifiedInstitution = document.getElementById("filterSpecifiedGroup").value;
	     	 var limit = document.getElementById('limitCheckBox').checked;
	         if (idgroup == -1) {
	        	
		         if (idInstitution == ""){
	            	// Groups not teached: means any group but all public
     	    		idgroup = '';
        	    	idInstitution = '';
		         }
		         else{//Si ha escogido algun centro
					//reutilizo el idgroup aunque ahora será un grupo perteneciente
					// al centro especificado
					if (idGroupOfSpecifiedInstitution == ""){//Si no ha escogido ningun grupo
						idgroup = '';
					}
					else{//si ha escogido algun grupo
						idgroup = idGroupOfSpecifiedInstitution;
					}
					
					
					
		         }
		         
        	 } else {
        	 	
        	 	// If the group is teached, the institution and groupOfInstution must be deleted
        	 	// The groupOfInstituion is not deleted because it is not considered
        	 	idInstitution = '';
        	 	
        	 }
        	 
      	     var scope = document.getElementById("filterScope").value;

      	     if(orderby==''){
				buscar = true;
          	 }
			 // Div to avoid double-click
    		 iTestLockPage('');        
    		 var questionType = -1;
		     // Obtains the list of questions that comply with the filter, sorted by orderby (callback updateQuestionList)
	         QuestionListMgmt.filterAndSearch(idgroup,idq,idtheme,text,textTheme,diff,scope,active,orderby,idInstitution,reverse,limit,questionType,{callback:updateQuestionList,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
	    } // runFilterAndSearch
		var reverse = false;
  
		// Using Ajax, pre-imports a question or de-pre-imports it, depends on the "value"
		function preImportQuestion (idquestion,value) {	
			// Update counters:
			if (value)
				preImportedQuestions++;
			else
				preImportedQuestions--;	
			// Div to avoid double-click
    		iTestLockPage();
    		// Callback: updatePreImportedQuestionList
		    QuestionListMgmt.preImportQuestion(idquestion,value,{callback:updatePreImportedQuestionList,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
	    } // preImportQuestion


		// Using Ajax, imports all the questions "pre-imported", that is, selected to be imported.
		function importQuestions () {
		
		   if (preImportedQuestions == 0) {
		   		alert("<fmt:message key="cannotImportNoQuestions"/>");
		   		return;
		   }
		   
		   var importTo = document.getElementById('destinationTheme').value;
           var conf = confirm ('<fmt:message key="confirmImportQuestions"/>\n\n<fmt:message key="msgImportQuestionsTo"/> '+document.getElementById('destinationTheme_'+importTo).text);
		   if (conf) {
		      // Get destination theme:
		      var idtheme = document.getElementById('destinationTheme').value;
		      if (idtheme == '') {
		         alert("<fmt:message key="cannotImportNoTheme"/>");
		         return;
		      }
			  // Div to avoid double-click
    		  iTestLockPage();
    		  
    		  // Importing ...
    		  importingQuestionsTo = importTo;
    		  
    		  // Callback: updateQuestionList
			  QuestionListMgmt.importQuestions(${group.id},idtheme,{callback:updateQuestionList,
					 timeout:callBackTimeOut,
					 errorHandler:function(message) { iTestUnlockPage('error');} });
	       }
	    } // importQuestions



		// Function to preview a question in a different window
		function previewQuestion (idquestion) {
			window.open('${pageContext.request.contextPath}/tutor/managequestion.itest?method=questionPreview&qId='+idquestion, '_blank', 'width=600,height=400,scrollbars=YES,resizable=YES');
		} // previewQuestion

		function fillInstitutions(centros){
			position=0;
			var lista=document.getElementById("filterInstitution");
			n=document.getElementById("filterInstitution").length;
			while (position<n-1){
				lista.remove(1);
			position++;
			}
			position=1;
			posAux=0;
			while (posAux<centros.length){
				var opcion=document.createElement('option');
				centro=centros[posAux];
				opcion.text=centro.name;
				opcion.value=centro.id;
				lista[position]=opcion;
				posAux++;
				position++;
			}
			document.getElementById("divAdvanced1").style.display="";
			document.getElementById("divAdvanced2").style.display="";
			document.getElementById("divAdvanced3").style.display="";
			document.getElementById("divAdvanced4").style.display="";
		}


		function fillVisibilidad(listaVisibilidad){
			//Relleno con las 3 visibilidades posibles
			var opcion=document.createElement('option');
				opcion.text='<fmt:message key="labelFilterShowAll"/>';
				opcion.value="";
				listaVisibilidad[0]=opcion;
				var opcion=document.createElement('option');
				opcion.text='<fmt:message key="scopeGroup"/>';
				opcion.value="0";
				listaVisibilidad[1]=opcion;
				var opcion=document.createElement('option');
				opcion.text='<fmt:message key="scopePublic"/>';
				opcion.value="2";
				listaVisibilidad[2]=opcion;		
		}

		
		function getInstitutions(state){
			document.getElementById('themesColumn').style.display="none";


			
			if (state==true){
				// obtener los temas de la asignatura
				if(document.getElementById('filterGroup').value=="-2"){
					//Ocultamos los select que no corresponden
					document.getElementById("divAdvanced1").style.display="none";
					document.getElementById("divAdvanced2").style.display="none";
					document.getElementById("divAdvanced3").style.display="none";
					document.getElementById("divAdvanced4").style.display="none";
					return;
				}
				if (document.getElementById('filterGroup').value=="-1"){
					//llamada por ajax para que nos de centros
					QuestionListMgmt.getInstitutionsWidthPublicQuestions(fillInstitutions);
					
					pos=0;
					var lista=document.getElementById("filterScope");
					n=document.getElementById("filterScope").length;
					//Esto hara que cuando se seleccione Publicas:Solo grupos no impartidos
					//se rellene el select de visibilidad solo con visibilidad Publica.
					while (pos<n-1){
							lista.remove(0);
							pos++;
						}
					
					}
				else{
					position=0;
					var lista=document.getElementById("filterSpecifiedGroup");
					n=document.getElementById("filterSpecifiedGroup").length;
					while (position<n-1){
						lista.remove(1);
					position++;
					}

					//Al pulsar sobre una opcion distinta a Publicas:Solo grupos no impartidos
					//se tiene que dar la posibilidad de elegir entre visibilidad de grupo, publica o ver todas.
					
					//limpiamos el select de visibilidad para cargarlos con las posibles visibilidades que se ofrecen
					// Only if the user selects a teached group: -10 means default "-" value
					if (document.getElementById('filterGroup').value!="-10"){
						position=0;
						var listaVisibilidad=document.getElementById("filterScope");
						n=document.getElementById("filterScope").length;
						while (position<n){
							listaVisibilidad.remove(0);
							position++;
						}
						//Rellenamos con las 3 visibilidades posibles
						fillVisibilidad(listaVisibilidad);
					}

					//Ocultamos los select que no corresponden
					document.getElementById("divAdvanced1").style.display="none";
					document.getElementById("divAdvanced2").style.display="none";
					document.getElementById("divAdvanced3").style.display="none";
					document.getElementById("divAdvanced4").style.display="none";
				}
				// obtener los temas de la asignatura
				if(document.getElementById('filterGroup').value!="-2" && document.getElementById('filterGroup').value!="-1"){
					//obtenemos los temas de la asignatura
					QuestionListMgmt.getSubjectsByGroup(document.getElementById('filterGroup').value,{callback:refreshThemes,
						 timeout:callBackTimeOut,
						 errorHandler:function(message) { iTestUnlockPage('error');} });
					return;
				}
				
			}
		}

		function fillSpecifiedGroups(grupos){
			//Rellenando Grupos Especificos
			
			num=document.getElementById("filterGroup").length;
			var misGrupos=document.getElementById("filterGroup");
			var misGruposImpartidos = new Array(num);
			pos=0;
			while (pos<num-1){
				misGruposImpartidos[pos]=misGrupos.options[pos].value;
				pos++;
			}
			position=0;
			var lista=document.getElementById("filterSpecifiedGroup");
			n=document.getElementById("filterSpecifiedGroup").length;
			while (position<n-1){
				lista.remove(1);
			position++;
			}
			position=1;
			posAux=0;
			while (posAux<grupos.length){
				grupo=grupos[posAux];
				if (grupo.id!=${group.id}){
					esUnGrupoMio=false;
					pos=0;
					while (pos<num-1){
						if (grupo.id==misGruposImpartidos[pos]){
							esUnGrupoMio=true;
						}
						pos++;
					}
					if (!esUnGrupoMio){
						var opcion=document.createElement('option');
						opcion.text=grupo.course.name + ", " + grupo.name + ", " + grupo.year;
						opcion.value=grupo.id;
						lista[position]=opcion;
						position++;
					}
				}
				posAux++;
			
			}
		}
		
		function getGroupsByInstitutions(state){
			if (state==true){	
				document.getElementById('themesColumn').style.display='none';
				if (document.getElementById('filterInstitution').value!=""){
					//llamada por ajax para que nos de grupos de un centro
					//Cargando Grupos especificados
					QuestionListMgmt.getGroupsByInstitutions(document.getElementById('filterInstitution').value,{callback:fillSpecifiedGroups,
						 timeout:callBackTimeOut,
						 errorHandler:function(message) { iTestUnlockPage('error');} }); 
				}
				else{
					position=0;
					var lista=document.getElementById("filterSpecifiedGroup");
					n=document.getElementById("filterSpecifiedGroup").length;
					//Borrando grupos especificados cuando no hay centro seleccionado
					while (position<n-1){
						lista.remove(1);
					position++;
					}
				}
			}
		}

		function resetValues(){
			document.getElementById('filterGroup').selectedIndex=0;
			getInstitutions(true);
			document.getElementById('questionstabletbody').innerHTML='';
		}

		function refreshThemes(themes){
			if(themes!=null){
				var elSel = document.getElementById('selectThemes');

				while(elSel.length > 0)
				  {
				    elSel.remove(elSel.length - 1);
				  }
				document.getElementById('themesColumn').style.display="";
				document.getElementById('selectThemes').options[0] = new Option("---------","-1","-1");
				for(var i=0;i<themes.length;i++){
					document.getElementById('selectThemes').options[i+1] = new Option(themes[i].subject,themes[i].id);
				}
				
								

				
				
				document.getElementById('selectThemes').options[0] = new Option("---------","0","0");

			}
			iTestUnlockPage();
		}

		function getThemesListFromOthersGroups(){
			var idGroup = document.getElementById('filterSpecifiedGroup').value;
			if(idGroup==""){
				document.getElementById('themesColumn').style.display='none';
				return;
			}
			iTestLockPage('');
			QuestionListMgmt.getSubjectsByGroup(idGroup,{callback:refreshThemes,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
		}
	</script>

	<div id="contenido">
			<div>
				<form id="filterForm" name="formfiltro" method="post" action="javascript:runFilterAndSearch('');">
				  <fieldset style="font-size:16px;">
				  <legend><fmt:message key="labelFilterTitle"/></legend>
					<table width="100%">
					  <tr>
						<td align="right"><fmt:message key="labelFilterId"/></td>
						<td align="left"><input id="filterId" name="filtroid" size="5"></input></td>
						<td align="right"><fmt:message key="labelFilterText"/></td>
						<td align="left" colspan="3"><input id="filterText" name="filtroenunciado" size="50"></input></td>
					  </tr>
					  
					  <tr>
						<td align="right"><fmt:message key="labelFilterGroup"/></td>
						<td align="left">
						   <select id="filterGroup" name="filtrogrupos" onchange="javascript:getInstitutions(true);">
								<option value="-2">-------------------</option>
							<c:if test="${!empty mygroups}">
								<c:forEach items="${mygroups}" var="mygroup">
								    <c:if test="${mygroup.id != group.id}">
									   <option value="${mygroup.id}">
									      	<c:out value="${mygroup.course.name}"/> (<c:out value="${mygroup.name}"/>), <c:out value="${mygroup.year}"/>
									   </option>
									</c:if>
								</c:forEach>
							</c:if>
							<!-- If the teacher has one group, he/she only can import public questions 
							     We need to change the value (FIXME) -->
						     
						  		<option value="-1"><fmt:message key="labelFilterShowOthers"/></option>
						  	</select>	
						</td>
						<td align="right"><fmt:message key="labelFilterContieneTema"/></td>
						<td align="left" colspan="3"><input id="filterTextTheme" name="filtroContieneTema" size="50"></input></td>
					  </tr>
					  <tr id="themesColumn" style="display:none">
					  	<td align="right"><fmt:message key="labelThemes"/></td>
					  	<td align="left"><select id="selectThemes"></select></td>
					  </tr>
					  <tr>
					  <td align="right"><div id="divAdvanced1" style="display:none;"><fmt:message key="labelFilterInstitution"/></div></td>
						<td align="left"><div id="divAdvanced2" style="display:none;">
						   <select id="filterInstitution" name="filtroCentro" onchange="javascript:getGroupsByInstitutions(true);">
						  		<option selected value=""><fmt:message key="labelFilterShowAll"/></option>
									<%-- THE VALUES ARE CONSTANTS OF THE PROJECT --%>
						  		<option value="0"><fmt:message key="diffLow"/></option>
						  		<option value="1"><fmt:message key="diffMedium"/></option>
						  		<option value="2"><fmt:message key="diffHigh"/></option>
						   </select>
						   </div>
						</td>
						<td><div id="divAdvanced3" style="display:none;"><fmt:message key="labelFilterSpecifiedGroup"/></div></td>
						<td><div id="divAdvanced4" style="display:none;" align="left">
						   <select id="filterSpecifiedGroup" name="filtrogruposdelcentro" onchange="javascript:getThemesListFromOthersGroups();">
						  		<option selected value=""><fmt:message key="labelFilterShowAll"/></option>
						   </select>
						   </div>
						</td>
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
						      <!-- If the teacher has one group, he/she only can import public questions -->
						      <c:if test="${fn:length(mygroups) gt 1}">
						  		<option selected value=""><fmt:message key="labelFilterShowAll"/></option>
 								<%-- THE VALUES ARE CONSTANTS OF THE PROJECT --%>
						  		<option value="0"><fmt:message key="scopeGroup"/></option>
						  	  </c:if>
						  		<option value="2"><fmt:message key="scopePublic"/></option>
						   </select>
						</td>
					  </tr>
					  <tr>
					    <td colspan="6" align="center">
					       <br>
					       <input type="reset" value="<fmt:message key="msgbuttonReset"/>" onclick="javascript:resetValues();"/>
					       <input type="submit" name="submitfiltrar" value="<fmt:message key="buttonFilterRun"/>"/>
					    	<input type="checkBox" id="limitCheckBox"/><label><fmt:message key="labelLimit100"/></label>
					    </td>	
					  </tr>						  
					</table>
				  </fieldset>
				</form>
			</div>
			<div>
			   <p>
			   <label><fmt:message key="labelImportToThisTheme"/></label>&nbsp;
						  <select id="destinationTheme" name="destTema">
							<c:if test="${!empty themes}">
								<c:forEach items="${themes}" var="theme">
									<option id="destinationTheme_${theme.id}" value="${theme.id}">
										<c:out value="${theme.sort}"/>.- <c:out value="${theme.subject}"/>
									</option>
								</c:forEach>
							</c:if>
						  </select>
				    &nbsp;<input type="button" name="submitimport" value="<fmt:message key="buttonGoImportQuestions"/>" onclick="javascript:importQuestions();">
					&nbsp;<input type="button" value="<fmt:message key="reverseSelection"/>"" onclick="javascript:reverseSelection()"/>
				</p>
				<hr/>
			</div>
			<div>
			<table id="questionstable" class="tabladatos">
			  <tr>
				<th><center><a href="javascript:runFilterAndSearch('id');"><fmt:message key="headerQlistId"/></a></center></th>
				<th><a href="javascript:runFilterAndSearch('text');"><fmt:message key="headerQlistText"/></a></th>	
				<th>&nbsp;</th>
				<th><a href="javascript:runFilterAndSearch('course');"><fmt:message key="headerQlistCourse"/></a></th>
				<th><a href="javascript:runFilterAndSearch('subject');"><fmt:message key="headerQlistTheme"/></a></th>
				<th><center><a href="javascript:runFilterAndSearch('diff');"><fmt:message key="headerQlistDiff"/></a></center></th>
				<th><center><a href="javascript:runFilterAndSearch('scope');"><fmt:message key="headerQlistScope"/></a></center></th>
				<th>&nbsp;</th>
				<th></th>
			  </tr>
			 <tbody id="questionstabletbody">
				  <tr>
				    <td align="center" colspan="9">&nbsp;</td>
				  </tr>	
				  <tr>
				    <td align="center" colspan="9"><fmt:message key="trySearchQuestions"/></td>
				  </tr>			 
			</tbody>
			</table>
			<br/>
			</div>
		</div>
	</body>
</html>