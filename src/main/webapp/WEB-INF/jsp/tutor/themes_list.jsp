<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>
<%@ page import="com.cesfelipesegundo.itis.model.Group" %>

<% 
	Group group = (Group)request.getAttribute("group");
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addStep(group.getCourse().getName()+" ("+group.getName()+")",request.getContextPath()+"/tutor/managegroup.itest?method=indexGroup");
	breadCrumb.addBundleStep("titleThemesList","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="tutor"/>
	<jsp:param name="menu" value="tutor"/>
	<jsp:param name="mathml" value="mathml"/>
</jsp:include>


	<!-- Ajax for themes -->
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/ThemeListMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>
    
    <script type='text/javascript'>

		var groupId = <c:out value="${group.id}"/>;
		var listSize = 0;
		var aniadido = false;
		/* Ajax for the list of themes (callback function, receives the list of themes of the group) */		
		function updateThemeList(themes) {
	  		if(themes==null){
	  			iTestUnlockPage();
	  			alert("<fmt:message key="msgThemeRepeat"/>");
				return;
		  	}
			var datatable,tbodyelement, theme, rowelement, cellelementCount, cellelementText, cellelementUp, cellelementDown, cellelementEdit, cellelementDel;

			// Create the table for the list
			tbodyelement=document.createElement('tbody');
			tbodyelement.setAttribute("id","themestabletbody");
			
			// Fills the table (DOM scripting): theme data ---------------
			var position = 0;
			
		   if (themes.length == 0) {
			
				// Just one expanded cell in one row:
		   		rowelement = document.createElement('tr');
				cellelementText = document.createElement('td');
				cellelementText.innerHTML = "<fmt:message key="noAvailableThemes"/>";
				cellelementText.colSpan=6;
				cellelementText.setAttribute("align","center");
				rowelement.appendChild(cellelementText);
				// Add row
				tbodyelement.appendChild(rowelement);
				
		   } else {
		      while (position < themes.length) {
		        theme = themes[position];
		        position++;

				rowelement = document.createElement('tr');
				rowelement.setAttribute("id","row"+theme.id);
			
				// All the cells:
			
				// Order:
				cellelementCount = document.createElement('td');
				cellelementCount.innerHTML = theme.order+".";
				cellelementCount.setAttribute("width","5%");
				cellelementCount.setAttribute("align","center");
				rowelement.appendChild(cellelementCount);
									
				// Name of the theme
				cellelementText = document.createElement('td');
				cellelementText.innerHTML = theme.subject + "<div id=\"div" + theme.id + "\" style=\"display:none;\"><form id=\"formEditSubject"+theme.id+"\" action=\"javascript:saveTheme("+theme.id+")\"><input id=\"editSubject" + theme.id + "\" name=\"editSubject" + theme.id + "\" type=\"text\" size=\"50\" maxlength=\"50\" value=\"" + theme.subject + "\"/>&nbsp;&nbsp;<input name=\"Submit\" value=\"<fmt:message key="buttonSaveTheme"/>\" type=\"submit\">&nbsp;&nbsp;<input name=\"Cancel\" value=\"<fmt:message key="buttonCancelEditTheme"/>\" type=\"button\" onclick=\"javascript:cancelEditTheme("+theme.id+");\"></form></div>";
				cellelementText.innerHTML += "<div id=\"divDetailsTheme"+theme.id+"\" style=\"display:none; padding-left: 30px;\"><table class=\"tabladatos\"><tr><td align=\"left\"><b><fmt:message key="numberQuestionsPerDifficulty"/></b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td align=\"center\"><fmt:message key="diffLow"/>:<label id=\"lowQuestions"+theme.id+"\" name=\"lowQuestions"+theme.id+"\"></td><td align=\"center\">&nbsp;</td><td align=\"center\"><fmt:message key="diffMedium"/>:<label id=\"mediumQuestions"+theme.id+"\" name=\"mediumQuestions"+theme.id+"\"></td><td align=\"center\">&nbsp;</td><td align=\"center\"><fmt:message key="diffHigh"/>:<label id=\"highQuestions"+theme.id+"\" name=\"highQuestions"+theme.id+"\"></td></tr><tr><td align=\"left\"><b><fmt:message key="numberQuestionsPerDifficulty"/>(<fmt:message key="labelQuestionFill"/>)</b>      </td><td align=\"center\"><fmt:message key="diffLow"/>:<label id=\"lowFillQuestions"+theme.id+"\" name=\"lowFillQuestions"+theme.id+"\"></td><td align=\"center\">&nbsp;</td><td align=\"center\"><fmt:message key="diffMedium"/>:<label id=\"mediumFillQuestions"+theme.id+"\" name=\"mediumFillQuestions"+theme.id+"\"></td><td align=\"center\">&nbsp;</td><td align=\"center\"><fmt:message key="diffHigh"/>:<label id=\"highFillQuestions"+theme.id+"\" name=\"highFillQuestions"+theme.id+"\"></td></tr></table></div>";
				rowelement.appendChild(cellelementText);
				// Control elements:
				cellelementUp = document.createElement('td');
				cellelementUp.innerHTML = "<a href=\"javascript:changeOrderTheme("+theme.order+","+(theme.order-1)+");\"><img src=\"${pageContext.request.contextPath}/imagenes/up.gif\" alt=\"<fmt:message key="labelUpOrderTheme"/>\" border=\"none\"></a>";
				cellelementUp.setAttribute("width","5%");
				cellelementUp.setAttribute("align","center");
				rowelement.appendChild(cellelementUp);

				cellelementDown = document.createElement('td');
				cellelementDown.innerHTML = "<a href=\"javascript:changeOrderTheme("+theme.order+","+(theme.order+1)+");\"><img src=\"${pageContext.request.contextPath}/imagenes/down.gif\" alt=\"<fmt:message key="labelDownOrderTheme"/>\" border=\"none\"></a>";
				cellelementDown.setAttribute("width","5%");
				cellelementDown.setAttribute("align","center");
				rowelement.appendChild(cellelementDown);
	
				cellelementEdit = document.createElement('td');
				cellelementEdit.innerHTML = "<a href=\"javascript:editTheme("+theme.id+");\"><img src=\"${pageContext.request.contextPath}/imagenes/editar.gif\" alt=\"<fmt:message key="labelEditTheme"/>\" border=\"none\"></a>";
				cellelementEdit.setAttribute("width","5%");
				cellelementEdit.setAttribute("align","center");
				rowelement.appendChild(cellelementEdit);
			
				cellelementDel = document.createElement('td');
				if (theme.usedInExam == 1) {
					cellelementDel.innerHTML = "<img src=\"${pageContext.request.contextPath}/imagenes/forb_dot.gif\" title=\"<fmt:message key="cannotDeleteTheme"/>\" alt=\"<fmt:message key="cannotDeleteTheme"/>\" border=\"none\">";
				} else {
					cellelementDel.innerHTML = "<a href=\"javascript:deleteTheme('"+theme.id+"');\"><img src=\"${pageContext.request.contextPath}/imagenes/borrar.gif\" alt=\"<fmt:message key="labelDeleteTheme"/>\" border=\"none\"></a>";
				}
				cellelementDel.setAttribute("width","5%");
				cellelementDel.setAttribute("align","center");
				rowelement.appendChild(cellelementDel);
				
				cellelementText = document.createElement('td');
				cellelementText.innerHTML = "<img name=\"imgPlus\" id=\"plus"+theme.id+"\" title=\"<fmt:message key="buttonShowDetailsTheme"/>\" src=\"${pageContext.request.contextPath}/imagenes/mas.jpg\" onclick=\"javascript:showDetailsTheme("+theme.id+","+groupId+");\" style=\"border:none;\"/><img name=\"imgMinus\" id=\"minus"+theme.id+"\" src=\"${pageContext.request.contextPath}/imagenes/menos.jpg\" onclick=\"javascript:showDetailsTheme("+theme.id+","+groupId+");\" style=\"display: none; border:none;\"/>";
				cellelementText.setAttribute("align","center");
				rowelement.appendChild(cellelementText);
				
				// Add row
				tbodyelement.appendChild(rowelement);
				
			  } // while
			  
			} // else
		
			// Update number of "next" element
			document.getElementById("nextTheme").setAttribute("value",themes.length+1);
			
			// Replaces tbody
			datatable=document.getElementById("themestable");
			datatable.replaceChild(tbodyelement,document.getElementById("themestabletbody"));

		    // Hiding the div to avoid double-click
		    iTestUnlockPage();

		} // updateThemeList
		
		
		function changeOrderTheme(oldorder,neworder) {
		     // Change the order using Ajax (callback function)
		    // Puts a div to avoid double-click
		     iTestLockPage('');
	         ThemeListMgmt.changeOrderTheme(oldorder,neworder,{callback:updateThemeList,
					timeout:callBackTimeOut,
					errorHandler:function(message) {iTestUnlockPage('error');} });
		} // changeOrderTheme
		
		
		function addTheme() {
			// Adds a new theme into the database
			aniadido = true;
			var subject = document.getElementById("subject").value;
			var order = document.getElementById("nextTheme").value;
			// Data are valid because the database size is larger than 50 and the
			// order is set to be "the next" one.
			subject = subject.replace(/^\s*|\s*$/g,"");
			if(subject.length==0){
				alert("<fmt:message key="msgEmptyTheme"/>");
				return;
			}
		    // Puts a div to avoid double-click
		    iTestLockPage('');
		    
			// Ajax: adds theme and, then, callback to updateListThemes
			ThemeListMgmt.addTheme(subject,order,{callback:updateThemeList,
				timeout:callBackTimeOut,
				errorHandler:function(message) { recorregir = false;
					iTestUnlockPage('error');} });

			// Delete text from input
			document.getElementById("subject").value = "";
			
		} // addTheme		


		function deleteTheme(idtheme) {
			// Deletes a theme from the database

			var conf = confirm ('<fmt:message key="confirmDeleteTheme"/>\n\n<fmt:message key="alertDeleteTheme"/>');
		  
			if (conf) {
			    // Puts a div to avoid double-click
			    iTestLockPage('');
			    
				// Ajax: deletes theme and, then, callback to updateThemeList
				ThemeListMgmt.deleteTheme(idtheme,{callback:updateThemeList,
					timeout:callBackTimeOut,
					errorHandler:function(message) {iTestUnlockPage('error');} });
			}
				
		} // deleteTheme


		function editTheme(idtheme) {
			// Shows the form to edit a theme
			document.getElementById("div"+idtheme).style.display = "";
			
		} // editTheme


		function cancelEditTheme(idtheme) {
			// Hides the form to edit a theme
			document.getElementById("div"+idtheme).style.display = "none";
		
			// Restores the form
			document.getElementById("formEditSubject"+idtheme).reset();
					
		} // cancelEditTheme
		
		function fillDetails(detallesDelTema){
			//Se rellenan los campos de detalle del tema con los datos recibidos
				document.getElementById("lowQuestions"+detallesDelTema.id).innerHTML=detallesDelTema.difficultyLow;
				document.getElementById("mediumQuestions"+detallesDelTema.id).innerHTML=detallesDelTema.difficultyMedium;
				document.getElementById("highQuestions"+detallesDelTema.id).innerHTML=detallesDelTema.difficultyHigh;
				document.getElementById("lowFillQuestions"+detallesDelTema.id).innerHTML=detallesDelTema.difficultyLowFill;
				document.getElementById("mediumFillQuestions"+detallesDelTema.id).innerHTML=detallesDelTema.difficultyMediumFill;
				document.getElementById("highFillQuestions"+detallesDelTema.id).innerHTML=detallesDelTema.difficultyHighFill;
			//Se pone visible el divDetailsTheme+detallesDelTema.id 
			document.getElementById("divDetailsTheme"+detallesDelTema.id).style.display = "";
			iTestUnlockPage('');
		}
		function showAllDetailsTheme(mostrar){			
			if(mostrar){
				showAllPlus();
				document.getElementById('plusAll').style.display='none';
				document.getElementById('minusAll').style.display='';
			}else{
				showAllMinus();
				document.getElementById('plusAll').style.display='';
				document.getElementById('minusAll').style.display='none';
			}
		}

		function showAllPlus(){
			var plus = document.getElementsByName('imgPlus');
			for(var i=0;i<plus.length;i++){
				if (plus[i].style.display == "block" || plus[i].style.display == ""){
					var idtheme = plus[i].id.split("plus")[1];
					showDetailsTheme(idtheme,${group.id},true);
				}
			}
		}

		function showAllMinus(){
			var minus = document.getElementsByName('imgMinus');
			for(var i=0;i<minus.length;i++){
				if (minus[i].style.display == "block" || minus[i].style.display == ""){
					var idtheme = minus[i].id.split("minus")[1];
					showDetailsTheme(idtheme,${group.id},true);
				}
			}
		}

		
		function showDetailsTheme(idtheme,idgroup){
			//Se llama al controlador para que devuelva las preguntas que hay de cada dificultad de ese tema
			//la llamada al controlador se hace por ajax y devuelve el resultado que tratara la funcion fillDetails(detallesDelTema)
			iTestLockPage('');
			if (document.getElementById("divDetailsTheme"+idtheme).style.display == "none"){
				ThemeListMgmt.showDetailsTheme(idtheme,0,idgroup,{callback:fillDetails,
					timeout:callBackTimeOut,
					errorHandler:function(message) {alert(message);iTestUnlockPage('error');} });
				document.getElementById("plus"+idtheme).style.display = 'none';
				document.getElementById("minus"+idtheme).style.display = '';
			}
			else{
				document.getElementById("divDetailsTheme"+idtheme).style.display = "none";
				document.getElementById("plus"+idtheme).style.display = '';
				document.getElementById("minus"+idtheme).style.display = 'none';
				iTestUnlockPage();
				}
		}

		
		function saveTheme(idtheme) {
			// Modifies the subject of an existing theme
			var subject = document.getElementById("editSubject"+idtheme).value;
			
		    // Puts a div to avoid double-click
		    iTestLockPage('');
		    
			// Ajax: deletes theme and, then, callback to updateThemeList
			ThemeListMgmt.saveTheme(idtheme,subject,{callback:updateThemeList,
				timeout:callBackTimeOut,
				errorHandler:function(message) {iTestUnlockPage('error');} });
			
		} // saveTheme



	</script>
		

		<div id="contenido">
			<p><legend><fmt:message key="headerThemesList" /></legend></p><hr><br>
			
			<div>
					<table id="themestable" class="tabladatos">
					  <tr>
						<th>&nbsp;</th>
						<th><fmt:message key="headerThlistText"/></th>	
						<th>&nbsp;</th>
						<th>&nbsp;</th>
						<th>&nbsp;</th>
						<th>&nbsp;</th>
						<th>
							<center>
								<img id="plusAll" title="<fmt:message key="buttonShowDetailsTheme"/>" src="${pageContext.request.contextPath}/imagenes/mas.jpg" onclick="javascript:showAllDetailsTheme(true);" style="border:none;"/>
								<img id="minusAll" src="${pageContext.request.contextPath}/imagenes/menos.jpg" onclick="javascript:showAllDetailsTheme(false);" style="display: none; border:none;"/>
							</center>
						</th>
					  </tr>
					  <tr>
					  	<td>&nbsp;</td>
						<td>&nbsp;</td>	
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					  	
				   	  <tbody id="themestabletbody">

			  <c:choose>
				 <c:when test="${!empty themes}">			
					  <c:forEach items="${themes}" var="theme">
						<tr id="row${theme.id}">
			 			  	<td align="center" width="5%"><c:out value="${theme.order}"/>.</td>
							<td>
							   <c:out value="${theme.subject}"/>
							   <div id="div${theme.id}" style="display:none;">
							   	   <br />
							      <form id="formEditSubject${theme.id}" action="javascript:saveTheme(${theme.id});"><table class="tabladatos"><tr><td><b><fmt:message key="newThemeText"/></b></td><td><input id="editSubject${theme.id}" name="editSubject${theme.id}" type="text" size="50" maxlength="50" value="${theme.subject}"/></td><td>&nbsp;</td><td>&nbsp;</td><td><input value="<fmt:message key="buttonSaveTheme"/>" type="submit"></td><td>&nbsp;</td><td>&nbsp;</td><td><input name="Cancel" value="<fmt:message key="buttonCancelEditTheme"/>" type="button" onclick="javascript:cancelEditTheme(${theme.id});"></td></tr></table></form>
							   </div>
							   <div id="divDetailsTheme${theme.id}" style="display:none; padding-left: 30px;">
							   	  <table class="tabladatos">
							   	  <tr>
							   	 	<td align="left"><b><fmt:message key="numberQuestionsPerDifficulty"/>(<fmt:message key="labelQuestionTest"/>)</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
							   	 	<td align="center"><fmt:message key="diffLow"/>:<label id="lowQuestions${theme.id}" name="lowQuestions${theme.id}"></td>
								   	<td align="center">&nbsp;</td>
								   	<td align="center"><fmt:message key="diffMedium"/>:<label id="mediumQuestions${theme.id}" name="mediumQuestions${theme.id}"></td>
								   	<td align="center">&nbsp;</td>
								   	<td align="center"><fmt:message key="diffHigh"/>:<label id="highQuestions${theme.id}" name="highQuestions${theme.id}"></td>
							   	  </tr>
							   	  <tr>
							   	 	<td align="left"><b><fmt:message key="numberQuestionsPerDifficulty"/>(<fmt:message key="labelQuestionFill"/>)</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
							   	 	<td align="center"><fmt:message key="diffLow"/>:<label id="lowFillQuestions${theme.id}" name="lowFillQuestions${theme.id}"></td>
								   	<td align="center">&nbsp;</td>
								   	<td align="center"><fmt:message key="diffMedium"/>:<label id="mediumFillQuestions${theme.id}" name="mediumFillQuestions${theme.id}"></td>
								   	<td align="center">&nbsp;</td>
								   	<td align="center"><fmt:message key="diffHigh"/>:<label id="highFillQuestions${theme.id}" name="highFillQuestions${theme.id}"></td>
							   	  </tr>
							   	  </table>
							   </div>
							</td>
							<td align="center" width="5%"><a href="javascript:changeOrderTheme(${theme.order},${theme.order-1});"><img src="${pageContext.request.contextPath}/imagenes/up.gif" alt="<fmt:message key="labelUpOrderTheme"/>" border="none"></a></td>
							<td align="center" width="5%"><a href="javascript:changeOrderTheme(${theme.order},${theme.order+1});"><img src="${pageContext.request.contextPath}/imagenes/down.gif" alt="<fmt:message key="labelDownOrderTheme"/>" border="none"></a></td>
							<td align="center" width="5%"><a href="javascript:editTheme(${theme.id});"><img src="${pageContext.request.contextPath}/imagenes/editar.gif" alt="<fmt:message key="labelEditTheme"/>" border="none"></a></td>
							<td align="center" width="5%">
							<c:choose>
								<c:when test="${theme.usedInExam eq 1}">
									<img src="${pageContext.request.contextPath}/imagenes/forb_dot.gif" title="<fmt:message key="cannotDeleteTheme"/>" alt="<fmt:message key="cannotDeleteTheme"/>" border="none">
								</c:when>
								<c:otherwise>
									<a href="javascript:deleteTheme('${theme.id}');"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="labelDeleteTheme"/>" border="none"></a>
								</c:otherwise>
							</c:choose>
							</td>
							<td align="center">
							   <img id="plus${theme.id}" name="imgPlus" title="<fmt:message key="buttonShowDetailsTheme"/>" src="${pageContext.request.contextPath}/imagenes/mas.jpg" onclick="javascript:showDetailsTheme(${theme.id},${group.id});" style="border:none;"/>
							   <img id="minus${theme.id}" name="imgMinus" src="${pageContext.request.contextPath}/imagenes/menos.jpg" onclick="javascript:showDetailsTheme(${theme.id},${group.id});" style="display: none; border:none;"/>
							</td>
						</tr>
					  </c:forEach>
					  
				 </c:when>
				 <c:otherwise>
				 
					<td align="center" colspan="6"><fmt:message key="noAvailableThemes"/></td>
					
				 </c:otherwise>
			 </c:choose>
			 
					  </tbody>
					</table>
			 
			</div>
			<hr>
			<form id="newTheme" action="javascript:addTheme();">
			   <p><fmt:message key="labelNewTheme" />&nbsp;<input id="subject" name="subject" type="text" size="50" maxlength="50"/><input name="Submit" value="<fmt:message key="buttonInclude"/>" type="submit"></p>
			   <input id="nextTheme" value="${fn:length(themes) + 1}" type="hidden"/>
			</form>
			<script type="text/javascript">
				listSize = document.getElementById('nextTheme').value - 1;
			</script>
			<hr>
		</div>
	</body>
</html>