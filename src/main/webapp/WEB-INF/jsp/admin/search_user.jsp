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
		dni = document.getElementById('dniFilter').value;
		name = document.getElementById('nameFilter').value;
		surname = document.getElementById('surnameFilter').value;
		username = document.getElementById('userFilter').value;
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
			cellelement.setAttribute("align","center");
			cellelement.innerHTML = user.surname;
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.setAttribute("align","center");
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
			if(user.role == 'LEARNER')
				cellelement.innerHTML = "<fmt:message key="learnerRoleOption"/>";
			if(user.role == 'KID')
				cellelement.innerHTML = "<fmt:message key="kidRoleOption"/>";
			if(user.role == 'TUTORAV')
				cellelement.innerHTML = "<fmt:message key="tutorAvRoleOption"/>";

			
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.setAttribute("align","center");
			cellelement.innerHTML = '<a href="institution.itest?method=editUser&iduser='+user.id+'"><img src="${pageContext.request.contextPath}/imagenes/editar.gif" alt="<fmt:message key="labelEditUser"/>" title="<fmt:message key="labelEditUser"/>" border="none"></a>'
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.setAttribute("align","center");
			cellelement.innerHTML = '<a onclick="return confirm (\'<fmt:message key="confirmDeleteUser"/> <fmt:message key="alertDeleteUser"/>\')" href="institution.itest?method=deleteUser&iduser='+user.id+'"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="labelDeleteUser"/>" title="<fmt:message key="labelDeleteUser"/>" border="none"></a>';
			rowelement.appendChild(cellelement);

			tbodyelement.appendChild(rowelement);
		}
		if (users.length == 0) {
		       if(users!=''){
		    	   rowelement = document.createElement('tr');
			       cellelement = document.createElement('td');
				   cellelement.colSpan=7;
			       cellelement.setAttribute("align","center");
				   cellelement.innerHTML = "<fmt:message key="noAvailableConfigExams"/>";
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
		document.getElementById('divTabla').style.display="block";
		iTestUnlockPage();
	}
</script>

		<div id="contenido">
			<fieldset>
				<legend><fmt:message key="labelFilterTitle"/></legend>
				<form action="return false;">
					<div class="divDosIzq">
						<ul class="listaopciones">
							<li><label><fmt:message key="name"/></label>:<input type="text" id="nameFilter"></li>
							<li><label><fmt:message key="surname"/></label>:<input type="text" id="surnameFilter"></li>
						</ul>
					</div>
					<div class="divDosDer">
						<ul class="listaopciones">
							<li><label><fmt:message key="user"/></label>:<input type="text" id="userFilter"></li>
							<li><label><fmt:message key="personalID"/></label>:<input type="text" id="dniFilter"></li>
							
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
						</ul>
						<br/>
						<input type="reset" value="<fmt:message key="msgbuttonReset"/>" onclick="javascript:updateTable('');"/>
						<input type="button" value="<fmt:message key="buttonFilterRun"/>" onclick="javascript:Userfilter();"/>
					</div>
				</form>
			</fieldset>
			
			<div id="divTabla" style="display:none">
			<table id="userstable" class="tabladatos">
				  <tr>
					<th><center><fmt:message key="headerStListPersId"/></center></th>	
					<th><center><fmt:message key="headerStListSurname"/></center></th>	
					<th><center><fmt:message key="headerStListName"/></center></th>
					<th><center><fmt:message key="headerStListUserName"/></center></th>
					<th><center><fmt:message key="headerStListRole"/></center></th>
					<th>&nbsp;</th>				
					<th>&nbsp;</th>
				 </tr>
				 <tbody id="userstabletbody"></tbody>
			 </table>
			</div>
		</div>
	</body>
</html>