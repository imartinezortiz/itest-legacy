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
	breadCrumb.addStep("100 últimas conexiones",request.getContextPath()+"/admin/admin.itest?method=goStatsByCenter");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<!-- JavaScript Calendar -->
  	<script type='text/javascript' src='${pageContext.request.contextPath}/common/resources/scw.js'></script>
  	
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>
	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/AdminMgmt.js'></script>
	<script type='text/javascript' src='${pageContext.request.contextPath}/common/resources/dates.js'></script>
	
	<script type='text/javascript'>
	

		var reverse = false;
		function masInformacion(id,userName,surname,name,role,email,persId){
			document.getElementById('userName').innerHTML = userName;
			document.getElementById('surname').innerHTML = surname;
			document.getElementById('name').innerHTML = name;
			document.getElementById('role').innerHTML = role;
			document.getElementById('email').innerHTML = email;
			document.getElementById('persId').innerHTML = persId;

			$('#divMoreInfo').show('slow',function(){});
		}
		function orderTable(orderBy){
			iTestLockPage();
			reverse = !reverse;
			AdminMgmt.order100LastConectionsBy(orderBy,reverse,{callback:updateTable,
				 timeout:callBackTimeOut,
				 errorHandler:function(message) { iTestUnlockPage('error');} });
		}

		function updateTable(list){
			var tbodyelement = document.createElement('tbody');
			tbodyelement.setAttribute("id","conectionsTableBody");
			for(var i=0;i<list.length;i++){
				var conection = list[i];
				var rowelement = document.createElement('tr');
				var cellelement = document.createElement('td');
				cellelement.innerHTML = conection.id;
				rowelement.appendChild(cellelement);

				cellelement = document.createElement('td');
				cellelement.innerHTML = conection.user.userName;
				rowelement.appendChild(cellelement);

				cellelement = document.createElement('td');
				cellelement.innerHTML = formatDate(conection.date,"d/MM/yy H:mm");
				rowelement.appendChild(cellelement);

				cellelement = document.createElement('td');
				cellelement.innerHTML = conection.ip;
				rowelement.appendChild(cellelement);
				
				cellelement = document.createElement('td');
				cellelement.innerHTML = '<a href="#" onclick="javascript:masInformacion('+conection.user.id+',\''+conection.user.userName+'\',\''+conection.user.surname+'\',\''+conection.user.name+'\',\''+conection.user.role+'\',\''+conection.user.email+'\',\''+conection.user.persId+'\');" ><img src="${pageContext.request.contextPath}/imagenes/mas.jpg" title="<fmt:message key="showDetails"/>" border="none"></a>';
				rowelement.appendChild(cellelement);
				
				tbodyelement.appendChild(rowelement);
			}
			if (list.length == 0) {
				if(list!=''){
			       rowelement = document.createElement('tr');
			       cellelement = document.createElement('td');
				   cellelement.colSpan=5;
			       cellelement.setAttribute("align","center");
				   cellelement.innerHTML = "<fmt:message key="noAvailableConections"/>";
				   rowelement.appendChild(cellelement);
				   tbodyelement.appendChild(rowelement);
				}
			}else{
			   rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=5;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML ="<hr/>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=5;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML ="<b><fmt:message key="totalLabel"/> "+list.length+"</b>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
			}
			var datatable=document.getElementById("searchConectionsTable");
			// Replaces tbody			
			datatable.replaceChild(tbodyelement,document.getElementById("conectionsTableBody"));
			iTestUnlockPage();
		}

		function runFilterAndSearch(){
			var idConection = document.getElementById('idConection').value;
			var userNameConection = document.getElementById('userNameConection').value;
			var dateConection1 = document.getElementById('startDate').value;
			var dateConection2 = document.getElementById('endDate').value;
			idConection = idConection.replace(/^\s*|\s*$/g,"");
			var num = parseInt(idConection,10);
			if(!isNaN(num) || idConection==''){
				if(isNaN(num)){
					iTestLockPage();
					AdminMgmt.runFilterAndSearch100Conections(idConection,userNameConection,dateConection1,dateConection2,{callback:updateTable,
						 timeout:callBackTimeOut,
						 errorHandler:function(message) { iTestUnlockPage('error');} });
				}else{
					iTestLockPage();
					AdminMgmt.runFilterAndSearch100Conections(num,userNameConection,dateConection1,dateConection2,{callback:updateTable,
						 timeout:callBackTimeOut,
						 errorHandler:function(message) { iTestUnlockPage('error');} });
				}
			}else{
				alert('id no válida');
			}
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
				<label>
				<table>
					<tr>
						<td><fmt:message key="headerQlistId"/> :</label></td>
						<td><input id="idConection" type="text"></td>
					</tr>
					<tr>
						<td><fmt:message key="headerStListUserName"/> :</label></td>
						<td><input id="userNameConection" type="text"></td>
					</tr>
				</table>
			</div>
			<div class="divDosDer">
				<table>
					<tr>
						<td><fmt:message key="labelDate"/> <fmt:message key="labelGreaterOrEquals"/>:</label></td>
						<td><input id="startDate" name="textfechafin" value="" size="8" readonly maxlength="8" type="text"/><img src="${pageContext.request.contextPath}/imagenes/scw.gif" onclick="javascript:scwShow(getElementById('startDate'),this);"/>&nbsp;&nbsp;&nbsp;</td>
					</tr>
					<tr>
						<td><fmt:message key="labelDate"/> <fmt:message key="labellessOrEquals"/>:</label></td>
						<td><input id="endDate" name="textfechafin" value="" size="8" readonly maxlength="8" type="text"/><img src="${pageContext.request.contextPath}/imagenes/scw.gif" onclick="javascript:scwShow(getElementById('endDate'),this);"/>&nbsp;&nbsp;&nbsp;</td>
					</tr>
				</table>
			</div>
			<div class="divcentro">
				<input type="reset" value="<fmt:message key="msgbuttonReset"/>" onclick="javascript:updateTable('');"/>
				<input type="button" value="<fmt:message key="buttonFilterRun"/>" onclick="javascript:runFilterAndSearch();">
			</div>
		</form>
	</fieldset>
	<br/>
	<br/>
	<table id="searchConectionsTable" class="tablaDatos">
		<thead>
			<tr>
				<th colspan="5"><center><fmt:message key="taskShow100LastConections"/></center></th>
			</tr>
			<tr>
				<th style="width:5%"><a href="javascript:orderTable('id');"><fmt:message key="headerQlistId"/></a></th>
				<th style="width:30%"><a href="javascript:orderTable('userName');"><fmt:message key="headerStListUserName"/></a></th>
				<th style="width:30%"><a href="javascript:orderTable('date');"><fmt:message key="labelDate"/></a></th>
				<th style="width:30%"><a href="javascript:orderTable('ip');"><fmt:message key="headerLabelIp"/></a></th>
				<th style="width:5%"></th>
			</tr>
		</thead>
		<tbody id="conectionsTableBody">
		<c:set var="numConectionsValue" value="0"></c:set>
			<c:forEach items="${conections}" var="conection" varStatus="numConections">
				<tr>
					<td>${conection.id}</td>
					<td>${conection.user.userName}</td>
					<td><fmt_rt:formatDate value="${conection.date}" type="both" dateStyle="short" timeStyle="short"/></td>
					<td>${conection.ip}</td>
					<td><a href="#" onclick="javascript:masInformacion('${conection.user.id}','${conection.user.userName}','${conection.user.surname}','${conection.user.name}','${conection.user.role}','${conection.user.email}','${conection.user.persId}');" ><img src="${pageContext.request.contextPath}/imagenes/mas.jpg" title="<fmt:message key="showDetails"/>" border="none"></a></td>
				</tr>
				<c:set var="numConectionsValue" value="${numConectionsValue+1}"></c:set>
			</c:forEach>
			<tr>
				<td align="center" colspan="5"><hr/></td>
			</tr>
			<tr>
				<td align="center" colspan="5">
					<b><fmt:message key="totalLabel"/> <c:out value="${numConectionsValue}"/> </b>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<div id="divMoreInfo" class="floatingDiv" style="display:none">
		<div class="floatingDivBody" style="overflow-y: scroll">
		<div align="right" style="position: fixed; background-color:white"><a href="" align="right" onclick="$('#divMoreInfo').hide('slow',function(){}); return false;"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif"></a></div>
			<br/>
			<div id="divUserInfo">
				<b><label><fmt:message key="textLoginUser"/></label></b>&nbsp;<label id="userName"></label>
			</div>
			<br/>
			<div id="divAction" style="background-color:#FFECD9"><br/></div>
			<br/>
			<hr/>
			<div id="groupList" align="center">
				<b><label><fmt:message key="surname"/>: </label></b><label id="surname"></label><br/>
				<b><label><fmt:message key="name"/>: </label></b><label id="name"></label><br/>
				<b><label><fmt:message key="role"/>: </label></b><label id="role"></label><br/>
				<b><label><fmt:message key="email"/>: </label></b><label id="email"></label><br/>
				<b><label><fmt:message key="personalID"/>: </label></b><label id="persId"></label><br/>
			</div>
			<hr/>
		</div>
	</div>
</body>


</html>