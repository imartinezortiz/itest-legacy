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
	breadCrumb.addBundleStep("gradesList","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="tutor"/>
	<jsp:param name="menu" value="tutor"/>
</jsp:include>

  	<script type='text/javascript' src='${pageContext.request.contextPath}/common/resources/scw.js'></script>

	<!-- Ajax for grades list -->
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/interface/GradeListMgmt.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
  	<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>
  	
  	<!--  Script for formatting dates, taken from http://www.mattkruse.co -->
  	<script type='text/javascript' src='${pageContext.request.contextPath}/common/resources/dates.js'></script> 	
  	
    <script type="text/javascript">

    $(document).ready(function(){
        $('#limitCheckBox').attr('checked', true);
    	runFilterAndSearch('');
    });
    
    recorregir = false;
    	// Format decimal numbers
		function format_number(pnumber,decimals){
			// Formatting to a fixed number of decimals
			var snum = new String(pnumber.toFixed(decimals));
			// Relacing the "." by a "," (spanish!!)
			return snum.replace('.',',');

		} // format_number

	    function gradeExam(iduser,idexam){
		    if(confirm("<fmt:message key="confirmCorrectExam"/>")){
				iTestLockPage('');
				recorregir = true;
		    	GradeListMgmt.gradeExam(${group.id},iduser,idexam,{callback:updateGradeList,
					 timeout:20000,
					 errorHandler:function(message) { recorregir = false;
					 									iTestUnlockPage('error');} });
		    	}
		}
    
        // Function to update the grade list
        function updateGradeList(grades) {
		  /*
		     This is a callback function that updates the information about the answers already saved (answerstable)
		     with the answer recently just saved.
		  */
		  deshabilitarVent();
  		  var rowelement, tbodyelement, grade, cellelement, formelement, inputelement;
			
		   tbodyelement=document.createElement('tbody');
		   tbodyelement.setAttribute("id","gradestabletbody");

		   // Fills the table (DOM scripting): answers data ---------------
		   var position = 0;
		   
		   while (position < grades.length) {
		        grade = grades[position];
		        
				rowelement = document.createElement('tr');
				
				// Student
				cellelement = document.createElement('td');
				cellelement.setAttribute("id","gradeUserName"+grade.id);
				cellelement.innerHTML = grade.learner.surname+", "+grade.learner.name+" ("+grade.learner.userName+")";
				rowelement.appendChild(cellelement);
				
				// Exam
				cellelement = document.createElement('td');
				cellelement.setAttribute("id","gradeExamTittle"+grade.id);
				cellelement.innerHTML = grade.exam.title;
				rowelement.appendChild(cellelement);
	
				// Grade
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				cellelement.innerHTML = format_number(grade.grade,2);
				rowelement.appendChild(cellelement);
				// Grade
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				cellelement.innerHTML = format_number(grade.exam.maxGrade,2);
				rowelement.appendChild(cellelement);

				// Button show exam
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				cellelement.innerHTML = "<input type=\"button\" name=\"showStudentExam\" value=\"<fmt:message key="buttonShowStudentExam"/>\" onclick=\"javascript:showStudentExam("+grade.learner.id+","+grade.exam.id+");\">";
				rowelement.appendChild(cellelement);

				// Button show re-Correction
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				if(grade.end == 'Thu Jan 01 1970 01:00:00 GMT+0100' || grade.end == 'Thu Jan 01 1970 01:00:00 GMT+0100 (CET)'){
					cellelement.innerHTML = "<input type=\"button\" value=\"<fmt:message key="buttonCorrectExam"/>\" onclick=\"javascript:gradeExam("+grade.learner.id+","+grade.exam.id+");\">";
					/*
					var input = document.createElement('input');
					input.type="button";
					input.value="<fmt:message key="buttonCorrectExam"/>";
					input.onclick=function(){gradeExam(grade.learner.id,grade.exam.id);};
					cellelement.appendChild(input);
					*/
				}
				rowelement.appendChild(cellelement);
				// Start Date
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				cellelement.innerHTML = formatDate(grade.begin,"d/MM/yy H:mm");
				rowelement.appendChild(cellelement);
				
				// End Date
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				cellelement.innerHTML = formatDate(grade.end,"d/MM/yy H:mm");
				rowelement.appendChild(cellelement);
				
				// Time
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				//cellelement.innerHTML = grade.time;
				if(grade.end == 'Thu Jan 01 1970 01:00:00 GMT+0100'){
					cellelement.innerHTML = "00:00:00";
				}else{
					cellelement.innerHTML = formatDateUTC(grade.duration,"H:mm:ss");
				}
				rowelement.appendChild(cellelement);
				
				// IP address
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				cellelement.innerHTML = grade.ip;
				rowelement.appendChild(cellelement);

				// Delete Exam
				cellelement = document.createElement('td');
				cellelement.setAttribute("align","center");
				cellelement.innerHTML = "<a href=\"javascript:deleteExam('"+grade.learner.id+"','"+grade.exam.id+"','"+grade.id+"');\"><img src=\"${pageContext.request.contextPath}/imagenes/borrar.gif\" alt=\"<fmt:message key="labelDeleteStdExam"/>\" title=\"<fmt:message key="labelDeleteStdExam"/>\" border=\"none\"></a>";
				rowelement.appendChild(cellelement);
				
				// Add row
				tbodyelement.appendChild(rowelement);
				
				position++;
			} // while
			
			// No grades: present message
		    if (grades.length == 0) {
		       rowelement = document.createElement('tr');
		       cellelement = document.createElement('td');
		       cellelement.colSpan=11;
		       cellelement.setAttribute("align","center");
			   cellelement.innerHTML = "<fmt:message key="noAvailableGrades"/>";
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
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
		       var limited = document.getElementById('limitCheckBox').checked;
		       if(limited && grades.length>=100){
		       		cellelement.innerHTML ="<b><fmt:message key="totalLabel"/> "+grades.length+"</b>&nbsp;&nbsp;<b>(<fmt:message key="labelSearchLimited"/>)</b>";
		       }else{
		       		cellelement.innerHTML ="<b><fmt:message key="totalLabel"/> "+grades.length+"</b>";
		       }
			   rowelement.appendChild(cellelement);
			   tbodyelement.appendChild(rowelement);
		    }
		
		
		
		    // Gets the datatable
		    datatable=document.getElementById("gradestable");
			// Replaces tbody			
			datatable.replaceChild(tbodyelement,document.getElementById("gradestabletbody"));
			
			// Restores div
			iTestUnlockPage();
			if(recorregir){
				alert("<fmt:message key="alertExamReviewCorrectly"/>");
				recorregir = false;
			}
		} // updateGradeList
		
    
	    // Function that, given some criteria, asks for the questions that make them true
	    function runFilterAndSearch(orderby) {
	         // Get the criteria of the filter:
	         var typeOrder = document.getElementById("typeOrder").value;
	         var idstudent = document.getElementById("filterStudent").value;
	         var idexam = document.getElementById("filterExam").value;
	         var grade = document.getElementById("filterGrade").value;
	         var limit = document.getElementById("limitCheckBox").checked;
	         if (isNaN(grade) || grade > 99) {
	         	alert("<fmt:message key="msgGradesListGradeError"/>");
	         	return;
	         }
	         var startDate = document.getElementById("filterStartDate").value;
	         var endDate = document.getElementById("filterEndDate").value;
	         var dur = document.getElementById("filterDur").value;
			if (isNaN(dur) || dur > 1440) {
	         	alert("<fmt:message key="msgGradesListDurError"/>");
	         	return;
	         }
	         
	         // Lock the page:
			 iTestLockPage('');
		     // Obtains the list of grades that comply with the filter, sorted by orderby (callback updateGradeList)
	         GradeListMgmt.filterAndSearch(${group.id},idstudent,idexam,grade,startDate,endDate,dur,orderby,typeOrder,limit,{callback:updateGradeList,
		         timeout:callBackTimeOut,
		         errorHandler:function(message){iTestUnlockPage('error');} });
	    } // runFilterAndSearch


	    function showStudentExam(alu,exam) {
	    	window.open("${pageContext.request.contextPath}/tutor/managegroup.itest?method=showStudentExam&alu="+alu+"&exam="+exam, "itest_preview", "width="+(screen.availWidth)+",height="+(screen.availHeight)+",top=0,left=0,scrollbars=yes");
	    } // showStudentExam


		// Function that deletes an exam done by a student
	    function deleteExam(idstudent,idexam, idgrade) {
	        // Ask for confirmation
	        var exam = document.getElementById('gradeExamTittle'+idgrade).innerHTML;
	        var student = document.getElementById('gradeUserName'+idgrade).innerHTML;
			var conf = confirm ("¿<fmt:message key="confirmDeleteStdExam"/>: "+exam+"\n<fmt:message key="confirmDeleteStdExam2"/>: "+student);
		  	if (conf) {
				// Lock the page:
				iTestLockPage('');
	         
		     	// Deletes the exam (callback updateGradeList)
	         	GradeListMgmt.deleteStudentExam(${group.id},idstudent,idexam,{callback:updateGradeList,
			         timeout:callBackTimeOut,
			         errorHandler:function(message){iTestUnlockPage('error');} });
	        }
	    } // deleteExam

	    function orderBy(orderby){

	    	var typeOrder = document.getElementById("typeOrder").value;
	    	if(typeOrder == 'ASC'){
	        	 document.getElementById("typeOrder").value = 'DESC';
		     }else{
		    	 document.getElementById("typeOrder").value = 'ASC';
			 }
	    	iTestLockPage('');
			 GradeListMgmt.orderCurrentGradeList(typeOrder,orderby,{callback:updateGradeList,
		         timeout:callBackTimeOut,
		         errorHandler:function(message){iTestUnlockPage('error');} });
		}
	</script>

		<div id="contenido">
		 <input type="hidden" id="typeOrder" value="ASC"/>
			<div>
				<form id="filterForm" name="formfiltro" method="post" action="javascript:runFilterAndSearch('');">
				  <fieldset style="font-size:16px;">
				  <legend><fmt:message key="labelFilterTitle"/></legend>
					<table width="100%">
					  <tr>
						<td align="right"><fmt:message key="labelFilterStudent"/></td>
						<td align="left">
						  <select id="filterStudent" name="filtroalu">
						  	<option selected value=""><fmt:message key="labelFilterShowAll"/></option>
							<c:if test="${!empty students}">
								<c:forEach items="${students}" var="stu">
									<option value="${stu.id}">
										<c:out value="${stu.surname}"/>, <c:out value="${stu.name}"/> (<c:out value="${stu.userName}"/>)
									</option>
								</c:forEach>
							</c:if>
						  </select>
						</td>
						<td align="right"><fmt:message key="labelFilterStartDate"/></td>
						<td align="left">
						   <input id="filterStartDate" name="textfechaini" size="8" readonly maxlength="8" type="text" value="" />&nbsp;
						   <img src="${pageContext.request.contextPath}/imagenes/scw.gif" onclick="javascript:scwShow(getElementById('filterStartDate'),this);"/>
						</td>
						<td align="right" colspan="2">
					       <input type="button" name="resetfiltrar" value="<fmt:message key="buttonFilterReset"/>" onclick="javascript:document.getElementById('filterForm').reset();document.getElementById('limitCheckBox').checked=false;runFilterAndSearch('');">
					    </td>
					  </tr>
					  
					  <tr>
						<td align="right"><fmt:message key="labelFilterExam"/></td>
						<td align="left">
						   <select id="filterExam" name="filtroexam">
								<option selected value=""><fmt:message key="labelFilterShowAll"/></option>
								<c:if test="${!empty exams}">
									<c:forEach items="${exams}" var="ex">
										<option value="${ex.id}">
											<c:out value="${ex.title}"/>
										</option>
									</c:forEach>
								</c:if>
						  </select>
						</td>
						<td align="right"><fmt:message key="labelFilterEndDate"/></td>
						<td align="left">
						   <input id="filterEndDate" name="textfechafin" size="8" readonly maxlength="8" type="text" value="" />&nbsp;
						   <img src="${pageContext.request.contextPath}/imagenes/scw.gif" onclick="javascript:scwShow(getElementById('filterEndDate'),this);"/>
						</td>
						<td align="right" colspan="2">
							<input type="reset" value="<fmt:message key="msgbuttonReset"/>" onclick="document.getElementById('gradestabletbody').innerHTML='';"/>
						</td>
					  </tr>
					  
					  <tr>
						<td align="right"><fmt:message key="labelFilterGrade"/>
						<td align="left"><input id="filterGrade" name="filtronota" size="4" /></td>
						
						
						
						
						<td align="right"><fmt:message key="labelFilterDur"/></td>
						<td align="left"><input id="filterDur" name="filtrodur" size="4" /></td>
						<td align="right">
							<input type="checkBox" id="limitCheckBox" checked/><label><fmt:message key="labelLimit100"/></label>
							<input type="submit" name="submitfiltrar" value="<fmt:message key="buttonFilterRun"/>"/>
						</td>
					  </tr>

					</table>
				  </fieldset>
				</form>
			</div>
			
			<div>
			<table id="gradestable" class="tabladatos">
			  <tr>
				<th><a href="javascript:orderBy('student');"><fmt:message key="headerGlistStudent"/></a></th>
				<th><a href="javascript:orderBy('exam');"><fmt:message key="headerGlistExam"/></a></th>	
				<th><center><a href="javascript:orderBy('grade');"><fmt:message key="headerGlistGrade"/></a></center></th>
				<th><center><a href="javascript:orderBy('maxGrade');"><fmt:message key="headerStatsMaxGrade"/></a></center></th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th><center><a href="javascript:orderBy('startDate');"><fmt:message key="headerGlistStartDate"/></a></center></th>
				<th><center><a href="javascript:orderBy('endDate');"><fmt:message key="headerGlistEndDate"/></a></center></th>
				<th><center><a href="javascript:orderBy('duration');"><fmt:message key="headerGlistDur"/></a></center></th>
				<th><center><a href="javascript:orderBy('ip');"><fmt:message key="headerGlistIP"/></a></center></th>
				<th>&nbsp;</th>
			  </tr>
			 <tbody id="gradestabletbody">
			
  			 <c:forEach items="${grades}" var="grade">
 			  <tr>
 			    <td id="gradeUserName${grade.id}"><c:out value="${grade.learner.surname}"/>, <c:out value="${grade.learner.name}"/> (<c:out value="${grade.learner.userName}"/>)</td>  				
 			    <td id="gradeExamTittle${grade.id}"><c:out value="${grade.exam.title}"/></td>
   			    <td align="center"><fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${grade.grade}" /></td>
   			    <td align="center"><fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${grade.exam.maxGrade}" /></td>
   			    <td align="center"><input type="button" name="showStudentExam" value="<fmt:message key="buttonShowStudentExam"/>" onclick="javascript:showStudentExam(${grade.learner.id},${grade.exam.id});"></td>
 			    <td>
 			    	<c:if test="${grade.end eq 'Thu Jan 01 01:00:00 CET 1970'}">
 			    		<input type="button" value="<fmt:message key="buttonCorrectExam"/>" onclick="javascript:gradeExam('${grade.learner.id}','${grade.exam.id}');"/>
 			    	</c:if>
 			    </td>
 			    <td style="text-align:center"><fmt_rt:formatDate value="${grade.begin}" type="both" dateStyle="short" timeStyle="short" /></td>
 			    <td style="text-align:center"><fmt_rt:formatDate value="${grade.end}" type="both" dateStyle="short" timeStyle="short" /></td>
 			    <td align="center">
 			    	<c:choose>
 			    		<c:when test="${grade.end eq 'Thu Jan 01 01:00:00 CET 1970'}">
 			    			00:00:00
 			    		</c:when><c:otherwise>
 			    			<fmt_rt:formatDate type="time" value="${grade.duration}" timeZone="0" pattern="H:mm:ss"/>
 			    		</c:otherwise>
 			    	</c:choose>
 			    </td>
 			    <td align="center"><c:out value="${grade.ip}"/></td>
				<td align="center"><a href="javascript:deleteExam('${grade.learner.id}','${grade.exam.id}','${grade.id}');"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif" alt="<fmt:message key="labelDeleteStdExam"/>" title="<fmt:message key="labelDeleteStdExam"/>" border="none"></a></td>
			  </tr>
			 </c:forEach>
			 	 
			<c:choose>
				 <c:when test="${empty grades}">
				  <tr>
				    <td align="center" colspan="11"><fmt:message key="noAvailableGrades"/></td>
				  </tr>
				 </c:when>
				 <c:otherwise>
				  <tr>
				    <td align="center" colspan="11"><hr/></td>
				  </tr>
				  <tr>
				    <td align="center" colspan="11"><b><fmt:message key="totalLabel"/> ${fn:length(grades)} </b></td>
				  </tr>				 
				 </c:otherwise>
			 </c:choose>
			</tbody>
			</table>
			<br/>
			</div>
		</div>

	</body>
</html>