<%@ include file="/spring/include.jsp" %>
<html>
<head><title>Usuarios</title></head>
<body>
<h1>Usuarios</h1>
<p>Saludo</p>
<!-- 
	Ya no utilizo más el tag fmt 
-->
<h3>Usuarios</h3>
<!-- 
	Con el tag forEach, recorro los elementos del modelo, contenidos por un 
	objeto de tipo ListadoUsuariosController
 -->
<c:forEach items="${model.usuarios}" var="usuario">
  <c:out value="${usuario.idusu}"/> <c:out value="${usuario.nombre}"/> <c:out value="${usuario.apes}"/> <c:out value="${usuario.dni}"/><br><br>
</c:forEach>
<br>
<a href="<c:url value="/spring/nuevo_usuario.itest"/>">Nuevo usuario</a><br>
<a href="<c:url value="/spring/listado_centros.itest"/>">Listado de centros</a>
<br>
</body>
</html>
