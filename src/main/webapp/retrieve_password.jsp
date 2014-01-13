<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page errorPage="/error.jsp" %>
<%@ page import="nl.captcha.Captcha" %>
<html>
  <head>
    <title>iTest</title>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.js"/>
	<script language=JavaScript></script>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/general.css" />
    <!--[if gte IE 5.5]>
    <![if lt IE 7]>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/arregloposie.css"/>
    <![endif]>
    <![endif]-->
	<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
    
    <script language="javascript" type="text/javascript">
       if (window.name != "itest"){
         window.open(window.location, "itest", " ");
         window.close();
       }

       function retrievePassword(){
			var userName = $('#userName').val();
			$('#divDclick').show('slow',function(){});
			$.ajax({
				  url: "${pageContext.request.contextPath}/api?method=retrievePassword",
				  context: "text/xml",
				  data: "userName="+userName,
				  success: function(xml){
					  $('#divDclick').hide('slow',function(){});
					  var resultado = $(xml).find('resultado').text();
					  if(resultado == 'true'){
						alert("<fmt:message key="labelEmailSend"/>");
						$('#formulario').submit();
					  }else if(resultado == 'false'){
						  alert("<fmt:message key="labelNoEmailAsigned"/>");
					  }else if(resultado == 'noMailSent'){
						  alert("<fmt:message key="labelMailNotSent"/>");
					  }else{
						alert("<fmt:message key="labelUserNoExist"/>");
					  }
				  },
			  	  error: function(jqXHR, textStatus, errorThrown){
					  alert("<fmt:message key="transactionError"/>");
				  }
				});
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
      <a href="http://itis.cesfelipesegundo.com">
        <img src="${pageContext.request.contextPath}/imagenes/logo_felipeii.jpg" alt="Logotipo del CES Felipe II" title="CES Felipe II" />
      </a>
    </div>
    <div id="menu">
      <ul>
        <li>
         
        </li>
      </ul>
    </div>
    <div id="contenido">
    <% // We're doing this in a JSP here, but in your own app you'll want to put
    // this logic in your MVC framework of choice.
    Captcha captcha = (Captcha) session.getAttribute(Captcha.NAME);
    request.setCharacterEncoding("UTF-8"); // Do this so we can capture non-Latin chars
    String answer = request.getParameter("answer");
    if (captcha.isCorrect(answer)) { %>
    			
			    
    		<p>
    				<h2><fmt:message key="labelRecoverPassword"/></h2>
    				
    		</p>
			<label>
				<fmt:message key="textLoginUser"/>
			</label>
			<input type="text" id="userName" name="userName"/>
			<br/>
			<br/>
			<input type="button" onclick="javascript:retrievePassword();" value="<fmt:message key="labelSend"/>"/>
    		<br/>
			<br/>
    <% }else{ %>
    	<form id="volver" action="${pageContext.request.contextPath}/captcha.jsp"></form>
    	<script>
    	$('#volver').submit();
    	</script>
    <% } %>
    	<a href="${pageContext.request.contextPath}/login.jsp"><fmt:message key="labelBack2LoginPage"/></a>
    </div>
    <form id="formulario" method="POST" action="${pageContext.request.contextPath}/login.jsp">
			</form>
			<div id="divDclick"><div id="divDclickMsg"><br/><br/><p><fmt:message key="waitMsg"/></p></div></div>
</body>
</html>