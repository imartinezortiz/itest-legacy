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
	breadCrumb.addStep("Prueba carga",request.getContextPath()+"/admin/admin.itest?method=loadTests");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<!-- JavaScript Calendar -->
  	<script type='text/javascript' src='${pageContext.request.contextPath}/common/resources/scw.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminGroupMgmt.js'></script>

  	<script type='text/javascript'>
  		var maxExams = 0;

		function setMaxExams(max){
			maxExams = max;
		}
  		
  		function changedCourse(){
			var courseSelect = document.getElementById('coursesSelect');
			var idCourse = courseSelect[courseSelect.selectedIndex].value;
			if(idCourse == -1){
				hideSelectGroups();
			}else{
				//Pedir lista de grupos mediante ajax
				iTestLockPage('');
				AdminGroupMgmt.getGroupsByCourse(idCourse,{callback:repaintGroupsSelect,
					 timeout:callBackTimeOut,
					 errorHandler:function(message) {iTestUnlockPage('error');} });
			}
  	  	}

  	  	function changedGroup(){
			var groupSelect = document.getElementById('groupsSelect');
			var idGroup = groupSelect[groupSelect.selectedIndex].value;
			if(idGroup==-1){
				hideSelectExams();
			}else{
				iTestLockPage('');
				AdminGroupMgmt.getExamsByGroup(idGroup,{callback:repaintExamsSelect,
					 timeout:callBackTimeOut,
					 errorHandler:function(message) {iTestUnlockPage('error');} });
			}
  	  	}

  	  	function repaintExamsSelect(exams){
			if(exams!=null){
				hideSelectExams();
				var examSelect = document.getElementById('examsSelect');
				var inputNumber = document.getElementById('divExamsNumber');
				examSelect.style.display='';
				inputNumber.style.display='';
				for(var i=0;i<exams.length;i++){
					var exam = exams[i];
					var option = document.createElement('option');
					option.value = exam.id;
					option.innerHTML = exam.title;
					examSelect.appendChild(option);
				}
			}
			iTestUnlockPage();
  	  	}
  	  	
		function repaintGroupsSelect(groups){
			if(groups!=null){
				hideSelectGroups();
				var groupSelect = document.getElementById('groupsSelect');
				groupSelect.style.display='';
				for(var i=0;i<groups.length;i++){
					var group = groups[i];
					var option = document.createElement('option');
					option.value = group.id;
					option.innerHTML = group.name+"("+group.year+")";
					groupSelect.appendChild(option);
				}
			}
			iTestUnlockPage();
		}
  		
  	  	function hideSelectGroups(){
			var groupSelect = document.getElementById('groupsSelect');
			document.getElementById('labelMaxExams').innerHTML = '0';
			groupSelect.innerHTML='<option value="-1"> ------------ </option>';
			groupSelect.style.display='none';
			hideSelectExams();
  	  	}

  	  	function hideSelectExams(){
  	  		document.getElementById('labelMaxExams').innerHTML = '0';
			var examsSelect = document.getElementById('examsSelect');
			var inputNumber = document.getElementById('divExamsNumber');
			examsSelect.innerHTML='<option value="-1"> ------------ </option>';
			examsSelect.style.display='none';
			inputNumber.style.display='none';
  	  	}

  	  	function fillValues(){
  	  	  	var examsNumber = document.getElementById('inputExamsNumber').value;
  	  		var examsSelect = document.getElementById('examsSelect');
  	  		var examId = examsSelect.value;
  	  		var maxExams = document.getElementById('maxExams').value;
  	  		var groupId = document.getElementById('groupsSelect').value
  	  		
  	  	  	var value = IsNumeric(examsNumber) && IsNumeric(examId) && IsNumeric(maxExams) && IsNumeric(groupId);
			if(value && examId>-1 && maxExams>0 && examsNumber<=maxExams && groupId>0){
				document.getElementById('examId').value = examId;
				document.getElementById('examsNumber').value = examsNumber;
				document.getElementById('groupId').value = groupId;
			}else{
				return false;
			}
			document.getElementById('examsForm').submit();
  	  	}
	  	  function IsNumeric(input) {    
	  	  		return (input - 0) == input && input.length > 0; 
	  	  }
	
	  	  function changedExam(){
	  		
	  		var examsSelect = document.getElementById('examsSelect');
	  		var examId = examsSelect.value;
	  		if(examId==-1){
	  			setMaxExams(0);
		  	}else{
		  		var groupSelect = document.getElementById('groupsSelect');
				var idGroup = groupSelect[groupSelect.selectedIndex].value;
		  		if(idGroup != -1){
		  			iTestLockPage('');
					AdminGroupMgmt.getLearnersNumber(idGroup,{callback:setMaxExams,
						 timeout:callBackTimeOut,
						 errorHandler:function(message) {iTestUnlockPage('error');} });
			  	}
			}
	  	  }

	  	  function setMaxExams(maxExams){
				document.getElementById('maxExams').value=maxExams;
				document.getElementById('labelMaxExams').innerHTML = maxExams;
				iTestUnlockPage();
		  }

  	</script>
  	
  	<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
		<jsp:param name="userRole" value="admin"/>
		<jsp:param name="menu" value="admin"/>
	</jsp:include>
		<div id="contenido">
		<div class="divDosIzq">
				<select id="coursesSelect" onChange="javascript:changedCourse();">
					<option value="-1"> ---------------- </option>
					<c:forEach items="${courses}" var="course">
						<option value="${course.id}">${course.name}</option>
					</c:forEach>
				</select>
				<br/>
				<br/>
				<select id="groupsSelect" onChange="javascript:changedGroup();" style="display:none;">
				</select>
			</div>
			
			<div class="divDosDer">
				<select id="examsSelect" style="display:none;" onChange="javascript:changedExam();">
				</select>
				<br/>
				<br/>
				<div id="divExamsNumber" style="display:none">
					<label><fmt:message key="headerNumberExams"/>:</label>
					<input id="inputExamsNumber" type="text"/><br/>
					<b><fmt:message key="labelMaxExams"/>:<label id="labelMaxExams"></label></b>
				</div>
			</div>	
			
			<div class="divcentro">
				<form id="examsForm" action="${pageContext.request.contextPath}/admin/admin.itest?method=generateTestExams" method="POST">
					<input id="examId" name="examId" type="hidden" value=""/>
					<input id="examsNumber" name="examsNumber" type="hidden" value=""/>
					<input id="maxExams" name="maxExams" type="hidden" value=""/>
					<input id="groupId" name="groupId" type="hidden" value=""/>
					<input type="button" onclick="javascript:fillValues();" value="<fmt:message key="buttonGenerateExams"/>">
				</form>
			</div>
			
		
		</div>
	</body>
</html>