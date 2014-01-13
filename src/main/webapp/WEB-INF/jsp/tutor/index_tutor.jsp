<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>

<% 
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addBundleStep("textMain","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="tutor"/>
	<jsp:param name="mathml" value="mathml"/>
</jsp:include>

<script type='text/javascript'>

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

		<div id="menu">
			<ul>
				<li>
					<a href="${pageContext.request.contextPath}/common/index.itest?method=changePassword"><fmt:message key="changePasswd" /></a>
				</li>
				<li> <a href="${pageContext.request.contextPath}/common/index.itest?method=checkPlugins"><fmt:message key="checkPlugins" /></a> </li>
				<li>
					<a href="${pageContext.request.contextPath}/Logout"><fmt:message key="exitButton" /></a>
				</li>
			</ul>
		</div>
		<div id="contenido">
			<p><fmt:message key="tutorTextSelectGroup" /></p>
			<hr>
			<ul class="listaopciones">
			<%--
			   Con el tag c forEach, se recorren los elementos del modelo, contenidos por un objeto de tipo IndexTutorController.
			   For each different year, a div is created, but only the first one is displayed.
			--%>
			  <c:set value="0" var="ant_anio" scope="page" />
			  <c:forEach items="${groups}" var="group">
				 <c:set value="${group.year}" var="anio" scope="page" />
				 <c:choose>
				 	 <%-- Groups of the first year have to be displayed --%>
					 <c:when test="${ant_anio eq '0'}">
					    <table align="center"><tr>
					 	  <td><span style="font-size:22; font-weight:bold;"><c:out value="${group.year}"/></span></td>
					 	  <td>&nbsp;</td><td><img id="plus${group.year}" src="${pageContext.request.contextPath}/imagenes/mas.jpg" onclick="javascript:showYear('${group.year}');" style="display:none; border:none;"/>
					 	      <img id="minus${group.year}" src="${pageContext.request.contextPath}/imagenes/menos.jpg" onclick="javascript:hideYear('${group.year}');" style="display:block; border:none;"/></td>
					 	</tr></table>
					 	<div id="div${group.year}">
					 </c:when>
					 <c:when test="${anio ne ant_anio}">
					 	</div>
					 	<table align="center"><tr>
					 	  <td><span style="font-size:22; font-weight:bold;"><c:out value="${group.year}"/></span></td>
					 	  <td>&nbsp;</td><td><img id="plus${group.year}" src="${pageContext.request.contextPath}/imagenes/mas.jpg" onclick="javascript:showYear('${group.year}');" style="display:block; border:none;"/>
					 	   	<img id="minus${group.year}" src="${pageContext.request.contextPath}/imagenes/menos.jpg" onclick="javascript:hideYear('${group.year}');" style="display:none; border:none;"/></td>
						</tr></table>					 	   	
					 	<div id="div${group.year}" style="display:none;">
					 </c:when>
				 </c:choose>
			     <form id="form${group.id}" action="${pageContext.request.contextPath}/tutor/managegroup.itest?method=indexGroup" method="post">
			        <input name="idgroup" value="${group.id}" type="hidden">
			     </form>
			     <li><a href="javascript:document.getElementById('form${group.id}').submit();"><c:out value="${group.course.name}"/> (<c:out value="${group.name}"/>)</a></li>
			     <c:set value="${anio}" var="ant_anio" scope="page" />
              </c:forEach>
              <%-- If there was any element, a final label is needed --%>
              <c:if test="${!empty groups}">
	              </div>
              </c:if>
			</ul>
		</div>
	</body>
</html>