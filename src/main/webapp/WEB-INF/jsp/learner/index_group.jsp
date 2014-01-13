<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fmt_rt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="com.cesfelipesegundo.itis.model.Group"%>
<% 
	Group group = (Group)request.getAttribute("group");
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addBundleStep("textMain","");
	breadCrumb.addStep(group.getCourse().getName()+" ("+group.getName()+")","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<script type="text/javascript">

var startExam = true;
	function getIdexam(idexam,opcion){
		var texto;
		if(opcion == 'exam')
			texto = "<fmt:message key="learnerStartExam"/> ";
		else
			texto =  "<fmt:message key="learnerStartReview"/> ";
		
		if (confirm(texto)){ 
			startExam = true;
			var idexams = document.getElementsByName('idexam');
			for(var i=0;i<idexams.length;i++){
				idexams[i].value = idexam;
			}
		}else{
			startExam = false;
			return false;
		}
	}
	function checkIdExam(formid) {
	    var f = document.getElementById(formid);
	    if(!startExam){
			return false;
		}
	    if (f.idexam.value == '') {
	       alert("<fmt:message key="messageSelectExam" />");
	       return false;
	    } else {
	       return true;// cambiar a true
	    }
	 } // checkIdExam
	function showExamDetails(id){
		document.getElementById('div'+id).style.display='block';
		document.getElementById('minus'+id).style.display='block';
		document.getElementById('plus'+id).style.display='none';
	}

	function hideExamDetails(id){
		document.getElementById('div'+id).style.display='none';
		document.getElementById('minus'+id).style.display='none';
		document.getElementById('plus'+id).style.display='block';
	}

	function showExamsFinished(){
		document.getElementById('plus').style.display='none';
		document.getElementById('minus').style.display='block';
		document.getElementById('divExamsFinished').style.display='block';
	}

	function hideExamsFinished(){
		document.getElementById('plus').style.display='block';
		document.getElementById('minus').style.display='none';
		document.getElementById('divExamsFinished').style.display='none';
	}
	function sendMail(groupId){
		document.sendMail.submit();
	}

	
</script>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="learner"/>
</jsp:include>
		
<div id="menu"> 
  <ul>
	<li> <a href="${pageContext.request.contextPath}/common/index.itest?method=changePassword"><fmt:message key="changePasswd"/></a> </li>
  	<li> <a href="javascript:sendMail();"><fmt:message key="textSendMail"/></a> </li>
  	<li> <a href="${pageContext.request.contextPath}/common/index.itest?method=checkPlugins"><fmt:message key="checkPlugins"/></a> </li>
  	<li> <a href="${pageContext.request.contextPath}/learner/index.itest?method=handleRequest"><fmt:message key="backToMain"/></a> </li>
    <li> <a href="${pageContext.request.contextPath}/Logout"><fmt:message key="exitButton" /></a> </li>
  </ul>
</div>
	<div id="contenido">
	<form id="sendMail" name="sendMail" action="${pageContext.request.contextPath}/learner/newexam.itest?method=goToSendMail" method="post">
		<input type="hidden" id="group" name="group" value="${group.id}">
	</form>
	<form id="formrealizarexamen" action="${pageContext.request.contextPath}/learner/newexam.itest?method=goNewExam" method="post" onSubmit="return checkIdExam('formrealizarexamen')">
		
		<fieldset>
			<legend><b><fmt:message key="doExam"/></b></legend>
			<c:choose>
				<c:when test="${!empty pendingExamsConfig}">
					<c:set var="lastYear" value=""/>
					<table class="tablaDatos">
						<c:forEach items="${pendingExamsConfig}" var="exams">
							<c:choose>
								<c:when test="${lastYear ne exams.group.year}">	
									<tr>
										<th colspan="8"><h3><center><c:out value="${exams.group.year}"/></center></h3></th>
									</tr>
									<tr>
										<th style="width:50%"><fmt:message key="courseGroup"/></th>
										<th><fmt:message key="startDate"/></th>
										<th><fmt:message key="endDate"/></th>
										<th><fmt:message key="headerGroupsListGroup"/></th>
										<th><fmt:message key="headerGlistExam"/></th>
										<th><fmt:message key="labelDetails"/></th>
										<th></th>
									</tr>
									<tr>
										<td>
											<b><c:out value="${exams.group.course.name}"/></b>
											<div id="div${exams.id}" style="display:none">
												<p>
													<b><fmt:message key="numberOfQuestion"/></b> <c:out value="${exams.questionNumber}"/><!-- Aqui es questionNumbers pero no está en la clase ConfigExam -->
												</p>
												<p>
													<b><fmt:message key="examTime"/></b> <c:out value="${exams.duration}"/> <fmt:message key="minutes"/>       
												</p>
												<c:choose>
													<c:when test="${!exams.partialCorrection}">
														<p>
															<b><fmt:message key="noPartialGrades"/></b>
															<br/>
															<b><fmt:message key="PenaltiesQuestionFailed"/></b> <fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${(exams.maxGrade/exams.questionNumber)*exams.penQuestionFailed}" />
															<br/>
															<b><fmt:message key="PenaltiesQuestionNotAnswered"/></b> <fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${(exams.maxGrade/exams.questionNumber)*exams.penQuestionNotAnswered}" />
														</p>
													</c:when>
													<c:otherwise>
														<p>
															<b><fmt:message key="partialGrades"/></b>
														</p>
													</c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${exams.showNumCorrectAnswers}">
														<b><fmt:message key="showCorrectAnswers"/>.</b>
													</c:when>
													<c:otherwise>
														<b><fmt:message key="noShowCorrectAnswers"/>.</b>
													</c:otherwise>
												</c:choose>
												<p>
													<b><fmt:message key="maximumGrade"/></b>  <fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${exams.maxGrade}" />
												</p>
												<c:if test="${exams.activeReview eq true}">
													<p>
														<b><fmt:message key="startDateRevision"/></b> <fmt_rt:formatDate value="${exams.startDateRevision}" type="both" dateStyle="short" timeStyle="short" />
													</p>
													<p>
														<b><fmt:message key="endDateRevision"/></b> <fmt_rt:formatDate value="${exams.endDateRevision}" type="both" dateStyle="short" timeStyle="short" />
													</p>
												</c:if>
												<p>
													<c:choose>
														<c:when test="${exams.confidenceLevel eq true}">
															<b><fmt:message key="labelActiveConfidenceLevel"/></b><br/>
															<b><fmt:message key="labelPenalizationConfidence"/>: <fmt_rt:formatNumber type="number" minFractionDigits="2" value="${exams.penConfidenceLevel*(exams.maxGrade/exams.questionNumber)}"/></b><br/>
															<b><fmt:message key="labelRewardConfidence"/>: <fmt_rt:formatNumber type="number" minFractionDigits="2" value="${exams.rewardConfidenceLevel*(exams.maxGrade/exams.questionNumber)}"/></b>
														</c:when>
														<c:otherwise>
															<b><fmt:message key="labelNotAcctiveConfidenceLevel"/></b>
														</c:otherwise>
													</c:choose>
												</p>
											</div>
										</td>
										<td>
											<fmt_rt:formatDate value="${exams.startDate}" type="both" dateStyle="short" timeStyle="short" />
										</td>
										<td>
											<fmt_rt:formatDate value="${exams.endDate}" type="both" dateStyle="short" timeStyle="short" />
										</td>
										<td><c:out value="${exams.group.name}"/></td>
										<td><c:out value="${exams.title}"/></td>
										<td>
											<img id="plus${exams.id}" name="imgPlus" alt="<fmt:message key="showDetails"/>" title="<fmt:message key="showDetails"/>" onclick="javascript:showExamDetails('${exams.id}');" src="${pageContext.request.contextPath}/imagenes/mas.jpg" style="display: block; border:none;"/>
											<img id="minus${exams.id}" name="imgMinus" alt="<fmt:message key="hideDetails"/>" title="<fmt:message key="hideDetails"/>"src="${pageContext.request.contextPath}/imagenes/menos.jpg" onclick="javascript:hideExamDetails('${exams.id}');" style="display: none; border:none;"/>
										</td>
										<td><input type="submit" value="<fmt:message key="startExam"/>" onclick="javascript:getIdexam('${exams.id}','exam')"/></td>
									</tr>
									
									<c:set var="lastYear" value="${exams.group.year}"/>
								</c:when>
								<c:otherwise>
									<tr>
										<td>
											<b><c:out value="${exams.group.course.name}"/></b>
											<div id="div${exams.id}" style="display:none">
												<p>
													<b><fmt:message key="numberOfQuestion"/></b> <c:out value="${exams.questionNumber}"/><!-- Aqui es questionNumbers pero no está en la clase ConfigExam -->
												</p>
												<p>
													<b><fmt:message key="examTime"/></b> <c:out value="${exams.duration}"/> <fmt:message key="minutes"/>       
												</p>
												<c:choose>
													<c:when test="${!exams.partialCorrection}">
														<p>
															<b><fmt:message key="noPartialGrades"/></b>
															<br/>
															<b><fmt:message key="PenaltiesQuestionFailed"/></b> <fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${(exams.maxGrade/exams.questionNumber)*exams.penQuestionFailed}" />
															<br/>
															<b><fmt:message key="PenaltiesQuestionNotAnswered"/></b> <fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${(exams.maxGrade/exams.questionNumber)*exams.penQuestionNotAnswered}" />
														</p>
													</c:when>
													<c:otherwise>
														<p>
															<b><fmt:message key="partialGrades"/></b>
														</p>
													</c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${exams.showNumCorrectAnswers}">
														<b><fmt:message key="showCorrectAnswers"/>.</b>
													</c:when>
													<c:otherwise>
														<b><fmt:message key="noShowCorrectAnswers"/>.</b>
													</c:otherwise>
												</c:choose>
												<p>
													<b><fmt:message key="maximumGrade"/></b>  <fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${exams.maxGrade}" />
												</p>
												<c:if test="${exams.activeReview eq true}">
													<p>
														<b><fmt:message key="startDateRevision"/></b> <fmt_rt:formatDate value="${exams.startDateRevision}" type="both" dateStyle="short" timeStyle="short" />
													</p>
													<p>
														<b><fmt:message key="endDateRevision"/></b> <fmt_rt:formatDate value="${exams.endDateRevision}" type="both" dateStyle="short" timeStyle="short" />
													</p>
												</c:if>
												<p>
													<c:choose>
														<c:when test="${exams.confidenceLevel eq true}">
															<b><fmt:message key="labelActiveConfidenceLevel"/></b><br/>
															<b><fmt:message key="labelPenalizationConfidence"/>: <fmt_rt:formatNumber type="number" minFractionDigits="2" value="${exams.penConfidenceLevel*(exams.maxGrade/exams.questionNumber)}"/></b><br/>
															<b><fmt:message key="labelRewardConfidence"/>: <fmt_rt:formatNumber type="number" minFractionDigits="2" value="${exams.rewardConfidenceLevel*(exams.maxGrade/exams.questionNumber)}"/></b>
														</c:when>
														<c:otherwise>
															<b><fmt:message key="labelNotAcctiveConfidenceLevel"/></b>
														</c:otherwise>
													</c:choose>
												</p>
											</div>
										</td>
										<td>
											<fmt_rt:formatDate value="${exams.startDate}" type="both" dateStyle="short" timeStyle="short" />
										</td>
										<td>
											<fmt_rt:formatDate value="${exams.endDate}" type="both" dateStyle="short" timeStyle="short" />
										</td>
										<td><c:out value="${exams.group.name}"/></td>
										<td><c:out value="${exams.title}"/></td>
										<td>
											<img id="plus${exams.id}" name="imgPlus" title="<fmt:message key="buttonShowDetailsTheme"/>" onclick="javascript:showExamDetails('${exams.id}');" src="${pageContext.request.contextPath}/imagenes/mas.jpg" style="display: block; border:none;"/>
											<img id="minus${exams.id}" name="imgMinus" src="${pageContext.request.contextPath}/imagenes/menos.jpg" onclick="javascript:hideExamDetails('${exams.id}');" style="display: none; border:none;"/>
										</td>
										<td><input type="submit" value="<fmt:message key="startExam"/>" onclick="javascript:getIdexam('${exams.id}','exam')"/></td>
									</tr>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</table>
				</c:when>
				<c:otherwise>
					<p><fmt:message key="noAvailableExams"/></p>
				</c:otherwise>
			</c:choose>
		</fieldset>
		
		
		<input type="hidden" name="idexam" id="idexam"/>
	</form>
	
	<form id="nextExams">
		<fieldset>
			<legend><b><fmt:message key="labelNextExams"/></b></legend>
			<c:set var="lastYear" value=""/>
			<table class="tablaDatos">
				<c:forEach items="${nextExamslist}" var="exams">
					<c:choose>
						<c:when test="${lastYear ne exams.group.year}">
							<tr>
									<th colspan="7"><h3><center><c:out value="${exams.group.year}"/></center></h3></th>
								</tr>
								<tr>
									<th style="width:50%"><fmt:message key="courseGroup"/></th>
									<th><fmt:message key="startDate"/></th>
									<th><fmt:message key="endDate"/></th>
									<th><fmt:message key="headerGroupsListGroup"/></th>
									<th><fmt:message key="headerGlistExam"/></th>
									<th><fmt:message key="labelDetails"/></th>
									
								</tr>
								<tr>
									<td>
										<b><c:out value="${exams.group.course.name}"/></b>
										<div id="div${exams.id}" style="display:none">
											<p>
												<b><fmt:message key="startDate"/>: </b><fmt_rt:formatDate value="${exams.startDate}" type="both" dateStyle="short" timeStyle="short" />
											</p>
											<p>
												<b><fmt:message key="endDate"/>: </b> <fmt_rt:formatDate value="${exams.startDate}" type="both" dateStyle="short" timeStyle="short" />
											</p>
											<p>
												<b><fmt:message key="numberOfQuestion"/></b> <c:out value="${exams.questionNumber}"/>
											</p>
											<p>
												<b><fmt:message key="examTime"/></b> <c:out value="${exams.duration}"/> <fmt:message key="minutes"/>       
											</p>
											<c:choose>
												<c:when test="${!exams.partialCorrection}">
													<p>
														<b><fmt:message key="PenaltiesQuestionFailed"/></b> <fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${(exams.maxGrade/exams.questionNumber)*exams.penQuestionFailed}"/>
													</p>
													<p>
														<b><fmt:message key="PenaltiesQuestionNotAnswered"/></b> <fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${(exams.maxGrade/exams.questionNumber)*exams.penQuestionNotAnswered}"/>
													</p>
												</c:when>
												<c:otherwise>
													<p>
														<b><fmt:message key="partialGrades"/></b>
													</p>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${exams.showNumCorrectAnswers}">
													<b><fmt:message key="showCorrectAnswers"/>.</b>
												</c:when>
												<c:otherwise>
													<b><fmt:message key="noShowCorrectAnswers"/>.</b>
												</c:otherwise>
											</c:choose>
											<p>
												<b><fmt:message key="maximumGrade"/></b> <c:out value="${exams.maxGrade}"/>
											</p>
											<c:if test="${exams.activeReview eq true}">
													<p>
														<b><fmt:message key="startDateRevision"/></b> <fmt_rt:formatDate value="${exams.startDateRevision}" type="both" dateStyle="short" timeStyle="short" />
													</p>
													<p>
														<b><fmt:message key="endDateRevision"/></b> <fmt_rt:formatDate value="${exams.endDateRevision}" type="both" dateStyle="short" timeStyle="short" />
													</p>
												</c:if>
											<p>
												<c:choose>
													<c:when test="${exams.confidenceLevel eq true}">
														<b><fmt:message key="labelActiveConfidenceLevel"/></b><br/>
														<b><fmt:message key="labelPenalizationConfidence"/>: <fmt_rt:formatNumber type="number" minFractionDigits="2" value="${exams.penConfidenceLevel*(exams.maxGrade/exams.questionNumber)}"/></b><br/>
														<b><fmt:message key="labelRewardConfidence"/>: <fmt_rt:formatNumber type="number" minFractionDigits="2" value="${exams.rewardConfidenceLevel*(exams.maxGrade/exams.questionNumber)}"/></b>
													</c:when>
													<c:otherwise>
														<b><fmt:message key="labelNotAcctiveConfidenceLevel"/></b>
													</c:otherwise>
												</c:choose>
											</p>
																			
										</div>
									</td>
									<td>
										<fmt_rt:formatDate value="${exams.startDate}" type="both" dateStyle="short" timeStyle="short" />
									</td>
									<td>
										<fmt_rt:formatDate value="${exams.endDate}" type="both" dateStyle="short" timeStyle="short" />
									</td>
									<td><c:out value="${exams.group.name}"/></td>
									<td><c:out value="${exams.title}"/></td>
									<td>
										<img id="plus${exams.id}" name="imgPlus" title="<fmt:message key="buttonShowDetailsTheme"/>" onclick="javascript:showExamDetails('${exams.id}');" src="${pageContext.request.contextPath}/imagenes/mas.jpg" style="display: block; border:none;"/>
										<img id="minus${exams.id}" name="imgMinus" src="${pageContext.request.contextPath}/imagenes/menos.jpg" onclick="javascript:hideExamDetails('${exams.id}');" style="display: none; border:none;"/>
									</td>
								</tr>
								<c:set var="lastYear" value="${exams.group.year}"/>
						</c:when>
						<c:otherwise>
							<tr>
								<td>
									<b><c:out value="${exams.group.course.name}"/></b>
									<div id="div${exams.id}" style="display:none">
										<p>
											<b><fmt:message key="startDate"/>: </b><fmt_rt:formatDate value="${exams.startDate}" type="both" dateStyle="short" timeStyle="short" />
										</p>
										<p>
											<b><fmt:message key="endDate"/>: </b> <fmt_rt:formatDate value="${exams.startDate}" type="both" dateStyle="short" timeStyle="short" />
										</p>
										<p>
											<b><fmt:message key="numberOfQuestion"/></b> <c:out value="${exams.questionNumber}"/>
										</p>
										<p>
											<b><fmt:message key="examTime"/></b> <c:out value="${exams.duration}"/> <fmt:message key="minutes"/>       
										</p>
										<c:choose>
											<c:when test="${!exams.partialCorrection}">
												<p>
													<b><fmt:message key="PenaltiesQuestionFailed"/></b> <c:out value="${(exams.maxGrade/exams.questionNumber)*exams.penQuestionFailed}"/>
												</p>
												<p>
													<b><fmt:message key="PenaltiesQuestionNotAnswered"/></b> <c:out value="${(exams.maxGrade/exams.questionNumber)*exams.penQuestionNotAnswered}"/>
												</p>
											</c:when>
											<c:otherwise>
												<p>
													<b><fmt:message key="partialGrades"/></b>
												</p>
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${exams.showNumCorrectAnswers}">
												<b><fmt:message key="showCorrectAnswers"/>.</b>
											</c:when>
											<c:otherwise>
												<b><fmt:message key="noShowCorrectAnswers"/>.</b>
											</c:otherwise>
										</c:choose>
										<p>
												<b><fmt:message key="maximumGrade"/></b> <c:out value="${exams.maxGrade}"/>
										</p>
										<c:if test="${exams.activeReview eq true}">
													<p>
														<b><fmt:message key="startDateRevision"/></b> <fmt_rt:formatDate value="${exams.startDateRevision}" type="both" dateStyle="short" timeStyle="short" />
													</p>
													<p>
														<b><fmt:message key="endDateRevision"/></b> <fmt_rt:formatDate value="${exams.endDateRevision}" type="both" dateStyle="short" timeStyle="short" />
													</p>
												</c:if>
										<p>
											<c:choose>
												<c:when test="${exams.confidenceLevel eq true}">
													<b><fmt:message key="labelActiveConfidenceLevel"/></b><br/>
													<b><fmt:message key="labelPenalizationConfidence"/>: <fmt_rt:formatNumber type="number" minFractionDigits="2" value="${exams.penConfidenceLevel*(exams.maxGrade/exams.questionNumber)}"/></b><br/>
													<b><fmt:message key="labelRewardConfidence"/>: <fmt_rt:formatNumber type="number" minFractionDigits="2" value="${exams.rewardConfidenceLevel*(exams.maxGrade/exams.questionNumber)}"/></b>
												</c:when>
												<c:otherwise>
													<b><fmt:message key="labelNotAcctiveConfidenceLevel"/></b>
												</c:otherwise>
											</c:choose>
										</p>
									</div>
								</td>
								<td>
									<fmt_rt:formatDate value="${exams.startDate}" type="both" dateStyle="short" timeStyle="short" />
								</td>
								<td>
									<fmt_rt:formatDate value="${exams.endDate}" type="both" dateStyle="short" timeStyle="short" />
								</td>
								<td><c:out value="${exams.group.name}"/></td>
								<td><c:out value="${exams.title}"/></td>
								<td>
									<img id="plus${exams.id}" name="imgPlus" title="<fmt:message key="buttonShowDetailsTheme"/>" onclick="javascript:showExamDetails('${exams.id}');" src="${pageContext.request.contextPath}/imagenes/mas.jpg" style="display: block; border:none;"/>
									<img id="minus${exams.id}" name="imgMinus" src="${pageContext.request.contextPath}/imagenes/menos.jpg" onclick="javascript:hideExamDetails('${exams.id}');" style="display: none; border:none;"/>
								</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</table>
		</fieldset>
	</form>
	
	<form id="revisarexamen" action="${pageContext.request.contextPath}/learner/newexam.itest?method=reviewExam" method="post" onSubmit="return checkIdExam('revisarexamen')">
		<fieldset style="margin-top:5%; margin-bottom:5%">
			<legend><fmt:message key="examReview"/></legend>
			<c:choose>
				<c:when test="${!empty doneExamsDetails}">
					<c:set var="lastYear" value=""/>
					<table class="tablaDatos">
						<c:forEach items="${doneExamsDetails}" var="exams">
							<c:choose>
								<c:when test="${lastYear ne exams.group.year}">	
									<tr>
										<th colspan="9"><h3><center><c:out value="${exams.group.year}"/></center></h3></th>
									</tr>
									<tr>
										<th><fmt:message key="courseGroup"/></th>
										<th><fmt:message key="headerGroupsListGroup"/></th>
										<th><fmt:message key="headerGlistExam"/></th>
										<th><fmt:message key="headerGlistGrade"/></th>
										<th><fmt:message key="headerGlistMaxGrade"/></th>
										<th><fmt:message key="headerGlistStartDate"/></th>
										<th><fmt:message key="headerGlistEndDate"/></th>
										<th><fmt:message key="headerGlistDur"/></th>
										<th></th>
										
									</tr>
									<tr>
										<td><c:out value="${exams.group.course.name}"/></td>
										<td><c:out value="${exams.group.name}"/></td>
										<td><c:out value="${exams.title}"/></td>
										<c:forEach items="${califData}" var="cal">
											<c:if test="${cal.idExam eq exams.id}">
												<td><fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${cal.grade}" /></td>
												<td><fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${cal.maxGrade}" /></td>
												<td><fmt_rt:formatDate value="${cal.begin}" type="both" dateStyle="short" timeStyle="short" /></td>
												<td><fmt_rt:formatDate value="${cal.end}" type="both" dateStyle="short" timeStyle="short" /></td>
												<td><fmt_rt:formatDate type="time" value="${cal.duration}" timeZone="0" pattern="H:mm:ss"/></td>
											</c:if>
										</c:forEach>
										<td><input type="submit" value="<fmt:message key="startRevision"/>" onclick="javascript:getIdexam('${exams.id}','review')"/></td>
									</tr>
									<c:set var="lastYear" value="${exams.group.year}"/>
								</c:when>
								<c:otherwise>
									<tr>
										<td><c:out value="${exams.group.course.name}"/></td>
										<td><c:out value="${exams.group.name}"/></td>
										<td><c:out value="${exams.title}"/></td>
										<c:forEach items="${califData}" var="cal">
											<c:if test="${cal.idExam eq exams.id}">
												<td><fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${cal.grade}" /></td>
												<td><fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${cal.maxGrade}" /></td>
												<td><fmt_rt:formatDate value="${cal.begin}" type="both" dateStyle="short" timeStyle="short" /></td>
												<td><fmt_rt:formatDate value="${cal.end}" type="both" dateStyle="short" timeStyle="short" /></td>
												<td><fmt_rt:formatDate type="time" value="${cal.duration}" timeZone="0" pattern="H:mm:ss"/></td>
											</c:if>
										</c:forEach>
										<td><input type="submit" value="<fmt:message key="startRevision"/>" onclick="javascript:getIdexam('${exams.id}','review')"/></td>
									</tr>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</table>
					</c:when>
					<c:otherwise>
						<p><fmt:message key="noAvailableExams"/></p>
					</c:otherwise>
				</c:choose>
		</fieldset>
		<input type="hidden" name="idexam" id="idexam"/>
		</form>
		<!-- Pongo este form porque si no visualmente no queda bien este legend con respecto a los demás -->
		<form>
			<fieldset>
				<legend><fmt:message key="learnerExamsFinished"/></legend>
				<img id="plus" name="imgPlus" alt="<fmt:message key="showDetails"/>" title="<fmt:message key="showDetails"/>" src="${pageContext.request.contextPath}/imagenes/mas.jpg"/ onclick="javascript:showExamsFinished();" style="display:block">
				<img id="minus" name="imgMinus" alt="<fmt:message key="hideDetails"/>" title="<fmt:message key="hideDetails"/>" src="${pageContext.request.contextPath}/imagenes/menos.jpg" onclick="javascript:hideExamsFinished();" style="display:none"/>
				<div id="divExamsFinished" style="display:none;">
					<c:choose>
						<c:when test="${!empty alreadyDoneExams}">
							<c:set var="lastYear" value=""/>
							<table class="tablaDatos">
								<c:forEach items="${alreadyDoneExams}" var="exams">
									<c:choose>
										<c:when test="${lastYear ne exams.group.year}">	
											<tr>
												<th colspan="8"><h3><center><c:out value="${exams.group.year}"/></center></h3></th>
											</tr>
											<tr>
												<th><fmt:message key="courseGroup"/></th>
												<th><fmt:message key="headerGroupsListGroup"/></th>
												<th><fmt:message key="headerGlistExam"/></th>
												<th><fmt:message key="headerGlistGrade"/></th>
												<th><fmt:message key="headerGlistMaxGrade"/></th>
												<th><fmt:message key="headerGlistStartDate"/></th>
												<th><fmt:message key="headerGlistEndDate"/></th>
												<th><fmt:message key="headerGlistDur"/></th>
												
											</tr>
											<tr>
												<td><c:out value="${exams.group.course.name}"/></td>
												<td><c:out value="${exams.group.name}"/></td>
												<td><c:out value="${exams.title}"/></td>
												<c:forEach items="${alreadyDoneExamsGrade}" var="cal">
													<c:if test="${cal.idExam eq exams.id}">
														<td><fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${cal.grade}" /></td>
														<td><fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${cal.maxGrade}" /></td>
														<td><fmt_rt:formatDate value="${cal.begin}" type="both" dateStyle="short" timeStyle="short" /></td>
														<td><fmt_rt:formatDate value="${cal.end}" type="both" dateStyle="short" timeStyle="short" /></td>
														<td><fmt_rt:formatDate type="time" value="${cal.duration}" timeZone="0" pattern="H:mm:ss"/></td>
													</c:if>
												</c:forEach>
											</tr>
											<c:set var="lastYear" value="${exams.group.year}"/>
										</c:when>
										<c:otherwise>
											<tr>
												<td><c:out value="${exams.group.course.name}"/></td>
												<td><c:out value="${exams.group.name}"/></td>
												<td><c:out value="${exams.title}"/></td>
												<c:forEach items="${alreadyDoneExamsGrade}" var="cal">
													<c:if test="${cal.idExam eq exams.id}">
														<td><c:out value="${cal.grade}"/></td>
														<td><fmt_rt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${cal.maxGrade}" /></td>
														<td><fmt_rt:formatDate value="${cal.begin}" type="both" dateStyle="short" timeStyle="short" /></td>
														<td><fmt_rt:formatDate value="${cal.end}" type="both" dateStyle="short" timeStyle="short" /></td>
														<td><fmt_rt:formatDate type="time" value="${cal.duration}" timeZone="0" pattern="H:mm:ss"/></td>
													</c:if>
												</c:forEach>
											</tr>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</table>
							</c:when>
							<c:otherwise>
								<p><fmt:message key="noAvailableExams"/></p>
							</c:otherwise>
						</c:choose>
				</div>
				
			</fieldset>
		</form>
		
	</div>
	
</body>
</html>