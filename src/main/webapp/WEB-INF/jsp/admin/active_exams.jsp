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
	breadCrumb.addStep("Exámenes activos",request.getContextPath()+"/admin/admin.itest?method=activeExams");
	request.setAttribute("breadCrumb",breadCrumb);
%>
	<!-- JavaScript Calendar -->
  	<script type='text/javascript' src='${pageContext.request.contextPath}/common/resources/scw.js'></script>
  	
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>
	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminMgmt.js'></script>
	<script type='text/javascript' src='${pageContext.request.contextPath}/common/resources/dates.js'></script> 	
	
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
	reverse = false;
	function orderTable(sort){
		iTestLockPage();
		AdminMgmt.orderActiveExamsList(sort,reverse,{callback:updateTable,
			 timeout:callBackTimeOut,
			 errorHandler:function(message) { iTestUnlockPage('error');} });
		reverse = !reverse;
	}
	
	function findExams(){
		var center = document.getElementById('centerFilter').value;
		var course = document.getElementById('courseFilter').value;
		var startDate = document.getElementById('startDate').value;
		var endDate = document.getElementById('endDate').value;
		iTestLockPage();
		AdminMgmt.getActiveExamsFiltered(center,course,startDate,endDate,{callback:updateTable,
			 timeout:callBackTimeOut,
			 errorHandler:function(message) { iTestUnlockPage('error');} });
		
	}

	function updateTable(list){
		iTestUnlockPage();
		tbodyelement = document.createElement('tbody');
		tbodyelement.setAttribute("id","activeExamsTableBody");
		
		for(var i=0;i<list.length;i++){
			exam = list[i];

			rowelement = document.createElement('tr');
			cellelement = document.createElement('td');
			cellelement.setAttribute("id","center"+exam.examId);
			cellelement.innerHTML = exam.center;
			//Añadir divInfo
				divelement = document.createElement('div');
				divelement.setAttribute("id","divInfo"+exam.examId);
				divelement.style.display="none";
					parrafo = document.createElement('p');
					parrafo.innerHTML = "<table class=\"tablaDatos\">"+
						"<td><b><fmt:message key="textColumnExamTittle"/></b> "+exam.examTitle+"</td>"+
						"<td><b><fmt:message key="textColumnExamSubject"/></b> "+exam.subject+"</td>"+
						"</table>"
				divelement.appendChild(parrafo);
					parrafo = document.createElement('p');
					//La lista de profesores
					listaProfesores = document.createElement('ol');
					//Relleneamos la lista de profesores
					for(var j=0;j<exam.teachers.length;j++){
						teacher = exam.teachers[j];
						listElement = document.createElement('li');
						listElement.innerHTML = teacher;
						listaProfesores.appendChild(listElement);
					}
					//fin lista de profesores
					parrafo.innerHTML = '<table class="tablaDatos">'+
						'<tr><td><b><fmt:message key="textColumnExamTeachers"/></b></td><td>'+listaProfesores.innerHTML+'</td></tr></table>';
				divelement.appendChild(parrafo);
			//fin añadir divInfo			
			cellelement.appendChild(divelement);
			//fin de la celda
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.setAttribute("id","subject"+exam.examId);
			cellelement.innerHTML = exam.subject+"("+exam.nameGroup+")";
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.innerHTML = formatDate(exam.startDate,"d/MM/yy H:mm");
			cellelement.setAttribute("align","center");
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.innerHTML = formatDate(exam.endDate,"d/MM/yy H:mm");
			cellelement.setAttribute("align","center");
			rowelement.appendChild(cellelement);


			cellelement = document.createElement('td');
			cellelement.innerHTML = '<a><img id="plus'+exam.examId+'" title="<fmt:message key="buttonShowDetailsTheme"/>" onclick="enableDivInfo('+exam.examId+')" src="${pageContext.request.contextPath}/imagenes/mas.jpg" style="border:none;"/><img id="minus'+exam.examId+'" title="<fmt:message key="buttonNoShowDetailsTheme"/>" onclick="disableDivInfo('+exam.examId+')" src="${pageContext.request.contextPath}/imagenes/menos.jpg" style="border:none; display:none"/></a>';
			rowelement.appendChild(cellelement);

			tbodyelement.appendChild(rowelement);
		}
		datatable=document.getElementById("Tabla1");
		// Replaces tbody			
		datatable.replaceChild(tbodyelement,document.getElementById("activeExamsTableBody"));
	}
</script>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="admin"/>
	<jsp:param name="menu" value="admin"/>
</jsp:include>
<div id="contenido">

