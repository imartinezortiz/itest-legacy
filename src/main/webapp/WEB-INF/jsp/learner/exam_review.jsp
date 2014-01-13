<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fmt_rt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>
<%@ page import="com.cesfelipesegundo.itis.model.BasicDataExam" %>

<% 
	BasicDataExam exam = (BasicDataExam)request.getAttribute("exam");
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addBundleStep("examReview","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="learner"/>
	<jsp:param name="mathml" value="mathml"/>
</jsp:include>
		
<div id="menu">
  <ul>
    <li> <a style="border: ridge; background-color:#CCCCCC; color:#000000;" href="${pageContext.request.contextPath}/learner/index.itest"><fmt:message key="textMain" /></a> </li>
    <li><fmt:message key="questions" />
        <select id="goQuestion" onchange="javascript:location.href=this.value;">
		    <c:forEach items="${exam.questions}" varStatus="numQuestion">
		    	<option value="#pregunta<c:out value="${numQuestion.count}"/>"><c:out value="${numQuestion.count}"/></option>
		    </c:forEach>
	    </select>
	</li>
<!--<li> <a style="border: ridge; background-color:#CCCCCC; color:#000000;" href="${pageContext.request.contextPath}/learner/newexam.itest?method=reviewExam2PDF"><fmt:message key="generatePDF" /></a></li> -->
    <li> <a style="border: ridge; background-color:#CCCCCC; color:#000000;" href="${pageContext.request.contextPath}/Logout"><fmt:message key="exitButton" /></a> </li>
  </ul>
</div>

<div id="contenido">

	<script type="text/javascript" src="${pageContext.request.contextPath}/common/resources/ASCII_MathML.js"></script>
	
	<center>
	
	 <div class="cartelRevision">
		<p class="tituloPregunta">Leyenda</p></a>
			<div class="preguntaExamen">
				<p class="enunciadoPregunta">1.- Las respuestas contestadas por el alumno y las respuestas correctas se identificar según:</p>
				<div class="respuestasTipoTest">
					<p><INPUT TYPE="checkbox" disabled="disabled" checked="checked"> Respuesta contestada </p>
					<p class="respuestaCorrecta"><INPUT TYPE="checkbox" disabled="disabled"> Respuesta correcta</p>
				</div>
				<p class="enunciadoPregunta">2.- La puntuación obtenida en cada pregunta se muestra al lado del título de la misma</p>
				<p class="enunciadoPregunta">3.- Existen comentarios del profesor al final de algunas preguntas</p>
			</div>
	</div>
	 
    <p class="tituloExamen"> <u><fmt:message key="course"/> <c:out value="${exam.group.course.name}"/> (<c:out value="${exam.group.name}"/>)</u> <br/><br/>
    <c:out value="${exam.title}"/></p>
    
    <c:forEach items="${exam.questions}" var="question" varStatus="numQuestion">
    	<c:set var="question" value="${question}" scope="request"/>
		<a name="pregunta${numQuestion.count}"></a>
		<jsp:include page="/WEB-INF/jsp/common/question.jsp" flush="true">
			<jsp:param name="view" value="review"/>
			<jsp:param name="numQuestion" value="${numQuestion.count}"/>
			<jsp:param name="ConfidenceLevel" value="${exam.confidenceLevel eq true}"/>
		</jsp:include>
	</c:forEach>
	
	<div id="calif" class="respuestasTipoTest">
	   <p align="center"><b><fmt:message key="yourcalif" />&nbsp;<fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${exam.examGrade}"/>
	   <br/><fmt:message key="headerGlistMaxGrade" />:&nbsp;<fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${exam.maxGrade}"/></legend></p>
	   <hr/>
	</div>
	<!-- <fieldset style ="border:0;">
			<legend>Nota media ${media}%</legend>
			<table width="858" height="38" border="1">
			<tr>
				<td height="32">
					<table width="${media}%" height="100%" style="background-color:blue;">
						<tr>
							<td height="32"></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		</fieldset> -->
	
	</center>	
</div>



</body>

</html>
