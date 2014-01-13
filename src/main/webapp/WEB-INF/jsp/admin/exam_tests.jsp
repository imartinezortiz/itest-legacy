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
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminMgmt.js'></script>
  	<script type='text/javascript'>

		function deleteExam(idstudent,idexam){
			iTestLockPage('');
			AdminMgmt.deleteExams(${group.id},idstudent,idexam,{callback:repaintTableList,
				timeout:callBackTimeOut,
				errorHandler:function(message){ alert(message);iTestUnlockPage('');} });
		}

		function repaintTableList(list){
			var tableBody = document.getElementById('califsTableBody');
			tableBody.innerHTML='';
			for(var i=0;i<list.length;i++){
				var row = list[i];
				var tr = document.createElement('tr');
				var td = document.createElement('td');
				
				td.innerHTML = row.exam.id;
				tr.appendChild(td);

				td = document.createElement('td');
				if(row.exam!=null)
					td.innerHTML = row.exam.title;
				tr.appendChild(td);

				td = document.createElement('td');
				if(row.exam!=null)
					td.innerHTML =  row.exam.questionsNumber;
				tr.appendChild(td);

				td = document.createElement('td');
				td.innerHTML = row.user.id;
				tr.appendChild(td);

				td = document.createElement('td');
				td.innerHTML = row.failed;
				tr.appendChild(td);

				td = document.createElement('td');
				td.innerHTML = row.time+' ms';
				tr.appendChild(td);

				td = document.createElement('td');
				td.innerHTML = "<input type=\"button\" value=\"<fmt:message key="buttonPreview"/>\" onclick=\"javascript:showStudentExam("+row.user.id+","+row.exam.id+")\"/>";
				tr.appendChild(td);

				td = document.createElement('td');
				td.innerHTML = "<a href=\"javascript:deleteExam("+row.user.id+","+row.exam.id+")\"><img src=\"${pageContext.request.contextPath}/imagenes/borrar.gif\" alt=\"<fmt:message key="labelDeleteStdExam"/>\" title=\"<fmt:message key="labelDeleteStdExam"/>\" border=\"none\"></a>";
				tr.appendChild(td);

				tableBody.appendChild(tr);
				   
				
			}
			if(list.length==0){
				tr = document.createElement('tr');
				td = document.createElement('td');
				td.colSpan=8;
				td.setAttribute("align","center");
				td.innerHTML ="<fmt:message key="noAviableExam"/>";
				tr.appendChild(td);
				tableBody.appendChild(tr);
			}
			tr = document.createElement('tr');
			td = document.createElement('td');
			td.colSpan=8;
			td.setAttribute("align","center");
			td.innerHTML ="<hr/>";
			tr.appendChild(td);
			tableBody.appendChild(tr);
			tr = document.createElement('tr');
			td = document.createElement('td');
			td.colSpan=8;
			td.setAttribute("align","center");
			td.innerHTML ="<b><fmt:message key="totalLabel"/> "+list.length+"</b>";
			tr.appendChild(td);
			tableBody.appendChild(tr);
			iTestUnlockPage();
		}

		function showStudentExam(alu,exam) {
	    	window.open("${pageContext.request.contextPath}/admin/admin.itest?method=showStudentExam&alu="+alu+"&exam="+exam, "itest_preview", "width="+(screen.availWidth)+",height="+(screen.availHeight)+",top=0,left=0,scrollbars=yes");
	    }
		function deleteAllExams(){
			iTestLockPage();
			AdminMgmt.deleteAllGeneratedExamsTest(${group.id},${examid},{callback:repaintTableList,
				timeout:callBackTimeOut,
				handleError:function(message){iTestUnlockPage('error');}
				});
		}
		
  	</script>
  	<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
		<jsp:param name="userRole" value="admin"/>
		<jsp:param name="menu" value="admin"/>
	</jsp:include>
		<div id="contenido">
			<fieldset>
				<legend><fmt:message key="legendGeneratedExam"/></legend>
				<label><b><fmt:message key="timeTaken"/>:</b></label> ${time} ms<br/><br/>
				<input type="button" value="<fmt:message key="buttonDeleteAllExams"/>" onclick="javascript:deleteAllExams();"/>
				<table class="tabladatos">
					<thead>
						<tr>
							<th width="5%"><fmt:message key="labelExamId"/></th>
							<th width="70%"><fmt:message key="labelTitle"/></th>
							<th width="10%"><fmt:message key="labelQuestionNumber"/></th>
							<th width="5%"><fmt:message key="labelUserId"/></th>
							<th width="5%"><fmt:message key="labelFailed"/></th>
							<th width="5%"><fmt:message key="timeTaken"/></th>
							<th width="5%"></th>
							<th width="5%"></th>
						</tr>
					</thead>
					<tbody id="califsTableBody">
						<c:forEach items="${generatedExam}" var="row" varStatus="rowCount">
							<tr>
								<td>${row.exam.id}</td>
								<td>${row.exam.title}</td>
								<td>${row.exam.questionsNumber}</td>
								<td>${row.user.id}</td>
								<td>${row.failed}</td>
								<td>${row.time} ms</td>
								<td><input type="button" value="<fmt:message key="buttonPreview"/>" onclick="javascript:showStudentExam(${row.user.id},${examid})"/></td>
								<td><a href="javascript:deleteExam(${row.user.id},${row.exam.id})"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="labelDeleteStdExam"/>" title="<fmt:message key="labelDeleteStdExam"/>" border="none"></a></td>
							</tr>
							<c:set var="numRowsValue" value="${rowCount.count}"></c:set>
						</c:forEach>
						<tr>
							<td colspan="8"><hr></td>
						</tr>
						<tr>
							<td colspan="8"><b><center><fmt:message key="totalLabel"/>${numRowsValue}</center></b></td>
						</tr>
					</tbody>
				</table>
			</fieldset>
		</div>
	</body>
</html>