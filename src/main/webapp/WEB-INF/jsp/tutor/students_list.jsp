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
	breadCrumb.addBundleStep("studentList","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="tutor"/>
	<jsp:param name="menu" value="tutor"/>
</jsp:include>


	<!-- Ajax for student list -->
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/StudentListMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>

    <script type="text/javascript">
		      
		// Updates the list of students after deleting or sorting students:
		function updateStudentList(students) {
		  /*
		     This is a callback function that updates the information about the answers already saved (answerstable)
		     with the answer recently just saved.
		  */
  		  var rowelement, tbodyelement, student, cellelement, datatable;
  		  
		   tbodyelement=document.createElement('tbody');
		   tbodyelement.setAttribute("id","studentstabletbody");

		   // Fills the table (DOM scripting): answers data ---------------
		   var position = 0;
		   
		   while (position < students.length) {
		        student = students[position];
		        
				rowelement = document.createElement('tr');

				// Personal ID of the student (DNI in Spain)
				cellelement = document.createElement('td');
				if (student.persId == null) {
				   cellelement.innerHTML = "&nbsp;";
				} else {
			       cellelement.innerHTML = student.persId;
			    }
   				cellelement.setAttribute("align","center");
   				cellelement.setAttribute("id","persId"+student.id);
				rowelement.appendChild(cellelement);
				
				// Surname of the student
				cellelement = document.createElement('td');
   				cellelement.setAttribute("id","surname"+student.id);
			    cellelement.innerHTML = student.surname;
				rowelement.appendChild(cellelement);

				// Name of the student
				cellelement = document.createElement('td');
   				cellelement.setAttribute("id","name"+student.id);
			    cellelement.innerHTML = student.name;
				rowelement.appendChild(cellelement);

				// Username of the student
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
   				cellelement.setAttribute("id","userName"+student.id);
			    cellelement.innerHTML = student.userName;
   				cellelement.setAttribute("align","center");
				rowelement.appendChild(cellelement);

				// Email of the student
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
   				cellelement.setAttribute("id","email"+student.id);
				if (student.email != null) {
 				   cellelement.innerHTML = student.email;
				}
				rowelement.appendChild(cellelement);
					
				// Control element: edit student
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				cellelement.innerHTML = "<a href=\"javascript:editStudent('"+student.id+"','"+student.role+"');\"><img src=\"${pageContext.request.contextPath}/imagenes/editar.gif\" alt=\"<fmt:message key="labelEditStudent"/>\" title=\"<fmt:message key="labelEditStudent"/>\" border=\"none\"></a>";
				rowelement.appendChild(cellelement);
				
				// Control element: unregister student
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");				
				cellelement.innerHTML = "<a href=\"javascript:unregisterStudent('"+student.id+"');\"><img src=\"${pageContext.request.contextPath}/imagenes/borrar.gif\" alt=\"<fmt:message key="labelUnregisterStudent"/>\" title=\"<fmt:message key="labelUnregisterStudent"/>\" border=\"none\"></a>";
				rowelement.appendChild(cellelement);
				
				// Add row
				tbodyelement.appendChild(rowelement);
				
				position++;
			} // while
			
			// No students: present message
		    if (students.length == 0) {
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=7;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML = "<fmt:message key="noAvailableStudents"/>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		    } else {
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=7;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML ="<hr>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=7;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML ="<b><fmt:message key="totalLabel"/> "+students.length+"</b>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		    }
		
		    // Gets the datatable
		    datatable=document.getElementById("studentstable");
			// Replaces tbody			
			datatable.replaceChild(tbodyelement,document.getElementById("studentstabletbody"));
			
	   		// All elements are cleared:
    		cancelAddStudentData();
    		cancelEditStudentData();
    		// Hides the div to avoid double-click
			iTestUnlockPage('');
			if(register!=null)
				if(register){
	    			document.getElementById("registerStudentId").options[document.getElementById("registerStudentId").selectedIndex]=null;
				}else{
					StudentListMgmt.getUnRegisteredStudent({callback:repaintSelect,
		 				  timeout:callBackTimeOut,
						  errorHandler:function(message) { iTestUnlockPage('error');} });
					  }

		} // updateStudentList

		function repaintSelect(students){
			var select = document.getElementById("registerStudentId");
			//Eliminamos los alumnos dentro del select
			for(var j=0;j<select.length;j++){
				select.remove(j);
			} 
			//Añadimos los alumnos que nos proporciona el servidor
			for(var i=0;i<students.length;i++){
				var student = students[i];
				var option = new Option(student.surname+', '+student.name+' ('+student.userName+')',student.id);
				select.options[i]=option;
			}
		}
		
		var reverse = false;
		// Orders the list of students considering the argument:
		function runOrderBy(orderby) {
			// Div to avoid double-click
    		iTestLockPage('');
    		// Order student list using Ajax and update list (callback: updateStudentList)
    		StudentListMgmt.runOrderBy(orderby,reverse,{callback:updateStudentList,
 				  timeout:callBackTimeOut,
				  errorHandler:function(message) { iTestUnlockPage('error');} });
    		reverse= !reverse;
		} // runOrderBy
		
		var register;
		// Function that registers an student in the current group
		function registerStudent() {
		   	// Take the id of the student from the select
    		var stdId = document.getElementById("registerStudentId").value;
    		register = true;
    		// The student is registered only if there is an id
    		if (stdId != "") {
    			// Div to avoid double-click
    			iTestLockPage('');
    			// Register using Ajax and update list (callback: updateStudentList)
    			StudentListMgmt.registerStudent(stdId,{callback:updateStudentList,
	   				  timeout:callBackTimeOut,
					  errorHandler:function(message) { iTestUnlockPage('error');} });
    		}
    		
		}  // registerStudent

		var stdIdGlobal;
		// Function that un-registers an student from the current group
		function unregisterStudent(stdId) {
		  register = false;
		  var userName = document.getElementById('userName'+stdId).innerHTML;
          var conf = confirm ("<fmt:message key="infoUnregStud"/> "+userName+"\n\n<fmt:message key="confirmUnregStud"/>\n\n<fmt:message key="alertUnregStud"/>");
		  stdIdGlobal = stdId;
		  if (conf) {
   			 // Div to avoid double-click
   			 iTestLockPage('');
   			 // Un-Register using Ajax and update list (callback: updateStudentList)
   			 StudentListMgmt.unRegisterStudent(stdId,{callback:updateStudentList,
									   				  timeout:callBackTimeOut,
													  errorHandler:function(message) { iTestUnlockPage('error');}});
   		  }
		}  // unregisterStudent


	    // Function that hides the div to add a new student and resets the form
    	function cancelAddStudentData() {
    		// Hide div
    		document.getElementById("divAddStudentForm").style.display = "none";
    		// Reset Form
    		document.getElementById("newStudentForm").reset();
    	} // cancelAddStudentData	
	

	    // Function that hides the div to edit a new student and resets the form
    	function cancelEditStudentData() {
    		// Hide div
    		document.getElementById("divEditStudentForm").style.display = "none";
    		// Reset Form
    		document.getElementById("editStudentForm").reset();
			// Disable password field:
			document.getElementById("editPasswd").disabled = true;
    	} // cancelEditStudentData
    	

	    // Function that shows the div to add a new student
    	function showAddStudent() {
    		// First, all elements are cleared:
    		cancelAddStudentData();
    		// Show the div
    		document.getElementById("divAddStudentForm").style.display = "block";
    	} // showAddStudent


		// Shows or hides the editPassword element
		function editNewPass(checkElem) {
			if (checkElem.checked) {
			   document.getElementById("editPasswd").disabled=false;
			} else {
			   document.getElementById("editPasswd").disabled=true;
			}
		} // editNewPass


	    // Function that shows the div and form to edit a new student
    	function editStudent(stdId,stdRole) {
	   		// First, all elements are cleared:
    		cancelEditStudentData();
    		register = null;
    		// Then, the elements are filled using stdId:
    		document.getElementById("editPersId").value = document.getElementById("persId"+stdId).innerHTML;
    		document.getElementById("editSurname").value = document.getElementById("surname"+stdId).innerHTML;
    		document.getElementById("editName").value = document.getElementById("name"+stdId).innerHTML;
    		document.getElementById("editUserName").value = document.getElementById("userName"+stdId).innerHTML;
    		document.getElementById("editEmail").value = document.getElementById("email"+stdId).innerHTML;
    		<c:if test="${!(group.studentType eq 1 || group.studentType eq 2)}">
    		if (stdRole == "LEARNER") {
    		   document.getElementById("editStudentRole").options[0].selected = true;
    		}
    		if (stdRole == "KID") {
    		   document.getElementById("editStudentRole").options[1].selected = true;
    		}
    		</c:if>
    		// States the id into the hidden element:
    		document.getElementById("editStudentId").value = stdId;
    		
    		// Shows the div
    		document.getElementById("divEditStudentForm").style.display = "block";
    	} // editStudent


		// Callback function for saveStudent.
		function callbckSaveStudent(res) {
			// Hide div to avoid double-click
			iTestUnlockPage();
			// res is a boolean. False when the user is used.
			if (!res) {
				alert("<fmt:message key="msgUserNameAlreadyUsed"/>");
			} else {
				alert("<fmt:message key="msgUserSaved"/>");
				// Everything OK, shows the student lists
				// Div to avoid double-click
				iTestLockPage('');
				// Order student list using Ajax and update list (callback: updateStudentList)
	    		StudentListMgmt.runOrderBy('surname',false,{callback:updateStudentList,
	   				  timeout:callBackTimeOut,
					  errorHandler:function(message) { iTestUnlockPage('error');} });
			}
		} // callbckSaveStudent


		// Function to add a new student into the database. Takes the data from newStudentForm form
		function addStudent() {
			// Checking introduced data:
			// Personal ID
			var newPersId = document.getElementById("newPersId").value;
			if ((newPersId.length == 0) || (newPersId.length > 9)) {
		          alert("<fmt:message key="cannotSaveStudent"/>\n\n<fmt:message key="msgPersIdError"/>");
		          return;			
			}
			// Surname
			var newSurname = document.getElementById("newSurname").value;
			if ((newSurname.length == 0) || (newSurname.length > 50)) {
		          alert("<fmt:message key="cannotSaveStudent"/>\n\n<fmt:message key="msgSurnameError"/>");
		          return;			
			}
			// Name
			var newName = document.getElementById("newName").value;
			if ((newName.length == 0) || (newName.length > 20)) {
		       alert("<fmt:message key="cannotSaveStudent"/>\n\n<fmt:message key="msgNameError"/>");
		       return;
		    }
			// Email
			var newEmail = document.getElementById("newEmail").value;
			if (newEmail.length > 40) {
		       alert("<fmt:message key="cannotSaveStudent"/>\n\n<fmt:message key="msgEmailError"/>");
		       return;
		    }
			// UserName
			var newUserName = document.getElementById("newUserName").value;
			if ((newUserName.length == 0) || (newUserName.length > 20)) {
		       alert("<fmt:message key="cannotSaveStudent"/>\n\n<fmt:message key="msgUserNameError"/>");
		       return;
		    }
			// Password
			var newPasswd = document.getElementById("newPasswd").value;
			if ((newPasswd.length == 0) || (newPasswd.length > 32)) {
		       alert("<fmt:message key="cannotSaveStudent"/>\n\n<fmt:message key="msgPasswdError"/>");
		       return;
		    }		    
		    // Student Role
		    var newStudentRole = document.getElementById("newStudentRole").value;
		    
			// Div to avoid double-click
			iTestLockPage('');
			// Add student using Ajax and update list (callback: callbckSaveStudent)
			// StudentId == "" means new student.
			StudentListMgmt.saveStudent("",newPersId,newSurname,newName,newEmail,newUserName,newPasswd,newStudentRole,{callback:callbckSaveStudent,
 				  timeout:callBackTimeOut,
				  errorHandler:function(message) { iTestUnlockPage('error');}});
    				
		} // addStudent


		// Function to save the data of the student into the database. Takes the data from editStudentForm form
		function saveStudent() {
			// Checking introduced data:
			// Personal ID
			var editPersId = document.getElementById("editPersId").value;
			if ((editPersId.length == 0) || (editPersId.length > 9)) {
		          alert("<fmt:message key="cannotSaveStudent"/>\n\n<fmt:message key="msgPersIdError"/>");
		          return;			
			}
			// Surname
			var editSurname = document.getElementById("editSurname").value;
			if ((editSurname.length == 0) || (editSurname.length > 50)) {
		          alert("<fmt:message key="cannotSaveStudent"/>\n\n<fmt:message key="msgSurnameError"/>");
		          return;			
			}
			// Name
			var editName = document.getElementById("editName").value;
			if ((editName.length == 0) || (editName.length > 20)) {
		       alert("<fmt:message key="cannotSaveStudent"/>\n\n<fmt:message key="msgNameError"/>");
		       return;
		    }
			// Email
			var editEmail = document.getElementById("editEmail").value;
			if (editEmail.length > 40) {
		       alert("<fmt:message key="cannotSaveStudent"/>\n\n<fmt:message key="msgEmailError"/>");
		       return;
		    }
			// UserName
			var editUserName = document.getElementById("editUserName").value;
			if ((editUserName.length == 0) || (editUserName.length > 20)) {
		       alert("<fmt:message key="cannotSaveStudent"/>\n\n<fmt:message key="msgUserNameError"/>");
		       return;
		    }
			// Password (maybe disabled)
			if (!document.getElementById("editPasswd").disabled) {
				var editPasswd = document.getElementById("editPasswd").value;
				if ((editPasswd.length == 0) || (editPasswd.length > 32)) {
			       alert("<fmt:message key="cannotSaveStudent"/>\n\n<fmt:message key="msgPasswdError"/>");
			       return;
			    }		   
			} 
		    // Student Role
		    var editStudentRole = document.getElementById("editStudentRole").value;
		    // Student Id
		    var editStudentId = document.getElementById("editStudentId").value;
		    
			// Div to avoid double-click
			iTestLockPage('');
			// Saves the data of the student using Ajax and update list (callback: callbckSaveStudent)
			StudentListMgmt.saveStudent(editStudentId,editPersId,editSurname,editName,editEmail,editUserName,editPasswd,editStudentRole,{callback:callbckSaveStudent,
 				  timeout:callBackTimeOut,
				  errorHandler:function(message) { iTestUnlockPage('error');}});
    				
		} // saveStudent		
		
		
		// Callback function for checking the username
		function checkErrorUserName(res) {
			// res is a boolean
			iTestUnlockPage('');
			if (!res) alert("<fmt:message key="msgUserNameAlreadyUsed"/>");
			else alert("<fmt:message key="msgUserNameAvailable"/>");
			return;
		}
		
		// Check out the user name
		function checkNewUserName() {
			// Getting the UserName
			var newUserName = document.getElementById("newUserName").value;
			if ((newUserName.length == 0) || (newUserName.length > 20)) {
		       alert("<fmt:message key="cannotSaveStudent"/>\n\n<fmt:message key="msgUserNameError"/>");
		       return;
		    }
			iTestLockPage('');
			// Ajax: checking username
			StudentListMgmt.checkNewUserName(newUserName,{callback:checkErrorUserName,
 				  timeout:callBackTimeOut,
				  errorHandler:function(message) { iTestUnlockPage('error');}});
		} // checkNewUserName
		
    </script>

	<div id="contenido">
		
			<div>
				<form id="registerStudentForm" name="registerStudentForm" method="post">
				  <fieldset style="font-size:16px;">
				  <legend><fmt:message key="labelRegisterStudentTitle"/></legend>
					<p><fmt:message key="labelNonRegisteredStudents"/>&nbsp;
					   <select id="registerStudentId" name="registerStudentId">
						     <c:choose>
								<c:when test="${!empty otherStudents}">
									<c:forEach items="${otherStudents}" var="std">
										<option value="${std.id}">
											<c:out value="${std.surname}"/>, <c:out value="${std.name}"/> (<c:out value="${std.userName}"/>)
										</option>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<option value=""><fmt:message key="noNonRegisteredStudents"/></option>
								</c:otherwise>
							 </c:choose>
						  </select>
					      <input type="button" name="submitRegister" value="<fmt:message key="buttonRegisterStudent"/>" onclick="javascript:registerStudent();">
					      &nbsp;&nbsp;<input type="button" name="submitRegister" value="<fmt:message key="buttonNewStudent"/>" onclick="javascript:showAddStudent();">
					  </p>
				  </fieldset>
				</form>
			
				<div id="divAddStudentForm" style="display:none;">
					<form id="newStudentForm" name="newStudentForm" method="post" action="javascript:addStudent();">
					  <fieldset>
  					  <legend id="labelNewStudent"><fmt:message key="labelNewStudentTitle"/></legend>
					  	<table width="100%">
					  		<tr>
							  <td><fmt:message key="labelStudentPersId"/></td><td><input id="newPersId" type="text" name="newPersId" size="10" width="9"/></td>
					  		  <td><fmt:message key="labelStudentSurname"/></td><td><input id="newSurname" type="text" name="newSurname" size="20" width="20"/></td>
					  		  <td><fmt:message key="labelStudentName"/></td><td><input id="newName" type="text" name="newName" size="20" width="20"/></td>
					  		</tr>
					  		<tr>
  					  		  <td><fmt:message key="labelStudentUserName"/></td><td><input id="newUserName" type="text" name="newUserName" size="20" width="20"/></td>
  					  		  <td><fmt:message key="labelStudentPassword"/></td><td><input id="newPasswd" type="password" name="newPasswd" size="10" width="32"/></td>
					  		  <td><fmt:message key="labelStudentEmail"/></td><td><input id="newEmail" type="text" name="newEmail" size="20" width="40"/></td>
							</tr>
							<tr>
  					  		  <td><fmt:message key="labelStudentRole"/></td>
  					  		  <td>
  					  		  	<c:choose>
  					  		  		<c:when test="${group.studentType eq 1}">
  					  		  		  <input type="text" readonly value="<fmt:message key="labelLearner"/>"/>
  					  		  		  <input type="hidden" id="editStudentRole" name="editStudentRole" value="LEARNER"/>
  					  		  		</c:when>
  					  		  		<c:when test="${group.studentType eq 2}">
									  <input type="text" readonly value="<fmt:message key="labelKid"/>"/>  					  		  			
  					  		  		  <input type="hidden" id="editStudentRole" name="editStudentRole" value="KID"/>
  					  		  		</c:when>
  					  		  		<c:otherwise>
									  <select id="newStudentRole" name="newStudentRole">
									     <option value="LEARNER" selected><fmt:message key="labelLearner"/></option>
									     <option value="KID"><fmt:message key="labelKid"/></option>
									  </select>
								  	</c:otherwise>
								</c:choose>
							  </td>
							  <td align="center" colspan="4">
							      <input id="buttonAddStudent" type="submit" name="submitAddStudent" value="<fmt:message key="buttonAddStudent"/>"/>
							      &nbsp;&nbsp;<input type="button" name="checkUserName" value="<fmt:message key="buttonCheckUserName"/>" onclick="javascript:checkNewUserName();">
							      &nbsp;&nbsp;<input type="button" name="submitCancelAddStudent" value="<fmt:message key="buttonCancelAddStudent"/>" onclick="javascript:cancelAddStudentData();" />
							  </td>
						  	</tr>
						 </table>
					  </fieldset>
					</form>
				</div>

				<div id="divEditStudentForm" style="display:none;">
					<form id="editStudentForm" name="editStudentForm" method="post">
					  <fieldset>
					  <legend id="labelEditStudent"><fmt:message key="labelEditStudentTitle"/></legend>
					  	<table width="100%">
					  		<tr>
							  <td><fmt:message key="labelStudentPersId"/></td><td><input id="editPersId" type="text" name="editPersId" size="10" width="9"/></td>
					  		  <td><fmt:message key="labelStudentSurname"/></td><td><input id="editSurname" type="text" name="editSurname" size="20" width="20"/></td>
					  		  <td><fmt:message key="labelStudentName"/></td><td><input id="editName" type="text" name="editName" size="20" width="20"/></td>
					  		</tr>
					  		<tr>
  					  		  <td title="<fmt:message key="msgCannotChangeUserName"/>"><fmt:message key="labelStudentUserName"/></td><td><input id="editUserName" type="text" name="editUserName" size="20" width="20" disabled /></td>
  					  		  <td><fmt:message key="labelStudentNewPassword"/>&nbsp;<input id="checkNewPass" type="checkbox" onclick="javascript:editNewPass(this);"/></td><td><input id="editPasswd" type="password" name="editPasswd" size="10" width="32" disabled /></td>
					  		  <td><fmt:message key="labelStudentEmail"/></td><td><input id="editEmail" type="text" name="editEmail" size="20" width="40"/></td>
					  		</tr>
					  		<tr>
  					  		  <td><fmt:message key="labelStudentRole"/></td>
  					  		  <td>
  					  		  	<c:choose>
  					  		  		<c:when test="${group.studentType eq 1}">
  					  		  		  <input type="text" readonly value="<fmt:message key="labelLearner"/>"/>
  					  		  		  <input type="hidden" id="newStudentRole" name="newStudentRole" value="LEARNER"/>
  					  		  		</c:when>
  					  		  		<c:when test="${group.studentType eq 2}">
									  <input type="text" readonly value="<fmt:message key="labelKid"/>"/>  					  		  			
  					  		  		  <input type="hidden" id="newStudentRole" name="newStudentRole" value="KID"/>
  					  		  		</c:when>
  					  		  		<c:otherwise>
		  					  		    <select id="editStudentRole" name="editStudentRole">
									     <option value="LEARNER" selected><fmt:message key="labelLearner"/></option>
									     <option value="KID"><fmt:message key="labelKid"/></option>
										</select>
								  	</c:otherwise>
								</c:choose>
							  </td>
  					  		  
							  <td align="center" colspan="4">
  							      <input id="editStudentId" type="hidden" name="editStudentId" />
							      <input id="buttonEditStudent" type="button" name="submitSaveStudent" value="<fmt:message key="buttonSaveStudent"/>" onclick="javascript:saveStudent();" />
							      &nbsp;&nbsp;<input type="button" name="submitCancelEditStudent" value="<fmt:message key="buttonCancelAddStudent"/>" onclick="javascript:cancelEditStudentData();" />
							  </td>
						  	</tr>
						 </table>
					  </fieldset>
					</form>
				</div>

			</div>			
			<div>
			<table id="studentstable" class="tabladatos">
			  <tr>
				<th><center><a href="javascript:runOrderBy('persId');"><fmt:message key="headerStListPersId"/></a></center></th>	
				<th><a href="javascript:runOrderBy('surname');"><fmt:message key="headerStListSurname"/></a></th>	
				<th><a href="javascript:runOrderBy('name');"><fmt:message key="headerStListName"/></a></th>
				<th><center><a href="javascript:runOrderBy('userName');"><fmt:message key="headerStListUserName"/></a></center></th>
				<th><center><fmt:message key="headerStListEmail"/></center></th>
				<th>&nbsp;</th>				
				<th>&nbsp;</th>
			  </tr>
			 <tbody id="studentstabletbody">
			
  			 <c:forEach items="${students}" var="student">
 			  <tr>  
 			  	<td id="persId${student.id}" align="center"><c:out value="${student.persId}"/></td>
 			  	<td id="surname${student.id}"><c:out value="${student.surname}"/></td>
			    <td id="name${student.id}"><c:out value="${student.name}"/></td>
				<td id="userName${student.id}" align="center"><c:out value="${student.userName}"/></td>
				<td id="email${student.id}" align="center"><c:out value="${student.email}"/></td>
				<!-- Controls -->
				<td align="center">
				   <a href="javascript:editStudent('${student.id}','${student.role}');"><img src="${pageContext.request.contextPath}/imagenes/editar.gif" alt="<fmt:message key="labelEditStudent"/>" title="<fmt:message key="labelEditStudent"/>" border="none"></a>
				</td>
				<td align="center">
					<a href="javascript:unregisterStudent('${student.id}');"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="labelUnregisterStudent"/>" title="<fmt:message key="labelUnregisterStudent"/>" border="none"></a>
				</td>
	<%--
				<td align="center">
				<c:choose>
					<c:when test="${!student.usedInExam}">
						<a href="javascript:deleteQuestion('${student.id}');"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="labelDeleteQuestion"/>" border="none"></a>
					</c:when>
					<c:otherwise>
						<img src="${pageContext.request.contextPath}/imagenes/forb_dot.gif" alt="<fmt:message key="cannotDeleteQuestion"/>" title="<fmt:message key="cannotDeleteQuestion"/>" border="none">
					</c:otherwise>
				</c:choose>
				</td>
      --%>
			  </tr>
 			 
 			  <form id="editq${student.id}" method="POST" action="${pageContext.request.contextPath}/tutor/managegroup.itest?method=editQuestion"><input type="hidden" name="idstudent" value="${student.id}"/></form>
			 
			 </c:forEach>
			 
			 <c:choose>
				 <c:when test="${empty students}">
				  <tr>
				    <td align="center" colspan="9"><fmt:message key="noAvailableStudents"/></td>
				  </tr>
				 </c:when>
				 <c:otherwise>
				  <tr>
				    <td align="center" colspan="9"><hr/></td>
				  </tr>
				  <tr>
				    <td align="center" colspan="9"><b><fmt:message key="totalLabel"/> ${fn:length(students)} </b></td>
				  </tr>				 
				 </c:otherwise>
			 </c:choose>
			</tbody>
			</table>
			<br/>
			</div>
		</div>
	</body>
</html>