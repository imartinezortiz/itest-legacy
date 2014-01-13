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
	<jsp:param name="userRole" value="learner"/>
</jsp:include>
	<script type='text/javascript'>
		
		function showCalification(){
			if (document.getElementById("divCalification").style.display=="block"){
				document.getElementById("divCalification").style.display="none";
				document.getElementById("showGradeButton").value="<fmt:message key="buttonShowCalification"/>";
				
			}
			else{
				document.getElementById("divCalification").style.display="block";
				document.getElementById("showGradeButton").value="<fmt:message key="buttonNoShowCalification"/>";
			}
		}
	</script>
<div id="menu"> 
  <ul>
    <li> <a href="${pageContext.request.contextPath}/learner/index.itest"><fmt:message key="textMain" /></a> </li>
    <li> <a href="${pageContext.request.contextPath}/Logout"><fmt:message key="exitButton" /></a> </li>
  </ul>
</div>
<div id="contenido">
	<input id="showGradeButton" name="show" type="button" onclick="javascript:showCalification();" value="<fmt:message key="buttonShowCalification"/>" />
	<br>
	<div id="divCalification" style="display:none;">
		<br><br>
		<p><b><fmt:message key="yourcalif" /></b></p>
		<br>
			<p class="grade"><fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${grade}" /></p>
			<p><b>(<fmt:message key="outOfMaxGrade"/> <fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${maxgrade}" />)</b></p>
			<br><br>
		<form action="${pageContext.request.contextPath}/learner/index.itest" method="post">
			<p><input type="submit" value="<fmt:message key="textMain" />"></p>
		</form>	
	</div>	
		
	<p><fmt:message key="startDateRevision"/> <fmt_rt:formatDate value="${startDateReview}" type="both" dateStyle="short" timeStyle="short" /></p>
	<p>
		<fmt:message key="endDateRevision"/> <fmt_rt:formatDate value="${endDateReview}" type="both" dateStyle="short" timeStyle="short" />
	</p>
	<p>
		<c:if test="${goToRevision eq true}">
			<form id="revisarexamen" action="${pageContext.request.contextPath}/learner/newexam.itest?method=reviewExam" method="post" onSubmit="return checkIdExam('revisarexamen')">
				<input type="submit" value="<fmt:message key="startRevision"/>" onclick="javascript:getIdexam('${idExam}','review')"/>
				<input type="hidden" id="idexam" name="idexam" value="${idExam}"/>
			</form>
		</c:if>
		
	</p>
</div>
	</body>
</html>
