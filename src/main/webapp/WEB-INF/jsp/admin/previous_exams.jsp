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
	breadCrumb.addStep("Exámenes pasados",request.getContextPath()+"/admin/admin.itest?method=previousExams");
	request.setAttribute("breadCrumb",breadCrumb);
%>

	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminCourseMgmt.js'></script>
	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminGroupMgmt.js'></script>
	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>
	<script type='text/javascript' src='${pageContext.request.contextPath}/common/resources/dates.js'></script> 	
	<script type='text/javascript' src='${pageContext.request.contextPath}/common/resources/grafico.js'></script> 	
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos/grafico.css" />
	
	
<script type="text/javascript">
	function enableDivInfo(id){
		document.getElementById("plus"+id).style.display='none';
		document.getElementById("divInfo"+id).style.display='block';
		document.getElementById("minus"+id).style.display='block';
	}

	function disableDivInfo(id){
		document.getElementById("plus"+id).style.display='block';
		document.getElementById("divInfo"+id).style.display='none';
		document.getElementById("minus"+id).style.display='none';
	}


	function changeInstitution(){
		var institutionSelect = document.getElementById('institutionSelect');
		updateTable('');
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
		updateTable('');
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

	function showInfo(){
		var selectCourse = document.getElementById('courseSelect').value;
		var selectYear = document.getElementById('yearSelect').value;
		var institutionSelect = document.getElementById('institutionSelect').value;
		iTestLockPage();
		AdminMgmt.getPreviousExamsFiltered(institutionSelect,selectCourse,selectYear,{callback:updateTable,
			 timeout:callBackTimeOut,
			 errorHandler:function(message) { iTestUnlockPage('error');} });
	}
	var examId;
	function obtenEstadisticasDeExamen(idexam){
		iTestLockPage();
		examId = idexam
		AdminMgmt.getCourseStatsByExam(idexam,{callback:getStats,
			 timeout:callBackTimeOut,
			 errorHandler:function(message) {iTestUnlockPage('error');} })
		return false;
		}
	
	reverse = true;
	function orderTable(orderby){
		reverse = !reverse;
		iTestLockPage();
		AdminMgmt.orderPreviousExamsList(orderby,reverse,{callback:updateTable,
			 timeout:callBackTimeOut,
			 errorHandler:function(message) { iTestUnlockPage('error');} });
	}
	repaint = false;
	function updateTable(list){
		iTestUnlockPage();
		tbodyelement = document.createElement('tbody');
		tbodyelement.setAttribute("id","previousExamsTableBody");
		for(var i=0;i<list.length;i++){
			exam = list[i];
			rowelement = document.createElement('tr');
			cellelement = document.createElement('td');
			cellelement.setAttribute("id","center"+exam.examId);
			cellelement.innerHTML = exam.center;
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.setAttribute("id","subject"+exam.examId);
			cellelement.innerHTML = exam.subject;
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.setAttribute("id","title"+exam.examId);
			cellelement.innerHTML = exam.examTitle;
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.innerHTML = formatDate(exam.startDate,"d/MM/yy H:mm");
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.innerHTML = formatDate(exam.endDate,"d/MM/yy H:mm");
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.innerHTML = '<a href="" onclick="return obtenEstadisticasDeExamen('+exam.examId+')"><img src="${pageContext.request.contextPath}/imagenes/resultados.png" alt="<fmt:message key="buttonShowStats"/>" title="<fmt:message key="buttonShowStats"/>" border="none"></a>';
			hidenelement = document.createElement('input');
			hidenelement.setAttribute("type","hidden");
			hidenelement.setAttribute("id","academicYear"+exam.examId);
			hidenelement.setAttribute("value",exam.academicYear);
			
			rowelement.appendChild(cellelement);
			rowelement.appendChild(hidenelement);

			hidenelement = document.createElement('input');
			hidenelement.setAttribute("type","hidden");
			hidenelement.setAttribute("id","nameGroup"+exam.examId);
			hidenelement.setAttribute("value",exam.nameGroup);
			rowelement.appendChild(hidenelement);

			tbodyelement.appendChild(rowelement);
		}
		if (list.length == 0) {
		       if(!repaint){
		    	   rowelement = document.createElement('tr');
			       cellelement = document.createElement('td');
				   cellelement.colSpan=5;
			       cellelement.setAttribute("align","center");
				   cellelement.innerHTML = "<fmt:message key="noAvailableConfigExams"/>";
				   rowelement.appendChild(cellelement);
				   tbodyelement.appendChild(rowelement);
			   }else{
				   repaint = false;
				}
		    } else {
			   rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=6;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML ="<hr/>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=6;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML ="<b><fmt:message key="totalLabel"/> "+list.length+"</b>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		    }
		datatable=document.getElementById("previousExamsTable");
		// Replaces tbody			
		datatable.replaceChild(tbodyelement,document.getElementById("previousExamsTableBody"));
	}

	function redondeo2decimales(numero)
	{
		var original=parseFloat(numero);
		var result=Math.round(original*100)/100 ;
		return result;
	}
	
	function getStats(stats){
		iTestUnlockPage();
		//document.getElementById('').style.display = 'block';
		$("#divEstadisticas").show('slow',function(){});
		var p = new Array(redondeo2decimales(100*stats.ss/stats.totalStudent),redondeo2decimales(100*stats.ap/stats.totalStudent),
				redondeo2decimales(100*stats.nt/stats.totalStudent),redondeo2decimales(100*stats.sb/stats.totalStudent),redondeo2decimales(100*stats.numExams/stats.totalStudentByGroup),100);
		var t = new Array("<fmt:message key="labelSs"/>","<fmt:message key="labelAp"/>","<fmt:message key="labelNt"/>","<fmt:message key="labelSb"/>","<fmt:message key="headerTestedStudents"/>","<fmt:message key="headerRegisterStudents"/>");
		var v = new Array(stats.ss,stats.ap,stats.nt,stats.sb,stats.numExams,stats.totalStudentByGroup);
		var tit = new Array("<fmt:message key="textColumnCenter"/> : "+document.getElementById('center'+examId).innerHTML,"<fmt:message key="courseGroup"/> : "+document.getElementById('subject'+examId).innerHTML,
				"<fmt:message key="academicYear"/> : "+document.getElementById('academicYear'+examId).value, "<fmt:message key="textColumnGroup"/> : "+document.getElementById('nameGroup'+examId).value);
		document.getElementById('tableDiv').innerHTML = createTable(p,t,v,'80%','100%',tit);
		
	}
	
</script>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="admin"/>
	<jsp:param name="menu" value="admin"/>
</jsp:include>
		<div id="contenido">
			<fieldset>
				<legend><fmt:message key="labelFilterTitle"/></legend>
				
				<div>
					<div class="divDosIzq">
						<label><fmt:message key="labelCenter"/> </label>
		 				<select id="institutionSelect" onchange="repaint = true; javascript:changeInstitution();">
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
				 			<select id="courseSelect" onchange="repaint = true; javascript:updateTable('');">
				 			</select>
						</div>
					</div>
					<div class="divDosIzq">
						<div id="yearDiv" style="display:none">
							<label><fmt:message key="labelCourse"/></label>			
							<select id="yearSelect" onchange="repaint = true; javascript:changeYear();"></select>
						</div>
					</div>
					
				</div>
				<div class="divcentro">
						<input type="button" value="<fmt:message key="buttonFilterRun"/>" onclick="javascript:showInfo();">
				</div>
			</fieldset>
		
			<table id="previousExamsTable" class="tablaDatos">
				<tr>
		     	<th colspan="6"><center><fmt:message key="textTableHeadPreviousExams"/></center></th>
		    </tr>
     		<tr>
     			<th style="width:50%"><a href="javascript:orderTable('center');"><fmt:message key="textColumnCenter"/></a></th>
     			<th><a href="javascript:orderTable('subject');"><fmt:message key="courseGroup"/></a></th>
     			<th><a href="javascript:orderTable('title');"><fmt:message key="examTitle"/></a></th>
	     		<th><a href="javascript:orderTable('startDate');"><fmt:message key="textColumnStartDate"/></a></th>
	     		<th><a href="javascript:orderTable('endDate');"><fmt:message key="textColumnEndDate"/></a></th>
	     		<th></th>
     		</tr>
     		<tbody id="previousExamsTableBody">
     		</tbody>
			</table>
		</div>
	</body>
	<div id="divEstadisticas" class="floatingDiv" style="display:none">
		<div class="floatingDivBody">
		
		<div align="right"><a href="" align="right" onclick="$('#divEstadisticas').hide('slow',function(){}); return false;"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif"></a></div>
			<div id="tableDiv">
				
			</div>
		</div>
	</div>
</html>