<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>
<%@ page import="com.cesfelipesegundo.itis.model.Group" %>


<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="tutor"/>
	<jsp:param name="menu" value="tutor"/>
</jsp:include>

<script>
	function showDetailsQuestion(idQuestion){
		$('#tableDivEstadisticas').html($('#answerStats'+idQuestion).html());
		$('#divEstadisticas').show('slow',function(){});
	}
</script>

		<div id="contenido">
			<table class="tabladatos">
			<tr>
				<c:choose>
					<c:when test="${view eq 'answerMarked'}">
						<th colspan="7" style="background-color: white"></th>
					</c:when>
					<c:when test="${view eq 'confidenceLevelView'}">
						<th colspan="7" style="background-color: white"></th>
					</c:when>
				</c:choose>
				<c:if test="${user.role eq 'TUTORAV'}">
					<th colspan="3">
						<table class="tabs" cellspacing="0">
								<tr>
									<c:choose>
										<c:when test="${view eq 'answerMarked'}">
											<th class="selected">
										</c:when>
										<c:otherwise>
											<th>
										</c:otherwise>
									</c:choose>
									<a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=${method}&view=answerMarked"><fmt:message key="headerViewAnswerMarked"/></a></th>
									<c:choose>
										<c:when test="${view eq 'confidenceLevelView'}">
											<th class="selected">
										</c:when>
										<c:otherwise>
											<th>
										</c:otherwise>
									</c:choose>
									<a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=${method}&view=confidenceLevelView"><fmt:message key="headerViewConfidenceLevel"/></a></th>
								</tr>
						</table>
					</th>
				</c:if>
			</tr>
				<col width="3%"/>
				<col width="30%"/>
				<col width="20%"/>
				<col width="10%"/>
				<col width="10%"/>
				<c:choose>
					<c:when test="${view eq 'answerMarked'}">
						<col width="5%"/>													
						<col width="5%"/>
						<col width="7%"/>
					</c:when>
					<c:when test="${view eq 'confidenceLevelView'}">
						<col width="5%"/>
						<col width="5%"/>
						<col width="7%"/>
					</c:when>
				</c:choose>
				<col width="7%"/>
				<col width="3%"/>
				<tr>
					<th>
						<c:choose>
							<c:when test="${orderby eq 'id' and reverse ne 'yes'}">
								<a href="?method=${method}&orderby=id&view=${view}&reverse">
							</c:when>
							<c:otherwise>
								<a href="?method=${method}&orderby=id&view=${view}">
							</c:otherwise>
						</c:choose>
						<fmt:message key="headerStatsQuestionsID"/>
					</a></th>
					<th>
						<c:choose>
							<c:when test="${orderby eq 'text' and reverse ne 'yes'}">
								<a href="?method=${method}&orderby=text&view=${view}&reverse">
							</c:when>
							<c:otherwise>
								<a href="?method=${method}&orderby=text&view=${view}">
							</c:otherwise>
						</c:choose>
						<fmt:message key="headerStatsQuestions"/>
					</a></th>
					<th>
						<c:choose>
							<c:when test="${orderby eq 'subject' and reverse ne 'yes'}">
								<a href="?method=${method}&orderby=subject&view=${view}&reverse">
							</c:when>
							<c:otherwise>
								<a href="?method=${method}&orderby=subject&view=${view}">
							</c:otherwise>
						</c:choose>
						<fmt:message key="headerStatsSubject"/>
					</a></th>
					<th>
						<c:choose>
							<c:when test="${orderby eq 'appearances' and reverse ne 'yes'}">
								<a href="?method=${method}&orderby=appearances&view=${view}&reverse">
							</c:when>
							<c:otherwise>
								<a href="?method=${method}&orderby=appearances&view=${view}">
							</c:otherwise>
						</c:choose>
						<fmt:message key="headerStatsAppearances"/><br/>
						<c:if test="${view eq 'confidenceLevelView'}">(<fmt:message key="headerStatsConfidenceLevelAppearances"/>)</c:if>
					</a></th>						
					
					<c:choose>
						<c:when test="${view eq 'answerMarked'}">
							<th>
								<c:choose>
									<c:when test="${orderby eq 'answers' and reverse ne 'yes'}">
										<a href="?method=${method}&orderby=answers&view=${view}&reverse">
									</c:when>
									<c:otherwise>
										<a href="?method=${method}&orderby=answers&view=${view}">
									</c:otherwise>
								</c:choose>
								<fmt:message key="headerStatsAnswers"/>
								</a>
							</th>
							<th>
								<c:choose>
									<c:when test="${orderby eq 'successPercentage' and reverse ne 'yes'}">
										<a href="?method=${method}&orderby=successPercentage&view=${view}&reverse">
									</c:when>
									<c:otherwise>
										<a href="?method=${method}&orderby=successPercentage&view=${view}">
									</c:otherwise>
								</c:choose>
								<fmt:message key="headerStatsSuccessPercentage"/>
							</a></th>									
							<th>
								<c:choose>
									<c:when test="${orderby eq 'failPercentage' and reverse ne 'yes'}">
										<a href="?method=${method}&orderby=failPercentage&view=${view}&reverse">
									</c:when>
									<c:otherwise>
										<a href="?method=${method}&orderby=failPercentage&view=${view}">
									</c:otherwise>
								</c:choose>
								<fmt:message key="headerStatsFailPercentage"/>
							</a></th>									
							<th>
								<c:choose>
									<c:when test="${orderby eq 'unansweredPercentage' and reverse ne 'yes'}">
										<a href="?method=${method}&orderby=unansweredPercentage&view=${view}&reverse">
									</c:when>
									<c:otherwise>
										<a href="?method=${method}&orderby=unansweredPercentage&view=${view}">
									</c:otherwise>
								</c:choose>
								<fmt:message key="headerStatsUnansweredPercentage"/>
							</a></th>
						</c:when>
						<c:when test="${view eq 'confidenceLevelView'}">
							<th>
								<c:choose>
									<c:when test="${orderby eq 'confidenceLevelAnswers' and reverse ne 'yes'}">
										<a href="?method=${method}&orderby=confidenceLevelAnswers&view=${view}&reverse">
									</c:when>
									<c:otherwise>
										<a href="?method=${method}&orderby=confidenceLevelAnswers&view=${view}">
									</c:otherwise>
								</c:choose>
								<fmt:message key="headerStatsAnswers"/>
								</a>
							</th>
							<th>
								<c:choose>
									<c:when test="${orderby eq 'spectiveEasiness' and reverse ne 'yes'}">
										<a href="?method=${method}&orderby=spectiveEasiness&view=${view}&reverse" title="<fmt:message key="titleSpectiveEasiness"/>">
									</c:when>
									<c:otherwise>
										<a href="?method=${method}&orderby=spectiveEasiness&view=${view}" title="<fmt:message key="titleSpectiveEasiness"/>">
									</c:otherwise>
								</c:choose>
								<fmt:message key="headerStatsSpectiveEasiness"/>
							</th>
							<th>
								<c:choose>
									<c:when test="${orderby eq 'trueEasiness' and reverse ne 'yes'}">
										<a href="?method=${method}&orderby=trueEasiness&view=${view}&reverse" title="<fmt:message key="titleTrueEasiness"/>">
									</c:when>
									<c:otherwise>
										<a href="?method=${method}&orderby=trueEasiness&view=${view}" title="<fmt:message key="titleTrueEasiness"/>">
									</c:otherwise>
								</c:choose>
								<fmt:message key="headerStatsTrueEasiness"/>
							</th>
							<th>
								<c:choose>
									<c:when test="${orderby eq 'insecurityEasiness' and reverse ne 'yes'}">
										<a href="?method=${method}&orderby=insecurityEasiness&view=${view}&reverse" title="<fmt:message key="titleInsecurityEasiness"/>">
									</c:when>
									<c:otherwise>
										<a href="?method=${method}&orderby=insecurityEasiness&view=${view}" title="<fmt:message key="titleInsecurityEasiness"/>">
									</c:otherwise>
								</c:choose>
								<fmt:message key="headerStatsInsecurityEasiness"/>
							</th>
						</c:when>
					</c:choose>	
					<th>
						<fmt:message key="headerStatsPreview"/>
					</th>
					<c:if test="${user.role eq 'TUTORAV'}">
						<th>
						</th>
					</c:if>							
				</tr>
				<c:forEach items="${stats}" var="stat">
				<tr>
					<td style="text-align: center"><c:out value="${stat.id}"/></td>
	   				<td>
		   				<c:choose>
		   					<c:when test="${not empty stat.title}">
		   						<c:out value="${stat.title}"/>
		   					</c:when>
		   					<c:when test="${fn:length(stat.text) gt 60}">
		   					    <c:out value="${fn:substring(stat.text,0,59)}"/>...
		   					</c:when>
		   					<c:when test="${fn:length(stat.text) eq 0}">
		   					    <i><c:out value="${stat.comment}"/></i>
		   					</c:when>
		   					<c:otherwise>
		   					    <c:out value="${stat.text}"/>
		   					</c:otherwise>
						</c:choose>
						<div id="answerStats${stat.id}" style="display:none;">
							<center><h2><fmt:message key="labelStatsByAnswer"/></h2></center>
							<div style="margin-top:10%;">
								<table class="tabladatos">
									<thead>
										<th width="10%"><fmt:message key="labelQuestion"/></th>
										<th width="60%" colspan="2"><fmt:message key="labelAnswer"/></th>
									<!--<th width="10%"><fmt:message key="headerStatsAppearances"/></th>-->
										<th width="10%">%</th>
									</thead>
									<tbody>
										<c:forEach items="${stat.answerStats}" var="answerStat">
											<tr <c:if test="${answerStat.solution eq 1}">style="background-color:#A9F5A9;"</c:if>>
												<td>${answerStat.idQuestion}</td>
												<td>${answerStat.idResp}</td>
												<td id="statAnswerTest${answerStat.idResp}" width="50%">
													<c:choose>
														<c:when test="${answerStat.idResp eq -1}"><fmt:message key="labelOtherQuestion"/>.</c:when>
														<c:otherwise>${answerStat.respText}</c:otherwise>
													</c:choose>
													
													
												</td>
												<!--<td>${answerStat.markedNumber}</td>-->
												<td>${answerStat.percentage}%</td>
											</tr>
											<c:if test="${answerStat.tipoPreg eq 0}">
												<script>
													parse2HTML('${answerStat.respText}',document.getElementById('statAnswerTest${answerStat.idResp}'));
												</script>
											</c:if>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</td>
					<td><c:out value="${stat.subject}"/></td>
					<c:choose>
						<c:when test="${view eq 'answerMarked'}">
							<td style="text-align: center"><c:out value="${stat.appeareances}"/></td>
						</c:when>
						<c:when test="${view eq 'confidenceLevelView'}">
							<td style="text-align: center"><c:out value="${stat.confidenceLevelAppeareances}"/></td>
						</c:when>
					</c:choose>
					<td style="text-align: center">
						<c:choose>
							<c:when test="${view eq 'answerMarked'}">
								<c:out value="${stat.answers}"/>
							</c:when>
							<c:when test="${view eq 'confidenceLevelView'}">
								<c:out value="${stat.confidenceLevelAnswers}"/>
							</c:when>
						</c:choose>
					</td>
					<c:choose>
						<c:when test="${view eq 'answerMarked'}">
							<td style="text-align: center">
								<fmt:formatNumber type="percent" maxFractionDigits="2">
									<c:out value="${stat.successes / stat.appeareances}"/>
								</fmt:formatNumber>
							</td>
							<td style="text-align: center">
								<fmt:formatNumber type="percent" maxFractionDigits="2">
									<c:out value="${(stat.answers - stat.successes) / stat.appeareances}"/>
								</fmt:formatNumber>
							</td>
							<td style="text-align: center">
								<fmt:formatNumber type="percent" maxFractionDigits="2">
									<c:out value="${(stat.appeareances - stat.answers ) / stat.appeareances}"/>
								</fmt:formatNumber>
							</td>
						</c:when>
						<c:when test="${view eq 'confidenceLevelView'}">
							<c:choose>
								<c:when test="${stat.confidenceLevelAppeareances gt 0}">
									<td>
										<fmt:formatNumber type="percent" maxFractionDigits="2">
											<c:out value="${(stat.spectiveEasiness ) / stat.confidenceLevelAppeareances}"/>
										</fmt:formatNumber>
									</td>
									<td>
										<fmt:formatNumber type="percent" maxFractionDigits="2">
											<c:out value="${(stat.trueEasiness ) / stat.confidenceLevelAppeareances}"/>
										</fmt:formatNumber>
									</td>
									<td>
										<fmt:formatNumber type="percent" maxFractionDigits="2">
											<c:out value="${(stat.insecurityEasiness ) / stat.confidenceLevelAppeareances}"/>
										</fmt:formatNumber>
									</td>
								</c:when>
								<c:otherwise>
									<td>---</td>
									<td>---</td>
									<td>---</td>
								</c:otherwise>
							</c:choose>
						</c:when>
					</c:choose>
					<td style="text-align: center">
						<input id="previewq${stat.id}" type="button" name="previewq${stat.id}" onclick="window.open('${pageContext.request.contextPath}/tutor/managequestion.itest?method=questionPreview&role=${group.studentRole}&qId=${stat.id}', '_blank', 'width=600,height=400,scrollbars=YES,resizable=YES'); return false;" value="<fmt:message key="previewQuestion"/>">
					</td>
					<c:if test="${user.role eq 'TUTORAV'}">
						<td align="center">
							<a href="javascript:showDetailsQuestion(${stat.id});"><img src="${pageContext.request.contextPath}/imagenes/resultados.png" border="none" title="<fmt:message key="labelStatsByAnswer"/>" alt="<fmt:message key="labelStatsByAnswer"/>"></a>
						</td>
					</c:if>
				
				</c:forEach>
				

				<c:choose>
					<c:when test="${user.role eq 'TUTORAV'}">
						  <tr>
						    <td align="center" colspan="10"><hr/></td>
						  </tr>
						  <tr>
						    <td align="center" colspan="10">
						    	<c:choose>
						    		<c:when test="${empty stats}">
						    			<fmt:message key="noAvailableExamsStats"/>
						    		</c:when>
						    		<c:otherwise>
						    			<b><fmt:message key="totalLabel"/> ${fn:length(stats)} </b>
						    		</c:otherwise>
						    	</c:choose>
						    </td>
						  </tr>	
					</c:when>
					<c:otherwise>
						<tr>
						    <td align="center" colspan="9"><hr/></td>
						  </tr>
						  <tr>
						    <td align="center" colspan="9">
						    	<c:choose>
						    		<c:when test="${empty stats}">
						    			<fmt:message key="noAvailableExamsStats"/>
						    		</c:when>
						    		<c:otherwise>
						    			<b><fmt:message key="totalLabel"/> ${fn:length(stats)} </b>
						    		</c:otherwise>
						    	</c:choose>
						    </td>
						  </tr>
					</c:otherwise>
				</c:choose>


		  </table>	
		</div>
		<div id="divEstadisticas" class="floatingDiv" style="display:none">
		<div class="floatingDivBody">
		
		<div align="right"><a href="" align="right" onclick="$('#divEstadisticas').hide('slow',function(){}); return false;"><img src="${pageContext.request.contextPath}/imagenes/borrar.gif"></a></div>
			<div id="tableDivEstadisticas">
				
			</div>
		</div>
	</div>
	</body>
</html>