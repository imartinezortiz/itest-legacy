<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>
<%@ page import="java.util.ResourceBundle" %>


<% 
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addBundleStep("textMain","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="kid"/>
</jsp:include>
		
<div id="menu"> 
  <ul>
  	<li> <a href="${pageContext.request.contextPath}/common/index.itest?method=checkPlugins"><fmt:message key="checkPlugins" /></a> </li>
    <li> <a href="${pageContext.request.contextPath}/Logout"><fmt:message key="exitButton" /></a> </li>
  </ul>
</div>


<div id="contenido">
	<c:choose>
		<c:when test="${!empty pendingExams}">
			<c:forEach items="${pendingExams}" var="exam" varStatus="i">
				<form id="formrealizarexamen${exam.id}" class="formrealizarexamenkid" action="${pageContext.request.contextPath}/learner/newexam.itest?method=goNewExam" method="post">
					<input name="idexam" type="hidden" value="${exam.id}" />			
					<input type="submit" class="buttonkid buttonkidcolor${i.count mod 4}" value="<c:out value="${exam.group.course.name}"/> (<c:out value="${exam.group.name}"/>) - <c:out value="${exam.title}"/>" />
				</form>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<p><fmt:message key="noAvailableExams"/></p><br/><br/><br/>
		</c:otherwise>
	</c:choose>		
	<hr/><hr/>
</div>

</body>
	
</html>
