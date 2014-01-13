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
	breadCrumb.addBundleStep("textUsersList","");
	request.setAttribute("breadCrumb",breadCrumb);
%>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="admin"/>
	<jsp:param name="menu" value="institution"/>
</jsp:include>


	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminInstitutionMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>


<script type="text/javascript">
	function Userfilter(){
		dni = document.getElementById('filterDNI').value;
		name = document.getElementById('filterName').value;
		surname = document.getElementById('filterSurname').value;
		username = document.getElementById('filterUserName').value;
		typeUser = document.getElementById('filterTypeUser').value;
		iTestLockPage();
		AdminInstitutionMgmt.filterByUser(dni,name,surname,username,typeUser,{callback:updateTable,
																					 timeout:callBackTimeOut,
																					 errorHandler:function(message) { iTestUnlockPage('error');} });
	}

	function updateTable(users){
		tbodyelement = document.createElement('tbody');
		tbodyelement.setAttribute("id","userstabletbody");
		for(i=0;i<users.length;i++){
			user = users[i];
			rowelement = document.createElement('tr');
			cellelement = document.createElement('td');
			cellelement.setAttribute("align","center");
			cellelement.innerHTML = user.persId;
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.innerHTML = user.surname;
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.innerHTML = user.name;
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.setAttribute("align","center");
			cellelement.innerHTML = user.userName;
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.setAttribute("align","center");
			if(user.role == 'TUTOR')
				cellelement.innerHTML = "<fmt:message key="tutorRoleOption"/>";
			if(user.role == 'TUTORAV')
				cellelement.innerHTML = "<fmt:message key="tutorAvRoleOption"/>";
			if(user.role == 'LEARNER')
				cellelement.innerHTML = "<fmt:message key="learnerRoleOption"/>";
			if(user.role == 'KID')
				cellelement.innerHTML = "<fmt:message key="kidRoleOption"/>";

			
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.setAttribute("align","center");
			cellelement.innerHTML = '<a href="institution.itest?method=editUser&iduser='+user.id+'"><img src="${pageContext.request.contextPath}/imagenes/editar.gif" alt="<fmt:message key="labelEditUser"/>" title="<fmt:message key="labelEditUser"/>" border="none"></a>'
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.setAttribute("align","center");
			cellelement.innerHTML = '<a href="institution.itest?method=deleteUser&iduser='+user.id+'&view='+${view}'" onclick="return confirm (\'<fmt:message key="confirmDeleteUser"/>\n<fmt:message key="alertDeleteUser"/>\')"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="labelDeleteUser"/>" title="<fmt:message key="labelDeleteUser"/>" border="none"></a>';
			rowelement.appendChild(cellelement);

			tbodyelement.appendChild(rowelement);
		}
		if (users.length == 0) {
			if(users!=''){
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
			   cellelement.colSpan=7;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML = "<fmt:message key="noAvailableUsers"/>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
			}
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
		       cellelement.colSpan=7;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML ="<b><fmt:message key="totalLabel"/> "+users.length+"</b>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		    }
		datatable=document.getElementById("userstable");
		// Replaces tbody			
		datatable.replaceChild(tbodyelement,document.getElementById("userstabletbody"));
		iTestUnlockPage();
	}
</script>

		<div id="contenido">
			<div>
				<fieldset>
					<legend><fmt:message key="labelFilterTitle"/></legend>
					<form action="return false;">
					<div class="divDosIzq">
						<ul class="listaopciones">
							<li><fmt:message key="personalID"/> :
								<input type="text" id="filterDNI"> 
							</li>
							<li>
								<fmt:message key="name"/> :
								<input type="text" id="filterName">
							</li>
						</ul>
					</div>
					
					<div class="divDosDer">
						<ul class="listaopciones">
							<li>
								<fmt:message key="surname"/> :
								<input type="text" id="filterSurname">
							</li>
							<li>
								<fmt:message key="textLoginUser"/>
								<input type="text" id="filterUserName">
							</li>
						</ul>
					</div>
					<div class="divcentro">
						<ul class="listaopciones">
							<li>
								<fmt:message key="headerStListRole"/>
								<select id="filterTypeUser">
									<option ></option>
									<option value="TUTORAV"><fmt:message key="tutorAvRoleOption"/></option>
									<option value="TUTOR"><fmt:message key="tutorRoleOption"/></option>
									<option value="LEARNER"><fmt:message key="learnerRoleOption"/></option>
									<option value="KID"><fmt:message key="kidRoleOption"/></option>
								</select>
							</li>
							<li>
								<input type="reset" value="<fmt:message key="msgbuttonReset"/>" onclick="javascript:updateTable('');"/>				
								<input type="button" value="<fmt:message key="buttonFilterRun"/>" onclick="javascript:Userfilter();"/>
							</li>
					</div>
					</form>
				</fieldset>
			</div>
			<table id="userstable" class="tabladatos">
			  <tr>
				<th><center><a href="${pageContext.request.contextPath}/admin/institution.itest?method=showUsersList&orderby=persid&view=${view}&reverse=${reverse}"><fmt:message key="headerStListPersId"/></a></center></th>	
				<th><a href="${pageContext.request.contextPath}/admin/institution.itest?method=showUsersList&orderby=surname&view=${view}&reverse=${reverse}"><fmt:message key="headerStListSurname"/></a></th>	
				<th><a href="${pageContext.request.contextPath}/admin/institution.itest?method=showUsersList&orderby=name&view=${view}&reverse=${reverse}"><fmt:message key="headerStListName"/></a></th>
				<th><center><a href="${pageContext.request.contextPath}/admin/institution.itest?method=showUsersList&orderby=username&view=${view}&reverse=${reverse}"><fmt:message key="headerStListUserName"/></a></center></th>
				<th><center><a href="${pageContext.request.contextPath}/admin/institution.itest?method=showUsersList&orderby=role&view=${view}&reverse=${reverse}"><fmt:message key="headerStListRole"/></center></a></th>
				<th>&nbsp;</th>				
				<th>&nbsp;</th>
			  </tr>
			 <tbody id="userstabletbody">
			
  			 <c:forEach items="${users}" var="user">
 			  <tr>  
 			  	<td id="persId${user.id}" align="center"><c:out value="${user.persId}"/></td>
 			  	<td id="surname${user.id}"><c:out value="${user.surname}"/></td>
			    <td id="name${user.id}"><c:out value="${user.name}"/></td>
				<td id="userName${user.id}" align="center"><c:out value="${user.userName}"/></td>
				<td id="role${user.id}" align="center"><c:choose>
					<c:when test="${user.role eq 'ADMIN'}">
						<fmt:message key="adminRole"/>					
					</c:when>
					<c:when test="${user.role eq 'TUTOR'}">					
						<fmt:message key="tutorRole"/>					
					</c:when>
					<c:when test="${user.role eq 'TUTORAV'}">					
						<fmt:message key="turorAvRole"/>					
					</c:when>
					<c:when test="${user.role eq 'LEARNER'}">					
						<fmt:message key="learnerRole"/>					
					</c:when>
					<c:when test="${user.role eq 'KID'}">					
						<fmt:message key="kidRole"/>					
					</c:when>
					<c:otherwise><c:out value="${user.role}"/></c:otherwise>
				</c:choose></td>
				<!-- Controls -->
				<td align="center">
				   <a href="institution.itest?method=editUser&iduser=${user.id}">
				   	<img src="${pageContext.request.contextPath}/imagenes/editar.gif" alt="<fmt:message key="labelEditUser"/>" title="<fmt:message key="labelEditUser"/>" border="none">
				   </a>
				</td>
				<td align="center">
					<a href="institution.itest?method=deleteUser&iduser=${user.id}&view=${view}" onclick="return confirm ('<fmt:message key="confirmDeleteUser"/>\n<fmt:message key="alertDeleteUser"/>')">
						<img src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="labelDeleteUser"/>" title="<fmt:message key="labelDeleteUser"/>" border="none">
					</a>
				</td>
			  </tr>
			 </c:forEach>
			 		 
			<c:choose>
				 <c:when test="${empty users}">
				  <tr>
				    <td align="center" colspan="7"><fmt:message key="noAvailableUsers"/></td>
				  </tr>
				 </c:when>
				 <c:otherwise>
				  <tr>
				    <td align="center" colspan="7"><hr/></td>
				  </tr>
				  <tr>
				    <td align="center" colspan="7"><b><fmt:message key="totalLabel"/> ${fn:length(users)} </b></td>
				  </tr>				 
				 </c:otherwise>
			 </c:choose>		 
			 
			</tbody>
			</table>
		</div>
	</body>
</html>