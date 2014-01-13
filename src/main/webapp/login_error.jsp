<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
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
			<a href="" title="página principal"><b></b></a>
		</div>
		<div id="logo1">
			<a href="login.html">
				<img src="${pageContext.request.contextPath}/imagenes/logoitest.gif" alt="Logotipo de iTest" title="iTest" />
			</a>
		</div>
		<div id="logo2">
	      <a href="http://www.gredossandiego.com/">
			<img src="${pageContext.request.contextPath}/imagenes/logo_gredos.jpg" alt="Logo GSD" title="Gredos San Diego" />
		  </a>
		</div>
		<div id="menu">
			<ul>
				<li>
				 
				</li>
			</ul>
		</div>
		<div id="contenido">
           <c:url var="url" value="/common/Home"/>
           <p><a href="${url}"><fmt:message key="loginError" /></a></p>
		   <br/><br/><hr/><br/>
		   <a id="loggingButton" href="${pageContext.request.contextPath}/common/Home">
		        <img src="${pageContext.request.contextPath}/imagenes/logoitest.gif" border="0" alt="Logotipo de iTest" title="iTest" />
	        </a><br/><br/><hr/><br/>
        </div>
   </body>
</html>