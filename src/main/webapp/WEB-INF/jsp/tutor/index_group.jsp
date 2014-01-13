<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>
<%@ page import="com.cesfelipesegundo.itis.model.Group" %>

<% 
	Group group = (Group)request.getAttribute("group");
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addStep(group.getCourse().getName()+" ("+group.getName()+")","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="tutor"/>
	<jsp:param name="menu" value="tutor"/>
	<jsp:param name="mathml" value="mathml"/>
</jsp:include>
<script type="text/javascript">
	function submitForm(form){
		form.submit();
	}
</script>
		<div id="contenido">
			<br>
			
			<div class="divDosIzq">
  				<p><b><fmt:message key="taskTitleQuestions" /></b></p>
  				<hr>
  				<form id="formAddNewQuestion" action="${pageContext.request.contextPath}/tutor/managegroup.itest?method=newQuestion" method="POST" style="display:none">
  					<input type="hidden" name="idgroup" value="${group.id}">
  				</form>
				<ul class="listaopcionesgroup">							
					<li><a href="javascript:submitForm(document.getElementById('formAddNewQuestion'));"><fmt:message key="taskAddQuestion" /></a> </li>
					<li><a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showQuestionsList"><fmt:message key="taskShowQuestionsList" /></a></li>
					<li><a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showSyllabus"><fmt:message key="taskShowSyllabus" /></a></li>
					<li><a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showImportQuestions"><fmt:message key="taskImportQuestions" /></a></li>	
				</ul>
				<br>
				
			</div>
			
			<div class="divDosDer">			
				<p><b><fmt:message key="taskTitleStats" /></b></p>
  				<hr>
				<ul class="listaopcionesgroup">
					<li><a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showGradesList"><fmt:message key="taskShowGradesList" /></a></li>
					<li><a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showStatsLearners"><fmt:message key="statsLearners" /></a></li>
						<li><a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showStatsExams"><fmt:message key="statsExams" /></a></li>
						<li><a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showStatsQuestions"><fmt:message key="statsQuestions" /></a></li>
				</ul>
				<br>
			</div>
			
			<div class="divcentro"></div>
			
			<div class="divDosIzq">
				<p><b><fmt:message key="taskTitleExams" /></b></p>
  				<hr>
				<ul class="listaopcionesgroup">	
					<li><a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=newExam"><fmt:message key="taskAddExam" /></a></li>
					<li><a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showExamsList"><fmt:message key="taskShowExamsList" /></a></li>
					<li><a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showIncidents"><fmt:message key="taskShowIncidents" /></a></li>
				</ul>
			</div>
			<div class="divDosDer">	
				<p><b><fmt:message key="taskTitleOthers" /></b></p>
  				<hr>
				<ul class="listaopcionesgroup">
					<li><a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showStudentsList"><fmt:message key="studentsList" /></a></li>	
					<li><a href="${pageContext.request.contextPath}/tutor/index.itest"><fmt:message key="taskChangeGroup" /></a></li>	
					<li><a href="${pageContext.request.contextPath}/tutor/list_Group.itest"><fmt:message key="taskImportGroup" /></a></li>	
					<li><a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showImportStudents"><fmt:message key="taskImportStudent" /></a></li>	
					<c:if test="${user.role eq 'TUTORAV'}">
						<li><a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=importStudentFromFile"><fmt:message key="taskImportStudentFromFile" /></a></li>
					</c:if>
				</ul>
			</div>
			<div class="divcentro"></div>
			
			<br><hr><br>
		    <p><fmt:message key="tutorTextSelectTask" /></p>
			
		</div>
		<c:if test="${!empty errorKey}">
		<script>
			alert("<fmt:message key="errorImport" />");
		</script>
		</c:if>
		<c:if test="${!empty successKey}">
			<script>
				alert("<fmt:message key="successImport" />");
			</script>
		</c:if>
		
	</body>
</html>