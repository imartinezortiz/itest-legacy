<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fmt_rt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>

<% 
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addBundleStep("grade","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="kid"/>
</jsp:include>

	<div id="menu"> 
	  <ul>
	  	<li>
		</li>
	  </ul>
	</div>
	
	<div id="contenido">
		
		<%-- The showed image depends on whether the question grading is correct  --%>
		<p>
		<A HREF="${pageContext.request.contextPath}/learner/newexam.itest?method=goNextQuestion&nextQuestionIndex=${nextQuestionIndex}">
			<c:choose>
			  <c:when test="${grade}">
				 <img src="${pageContext.request.contextPath}/imagenes/Superheroe2Contento2.jpg" title="i-Test" style="border:none;"/>
			  </c:when>
			  <c:otherwise>
			     <img src="${pageContext.request.contextPath}/imagenes/Superheroe2Triste2.jpg" title="i-Test" style="border:none;"/>
			  </c:otherwise>
			</c:choose>
			<br/>
			<img src="${pageContext.request.contextPath}/imagenes/boton_siguiente.png" alt="<fmt:message key="nextQuestion" />"/>		
		</A>		
		<c:choose>
		<c:when test="${grade}">
			<c:choose>
				<c:when test="${empty nextQuestionIndex}">
					<!-- Ultima pregunta -->
					<embed src="${pageContext.request.contextPath}/common/mmedia/Bien-Terminar.mp3" autostart="true" loop="false" hidden="true">
				</c:when> 
				<c:otherwise>
					<embed src="${pageContext.request.contextPath}/common/mmedia/Bien-Continuar.mp3" autostart="true" loop="false" hidden="true">
				</c:otherwise> 
			</c:choose> 
			
		</c:when>
		<c:otherwise>
		
			 <c:choose>
			<c:when test="${empty nextQuestionIndex}">
					<!-- Ultima pregunta -->
					<embed src="${pageContext.request.contextPath}/common/mmedia/Mal-Terminar.mp3" autostart="true" loop="false" hidden="true">
				</c:when> 
				<c:otherwise>
					<embed src="${pageContext.request.contextPath}/common/mmedia/Mal-Continuar.mp3" autostart="true" loop="false" hidden="true">
				</c:otherwise> 
			</c:choose> 
			
		</c:otherwise>
		</c:choose>
		</p>
		<p>
	</div>

</body>

</html>
