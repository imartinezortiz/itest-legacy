<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>

<% 
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addBundleStep("textNewInstitution","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="admin"/>
	<jsp:param name="menu" value="admin"/>
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
		if (COD.length == 0){
			alert('<fmt:message key="titleCreateError"/>\n\n<fmt:message key="msgCodeEmptyError"/>');
			return false;	
		}
		if (NAME.length > 50){
	 		alert('<fmt:message key="titleCreateError"/>\n\n<fmt:message key="msgNameLengthError"/>');
			return false;	
		}
		if (NAME.length == 0){
			alert('<fmt:message key="titleCreateError"/>\n\n<fmt:message key="msgNameEmptyError"/>');
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

			<form action="institution.itest?method=addInstitution" method="post"  onSubmit="return validateFields();">
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
							<td><input type="text" name="code" id="code"/></td>
							<th><label for="name"><fmt:message key="name"/></th>
							<td colspan="3"><input type="text" name="name" id="name"/></td>
						</tr>
						<tr>
							<th><label for="address"><fmt:message key="address"/></th>
							<td colspan="5"><input type="text" name="address" id="address"/></td>
						</tr>
						<tr>
							<th><label for="zip"><fmt:message key="zip"/></th>
							<td><input type="text" name="zip" id="zip"/></td>
							<th><label for="city"><fmt:message key="city"/></label></th>
							<td><input type="text" name="city" id="city"/></td>
							<th><label for="area"><fmt:message key="area"/></th>
							<td><input type="text" name="area" id="area"/></td>
						</tr>
						<tr>
							<th><label for="phone"><fmt:message key="phone"/></th>
							<td><input type="text" name="phone" id="phone"/></td>
							<th><label for="fax"><fmt:message key="fax"/></th>
							<td><input type="text" name="fax" id="fax"/></td>
							<th><label for="email"><fmt:message key="email"/></th>
							<td><input type="text" name="email" id="email"/></td>
						</tr>
						<tr>
							<th><label for="contactPerson"><fmt:message key="contactPerson"/></th>
							<td><input type="text" name="contactPerson" id="contactPerson"/></td>
							<th><label for="contactPhone"><fmt:message key="contactPhone"/></th>
							<td><input type="text" name="contactPhone" id="contactPhone"/></td>
							<th><label for="web"><fmt:message key="web"/></th>
							<td><input type="text" name="web" id="web"/></td>
						</tr>
						<tr>
							<th><label for="degreeDepartment"></label><fmt:message key="degreeDepartment"/></th>
							<td>
								<select id="titulacion" name="titulacion" onchange="javascript:enableInput();">
									<option value=""></option>
									<option value="-1">Otros</option>
								</select>
							</td>
							<th id="certificationText" style="display:none"><label for="certification"><fmt:message key="labelNewDegree"/></th>
							<td id="certificationInput"  style="display:none">
								<input id="certification" name="certification" type="text" value="" />
							</td>
						</tr>
						
					</table>
					<fieldset>
						<legend><fmt:message key="headerInstListLevel"/></legend>
						<center>
							<div class="divDosIzq">
								<ul style="list-style:none;">
									<li><INPUT TYPE=CHECKBOX NAME="Infantil"/><label><fmt:message key="labelCheckInfantil"/></label></li>
									<li><INPUT TYPE=CHECKBOX NAME="Primaria"   /><label><fmt:message key="labelCheckPrimaria"/></label></li>
									<li><INPUT TYPE=CHECKBOX NAME="Secundaria"/><label><fmt:message key="labelCheckSecundaria"/></label></li>
									
								</ul>
							</div>
							<div class="divDosDer">
								<ul style="list-style:none;">
								<li><INPUT TYPE=CHECKBOX NAME="FormacionProfesional"/><label><fmt:message key="labelCheckFp"/></label></li>
									<li><INPUT TYPE=CHECKBOX NAME="Bachillerato"   /><label><fmt:message key="labelCheckBachillerato"/></label></li>
									<li><INPUT TYPE=CHECKBOX NAME="Universidad"/><label><fmt:message key="labelCheckUniversidad"/></label></li>
								</ul>
							</div>
						</center>
					</fieldset>
					<input type="submit" value="<fmt:message key="addButton"/>"/>
				</fieldset>
			</form>	
		</div>
	</body>
</html>