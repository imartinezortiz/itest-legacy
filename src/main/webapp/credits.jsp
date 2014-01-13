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
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.js"/>
	<script language=JavaScript>
</script>
	<script type="text/javascript">
		function showDiv(nombre){
			$("#year").html(nombre);
			$("#2010-2011").hide('slow',function(){});
			$("#2009-2010").hide('slow',function(){});
			$("#2008-2009").hide('slow',function(){});
			$("#2007-2008").hide('slow',function(){});
			$("#2006-2007").hide('slow',function(){});
			$("#"+nombre).show('slow',function(){});
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
      <a href="http://www.gredossandiego.com/">
		<img src="${pageContext.request.contextPath}/imagenes/logo_gredos.jpg" alt="Logo GSD" title="Gredos San Diego" />
	  </a>
    </div>
    <div id="menu">
      <ul>
      	<li><a href="javascript:showDiv('2010-2011')">2010-2011</a></li>
      	<li><a href="javascript:showDiv('2009-2010')">2009-2010</a></li>
      	<li><a href="javascript:showDiv('2008-2009')">2008-2009</a></li>
      	<li><a href="javascript:showDiv('2007-2008')">2007-2008</a></li>
      	<li><a href="javascript:showDiv('2006-2007')">2006-2007</a></li>
      </ul>
    
    </div>
    <div id="contenido" style="position:fixed; background-color:#FFFFFF; margin-top:-20px; height:315px;">
      <h2><fmt:message key="academicYear"/>: <font id="year"></font></h2>
      <div id="2010-2011" style="display:none">
      <br/>
      <br/>
      <br/>
      	<table width="100%" class="tabladatos" border="1">
      			<tr>
      				<th><center><fmt:message key="creditsLabelManagement"/></center></th>
      				<th><center><fmt:message key="creditsLabelTeachersLearning"/></center></th>
      				<th><center><fmt:message key="creditsLabelDevelopmentAndMaintenance"/></center></th>
      				<th><center><fmt:message key="creditsLabelGraphicDesign"/></center></th>
      			</tr>
      			<tr>
      				<td>
						<center>
							Nuria Joglar Prieto (Becarios)<br/>
							Diego Mart&iacute;n Mart&iacute;n<br/>
							Jos&eacute; I. Hidalgo P&eacute;rez<br/>
							Jos&eacute; M. Colmenar Verdugo (Becarios)<br/>
						</center>
      				</td>
      				<td>
						<center>
							Nuria Joglar Prieto<br/>
							Diego Mart&iacute;n Mart&iacute;n<br/>
						</center>
      				</td>
      				<td>
						<center>
							Jos&eacute; M. Colmenar Verdugo (Jefe de programaci&oacute;n)<br/>
							Iv&aacute;n Mart&iacute;nez Ort&iacute;z<br/>
							Rub&eacute;n S&aacute;nchez Pelegr&iacute;n<br/>
						</center>
      				</td>
      				<td>
						<center>
							Diego Mart&iacute;n Mart&iacute;n<br/>
							Alfredo Cuesta Infante<br/>
							Rub&eacute;n S&aacute;nchez Pelegr&iacute;n<br/>
						</center>
      				</td>
      			</tr>
      	</table>
      	<br/>
      	<center>
      		<h3><fmt:message key="creditsLabelHelp"/>:</h3>
				Cristina Esteban Luis (Tester, formaci&oacute;n wikis)<br/>
				Javier P&eacute;rez Rico (Tester, formaci&oacute;n wikis)<br/>
				Ezequiel Yuste Montero (Programaci&oacute;n)<br/>
      	</center>
      </div>
      <div id="2009-2010" style="display:none">
	      <br/>
	      <br/>
	      <br/>
      	<table width="100%" class="tabladatos" border="1">
      			<tr>
      				<th><center><fmt:message key="creditsLabelManagement"/></center></th>
      				<th><center><fmt:message key="creditsLabelTeachersLearning"/></center></th>
      				<th><center><fmt:message key="creditsLabelDevelopmentAndMaintenance"/></center></th>
      				<th><center><fmt:message key="creditsLabelGraphicDesign"/></center></th>
      			</tr>
      			<tr>
      				<td>
						<center>
							Nuria Joglar Prieto<br/>
							Diego Mart&iacute;n Mart&iacute;n<br/>
							Jos&eacute; I. Hidalgo P&eacute;rez<br/>
							Rub&eacute;n S&aacute;nchez Pelegr&iacute;n (Becarios)<br/>
							Jos&eacute; M. Colmenar Verdugo (Becarios)<br/>
						</center>
      				</td>
      				<td>
						<center>
							Nuria Joglar Prieto<br/>
							Diego Mart&iacute;n Mart&iacute;n<br/>
							Rub&eacute;n S&aacute;nchez Pelegr&iacute;n<br/>
						</center>
      				</td>
      				<td>
						<center>
							Jos&eacute; M. Colmenar Verdugo (Jefe de programaci&oacute;n)<br/>
							Iv&aacute;n Mart&iacute;nez Ort&iacute;z<br/>
							Rub&eacute;n S&aacute;nchez Pelegr&iacute;n<br/>
						</center>
      				</td>
      				<td>
						<center>
							Diego Mart&iacute;n Mart&iacute;n<br/>
							Alfredo Cuesta Infante<br/>
							Rub&eacute;n S&aacute;nchez Pelegr&iacute;n<br/>
						</center>
      				</td>
      			</tr>
      	</table>
      	<center>
      		<h3><fmt:message key="creditsLabelHelp"/>:</h3>
				Enrique Gonz&aacute;lez &Aacute;lvarez-Palencia (iQuest)<br/>
				Ezequiel Yuste Montero (Programaci&oacute;n)<br/>
      	</center>
      </div>
      <div id="2008-2009" style="display:none">
      	<br/>
	      <br/>
	      <br/>
      	<table width="100%" class="tabladatos" border="1">
      			<tr>
      				<th><center><fmt:message key="creditsLabelManagement"/></center></th>
      				<th><center><fmt:message key="creditsLabelTeachersLearning"/></center></th>
      				<th><center><fmt:message key="creditsLabelDevelopmentAndMaintenance"/></center></th>
      				<th><center><fmt:message key="creditsLabelGraphicDesign"/></center></th>
      			</tr>
      			<tr>
      				<td>
						<center>
							Nuria Joglar Prieto<br/>
							Diego Mart&iacute;n Mart&iacute;n<br/>
							Jos&eacute; I. Hidalgo P&eacute;rez<br/>
							Rub&eacute;n S&aacute;nchez Pelegr&iacute;n (Becarios)<br/>
						</center>
      				</td>
      				<td>
						<center>
							Nuria Joglar Prieto<br/>
							Diego Mart&iacute;n Mart&iacute;n<br/>
							Rub&eacute;n S&aacute;nchez Pelegr&iacute;n<br/>
							Jos&eacute; I. Hidalgo P&eacute;rez<br/>
						</center>
      				</td>
      				<td>
						<center>
							Rub&eacute;n S&aacute;nchez Pelegr&iacute;n (Jefe de programaci&oacute;n)<br/>
							Jos&eacute; M. Colmenar Verdugo (Base de datos)<br/>
							Iv&aacute;n Mart&iacute;nez Ort&iacute;z (JAVA)<br/>
						</center>
      				</td>
      				<td>
						<center>
							Diego Mart&iacute;n Mart&iacute;n<br/>
							Alfredo Cuesta Infante<br/>
							Rub&eacute;n S&aacute;nchez Pelegr&iacute;n<br/>
						</center>
      				</td>
      			</tr>
      	</table>
      	<center>
      		<h3><fmt:message key="creditsLabelHelp"/>:</h3>
				Julio A. Cruz Moya (Programaci&oacute;n)<br/>
				Gonz&aacute;lo Alonso Gonz&aacute;lez (Programaci&oacute;n)<br/>
      	</center>
      </div>
      
      <div id="2007-2008" style="display:none">
      	<br/>
	      <br/>
	      <br/>
      	<table width="100%" class="tabladatos" border="1">
      			<tr>
      				<th><center><fmt:message key="creditsLabelManagement"/></center></th>
      				<th><center><fmt:message key="creditsLabelTeachersLearning"/></center></th>
      				<th><center><fmt:message key="creditsLabelDevelopmentAndMaintenance"/></center></th>
      				<th><center><fmt:message key="creditsLabelGraphicDesign"/></center></th>
      			</tr>
      			<tr>
      				<td>
						<center>
							Nuria Joglar Prieto (Becarios)<br/>
							Diego Mart&iacute;n Mart&iacute;n<br/>
							Alfredo Cuesta Infante<br/>
							Jos&eacute; I. Hidalgo P&eacute;rez<br/>
							Rub&eacute;n S&aacute;nchez Pelegr&iacute;n (Becarios)<br/>
						</center>
      				</td>
      				<td>
						<center>
							Nuria Joglar Prieto<br/>
							Diego Mart&iacute;n Mart&iacute;n<br/>
							Rub&eacute;n S&aacute;nchez Pelegr&iacute;n<br/>
						</center>
      				</td>
      				<td>
						<center>
							Jos&eacute; M. Colmenar Verdugo (Jefe de programaci&oacute;n)<br/>
							Jos&eacute; Luis Risco Mart&iacute;n (Base de datos)<br/>
							Iv&aacute;n Mart&iacute;nez Ort&iacute;z (JAVA)<br/>
							Alberto D&iacute;az Esteban (JAVA)<br/>
						</center>
      				</td>
      				<td>
						<center>
							Diego Mart&iacute;n Mart&iacute;n<br/>
							Alfredo Cuesta Infante<br/>
							Rub&eacute;n S&aacute;nchez Pelegr&iacute;n<br/>
						</center>
      				</td>
      			</tr>
      	</table>
      	<center>
      		<h3><fmt:message key="creditsLabelHelp"/>:</h3>
				Gonzalo J. Canelada Purcell (Programaci&oacute;n)<br/>
				Julio A. Cruz Moya (Flash y web)<br/>
				Gonz&aacute;lo Alonso Gonz&aacute;lez (Flash app) <br/>
				Luis G&oacute;mez Lancha (Flash app)<br/>
      	</center>
      </div>
      
      <div id="2006-2007" style="display:none">
      	<br/>
	      <br/>
	      <br/>
      	<table width="100%" class="tabladatos" border="1">
      			<tr>
      				<th><center><fmt:message key="creditsLabelManagement"/></center></th>
      				<th><center><fmt:message key="creditsLabelTeachersLearning"/></center></th>
      				<th><center><fmt:message key="creditsLabelDevelopmentAndMaintenance"/></center></th>
      				<th><center><fmt:message key="creditsLabelGraphicDesign"/></center></th>
      			</tr>
      			<tr>
      				<td>
						<center>
							Nuria Joglar Prieto (Becarios)<br/>
							Diego Mart&iacute;n Mart&iacute;n<br/>
							Alfredo Cuesta Infante<br/>
						</center>
      				</td>
      				<td>
						<center>
							Nuria Joglar Prieto<br/>
							Diego Mart&iacute;n Mart&iacute;n<br/>
							Rub&eacute;n S&aacute;nchez Pelegr&iacute;n<br/>
						</center>
      				</td>
      				<td>
						<center>
							Jos&eacute; M. Colmenar Verdugo (Jefe de programaci&oacute;n)<br/>
							Jos&eacute; Luis Risco Mart&iacute;n (Base de datos)<br/>
							Iv&aacute;n Mart&iacute;nez Ort&iacute;z (JAVA)<br/>
							Alberto D&iacute;az Esteban (JAVA)<br/>
						</center>
      				</td>
      				<td>
						<center>
							Diego Mart&iacute;n Mart&iacute;n<br/>
							Alfredo Cuesta Infante<br/>
							Rub&eacute;n S&aacute;nchez Pelegr&iacute;n<br/>
						</center>
      				</td>
      			</tr>
      	</table>
      	<center>
      		<h3><fmt:message key="creditsLabelHelp"/>:</h3>
				Francisco Alonso Caballero (Flash)<br/>
				Alfonso Mart&iacute;nez D&iacute;az (Flash)<br/>
				Jonathan Navidad Rodrigo (Flash) <br/>
      	</center>
      </div>
    </div>
    <div id="barra_creditos"> <a style="color:white;" href="${pageContext.request.contextPath}/index.jsp"><fmt:message key="back2index"/></a> </div>  
      
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
    
    <script type="text/javascript">
    showDiv('2010-2011');
    </script>
    
  </body>
</html>