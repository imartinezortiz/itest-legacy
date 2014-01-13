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
			cellelement.innerHTML = '<a href="institution.itest?method=deleteGroup&idgroup='+group.id+'" onclick="return confirm (\'<fmt:message key="confirmDeleteGroup"/>\n<fmt:message key="alertDeleteGroup"/>\')"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="labelDeleteGroup"/>" title="<fmt:message key="labelDeleteGroup"/>" border="none"></a>';
			rowelement.appendChild(cellelement);

			tbodyelement.appendChild(rowelement);
		}
			if (groups.length == 0) {
				if(groups!=''){
			       rowelement = document.createElement('tr');
			       cellelement = document.createElement('td');
				   cellelement.colSpan=5;
			       cellelement.setAttribute("align","center");
				   cellelement.innerHTML = "<fmt:message key="noAvailableGroups"/>";
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

	function groupFilter(){
		course = document.getElementById('courseFilter').value;
		group = document.getElementById('groupFilter').value;
		year = document.getElementById('yearFilter').value;
		iTestLockPage();
		AdminInstitutionMgmt.filterByGroup(course,group,year,{callback:updateTable,
															 timeout:callBackTimeOut,
															 errorHandler:function(message) { iTestUnlockPage('error');} });
	}
</script>

		<div id="contenido">
			<div>
				<fieldset>
					<legend><fmt:message key="labelFilterTitle"/></legend>
					<form>
						<div class="divDosIzq">
							<ul class="listaopciones">
								<li><fmt:message key="headerGroupsListCourse"/> : <input type="text" id="courseFilter"/></li>
								<li><fmt:message key="headerGroupsListGroup"/> : <input type="text" id="groupFilter"/></li>
							</ul>
						</div>
						<div class="divDosDer">
						<ul class="listaopciones">
							<li>
								<fmt:message key="headerGroupsListYear"/> : <input type="text" id="yearFilter"/>
							</li>
						</ul>
						</div>
						<div class="divcentro">
							<input type="reset" value="<fmt:message key="msgbuttonReset"/>" onclick="javascript:updateTable('');"/>
						</div>
					</form>
					<input type="button" value="<fmt:message key="buttonFilterRun"/>" onclick="javascript:groupFilter();">
					
				</fieldset>
			</div>
			<table id="groupstable" class="tabladatos">
			  <col width="*"/>
			  <col width="0*"/>
			  <col width="0*"/>
			  <col width="0*"/>
			  <col width="0*"/>	
			  <tr>
				<th><center>
					<c:choose>
						<c:when test="${orderby eq 'course' and reverse ne 'yes'}">
							<a href="?method=showGroupsList&orderby=course&reverse">
						</c:when>
						<c:otherwise>
							<a href="?method=showGroupsList&orderby=course">
						</c:otherwise>
					</c:choose>
					<fmt:message key="headerGroupsListCourse"/></a></center>
				</th>
				<th><center>
					<c:choose>
						<c:when test="${orderby eq 'name' and reverse ne 'yes'}">
							<a href="?method=showGroupsList&orderby=name&reverse">
						</c:when>
						<c:otherwise>
							<a href="?method=showGroupsList&orderby=name">
						</c:otherwise>
					</c:choose>

					<fmt:message key="headerGroupsListGroup"/></a></center>
				</th>
				<th><center>
					<c:choose>
						<c:when test="${orderby eq 'year' and reverse ne 'yes'}">
							<a href="?method=showGroupsList&orderby=year&reverse">
						</c:when>
						<c:otherwise>
							<a href="?method=showGroupsList&orderby=year">
						</c:otherwise>
					</c:choose>

					<fmt:message key="headerGroupsListYear"/></a></center>
				</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
			  </tr>
			 <tbody id="groupstabletbody">
			
  			 <c:forEach items="${groups}" var="group">
 			  <tr id="row${institution.id}">
 			    <td><c:out value="${group.course.name}"/></td>
 			    <td><c:out value="${group.name}"/></td>
 			    <td><c:out value="${group.year}"/></td>
				<td>
				   <a href="institution.itest?method=editGroup&idgroup=${group.id}"><img src="${pageContext.request.contextPath}/imagenes/editar.gif" alt="<fmt:message key="labelEditGroup"/>" title="<fmt:message key="labelEditGroup"/>" border="none"></a>
				</td>
				<td>
					<a href="institution.itest?method=deleteGroup&idgroup=${group.id}" onclick="return confirm ('<fmt:message key="confirmDeleteGroup"/>\n<fmt:message key="alertDeleteGroup"/>')">
					<img src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="labelDeleteGroup"/>" title="<fmt:message key="labelDeleteGroup"/>" border="none"></a>
				</td>
			  </tr>
			 </c:forEach>
			 		 
			<c:choose>
				 <c:when test="${empty groups}">
				  <tr>
				    <td align="center" colspan="5"><fmt:message key="noAvailableGroups"/></td>
				  </tr>
				 </c:when>
				 <c:otherwise>
				  <tr>
				    <td align="center" colspan="5"><hr/></td>
				  </tr>
				  <tr>
				    <td align="center" colspan="5"><b><fmt:message key="totalLabel"/> ${fn:length(groups)} </b></td>
				  </tr>				 
				 </c:otherwise>
			 </c:choose>
			 
			 
			</tbody>
			</table>
		</div>
		<c:if test="${!empty groupDeleted}">
			<script>
				alert('<fmt:message key="alertDeletedGroup"/>');
			</script>
		</c:if>
	</body>
</html>