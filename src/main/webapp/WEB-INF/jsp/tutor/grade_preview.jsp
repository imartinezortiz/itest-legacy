<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fmt_rt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>

<% 
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addBundleStep("grade","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="learner"/>
</jsp:include>
		
<div id="menu"> 
  <ul>
  	<li> <a href="javascript:window.close()"><fmt:message key="exitButton" /></a> </li>
  </ul>
</div>

<div id="contenido">
	<br><br>
	<p><b><fmt:message key="yourcalif" /></b></p>
	<br>
	<p class="grade"><fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${grade}" /></p>
	<p><b>(<fmt:message key="outOfMaxGrade"/> <c:out value="${maxgrade}"/>)</b></p>
	<br><br>
	
	<form action="javascript:window.close()" method="post">
		<p><input type="submit" value="<fmt:message key="exitButton" />"></p>
	</form>			
</div>

	</body>
</html>
