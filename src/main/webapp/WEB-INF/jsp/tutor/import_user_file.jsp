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
	breadCrumb.addBundleStep("taskImportStudentFromFile","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="tutor"/>
	<jsp:param name="menu" value="tutor"/>
</jsp:include>

<script type='text/javascript' src='${pageContext.request.contextPath}/common/resources/scw.js'></script>

	<!-- Ajax for grades list -->
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/StudentListMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>
  	
  	<script type="text/javascript">
  		function notAllowedExtendsion(){
			alert('<fmt:message key="labelNotAllowedExtension"/>');
			iTestUnlockPage();
  	  	}

  	  	function noFileFound(){
  	  		alert('<fmt:message key="labelFileNotFound"/>');
  	  		iTestUnlockPage();
  	  	}

  	  	function succesUpload(xml){
  	  	  	if(typeof(xml) != "undefined"){
  	  	  	  	if(typeof(xml) == "string" && xml.length==0){
  	  	  	  		alert('<fmt:message key="labelFormatError"/>');
  	  	  	 		iTestUnlockPage();
  	  	  	  	  	return;
  	  	  	  	}
	  	  	  	var algo = xml.split('[');
	  	  	  	xml = algo[1];
	  	  		algo = xml.split(']');
	  	  		xml = algo[0];
	  	  		var body = document.getElementById('bodyUserTable');
	  	  		body.innerHTML='';
	  	  		var bodyIncident = document.getElementById('bodyUserIncidentTable');
	  	  		bodyIncident.innerHTML='';
	  	  		var cont = 0;
	  	  		var auxCont = 0;
	  	  		body.innerHTML='';
	  	  		$(xml).find('user').each(function(){
	  	  			var persId = $(this).find('persId').text();
	  	  			var name = $(this).find('name').text();
	  	  			var surname = $(this).find('surname').text();
	  	  			var userName = $(this).find('userName').text();
	  				var email = $(this).find('email').text();
	  				var isInDB = $(this).find('isInDB').text();
	  				var imported = $(this).find('imported').text();
	  				var repeated = $(this).find('repeated').text();

	  				var tr = document.createElement('tr');
	  				var td = document.createElement('td');

	  				td.innerHTML = persId;
	  				tr.appendChild(td);
	
	  				td = document.createElement('td');
	  				td.innerHTML = name;
	  				tr.appendChild(td);
	
	  				td = document.createElement('td');
	  				td.innerHTML = surname;
	  				tr.appendChild(td);
	
	  				td = document.createElement('td');
	  				td.innerHTML = email;
	  				tr.appendChild(td);
	
	  				td = document.createElement('td');
	  				td.innerHTML = userName;
	  				tr.appendChild(td);
	  				td = document.createElement('td');
					if(isInDB== 'false' && repeated== 'false' && imported== 'true'){
						td.innerHTML = '<img src=\"${pageContext.request.contextPath}/imagenes/tick.gif\" border=\"none\" width="20px" height="20px">';
						tr.appendChild(td);
		  				body.appendChild(tr);
		  				cont++;
					}else{
						var div = document.createElement('div');
						var ul = document.createElement('ul');
						var li = document.createElement('li');
						div.appendChild(ul);
						td.appendChild(div);
						if(isInDB == 'true'){
							li = document.createElement('li');
							li.innerHTML += '<fmt:message key="msgUserNameAlreadyExists"/>';
							ul.appendChild(li);
						}
						if(imported == 'false'){
							li = document.createElement('li');
							li.innerHTML += '<fmt:message key="labelFailInUserData"/>';
							ul.appendChild(li);
						}
						if(repeated == 'true'){
							li = document.createElement('li');
							li.innerHTML += '<fmt:message key="labelFileWidthRepeatedUser"/>';
							ul.appendChild(li);
						}
						tr.appendChild(td);
						bodyIncident.appendChild(tr);
						auxCont++;
					}
	  				
	  	  	  	});
	  	  	  	$('#tableFieldSet').show('slow',function(){
					document.getElementById('finalCount').innerHTML = '<hr/><br/><center><b><fmt:message key="totalLabel"/>'+cont+'</b></center>';
					document.getElementById('finalCountIncident').innerHTML = '<hr/><br/><center><b><fmt:message key="totalLabel"/>'+auxCont+'</b></center>';
					iTestUnlockPage();
	  	  	  	});
  	  	  	}
  	  	}

  	 	function errorUploading(){
  	 		alert('<fmt:message key="labelErrorUploading"/>');
  	 		iTestUnlockPage();
	  	}

	  	function MMSizeError(){
	  		alert('<fmt:message key="labelSizeError"/>');
			iTestUnlockPage();
		}

		function importStudents(){
			var cont = 0;
			$('#bodyUserTable > tr').each(function(){
				cont++;
			});
			if(cont!=0){
				if(confirm('<fmt:message key="confirmImportStudents"/>')){
					StudentListMgmt.importStudentsFromCurrentList({callback:importedStudents,
						 timeout:20000,
						 errorHandler:function(message) {iTestUnlockPage('error');} 
						});
				}
			}else{
				alert('<fmt:message key="noAvailableUsers"/>');
			}
		}

		function importedStudents(n_matriculados){
			$('#tableFieldSet').hide('slow',function(){
  	  	  	  	});
			alert(n_matriculados+" <fmt:message key="headerRegisterStudents"/>");
		}

  	</script>
  	<div id="contenido">
		<fieldset>
			<form method="post" enctype="multipart/form-data" name="formUploadFile" id="formUploadFile" action="${pageContext.request.contextPath}/tutor/fileupload.itest" target="form1_iframe" onsubmit="$('#tableFieldSet').hide('slow',function(){iTestLockPage();});">
				<label><fmt:message key="labelSelectUploadFile"/></label> <input type="file" name="file" id="inputFileSelected"/>
				<input type="hidden" name="importUserFile" value="true"/>
				<input type="submit" value="<fmt:message key="labelSend"/>">
				<br/>
				<br/>
				<a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=getCSVTemplate"/><fmt:message key="labelDownloadCSVTemplate"/></a>
				<a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=getXLSTemplate"/><fmt:message key="labelDownloadXLSTemplate"/></a>
			</form>
			<iframe name="form1_iframe" id="form1_iframe" src="blank.html" style="border: 0pt none ; padding: 0pt; height: 0pt; width: 0pt; position: absolute;"></iframe>
		</fieldset>
		<br/>
		<br/>
		<br/>
		<fieldset id="tableFieldSet" style="display:none;">
			<legend><fmt:message key="taskShowLearners"/></legend>
			<fieldset>
				<legend><fmt:message key="labelImportableUsers"/></legend>
				<div>
					<input type="button" value="<fmt:message key="taskTitleImports"/>" onclick="javascript:importStudents();"/>
				</div>
				<br/>
				<br/>
				<table class="tabladatos">
					<thead>
						<tr>
							<th><fmt:message key="personalID"/></th>
							<th><fmt:message key="name"/></th>
							<th><fmt:message key="surname"/></th>
							<th><fmt:message key="email"/></th>
							<th><fmt:message key="user"/></th>
							<th></th>
						</tr>
					</thead>
					<tbody id="bodyUserTable"></tbody>
					<tfoot>
						<tr>
							<td colspan="6" id="finalCount"></td>
						</tr>
					</tfoot>
				</table>
			</fieldset>
			<br/>
			<br/>
			<fieldset>
				<legend><fmt:message key="labelNotImportableUsers"/></legend>
				<table class="tabladatos">
					<thead>
						<tr>
							<th><fmt:message key="personalID"/></th>
							<th><fmt:message key="name"/></th>
							<th><fmt:message key="surname"/></th>
							<th><fmt:message key="email"/></th>
							<th><fmt:message key="user"/></th>
							<th width="30%"></th>
						</tr>
					</thead>
					<tbody id="bodyUserIncidentTable"></tbody>
					<tfoot>
						<tr>
							<td colspan="6" id="finalCountIncident"></td>
						</tr>
					</tfoot>
				</table>
			</fieldset>
		</fieldset>	 
	</div>

	</body>
</html>