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
	breadCrumb.addStep("Estadísticas por centro",request.getContextPath()+"/admin/admin.itest?method=goStatsByCenter");
	request.setAttribute("breadCrumb",breadCrumb);
%>
<!-- Ajax for groups -->
	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminCourseMgmt.js'></script>
	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminGroupMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>
  	

<script type='text/javascript'>
function changeInstitution(){
	reset();
	var institutionSelect = document.getElementById('institutionSelect');
	document.getElementById('courseDiv').style.display='none';
	if(institutionSelect.selectedIndex!=0){
		institutionId = institutionSelect.value;
		iTestLockPage();
		AdminGroupMgmt.getGroupsByInstitution(institutionId,{callback:uploadGroupSelect,
			 timeout:callBackTimeOut,
			 errorHandler:function(message) { iTestUnlockPage('error');} });
	}else{
		document.getElementById('subjectOptionsDiv').style.display='none';
		document.getElementById('courseDiv').style.display='none';
		document.getElementById('yearDiv').style.display='none';
		document.getElementById('courseSelect').selectedIndex = 0;
		document.getElementById('yearSelect').selectedIndex = 0;
		
	}
}
function uploadGroupSelect(groups){
	// borramos los datos actuales
	do{
		var selectYear = document.getElementById('yearSelect');
		for(var i=0;i<selectYear.options.length;i++){
			selectYear.options[i]=null;
		}
	}while(selectYear.length!=0);

	do{
		var selectCourse = document.getElementById('courseSelect');
		for(var i=0;i<selectCourse.options.length;i++){
			selectCourse.options[i]=null;
		}
	}while(selectCourse.length !=0);
	
	selectYear.options[0]= new Option('--------','-1');
	selectCourse.options[0]= new Option('--------','-1');
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

function changeYear(){
	reset();
	var selectYear = document.getElementById('yearSelect');
	if(selectYear.selectedIndex != 0){
		institutionSelect = document.getElementById('institutionSelect');
		institutionId = institutionSelect.value;
		year = selectYear.value
		iTestLockPage();
		AdminCourseMgmt.getCourseByInstitutionAndGroup(institutionId,year,{callback:uploadCourseSelect,
			 timeout:callBackTimeOut,
			 errorHandler:function(message) { iTestUnlockPage('error');} });
	}else{
		document.getElementById('courseDiv').style.display='none';
		document.getElementById('courseSelect').selectedIndex = 0;
	}
}

function uploadCourseSelect(courses){
	// borramos los datos actuales
	do{
		var selectCourse = document.getElementById('courseSelect');
		for(var i=0;i<selectCourse.options.length;i++){
			selectCourse.options[i]=null;
		}
	}while(selectCourse.length !=0);
	
	selectCourse.options[0]= new Option('--------','-1');
	for(var i=0;i<courses.length;i++){
		course = courses[i];
		bool = false;
		for(var j=1;j<selectCourse.length;j++){
			if(selectCourse.options[j].innerHTML == course.name){
				bool = true;
			}
		}
		if(!bool){
			selectCourse.options[selectCourse.length]= new Option(course.name,course.id);
		}
	}
	document.getElementById('courseDiv').style.display='block';
	iTestUnlockPage();
}

function showStats(){
	var selectCourse = document.getElementById('courseSelect');
	var selectYear = document.getElementById('yearSelect');
	var institutionSelect = document.getElementById('institutionSelect');
	iTestLockPage();
	AdminGroupMgmt.showStatsByInstitution(institutionSelect.value,
											selectYear.value,
											selectCourse.value,
											{callback:updateStats,
												 timeout:callBackTimeOut,
												 errorHandler:function(message) { iTestUnlockPage('error');} });
}

function updateStats(stats){
	document.getElementById('teachers').innerHTML = stats.teachers;
	document.getElementById('students').innerHTML = stats.students;
	document.getElementById('subjects').innerHTML = stats.subjects;
	document.getElementById('groups').innerHTML = stats.groups;
	document.getElementById('exams').innerHTML = stats.configExams;
	document.getElementById('teachers%').innerHTML = redondeo2decimales(stats.teachers*100/stats.allTeachers)+'%';
	document.getElementById('students%').innerHTML = redondeo2decimales(stats.students*100/stats.allStudents)+'%';
	document.getElementById('subjects%').innerHTML = redondeo2decimales(stats.subjects*100/stats.allSubjects)+'%';
	document.getElementById('groups%').innerHTML = redondeo2decimales(stats.groups*100/stats.allGroups)+'%';
	document.getElementById('exams%').innerHTML = redondeo2decimales(stats.configExams*100/stats.allConfigExams)+'%';
	iTestUnlockPage();
}
function redondeo2decimales(numero)
{
	var original=parseFloat(numero);
	var result=Math.round(original*100)/100 ;
	return result;
}

function showGroups(){
	var selectCourse = document.getElementById('courseSelect');
	var selectYear = document.getElementById('yearSelect');
	var institutionSelect = document.getElementById('institutionSelect');
	iTestLockPage();
	AdminGroupMgmt.showGroupDetails(institutionSelect.value,
			selectYear.value,
			selectCourse.value,
			{callback:updateGroupList,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });

	
}

function updateGroupList(list){
	iTestUnlockPage();

	document.getElementById('groupList').innerHTML = '';
	ulList = document.createElement('ul');
	for(var i=0;i<list.length;i++){
		group = list[i];
		listelement = document.createElement('li');
		p = document.createElement('label');
		p.innerHTML = '<b><fmt:message key="labelFilterInstitution"/> </b>'+group.centerName;
		listelement.appendChild(p);
		listelement.appendChild(document.createElement('br'));
		
		p = document.createElement('label');
		p.innerHTML = '<b><fmt:message key="scopeCourse"/>: </b>'+group.subjectName;
		listelement.appendChild(p);
		listelement.appendChild(document.createElement('br'));

		p = document.createElement('label');
		p.innerHTML = '<b><fmt:message key="scopeGroup"/>: </b>'+group.groupName;
		listelement.appendChild(p);
		listelement.appendChild(document.createElement('br'));

		p = document.createElement('label');
		p.innerHTML = '<b><fmt:message key="academicYear"/>: </b>'+group.year;
		listelement.appendChild(p);
		listelement.appendChild(document.createElement('br'));

		p = document.createElement('label');
		p.innerHTML = '<b><fmt:message key="numQuestions"/>: </b>'+group.numQuestion;
		listelement.appendChild(p);
		listelement.appendChild(document.createElement('br'));
		
		p = document.createElement('label');
		p.innerHTML = '<b><fmt:message key="numThemes"/>: </b>'+group.numTheme;
		listelement.appendChild(p);
		listelement.appendChild(document.createElement('br'));

		p = document.createElement('label');
		p.innerHTML = '<b><fmt:message key="taskShowTutors"/>: </b>';
		var ul = document.createElement('ul');
		for(var j=1;j<=group.teachers.length;j++){
			var li = document.createElement('li');
			li.innerHTML = j+". "+group.teachers[j-1];
			ul.appendChild(li);
		}
		listelement.appendChild(p);
		listelement.appendChild(ul);
		listelement.appendChild(document.createElement('br'));

		
		ulList.appendChild(listelement);
		ulList.innerHTML = ulList.innerHTML +"<hr/>";
	}
	$('#divMoreInfo').show('slow',function(){});
	document.getElementById('groupList').appendChild(ulList);
	return false;
}

function reset(){
	document.getElementById('teachers').innerHTML = '';
	document.getElementById('students').innerHTML = '';
	document.getElementById('subjects').innerHTML = '';
	document.getElementById('groups').innerHTML = '';
	document.getElementById('exams').innerHTML = '';
	document.getElementById('teachers%').innerHTML = '';
	document.getElementById('students%').innerHTML = '';
	document.getElementById('subjects%').innerHTML = '';
	document.getElementById('groups%').innerHTML = '';
	document.getElementById('exams%').innerHTML = '';
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
			<div class="divDosDer">
				<div id="courseDiv" style="display:none">
					<label><fmt:message key="menuCourses"/> </label>
		 			<select id="courseSelect" onchange="reset();">
		 			</select>
				</div>
			</div>
			<div class="divDosIzq">
				<div id="yearDiv" style="display:none">
					<label><fmt:message key="labelCourse"/></label>			
					<select id="yearSelect" onchange="javascript:changeYear();"></select>
				</div>
			</div>
			
		</div>
		<div class="divcentro">
			<input type="button" value="<fmt:message key="buttonShowStats"/>" onclick="javascript:showStats();">
			<input type="button" value="<fmt:message key="buttonShowGroups"/>" onclick="javascript:showGroups(); ">
		</div>
	</fieldset>
	
	<fieldset style="margin-top:5%">
		<legend><fmt:message key="labelStats"/></legend>
		<table class="tablaDatos">
			<tbody>
				<tr>
					<th><fmt:message key="headerTeachersNumber"/></th>
					<th><fmt:message key="headerStudentsNumber"/></th>
					<th><fmt:message key="headerSubjectsNumber"/></th>
					<th><fmt:message key="headerGroupsNumber"/></th>
					<th><fmt:message key="headerExamsConfigNumber"/></th>
				</tr>
				<tr>
					<td id="teachers"></td>
					<td id="students"></td>
					<td id="subjects"></td>
					<td id="groups"></td>
					<td id="exams"></td>
				</tr>
				<tr>
					<td id="teachers%"></td>
					<td id="students%"></td>
					<td id="subjects%"></td>
					<td id="groups%"></td>
					<td id="exams%"></td>
				</tr>
			</tbody>
		</table>
	</fieldset>
</div>
<div id="divMoreInfo" class="floatingDiv" style="display:none">
		<div class="floatingDivBody" style="overflow-y: scroll">
		<div align="right" style="position: fixed; background-color:white"><a href="" align="right" onclick="$('#divMoreInfo').hide('slow',function(){}); return false;"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif"></a></div>
			<div id="divAction" style="background-color:#FFECD9"></div>
			<br/>
			<div id="groupList" align="left">
			
			</div>
		</div>
	</div>
</body>
</html>