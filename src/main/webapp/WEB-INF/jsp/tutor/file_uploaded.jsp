<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
  
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>i-Test</title>
<script type="text/javascript">
	parent.<c:out value="${callback}"/>('<c:out value="${filename}" escapeXml="false"/>');
</script>
</head>
<body>
Fichero Subido
</body>
</html>