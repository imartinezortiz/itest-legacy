<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>
<%@ page import="com.cesfelipesegundo.itis.model.Institution" %>

<% 
	Institution institution = (Institution)request.getAttribute("institution");
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addStep(institution.getName(),request.getContextPath()+"/admin/institution.itest?method=indexInstitution");
	if (request.getParameter("idgroup") == null)
		breadCrumb.addBundleStep("textNewGroup","");
	else
		breadCrumb.addBundleStep("textEditGroup","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="admin"/>
	<jsp:param name="menu" value="institution"/>
</jsp:include>

	<!-- Ajax for groups -->
	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminGroupMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>

	<script type='text/javascript'>
	
		var tutors = new Array();
		var learners = new Array();
		var tutorsFilled = false;
		var learnersFilled = false;
		var currentView = "tutors";
		
		<c:choose>
	  	<c:when test="${!empty group}">
			var isNew = false;
			var groupID = <c:out value="${group.id}"/>;
			showTutors();
			
		</c:when>
		<c:otherwise>
			var isNew = true;
			var groupID = -1;
			tutorsFilled = true;
			learnersFilled = true;
		</c:otherwise>
		</c:choose>
		
		function showLearners() {
			if (learnersFilled) {
				updateUsersTable("learners");
			}
			else {
				learnersFilled = true;
				// Call to AJAX to get the list of students, receiveTutors is the callback
				AdminGroupMgmt.getRegisteredStudents({callback:receiveLearners,
					 timeout:callBackTimeOut,
					 errorHandler:function(message) { iTestUnlockPage('error');} });
			}
		}

		function showTutors() {
			if (tutorsFilled) {
				updateUsersTable("tutors");
			}
			else {
				tutorsFilled = true;
				// Call to AJAX to get the list of students, receiveTutors is the callback
				AdminGroupMgmt.getAssignedTutors({callback:receiveTutors,
					 timeout:callBackTimeOut,
					 errorHandler:function(message) { iTestUnlockPage('error');} });
			}
		}
		
		function receiveLearners(learnersList) {
			learners = learnersList;
			learners.sort(compSurname);
			updateUsersTable("learners");
		}
		
		function receiveTutors(tutorsList) {
			tutors = tutorsList;
			tutors.sort(compSurname);
			updateUsersTable("tutors");
		}

		function updateUsersTable(view) {
			var users;
			var user;
			currentView = view;

			if (view == "learners") {
				users = learners;
				document.getElementById('learnersTab').className = 'selected';
				document.getElementById('tutorsTab').className = '';
				var button = document.getElementById('assignbutton');
				button.value = '<fmt:message key="buttonRegisterNewLearner"/>';
				button.onclick = openAssignLearner;
				closeAssignTutor();
			} else {
				users = tutors;
				document.getElementById('learnersTab').className = '';
				document.getElementById('tutorsTab').className = 'selected';
				var button = document.getElementById('assignbutton');
				button.value = '<fmt:message key="buttonAssignNewTutor"/>';
				button.onclick = openAssignTutor;
				closeAssignLearner();
			}
			
			tbodyelement = document.getElementById('userstabletbody');
			// delete all rows in the table
			if ( tbodyelement.hasChildNodes() )	{
			    while ( tbodyelement.childNodes.length >= 1 ) {
			        tbodyelement.removeChild( tbodyelement.firstChild );       
			    } 
			}

			for (i=0; i<users.length; i++) {
		        user = users[i];
		        
				rowelement = document.createElement('tr');

				// Personal ID of the student (DNI in Spain)
				cellelement = document.createElement('td');
				if (user.persId == null) {
				   cellelement.innerHTML = "&nbsp;";
				} else {
			       cellelement.innerHTML = user.persId;
			    }
   				cellelement.setAttribute("align","center");
   				cellelement.setAttribute("id","persId"+user.id);
				rowelement.appendChild(cellelement);
				
				// Surname of the student
				cellelement = document.createElement('td');
   				cellelement.setAttribute("id","surname"+user.id);
			    cellelement.innerHTML = user.surname;
				rowelement.appendChild(cellelement);

				// Name of the student
				cellelement = document.createElement('td');
   				cellelement.setAttribute("id","name"+user.id);
			    cellelement.innerHTML = user.name;
				rowelement.appendChild(cellelement);

				// Username of the student
				cellelement = document.createElement('td');
   				cellelement.setAttribute("id","userName"+user.id);
			    cellelement.innerHTML = user.userName;
   				cellelement.setAttribute("align","center");
				rowelement.appendChild(cellelement);

				// Control element: unregister student or unassign tutor
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				if (view == "learners") {
					cellelement.innerHTML = "<a href=\"javascript:unregisterStudent('"+user.id+"');\"><img src=\"${pageContext.request.contextPath}/imagenes/borrar.gif\" alt=\"<fmt:message key="labelUnregisterStudent"/>\" title=\"<fmt:message key="labelUnregisterStudent"/>\" border=\"none\"></a>";				
				} else {
					cellelement.innerHTML = "<a href=\"javascript:unassignTutor('"+user.id+"');\"><img src=\"${pageContext.request.contextPath}/imagenes/borrar.gif\" alt=\"<fmt:message key="labelUnassignTutor"/>\" title=\"<fmt:message key="labelUnassignTutor"/>\" border=\"none\"></a>";
				}				
				rowelement.appendChild(cellelement);
				
				// Add row
				tbodyelement.appendChild(rowelement);
			}
			
			// No userss: present message
		    if (users.length == 0) {
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=5;
		       cellelement.setAttribute("align","center");
		       if (view == "learners")
			   	cellelement.innerHTML = "<fmt:message key="noAvailableStudents"/>";
			   else
			   	cellelement.innerHTML = "<fmt:message key="noAvailableTutors"/>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		    } else {
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=5;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML ="<hr/>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=5;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML ="<b><fmt:message key="totalLabel"/> "+users.length+"</b>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		    }
			
		}
			
		/* For Ajax */
		function saveGroup() {

			var groupName = document.getElementById("name").value;   
			if (groupName == "") {
					alert ('<fmt:message key="msgGroupNameEmpty"/>');
					return;
			}
			if (groupName.length > 4) {
					alert ('<fmt:message key="msgGroupNameTooLong"/>');
					return;
			}		   

			var groupYear = document.getElementById("year").value;
			if (groupYear == "") {
					alert ('<fmt:message key="msgGroupYearEmpty"/>');
					return;
			}
			if (groupYear.length > 9) {
					alert ('<fmt:message key="msgGroupYearTooLong"/>');
					return;
			}		   
			var groupStudentType = document.getElementById("studentType").value;
		
			// Save the group using Ajax
			if (isNew) {
				var groupCourse = document.getElementById("course");			
				// Call to AJAX, groupAdded is the callback
				AdminGroupMgmt.addGroup(groupName,groupYear,groupCourse.value,groupStudentType,{callback:groupAdded,
					 timeout:callBackTimeOut,
					 errorHandler:function(message) { iTestUnlockPage('error');} });
				isNew = false;
			}
			else {
				// Call to AJAX, without callback
				AdminGroupMgmt.saveGroup(groupID,groupName,groupYear,groupStudentType,correctSaved);
			}
				
		} // saveGroup

		function correctSaved(){
			alert('<fmt:message key="alertGroupSaved"/>');
		}
		
		function groupAdded(group) {
			correctSaved();
			isNew = false;
			groupID = group.id;
			
			// We show the hidden div...
			// Assigned Tutors
			document.getElementById("divtutors").style.display="block";
			
			// Changing button text
			document.getElementById("savebutton").value="<fmt:message key="buttonSave"/>";
			
			// Changing courses select list for a readonly text input
			var tdcourse = document.getElementById("tdcourse");
			if ( tdcourse.hasChildNodes() )	{
			    while ( tdcourse.childNodes.length >= 1 ) {
			        tdcourse.removeChild( tdcourse.firstChild );       
			    } 
			}
			tdcourse.innerHTML="<input type='text' readonly value='"+group.course.name+"'/>";
								    		   
		} // groupAdded
		
		function openAssignTutor() { 
			AdminGroupMgmt.getUnassignedTutors({callback:fillTutorsSelectList,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
			document.getElementById('divassigntutor').style.display='block';
		}

		function openAssignLearner() {
			AdminGroupMgmt.getUnregisteredStudents({callback:fillLearnersSelectList,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
			document.getElementById('divassignlearner').style.display='block';			
		}
		
		function fillLearnersSelectList (users) {
			var selectList = document.getElementById('registerStudentId');
			// delete previous items in the select
			if ( selectList.hasChildNodes() )	{
			    while ( selectList.childNodes.length >= 1 ) {
			        selectList.removeChild( selectList.firstChild );       
			    } 
			}

			for (i=0; i<users.length; i++) {
				var user = users[i];
				var optionItem = document.createElement('option');
				optionItem.value = user.id;
				optionItem.innerHTML = user.surname + ", " + user.name + "(" + user.persId + ")";
				selectList.appendChild(optionItem);
			}
		}
		
		function fillTutorsSelectList (users) {
			var selectList = document.getElementById('assignTutorId');
			// delete previous items in the select
			if ( selectList.hasChildNodes() )	{
			    while ( selectList.childNodes.length >= 1 ) {
			        selectList.removeChild( selectList.firstChild );       
			    } 
			}

			for (i=0; i<users.length; i++) {
				var user = users[i];
				var optionItem = document.createElement('option');
				optionItem.value = user.id;
				optionItem.innerHTML = user.surname + ", " + user.name + "(" + user.persId + ")";
				selectList.appendChild(optionItem);
			}
		}

		function closeAssignTutor() {
			document.getElementById('divassigntutor').style.display='none';
		}
		
		function closeAssignLearner() {
			document.getElementById('divassignlearner').style.display='none';
		}
		
		function assignTutor() {
			var tutorid = document.getElementById('assignTutorId').value;
			var select = document.getElementById('assignTutorId');
			select.remove(select.selectedIndex);
			// AJAX call, tutorAssigned is the callback
			AdminGroupMgmt.assignTutor(tutorid,{callback:tutorAssigned,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
		}
		
		function tutorAssigned (tutor) {
			if (tutor != null) {
				tutors.push(tutor);
				updateUsersTable("tutors");
				alert("<fmt:message key="alertTutorAssignedCorrectly"/>");
			}
		}
		
		function unassignTutor(id) {
			// AJAX call, tutorUnassigned is the callback
			if(confirm('<fmt:message key="unAssignUserFromGroup"/>')){
				AdminGroupMgmt.unAssignTutor(id,{callback:tutorUnassigned,
					 timeout:callBackTimeOut,
					 errorHandler:function(message) { iTestUnlockPage('error');} });
			}
		}
		
		function tutorUnassigned(tutor) {
			if (tutor != null) {
				deletefromlist(tutor,tutors);
				updateUsersTable("tutors");
				AdminGroupMgmt.getUnassignedTutors({callback:fillTutorsSelectList,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
				document.getElementById('divassigntutor').style.display='block';
				alert("<fmt:message key="alertTutorUnassignedCorrectly"/>");
			}
		}

		function registerStudent() {
			var learnerid = document.getElementById('registerStudentId').value;
			var select = document.getElementById('registerStudentId');
			select.remove(select.selectedIndex);
			// AJAX call, learnerRegistered is the callback
			AdminGroupMgmt.registerLearner(learnerid,{callback:learnerRegistered,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
		}
		
		function learnerRegistered (learner) {
			if (learner != null) {
				learners.push(learner);
				updateUsersTable("learners");
				alert("<fmt:message key="alertLearnerAssignedCorrectly"/>");
			}
		}

		function unregisterStudent(id) {
			// AJAX call, learnerUnregistered is the callback
			if(confirm('<fmt:message key="unAssignUserFromGroup"/>')){
				AdminGroupMgmt.unRegisterLearner(id,{callback:learnerUnregistered,
					 timeout:callBackTimeOut,
					 errorHandler:function(message) { iTestUnlockPage('error');} });
			}
		}
		
		function learnerUnregistered(learner) {
			if (learner != null) {
				deletefromlist(learner,learners);
				updateUsersTable("learners");
				AdminGroupMgmt.getUnregisteredStudents({callback:fillLearnersSelectList,
					 timeout:callBackTimeOut,
					 errorHandler:function(message) { iTestUnlockPage('error');} });
				document.getElementById('divassignlearner').style.display='block';
				alert("<fmt:message key="alertTutorUnassignedCorrectly"/>");
			}
		}
		
		function deletefromlist(user, userlist) {
			for (i=0; i<userlist.length; i++) {
				if (user.id == userlist[i].id) {
					userlist.splice(i,1);
					i--;
				}
			}
		}
		
		function sortUsers(method) {
			if (currentView == "learners")
				learners.sort(method);
			else
				tutors.sort(method);
			updateUsersTable(currentView);
		}
		
		function compPersId(a,b) {
			return compareString(a.persId, b.persId);
		}
		
		function compSurname(a,b) {
			return compareString(a.surname, b.surname);
		}

		function compName(a,b) {
			return compareString(a.name, b.name);
		}

		function compUserName(a,b) {
			return compareString(a.userName, b.userName);
		}
		
		function compareString(a, b) {
			if ( a.toLowerCase() < b.toLowerCase() ) {
				return -1;
    		}
    		else if ( a.toLowerCase() < b.toLowerCase() ) {
        		return 1;
    		}
		    else {
		        return 0;
		    }
		}

	</script>

	<div id="contenido">
		<form name="formconfiggroup" method="post" action="javascript:saveGroup();">
			<fieldset>
				<legend><fmt:message key="data"/></legend>
				<table class="tablaformulario">
					<col width="15%"/>
					<col width="20%"/>
					<col width="15%"/>
					<col width="20%"/>
					<col width="10%"/>
					<col width="20%"/>
					<tr>
						<th><label for="code"><fmt:message key="codeGroup"/> :</label></th>
						<td>
						<c:choose>
					  	<c:when test="${empty group}">
							<input type="text" name="name" id="name"/>
						</c:when>
						<c:otherwise>
							<input type="text" name="name" id="name" value="${group.name}"/>
						</c:otherwise>
						</c:choose>
						</td>
						<th><label for="year"><fmt:message key="academicYear"/> :</th>
						<td>
						<c:choose>
					  	<c:when test="${empty group}">
							<select id="year" name="year">
								<option value="2006-2007">2006-2007</option>
								<option value="2007-2008">2007-2008</option>
								<option value="2008-2009">2008-2009</option>
								<option value="2009-2010">2009-2010</option>
								<option value="2010-2011">2010-2011</option>
								<option value="2011-2012">2011-2012</option>
								<option value="2012-2013">2012-2013</option>
								<option value="2013-2014">2013-2014</option>
								<option value="2014-2015">2014-2015</option>
							</select>
						</c:when>
						<c:otherwise>
							<select id="year" name="year">
								<c:choose>
									<c:when test="${group.year eq '2006-2007'}">
										<option value="2006-2007" selected>2006-2007</option>
									</c:when>
									<c:otherwise>
										<option value="2006-2007">2006-2007</option>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${group.year eq '2007-2008'}">
										<option value="2007-2008" selected>2007-2008</option>
									</c:when>
									<c:otherwise>
										<option value="2007-2008">2007-2008</option>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${group.year eq '2008-2009'}">
										<option value="2008-2009" selected>2008-2009</option>
									</c:when>
									<c:otherwise>
										<option value="2008-2009">2008-2009</option>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${group.year eq '2009-2010'}">
										<option value="2009-2010" selected>2009-2010</option>
									</c:when>
									<c:otherwise>
										<option value="2009-2010">2009-2010</option>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${group.year eq '2010-2011'}">
										<option value="2010-2011" selected>2010-2011</option>
									</c:when>
									<c:otherwise>
										<option value="2010-2011">2010-2011</option>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${group.year eq '2011-2012'}">
										<option value="2011-2012" selected>2011-2012</option>
									</c:when>
									<c:otherwise>
										<option value="2011-2012">2011-2012</option>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${group.year eq '2012-2013'}">
										<option value="2012-2013" selected>2012-2013</option>
									</c:when>
									<c:otherwise>
										<option value="2012-2013">2012-2013</option>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${group.year eq '2013-2014'}">
										<option value="2013-2014" selected>2013-2014</option>
									</c:when>
									<c:otherwise>
										<option value="2013-2014">2013-2014</option>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${group.year eq '2014-2015'}">
										<option value="2014-2015" selected>2014-2015</option>
									</c:when>
									<c:otherwise>
										<option value="2014-2015">2014-2015</option>
									</c:otherwise>
								</c:choose>
							</select>
						</c:otherwise>
						</c:choose>
						</td>
						<th><label for="studentType"><fmt:message key="labelStudentRole"/></label></th>
						<td>
						<c:choose>
					  	<c:when test="${empty group}">
							<select id="studentType" name="studentType" size="1">
								<option value="generic" selected><fmt:message key="labelAnyStudent"/></option>
								<option value="learner"><fmt:message key="labelLearner"/></option>
								<option value="kid"><fmt:message key="labelKid"/></option>
							</select>
						</c:when>
						<c:otherwise>
							<select id="studentType" name="studentType" size="1">
								<c:choose>
									<c:when test="${!(group.studentType eq 1 || group.studentType eq 2)}">
										<option value="generic" selected><fmt:message key="labelAnyStudent"/></option>
									</c:when>
									<c:otherwise>
										<option value="generic"><fmt:message key="labelAnyStudent"/></option>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${group.studentType eq 1}">
										<option value="learner" selected><fmt:message key="labelLearner"/></option>
									</c:when>
									<c:otherwise>
										<option value="learner"><fmt:message key="labelLearner"/></option>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${group.studentType eq 2}">
										<option value="kid" selected><fmt:message key="labelKid"/></option>
									</c:when>
									<c:otherwise>
										<option value="kid"><fmt:message key="labelKid"/></option>
									</c:otherwise>
								</c:choose>
							</select>
						</c:otherwise>
						</c:choose>
						</td>
					</tr>
					<tr>
						<th><label for="course"><fmt:message key="courseGroup"/> :</label></th>
						<td colspan="5" id="tdcourse">
						<c:choose>
					  	<c:when test="${empty group}">
						<select id="course" name="course" size="1">
						<c:forEach items="${courses}" var="course">>
							<option value="${course.id}"><c:out value="${course.code} - ${course.name}"/></option>
						</c:forEach>
						</select>
						</c:when>
						<c:otherwise>
							<input type="text" readonly value="${group.course.name}"/>
						</c:otherwise>
						</c:choose>
						</td>
					</tr>
				</table>
				<c:choose>
			  	<c:when test="${empty group}">
					<input id="savebutton" type="submit" value="<fmt:message key="addButton"/>"/>
				</c:when>
				<c:otherwise>
					<input id="savebutton" type="submit" value="<fmt:message key="buttonSave"/>"/>
				</c:otherwise>
				</c:choose>					
			</fieldset>
		</form>
		
		<div id="divassigntutor" style="display:none;">
			<form id="aasignTutorForm" name="aasignTutorForm" method="post">
			  <fieldset>
				  <legend><fmt:message key="labelAssignTutorTitle"/></legend>
					<p><fmt:message key="labelNonAssignedTutors"/>&nbsp;
					<select id="assignTutorId" name="assignTutorId">
				   	</select>
				   	<input type="button" name="submitAssignation" value="<fmt:message key="buttonAssignTutor"/>" onclick="javascript:assignTutor();">
				   	<input type="button" name="cancel" value="<fmt:message key="buttonCancel"/>" onclick="javascript:closeAssignTutor();">
			  	</p>
			  </fieldset>
			</form>
		</div>

		<div id="divassignlearner" style="display:none;">
			<form id="registerStudentForm" name="registerStudentForm" method="post">
			  <fieldset>
			  <legend><fmt:message key="labelRegisterStudentTitle"/></legend>
				<p><fmt:message key="labelNonRegisteredStudents"/>&nbsp;
				   <select id="registerStudentId" name="registerStudentId">
				   </select>
				   <input type="button" name="submitRegister" value="<fmt:message key="buttonRegisterStudent"/>" onclick="javascript:registerStudent();">
				   <input type="button" name="cancel" value="<fmt:message key="buttonCancel"/>" onclick="javascript:closeAssignLearner();">
			  	</p>
			  </fieldset>
			</form>
		</div>

		<!-- maybe editing -->	
		<c:choose>
			<c:when test="${!empty group}">
				<div id="divtutors">
			</c:when>
			<c:otherwise>
				<div id="divtutors" style="display:none;">
			</c:otherwise>
		</c:choose>
		
			<table id="userstable" class="tabladatos">
				<col width="15%"/>
				<col width="40%"/>
				<col width="25%"/>
				<col width="15%"/>
				<col width="5%"/>
				<tr class="noresalt">
					<th colspan="5" style="background-color: transparent">
						<table class="tabs" cellspacing="0" width="100%">
							<col width="0*"/>
							<col width="*"/>
							<tr class="noresalt">
								<th id="tutorsTab" class="selected"><a href="javascript:showTutors()"/>
									<fmt:message key="headerGroupTutors"/>
								</a></th>
								<th id="learnersTab"><a href="javascript:showLearners()"/>
									<fmt:message key="headerGroupLearners"/>
								</a></th>
								
								<td style="text-align:right">
									<input type="button" id="assignbutton" value="<fmt:message key="buttonAssignNewTutor"/>" onclick="javascript:openAssignTutor();"/>
								</td>								
							</tr>
						</table>
					</th>
			  	</tr>
			  <tr>
				<th><center><a href="javascript:sortUsers(compPersId)"><fmt:message key="headerStListPersId"/></a></center></th>	
				<th><a href="javascript:sortUsers(compSurname)"><fmt:message key="headerStListSurname"/></a></th>	
				<th><a href="javascript:sortUsers(compName)"><fmt:message key="headerStListName"/></a></th>
				<th><center><a href="javascript:sortUsers(compUserName)"><fmt:message key="headerStListUserName"/></a></center></th>
				<th>&nbsp;</th>
			  </tr>
			 <tbody id="userstabletbody">
			 	<c:if test="${empty group}">
				  <tr>
				    <td align="center" colspan="5"><fmt:message key="noAvailableTutors"/></td>
				  </tr>	 		
			 	</c:if>	 		 			 
			</tbody>
			</table>
		</div>

	</div>
	
	</body>
</html>