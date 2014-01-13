<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page errorPage="/error.jsp" %>
<jsp:useBean id="date" class="java.util.Date" />
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
	<script type="text/javascript" language="javascript">

		function changePassword(){
			var newPasswd1 = $('#newPasswd1').val();
			var newPasswd2 = $('#newPasswd2').val();
			if(newPasswd1 == newPasswd2){
				$('#divDclick').show('slow',function(){});
				$.ajax({
					  url: "${pageContext.request.contextPath}/api?method=changePassword",
					  context: "text/xml",
					  type: "POST",
					  data: "newPasswd1="+newPasswd1+"&newPasswd2="+newPasswd2,
					  success: function(xml){
						  $('#divDclick').hide('slow',function(){});
						  var resultado = $(xml).find('resultado').text();
						  if(resultado==0){
							alert("<fmt:message key="labelPasswordChangeCorrect"/>");
							$('#inicio').submit();
						  }else if(resultado==2){
							alert("<fmt:message key="labelDifferentPasswords"/>");
						  }else{
							alert("<fmt:message key="labelPasswordNotChanged"/>");
						  }
					  },
					  error: function(jqXHR, textStatus, errorThrown){
							alert("<fmt:message key="transactionError"/>");
					  }
					});
				}else{
					alert("<fmt:message key="labelDifferentPasswords"/>");
				}
		}
	</script>
	</head>
  <body>
    <div id="logo1">
      <a id="loggingLink" href="${pageContext.request.contextPath}/common/Home">
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
      	<li><fmt:message key="welcomeMenu"/></li>
      </ul>
    <div id="fecha">
    	<fmt:message key="todayIs"/> <fmt:formatDate value="${date}" type="both" dateStyle="full" timeStyle="short" />
    </div>
    </div>
    <div id="contenido" style="position:fixed; background-color:#FFFFFF; margin-top:-20px; height:315px;">
        <table align="center" style="width:50%;" CELLPADDING="8" >
			<tr>
			  	<td width="60%"><fmt:message key="labelCommonNewPassword1"/></td><td align="center"><input id="newPasswd1" type="password" name="newPasswd1" size="10" width="32"/></td>
			</tr>
			<tr>
		  		<td width="60%"><fmt:message key="labelCommonNewPassword2"/></td><td align="center"><input id="newPasswd2" type="password" name="newPasswd2" size="10" width="32"/></td>
			</tr>
 		</table>
 		<p><input id="btnSubmit" type="button" name="submit" onclick="javascript:changePassword()" value="<fmt:message key="btnCommonGoChangePassword"/>"/></p>
    </div>
    <form id="inicio" action="${pageContext.request.contextPath}/"></form>
    <div id="divDclick"><div id="divDclickMsg"><br/><br/><p><fmt:message key="waitMsg"/></p></div></div>
    
  </body>
</html>