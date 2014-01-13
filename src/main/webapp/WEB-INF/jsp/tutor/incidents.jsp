<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fmt_rt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>
<%@ page import="com.cesfelipesegundo.itis.model.Group" %>

<% 
	Group group = (Group)request.getAttribute("group");
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addStep(group.getCourse().getName()+" ("+group.getName()+")",request.getContextPath()+"/tutor/managegroup.itest?method=indexGroup");
	breadCrumb.addBundleStep("taskShowIncidents","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="tutor"/>
	<jsp:param name="menu" value="tutor"/>
	<jsp:param name="mathml" value="mathml"/>
</jsp:include>

		<div id="contenido">
			<div align="left">
				<dl>
					<dt><h1><fmt_rt:message key="showListIncidence1"/></h1></dt>
						<div>
							 <fmt_rt:message key="showTextIncidence1"/>
						</div>
					<dt><h1><fmt_rt:message key="showListIncidence2"/></h1></dt>
						<div>
							 <fmt_rt:message key="showTextIncidence2"/>
						</div>
				</dl>
			</div>
		</div>
	</body>
</html>