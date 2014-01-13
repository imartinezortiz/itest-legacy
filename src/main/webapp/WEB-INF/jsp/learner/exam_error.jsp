<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fmt_rt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>

<% 
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addBundleStep("examError","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="learner"/>
</jsp:include>
		
<div id="menu"> 
  <ul>
    <li> <a href="${pageContext.request.contextPath}/learner/index.itest"><fmt:message key="textMain" /></a> </li>
    <li> <a href="${pageContext.request.contextPath}/Logout"><fmt:message key="exitButton" /></a> </li>
  </ul>
</div>

<div id="contenido">
	<br><br>
	<p><b><fmt:message key="examNotValided" /></b></p>
	<br>
	<p>
		<c:choose>
			<c:when test="${keyError eq 'reloadExamError'}">
				<fmt:message key="reloadExamError" />
			</c:when>
			<c:when test="${keyError eq 'concurrentExamError'}">
				<fmt:message key="concurrentExamError" />
			</c:when>
			<c:when test="${keyError eq 'examAlreadyStarted'}">
				<fmt:message key="examAlreadyStarted" />
			</c:when>
			<c:when test="${keyError eq 'examNotDefined'}">
				<fmt:message key="examNotDefined" />
			</c:when>
		</c:choose>
	</p>
	<br><br>
	
	<form action="${pageContext.request.contextPath}/learner/index.itest" method="post">
		<p><input type="submit" value="<fmt:message key="textMain" />"></p>
	</form>			
</div>

	</body>
</html>