<fieldset>
	<legend><fmt:message key="labelFilterTitle"/></legend>
	<form>
		<div class="divDosIzq">
			<table>
				<tr><td><fmt:message key="textColumnCenter"/> :</td><td><input type="text" id="centerFilter"/></td></tr>
				<tr><td><fmt:message key="courseGroup"/> :</td><td><input type="text" id="courseFilter"/></td></tr>
			</table>
		</div>
		<div class="divDosDer">
			<table>
				<tr><td><fmt:message key="textColumnStartDate"/> : <fmt:message key="labelGreaterOrEquals"/></td><td><input id="startDate" name="textfechafin" value="" size="8" readonly maxlength="8" type="text"/><img src="${pageContext.request.contextPath}/imagenes/scw.gif" onclick="javascript:scwShow(getElementById('startDate'),this);"/>&nbsp;&nbsp;&nbsp;</td></tr>
				<tr><td><fmt:message key="textColumnEndDate"/> :  <fmt:message key="labellessOrEquals"/></td><td><input id="endDate" name="textfechafin" value="" size="8" readonly maxlength="8" type="text"/><img src="${pageContext.request.contextPath}/imagenes/scw.gif" onclick="javascript:scwShow(getElementById('endDate'),this);"/>&nbsp;&nbsp;&nbsp;</td></tr>
			</table>
		</div>
		<div class="divcentro">
			<input type="reset" value="<fmt:message key="msgbuttonReset"/>"/>
			<input type="button" onclick="javascript:findExams()" value="<fmt:message key="buttonFilterRun"/>"/>
		</div>
	</form>
</fieldset>

     <table id="Tabla1" class="tablaDatos">
     	<tr>
	     	<th colspan="5"><center><fmt:message key="textTableHeadActiveExams"/></center></th>
	    </tr>
   		<tr>
   			<th style="width:60%"> <a href="javascript:orderTable('center');"><fmt:message key="textColumnCenter"/></a></th>
   			<th><a href="javascript:orderTable('subject');"><fmt:message key="courseGroup"/></a></th>
    		<th><a href="javascript:orderTable('startDate');"><fmt:message key="textColumnStartDate"/></a></th>
    		<th><a href="javascript:orderTable('endDate');"><fmt:message key="textColumnEndDate"/></a></th>
    		<th></th>
   		</tr>
     	<tbody id="activeExamsTableBody">
	     	<c:forEach items="${activeExams}" var="exam">
		     	<tr>
		     		<td>
		     			<c:out value="${exam.center}"></c:out>
		     			<div id="divInfo${exam.examId}" style="display:none;">
		     				<p>
		     					<table class="tablaDatos">
		     						<td><b><fmt:message key="textColumnExamTittle"/></b> <c:out value="${exam.examTitle}"></c:out></td>
		     						<td><b><fmt:message key="textColumnExamSubject"/></b> <c:out value="${exam.subject}"></c:out></td>
		     					</table>
		     				</p>
		     				<p> 
		     					<table class="tablaDatos">
			     					<tr>
			     						<td><b><fmt:message key="textColumnExamTeachers"/></b></td>
				     					<td>
				     						<ol>
						     					<c:forEach items="${exam.teachers}" var="teachers">
						     						<li><c:out value="${teachers}"/></li>
						     					</c:forEach>
				     						</ol>
				     					</td>
			     					</tr>
			     				</table>
		     				</p>
		     			</div>
		     		</td>
		     		<td><c:out value="${exam.subject}"></c:out>(<c:out value="${exam.nameGroup}"></c:out>)</td>
		     		<td style="text-align:center"><fmt_rt:formatDate value="${exam.startDate}" type="both" dateStyle="short" timeStyle="short" /></td>
 			    	<td style="text-align:center"><fmt_rt:formatDate value="${exam.endDate}" type="both" dateStyle="short" timeStyle="short" /></td>
 			    	<td>
 			    		<img id="plus${exam.examId}" title="<fmt:message key="buttonShowDetailsTheme"/>" onclick="enableDivInfo(${exam.examId})" src="${pageContext.request.contextPath}/imagenes/mas.jpg" style="border:none;"/>
 			    		<img id="minus${exam.examId}" title="<fmt:message key="buttonNoShowDetailsTheme"/>" onclick="disableDivInfo(${exam.examId})" src="${pageContext.request.contextPath}/imagenes/menos.jpg" style="border:none; display:none"/>
 			    	</td>		     	
 			    </tr>
		    </c:forEach>
	     </tbody>
     </table>
</div>
</body>
</html>