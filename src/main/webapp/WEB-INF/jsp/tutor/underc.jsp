<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>

<% 
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="tutor"/>
	<jsp:param name="mathml" value="mathml"/>
</jsp:include>

		<div id="menu">
			<ul>
				<li>
					<a href="${pageContext.request.contextPath}/Logout"><fmt:message key="exitButton" /></a>
				</li>
			</ul>
		</div>
		<div id="contenido">
			<br><br>
			<h2><fmt:message key="underConstruction" /></h2>
			<br><br>
			<hr/>
			<p>&nbsp;</p>
			<p>&nbsp;</p>
			<p><a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=indexGroup"><fmt:message key="menuTasks" /></a></p>
		</div>
	</body>
</html>