<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>
<%@ page import="com.cesfelipesegundo.itis.model.Institution" %>

<% 
	Institution institution = (Institution)request.getAttribute("institution");
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addStep(institution.getName(),"");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="admin"/>
	<jsp:param name="menu" value="institution"/>
</jsp:include>


<script type="text/javascript">
<!--
	function validateFields(){
		var COD = document.getElementById('code').value;
		var NAME = document.getElementById('name').value;
		var AD = document.getElementById('address').value;
 		var CP = document.getElementById('zip').value;
		var CI = document.getElementById('city').value;
		var AR = document.getElementById('area').value;
		var PH = document.getElementById('phone').value;
		var FA = document.getElementById('fax').value;
 		var EM = document.getElementById('email').value;
 		var WE = document.getElementById('web').value;
 		var CONPE = document.getElementById('contactPerson').value;
 		var CONPH = document.getElementById('contactPhone').value;
	 	if (COD.length > 10){
	 		alert('<fmt:message key="titleCreateError"/>\n\n<fmt:message key="msgCodeLengthError"/>');
			return false;	
		}	
		if (NAME.length > 50){
	 		alert('<fmt:message key="titleCreateError"/>\n\n<fmt:message key="msgNameLengthError"/>');
			return false;	
		}	
		if (AD.length > 50){
	 		alert('<fmt:message key="titleCreateError"/>\n\n<fmt:message key="msgAddressLengthError"/>');
			return false;	
		}	
		if (CP.length > 5){
	 		alert('<fmt:message key="titleCreateError"/>\n\n<fmt:message key="msgZipLengthError"/>');
			return false;	
		}	
		if (CI.length > 50){
	 		alert('<fmt:message key="titleCreateError"/>\n\n<fmt:message key="msgCityLengthError"/>');
			return false;	
		}	
		if (AR.length > 15){
	 		alert('<fmt:message key="titleCreateError"/>\n\n<fmt:message key="msgAreaLengthError"/>');
			return false;	
		}	
		if (PH.length > 20){
	 		alert('<fmt:message key="titleCreateError"/>\n\n<fmt:message key="msgPhoneLengthError"/>');
			return false;	
		}	
		if (FA.length > 20){
	 		alert('<fmt:message key="titleCreateError"/>\n\n<fmt:message key="msgFaxLengthError"/>');
			return false;	
		}
		if (EM.length > 50){
	 		alert('<fmt:message key="titleCreateError"/>\n\n<fmt:message key="msgEmailLengthError"/>');
			return false;	
		}
		if (WE.length > 100){
	 		alert('<fmt:message key="titleCreateError"/>\n\n<fmt:message key="msgWebLengthError"/>');
			return false;	
		}
		if (CONPE.length > 60){
	 		alert('<fmt:message key="titleCreateError"/>\n\n<fmt:message key="msgContactPersonLengthError"/>');
			return false;	
		}
		if (CONPH.length > 20){
	 		alert('<fmt:message key="titleCreateError"/>\n\n<fmt:message key="msgContactPhoneLengthError"/>');
			return false;	
		}
		
		return true;
	}


	function enableInput(){
		var value = document.getElementById('titulacion').value;
		if(value == -1){
			document.getElementById('certificationText').style.display="";
			document.getElementById('certificationInput').style.display="";
		}else{
			document.getElementById('certificationText').style.display="none";
			document.getElementById('certificationInput').style.display="none";
		}
	}

