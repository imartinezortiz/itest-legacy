<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>

<% 
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	if (request.getParameter("idcourse") == null)
		breadCrumb.addBundleStep("textNewCourse","");
	else
		breadCrumb.addBundleStep("textEditCourse","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="admin"/>
	<jsp:param name="menu" value="admin"/>
</jsp:include>

	<!-- Ajax for groups -->
	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminCourseMgmt.js'></script>
	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminGroupMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>

	<script type='text/javascript'>
			
		//Start call, fills the DataTable when the page loads.
		showRelatedInfo();
				
		/**
		 * Obtains the value of the parameter in the queryString
		 * @param queryString the string of the query to the server answered with this page
		 * @param parameterName the name of the parameter
		 * @return the value of the parameter if exists null if it doesnt
		 */
		function getParameter ( queryString, parameterName ) {
			// Add "=" to the parameter name (i.e. parameterName=value)
			var parameterName = parameterName + "=";
				if ( queryString.length > 0 ) {
					// Find the beginning of the string
					begin = queryString.indexOf ( parameterName );
					// If the parameter name is not found, skip it, otherwise return the value
					if ( begin != -1 ) {
					   // Add the length (integer) to the beginning
					   begin += parameterName.length;
					   // Multiple parameters are separated by the "&" sign
					   end = queryString.indexOf ( "&" , begin );
					if ( end == -1 ) {
					   end = queryString.length
					}
					// Return the string
					return unescape ( queryString.substring ( begin, end ) );
				}
				// Return "null" if no parameter has been found
				return "null";
			}
		}
		
		
		/**
			AJAX call, obtains the list of groups of the current course and the related tutors of each group
		*/
		function showRelatedInfo(){
			// Obtain the query string
			var queryString = window.top.location.search.substring(1);
			// Obtain the course id value from the query string
			var ID = getParameter(queryString,"idcourse");
			// Checks if the ID param is in the query string (If not we are creating a new course)
			if (ID != "null")
				AdminCourseMgmt.getOrderedGroupsWithTutors(ID,"",{callback:updateDataTable,
					 timeout:callBackTimeOut,
					 errorHandler:function(message) { iTestUnlockPage('error');} });
		}



		/**
		 * Updates the data table
		 * @param list list of objects to show in the DataTable
		 */
		function updateDataTable(list){
			// Variables
			var element; 			// Stores the values of the lists (Loop variable)
			var userList;			// Stores the list of tutors of the current group (Loop variable)
			var nameText;			// Text with the names of the tutors for one group
			var surnameText;		// Text with the surnames of the tutors for one group
			var tutorOptiontext;	// Text with the html text for the option buttons
			var groupID;			// Id of the group being procesed
			var contador = 0;		// Number of tutors displayed
			tbodyelement = document.getElementById('datatabletbody');
			// delete all rows in the table
			if ( tbodyelement.hasChildNodes() )	{
			    while ( tbodyelement.childNodes.length >= 1 ) {
		        	tbodyelement.removeChild( tbodyelement.firstChild );       
			    } 
			}
			
			// We Loop though the Group List
			for (i=0; i<list.length; i++) {
				// Obtain Group from list, list of the group's tutors and group's ID
				element = list[i];
		        userList = element.tutorList;
				groupID = element.id;
		        
				rowelement = document.createElement('tr');
				
	        	// Institution's name
				cellelement = document.createElement('td');
   				cellelement.setAttribute("id","institution"+element.id);
			   	cellelement.innerHTML = element.institution.name;
				rowelement.appendChild(cellelement);

				// Group's Year
				cellelement = document.createElement('td');
   				cellelement.setAttribute("id","year"+element.id);
			   	cellelement.innerHTML = element.year;
				rowelement.appendChild(cellelement);
				
				// Group's name 
				cellelement = document.createElement('td');
   				cellelement.setAttribute("id","group"+element.id);
   				cellelement.setAttribute("align","center");
			   	cellelement.innerHTML = element.name;
				rowelement.appendChild(cellelement);
				
				// Group's option buttons
				cellelement = document.createElement('td');
   				cellelement.setAttribute("id","group"+element.id);
   				cellelement.setAttribute("align","center");
			   	cellelement.innerHTML = "<a href=\"institution.itest?method=editGroup&idgroup=${'"+element.id+"'}&idinstitution=${'"+element.institution.id+"'}\">" +
				   	"<img src=\"${pageContext.request.contextPath}/imagenes/editar.gif\" alt=\"<fmt:message key="labelEditGroup"/>\" title=\"<fmt:message key="labelEditGroup"/>\" border=\"none\"></a>" +
					"<a href=\"javascript:deleteGroup('"+element.id+"');\">"+
					"<img src=\"${pageContext.request.contextPath}/imagenes/borrar.gif\" alt=\"<fmt:message key="labelDeleteGroup"/>\" title=\"<fmt:message key="labelDeleteGroup"/>\" border=\"none\"></a>";
				rowelement.appendChild(cellelement);
				
				//Group Tutor's table
				var nestedtable = document.createElement('table');
				nestedtable.setAttribute("class","nested");
				nestedtable.setAttribute("cellpadding","0");
				nestedtable.setAttribute("cellspacing","0");
				nestedtable.setAttribute("width","100%");
				//Columns configuration
				var colelement = document.createElement("col");
				colelement.setAttribute("width","70%");
				nestedtable.appendChild(colelement);
				var colelement = document.createElement("col");
				colelement.setAttribute("width","30%");
				colelement.setAttribute("align","right");
				nestedtable.appendChild(colelement);
				//Variables
				var trelement;
				var tdelement;
				var optiontext;
		        for (j=0;j<userList.length;j++){
					element = userList[j];
					trelement = document.createElement("tr");
					//Tutor's surname and name
				   	tdelement = document.createElement("td");
				   	tdelement.innerHTML=element.surname + ", " + element.name;
				   	trelement.appendChild(tdelement);
					//Tutor's option buttons
				   	optiontext = '<td>' + "<center><a href=\"institution.itest?method=editUser&iduser="+element.id+"\">	<img src=\"${pageContext.request.contextPath}/imagenes/editar.gif\" alt=\"<fmt:message key="labelEditUser"/>\" title=\"<fmt:message key="labelEditUser"/>\" border=\"none\">" +
				   	"<a href=\"javascript:unassignTutor('"+element.id+"','"+groupID+"');\"><img src=\"${pageContext.request.contextPath}/imagenes/borrar.gif\" alt=\"<fmt:message key="labelUnassignTutor"/>\" title=\"<fmt:message key="labelUnassignTutor"/>\" border=\"none\"></a></center></td></tr>";
				   	tdelement = document.createElement("td");
				   	tdelement.innerHTML=optiontext;
				   	
				   	trelement.appendChild(tdelement);
		        	nestedtable.appendChild(trelement);
					   				   	
				   	
		        }

		        
				// Tutor's table
				cellelement = document.createElement('td');
	   			cellelement.setAttribute("id","userName"+element.id);
	   			cellelement.setAttribute("class","noresalt");
	   			cellelement.setAttribute("colspan",3);
				cellelement.appendChild(nestedtable);
				rowelement.appendChild(cellelement);
				contador++;
				// Add row
				tbodyelement.appendChild(rowelement);
			}
			
			// No related Information: present message
		    if (list.length == 0) {
		       	rowelement = document.createElement('tr');
		       	cellelement = document.createElement('td');
		       	cellelement.colSpan=7;
		       	cellelement.setAttribute("align","center");
	       		cellelement.innerHTML = "<fmt:message key="noCourseRelatedData"/>";
			   	rowelement.appendChild(cellelement);
			   	tbodyelement.appendChild(rowelement);
		    } else {
		    	rowelement = document.createElement('tr');
				cellelement = document.createElement('td');
				cellelement.colSpan=7;
				cellelement.setAttribute("align","center");
				cellelement.innerHTML ="<hr/>";
				rowelement.appendChild(cellelement);
				tbodyelement.appendChild(rowelement);
				rowelement = document.createElement('tr');
				cellelement = document.createElement('td');
				cellelement.colSpan=6;
				cellelement.setAttribute("align","center");
				cellelement.innerHTML ="<b><fmt:message key="totalLabel"/> "+contador+"</b>";
				rowelement.appendChild(cellelement);
				tbodyelement.appendChild(rowelement);
		    }
		
		}
		
		/**
		 *	AJAX call deletes a group
		 */
		function deleteGroup(id){
			if (confirm ('<fmt:message key="confirmDeleteGroup"/>\n<fmt:message key="alertDeleteGroup"/>'))
				// AJAX call, showRelatedInfo is the callback
				AdminGroupMgmt.deleteGroup(id,{callback:showRelatedInfo,
					 timeout:callBackTimeOut,
					 errorHandler:function(message) { iTestUnlockPage('error');} });
		}
			
		/**
		 *	AJAX call unassings a tutor from a group
		 */
		function unassignTutor(idTutor,idGroup) {
			// AJAX call, showRelatedInfo is the callback
			AdminGroupMgmt.unAssignTutorCourseView(idTutor,idGroup,{callback:showRelatedInfo,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
		}

		/**
		 *	AJAX call, asks for the list of groups ordered by group name and changes the html swaping the order mode
		 *	@reverse if its empty, the lists is ordered normaly, if not it's order is reversed
		 */
		function sortGroups(reverse){
			// Obtain the query string
			var queryString = window.top.location.search.substring(1);
			// Obtain the course id value from the query string
			var ID = getParameter(queryString,"idcourse");
			// Checks if the ID param is in the query string (If not we are creating a new course)
			if (ID != "null")
				// Reverse order
				if (reverse != null){
					// Change the link to de function, we swap methods
					document.getElementById('groupOrder').setAttribute("href","javascript:sortGroups()");
					// AJAX call
					AdminCourseMgmt.getOrderedGroupsWithTutors(ID,"groupreverse",{callback:updateDataTable,
						 timeout:callBackTimeOut,
						 errorHandler:function(message) { iTestUnlockPage('error');} });
				}else{
					// Change the link to de function, we swap methods
					document.getElementById('groupOrder').setAttribute("href","javascript:sortGroups('reverse')");
					// AJAX call
					AdminCourseMgmt.getOrderedGroupsWithTutors(ID,"group",{callback:updateDataTable,
						 timeout:callBackTimeOut,
						 errorHandler:function(message) { iTestUnlockPage('error');} });
				}
		}
		
		/**
		 *	AJAX call, asks for the list of groups ordered by institution name and changes the html swaping the order mode
		 *	@reverse if its empty, the lists is ordered normaly, if not it's order is reversed
		 */
		function sortInstitutions(reverse){
			// Obtain the query string
			var queryString = window.top.location.search.substring(1);
			// Obtain the course id value from the query string
			var ID = getParameter(queryString,"idcourse");
			// Checks if the ID param is in the query string (If not we are creating a new course)
			if (ID != "null")
				if (reverse != null){
					// Change the link to de function, we swap methods
					document.getElementById('institutionOrder').setAttribute("href","javascript:sortInstitutions()");
					// AJAX call
					AdminCourseMgmt.getOrderedGroupsWithTutors(ID,"institutionreverse",{callback:updateDataTable,
						 timeout:callBackTimeOut,
						 errorHandler:function(message) { iTestUnlockPage('error');} });
				}else{
					// Change the link to de function, we swap methods
					document.getElementById('institutionOrder').setAttribute("href","javascript:sortInstitutions('reverse')");
					// AJAX call
					AdminCourseMgmt.getOrderedGroupsWithTutors(ID,"institution",{callback:updateDataTable,
						 timeout:callBackTimeOut,
						 errorHandler:function(message) { iTestUnlockPage('error');} });
				}
		}
	
		/**
		 *	AJAX call, asks for the list of groups ordered by year and changes the html swaping the order mode
		 *	@reverse if its empty, the lists is ordered normaly, if not it's order is reversed
		 */
		function sortYears(reverse){
			// Obtain the query string
			var queryString = window.top.location.search.substring(1);
			// Obtain the course id value from the query string
			var ID = getParameter(queryString,"idcourse");
			// Checks if the ID param is in the query string (If not we are creating a new course)
			if (ID != "null")
				if (reverse != null){
					// Change the link to de function, we swap methods
					document.getElementById('yearsOrder').setAttribute("href","javascript:sortYears()");
					// AJAX call
					AdminCourseMgmt.getOrderedGroupsWithTutors(ID,"yearsreverse",{callback:updateDataTable,
						 timeout:callBackTimeOut,
						 errorHandler:function(message) { iTestUnlockPage('error');} });
				}else{
					// Change the link to de function, we swap methods
					document.getElementById('yearsOrder').setAttribute("href","javascript:sortYears('reverse')");
					// AJAX call
					AdminCourseMgmt.getOrderedGroupsWithTutors(ID,"years",{callback:updateDataTable,
						 timeout:callBackTimeOut,
						 errorHandler:function(message) { iTestUnlockPage('error');} });
				}
		}

		function checkValuesCourse(){
			var code = document.getElementById("code").value;
			var name = document.getElementById("name").value;
			var level = document.getElementById("level").value;
			var studies = document.getElementById("studies").value;
			var comments = document.getElementById("comments").value;
			if((code.length>10)||(code.length<1)){
				alert("<fmt:message key="alertCode"/>");
				return false;
			}
			if((name.length>100)||(name.length<1)){
				alert("<fmt:message key="alertName"/>");
				return false;
			}
			if((level.length>2)||(level.length<1)){
				alert("<fmt:message key="alertLevel"/>");
				return false;
			}
			if((studies.length>20)||(studies.length<1)){
				alert("<fmt:message key="alertStudies"/>");
				return false;
			}
			if(comments.length>250){
				alert("<fmt:message key="alertComments"/>");
				return false;
			}
			
			
			return true;
		}

		function isCodeAlreadyInUse(){
			AdminCourseMgmt.isCodeAlreadyInUse(document.getElementById('code').value,{callback:checkCodeUse,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
			return false;
		}

		function checkCodeUse(bool){
			if(bool){
				var modified;
			    modified=document.createElement('label');
		   	 	modified.setAttribute("id","labelCodeModified");
		   	 	modified.innerHTML = "\n<fmt:message key="alertCourseRepeat"/>";
		   	 	modified.style.color="#FF0000";
		   	 	//modified.style.fontSize="10";
				if (document.getElementById("labelCodeModified")){
			   		document.getElementById("labelCode").removeChild(document.getElementById("labelCodeModified"));
				}
		   	 	if (!document.getElementById("labelCodeModified"))
		   			document.getElementById("labelCode").appendChild(modified);
			}else{
				var modified;
			    modified=document.createElement('label');
		   	 	modified.setAttribute("id","labelCodeModified");
		   	 	modified.innerHTML = "\n<fmt:message key="alertCourseNoRepeat"/>";
		   	 	//modified.style.fontSize="10";
				if (document.getElementById("labelCodeModified")){
			   		document.getElementById("labelCode").removeChild(document.getElementById("labelCodeModified"));
				}
		   	 	if (!document.getElementById("labelCodeModified"))
		   			document.getElementById("labelCode").appendChild(modified);
			}
		}

		function changedCode(){
			if (document.getElementById("labelCodeModified")){
		   		document.getElementById("labelCode").removeChild(document.getElementById("labelCodeModified"));
			}
		}

		function checkSubject(){
			AdminCourseMgmt.isCodeAlreadyInUse(document.getElementById('code').value,{callback:checkCodeUsed,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
			return false;
		}

		function checkCodeUsed(obj){
			if(!obj){
				document.myform.submit();
			}else{
				alert("<fmt:message key="alertCourseRepeat"/>");
			}
		}
	</script>


		<div id="contenido">
			<form name="myform" action="admin.itest?method=saveCourse" method="post" onsubmit="return checkSubject()">
			<c:if test="${!empty course}">
				<input type="hidden" name="idcourse" value="${course.id}"/>
			</c:if>
				<fieldset>
					<legend><fmt:message key="data"/></legend>
					<input align="left" type="button" value="<fmt:message key="buttonCheckCode"/>" onclick="return isCodeAlreadyInUse();">
					<table class="tablaformulario">
						<col width="15%"/>
						<col width="20%"/>
						<col width="15%"/>
						<col width="50%"/>
						<tr>
							<th><label for="code"><fmt:message key="code"/></label></th>
							<td id="labelCode">
							<c:choose>
						  	<c:when test="${empty course}">
								<input type="text" name="code" id="code" onkeydown="javascript:changedCode()"/>
							</c:when>
							<c:otherwise>
								<input type="text" name="code" id="code" value="${course.code}" onkeydown="javascript:changedCode()"/>
							</c:otherwise>
							</c:choose>
							</td>
							<th><label for="name"><fmt:message key="name"/></th>
							<td>
							<c:choose>
						  	<c:when test="${empty course}">
								<input type="text" name="name" id="name"/>
							</c:when>
							<c:otherwise>
								<input type="text" name="name" id="name" value="${course.name}"/>
							</c:otherwise>
							</c:choose>
							</td>
						</tr>
						<tr>
							<th><label for="level"><fmt:message key="level"/></label></th>
							<td>
							<c:choose>
						  	<c:when test="${empty course}">
								<select name="level" id="level">
									<option value="1º">1º</option>
									<option value="2º">2º</option>
									<option value="3º">3º</option>
									<option value="4º">4º</option>
									<option value="5º">5º</option>
									<option value="6º">6º</option>
									<option value="*"><fmt:message key="labelCheckOtros"/></option>
								</select>
							</c:when>
							<c:otherwise>
								<select name="level" id="level">
									<option value="*"><fmt:message key="labelCheckOtros"/></option>
									<c:choose>
										<c:when test="${course.level eq '1º'}">
											<option selected value="1º">1º</option>
										</c:when>
										<c:otherwise>
											<option value="1º">1º</option>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${course.level eq '2º'}">
											<option selected value="2º">2º</option>
										</c:when>
										<c:otherwise>
											<option value="2º">2º</option>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${course.level eq '3º'}">
											<option selected value="3º">3º</option>
										</c:when>
										<c:otherwise>
											<option value="3º">3º</option>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${course.level eq '4º'}">
											<option selected value="4º">4º</option>
										</c:when>
										<c:otherwise>
											<option value="4º">4º</option>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${course.level eq '5º'}">
											<option selected value="5º">5º</option>
										</c:when>
										<c:otherwise>
											<option value="5º">5º</option>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${course.level eq '6º'}">
											<option selected value="6º">6º</option>
										</c:when>
										<c:otherwise>
											<option value="6º">6º</option>
										</c:otherwise>
									</c:choose>
								</select>
							</c:otherwise>
							</c:choose>
							</td>
							<th><label for="studies"><fmt:message key="studies"/></th>
							<td>
							<c:choose>
						  	<c:when test="${empty course}">
								<select name="studies" id="studies">
									<option value="Infantil"><fmt:message key="labelCheckInfantil"/></option>
									<option value="Primaria"/><fmt:message key="labelCheckPrimaria"/></option>
									<option value="Secundaria"><fmt:message key="labelCheckSecundaria"/></option>
									<option value="Ciclo Formativo"><fmt:message key="labelCheckFp"/></option>
									<option value="Bachillerato"><fmt:message key="labelCheckBachillerato"/></option>
									<option value="Universidad"><fmt:message key="labelCheckUniversidad"/></option>
									<option value="Otros"><fmt:message key="labelCheckOtros"/></option>
								</select>
							</c:when>
							<c:otherwise>
								<select name="studies" id="studies">
									<option value="Otros"><fmt:message key="labelCheckOtros"/></option>
									<c:choose>
										<c:when test="${course.studies eq 'Infantil'}">
											<option selected value="Infantil"><fmt:message key="labelCheckInfantil"/></option>
										</c:when>
										<c:otherwise>
											<option value="Infantil"><fmt:message key="labelCheckInfantil"/></option>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${course.studies eq 'Primaria'}">
											<option selected value="Primaria"><fmt:message key="labelCheckPrimaria"/></option>
										</c:when>
										<c:otherwise>
											<option value="Primaria"><fmt:message key="labelCheckPrimaria"/></option>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${course.studies eq 'Secundaria'}">	
											<option selected value="Secundaria"><fmt:message key="labelCheckSecundaria"/></option>
										</c:when>
										<c:otherwise>
											<option value="Secundaria"><fmt:message key="labelCheckSecundaria"/></option>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${course.studies eq 'Ciclo Formativo'}">	
											<option selected value="Ciclo Formativo"><fmt:message key="labelCheckFp"/></option>
										</c:when>
										<c:otherwise>
											<option value="Ciclo Formativo"><fmt:message key="labelCheckFp"/></option>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${course.studies eq 'Bachillerato'}">	
											<option selected value="Bachillerato"><fmt:message key="labelCheckBachillerato"/></option>
										</c:when>
										<c:otherwise>
											<option value="Bachillerato"><fmt:message key="labelCheckBachillerato"/></option>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${course.studies eq 'Universidad'}">	
											<option selected value="Universidad"><fmt:message key="labelCheckUniversidad"/></option>
										</c:when>
										<c:otherwise>
											<option value="Universidad"><fmt:message key="labelCheckUniversidad"/></option>
										</c:otherwise>
									</c:choose>
									
								</select>
							</c:otherwise>
							</c:choose>
							</td>
						</tr>
						<tr>
							<th><label for="comments"><fmt:message key="comments"/></label></th>
							<td colspan="3">
							<c:choose>
						  	<c:when test="${empty course}">
								<textarea rows="3" name="comments" id="comments"></textarea>
							</c:when>
							<c:otherwise>
								<textarea rows="3" name="comments" id="comments"><c:out value="${course.comments}"/></textarea>	
							</c:otherwise>
							</c:choose>
							</td>
						</tr>
					</table>
					<c:choose>
				  	<c:when test="${empty course}">
						<input type="submit" value="<fmt:message key="addButton"/>"/ onclick="return checkValuesCourse()">
					</c:when>
					<c:otherwise>
						<input type="submit" value="<fmt:message key="buttonSave"/>"/ onclick="return checkValuesCourse()">
					</c:otherwise>
					</c:choose>				
				</fieldset>
			</form>	
			
			<!-- ---Gonzalo Modification Start-- -->
			<!-- Add new section with a list to show related information about the course shown-->
			<!-- Institutions with groups of this course, the courses and the tutors of those curses-->

			<table id="groupstable" class="tabladatos">
				<col width="15%"/>
				<col width="5%"/>
				<col width="10%"/>
				<col width="5%"/>
				<col width="60%"/>
				<tr>
					<th><a id="institutionOrder" href="javascript:sortInstitutions()"><fmt:message key="textInstitutionsList"/></a></th>
					<th><center><a id="yearsOrder" href="javascript:sortYears()"><fmt:message key="yearGroup"/></a></center></th>
					<th colspan=2><center><a id="groupOrder" href="javascript:sortGroups()"><fmt:message key="textGroupsList"/></a></center></th>
					<th colspan><fmt:message key="tutorRole"/></th>
			  	</tr>
	
				<tbody id="datatabletbody">
							 
				 	<c:if test="${empty group}">
					  <tr>
					    <td id="lastrow" align="center" colspan="5"><fmt:message key="noCourseRelatedData"/></td>
					  </tr>
				 	</c:if>	 		 			 
				</tbody>
			</table>
			
		</div>
		<c:if test="${!empty errorCourse}">
			<script type="text/javascript">alert('<fmt:message key="alertCourseRepeat"/>');</script>
		</c:if>
	</body>
</html>