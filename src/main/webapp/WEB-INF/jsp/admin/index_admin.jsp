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
	<jsp:param name="userRole" value="admin"/>
	<jsp:param name="menu" value="admin"/>
</jsp:include>

		<div id="contenido">
			<br>
			
			<div class="divDosIzq">
  				<p><b><fmt:message key="taskTitleInstitutions" /></b></p>
  				<hr>
				<ul class="listaopcionesgroup">							
					<li><a href="${pageContext.request.contextPath}/admin/admin.itest?method=newInstitution"><fmt:message key="taskAddInstitution" /></a> </li>
					<li><a href="${pageContext.request.contextPath}/admin/admin.itest?method=showInstitutionsList"><fmt:message key="taskShowInstitutionsList" /></a></li>
				</ul>
			</div>
			<div class="divDosDer">			
				<p><b><fmt:message key="taskTitleCourses" /></b></p>
  				<hr>
				<ul class="listaopcionesgroup">							
					<li><a href="${pageContext.request.contextPath}/admin/admin.itest?method=newCourse"><fmt:message key="taskAddCourse" /></a> </li>
					<li><a href="${pageContext.request.contextPath}/admin/admin.itest?method=showCoursesList"><fmt:message key="taskShowCoursesList" /></a></li>
				</ul>
			</div>
			
			<div class="divcentro">
			</div>
			<div class="divDosIzq">
  				<p><b><fmt:message key="taskTitleNextExams" /></b></p>
  				<hr>
				<ul class="listaopcionesgroup">							
					<li><a href="${pageContext.request.contextPath}/admin/admin.itest?method=activeExams"><fmt:message key="taskShowActiveExams" /></a> </li>
					<li><a href="${pageContext.request.contextPath}/admin/admin.itest?method=nextExams"><fmt:message key="taskShowNextExams" /></a> </li>
					<li><a href="${pageContext.request.contextPath}/admin/admin.itest?method=previousExams"><fmt:message key="taskShowPreviousExams" /></a> </li>
					<li><a href="${pageContext.request.contextPath}/admin/admin.itest?method=searchUsers"><fmt:message key="taskShowSearch" /></a> </li>
				</ul>
			</div>
			
			<div class="divDosDer">
				<p><b><fmt:message key="taskShowStats" /></b></p>
  				<hr>
				<ul class="listaopcionesgroup">							
					<li><a href="${pageContext.request.contextPath}/admin/admin.itest?method=goStatsBySubject"><fmt:message key="taskShowStatsBySubject" /></a> </li>
					<li><a href="${pageContext.request.contextPath}/admin/admin.itest?method=goStatsByCenter"><fmt:message key="taskShowStatsByCenter" /></a> </li>
					<li><a href="${pageContext.request.contextPath}/admin/admin.itest?method=go100LastConections"><fmt:message key="taskShow100LastConections"/></a> </li>
				</ul>
			</div>	
			
			<div class="divcentro">
			<div class="divDosIzq">
  				<p><b><fmt:message key="labelTests" /></b></p>
  				<hr>
				<ul class="listaopcionesgroup">							
					<li><a href="${pageContext.request.contextPath}/admin/admin.itest?method=loadTests"><fmt:message key="labelLoadExams" /></a> </li>
				</ul>
			</div>
			
			<div class="divDosDer">
				
			</div>	
			
			<div class="divcentro">
			</div>
			
			<br><hr><br>
		    <p><fmt:message key="tutorTextSelectTask" /></p>
			<br><hr><br>
		    <p><a href="${pageContext.request.contextPath}/admin/jamonReport"><fmt:message key="adminShowProfileInfo" /></a></p>
			
		</div>
	</body>
</html>