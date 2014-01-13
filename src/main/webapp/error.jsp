<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=utf8" language="java"  isErrorPage="true" %>
<html>
<head>
		<title>iTest</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/general.css" />
		<!--[if gte IE 5.5]>
		<![if lt IE 7]>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/arregloposie.css"/>
		<![endif]>
		<![endif]-->
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
</head>
<body>
		<div id="cabecera">
			<fmt:message key="headerErrorPage" />
		</div>
		
		<div id="logo1">
		  	<c:if test="${empty param.exam}"><a href="${pageContext.request.contextPath}"> </c:if>
		  		<img src="${pageContext.request.contextPath}/imagenes/logoitest.gif" alt="Logo iTest" title="iTest" />
		 	<c:if test="${empty param.exam}"></a> </c:if>
		</div>
		
		<div id="logo2">
			<c:if test="${empty param.exam}"><a href="http://www.gredossandiego.com/"> </c:if>
				<img src="${pageContext.request.contextPath}/imagenes/logo_gredos.jpg" alt="Logo GSD" title="Gredos San Diego" />
			<c:if test="${empty param.exam}"></a> </c:if>
		</div>
		
		<div id="menu">
			<ul>
				<li>
				</li>
			</ul>
		</div>
		<div id="contenido">
            <br/><br/>
            <legend><b><fmt:message key="textError" /></b></legend>
            <br/><br/><hr/><br/>
		    <a id="loggingButton" href="${pageContext.request.contextPath}/common/Home">
		        <img src="${pageContext.request.contextPath}/imagenes/logoitest.gif" border="0" alt="Logotipo de iTest" title="iTest" />
	        </a><br/><br/><hr/><br/>
	        <br/><br/>
            <legend><b><fmt:message key="msgColaboration" /></b></legend>&nbsp;&nbsp;
            <a href="mailto:itest@ajz.ucm.es"><img style="border:none; vertical-align: middle;" src="${pageContext.request.contextPath}/imagenes/icono_mail.gif" alt="itest@ajz.ucm.es"/></a>
        </div>
</body>