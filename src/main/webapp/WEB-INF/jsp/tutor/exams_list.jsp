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
	//breadCrumb.addBundleStep("tasksMain",request.getContextPath()+"/tutor/managegroup.itest?method=indexGroup");
	breadCrumb.addBundleStep("configExamList","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="tutor"/>
	<jsp:param name="menu" value="tutor"/>
</jsp:include>

	<!-- Ajax for question list -->
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/TutorConfigExamListMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>
  	
  	<!--  Script for formatting dates, taken from http://www.mattkruse.co -->
  	<script type='text/javascript' src='${pageContext.request.contextPath}/common/resources/dates.js'></script>

    <script type="text/javascript">
		    
		// Updates the list of exam configurations after deleting or sorting them:
		function updateExamList(exams) {
		  /*
		     This is a callback function that updates the information about the answers already saved (answerstable)
		     with the answer recently just saved.
		  */
  		  var rowelement, tbodyelement, exam, cellelement, formelement, inputelement;

		   tbodyelement=document.createElement('tbody');
		   tbodyelement.setAttribute("id","examstabletbody");

		   // Fills the table (DOM scripting): answers data ---------------
		   var position = 0;
		   var ahora = new Date();
		   
		   while (position < exams.length) {
		        exam = exams[position];
		        
				rowelement = document.createElement('tr');
								
				// Checkbox
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				checkboxelement = document.createElement('input');
				checkboxelement.setAttribute("type","checkbox");
				checkboxelement.setAttribute("id","check"+exam.id);
				checkboxelement.setAttribute("name","examCheckbox");
				checkboxelement.setAttribute("value",exam.id);
				cellelement.appendChild(checkboxelement);
				rowelement.appendChild(cellelement);
				
				// Title
				cellelement = document.createElement('td');
				cellelement.id='examTittle'+exam.id;
				cellelement.innerHTML = exam.title;
				rowelement.appendChild(cellelement);
				
				// Duration
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				cellelement.innerHTML = exam.duration;
				rowelement.appendChild(cellelement);

				// Start Date
				var sDate = new Date(exam.startDate);
				
				// End Date
				var eDate = new Date(exam.endDate);
				var ahora = new Date();

				// Exam active or not
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				if(exam.visibility == 1){
					cellelement.innerHTML=formatDate(sDate,"d/MM/yy H:mm")+' - '+formatDate(eDate,"d/MM/yy H:mm")
					if(sDate<=ahora && eDate>=ahora){
						cellelement.style.backgroundColor="#BEF781";
					}else{
						cellelement.style.backgroundColor="#FF3800";
					}
				}else{
					cellelement.innerHTML="<a><img src=\"${pageContext.request.contextPath}/imagenes/red_dot.gif\" alt=\"<fmt:message key="notActive"/>\" title=\"<fmt:message key="notActive"/>\" border=\"none\"></a>";
				}
				rowelement.appendChild(cellelement);

								
				// Revision active or not
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				if (exam.activeReview) {
					cellelement.innerHTML=formatDate(exam.startDateRevision,"d/MM/yy H:mm")+' - '+formatDate(exam.endDateRevision,"d/MM/yy H:mm")
					if(exam.startDateRevision<=ahora && exam.endDateRevision>=ahora){
						cellelement.style.backgroundColor="#BEF781";
					}else{
						cellelement.style.backgroundColor="#FF3800";
					}
				} else {
					cellelement.innerHTML = "<a><img src=\"${pageContext.request.contextPath}/imagenes/red_dot.gif\" alt=\"<fmt:message key="notActive"/>\" title=\"<fmt:message key="notActive"/>\" border=\"none\"></a>";	
				}
				rowelement.appendChild(cellelement);	

				
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				if(exam.published == true){
					cellelement.innerHTML = '<a><img src="${pageContext.request.contextPath}/imagenes/visible.gif" border="none" width="20px" height="20px" alt=\"<fmt:message key="active"/>\" title=\"<fmt:message key="active"/>\"></a>'
				}else{
					cellelement.innerHTML = '<a><img src="${pageContext.request.contextPath}/imagenes/invisible.gif" border="none" width="20px" height="20px" alt=\"<fmt:message key="notactive"/>\" title=\"<fmt:message key="notactive"/>\"></a>'
				}
				rowelement.appendChild(cellelement);	

				<c:if test="${user.role eq 'TUTORAV'}">
					cellelement = document.createElement('td');
					cellelement.setAttribute("align","center");
					if(exam.customized){
						cellelement.innerHTML = "<a><img width=\"20px\" height=\"20px\" src=\"${pageContext.request.contextPath}/imagenes/customized.gif\"  border=\"none\" alt=\"<fmt:message key="labelCustomized"/>\"> title=\"<fmt:message key="labelCustomized"/>\"></a>";
					}else{
						cellelement.innerHTML = "";	
					}
					rowelement.appendChild(cellelement);
				</c:if>
				
				// Control element: edit exam
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				cellelement.innerHTML = "<a href=\"javascript:document.getElementById('editCE"+exam.id+"').submit();\"><img src=\"${pageContext.request.contextPath}/imagenes/editar.gif\" alt=\"<fmt:message key="labelEditConfigExam"/>\" title=\"<fmt:message key="labelEditConfigExam"/>\" border=\"none\"></a>";
				rowelement.appendChild(cellelement);
				
				// Control element: delete exam configuration
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				cellelement.innerHTML = "<a href=\"javascript:deleteConfigExam('"+exam.id+"');\"><img src=\"${pageContext.request.contextPath}/imagenes/borrar.gif\" alt=\"<fmt:message key="labelDeleteConfigExam"/>\" title=\"<fmt:message key="labelDeleteConfigExam"/>\" border=\"none\"></a>";
				rowelement.appendChild(cellelement);

				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				
				rowelement.appendChild(cellelement);
				
				// Add row
				tbodyelement.appendChild(rowelement);

				// Important to add the form to the control operations:
				formelement = document.createElement('form');
				formelement.setAttribute("id","editCE"+exam.id);
				formelement.setAttribute("method","POST");
				formelement.setAttribute("action","${pageContext.request.contextPath}/tutor/managegroup.itest?method=editConfigExam");
				inputelement = document.createElement('input');
				inputelement.setAttribute("name","idconfigexam");
				inputelement.setAttribute("value",exam.id);
				inputelement.setAttribute("type","hidden");
				formelement.appendChild(inputelement);
				tbodyelement.appendChild(formelement);
				
				position++;
			} // while
			
			// No exams: present message
		    if (exams.length == 0) {
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
			   cellelement.colSpan=9;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML = "<fmt:message key="noAvailableConfigExams"/>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		    } else {
			   rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=9;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML ="<hr/>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=9;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML ="<b><fmt:message key="totalLabel"/> "+exams.length+"</b>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		    }
		
		    // Gets the datatable
		    datatable=document.getElementById("examstable");
			// Replaces tbody			
			datatable.replaceChild(tbodyelement,document.getElementById("examstabletbody"));
			iTestUnlockPage();
		} // updateExamList
		
		reverse = false;
	    // Function that, given some order, asks for the exam configurations ordered by that criteria
	    function sort(orderby) {	
	    	iTestLockPage();         
		     // Obtains the list of exam cofigurations sorted by orderby (callback updateExamList)
	         TutorConfigExamListMgmt.sort(${group.id},orderby,reverse,updateExamList);
	         reverse = !reverse;
	    } // sort

		
	    // Function that changes the activity of the exam
	    function changeReviewExam(idconfigexam,value) {
	    	iTestLockPage();
		     // Change the activity using Ajax (callback updateExamList)
	         TutorConfigExamListMgmt.changeReviewExam(idconfigexam,value,updateExamList);
	    } // changeReviewExam


		// Using Ajax, deletes the exam
		function deleteConfigExam(idexam) {
		  var tittle = document.getElementById('examTittle'+idexam).innerHTML;
          var conf = confirm ('<fmt:message key="confirmDeleteExam"/>: '+tittle+'?\n\n<fmt:message key="warningDeleteExam"/>\n');
		  
		  if (conf) {
		     // Delete the exam using Ajax (callback updateExamList)
		     iTestLockPage();
	         TutorConfigExamListMgmt.deleteConfigExam(idexam,updateExamList);
	      }
		   
		} // deleteConfigExam
		
		// Deletes all checked exams using AJAX
		function deleteSelectedConfigExams() {			
	    	var exams = selectedConfigExams();
	    	if (exams.length > 0) {
	    		var conf = confirm ('<fmt:message key="confirmDeleteExams"/>\n\n<fmt:message key="warningDeleteExams"/>\n');
	    		if (conf) {
	    			iTestLockPage();
			        TutorConfigExamListMgmt.deleteConfigExams(exams,updateExamList);
			    }
			}
		}	

		function setVisible(idexam,value){
			iTestLockPage();
			TutorConfigExamListMgmt.setExamVisible(idexam,value,iTestUnlockPage);
			
			if(value == 0){
				document.getElementById('EVis'+idexam).style.display = 'none';
				document.getElementById('ENVis'+idexam).style.display = 'block';
			}else{
				document.getElementById('EVis'+idexam).style.display = 'block';
				document.getElementById('ENVis'+idexam).style.display = 'none';
			}
		}
		    
		function switchAllCheckboxes() { 
			var mytable = document.getElementById("examstable");
			if(!mytable)
				return;
			var inputs = mytable.getElementsByTagName("input");
			if(!inputs)
				return;
			// switch the check value for all check boxes
			for(var i = 0; i < inputs.length; i++)
				if (inputs[i].type == 'checkbox')
					inputs[i].checked = !inputs[i].checked;
		}
		
		// Returns an array with the IDs of the checked questions
		function selectedConfigExams() {
			var resultList = new Array();
			var mytable = document.getElementById("examstable");
			if(!mytable)
				return resultList;
			var inputs = mytable.getElementsByTagName("input");
			if(!inputs)
				return resultList;
			// switch the check value for all check boxes
			for(var i = 0; i < inputs.length; i++)
				if (inputs[i].type == 'checkbox' && inputs[i].checked)
					resultList.push(new String(inputs[i].value));
					
			return resultList;
		}
		    	    
	</script>

	<div id="contenido">
		
			<div class="botonera">
				<form>
				<fieldset>
				<table width="100%">
				<col width="20%" span="5"/>
				<tr>
					<td style="text-align: left"><input type="button" value="<fmt:message key="newExam"/>" onclick="document.location = 'managegroup.itest?method=newExam'"/></td>
					<td colspan="2"></td>
					<td><input type="button" value="<fmt:message key="switchAll"/>" onclick="switchAllCheckboxes()"/></td>
					<td><input type="button" value="<fmt:message key="deleteSelected"/>" onclick="deleteSelectedConfigExams()"/></td>
				</tr>
				</table>
				</fieldset>
				</form>
			</div>
			
	
			<div>
			<table id="examstable" class="tabladatos">
			<col width="3%">
			<col width="20%">
			<col width="5%">
			<col width="35%">
			<col width="35%">
			<col width="3%">
			<col width="3%">
			<col width="3%">
			<col width="3%">
			  <tr>
			  	<th></th>
				<th><a href="javascript:sort('title');"><fmt:message key="headerCElistTitle"/></a></th>
				<th><center><a href="javascript:sort('min');"><fmt:message key="headerCElistDuration"/></a></center></th>
				<th><center><a href="javascript:sort('sdate');"><fmt:message key="headerCElistActive"/></a></center></th>
				<th><center><a href="javascript:sort('revsdate');"><fmt:message key="headerCElistReview"/></a></center></th>
				<th><center><fmt:message key="labelPublish"/></center></th>
				<c:if test="${user.role eq 'TUTORAV'}">
					<th>&nbsp;</th>
				</c:if>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
			  </tr>
			 <tbody id="examstabletbody">
			
 			 <jsp:useBean id="ahora" class="java.util.Date" />

			 
			 
			 
  			 <c:forEach items="${exams}" var="exam">
 			  <tr id="row${exam.id}">
 			  	<td align="center"><input type="checkbox" id="check${exam.id}" name="examCheckbox" value="${exam.id}"/></td>
 			    <td id="examTittle${exam.id}"><c:out value="${exam.title}"/></td>
 			    <td style="text-align:center"><c:out value="${exam.duration}"/></td>
		    	<c:choose>
		    		<c:when test="${exam.visibility eq 1}">
		    			<c:choose>
		    				<c:when test="${exam.startDate le ahora and exam.endDate ge ahora}">
		    					<td style="background-color:#BEF781">
			 			    		<center><fmt_rt:formatDate value="${exam.startDate}" type="both" dateStyle="short" timeStyle="short" /> - <fmt_rt:formatDate value="${exam.endDate}" type="both" dateStyle="short" timeStyle="short" /></center>
			 			    	</td>
		    				</c:when>
		    				<c:otherwise>
		    					<td style="background-color:#FF3800">
			 			    		<center><fmt_rt:formatDate value="${exam.startDate}" type="both" dateStyle="short" timeStyle="short" /> - <fmt_rt:formatDate value="${exam.endDate}" type="both" dateStyle="short" timeStyle="short" /></center>
			 			    	</td>
		    				</c:otherwise>
		    			</c:choose>
		    		</c:when>
		    		<c:otherwise>
		    			<td>
		    				<center><img src="${pageContext.request.contextPath}/imagenes/red_dot.gif" border="none"></center>
	 			    	</td>
		    		</c:otherwise>
		    	</c:choose>
 			    <!-- Using fmt_rt, not just fmt-->
		    	<c:choose>
		    		<c:when test="${exam.activeReview eq 'true'}">
		    			<c:choose>
		    				<c:when test="${exam.startDateRevision le ahora and exam.endDateRevision ge ahora}">
		    					<td style="background-color:#BEF781">
				    				<center><fmt_rt:formatDate value="${exam.startDateRevision}" type="both" dateStyle="short" timeStyle="short" /> - <fmt_rt:formatDate value="${exam.endDateRevision}" type="both" dateStyle="short" timeStyle="short" /></center>
				    			</td>
		    				</c:when>
		    				<c:otherwise>
		    					<td style="background-color:#FF3800">
				    				<center><fmt_rt:formatDate value="${exam.startDateRevision}" type="both" dateStyle="short" timeStyle="short" /> - <fmt_rt:formatDate value="${exam.endDateRevision}" type="both" dateStyle="short" timeStyle="short" /></center>
				    			</td>
		    				</c:otherwise>
		    			</c:choose>
		    		</c:when>
		    		<c:otherwise>
		    			<td>
							<center><a><img src="${pageContext.request.contextPath}/imagenes/red_dot.gif" alt="<fmt:message key="notActive"/>" title="<fmt:message key="notActive"/>" border="none"></a></center>
						</td>
					</c:otherwise>
		    	</c:choose>
 			    
			    <td align="center">
			    	<c:choose>
			    		<c:when test="${exam.published eq true}">
			    			<a><img src="${pageContext.request.contextPath}/imagenes/visible.gif" alt="<fmt:message key="active"/>" title="<fmt:message key="active"/>" border="none" width="20px" height="20px"></a>
			    		</c:when>
			    		<c:otherwise>
			    			<a><img src="${pageContext.request.contextPath}/imagenes/invisible.gif" alt="<fmt:message key="notactive"/>" title="<fmt:message key="notactive"/>" border="none" width="20px" height="20px"></a>
			    		</c:otherwise>
			    	</c:choose>
			    </td>
				<c:if test="${user.role eq 'TUTORAV'}">
					<td align="center">
						<c:if test="${exam.customized eq true}">
							<a><img width="20px" height="20px" src="${pageContext.request.contextPath}/imagenes/customized.gif" border="none" title="<fmt:message key="labelCustomized"/>"/></a>
						</c:if>
					</td>
				</c:if>
				<td align="center">
				   <a href="javascript:document.getElementById('editCE${exam.id}').submit();"><img src="${pageContext.request.contextPath}/imagenes/editar.gif" alt="<fmt:message key="labelEditConfigExam"/>" title="<fmt:message key="labelEditConfigExam"/>" border="none"></a>
				</td>
				<td align="center">
				   <a href="javascript:deleteConfigExam('${exam.id}');"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="labelDeleteConfigExam"/>" title="<fmt:message key="labelDeleteConfigExam"/>" border="none"></a>
				</td>
				
			  </tr>
 			 
 			  <form id="editCE${exam.id}" method="POST" action="${pageContext.request.contextPath}/tutor/managegroup.itest?method=editConfigExam"><input type="hidden" name="idconfigexam" value="${exam.id}"/></form>
			 
			 </c:forEach>
			 		 
			<c:choose>
				 <c:when test="${empty exams}">
				  <tr>
				    <td align="center" colspan="9"><fmt:message key="noAvailableExams"/></td>
				  </tr>
				 </c:when>
				 <c:otherwise>
				  <tr>
				    <td align="center" colspan="9"><hr/></td>
				  </tr>
				  <tr>
				    <td align="center" colspan="9"><b><fmt:message key="totalLabel"/> ${fn:length(exams)} </b></td>
				  </tr>				 
				 </c:otherwise>
			 </c:choose>
			 
			 
			</tbody>
			</table>
			</div>
		</div>
	</body>
</html>