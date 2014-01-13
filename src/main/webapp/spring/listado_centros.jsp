<%@ include file="/spring/include.jsp" %>
<html>
<head><title><fmt:message key="listado_centros.title"/></title></head>
<body>
<h1><fmt:message key="listado_centros.heading"/></h1>
<p><fmt:message key="listado_centros.saludo"/></p>
<!-- 
	Ya no utilizo más el tag fmt 
-->
<h3>Centros</h3>
<!-- 
	Con el tag forEach, recorro los elementos del modelo, contenidos por un 
	objeto de tipo ListadoCentrosController
 -->
<c:forEach items="${model.centros}" var="centro">
  <c:out value="${centro.idcentro}"/> <c:out value="${centro.nombre}"/> <c:out value="${centro.localidad}"/> <c:out value="${centro.cpostal}"/><br><br>
</c:forEach>
<br>
<a href="<c:url value="/spring/nuevo_centro.itest"/>">Nuevo centro</a><br>
<a href="<c:url value="/spring/listado_usuarios.itest"/>">Listado de usuarios</a>
<br>
</body>
</html>
