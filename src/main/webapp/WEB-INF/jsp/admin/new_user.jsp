<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>
<%@ page import="com.cesfelipesegundo.itis.model.Institution" %>
<%@ page import="com.cesfelipesegundo.itis.model.User" %>


<c:choose>
<c:when test="${empty institution}">
	<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
		<jsp:param name="userRole" value="admin"/>
		<jsp:param name="menu" value="admin"/>
	</jsp:include>
</c:when>
<c:otherwise>
	<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
		<jsp:param name="userRole" value="admin"/>
		<jsp:param name="menu" value="institution"/>
	</jsp:include>
</c:otherwise>
</c:choose>

	<!-- Ajax for users -->
	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminGroupMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminUserMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>

    <script type="text/javascript">
    
    	

        function deleteGroup(idGroup){
        	if(confirm("<fmt:message key="unAssignUserFromGroup"/>")){
            	var idTutor = document.getElementById("editUserId").value;
	        	AdminGroupMgmt.unAssignUserGroup(idTutor,idGroup,{timeout:callBackTimeOut,
					 errorHandler:function(message) { iTestUnlockPage('error');} });
	        	showGroups();
            }
        }
        
		// Callback function for saveUser.
		function callbckSaveUser(res) {
			// Hide div to avoid double-click
			iTestUnlockPage();
			// res is a boolean. False when the user is used.
			if (!res) {
				alert("<fmt:message key="msgUserNameAlreadyUsed"/>");
			} else {
				alert("<fmt:message key="msgUserSaved"/>");
				// Everything OK, if adding resets the form:
				<c:choose>
					<c:when test="${empty editUser}">document.getElementById("newUserForm").reset();</c:when>
				</c:choose>
			}
		} // callbckSaveUser


		// Function to add a new user or save the user data into the database. Takes the data from newUserForm form
		function saveUser(userId) {
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
			var passwd;
			
			if (userId != '') {
				// Edit user, edit password
				passwd = document.getElementById("editPasswd").value;
				isEdit = document.getElementById("isEdit").value;
				if((isEdit!='edit'))
					if ((passwd.length == 0) || (passwd.length > 32)) {
				       alert("<fmt:message key="cannotSaveStudent"/>\n\n<fmt:message key="msgPasswdError"/>");
				       return;
				    }		    

			} else {
				// New user, new password
				passwd = document.getElementById("newPasswd").value;
				if ((passwd.length == 0) || (passwd.length > 32)) {
			       alert("<fmt:message key="cannotSaveStudent"/>\n\n<fmt:message key="msgPasswdError"/>");
			       return;
			    }	
			}	    
		    // Role
		    var newRole = document.getElementById("newRole").value;
			// Div to avoid double-click
			iTestLockPage();
			// Add user using Ajax and update list (callback: callbckSaveUser)
			// UserId == "" means new user. Any other thing means save user.
			AdminUserMgmt.saveUser(userId,newPersId,newSurname,newName,newEmail,newUserName,passwd,newRole,{callback:callbckSaveUser,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
			
							
    				
		} // saveUser


		// Callback function for checking the username
		function checkErrorUserName(res) {
			// res is a boolean
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
			// Ajax: checking username
			AdminUserMgmt.checkNewUserName(newUserName,{callback:checkErrorUserName,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
			
		} // checkNewUserName


		// Shows or hides the editPassword element
		function editNewPass(checkElem) {
			if (checkElem.checked) {
			   document.getElementById("editPasswd").disabled=false;
			} else {
			   document.getElementById("editPasswd").disabled=true;
			}
		} // editNewPass

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
		function showGroups(){
			// Obtain the query string
			var queryString = window.top.location.search.substring(1);
			// Obtain the course id value from the query string
			var ID = getParameter(queryString,"iduser");
			// Checks if the ID param is in the query string (If not we are creating a new course)
			var id = document.getElementById('userId').value;
			if(ID == "null" && id != null){
				ID = id;
			}
			if (ID != "null")
				AdminUserMgmt.getOrderedGroups(ID,"",{callback:updateDataTable,
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
				groupID = element.id;
		        
				rowelement = document.createElement('tr');
				
	        	// Institution's name
				cellelement = document.createElement('td');
   				cellelement.setAttribute("id","institution"+element.id);
			   	cellelement.innerHTML = element.institution.name;
				rowelement.appendChild(cellelement);

				// Course's name 
				cellelement = document.createElement('td');
   				cellelement.setAttribute("id","course"+element.course.name);
   				cellelement.setAttribute("align","center");
			   	cellelement.innerHTML = element.course.name;
				rowelement.appendChild(cellelement);

				// Group's name 
				cellelement = document.createElement('td');
   				cellelement.setAttribute("id","group"+element.id);
   				cellelement.setAttribute("align","center");
			   	cellelement.innerHTML = element.name;
				rowelement.appendChild(cellelement);
				
				// Group's Year
				cellelement = document.createElement('td');
   				cellelement.setAttribute("id","year"+element.id);
			   	cellelement.innerHTML = element.year;
				rowelement.appendChild(cellelement);
				
				
				// Group's option buttons
				cellelement = document.createElement('td');
   				cellelement.setAttribute("id","group"+element.id);
   				cellelement.setAttribute("align","center");
			   	cellelement.innerHTML = "<a href=\"institution.itest?method=editGroup&idgroup=${'"+element.id+"'}&idinstitution=${'"+element.institution.id+"'}\">" +
				   	"<img src=\"${pageContext.request.contextPath}/imagenes/editar.gif\" alt=\"<fmt:message key="labelEditGroup"/>\" title=\"<fmt:message key="labelEditGroup"/>\" border=\"none\"></a>";
				rowelement.appendChild(cellelement);
				
				cellelement = document.createElement('td');
   				cellelement.setAttribute("id","group"+element.id);
   				cellelement.setAttribute("align","center");
			   	cellelement.innerHTML = "<a href=\"javascript:deleteGroup('"+element.id+"');\">"+
					"<img src=\"${pageContext.request.contextPath}/imagenes/borrar.gif\" alt=\"<fmt:message key="labelDeleteGroup"/>\" title=\"<fmt:message key="labelDeleteGroup"/>\" border=\"none\"></a>";
				rowelement.appendChild(cellelement);
								
				// Add row
				tbodyelement.appendChild(rowelement);
			
				//Count number of elements
				contador++;
		}
			
			// No related Information: present message
		    if (list.length == 0) {
		       	rowelement = document.createElement('tr');
		       	cellelement = document.createElement('td');
		       	cellelement.colSpan=6;
		       	cellelement.setAttribute("align","center");
	       		cellelement.innerHTML = "<fmt:message key="noUserRelatedData"/>";
			   	rowelement.appendChild(cellelement);
			   	tbodyelement.appendChild(rowelement);
		    } else {
		    	rowelement = document.createElement('tr');
				cellelement = document.createElement('td');
				cellelement.colSpan=6;
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
		 *	AJAX call, asks for the list of groups ordered by group name and changes the html swaping the order mode
		 *	@reverse if its empty, the lists is ordered normaly, if not it's order is reversed
		 */
		function sortGroups(reverse){
			// Obtain the query string
			var queryString = window.top.location.search.substring(1);
			// Obtain the course id value from the query string
			var ID = getParameter(queryString,"iduser");
			// Checks if the ID param is in the query string (If not we are creating a new course)
			if (ID != "null")
				// Reverse order
				if (reverse != null){
					// Change the link to de function, we swap methods
					document.getElementById('groupOrder').setAttribute("href","javascript:sortGroups()");
					// AJAX call
					AdminUserMgmt.getOrderedGroups(ID,"groupreverse",{callback:updateDataTable,
						 timeout:callBackTimeOut,
						 errorHandler:function(message) { iTestUnlockPage('error');} });
					
				}else{
					// Change the link to de function, we swap methods
					document.getElementById('groupOrder').setAttribute("href","javascript:sortGroups('reverse')");
					// AJAX call
					AdminUserMgmt.getOrderedGroups(ID,"group",{callback:updateDataTable,
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
			var ID = getParameter(queryString,"iduser");
			// Checks if the ID param is in the query string (If not we are creating a new course)
			if (ID != "null")
				if (reverse != null){
					// Change the link to de function, we swap methods
					document.getElementById('institutionOrder').setAttribute("href","javascript:sortInstitutions()");
					// AJAX call
					AdminUserMgmt.getOrderedGroups(ID,"institutionreverse",{callback:updateDataTable,
						 timeout:callBackTimeOut,
						 errorHandler:function(message) { iTestUnlockPage('error');} });
				}else{
					// Change the link to de function, we swap methods
					document.getElementById('institutionOrder').setAttribute("href","javascript:sortInstitutions('reverse')");
					// AJAX call
					AdminUserMgmt.getOrderedGroups(ID,"institution",{callback:updateDataTable,
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
			var ID = getParameter(queryString,"iduser");
			// Checks if the ID param is in the query string (If not we are creating a new course)
			if (ID != "null")
				if (reverse != null){
					// Change the link to de function, we swap methods
					document.getElementById('yearOrder').setAttribute("href","javascript:sortYears()");
					// AJAX call
					AdminUserMgmt.getOrderedGroups(ID,"yearreverse",{callback:updateDataTable,
						 timeout:callBackTimeOut,
						 errorHandler:function(message) { iTestUnlockPage('error');} });
				}else{
					// Change the link to de function, we swap methods
					document.getElementById('yearOrder').setAttribute("href","javascript:sortYears('reverse')");
					// AJAX call
					AdminUserMgmt.getOrderedGroups(ID,"year",{callback:updateDataTable,
						 timeout:callBackTimeOut,
						 errorHandler:function(message) { iTestUnlockPage('error');} });
				}
		}

		/**
		 *	AJAX call, asks for the list of groups ordered by year and changes the html swaping the order mode
		 *	@reverse if its empty, the lists is ordered normaly, if not it's order is reversed
		 */
		function sortCourses(reverse){
			// Obtain the query string
			var queryString = window.top.location.search.substring(1);
			// Obtain the course id value from the query string
			var ID = getParameter(queryString,"iduser");
			// Checks if the ID param is in the query string (If not we are creating a new course)
			if (ID != "null")
				if (reverse != null){
					// Change the link to de function, we swap methods
					document.getElementById('courseOrder').setAttribute("href","javascript:sortCourses()");
					// AJAX call
					AdminUserMgmt.getOrderedGroups(ID,"coursereverse",{callback:updateDataTable,
						 timeout:callBackTimeOut,
						 errorHandler:function(message) { iTestUnlockPage('error');} });
				}else{
					// Change the link to de function, we swap methods
					document.getElementById('courseOrder').setAttribute("href","javascript:sortCourses('reverse')");
					// AJAX call
					AdminUserMgmt.getOrderedGroups(ID,"course",{callback:updateDataTable,
						 timeout:callBackTimeOut,
						 errorHandler:function(message) { iTestUnlockPage('error');} });
				}
		}

	</script>

	<div id="contenido">
		<%-- Edition or addition of user --%>
			<form id="newUserForm" name="newUserForm" method="post" action="javascript:saveUser('<c:out value="${editUser.id}"/>');">
				  <fieldset>
				  <legend id="data"><fmt:message key="data"/></legend>
				  	<table width="100%">
				  		<tr>
							  <td><fmt:message key="personalID"/></td><td><input id="newPersId" type="text" name="newPersId" size="10" width="9" value="<c:out value="${editUser.persId}"/>"/></td>
					  		  <td><fmt:message key="surname"/></td><td><input id="newSurname" type="text" name="newSurname" size="20" width="20" value="<c:out value="${editUser.surname}"/>"/></td>
					  		  <td><fmt:message key="name"/></td><td><input id="newName" type="text" name="newName" size="20" width="20" value="<c:out value="${editUser.name}"/>"/></td>
				  		</tr>
				  		<tr>
  					  		  <td><fmt:message key="user"/></td>
  					  		  <td>
								<c:choose>
  					  		  		<c:when test="${!empty editUser}">
  					  		  		   <input id="newUserName" type="text" name="newUserName" size="20" width="10" value="<c:out value="${editUser.userName}"/>" disabled/>
  					  		  		</c:when>
  					  		  		<c:otherwise>
  					  		  		   <input id="newUserName" type="text" name="newUserName" size="20" width="10"/>
  					  		  		</c:otherwise>
  					  		  	</c:choose>
  					  		  	<c:choose>
  					  		  		<c:when test="${!empty editUser}">
  					  		  		   <input id="isEdit" type="hidden" value="edit"/>
  					  		  		</c:when>
  					  		  		<c:otherwise>
  					  		  		   <input id="isEdit" type="hidden" value="no edit"/>
  					  		  		</c:otherwise>
  					  		  	</c:choose>		
  					  		  </td>
  					  		  <td><fmt:message key="passwd"/></td>
  					  		  <td>
  					  		  	<c:choose>
  					  		  		<c:when test="${!empty editUser}">
  					  		  		   <input type="hidden"  id="editUserId" value="${editUser.id}" />
  					  		  		   <input id="checkNewPass" type="checkbox" onclick="javascript:editNewPass(this);"/>&nbsp;&nbsp;<input id="editPasswd" type="password" name="editPasswd" size="10" width="32" disabled />
  					  		  		</c:when>
  					  		  		<c:otherwise>
	  					  		  	   <input id="newPasswd" type="password" name="newPasswd" size="10" width="32"/>
	  					  		  	</c:otherwise>
	  					  		</c:choose>
  					  		  </td>
					  		  <td><fmt:message key="email"/></td><td><input id="newEmail" type="text" name="newEmail" size="20" width="40" value="<c:out value="${editUser.email}"/>"/></td>
						</tr>
						<tr>
  					  		  <td><fmt:message key="role"/></td>
  					  		  <td>
								  <select id="newRole" name="newRole">
									  <%-- THE VALUES ARE CONSTANTS OF THE PROJECT --%>
									  <%-- Maybe editing --%>
									  <c:choose>
										  <c:when test="${!empty editUser and editUser.role eq 'TUTOR'}">
										  	  <option value="TUTOR" selected><fmt:message key="tutorRoleOption"/></option>
										  </c:when>
										  <c:otherwise>
										      <option value="TUTOR"><fmt:message key="tutorRoleOption"/></option>
										  </c:otherwise>
									  </c:choose>
									  <c:choose>
										  <c:when test="${!empty editUser and editUser.role eq 'LEARNER'}">
										  	  <option value="LEARNER" selected><fmt:message key="learnerRole"/></option>
										  </c:when>
										  <c:otherwise>
											  <option value="LEARNER"><fmt:message key="learnerRole"/></option>
										  </c:otherwise>
									  </c:choose>
									  <c:choose>
										  <c:when test="${!empty editUser and editUser.role eq 'KID'}">
										  	  <option value="KID" selected><fmt:message key="kidRole"/></option>
										  </c:when>
										  <c:otherwise>
											  <option value="KID"><fmt:message key="kidRole"/></option>
										  </c:otherwise>
									  </c:choose>
								  </select>
							  </td>
							  <td align="center" colspan="4">
							      <input id="buttonAddStudent" type="submit" name="submitAddUser" value="<fmt:message key="saveButton"/>" />
							      <%-- Maybe adding a user --%>
							      <c:choose>
									  <c:when test="${empty editUser}">
									      &nbsp;&nbsp;<input type="button" name="checkUserName" value="<fmt:message key="buttonCheckUserName"/>" onclick="javascript:checkNewUserName();">
									  </c:when>
								  </c:choose>
								  &nbsp;&nbsp;<input id="buttonCancel" type="button" name="buttonCancel" value="<fmt:message key="buttonCancel"/>" onclick="javascript:window.location='${pageContext.request.contextPath}/admin/institution.itest?method=indexInstitution';" />
							  </td>
					    </tr>
					</table>
				  </fieldset>
			</form>
			
			<!-- ---Gonzalo Modification Start-- -->
			<!-- Add new section with a list to show related information about the user shown-->
			<!-- groups of the user, with information of courses and institutions of those groups-->
			<c:choose>
				<c:when test="${!empty editUser and editUser.role eq 'TUTOR' or editUser.role eq 'TUTORAV' or editUser.role eq 'KID' or editUser.role eq 'LEARNER'}">
					<table id="groupstable" class="tabladatos">
						<col width="10%"/>
						<col width="*"/>
						<col width="0*"/>
						<col width="0*"/>
						<col width="0*"/>
						<col width="0*"/>	
		
						<tr>
							<th><center><a id="institutionOrder" href="javascript:sortInstitutions()"><fmt:message key="textInstitutionsList"/></a></center></th>
							<th><center><a id="courseOrder" href="javascript:sortCourses()"><fmt:message key="headerGroupsListCourse"/></a></center></th>
							<th><center><a id="groupOrder" href="javascript:sortGroups()"><fmt:message key="textGroupsList"/></a></center></th>
							<th><center><a id="yearOrder" href="javascript:sortYears()"><fmt:message key="yearGroup"/></a></center></th>
							<th>&nbsp;</th>
							<th>&nbsp;</th>
					  	</tr>
						
			
			
						<tbody id="datatabletbody">
									 
						 	<c:if test="${empty group}">
							  <tr>
							    <td id="lastrow" align="center" colspan="5"><fmt:message key="noUserRelatedData"/></td>
							  </tr>
						 	</c:if>	 		 			 
						</tbody>
					</table>
				</c:when>
			</c:choose>
	</div>
	<c:if test="${!empty editUser}">
				  	<input type="hidden" value="${editUser.id}" id="userId" name="userId"/>
				  </c:if>

    	<c:choose>
			<c:when test="${!empty editUser and editUser.role eq 'TUTOR' or editUser.role eq 'TUTORAV' or editUser.role eq 'KID' or editUser.role eq 'LEARNER'}">
    			<script type="text/javascript">showGroups();</script>
    		</c:when>
    	</c:choose>
  </body>
</html>