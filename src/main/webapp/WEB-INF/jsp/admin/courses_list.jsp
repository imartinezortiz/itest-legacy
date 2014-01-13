<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>

<% 
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addBundleStep("textCoursesList","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="admin"/>
	<jsp:param name="menu" value="admin"/>
</jsp:include>

	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminCourseMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>


<script type="text/javascript">
	function findCourse(){
		codigo = document.getElementById('codFilter').value;
		nombre = document.getElementById('nameFilter').value;
		estudios = document.getElementById('studiesFilter').value;
		if(document.getElementById('levelFilter').value == "Otros"){
			curso = document.getElementById('otherLevelFilter').value;
		}else{
			curso = document.getElementById('levelFilter').value;
		}
		
		if(curso == 'null')
			curso = null;
		if(estudios =='null')
			estudios = null;
		iTestLockPage();
		AdminCourseMgmt.filterByCourse(codigo,nombre,curso,estudios,{callback:updateTable,
			 timeout:callBackTimeOut,
			 errorHandler:function(message) { iTestUnlockPage('error');} });
	}

	function updateTable(courses){
		tbodyelement=document.createElement('tbody');
		tbodyelement.setAttribute("id","coursestabletbody");
		
		for(var i=0;i<courses.length;i++){
			course = courses[i];
			rowelement = document.createElement('tr');
			rowelement.setAttribute("id","row"+course.id);
			cellelement = document.createElement('td');
			cellelement.innerHTML = course.code;
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.innerHTML = course.name;
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.innerHTML = course.studies;
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.setAttribute("align","center");
			cellelement.innerHTML = course.level;
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.setAttribute("align","center");
			cellelement.innerHTML =course.numGroups;
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.innerHTML = '<a href="${pageContext.request.contextPath}/admin/admin.itest?method=editCourse&idcourse='+course.id+'"><img src="${pageContext.request.contextPath}/imagenes/editar.gif" alt="<fmt:message key="labelEditCourse"/>" title="<fmt:message key="labelEditCourse"/>" border="none"></a>'
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.innerHTML = '<a href="${pageContext.request.contextPath}/admin/admin.itest?method=deleteCourse&idcourse='+course.id+'" onclick="return confirm (\'<fmt:message key="confirmDeleteCourse"/> <fmt:message key="alertDeleteCourse"/>\')"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="labelDeleteCourse"/>" title="<fmt:message key="labelDeleteCourse"/>" border="none"></a>'
			rowelement.appendChild(cellelement);
			
			
			tbodyelement.appendChild(rowelement);
		}
		if (courses.length == 0) {
		       if(courses!=''){
		    	   rowelement = document.createElement('tr');
			       cellelement = document.createElement('td');
				   cellelement.colSpan=6;
			       cellelement.setAttribute("align","center");
				   cellelement.innerHTML = "<fmt:message key="noAvailableConfigExams"/>";
				   rowelement.appendChild(cellelement);
				   tbodyelement.appendChild(rowelement);
			}
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
			   cellelement.innerHTML ="<b><fmt:message key="totalLabel"/> "+courses.length+"</b>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		    }
		datatable=document.getElementById("coursestable");
		// Replaces tbody			
		datatable.replaceChild(tbodyelement,document.getElementById("coursestabletbody"));
		iTestUnlockPage();
	}

	function checkTypeLevelCourse(element){
		if(element.value == "Otros"){
			document.getElementById('divOtherLevelFilter').style.display=""
		}else{
			document.getElementById('divOtherLevelFilter').style.display="none";
		}
	}
	
</script>
		<div id="contenido">
			<fieldset>
				<legend><fmt:message key="labelFilterTitle"/></legend>
				<form action="return false;">
					<div class="divDosIzq" style="text-align:right; width:20%;">
						<label><fmt:message key="code"/> :</label>
						<input type="text" id="codFilter"/> <br/><br/>
						<label><fmt:message key="name"/> :</label>
						<input type="text" id="nameFilter"/>
					</div>
					<div class="divDosDer" style="text-align:left; width:40%;">
						<label><fmt:message key="level"/> :</label>
						<select name="levelFilter" id="levelFilter" onclick="javascript:checkTypeLevelCourse(this);">
							<option value="null">----------</option>
							<option value="1º">1º</option>
							<option value="2º">2º</option>
							<option value="3º">3º</option>
							<option value="4º">4º</option>
							<option value="5º">5º</option>
							<option value="6º">6º</option>
							<option value="Otros"><fmt:message key="labelCheckOtros"/></option>
						</select>
						<br/>
						<div id="divOtherLevelFilter" style="display:none">
							<fmt:message key="labelCheckOtros"/> :	<input type="text" id="otherLevelFilter"/>
						</div>
						<br/><br/>
						<label><fmt:message key="studies"/> :</label>
						<select name="studiesFilter" id="studiesFilter">
							<option value="null">------------</option>
							<option value="Infantil"><fmt:message key="labelCheckInfantil"/></option>
							<option value="Primaria"/><fmt:message key="labelCheckPrimaria"/></option>
							<option value="Secundaria"><fmt:message key="labelCheckSecundaria"/></option>
							<option value="Ciclo Formativo"><fmt:message key="labelCheckFp"/></option>
							<option value="Bachillerato"><fmt:message key="labelCheckBachillerato"/></option>
							<option value="Universidad"><fmt:message key="labelCheckUniversidad"/></option>
						</select>
					</div>
					<div class="divcentro">
						<input type="reset" value="<fmt:message key="msgbuttonReset"/>" onclick="javascript:updateTable('');"/>
						<input type="button" value="<fmt:message key="buttonFilterRun"/>" onclick="javascript:findCourse();"/>
					</div>
				</form>
			</fieldset>
			<table id="coursestable" class="tabladatos">
			  <col width="0*"/>
			  <col width="*"/>
			  <col width="0*"/>
			  <col width="0*"/>
			  <col width="0*"/>
			  <col width="0*"/>
			  <col width="0*"/>	
			  <tr>
				<th>
					<c:choose>
						<c:when test="${orderby eq 'code' and reverse ne 'yes'}">
							<a href="?method=showCoursesList&orderby=code&reverse">
						</c:when>
						<c:otherwise>
							<a href="?method=showCoursesList&orderby=code">
						</c:otherwise>
					</c:choose>
					<fmt:message key="headerCoursesListCode"/></a>
				</th>
				<th><center>
					<c:choose>
						<c:when test="${orderby eq 'name' and reverse ne 'yes'}">
							<a href="?method=showCoursesList&orderby=name&reverse">
						</c:when>
						<c:otherwise>
							<a href="?method=showCoursesList&orderby=name">
						</c:otherwise>
					</c:choose>
					<fmt:message key="headerCoursesListName"/></a></center>
				</th>	
				<th><center>
					<c:choose>
						<c:when test="${orderby eq 'studies' and reverse ne 'yes'}">
							<a href="?method=showCoursesList&orderby=studies&reverse">
						</c:when>
						<c:otherwise>
							<a href="?method=showCoursesList&orderby=studies">
						</c:otherwise>
					</c:choose>
					<fmt:message key="headerCoursesListStudies"/></a></center>
				</th>
				<th><center>
					<c:choose>
						<c:when test="${orderby eq 'level' and reverse ne 'yes'}">
							<a href="?method=showCoursesList&orderby=level&reverse">
						</c:when>
						<c:otherwise>
							<a href="?method=showCoursesList&orderby=level">
						</c:otherwise>
					</c:choose>
					<fmt:message key="headerCoursesListLevel"/></a></center>
				</th>
				<th><center>
					<c:choose>
						<c:when test="${orderby eq 'numGroups' and reverse ne 'yes'}">
							<a href="?method=showCoursesList&orderby=numGroups&reverse">
						</c:when>
						<c:otherwise>
							<a href="?method=showCoursesList&orderby=numGroups">
						</c:otherwise>
					</c:choose>
					<fmt:message key="headerCoursesListNumGroups"/></a></center>
				</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
			  </tr>
			 <tbody id="coursestabletbody">
			
  			 <c:forEach items="${courses}" var="course">
 			  <tr id="row${course.id}">
 			    <td><c:out value="${course.code}"/></td>
 			    <td><c:out value="${course.name}"/></td>
 			    <td><c:out value="${course.studies}"/></td>
 			    <td align="center"><c:out value="${course.level}"/></td>
				<td align="center"><c:out value="${course.numGroups}"/></td>
				<td>
				   <a href="${pageContext.request.contextPath}/admin/admin.itest?method=editCourse&idcourse=${course.id}">
				   	<img src="${pageContext.request.contextPath}/imagenes/editar.gif" alt="<fmt:message key="labelEditCourse"/>" title="<fmt:message key="labelEditCourse"/>" border="none">
				   </a>
				</td>
				<td>
					<a href="${pageContext.request.contextPath}/admin/admin.itest?method=deleteCourse&idcourse=${course.id}" onclick="return confirm ('<fmt:message key="confirmDeleteCourse"/> <fmt:message key="alertDeleteCourse"/>')">
						<img src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="labelDeleteCourse"/>" title="<fmt:message key="labelDeleteCourse"/>" border="none">
					</a>
				</td>
			  </tr>
			 </c:forEach>
			 		 
			<c:choose>
				 <c:when test="${empty courses}">
				  <tr>
				    <td align="center" colspan="7"><fmt:message key="noAvailableCourses"/></td>
				  </tr>
				 </c:when>
				 <c:otherwise>
				  <tr>
				    <td align="center" colspan="7"><hr/></td>
				  </tr>
				  <tr>
				    <td align="center" colspan="7"><b><fmt:message key="totalLabel"/> ${fn:length(courses)} </b></td>
				  </tr>				 
				 </c:otherwise>
			 </c:choose>
			 
			 
			</tbody>
			</table>
		</div>
		<c:if test="${deleted eq true}">
			<script>
				alert('<fmt:message key="alertSubjectDeleteOk"/>');
			</script>
		</c:if>
	</body>
</html>