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
	breadCrumb.addStep("Busqueda de usuarios",request.getContextPath()+"/admin/admin.itest?method=searchUsers");
	request.setAttribute("breadCrumb",breadCrumb);
%>

	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminMgmt.js'></script>
	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminGroupMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>
	<script type='text/javascript' src='${pageContext.request.contextPath}/common/resources/dates.js'></script>
	

<script type="text/javascript">
	

	function showInfo(){
		var name = document.getElementById('name').value;
		var apes = document.getElementById('surname').value;
		var user = document.getElementById('userName').value;
		var tipo = document.getElementById('tipo').value;
		iTestLockPage();
		AdminMgmt.getSearchUsersFiltered(name,apes,user,tipo,{callback:updateTable,
			 timeout:callBackTimeOut,
			 errorHandler:function(message) { iTestUnlockPage('error'); alert(message);} });
	}
	
	
	var reverse = true;
	function orderTable(orderby){
		reverse = !reverse;
		iTestLockPage();
		AdminMgmt.orderUserList(orderby,reverse,{callback:updateTable,
			 timeout:callBackTimeOut,
			 errorHandler:function(message) { iTestUnlockPage('error');} });
	}
	
	function updateTable(list){
		iTestUnlockPage();
		var tbodyelement = document.createElement('tbody');
		tbodyelement.setAttribute("id","searchUsersTableBody");
		for(var i=0;i<list.length;i++){
			var user = list[i];
			var rowelement = document.createElement('tr');
			var cellelement = document.createElement('td');
			cellelement.innerHTML = user.id;
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.setAttribute("id","nombre"+user.id);
			cellelement.innerHTML = user.name;
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.setAttribute("id","apes"+user.id);
			cellelement.innerHTML = user.surname;
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.innerHTML = user.userName;
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.setAttribute("id","role"+user.id);
			cellelement.innerHTML = user.role;
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.innerHTML = user.persId;
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.innerHTML = user.email;
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.innerHTML ='<a href="" onclick="return masInformacion('+user.id+')"><img src="${pageContext.request.contextPath}/imagenes/mas.jpg" border="none" title="<fmt:message key="buttonShowGroups"/>"></a>';
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.innerHTML ='<a href="" onclick="return ultimasConexiones('+user.id+')"><img src="${pageContext.request.contextPath}/imagenes/resultados.png" border="none" title="<fmt:message key="label5LastConnections"/>"></a>';
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.innerHTML ='<a href="" onclick="return editUser('+user.id+')"><img src="${pageContext.request.contextPath}/imagenes/editar.gif" border="none" title="<fmt:message key="labelEditUser"/>"></a>';
			rowelement.appendChild(cellelement);

			cellelement = document.createElement('td');
			cellelement.innerHTML ='<a href="" onclick="return deleteUser('+user.id+')"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif" border="none" title="<fmt:message key="labelDeleteUser"/>"></a>';
			rowelement.appendChild(cellelement);
			
			tbodyelement.appendChild(rowelement);
			
		}
		if (list.length == 0) {
			if(list!=''){
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
			   cellelement.colSpan=11;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML = "<fmt:message key="noAvailableConfigExams"/>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
			}
		    } else {
			   rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=11;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML ="<hr/>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=11;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML ="<b><fmt:message key="totalLabel"/> "+list.length+"</b>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		    }
		var datatable=document.getElementById("searchUsersTable");
		// Replaces tbody			
		datatable.replaceChild(tbodyelement,document.getElementById("searchUsersTableBody"));
	}


	function masInformacion(id){
		var nombre = document.getElementById("nombre"+id).innerHTML;
		var apes = document.getElementById('apes'+id).innerHTML;
		var role = document.getElementById('role'+id).innerHTML;
		document.getElementById('divUserInfo').innerHTML = '<b><fmt:message key="name"/>: </b>'+nombre+
															'<br/><b><fmt:message key="surname"/>: </b>'+apes+
															'<br/><b><fmt:message key="role"/>: </b>'+role;
															
		if(role == 'TUTOR' || role == 'TUTORAV'){
			document.getElementById('divAction').innerHTML = '<hr /><b><fmt:message key="labelTaught"/>:</b><hr />';
		}else if(role == 'ADMIN'){
			document.getElementById('divAction').innerHTML = '<hr /><b>ADMIN</b><hr />';
		}else{
			document.getElementById('divAction').innerHTML = '<hr /><b><fmt:message key="labelRegistered"/>:</b><hr />';
		}
			 AdminGroupMgmt.getUserInfoGroups(id,{callback:mostrarInformacion,
			 timeout:callBackTimeOut,
			 errorHandler:function(message) { iTestUnlockPage('error'); return false;} });

		return false;
	}
	function mostrarInformacion(list){

		document.getElementById('groupList').innerHTML = '';
		var ulList = document.createElement('ul');
		for(var i=0;i<list.length;i++){
			var group = list[i];
			var listelement = document.createElement('li');
			var p = document.createElement('label');
			p.innerHTML = '<b><fmt:message key="labelFilterInstitution"/> </b>'+group.institution.name;
			listelement.appendChild(p);
			listelement.appendChild(document.createElement('br'));
			
			p = document.createElement('label');
			p.innerHTML = '<b><fmt:message key="scopeCourse"/> : </b>'+group.course.name;
			listelement.appendChild(p);
			listelement.appendChild(document.createElement('br'));

			p = document.createElement('label');
			p.innerHTML = '<b><fmt:message key="scopeGroup"/> : </b>'+group.name;
			listelement.appendChild(p);
			listelement.appendChild(document.createElement('br'));

			p = document.createElement('label');
			p.innerHTML = '<b><fmt:message key="academicYear"/> : </b>'+group.year;
			listelement.appendChild(p);
			listelement.appendChild(document.createElement('br'));

			
			ulList.appendChild(listelement);
			ulList.innerHTML = ulList.innerHTML +"<hr/>";
		}
		$('#divMoreInfo').show('slow',function(){});
		document.getElementById('groupList').appendChild(ulList);
		return false;
	}

	function ultimasConexiones(id){
		
		var headDiv = document.getElementById('divUserInfo2');
		headDiv.innerHTML='';
		var name = document.getElementById('nombre'+id).innerHTML;
		var surname = document.getElementById('apes'+id).innerHTML;
		var type = document.getElementById('role'+id).innerHTML;
		var label = document.createElement('label');
		label.innerHTML = '<b><fmt:message key="name"/> : </b>'+name;
		headDiv.appendChild(label);
		headDiv.appendChild(document.createElement('br'));
		label = document.createElement('label');
		label.innerHTML = '<b><fmt:message key="surname"/> : </b>'+surname;
		headDiv.appendChild(label);
		headDiv.appendChild(document.createElement('br'));
		label = document.createElement('label');
		label.innerHTML = '<b><fmt:message key="role"/> : </b>'+type;
		headDiv.appendChild(label);
		AdminMgmt.show5LastConections(id,{callback:mostrarUltimasConexiones,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error'); alert(message);return false;} });
		return false;
	}

	function mostrarUltimasConexiones(list){
		$('#divMoreInfo2').show('slow',function(){});
		var conectionsDiv = document.getElementById('groupList2');
		conectionsDiv.innerHTML='';
		var ol = document.createElement('ol');
			
			
		for(var i=0;i<list.length;i++){
			var conection = list[i];
			var li = document.createElement('li');
			li.innerHTML = formatDate(conection.date,"d/MM/yy H:mm") +" : "+conection.ip;
			ol.appendChild(li);
		}
		if(list.length==0){
			var label = document.createElement('td');
			label.innerHTML='0 ';
			conectionsDiv.appendChild(label);
		}else{
			conectionsDiv.appendChild(ol);
			var labelTotal = document.createElement('b');
			labelTotal.innerHTML = '<fmt:message key="totalLabel"/> '+list.length;
			conectionsDiv.appendChild(labelTotal);
		}
		
		conectionsDiv.setAttribute("align","center");;
	}

	function showUsersNotVinculated(){
		iTestLockPage();
		AdminMgmt.showUsersNotVinculated({callback:updateTable,
			 timeout:callBackTimeOut,
			 errorHandler:function(message) { iTestUnlockPage('error'); alert(message);} });
	}


	function deleteUser(idUser){
		if(confirm("<fmt:message key="confirmDeleteUser"/>"+idUser+"?")){
			iTestLockPage();
			AdminMgmt.deleteUserById(idUser,{callback:updateTable,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error'); alert(message);} });
		}
		
		return false;
	}

	function editUser(idUser){
		var form = document.getElementById('editUserForm');
		document.getElementById('iduser').value=idUser;
		form.submit();
		return false;
	}
</script>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="admin"/>
	<jsp:param name="menu" value="admin"/>
</jsp:include>
		<div id="contenido">
			<fieldset>
				<legend><fmt:message key="labelFilterTitle"/></legend>
				<form action="return false;">
				<div class="divDosIzq">
				<table>
					<tr>
						<td><fmt:message key="name"/> : </td>
						<td><input id="name" type="text" name="newUserName"/></td>
					</tr>
					<tr>
						<td><fmt:message key="headerStListRole"/> : </td>
						<td>
							<select id="tipo">
								<option></option>
								<option value="TUTORAV"><fmt:message key="tutorAvRoleOption"/></option>
								<option value="TUTOR"><fmt:message key="tutorRoleOption"/></option>
								<option value="LEARNER"><fmt:message key="learnerRoleOption"/></option>
								<option value="KID"><fmt:message key="kidRoleOption"/></option>
							</select>
						</td>
					</tr>
				</table>
					
				</div>
				<div class="divDosDer">
					<table>
						<tr>
							<td><fmt:message key="surname"/> :</td>
							<td><input id="surname" type="text" name="newUserName"/></td>
						</tr>
						<tr>
							<td><fmt:message key="user"/> :</td>
							<td><input id="userName" type="text" name="newUserName"/></td>
						</tr>
					</table>
				</div>
				
				<div class="divcentro">
					<input type="reset" value="<fmt:message key="msgbuttonReset"/>" onclick="javascript:updateTable('');"/>
					<input type="button" value="<fmt:message key="buttonFilterRun"/>" onclick="javascript:showInfo();">
					<input type="button" value="<fmt:message key="buttonNotVinculatedUsers"/>" onclick="javascript:showUsersNotVinculated();">
				</div>
				</form>
			</fieldset>
		
			<table id="searchUsersTable" class="tablaDatos">
				<tr>
		     	<th colspan="12"><center><fmt:message key="textUsersList"/></center></th>
		    </tr>
     		<tr>
     			<th><a href="javascript:orderTable('id');"><fmt:message key="headerQlistId"/></a></th>
     			<th><a href="javascript:orderTable('name');"><fmt:message key="name"/></a></th>
     			<th><a href="javascript:orderTable('surname');"><fmt:message key="surname"/></a></th>
     			<th><a href="javascript:orderTable('username');"><fmt:message key="user"/></a></th>
     			<th><a href="javascript:orderTable('role');"><fmt:message key="role"/></a></th>
     			<th><a href="javascript:orderTable('persid');"><fmt:message key="personalID"/></a></th>
     			<th><a href="javascript:orderTable('email');"><fmt:message key="email"/></a></th>
     			<th></th>
     			<th></th>
     			<th></th>
     			<th></th>
     			<th></th>
     		</tr>
     		<tbody id="searchUsersTableBody">
     		</tbody>
			</table>
		
		</div>
		<form id="editUserForm" action="admin.itest?method=editUser" method="post">
			<input type="hidden" name="iduser" id="iduser">
		</form>
	</body>
	<div id="divMoreInfo" class="floatingDiv" style="display:none">
		<div class="floatingDivBody" style="overflow-y: scroll">
		<div align="right" style="position: fixed; background-color:white"><a href="" align="right" onclick="$('#divMoreInfo').hide('slow',function(){}); return false;"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif"></a></div>
			<div id="divUserInfo">
				
			</div>
			<br/>
			<div id="divAction" style="background-color:#FFECD9"></div>
			<br/>
			<div id="groupList" align="left">
			
			</div>
		</div>
	</div>
	<div id="divMoreInfo2" class="floatingDiv" style="display:none">
		<div class="floatingDivBody" style="overflow-y: scroll">
		<div align="right" style="position: fixed; background-color:white"><a href="" align="right" onclick="$('#divMoreInfo2').hide('slow',function(){}); return false;"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif"></a></div>
			<div id="divUserInfo2">
				
			</div>
			<br/>
			<div id="divAction2" style="background-color:#FFECD9"><fmt:message key="label5LastConnections"/></div>
			<br/>
			<div id="groupList2" align="left">
				Conexiones...
			</div>
		</div>
	</div>
</html>