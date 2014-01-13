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
	breadCrumb.addStep(group.getCourse().getName()+" ("+group.getName()+")",request.getContextPath()+"/tutor/managegroup.itest?method=indexGroup");
	breadCrumb.addBundleStep("titleIndexStats","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="tutor"/>
	<jsp:param name="menu" value="tutor"/>
</jsp:include>

		<div id="contenido">
			<p><fmt:message key="tutorTextSelectStat" /></p><hr><br>
			<div>
				<ul class="listaopciones">				
					<li><a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showStatsLearners"><fmt:message key="statsLearners" /></a></li>
					<li><a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showStatsExams"><fmt:message key="statsExams" /></a></li>
					<li><a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showStatsQuestions"><fmt:message key="statsQuestions" /></a></li>
				</ul>
		</div>
	</body>
</html>