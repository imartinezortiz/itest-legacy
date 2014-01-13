<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page errorPage="/error.jsp" %>
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
    
	<script type='text/javascript'>
		
		/* Go to student demo */
		function goStudentDemo() {
		   // Start session for a given user (demo user)
		   document.getElementById("j_username").value = "udemo";
		   document.getElementById("j_password").value = "u";
		   // Submit form
		   document.getElementById("formlogin").submit();
	    }
	    
	</script>
    
    <script language="javascript" type="text/javascript">
       if (window.name != "itest"){
         window.open(window.location, "itest", " ");
         window.close();
       }
    </script>
  </head>
  <body>
    <div id="cabecera">
      <a href="" title="página principal"><b></b></a>
    </div>
    <div id="logo1">
      <a href="login.jsp">
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
      <form id="formlogin" name="formlogin" method="post" action="j_security_check">
      <br><br>
      <p><fmt:message key="textWelcomeLogin" /></p>
      <center>
      <table>
                <tr>
                <td><fmt:message key="textLoginUser" /></td>
                  <td>&nbsp;</td>
                  <td><input id="j_username" type="text" name="j_username"></td>
                </tr>
              <tr>
                  <td><fmt:message key="textPassUser" /></td>
                <td>&nbsp;</td>
                  <td><input id="j_password" type="password" name="j_password"></td>
              </tr>
            </table>
      </center>
      <p><input type="submit" name="Submit" value="<fmt:message key="enter" />">
      </form>
	  <br/><br/><br/>
	  <a href="${pageContext.request.contextPath}/captcha.jsp"><fmt:message key="labelForgottenPassword"/></a>
	  <br/><hr/><br/>
		 <img src="${pageContext.request.contextPath}/imagenes/logoitest.gif" border="0" alt="Logotipo de iTest" title="iTest" />
	  <br/><br/><hr/><br/>
  	  <br/><br/><p><span style="font-size:16; font-weight:bold;"><a href="#" onClick="javascript:goStudentDemo();"><fmt:message key="textGoStudentDemo" /></a></span></p>
    </div>
    <script type="text/javascript"  lanaguage="javascript">
        document.formlogin.j_username.focus();
    </script>
  </body>
</html>