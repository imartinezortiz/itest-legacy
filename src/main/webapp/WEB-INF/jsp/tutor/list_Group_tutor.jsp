<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>
<%@ page import="com.cesfelipesegundo.itis.model.Group" %>

<% 
	Group group = (Group)request.getAttribute("currentGroup");
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addBundleStep("textMain","");
	breadCrumb.addStep(group.getCourse().getName()+" ("+group.getName()+")","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="tutor"/>
	<jsp:param name="menu" value="tutor"/>
	<jsp:param name="mathml" value="mathml"/>
</jsp:include>

<script type='text/javascript'>

	function confirmar(groupCourseNameSource,anioSource,nombreSource,groupCourseName,anio,nombre,groupId){
		 if (confirm('<fmt:message key="msgConfirmImport"/>'+nombre+" "+anio+" ("+groupCourseName+")"+' <fmt:message key="msgConfirmImport2"/>'+nombreSource+" "+anioSource+" ("+groupCourseNameSource+")"+' <fmt:message key="msgConfirmImport3"/>')){ 
			 document.getElementById('form'+groupId).submit();
		    } 
		} 
				
	// Function to display the symbols and divs:
	function showYear(year) {
		// Show div
		document.getElementById('div'+year).style.display='block';
		// Show "minus"
		document.getElementById('minus'+year).style.display='block';		
		// Hide plus
		document.getElementById('plus'+year).style.display='none';
	} // showYear

	// Function to hide the symbols and divs:
	function hideYear(year) {
		// Hide div
		document.getElementById('div'+year).style.display='none';
		// Hide "minus"
		document.getElementById('minus'+year).style.display='none';		
		// Show plus
		document.getElementById('plus'+year).style.display='block';
	} // showYear	
	
</script>

		<div id="contenido">
			<p><fmt:message key="tutorTextSelectGroupImport" /></p>
			<hr>
			
			<%--
			   Con el tag c forEach, se recorren los elementos del modelo, contenidos por un objeto de tipo TutorGroupImport.
			   For each different year, a div is created, but only the first one is displayed.
			--%>
			<c:set value="${currentGroup}" var="currentGroup" scope="page" />
			  <c:set value="0" var="ant_anio" scope="page" />
			  <c:set value="0" var="i" scope="page" />
			  <c:forEach items="${groups}" var="group">
				 <c:set value="${group.year}" var="anio" scope="page" />
				 <c:choose>	
					 <c:when test="${group.id ne currentGroup.id}">
						 <c:choose>
						 	 <%-- Groups of the first year have to be displayed --%>
							 <c:when test="${ant_anio eq '0'}">
							    <table align="center"><tr>
							 	  <td><h2><c:out value="${group.year}"/></h2></td>
							 	  <td>&nbsp;</td><td><img id="plus${group.year}" src="${pageContext.request.contextPath}/imagenes/mas.jpg" onclick="javascript:showYear('${group.year}');" style="display:block; border:none;"/>
							 	      <img id="minus${group.year}" src="${pageContext.request.contextPath}/imagenes/menos.jpg" onclick="javascript:hideYear('${group.year}');" style="display:none; border:none;"/></td>
							 	</tr></table>
							 	<div id="div${group.year}" style="display:none;">
							 	 <table id="groupsList${group.year}" class="tabladatos">
							 	 <tr>
							 	 <th style="width:70%"><fmt:message key="scopeGroup"/></th>
								 <th><center><fmt:message key="taskTitleImports"/></center></th>
							 	 </tr>
							 </c:when>
							 <c:when test="${anio ne ant_anio}">
									 	</table>
									 	</div>
									 	<table align="center"><tr>
									 	  <td><h2><c:out value="${group.year}"/></h2></td>
									 	  <td>&nbsp;</td><td><img id="plus${group.year}" src="${pageContext.request.contextPath}/imagenes/mas.jpg" onclick="javascript:showYear('${group.year}');" style="display:block; border:none;"/>
									 	   	<img id="minus${group.year}" src="${pageContext.request.contextPath}/imagenes/menos.jpg" onclick="javascript:hideYear('${group.year}');" style="display:none; border:none;"/></td>
										</tr></table>					 	   	
									 	<div id="div${group.year}" style="display:none;">
									 	<table  id="groupsList${group.year}" class="tabladatos">
									 	<tr>
									 	 <th style="width:70%"><fmt:message key="scopeGroup"/></th>
										 <th><center><fmt:message key="taskTitleImports"/></center></th>
									 	 </tr>
							 </c:when>
						 </c:choose>
						
					     <c:choose>
					     	<c:when test="${i eq 1}">
					     		<script>
					     			showYear('${group.year}');
					     		</script>
					     	</c:when>
					     </c:choose>
							  <tr>
							     <form id="form${group.id}" action="${pageContext.request.contextPath}/tutor/managegroup.itest?method=importGroup" method="post">
							        <input name="idgroup" value="${group.id}" type="hidden">
							     </form>
							     <td><c:out value="${group.course.name}"/> (<c:out value="${group.name}"/>)</td>
							     
							     		<td><center><input type="button" value="<fmt:message key="taskTitleImports"/>" onclick="javascript:confirmar('${currentGroup.name}','${currentGroup.year}','${currentGroup.course.name}','${group.name}','${group.year}','${group.course.name}','${group.id}');"></center></td>
								     
							     </tr>
						     		     
							     <c:set value="${anio}" var="ant_anio" scope="page" />
							     <c:set value="${i+1}" var="i" scope="page" />
			     			</c:when>
			     </c:choose>
			     
              </c:forEach>
              <%-- If there was any element, a final label is needed --%>
              <c:if test="${!empty groups}">
              		</table>
	              </div>
              </c:if>
			
		</div>
	</body>
</html>