<%@ include file="/spring/include.jsp" %>
<%@ taglib prefix="spring" uri="spring" %>

<!-- 
	No utilizo más los tags fmt por no pararme, pero se deberían utilizar.
	Se puden agregar condiciones c:if para utilizar esta página para un
	nuevo usuario o editar uno existente. Yo no me meto en tanto.
 -->
<html>
<head><title>Usuario</title></head>
<body>
<h1>Nuevo Usuario</h1>
<form method="POST">
  <b>DNI:</b>
  <spring:bind path="usuario.dni">
    <font color="red">
      <b><c:out value="${status.errorMessage}"/></b>
    </font>
    <br><input type="text" maxlength="9" size="10" name="dni" value="<c:out value="${status.value}"/>" />
  </spring:bind>
  <p>
  <b>Nombre:</b>
  <spring:bind path="usuario.nombre">
    <font color="red">
      <b><c:out value="${status.errorMessage}"/></b>
    </font>
    <br><input type="text" maxlength="20" size="20" name="nombre" value="<c:out value="${status.value}"/>" />
  </spring:bind>
  <p>
  <b>Apellidos:</b>
  <spring:bind path="usuario.apes">
    <font color="red">
      <b><c:out value="${status.errorMessage}"/></b>
    </font>
    <br><input type="text" maxlength="20" size="20" name="apes" value="<c:out value="${status.value}"/>" />
  </spring:bind>
  <p>
  <b>Email:</b>
  <spring:bind path="usuario.email">
    <font color="red">
      <b><c:out value="${status.errorMessage}"/></b>
    </font>
    <br><input type="text" maxlength="40" size="20" name="email" value="<c:out value="${status.value}"/>" />
  </spring:bind>
  <p>
  <b>Usuario:</b>
  <spring:bind path="usuario.usuario">
    <font color="red">
      <b><c:out value="${status.errorMessage}"/></b>
    </font>
    <br><input type="text" maxlength="10" size="10" name="usuario" value="<c:out value="${status.value}"/>" />
  </spring:bind>
  <p>
  <b>Contraseña:</b>
  <spring:bind path="usuario.passw">
    <font color="red">
      <b><c:out value="${status.errorMessage}"/></b>
    </font>
    <br><input type="text" maxlength="8" size="10" name="passw" value="<c:out value="${status.value}"/>" />
  </spring:bind>
  <p>
  <b>Ruta Foto:</b>
  <spring:bind path="usuario.ruta_foto">
    <font color="red">
      <b><c:out value="${status.errorMessage}"/></b>
    </font>
    <br><input type="text" maxlength="30" size="30" name="ruta_foto" value="<c:out value="${status.value}"/>" />
  </spring:bind>
  <p>
  <B>Centro:</B>
  <spring:bind path="usuario.centro">
    <font color="red">
      <b><c:out value="${status.errorMessage}"/></b>
    </font>
    <br>
    <select name="centroId">
      <c:forEach items="${centrosRef}" var="centro">
          <option value="<c:out value="${centro.idcentro}"/>"><c:out value="${centro.nombre}"/></OPTION>
      </c:forEach>
    </select>
  </spring:bind>
  <p>
  <input type="submit" value="Nuevo usuario"/>
</form>
<br>
<a href="<c:url value="/spring/listado_usuarios.itest"/>">Listado de usuarios</a>
</body>
</html>