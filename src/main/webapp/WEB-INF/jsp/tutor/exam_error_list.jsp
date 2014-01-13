<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fmt_rt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ page errorPage="/error.jsp" %>

<head>
	<title>iTest</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/general.css" />
	<c:if test="${param.userRole eq 'learner'}">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/alumno.css" />
	</c:if>
	<c:if test="${param.userRole eq 'tutor' or param.userRole eq 'tutorav'}">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/profesor.css" />
	</c:if>
	<!--[if gte IE 5.5]>
	<![if lt IE 7]>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/arregloposie.css"/>
	<![endif]>
	<![endif]-->
	<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF8">		

</head>
		
<div id="cabecera">
	<fmt:message key="tutorExamErrorListTitle" /> 
</div>
		
<div id="logo1">
  	<img src="${pageContext.request.contextPath}/imagenes/logoitest.gif" alt="Logo iTest" title="iTest" />
</div>

<div id="logo2">
	<img src="${pageContext.request.contextPath}/imagenes/logo_gredos.jpg" alt="Logo GSD" title="Gredos San Diego" />
</div>

<div id="menu"><ul><li>&nbsp;</li></ul></div>

<div id="contenido">
<ul>
	<c:forEach var="msg" items="${mlist}">
		<li><fmt_rt:message key="${msg.key}">
		  <c:forEach var="arg" items="${msg.params}">
		    <fmt_rt:param value="${arg}"/>
		  </c:forEach>
		  </fmt_rt:message>
		</li>
	</c:forEach>
	
	<c:if test="${empty mlist}">
		<li><fmt:message key="noErrorsInConfigExam"/></li>
	</c:if>
	
</ul>
</div>		
	</body>
</html>