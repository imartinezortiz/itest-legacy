<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>

<% 
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addBundleStep("textChangePass","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="menu" value="common"/>
	<jsp:param name="userRole" value="${user.role}"/>
</jsp:include>

		<div id="contenido">
			<br/><br/><br/><br/>
			<c:choose>
				<c:when test="${!resp eq true}">
					<h2 style="margin:30pt;"><fmt:message key="msgErrorChangePasswd"/></h2>				
				</c:when>
				<c:otherwise>
					<h2 style="margin:30pt;"><fmt:message key="msgPasswdChanged"/></h2>
				</c:otherwise>
			 </c:choose>
		</div>
	</body>
		
	</body>
</html>