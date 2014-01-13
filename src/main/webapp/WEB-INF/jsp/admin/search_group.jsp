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
	breadCrumb.addBundleStep("textGroupsList","");
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

function groupFilter(){
	course = document.getElementById('courseFilter').value;
	group = document.getElementById('groupFilter').value;
	year = document.getElementById('yearFilter').value;
	iTestLockPage();
	AdminInstitutionMgmt.filterByGroup(group,course,year,{callback:updateTable,
		 timeout:callBackTimeOut,
		 errorHandler:function(message) { iTestUnlockPage('error');} });
}

function updateTable(groups){
	tbodyelement = document.createElement('tbody');
	tbodyelement.setAttribute("id","groupstabletbody");

	for(i=0;i<groups.length;i++){
		group = groups[i];
		rowelement = document.createElement('tr');

		cellelement = document.createElement('td');
		cellelement.innerHTML = group.course.name;
		rowelement.appendChild(cellelement);

		cellelement = document.createElement('td');
		cellelement.innerHTML = group.name;
		rowelement.appendChild(cellelement);

		cellelement = document.createElement('td');
		cellelement.innerHTML = group.year;
		rowelement.appendChild(cellelement);
		
		cellelement = document.createElement('td');
		cellelement.setAttribute("align","center");
		cellelement.innerHTML = '<a href="institution.itest?method=editGroup&idgroup='+group.id+'"><img src="${pageContext.request.contextPath}/imagenes/editar.gif" alt="<fmt:message key="labelEditGroup"/>" title="<fmt:message key="labelEditGroup"/>" border="none"></a>'
		rowelement.appendChild(cellelement);

		cellelement = document.createElement('td');
		cellelement.setAttribute("align","center");
		cellelement.innerHTML = '<a href="institution.itest?method=deleteGroup&idgroup='+group.id+'" onclick="return confirm (\'<fmt:message key="confirmDeleteGroup"/> <fmt:message key="alertDeleteGroup"/>\')"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="labelDeleteGroup"/>" title="<fmt:message key="labelDeleteGroup"/>" border="none"></a>';
		rowelement.appendChild(cellelement);

		tbodyelement.appendChild(rowelement);
	}
		if (groups.length == 0) {
		       if(groups!=''){
		    	   rowelement = document.createElement('tr');
			       cellelement = document.createElement('td');
				   cellelement.colSpan=5;
			       cellelement.setAttribute("align","center");
				   cellelement.innerHTML = "<fmt:message key="noAvailableConfigExams"/>";
				   rowelement.appendChild(cellelement);
				   tbodyelement.appendChild(rowelement);
			   }
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
			   cellelement.innerHTML ="<b><fmt:message key="totalLabel"/> "+groups.length+"</b>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		    }
		datatable=document.getElementById("groupstable");
		// Replaces tbody			
		datatable.replaceChild(tbodyelement,document.getElementById("groupstabletbody"));
	iTestUnlockPage();
}
</script>

		<div id="contenido">
			<fieldset>
				<legend><fmt:message key="labelFilterTitle"/></legend>
				<form >
				<div class="divDosIzq">
					<ul class="listaopciones">
						<li><label><fmt:message key="headerGroupsListCourse"/>: </label><input type="text" id="groupFilter"></li>
						<li><label><fmt:message key="headerGroupsListGroup"/>: </label><input type="text" id="courseFilter"></li>
					</ul>
				</div>
				<div class="divDosDer">
					<ul class="listaopciones">
						<li><label><fmt:message key="headerGroupsListYear"/>: </label><input type="text" id="yearFilter"></li>
					</ul>
				</div>
				<div class="divcentro">
					<input type="reset" value="<fmt:message key="msgbuttonReset"/>" onclick="javascript:updateTable('');"/>
				</div>
				</form>
				<input type="button" value="<fmt:message key="buttonFilterRun"/>" onclick="javascript:groupFilter();"/>
			</fieldset>
		
			<div id="divTabla">
			<table id="groupstable" class="tabladatos">
				<col width="*"/>
			    <col width="0*"/>
			  	<col width="0*"/>
			  	<col width="0*"/>
			  	<col width="0*"/>	
			  	<tr>
			  		<th><center><fmt:message key="headerGroupsListCourse"/></center></th>
					<th><center><fmt:message key="headerGroupsListGroup"/></center></th>
					<th><center><fmt:message key="headerGroupsListYear"/></center></th>
					<th>&nbsp;</th>
					<th>&nbsp;</th>
			  	</tr>
			  	<tbody id="groupstabletbody"></tbody>
			</table>
			</div>
		</div>
	</body>
</html>