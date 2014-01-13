<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fmt_rt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>
<%@ page import="com.cesfelipesegundo.itis.model.ExamGlobalInfo" %>
<% 
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addBundleStep("textMain","");
	breadCrumb.addStep("Estadísticas por asignatura",request.getContextPath()+"/admin/admin.itest?method=goStatsBySubject");
	request.setAttribute("breadCrumb",breadCrumb);
%>
<!-- Ajax for groups -->
	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminCourseMgmt.js'></script>
	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminGroupMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>
	<script type='text/javascript' src='${pageContext.request.contextPath}/common/resources/grafico.js'></script> 	
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/grafico.css" />

<script type="text/javascript">
	function showStats(){
		var selectInstitution = document.getElementById('institutionSelect');
		var selectCourse = document.getElementById('courseSelect');
		var selectYear = document.getElementById('yearSelect');
		iTestLockPage();
		AdminCourseMgmt.getCourseStats(selectInstitution.value,selectCourse.value,selectYear.value,{callback:updateStats,
			 timeout:callBackTimeOut,
			 errorHandler:function(message) { iTestUnlockPage('error');} });
		
	}

	function redondeo2decimales(numero)
	{
		var original=parseFloat(numero);
		var result=Math.round(original*100)/100 ;
		return result;
	}
	
	function updateStats(stats){
		document.getElementById('column n groups').innerHTML=redondeo2decimales(stats.groups);
		document.getElementById('column n exams').innerHTML=redondeo2decimales(stats.numExams);
		document.getElementById('column average student').innerHTML=redondeo2decimales(stats.avgStudents);
		document.getElementById('column total student').innerHTML=redondeo2decimales(stats.totalStudentByGroup);
		document.getElementById('column ss').innerHTML=redondeo2decimales(stats.ss);
		document.getElementById('column ap').innerHTML=redondeo2decimales(stats.ap);
		document.getElementById('column nt').innerHTML=redondeo2decimales(stats.nt);
		document.getElementById('column sb').innerHTML=redondeo2decimales(stats.sb);
		document.getElementById('column average student%').innerHTML=redondeo2decimales(100*stats.avgStudents/stats.totalStudentByGroup)+'%';
		document.getElementById('column ss%').innerHTML=redondeo2decimales(100*stats.ss/stats.totalStudent)+'%';
		document.getElementById('column ap%').innerHTML=redondeo2decimales(100*stats.ap/stats.totalStudent)+'%';
		document.getElementById('column nt%').innerHTML=redondeo2decimales(100*stats.nt/stats.totalStudent)+'%';
		document.getElementById('column sb%').innerHTML=redondeo2decimales(100*stats.sb/stats.totalStudent)+'%';
		iTestUnlockPage();
		document.getElementById("divGraphic").style.display = 'block';
		var p = new Array(redondeo2decimales(100*stats.ss/stats.totalStudent),redondeo2decimales(100*stats.ap/stats.totalStudent),
				redondeo2decimales(100*stats.nt/stats.totalStudent),redondeo2decimales(100*stats.sb/stats.totalStudent));
		var t = new Array("<fmt:message key="labelSs"/>","<fmt:message key="labelAp"/>","<fmt:message key="labelNt"/>","<fmt:message key="labelSb"/>");
		var v = new Array(stats.ss,stats.ap,stats.nt,stats.sb);
		document.getElementById('divGraphic').innerHTML = createTable(p,t,v,'400px','50%');
	}

	function changeInstitution(){
		reset();
		var selectInstitution = document.getElementById('institutionSelect');
		var idInstitution = selectInstitution.options[selectInstitution.selectedIndex].value;
		if(selectInstitution.selectedIndex!=0){
			iTestLockPage();
			AdminCourseMgmt.getCourses(idInstitution,{callback:uploadCourseSelect,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
		}else{
			document.getElementById('subjectOptionsDiv').style.display='none';
			document.getElementById('courseDiv').style.display='none';
			var selectCourse = document.getElementById('courseSelect');
			selectCourse.innerHTML='';
			selectCourse.options[0]= new Option('--------','-1');
			selectCourse.selectedIndex = 0;
			document.getElementById('yearSelect').selectedIndex = 0;
			
		}
		document.getElementById('yearDiv').style.display='none';
	}

	function changeCourse(){
		reset();
		var selectInstitution = document.getElementById('institutionSelect');
		var selectCourse = document.getElementById('courseSelect');
		if(selectCourse.selectedIndex!=0){
			iTestLockPage();
			AdminGroupMgmt.getGroups(selectCourse.options[selectCourse.selectedIndex].value, selectInstitution.options[selectInstitution.selectedIndex].value,{callback:uploadYearGroupSelect,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
		}else{
			document.getElementById('yearDiv').style.display='none';
			document.getElementById('yearSelect').selectedIndex = 0;
		}
	}

	function uploadCourseSelect(courses){
		// borramos los datos actuales
		var selectCourse = document.getElementById('courseSelect');
		do{
			for(var i=0;i<selectCourse.options.length;i++){
				selectCourse.options[i]=null;
			}
		}while(selectCourse.length!=0);

		
		selectCourse.options[0]= new Option('--------','-1');
		for(var i=0;i<courses.length;i++){
			course = courses[i];
			selectCourse.options[i+1]= new Option(course.name,course.id);
		}
		document.getElementById('subjectOptionsDiv').style.display='block';
		document.getElementById('courseDiv').style.display='block';
		iTestUnlockPage();
	}
	function uploadYearGroupSelect(groups){
		// borramos los datos actuales
		var selectYear = document.getElementById('yearSelect');
		do{
			for(var i=0;i<selectYear.options.length;i++){
				selectYear.options[i]=null;
			}
		}while(selectYear.length!=0);
		
		selectYear.options[0]= new Option('--------','-1');
		for(var i=0;i<groups.length;i++){
			group = groups[i];
			bool = false;
			for(var j=1;j<selectYear.length;j++){
				if(selectYear.options[j].innerHTML == group.year){
					bool = true;
				}
			}
			if(!bool){
				selectYear.options[selectYear.length]= new Option(group.year,group.year);
			}
		}
		document.getElementById('subjectOptionsDiv').style.display='block';
		document.getElementById('yearDiv').style.display='block';
		iTestUnlockPage();
	}
	function reset(){
		document.getElementById('column n groups').innerHTML='';
		document.getElementById('column n exams').innerHTML='';
		document.getElementById('column average student').innerHTML='';
		document.getElementById('column total student').innerHTML='';
		document.getElementById('column ss').innerHTML='';
		document.getElementById('column ap').innerHTML='';
		document.getElementById('column nt').innerHTML='';
		document.getElementById('column sb').innerHTML='';
		document.getElementById('column average student%').innerHTML='';
		document.getElementById('column ss%').innerHTML='';
		document.getElementById('column ap%').innerHTML='';
		document.getElementById('column nt%').innerHTML='';
		document.getElementById('column sb%').innerHTML='';
		document.getElementById("divGraphic").style.display = 'none';
	}
</script>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="admin"/>
	<jsp:param name="menu" value="admin"/>
</jsp:include>
<div id="contenido">
	<fieldset>
		<legend> <fmt:message key="lengendOptions"/> </legend>
		<div>
			<div class="divDosIzq">
				<label><fmt:message key="labelCenter"/> </label>
 				<select id="institutionSelect" onchange="javascript:changeInstitution();">
 					<option value=-1>--------</option>
 					<c:forEach items="${institutions}" var="institution">
	 					<option value="${institution.id}">
							<c:out value="${institution.name}"/>
						</option>
	 				</c:forEach>
	 			</select>
			</div>
			<div class="divDosDer">
			</div>
			<div class="divcentro">
			</div>
		</div>
		<div id="subjectOptionsDiv" style="display:none">
			<div class="divDosIzq">
				<div id="courseDiv">
					<label><fmt:message key="labelSubject"/> </label>
		 			<select id="courseSelect" onchange="javascript:changeCourse();">
		 			</select>
				</div>
			</div>
			<div class="divDosDer">
				<div id="yearDiv" style="display:none">
					<label><fmt:message key="labelCourse"/></label>			
					<select id="yearSelect" onchange="javascript:reset();"></select>
				</div>
			</div>
		</div>
		<div class="divcentro">
			<input type="button" value="<fmt:message key="buttonShowStats"/>" onclick="javascript:showStats();">
		</div>
	</fieldset>
	<fieldset style="margin-top:5%">
		<legend><fmt:message key="labelStats"/></legend>
		<table class="tablaDatos">
			<tbody>
				<tr>
					<th><fmt:message key="labelGroupNumbers"/></th>
					<th><fmt:message key="headerNumberExams"/></th>
					<th><fmt:message key="headerStudentExamAverage"/></th>
					<th><fmt:message key="headerTotalStudents"/></th>
					<th><fmt:message key="labelSs"/></th>
					<th><fmt:message key="labelAp"/></th>
					<th><fmt:message key="labelNt"/></th>
					<th><fmt:message key="labelSb"/></th>
				</tr>
				<tr>
					<td id="column n groups"></td>
					<td id="column n exams"></td>
					<td id="column average student"></td>
					<td id="column total student"></td>
					<td id="column ss"></td>
					<td id="column ap"></td>
					<td id="column nt"></td>
					<td id="column sb"></td>
				</tr>
				<tr>
					<td></td>
					<td id="column n exams%"></td>
					<td id="column average student%"></td>
					<td></td>
					<td id="column ss%"></td>
					<td id="column ap%"></td>
					<td id="column nt%"></td>
					<td id="column sb%"></td>
				</tr>
			</tbody>
		</table>
	</fieldset>
	<br/>
	<div id=statsTableDiv" class="divcentro">
		
	</div>
	<fieldset style="margin-top:5%">
		<legend> <fmt:message key="legendGraphics"/> </legend>
		<div id="divGraphic" style="display:none">
			
		</div>
	</fieldset>
</div>