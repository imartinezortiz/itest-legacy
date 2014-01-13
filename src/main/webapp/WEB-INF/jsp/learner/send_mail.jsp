<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="com.cesfelipesegundo.itis.model.Group"%>


<% 
Group group = (Group)request.getAttribute("group");
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addBundleStep("textMain","");
	breadCrumb.addStep(group.getCourse().getName()+" ("+group.getName()+")","");
	request.setAttribute("breadCrumb",breadCrumb);
%>
<!-- Estas css son solo para poder mostrar la lista de grupos asique sólo las pongo en este jsp-->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/profesor.css" />
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="learner"/>
</jsp:include>

<script type='text/javascript'>
	function getEmail(){
		document.getElementById('toSend').value = document.getElementById('to').value;
		document.getElementById('message').value = document.getElementById('text').value;
		if(document.getElementById('message').value == ''){
			alert("<fmt:message key="alertEmptyMessage"/>");
			return false;
		}
		if(document.getElementById('subject').value == ''){
			alert("<fmt:message key="alertEmptySubject"/>");
			return false;
		}
		if(document.getElementById('toSend').value == ''){
			alert("<fmt:message key="alertTeacherNoEmail"/>");
			return false;
		}
		if(document.getElementById('from').value == ''){
			alert("<fmt:message key="alertStudentNoEmail"/>");
			return false;
		}
	}
</script>
		
<div id="menu"> 
  <ul>
	<li> <a href="${pageContext.request.contextPath}/common/index.itest?method=changePassword"><fmt:message key="changePasswd"/></a> </li>
  	<li> <a href="${pageContext.request.contextPath}/common/index.itest?method=checkPlugins"><fmt:message key="checkPlugins"/></a> </li>
  	<li> <a href="${pageContext.request.contextPath}/learner/index.itest?method=handleRequest"><fmt:message key="backToMain"/></a> </li>
    <li> <a href="${pageContext.request.contextPath}/Logout"><fmt:message key="exitButton" /></a> </li>
  </ul>
</div>

<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/ExamMgmt.js'></script>
<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>

<div id="contenido">
	<form name="sendMail" id="sendMail" action="${pageContext.request.contextPath}/learner/newexam.itest?method=sendMail" method="post" onSubmit="return getEmail();">
		<div style="padding:10px 5px 15px 20px;">
			<center><fmt:message key="textYourEmail"/>: <b id="from" name="from"><c:out value="${user.email}"/></b></center>
		</div>
		<div style="padding:10px 5px 15px 20px;">
			<fmt:message key="textSendMailTo"/>	
			<select id="to" name="to">
				<c:forEach items="${tutors}" var="tutor">
					<c:if test="${!empty tutor.email}">
						<option value="${tutor.email}"><c:out value="${tutor.surname}"/>,<c:out value="${tutor.name}"/></option>
					</c:if>
				</c:forEach> 
			</select>
		</div>
		<input type="hidden" id="toSend" name="toSend" value=""/>
		<input type="hidden" id="message" name="message" value=""/>
		
		<div style="padding:15px 5px 15px 20px;">
			<center><label style="width:60%;"><fmt:message key="labelSubjectMail"/></label></br>
			<input type="text" id="subject" name="message" style="width:60%;" value=""/></center>
			</br>
			<center><label style="width:60%;"><fmt:message key="labelMessage"/></label></br>
			<textArea id="text" rows="20" style="width:60%;" wrap='off'></textArea></center>
		</div>
		<input type="submit" onclick=""value="<fmt:message key="buttonSendMail"/>"/>
	</form>
	
	
</div>