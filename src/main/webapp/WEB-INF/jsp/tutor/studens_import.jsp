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
  	
  	<script type='text/javascript'>
  	var imported = false;
		function importStudents(){
			var encontrado = false;
			for(var i=0;i<document.getElementsByName('studentSelect').length;i++){
				if(document.getElementsByName('studentSelect')[i].checked){
					encontrado = true;
					break;
				}
			}
			if(encontrado)
			if(confirm("<fmt:message key="confirmImportStudents"/>")){
				iTestLockPage();
				imported=true;
				StudentListMgmt.importStudents(updateUserTable);
			}
		}

		function IsNumeric(input) {    
  	  		return (input - 0) == input && input.length > 0; 
  	  	}
  	
		function getStudents(){
			var id = document.getElementById('registerStudentId').value;
			if(IsNumeric(id)){
				iTestLockPage();
				StudentListMgmt.getStudents(id,${group.id},updateUserTable);
			}
		}

		function updateUserTable(students){
			tbodyelement=document.createElement('tbody');
			tbodyelement.setAttribute("id","studentstabletbody");
			 var rowelement, tbodyelement, student, cellelement;
			
			for(var i=0;i<students.length;i++){
				var student = students[i];
				rowelement = document.createElement('tr');
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
   				cellelement.setAttribute("id","userName"+student.id);
			    cellelement.innerHTML = student.userName;
   				cellelement.setAttribute("align","center");
				rowelement.appendChild(cellelement);

				// Email of the student
				cellelement = document.createElement('td');
   				cellelement.setAttribute("id","email"+student.id);
				if (student.email != null) {
 				   cellelement.innerHTML = student.email;
				}
				rowelement.appendChild(cellelement);
				
				// Control element: unregister student
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");				
				cellelement.innerHTML = "<input type=\"checkbox\" id=\""+student.id+"\" name=\"studentSelect\" onclick=\"javascript:selecStudent('"+student.id+"')\"/>";
				rowelement.appendChild(cellelement);
				
				// Add row
				tbodyelement.appendChild(rowelement);
			}

			if (students.length == 0) {
			       rowelement = document.createElement('tr');
			       cellelement = document.createElement('td');
			       cellelement.colSpan=7;
			       cellelement.setAttribute("align","center");
				   cellelement.innerHTML = "<fmt:message key="confirmedNoStudentSelected"/>";
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
				// Hides the div to avoid double-click
				iTestUnlockPage();
				if(imported){
					alert("<fmt:message key="confirmedImportStudents"/>");
					imported=false;
				}
		}

		// Orders the list of students considering the argument:
		function runOrderBy(orderby) {
			// Div to avoid double-click
    		iTestLockPage();
    		// Order student list using Ajax and update list (callback: updateStudentList)
    		StudentListMgmt.orderStudents(orderby,document.getElementById('registerStudentId').value,updateUserTable);
		} // runOrderBy


		function selecStudent(id){
			StudentListMgmt.selectStudent(id,document.getElementById(id).checked);
		}

		function selecAllStudent(){
			var students = document.getElementsByName('studentSelect');
			for(var i=0;i<students.length;i++){
				if(students[i].checked != document.getElementById('selectAll').checked){
					students[i].checked = document.getElementById('selectAll').checked;
					selecStudent(students[i].id);
				}
			}
		}

		function reverseSelection(){
			var students = document.getElementsByName('studentSelect');
			for(var i=0;i<students.length;i++){
				students[i].checked = !students[i].checked;
				selecStudent(students[i].id);
				
			}
		}

		function resetValues(){
			document.getElementById("studentstabletbody").innerHTML="<td align=\"center\" colspan=\"9\"><fmt:message key="confirmedNoStudentSelected"/></td>";
		}
  	</script>
  	
<div id="contenido">
		
			<div>
				  <fieldset style="font-size:16px;">
				  <legend><fmt:message key="textGroupsList"/></legend>
				  <form action="return false;">
					<p><fmt:message key="textGroupsList"/>&nbsp;
					   <select id="registerStudentId" name="registerStudentId" onchange="resetValues();">
						     <c:choose>
								<c:when test="${!empty gplist}">
									<c:forEach items="${gplist}" var="group">
										<option value="${group.id}">
											<c:out value="${group.name}"/>, <c:out value="${group.course.name}"/> (<c:out value="${group.year}"/>)
										</option>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<option value=""><fmt:message key="noNonRegisteredStudents"/></option>
								</c:otherwise>
							 </c:choose>
						  </select><br/><br/><br/>
					      <input type="button" name="submitRegister" value="<fmt:message key="buttonSelectGroup"/>" onclick="javascript:getStudents();">
					      <input type="reset" value="<fmt:message key="msgbuttonReset"/>" onclick="javascript:resetValues();"/>
					      <input type="button" name="submitRegister" value="<fmt:message key="buttonImportStudents"/>" onclick="javascript:importStudents();">
					  </p>
				  </form>
				  </fieldset>
  			</div>
  			
  			<fieldset>
					  <legend id="labelEditStudent"><fmt:message key="labelEditStudentTitle"/></legend>
  			<div>
			<table id="studentstable" class="tabladatos">
			  <tr>
				<th><center><a href="javascript:runOrderBy('persId');"><fmt:message key="headerStListPersId"/></a></center></th>	
				<th><a href="javascript:runOrderBy('surname');"><fmt:message key="headerStListSurname"/></a></th>	
				<th><a href="javascript:runOrderBy('name');"><fmt:message key="headerStListName"/></a></th>
				<th><center><a href="javascript:runOrderBy('userName');"><fmt:message key="headerStListUserName"/></a></center></th>
				<th><center><fmt:message key="headerStListEmail"/></center></th>				
				<th style="width:10%"><center><input type="button" name="submitRegister" value="<fmt:message key="reverseSelection"/>" onclick="javascript:reverseSelection();"></center></th>
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
					<input type="checkbox" id="${student.id}" name="studentSelect" onclick="javascript:selecStudent('${student.id}')"/>
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
				    <td align="center" colspan="9"><fmt:message key="confirmedNoStudentSelected"/></td>
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
			</fieldset>
 </div>