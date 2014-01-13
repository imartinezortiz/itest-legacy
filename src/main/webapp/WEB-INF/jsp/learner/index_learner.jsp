<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>
<%@ page import="java.util.ResourceBundle" %>


<% 
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addBundleStep("textMain","");
	request.setAttribute("breadCrumb",breadCrumb);
%>
<!-- Estas css son solo para poder mostrar la lista de grupos asique sólo las pongo en este jsp-->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/profesor.css" />
<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="learner"/>
</jsp:include>
		
<div id="menu"> 
  <ul>
	<li> <a href="${pageContext.request.contextPath}/common/index.itest?method=changePassword"><fmt:message key="changePasswd" /></a> </li>
  	<li> <a href="${pageContext.request.contextPath}/common/index.itest?method=checkPlugins"><fmt:message key="checkPlugins" /></a> </li>
    <li> <a href="${pageContext.request.contextPath}/Logout"><fmt:message key="exitButton" /></a> </li>
  </ul>
</div>

<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/ExamMgmt.js'></script>
<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>

<script type='text/javascript'>

   // Check the selection of exams:
   function checkIdExam(formid) {
      var f = document.getElementById(formid);
      
      if (f.idexam.value == '') {
         alert("<fmt:message key="messageSelectExam" />");
         return false;
      } else {
         return true;// cambiar a true
      }
   } // checkIdExam

   function selectGroup(id){
		document.getElementById('group').value = id;
		document.getElementById('option').value = "goGroupIndex";
		document.formrevisarexamen.submit();
	}

	function sendMail(groupId){
		document.getElementById('group').value = groupId;
		document.getElementById('option').value = "sendMail";
		document.formrevisarexamen.submit();
	}

// Function to display the symbols and divs:
	function showYear(year) {
		// Show div
		document.getElementById('div'+year).style.display='block';
		// Show "minus"
		document.getElementById('minus'+year).style.display='block';		
		// Hide plus
		document.getElementById('plus'+year).style.display='none';
	} // showYear

	// Function to hide the symbols and divs:
	function hideYear(year) {
		// Hide div
		document.getElementById('div'+year).style.display='none';
		// Hide "minus"
		document.getElementById('minus'+year).style.display='none';		
		// Show plus
		document.getElementById('plus'+year).style.display='block';
	} // showYear	

	function checkNavigator(){
		if (navigator.appName!='Netscape'){
			document.getElementById('textNavigator').style.display="";
		}
	}

</script>

<div id="contenido">
		
	<form name="formrevisarexamen" id="formrevisarexamen" action="${pageContext.request.contextPath}/learner/newexam.itest?method=goGroupExams" method="post">
		<fieldset>
			<legend><b><fmt:message key="textLengendWelcome"/></b></legend>
			<ul style ="font-size:20px; ">
				<li id="textNavigator" style="display:none;"><b><fmt:message key="textWelcome1"/></b></li>
				<li><b><fmt:message key="textWelcome2"/></b></li>
			</ul>
			<script type="text/javascript">
				checkNavigator();
			</script>
		</fieldset>	
		<fieldset>
			<legend><b><fmt:message key="textCoursesList"/></b></legend>
			<ul style ="font-size:15px; ">
				<li><b><fmt:message key="textWelcome3"/></b></li>
			</ul>
			<ul class="listaopcionesgroup">
			<c:choose>
				<c:when test="${!empty groupList}">
					<c:set var="lastYear" value="-1"/>
					<c:forEach items="${groupList}" var="group">
						<c:if test="${group.year ne lastYear}">
							<c:if test="${lastYear ne '-1'}">
								</div>
								<br/>
								<br/>
							</c:if>
							<table align="center">
								<tr>
									<td><span style="font-size:22; font-weight:bold;"> <c:out value="${group.year}"></c:out> </span></td>
									<c:choose>
									<c:when test="${lastYear eq '-1'}">
										<td><img id="plus${group.year}" src="${pageContext.request.contextPath}/imagenes/mas.jpg" onclick="javascript:showYear('${group.year}');" style="display:none; border:none;"/></td>
										<td>&nbsp;</td>
										<td><img id="minus${group.year}" src="${pageContext.request.contextPath}/imagenes/menos.jpg" onclick="javascript:hideYear('${group.year}');" style="display:block; border:none;"/></td>
									</c:when>
									<c:otherwise>
										<td><img id="plus${group.year}" src="${pageContext.request.contextPath}/imagenes/mas.jpg" onclick="javascript:showYear('${group.year}');" style="display:block; border:none;"/></td>
										<td>&nbsp;</td>
										<td><img id="minus${group.year}" src="${pageContext.request.contextPath}/imagenes/menos.jpg" onclick="javascript:hideYear('${group.year}');" style="display:none; border:none;"/></td>
									</c:otherwise>
								</c:choose>
									
								</tr>
							</table>
								<c:choose>
									<c:when test="${lastYear eq '-1'}">
										<div id="div${group.year}" style="display:block">
									</c:when>
									<c:otherwise>
										<div id="div${group.year}" style="display:none">
									</c:otherwise>
								</c:choose>
								<c:set var="lastYear" value="${group.year }"/>
						</c:if>
							
							<li><a href="javascript:selectGroup('${group.id}')">${group.course.name }(${group.name })</a><br/></li>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<!--  No está matriculado en ningún grupo -->
				</c:otherwise>
			</c:choose>
			</ul>
			<input type="hidden" value="" id="group" name="group"/>
			<input type="hidden" value="" id="option" name="option"/>
			
		</fieldset>	
	</form>
	<c:if test="${!empty sendedMail}">
		<script>
			alert('<fmt:message key="textErrorSendMail"/>');
		</script>
	</c:if>
	<c:if test="${!empty noUserMail}">
		<script>
			alert('<fmt:message key="textErrorUserMail"/>');
		</script>
	</c:if>
</div>

</body>
	
</html>
