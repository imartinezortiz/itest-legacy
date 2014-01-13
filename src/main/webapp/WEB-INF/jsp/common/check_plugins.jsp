<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>

<% 
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addBundleStep("plugins","");	
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="learner"/>
	<jsp:param name="mathml" value="mathml"/>
</jsp:include>
		
<div id="menu"> 
  <ul>
  	<c:choose>
   	<c:when test="${user.role eq 'LEARNER'}">
   		<li> <a href="../learner/index.itest"><fmt:message key="textMain" /></a> </li>
   	</c:when>
   	   	<c:when test="${user.role eq 'KID'}">
   		<li> <a href="../learner/index.itest"><fmt:message key="textMain" /></a> </li>
   	</c:when>
   	<c:when test="${user.role eq 'TUTOR'}">
   		<li> <a href="../tutor/index.itest"><fmt:message key="textMain" /></a> </li>
   	</c:when>
   	<c:when test="${user.role eq 'TUTORAV'}">
   		<li> <a href="../tutor/index.itest"><fmt:message key="textMain" /></a> </li>
   	</c:when>
    </c:choose>
    <li> <a href="${pageContext.request.contextPath}/Logout"><fmt:message key="exitButton" /></a> </li>
  </ul>
</div>

<div id="contenido">

    <script type="text/javascript" src="${pageContext.request.contextPath}/common/resources/ASCII_MathML.js"></script>
	 

	<div class="comprobarPluginIzquierda">
		<p class="cabeceraPlugins"><fmt:message key="pluginFlash" /></p>
		<p class="textoPlugins"><fmt:message key="checkFlash" /></p>
		<object width="400" height="250" classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/
  shockwave/cabs/flash/swflash.cab#version=8,0,0,0">
			<param name="movie" value="${pageContext.request.contextPath}/common/resources/test/testflash.swf" />
			<embed src="${pageContext.request.contextPath}/common/resources/test/testflash.swf" width="400" height="250" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
		</object>
	</div>
 	
	<div class="comprobarPluginDerecha">
 	<p class="cabeceraPlugins"><fmt:message key="pluginMathML" /></p>

     <p class="textoPlugins"><fmt:message key="checkMathML" /></p>
     
    <table align="center" border="0" bgcolor="beige">       
       <tr>
         <td align="center"><b class="textoPlugins"><fmt:message key="formulaMathML" /></b></td>
	 <td>&nbsp;</td>

         <td align="center"><b class="textoPlugins"><fmt:message key="jpgImage" /></b></td>
       </tr>
       <tr valign="middle">
         <td align="center">
               `int_-1^1 sqrt(1-x^2) = pi/2`
	 </td>
	 <td width="20">&nbsp;</td>
	 <td align="center">
	    <img src="${pageContext.request.contextPath}/common/resources/test/ecuacion.jpg">

	 <td>
       </tr>
    </table>   
    <p class="textoPlugins"><fmt:message key="warningIE"/></p>
<p class="textoPlugins"><fmt:message key="warningFirefox" /></p>

</div>
	
	<div class="comprobarPluginIzquierda"> 
 		<p class="cabeceraPlugins"><fmt:message key="pluginWAV" /></p>
 		<object>
			<embed src="${pageContext.request.contextPath}/common/resources/test/testwav.wav" autostart="false" loop="false" width="300" height="50">
  			</embed>
		</object>
		<p class="textoPlugins"><fmt:message key="checkPluginWAV" /></p>
	</div>
	
 	<div class="comprobarPluginDerecha">
		<p class="cabeceraPlugins"><fmt:message key="pluginMP3" /></p>
 		<object>
			<embed src="${pageContext.request.contextPath}/common/resources/test/testmp3.mp3" autostart="false" loop="false" width="300" height="50">
  			</embed>
		</object>
		<p class="textoPlugins"><fmt:message key="checkPluginMP3" /></p>
	</div>

	<div class="comprobarPluginIzquierda"> 
 		<p class="cabeceraPlugins"><fmt:message key="pluginJAVA" /></p>
 		<applet code="SnakePit.class" codebase="${pageContext.request.contextPath}/common/resources/test" width="300" height="300">
 			<p class="textoPluginsAviso"><fmt:message key="noJAVA" /></p>
 		</applet>
 		<p class="textoPlugins"><fmt:message key="checkPluginJAVA" /></p>
 		<p class="textoPlugins"><fmt:message key="checkPluginJAVA2" /></p>
	</div>

 	<div class="comprobarPluginDerecha">
		<p class="cabeceraPlugins"><fmt:message key="pluginMIDI" /></p>
 		<object>
			<embed src="${pageContext.request.contextPath}/common/resources/test/testmidi.mid" autostart="false" loop="false" width="300" height="50">
  			</embed>
		</object>
		<p class="textoPlugins"><fmt:message key="checkPluginMIDI" /></p>
	</div>
	
 	<div class="comprobarPluginDerecha">
		<p class="cabeceraPlugins"><fmt:message key="pluginSibelius" /></p>
		<object id="ScorchPlugin"
				classid="clsid:A8F2B9BD-A6A0-486A-9744-18920D898429"
				width="300"
				height="200"
				codebase="http://www.sibelius.com/download/software/win/ActiveXPlugin.cab#4,0,0,0">
		<param name="src" value="${pageContext.request.contextPath}/common/resources/test/">
		<param name="type" value="application/x-sibelius-score">
		<param name="scorch_minimum_version" value="4000">
		<param name="scorch_preferred_version" value="4000">
		<embed src="${pageContext.request.contextPath}/common/resources/test/testsibelius.sib"
				 scorch_minimum_version="4000"
				 scorch_preferred_version="4000"
				 width="300"
				 height="200"
				 type="application/x-sibelius-score"
				 pluginspage="http://www.sibelius.com/cgi/plugin.pl">
		</embed>
		</object>
		<p class="textoPlugins"><fmt:message key="checkPluginSibelius" /></p>
	</div>

	<div style="clear:both" class="botoniniciarexamen">
		<c:choose>
		<c:when test="${not empty idexam}">
			<form action="newexam.itest?method=goNewExam" method="post">
			<p><input style="height:50; width:50%" type="submit" value="<fmt:message key="pluginsOK_startExam" />"></p>
			<input type="hidden" name="idexam" value="${idexam}">
			</form>
		</c:when>
		<c:otherwise>
			<c:choose>
			<c:when test="${user.role eq 'LEARNER'}">
				<form action="../learner/index.itest" method="get">
			</c:when>
			<c:when test="${user.role eq 'TUTOR'}">
				<form action="../tutor/index.itest" method="get">
			</c:when>
			<c:when test="${user.role eq 'TUTORAV'}">
				<form action="../tutor/index.itest" method="get">
			</c:when>
			</c:choose>
			<p><input style="height:50; width:50%" type="submit" value="<fmt:message key="backToMain" />"></p>
			</form>
		</c:otherwise>
		</c:choose>
	</div>

</div>

</body>

</html>