//-->
</script>
		<div id="contenido">

			<form action="institution.itest?method=saveInstitution" method="post" onSubmit="return validateFields();">
				<fieldset>
					<legend><fmt:message key="data"/></legend>
					<table class="tablaformulario">
						<col width="15%"/>
						<col width="15%"/>
						<col width="15%"/>
						<col width="20%"/>
						<col width="15%"/>
						<col width="20%"/>
						<tr>
							<th><label for="code"><fmt:message key="code"/></label></th>
							<td><input type="text" name="code" id="code" value="<c:out value="${institution.code}"/>"/></td>
							<th><label for="name"><fmt:message key="name"/></th>
							<td colspan="3"><input type="text" name="name" id="name" value="<c:out value="${institution.name}"/>"/></td>
						</tr>
						<tr>
							<th><label for="address"><fmt:message key="address"/></th>
							<td colspan="5"><input type="text" name="address" id="address" value="<c:out value="${institution.address}"/>"/></td>
						</tr>
						<tr>
							<th><label for="zip"><fmt:message key="zip"/></th>
							<td><input type="text" name="zip" id="zip" value="<c:out value="${institution.zipCode}"/>"/></td>
							<th><label for="city"><fmt:message key="city"/></label></th>
							<td><input type="text" name="city" id="city" value="<c:out value="${institution.city}"/>"/></td>
							<th><label for="area"><fmt:message key="area"/></th>
							<td><input type="text" name="area" id="area" value="<c:out value="${institution.state}"/>"/></td>
						</tr>
						<tr>
							<th><label for="phone"><fmt:message key="phone"/></th>
							<td><input type="text" name="phone" id="phone" value="<c:out value="${institution.phone}"/>"/></td>
							<th><label for="fax"><fmt:message key="fax"/></th>
							<td><input type="text" name="fax" id="fax" value="<c:out value="${institution.fax}"/>"/></td>
							<th><label for="email"><fmt:message key="email"/></th>
							<td><input type="text" name="email" id="email" value="<c:out value="${institution.email}"/>"/></td>
						</tr>
						<tr>
							<th><label for="contactPerson"><fmt:message key="contactPerson"/></th>
							<td><input type="text" name="contactPerson" id="contactPerson" value="<c:out value="${institution.contactPerson}"/>"/></td>
							<th><label for="contactPhone"><fmt:message key="contactPhone"/></th>
							<td><input type="text" name="contactPhone" id="contactPhone" value="<c:out value="${institution.contactPhone}"/>"/></td>
							<th><label for="web"><fmt:message key="web"/></th>
							<td><input type="text" name="web" id="web" value="<c:out value="${institution.web}"/>"/></td>
						</tr>
						<tr>
							<th><label for="degreeDepartment"></label><fmt:message key="degreeDepartment"/></th>
							<td>
								<select id="titulacion" name="titulacion" onchange="javascript:enableInput();">
									<option value="-1"><fmt:message key="labelCheckOtros"/></option>
									<c:forEach items="${certifications}" var="certification">
										<option value="${certification}"><c:out value="${certification}"/></option>
									</c:forEach>
								</select>
							</td>
							<th id="certificationText"><label for="certification"><fmt:message key="labelNewDegree"/></th>
							<td id="certificationInput">
								<input id="certification" name="certification" type="text" value="<c:out value="${institution.certification}"/>" />
							</td>
						</tr>
					</table>
					<fieldset>
						<legend><fmt:message key="headerInstListLevel"/></legend>
						<center>
							<div class="divDosIzq">
								<ul style="list-style:none;">
								<c:choose>
									<c:when test="${studies.infantil eq true}">
										<li><INPUT TYPE=CHECKBOX NAME="Infantil" checked /><label><fmt:message key="labelCheckInfantil"/></label></li>
									</c:when>
									<c:otherwise>
										<li><INPUT TYPE=CHECKBOX NAME="Infantil"   /><label><fmt:message key="labelCheckInfantil"/></label></li>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${studies.primary eq true}">
										<li><INPUT TYPE=CHECKBOX NAME="Primaria" checked /><label><fmt:message key="labelCheckPrimaria"/></label></li>
									</c:when>
									<c:otherwise>
										<li><INPUT TYPE=CHECKBOX NAME="Primaria"   /><label><fmt:message key="labelCheckPrimaria"/></label></li>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${studies.secundary eq true}">
										<li><INPUT TYPE=CHECKBOX NAME="Secundaria" checked/><label><fmt:message key="labelCheckSecundaria"/></label></li>
									</c:when>
									<c:otherwise>
										<li><INPUT TYPE=CHECKBOX NAME="Secundaria"/><label><fmt:message key="labelCheckSecundaria"/></label></li>
									</c:otherwise>
								</c:choose>
										
								</ul>
							</div>
							<div class="divDosDer">
								<ul style="list-style:none;">
									<c:choose>
										<c:when test="${studies.vocationalTraining eq true}">
											<li><INPUT TYPE=CHECKBOX NAME="FormacionProfesional" checked/><label><fmt:message key="labelCheckFp"/></label></li>									
										</c:when>
										<c:otherwise>
											<li><INPUT TYPE=CHECKBOX NAME="FormacionProfesional"/><label><fmt:message key="labelCheckFp"/></label></li>									
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${studies.hightSchool eq true}">
											<li><INPUT TYPE=CHECKBOX NAME="Bachillerato" checked  /><label><fmt:message key="labelCheckBachillerato"/></label></li>
										</c:when>
										<c:otherwise>
											<li><INPUT TYPE=CHECKBOX NAME="Bachillerato"   /><label><fmt:message key="labelCheckBachillerato"/></label></li>										
										</c:otherwise>
									</c:choose>	
									<c:choose>
										<c:when test="${studies.university eq true}">
											<li><INPUT TYPE=CHECKBOX NAME="Universidad" checked/><label><fmt:message key="labelCheckUniversidad"/></label></li>
										</c:when>
										<c:otherwise>
											<li><INPUT TYPE=CHECKBOX NAME="Universidad"/><label><fmt:message key="labelCheckUniversidad"/></label></li>
										</c:otherwise>
									</c:choose>	
									
								</ul>
							</div>
						</center>
					</fieldset>
					<input type="submit" value="<fmt:message key="buttonSave"/>"/>
				</fieldset>
			</form>
			<br>
			<br>
			<hr/>
			<div class="divDosIzq">
  				<p><b><fmt:message key="taskTitleUsers" /></b></p>
  				<hr>
				<ul class="listaopcionesgroup">							
					<li><a href="${pageContext.request.contextPath}/admin/institution.itest?method=newUser"><fmt:message key="taskNewUser" /></a> </li>
					<!-- <li><a href="${pageContext.request.contextPath}/admin/institution.itest?method=showUsersList&view=tutors"><fmt:message key="taskShowTutors" /></a> </li>
					<li><a href="${pageContext.request.contextPath}/admin/institution.itest?method=showUsersList&view=learners"><fmt:message key="taskShowLearners" /></a></li> -->
					<li><a href="${pageContext.request.contextPath}/admin/institution.itest?method=showUsersList"><fmt:message key="taskShowAll" /></a></li>
					<li><a href="${pageContext.request.contextPath}/admin/institution.itest?method=findUser"><fmt:message key="taskFindUsers" /></a></li>
				</ul>
			</div>
			
			<div class="divDosDer">
  				<p><b><fmt:message key="taskTitleGroups" /></b></p>
  				<hr>
				<ul class="listaopcionesgroup">							
					<li><a href="${pageContext.request.contextPath}/admin/institution.itest?method=newGroup"><fmt:message key="taskNewGroup" /></a> </li>
					<li><a href="${pageContext.request.contextPath}/admin/institution.itest?method=showGroupsList"><fmt:message key="taskShowGroups" /></a></li>
					<li><a href="${pageContext.request.contextPath}/admin/institution.itest?method=findGroup"><fmt:message key="taskFindGroups" /></a></li>
					
				</ul>
			
			</div>	
			
			<div class="divcentro"></div>	

			<br><hr><br>
			
		</div>
		<c:if test="${!empty addedInstituion}">
			<script>
				alert('<fmt:message key="alertAddInstitutionOk"/>');
			</script>
		</c:if>
		<c:if test="${!empty savedInstituion}">
			<script>
				alert('<fmt:message key="alertSavedInstitutionOk"/>');
			</script>
		</c:if>
	</body>
</html>