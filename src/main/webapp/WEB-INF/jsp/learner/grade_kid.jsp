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
	    <li> <a href="${pageContext.request.contextPath}/learner/index.itest"><fmt:message key="textMain" /></a> </li>
	    <li> <a href="${pageContext.request.contextPath}/Logout"><fmt:message key="exitButton" /></a> </li>
	  </ul>
	</div>
	
	<div id="contenido">
		<br><br>
		<p><b><fmt:message key="yourcalif" /></b></p>
		<br>
		<p class="grade"><fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${grade}" /></p>
		<p><b>(<fmt:message key="outOfMaxGrade"/> <c:out value="${maxgrade}"/>)</b></p>
		<br><br>
		
		<%-- The showed image depends on the grade: happy face shown when grade > 40% of maxgrade  --%>
		<A HREF="${pageContext.request.contextPath}/Logout">
			<c:choose>
				  <c:when test="${grade gt (0.4*maxgrade)}">
					 <img src="${pageContext.request.contextPath}/imagenes/Superheroe2Contento2.jpg" title="i-Test" style="border:none;"/>
				  </c:when>
				  <c:otherwise>
				     <img src="${pageContext.request.contextPath}/imagenes/Superheroe2Triste2.jpg" title="i-Test" style="border:none;"/>
				  </c:otherwise>
			</c:choose>
		</A>
			<c:choose>
				  <c:when test="${grade gt (0.4*maxgrade)}">
					 <embed src="${pageContext.request.contextPath}/common/mmedia/LoHasHechoMuyBien.mp3" autostart="true" loop="false" hidden="true">
				  </c:when>
				  <c:otherwise>
				     <embed src="${pageContext.request.contextPath}/common/mmedia/HasFalladoAlgunaPregunta.mp3" autostart="true" loop="false" hidden="true">
				  </c:otherwise>
			</c:choose>			
	</div>

</body>

</html>
