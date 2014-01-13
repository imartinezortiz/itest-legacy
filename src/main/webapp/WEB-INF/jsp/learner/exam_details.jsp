<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>
<%@ page import="com.cesfelipesegundo.itis.model.Exam" %>
<%@ page import="com.cesfelipesegundo.itis.model.BasicDataExam" %>
<%@ page import="java.util.ResourceBundle" %>


<% 
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addBundleStep("textMain","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="learner"/>
</jsp:include>
		
<script>
	function showDetails(){
		if(document.getElementById("moreDetailsButton").value == "<fmt:message key="showDetails"/>"){
			document.getElementById("moreDetails").style.display="block";
			document.getElementById("moreDetailsButton").value ="<fmt:message key="hideDetails"/>";
		}else{
			document.getElementById("moreDetails").style.display="none";
			document.getElementById("moreDetailsButton").value ="<fmt:message key="showDetails"/>";
			}
	}
</script>
<div id="menu"> 
  <ul>
	<li> <a href="${pageContext.request.contextPath}/common/index.itest?method=changePassword"><fmt:message key="changePasswd" /></a> </li>
  	<li> <a href="${pageContext.request.contextPath}/common/index.itest?method=checkPlugins"><fmt:message key="checkPlugins" /></a> </li>
    <li> <a href="${pageContext.request.contextPath}/Logout"><fmt:message key="exitButton" /></a> </li>
  </ul>
</div>

<div id="contenido">
	<div>
		<fieldset>
			<legend><fmt:message key="examDetails"/></legend>
			<div>
				<p>
					<fmt:message key="numberOfQuestion"/> <c:out value="${exam.questionsNumber}"/>
				</p>
				<p>
					<fmt:message key="examTime"/> <c:out value="${exam.duration}"/> <fmt:message key="minutes"/>       
				</p>
				<p>
					<input type="button" id="moreDetailsButton" value="<fmt:message key="showDetails"/>" onclick="javascript:showDetails();"/>
				</p>
				<div id="moreDetails" style="display:none">
					<center>
						<fieldset style="width:60%;">
							<legend><fmt:message key="otherDetails"/></legend>
							<c:choose>
								<c:when test="${!exam.partialCorrection}">
									<p>
										<fmt:message key="PenaltiesQuestionFailed"/> <c:out value="${exam.penQuestionFailed}"/>
									</p>
									<p>
										<fmt:message key="PenaltiesQuestionNotAnswered"/> <c:out value="${exam.penQuestionNotAnswered}"/>
									</p>
									<p>
										<fmt:message key="PenaltiesAnswerFailed"/> <c:out value="${exam.penAnswerFailed}"/>
									</p>
								</c:when>
								<c:otherwise>
									<p>
										<fmt:message key="partialGrades"/>
									</p>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${exam.showNumCorrectAnswers}"><fmt:message key="showCorrectAnswers"/></c:when>
							</c:choose>
							<p>
								<fmt:message key="maximumGrade"/> <c:out value="${exam.maxGrade}"/>
							</p>
						</fieldset>
					</center>
				</div>
			</div>
		</fieldset>
	</div>
	<div>
	<fieldset>
			<legend><fmt:message key="startExam"/></legend>
			<form id="formrealizarexamen" class="formrealizarexamen" action="${pageContext.request.contextPath}/learner/newexam.itest?method=goNewExam" method="post">
				<input type="hidden" id="idexam" name="idexam" value="${idexam}"/>
				<input type="submit" value="<fmt:message key="startExam"/>"/>
			</form>
	</fieldset>
	</div>
</div>

</body>
</html>