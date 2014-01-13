<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page errorPage="/error.jsp" %>
<jsp:useBean id="date" class="java.util.Date" />
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
	<script type="text/javascript" language="javascript">
		function loginButtonClick(){
		  window.open("about:blank", "itest", "width="+screen.availWidth+",height="+screen.availHeight+",top=0,left=0, scrollbars=yes");
		}

		function checkNavigator(){
			if (navigator.appName!='Netscape'){
				alert("<fmt:message key="textNoFirefox"/>,\n<fmt:message key="TextWarningNoFirefox"/>");
			}
		}
		
		function indexLoaded(){
		  var loginButton = document.getElementById("loggingButton");
		  loginButton.target="itest";
		  loginButton.onclick=loginButtonClick;
		  var loginLink = document.getElementById("loggingLink");
		  loginLink.target="itest";
		  loginLink.onclick=loginButtonClick;
		  Cookies();
		  checkNavigator();
		}

		 //comprobacion de cookies
		 function Cookies() { 
			if (navigator.cookieEnabled ==false){
				document.getElementById('textoCookies').innerHTML="No ";
				document.getElementById('warningCookies').style.display="block"; 
			}
			else{
				document.getElementById('warningCookies').style.display="none";
			} 
		} 

		window.onload = indexLoaded;
			
	</script>
  </head>
  <body>
    <div id="logo1">
      <a id="loggingLink" href="${pageContext.request.contextPath}/common/Home">
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
      	<li><fmt:message key="welcomeMenu"/></li>
      </ul>
    <div id="fecha">
    	<fmt:message key="todayIs"/> <fmt:formatDate value="${date}" type="both" dateStyle="full" timeStyle="short" />
    </div>
    </div>
    <div id="contenido" style="position:fixed; background-color:#FFFFFF; margin-top:-20px; height:315px;">
      <br/>
      <legend><b><fmt:message key="welcomeText"/></b></legend><br/><br/><hr/><br/>
      <a id="loggingButton" href="${pageContext.request.contextPath}/common/Home">
        <img src="${pageContext.request.contextPath}/imagenes/logoitest.gif" border="0" alt="Logotipo de iTest" title="iTest" />
      </a><br/><br/><hr/><br/>
      <legend><fmt:message key="welcomeWarn"/></legend>
      <br />
      <div id="warningCookies" style="display:none; color: red;">
      <legend><br/><b><fmt:message key="welcomeWarningCookiesBegin"/><label id="textoCookies"></label><fmt:message key="welcomeWarningCookiesEnd"/></b><br /></legend>
       </div>
    </div>
    
    <div id="barra_creditos"><a style="color:white;" href="${pageContext.request.contextPath}/credits.jsp"><fmt:message key="crdHeader"/></a></div>  
      
    <div id="creditos">
    <br/>
		<br>
	    <hr/>  
		<h3 style="text-align:center;">Grupo iTest - Subgrupo de <A HREF="http://dosi.itis.cesfelipesegundo.com" target="_new"><img alt="DOSI" title="DOSI Web" style="border:none; vertical-align: middle;" src="${pageContext.request.contextPath}/imagenes/logodosibn.gif" height="54"></A>
	      &nbsp;---&nbsp;<fmt:message key="msgGetMoreInfo" />&nbsp;&nbsp;<a href="mailto:itest@ajz.ucm.es"><img style="border:none; vertical-align: middle;" src="${pageContext.request.contextPath}/imagenes/icono_mail.gif" alt="itest@ajz.ucm.es"/></a></h3>
	    <hr/>
  	    <br/>
		<table border="0" width="100%">
			<tr>
			    <td width="50%"><h3>MathML formulas displayed using ASCIIMathML.js</h3></td>
		        <td width="50%"><h3>Math support using Geogebra</h3></td>
			</tr>
			<tr>
			    <td><a target="_new" href="http://www1.chapman.edu/~jipsen/mathml/asciimath.html">http://www1.chapman.edu/~jipsen/mathml/asciimath.html</a></td>
			    <td><a target="_new" href="http://www.geogebra.org/">http://www.geogebra.org/</a></td>
            </tr>
		 </table>  
		 
    </div>
  </body>
</html>