<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page errorPage="/error.jsp" %>

<html>
<head>
<title>iTest</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/general.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/alumno.css" />
<%-- Maybe contains math --%>
<script type="text/javascript" src="${pageContext.request.contextPath}/common/resources/ASCII_MathML.js"></script>
<script type="text/javascript">
	function resize(){
		window.moveTo(0,0);
		window.resizeTo(screen.width,screen.height/2);
	}
</script>
</head>
<body onload="resize()">
	

	<center>
	<jsp:include page="/WEB-INF/jsp/common/question.jsp" flush="true">
		<jsp:param name="view" value="preview"/>
		<jsp:param name="role" value="${grouprole}"/>
	</jsp:include>
	</center>
</body>
</html>