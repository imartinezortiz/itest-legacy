<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>
<%@ page import="com.cesfelipesegundo.itis.model.Group" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:choose>
	<c:when test="${param.mathml eq 'mathml'}">
<html xmlns:mml="http://www.w3.org/1998/Math/MathML">
	</c:when>
	<c:otherwise>
<html>
	</c:otherwise>
</c:choose>

<head>
	<title>iTest</title>
	
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/general.css" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/BBCodecolorPicker.css" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/BBCodestyle.css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/common/resources/BBCodecolorPicker.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/common/resources/BBCodefunctions.js"></script>	
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.js"/>
	<script language=JavaScript></script>	
	<c:if test="${param.userRole eq 'learner'}">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/alumno.css" />
	</c:if>
	<c:if test="${param.userRole eq 'tutor' or param.userRole eq 'TUTORAV' or param.userRole eq 'admin'}">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/profesor.css" />		
	</c:if>
	<c:if test="${param.userRole eq 'kid'}">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/kid.css" />
	</c:if>
	<!--[if gte IE 5.5]>
	<![if lt IE 7]>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/arregloposie.css"/>
	<![endif]>
	<![endif]-->
	<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF8">		

		
    <c:if test="${not empty param.examduration}">
      <!-- Doing the exam.. -->
      <c:choose>
      	<c:when test="${param.userRole eq 'kid'}">
      		<script language="javascript" src="${pageContext.request.contextPath}/common/resources/relojKid.js" type="text/javascript"></script>
      	</c:when>
      	<c:otherwise>
      		<script language="javascript" src="${pageContext.request.contextPath}/common/resources/relojExamen.js" type="text/javascript"></script>
      	</c:otherwise>
      </c:choose>  
 	</c:if>
    
    <script language="javascript" type="text/javascript">
       // All the windows start with "itest"
       if (window.name.indexOf("itest") != 0) {
		    window.open(window.location, "itest", "width="+screen.availWidth+",height="+screen.availHeight+",top=0,left=0");
    	    window.close();
       }
    </script>
    
	
</head>

<c:choose>
	<c:when test="${not empty param.examduration}">
	<c:if test="${param.userRole eq 'learner'}">
		<body onLoad="startExam()">
	</c:if>
	<c:if test="${param.userRole eq 'kid'}">
		<body onLoad="startExamKid()">
	</c:if>
		 
	</c:when>
	<c:otherwise>
		<body>
	</c:otherwise>
</c:choose>
		
<div id="cabecera">
   <c:choose>
   	<c:when test="${user.role eq 'LEARNER' or user.role eq 'KID'}">
   		<a href="${pageContext.request.contextPath}/learner/index.itest" title="<fmt:message key="textMain" />"><b>
   		<fmt:message key="learnerMainPageTitle" /> 
   		<script type="text/javascript">
   		//El siguiente comando desactiva el botón derecho.
		document.oncontextmenu = function(){return false}
   		</script>
   	</c:when>
   	<c:when test="${user.role eq 'TUTOR' or user.role eq 'TUTORAV'}">
   		<a href="${pageContext.request.contextPath}/tutor/index.itest" title="<fmt:message key="textMain" />"><b>
   		<fmt:message key="tutorMainPageTitle" /> 
   	</c:when>
   	<c:when test="${user.role eq 'ADMIN'}">
   		<a href="${pageContext.request.contextPath}/admin/index.itest" title="<fmt:message key="textMain" />"><b>
   		<fmt:message key="adminMainPageTitle" /> 
   	</c:when>
   	<c:otherwise>
   		<c:out value="${user.role}"/>: 
   	</c:otherwise>
   </c:choose>
   <c:out value="${user.name}"/> <c:out value="${user.surname}"/></b></a>
   <br />
   <c:if test="${user.role eq 'TUTOR' or user.role eq 'TUTORAV'}">
	<%
	    /* Showing year at header for tutor users. It is also a link to change group */
		try {
	    	Group groupHeader = (Group)request.getAttribute("group");
			if (groupHeader != null)
				if (groupHeader.getYear() != null) {
					// Page context needed
					%><a href="${pageContext.request.contextPath}/tutor/index.itest"><%
					out.println(""+groupHeader.getYear()+"</a> -> ");
				}
		} catch (Exception e) {}
	%>
	</c:if>
	
	<%
	/* Then, breadcrumb */
	try {
   		BreadCrumb bc = (BreadCrumb)request.getAttribute("breadCrumb");
   		out.println(bc.getText());
   	}
    catch (Exception e) {
    }
   %>
</div>
		
<div id="logo1">
  	<c:if test="${empty param.examduration}"><a href="${pageContext.request.contextPath}"> </c:if>
  		<img src="${pageContext.request.contextPath}/imagenes/logoitest.gif" alt="Logo iTest" title="iTest" />
 	<c:if test="${empty param.examduration}"></a> </c:if>
</div>

<div id="logo2">
	<c:if test="${empty param.examduration}"><a href="http://www.gredossandiego.com/"> </c:if>
		<img src="${pageContext.request.contextPath}/imagenes/logo_gredos.jpg" alt="Logo GSD" title="Gredos San Diego" />
	<c:if test="${empty param.examduration}"></a> </c:if>
</div>

<%-- Menú estático de profesor --%>
<c:if test="${param.menu eq 'tutor'}">

<div id="menu">
			<ul>
				<li>
					<a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=indexGroup"><fmt:message key="menuTasks" /></a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showQuestionsList"><fmt:message key="menuQuestions" /></a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showExamsList"><fmt:message key="menuExams" /></a>
				</li>	
				<li><a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showGradesList"><fmt:message key="menuGradesList" /></a></li>
				<li>
					<a href="${pageContext.request.contextPath}/tutor/index.itest"><fmt:message key="menuChooseGroup" /></a>
				</li>
				<li> <a href="${pageContext.request.contextPath}/Logout"><fmt:message key="exitButton" /></a> </li>
			</ul>
		</div>
