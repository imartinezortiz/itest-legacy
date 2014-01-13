<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>

<% 
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addBundleStep("textInstitutionsList","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="admin"/>
	<jsp:param name="menu" value="admin"/>
</jsp:include>

	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>


<script type="text/javascript">
function findInstitution(){
	var array = new Array(6);
	if(document.getElementById('Primaria').checked)
		array[0]="Primaria";
	else
		array[0]=null;
	if(document.getElementById('Secundaria').checked)
		array[1]="Secundaria";
	else
		array[1]=null;
	if(document.getElementById('FormacionProfesional').checked)
		array[2]="Formacion profesional";
	else
		array[2]=null;
	if(document.getElementById('Bachillerato').checked)
		array[3]="Bachillerato";
	else
		array[3]=null;
	if(document.getElementById('Universidad').checked)
		array[4]="Universidad";
	else
		array[4]=null;
	if(document.getElementById('Infantil').checked)
		array[5]="Infantil";
	else
		array[5]=null;
	id=document.getElementById('idFilter').value;
	nombre=document.getElementById('nameFilter').value;
	localidad=document.getElementById('cityFilter').value;
	provincia=document.getElementById('areaFilter').value;
	certification=document.getElementById('certification').value;
	iTestLockPage();
	AdminMgmt.findInstitution(id,nombre,provincia,localidad,certification,array,{callback:updateTable,
		 timeout:callBackTimeOut,
		 errorHandler:function(message) { iTestUnlockPage('error');} });
	
}
reverse = true;
function orderTable(orderby){
	reverse = !reverse;
	iTestLockPage();
	AdminMgmt.orderInstitutionList(orderby,reverse,{callback:updateTable,
		 timeout:callBackTimeOut,
		 errorHandler:function(message) { iTestUnlockPage('error');} });
}
function updateTable(list){
	
	tbodyelement=document.createElement('tbody');
	tbodyelement.setAttribute("id","institutionstabletbody");
	for(var i=0;i<list.length;i++){
		institution = list[i];
		rowelement = document.createElement('tr');
		cellelement = document.createElement('td');
		cellelement.innerHTML = institution.code;
		rowelement.appendChild(cellelement);
		
		cellelement = document.createElement('td');
		cellelement.innerHTML = institution.name;
		rowelement.appendChild(cellelement);
		
		cellelement = document.createElement('td');
		cellelement.innerHTML = institution.city;
		rowelement.appendChild(cellelement);
		
		cellelement = document.createElement('td');
		cellelement.innerHTML = institution.state;
		rowelement.appendChild(cellelement);
		
		
		cellelement = document.createElement('td');
		ullist = document.createElement('ul');
		
		if(institution.studies.primary == true){
			listelement = document.createElement('li');
			listelement.innerHTML="<fmt:message key="labelCheckPrimaria"/>";
			ullist.appendChild(listelement);
		}
		if(institution.studies.secundary == true){
			listelement = document.createElement('li');
			listelement.innerHTML="<fmt:message key="labelCheckSecundaria"/>";
			ullist.appendChild(listelement);
		}
		if(institution.studies.vocationalTraining == true){
			listelement = document.createElement('li');
			listelement.innerHTML="<fmt:message key="labelCheckFp"/>";
			ullist.appendChild(listelement);
		}
		if(institution.studies.hightSchool == true){
			listelement = document.createElement('li');
			listelement.innerHTML="<fmt:message key="labelCheckBachillerato"/>";
			ullist.appendChild(listelement);
		}
		if(institution.studies.university == true){
			listelement = document.createElement('li');
			listelement.innerHTML="<fmt:message key="labelCheckUniversidad"/>";
			ullist.appendChild(listelement);
		}
		if(institution.studies.infantil == true){
			listelement = document.createElement('li');
			listelement.innerHTML="<fmt:message key="labelCheckInfantil"/>";
			ullist.appendChild(listelement);
		}
			
			
		cellelement.appendChild(ullist);
		rowelement.appendChild(cellelement);


		cellelement = document.createElement('td');
		cellelement.innerHTML = institution.certification;
		rowelement.appendChild(cellelement);

		
		cellelement = document.createElement('td');
		cellelement.innerHTML='<a href="institution.itest?method=indexInstitution&idinstitution='+institution.id+'"><img src="${pageContext.request.contextPath}/imagenes/editar.gif" alt="<fmt:message key="labelEditInstitution"/>" title="<fmt:message key="labelEditInstitution"/>" border="none"></a>'
		rowelement.appendChild(cellelement);


		cellelement = document.createElement('td');
		cellelement.innerHTML='<a href="admin.itest?method=deleteInstitution&idinstitution='+institution.id+'" onclick="return confirm ("<fmt:message key="confirmDeleteInstitution"/>\n<fmt:message key="alertDeleteInstitution"/>")"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="labelDeleteInstitution"/>" title="<fmt:message key="labelDeleteInstitution"/>" border="none"></a>'
		rowelement.appendChild(cellelement);
		
		tbodyelement.appendChild(rowelement);
	}
	if (list.length == 0) {
	       rowelement = document.createElement('tr');
	       cellelement = document.createElement('td');
		   cellelement.colSpan=8;
	       cellelement.setAttribute("align","center");
		   cellelement.innerHTML = "<fmt:message key="noAvailableConfigExams"/>";
		   rowelement.appendChild(cellelement);
		   tbodyelement.appendChild(rowelement);
	    } else {
		   rowelement = document.createElement('tr');
	       cellelement = document.createElement('td');
	       cellelement.colSpan=8;
	       cellelement.setAttribute("align","center");
		   cellelement.innerHTML ="<hr/>";
		   rowelement.appendChild(cellelement);
		   tbodyelement.appendChild(rowelement);
	       rowelement = document.createElement('tr');
	       cellelement = document.createElement('td');
	       cellelement.colSpan=8;
	       cellelement.setAttribute("align","center");
		   cellelement.innerHTML ="<b><fmt:message key="totalLabel"/> "+list.length+"</b>";
		   rowelement.appendChild(cellelement);
		   tbodyelement.appendChild(rowelement);
	    }
	datatable=document.getElementById("institutionstable");
	// Replaces tbody			
	datatable.replaceChild(tbodyelement,document.getElementById("institutionstabletbody"));
	iTestUnlockPage();
}
</script>

		<div id="contenido">
			<fieldset>
				<legend><fmt:message key="labelFilterTitle"/></legend>
				<div>
					<div class="divDosIzq" style="text-align:right; width:20%;">
						<table>
							<tr>
								<td><label><fmt:message key="code"/> :</label></td>
								<td><input type="text" id="idFilter"/></td>
							</tr>
							<tr>
								<td><label><fmt:message key="name"/> :</label></td>
								<td><input type="text" id="nameFilter"/></td>
							</tr>
						</table>
					</div>
					<div class="divDosDer" style="text-align:left; width:40%;">
						<table>
							<tr>
								<td><label><fmt:message key="city"/> :</label></td>
								<td><input type="text" id="cityFilter"/></td>
							</tr>
							<tr>
								<td><label><fmt:message key="area"/> :</label></td>
								<td><input type="text" id="areaFilter"/></td>
							</tr>
						</table>
					</div>
					<div class="divcentro">
						<center>
							<label style="font-size:12pt"><fmt:message key="degreeDepartment"/> :</label>
							<input type="text" id="certification"/>
						</center>
						<div class="divDosIzq">
							<ul style="list-style:none;">
								<li style="margin-left:70%"><INPUT TYPE=CHECKBOX id="Infantil"/><label><fmt:message key="labelCheckInfantil"/></label></li>
								<li style="margin-left:70%"><INPUT TYPE=CHECKBOX id="Primaria"   /><label><fmt:message key="labelCheckPrimaria"/></label></li>
								<li style="margin-left:70%"><INPUT TYPE=CHECKBOX id="Secundaria"/><label><fmt:message key="labelCheckSecundaria"/></label></li>
								
							</ul>
						</div>
						<div class="divDosDer" style="text-align:left;">
							<ul style="list-style:none;">
								<li><INPUT TYPE=CHECKBOX id="FormacionProfesional"/><label><fmt:message key="labelCheckFp"/></label></li>
								<li><INPUT TYPE=CHECKBOX id="Bachillerato"   /><label><fmt:message key="labelCheckBachillerato"/></label></li>
								<li><INPUT TYPE=CHECKBOX id="Universidad"/><label><fmt:message key="labelCheckUniversidad"/></label></li>
							</ul>
						</div>
						<div class="divcentro">
							<input type="button" value="<fmt:message key="buttonFilterRun"/>" onclick="javascript:findInstitution();"/>
						</div>
					</div>
				</div>
				<br/>
			</fieldset>
			<table id="institutionstable" class="tabladatos">
			  <col width="0*"/>
			  <col width="*"/>
			  <col width="0*"/>
			  <col width="0*"/>
			  <col width="*"/>
			  <col width="0*"/>
			  <col width="0*"/>	
			  <tr>
				<th><a href="javascript:orderTable('code')"><fmt:message key="headerInstListCode"/></a></th>
				<th><center><a href="javascript:orderTable('name')"><fmt:message key="headerInstListName"/></a></center></th>	
				<th><center><a href="javascript:orderTable('city')"><fmt:message key="headerInstListCity"/></a></center></th>
				<th><center><a href="javascript:orderTable('area')"><fmt:message key="headerInstListArea"/></a></center></th>
				<th><center><fmt:message key="headerInstListLevel"/></center></th>
				<th><center><a href="javascript:orderTable('certification')"><fmt:message key="degreeDepartment"/></a></center></th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
			  </tr>
			 <tbody id="institutionstabletbody">
			
  			 <c:forEach items="${institutions}" var="institution">
 			  <tr id="row${institution.id}">
 			    <td><c:out value="${institution.code}"/></td>
 			    <td><c:out value="${institution.name}"/></td>
 			    <td><c:out value="${institution.city}"/></td>
 			    <td><c:out value="${institution.state}"/></td>
				<td>
					<ul>
						<c:forEach items="${institution.studies.studies}" var="study">
							<li><c:out value="${study}"/></li>
						</c:forEach>
					</ul>
				</td>
				<td><c:out value="${institution.certification}"/></td>
				<td>
				   <a href="institution.itest?method=indexInstitution&idinstitution=${institution.id}"><img src="${pageContext.request.contextPath}/imagenes/editar.gif" alt="<fmt:message key="labelEditInstitution"/>" title="<fmt:message key="labelEditInstitution"/>" border="none"></a>
				</td>
				<td>
					<a href="admin.itest?method=deleteInstitution&idinstitution=${institution.id}" onclick="return confirm ('<fmt:message key="confirmDeleteInstitution"/>\n<fmt:message key="alertDeleteInstitution"/>')"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="labelDeleteInstitution"/>" title="<fmt:message key="labelDeleteInstitution"/>" border="none"></a>
				</td>
			  </tr>
			 </c:forEach>
			 		 
			<c:choose>
				 <c:when test="${empty institutions}">
				  <tr>
				    <td align="center" colspan="8"><fmt:message key="noAvailableInstitutions"/></td>
				  </tr>
				 </c:when>
				 <c:otherwise>
				  <tr>
				    <td align="center" colspan="8"><hr/></td>
				  </tr>
				  <tr>
				    <td align="center" colspan="8"><b><fmt:message key="totalLabel"/> ${fn:length(institutions)} </b></td>
				  </tr>				 
				 </c:otherwise>
			 </c:choose>
			 
			 
			</tbody>
			</table>
		</div>
		<c:if test="${!empty deletedInstitution}">
			<script>
				alert('<fmt:message key="alertInstitutionDeleted"/>');
			</script>
		</c:if>
	</body>
</html>