<%@ include file="/spring/include.jsp" %>
<%@ taglib prefix="spring" uri="spring" %>

<!-- 
	No utilizo más los tags fmt por no pararme, pero se deberían utilizar.
	Se puden agregar condiciones c:if para utilizar esta página para un
	nuevo centro o editar uno existente. Yo no me meto en tanto.
 -->
<html>
<head><title>Nuevo Centro</title></head>
<body>
<h1>Nuevo Centro</h1>
<form method="POST">
  <b>Código:</b>
  <spring:bind path="centro.cod">
    <font color="red">
      <b><c:out value="${status.errorMessage}"/></b>
    </font>
    <br><input type="text" maxlength="10" size="10" name="cod" value="<c:out value="${status.value}"/>" />
  </spring:bind>
  <p>
  <b>Nombre:</b>
  <spring:bind path="centro.nombre">
    <font color="red">
      <b><c:out value="${status.errorMessage}"/></b>
    </font>
    <br><input type="text" maxlength="50" size="30" name="nombre" value="<c:out value="${status.value}"/>" />
  </spring:bind>
  <p>
  <b>Dirección:</b>
  <spring:bind path="centro.direccion">
    <font color="red">
      <b><c:out value="${status.errorMessage}"/></b>
    </font>
    <br><input type="text" maxlength="50" size="30" name="direccion" value="<c:out value="${status.value}"/>" />
  </spring:bind>
  <br>
  <p>
  <b>Localidad:</b>
  <spring:bind path="centro.localidad">
    <font color="red">
      <b><c:out value="${status.errorMessage}"/></b>
    </font>
    <br><input type="text" maxlength="50" size="30" name="localidad" value="<c:out value="${status.value}"/>" />
  </spring:bind>
  <br>
  <p>
  <b>Código postal:</b>
  <spring:bind path="centro.cpostal">
    <font color="red">
      <b><c:out value="${status.errorMessage}"/></b>
    </font>
    <br><input type="text" maxlength="5" size="5" name="cpostal" value="<c:out value="${status.value}"/>" />
  </spring:bind>
  <br>
  <p>
  <b>Provincia:</b>
  <spring:bind path="centro.provincia">
    <font color="red">
      <b><c:out value="${status.errorMessage}"/></b>
    </font>
    <br><input type="text" maxlength="15" size="10" name="provincia" value="<c:out value="${status.value}"/>" />
  </spring:bind>
  <br>
  <input type="submit" value="Nuevo centro"/>
</form>
<br>
<a href="<c:url value="/spring/listado_centros.itest"/>">Listado de centros</a>
</body>
</html>