</c:if>

<c:if test="${param.menuNewQuestion eq 'tutor'}">

<div id="menu">
			<ul>
				<li>
					<a onclick="return alert2Exit('salir');" href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=indexGroup"><fmt:message key="menuTasks" /></a>
				</li>
				<li>
					<a onclick="return alert2Exit('salir');" href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showQuestionsList"><fmt:message key="menuQuestions" /></a>
				</li>
				<li>
					<a onclick="return alert2Exit('salir');" href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showExamsList"><fmt:message key="menuExams" /></a>
				</li>	
				<li><a onclick="return alert2Exit('salir');" href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showGradesList"><fmt:message key="menuGradesList" /></a></li>	
				<li>
					<a onclick="return alert2Exit('salir');" href="${pageContext.request.contextPath}/tutor/index.itest"><fmt:message key="menuChooseGroup" /></a>
				</li>
				<li> <a onclick="return alert2Exit('salir');" href="${pageContext.request.contextPath}/Logout"><fmt:message key="exitButton" /></a> </li>
			</ul>
		</div>
</c:if>

<%-- Menu estatico de administrador --%>
<c:if test="${param.menu eq 'admin'}">

<div id="menu">
			<ul>
				<li>
					<a href="${pageContext.request.contextPath}/admin/index.itest"><fmt:message key="textMain" /></a>
				</li>	
				<li>
					<a href="${pageContext.request.contextPath}/admin/admin.itest?method=showInstitutionsList"><fmt:message key="menuInstitutions" /></a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/admin/admin.itest?method=showCoursesList"><fmt:message key="menuCourses" /></a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/common/index.itest?method=changePassword"><fmt:message key="changePasswd" /></a>
				</li>
				<li> <a href="${pageContext.request.contextPath}/Logout"><fmt:message key="exitButton" /></a> </li>
			</ul>
		</div>
</c:if>

<%-- Menu estatico de administrador, administrando un centro --%>
<c:if test="${param.menu eq 'institution'}">

<div id="menu">
			<ul>
				<li>
					<a href="${pageContext.request.contextPath}/admin/index.itest"><fmt:message key="textMain" /></a>
				</li>	
				<li>
					<a href="${pageContext.request.contextPath}/admin/institution.itest?method=indexInstitution"><fmt:message key="textMainInstitution" /></a>
				</li>	
				<li>
					<a href="${pageContext.request.contextPath}/admin/institution.itest?method=showUsersList"><fmt:message key="menuUsersInstitution" /></a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/admin/institution.itest?method=showGroupsList"><fmt:message key="menuGroupsInstitution" /></a>
				</li>
				<li> <a href="${pageContext.request.contextPath}/Logout"><fmt:message key="exitButton" /></a> </li>
			</ul>
		</div>
</c:if>

<%-- Common menu. Used, for example, in the change password common functionality --%>
<c:if test="${param.menu eq 'common'}">

	<div id="menu">
			<ul>
				<li>
				<%-- Role constants come from database (they are capitalized) --%>
				<c:choose>
				   	<c:when test="${param.userRole eq 'LEARNER'}">
				   		<a href="${pageContext.request.contextPath}/learner/index.itest" title="<fmt:message key="textMain" />">
				   	</c:when>
				   	<c:when test="${param.userRole eq 'TUTOR' or param.userRole eq 'TUTORAV'}">
				   		<a href="${pageContext.request.contextPath}/tutor/index.itest" title="<fmt:message key="textMain" />">
				   	</c:when>
				   	<c:when test="${param.userRole eq 'ADMIN'}">
				   		<a href="${pageContext.request.contextPath}/admin/index.itest" title="<fmt:message key="textMain" />">
				   	</c:when>
				   </c:choose>
				   <b><fmt:message key="textMain" /></b></a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/Logout"><fmt:message key="exitButton" /></a>
				</li>
			</ul>
		</div>
</c:if>


<%-- This is the layer that avoids double-click errors --%>
<div id="divDclick"><div id="divDclickMsg"><br/><br/><p><fmt:message key="waitMsg"/></p></div></div>

<%-- This is the layer for hiding the exam until it is fully loaded --%>
<c:if test="${not empty param.examduration}">
	<div id="divHideExam" style="display: block"><p><fmt:message key="examLoadingMsg"/></p></div>
</c:if>

<%-- These are the javascript functions related to this layer --%>
<script type='text/javascript'>

		var bloqueado = false;
		/* Protects the page from the double-click error */
		function iTestLockPage(desactivar) {
			document.getElementById("divDclick").style.display = "block";
			if(desactivar == null){
				bloqueado = true;
	 		    var temporizador = setTimeout("abrirVent()", 7500);
			}
		} // iTestProtectPage
		
		/* Frees the page from the double-click error */
		function iTestUnlockPage(desactivar) {
			document.getElementById("divDclick").style.display = "none";
			deshabilitarVent();
			if(desactivar == 'error'){
				alert("<fmt:message key="transactionError"/>");
			}
		} // iTestUnlockPage


		function deshabilitarVent(){
			bloqueado = false;
		}

		var callBackTimeOut= 8500;
		
		function abrirVent(){
			if(bloqueado){
				alert("<fmt:message key="transactionError"/>");
				iTestUnlockPage();
			}
			
		}

		function startExamKid(){
			intervalo=setInterval("refrescarReloj()",1000);
			document.getElementById("divHideExam").style.display = "none"; 
		}
		
</script>
