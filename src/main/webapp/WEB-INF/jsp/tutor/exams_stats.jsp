f<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	breadCrumb.addBundleStep("titleIndexStats",request.getContextPath()+"/tutor/managegroup.itest?method=showIndexStats");
	breadCrumb.addBundleStep("titleExamsStats","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="tutor"/>
	<jsp:param name="menu" value="tutor"/>
</jsp:include>

		<div id="contenido">
			<table class="tabladatos">
				<colgroup id="examcols">
					<col width="30%"/>
					<col width="5%"/>
					<col width="5%"/>
					<col width="10%"/>
					<col width="10%"/>
				</colgroup>
				<c:choose>
					<c:when test="${view eq 'grade'}">
						<colgroup id="gradecols">
							<col width="8%"/>
							<col width="8%"/>
							<col width="8%"/>
							<col width="8%"/>
							<col width="8%"/>
						</colgroup>
					</c:when>
					<c:when test="${view eq 'time'}">
						<colgroup id="timecols">
							<col width="8%"/>
							<col width="8%"/>
							<col width="8%"/>
							<col width="8%"/>
							<col width="8%"/>
						</colgroup>
					</c:when>
					<c:otherwise>
						<colgroup id="globalcols">
							<col width="8%"/>
							<col width="8%"/>
							<col width="8%"/>
							<col width="8%"/>
							<col width="8%"/>
						</colgroup>
					</c:otherwise>
				</c:choose>
				<tr class="noresalt">
					<th colspan="3" style="background-color: transparent"/>
					<th colspan="5" style="background-color: transparent">
						<table class="tabs" cellspacing="0">
							<tr>
								<c:choose>
									<c:when test="${view eq 'global'}"><th class="selected"></c:when>
									<c:otherwise><th></c:otherwise>
								</c:choose>
								<a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showStatsExams&view=global"/>
									<fmt:message key="headerStatsGlobals"/>
								</a></th>
								<c:choose>
									<c:when test="${view eq 'grade'}"><th class="selected"></c:when>
									<c:otherwise><th></c:otherwise>
								</c:choose>
								<a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showStatsExams&view=grade"/>
									<fmt:message key="headerStatsGrades"/>
								</a></th>
								<c:choose>
									<c:when test="${view eq 'time'}"><th class="selected"></c:when>
									<c:otherwise><th></c:otherwise>
								</c:choose>
								<a href="${pageContext.request.contextPath}/tutor/managegroup.itest?method=showStatsExams&view=time"/>
									<fmt:message key="headerStatsTimes"/>
								</a></th>
							</tr>
						</table>
					</th>
			  	</tr>
				<tr>
					<th>
						<c:choose>
							<c:when test="${orderby eq 'title' and reverse ne 'yes'}">
								<a href="?method=showStatsExams&view=<c:out value="${view}"/>&orderby=title&reverse">
							</c:when>
							<c:otherwise>
								<a href="?method=showStatsExams&view=<c:out value="${view}"/>&orderby=title">
							</c:otherwise>
						</c:choose>
						<fmt:message key="headerStatsTitle"/>
						</a>
					</th>
					
					<th>
					<c:choose>
							<c:when test="${orderby eq 'customized' and reverse ne 'yes'}">
								<a href="?method=showStatsExams&view=<c:out value="${view}"/>&orderby=customized&reverse">
							</c:when>
							<c:otherwise>
								<a href="?method=showStatsExams&view=<c:out value="${view}"/>&orderby=customized">
							</c:otherwise>
						</c:choose>
						<fmt:message key="labelCustomized"/>
						</a>
					</th>
					<th></th>
					<th>
						<c:choose>
							<c:when test="${orderby eq 'maxGrade' and reverse ne 'yes'}">
								<a href="?method=showStatsExams&view=<c:out value="${view}"/>&orderby=maxGrade&reverse">
							</c:when>
							<c:otherwise>
								<a href="?method=showStatsExams&view=<c:out value="${view}"/>&orderby=maxGrade">
							</c:otherwise>
						</c:choose>
						<fmt:message key="headerStatsValue"/>
					</a></th>					
					<th>
						<c:choose>
							<c:when test="${orderby eq 'duration' and reverse ne 'yes'}">
								<a href="?method=showStatsExams&view=<c:out value="${view}"/>&orderby=duration&reverse">
							</c:when>
							<c:otherwise>
								<a href="?method=showStatsExams&view=<c:out value="${view}"/>&orderby=duration">
							</c:otherwise>
						</c:choose>
						<fmt:message key="headerStatsDuration"/>
					</a></th>
					<c:choose>
						<c:when test="${view eq 'grade'}">
							<th style="text-align: center" title="<fmt:message key="headerStatsMin"/>">
								<c:choose>
									<c:when test="${orderby eq 'gradeMin' and reverse ne 'yes'}">
										<a href="?method=showStatsExams&view=grade&orderby=gradeMin&reverse">
									</c:when>
									<c:otherwise>
										<a href="?method=showStatsExams&view=grade&orderby=gradeMin">
									</c:otherwise>
								</c:choose>							
								<fmt:message key="headerStatsAbbrMin"/></a>
							</th>
							<th style="text-align: center" title="<fmt:message key="headerStatsMax"/>">
								<c:choose>
									<c:when test="${orderby eq 'gradeMax' and reverse ne 'yes'}">
										<a href="?method=showStatsExams&view=grade&orderby=gradeMax&reverse">
									</c:when>
									<c:otherwise>
										<a href="?method=showStatsExams&view=grade&orderby=gradeMax">
									</c:otherwise>
								</c:choose>							
								<fmt:message key="headerStatsAbbrMax"/></a>
							</th>
							<th style="text-align: center" title="<fmt:message key="headerStatsAverage"/>">
								<c:choose>
									<c:when test="${orderby eq 'gradeAverage' and reverse ne 'yes'}">
										<a href="?method=showStatsExams&view=grade&orderby=gradeAverage&reverse">
									</c:when>
									<c:otherwise>
										<a href="?method=showStatsExams&view=grade&orderby=gradeAverage">
									</c:otherwise>
								</c:choose>							
								<fmt:message key="headerStatsAbbrAverage"/></a>
							</th>
							<th style="text-align: center" title="<fmt:message key="headerStatsMedian"/>">
								<c:choose>
									<c:when test="${orderby eq 'gradeMedian' and reverse ne 'yes'}">
										<a href="?method=showStatsExams&view=grade&orderby=gradeMedian&reverse">
									</c:when>
									<c:otherwise>
										<a href="?method=showStatsExams&view=grade&orderby=gradeMedian">
									</c:otherwise>
								</c:choose>							
								<fmt:message key="headerStatsAbbrMedian"/></a>
							</th>	
							<th style="text-align: center" title="<fmt:message key="headerStatsStandardDeviation"/>">
								<c:choose>
									<c:when test="${orderby eq 'gradeStandardDeviation' and reverse ne 'yes'}">
										<a href="?method=showStatsExams&view=grade&orderby=gradeStandardDeviation&reverse">
									</c:when>
									<c:otherwise>
										<a href="?method=showStatsExams&view=grade&orderby=gradeStandardDeviation">
									</c:otherwise>
								</c:choose>							
								<fmt:message key="headerStatsAbbrStandardDeviation"/></a>
							</th>									
						</c:when>
						<c:when test="${view eq 'time'}">
							<th style="text-align: center" title="<fmt:message key="headerStatsMin"/>">
								<c:choose>
									<c:when test="${orderby eq 'timeMin' and reverse ne 'yes'}">
										<a href="?method=showStatsExams&view=time&orderby=timeMin&reverse">
									</c:when>
									<c:otherwise>
										<a href="?method=showStatsExams&view=time&orderby=timeMin">
									</c:otherwise>
								</c:choose>	
								<fmt:message key="headerStatsAbbrMin"/></a>
							</th>
							<th style="text-align: center" title="<fmt:message key="headerStatsMax"/>">
								<c:choose>
									<c:when test="${orderby eq 'timeMax' and reverse ne 'yes'}">
										<a href="?method=showStatsExams&view=time&orderby=timeMax&reverse">
									</c:when>
									<c:otherwise>
										<a href="?method=showStatsExams&view=time&orderby=timeMax">
									</c:otherwise>
								</c:choose>
								<fmt:message key="headerStatsAbbrMax"/></a>
							</th>
							<th style="text-align: center" title="<fmt:message key="headerStatsAverage"/>">
								<c:choose>
									<c:when test="${orderby eq 'timeAverage' and reverse ne 'yes'}">
										<a href="?method=showStatsExams&view=time&orderby=timeAverage&reverse">
									</c:when>
									<c:otherwise>
										<a href="?method=showStatsExams&view=time&orderby=timeAverage">
									</c:otherwise>
								</c:choose>
								<fmt:message key="headerStatsAbbrAverage"/></a>
							</th>
							<th style="text-align: center" title="<fmt:message key="headerStatsMedian"/>">
								<c:choose>
									<c:when test="${orderby eq 'timeMedian' and reverse ne 'yes'}">
										<a href="?method=showStatsExams&view=time&orderby=timeMedian&reverse">
									</c:when>
									<c:otherwise>
										<a href="?method=showStatsExams&view=time&orderby=timeMedian">
									</c:otherwise>
								</c:choose>
								<fmt:message key="headerStatsAbbrMedian"/></a>
							</th>	
							<th style="text-align: center" title="<fmt:message key="headerStatsStandardDeviation"/>">
								<c:choose>
									<c:when test="${orderby eq 'timeStandardDeviation' and reverse ne 'yes'}">
										<a href="?method=showStatsExams&view=time&orderby=timeStandardDeviation&reverse">
									</c:when>
									<c:otherwise>
										<a href="?method=showStatsExams&view=time&orderby=timeStandardDeviation">
									</c:otherwise>
								</c:choose>
								<fmt:message key="headerStatsAbbrStandardDeviation"/></a>
							</th>									
						</c:when>
						<c:otherwise>
							<th style="text-align: center" title="<fmt:message key="headerStatsMatriculated"/>">
								<c:choose>
									<c:when test="${orderby eq 'learnersNumber' and reverse ne 'yes'}">
										<a>
									</c:when>
									<c:otherwise>
										<a>
									</c:otherwise>
								</c:choose>
								<fmt:message key="headerStatsAbbrMatriculated"/></a>
							</th>
							<th style="text-align: center" title="<fmt:message key="headerStatsDone"/>">
								<c:choose>
									<c:when test="${orderby eq 'doneNumber' and reverse ne 'yes'}">
										<a href="?method=showStatsExams&view=global&orderby=doneNumber&reverse">
									</c:when>
									<c:otherwise>
										<a href="?method=showStatsExams&view=global&orderby=doneNumber">
									</c:otherwise>
								</c:choose>
								<fmt:message key="headerStatsAbbrDone"/></a>
							</th>
							<th style="text-align: center" title="<fmt:message key="headerStatsPassedPercentage"/>">
								<c:choose>
									<c:when test="${orderby eq 'passedPercentage' and reverse ne 'yes'}">
										<a href="?method=showStatsExams&view=global&orderby=passedPercentage&reverse">
									</c:when>
									<c:otherwise>
										<a href="?method=showStatsExams&view=global&orderby=passedPercentage">
									</c:otherwise>
								</c:choose>
								<fmt:message key="headerStatsAbbrPassedPercentage"/></a>
							</th>
							<th style="text-align: center" title="<fmt:message key="headerStatsFailedPercentage"/>">
								<c:choose>
									<c:when test="${orderby eq 'failedPercentage' and reverse ne 'yes'}">
										<a href="?method=showStatsExams&view=global&orderby=failedPercentage&reverse">
									</c:when>
									<c:otherwise>
										<a href="?method=showStatsExams&view=global&orderby=failedPercentage">
									</c:otherwise>
								</c:choose>
								<fmt:message key="headerStatsAbbrFailedPercentage"/></a>
							</th>
							<th style="text-align: center" title="<fmt:message key="headerStatsUndonePercentage"/>">
								<c:choose>
									<c:when test="${orderby eq 'undonePercentage' and reverse ne 'yes'}">
										<a href="?method=showStatsExams&view=global&orderby=undonePercentage&reverse">
									</c:when>
									<c:otherwise>
										<a href="?method=showStatsExams&view=global&orderby=undonePercentage">
									</c:otherwise>
								</c:choose>
								<fmt:message key="headerStatsAbbrUndonePercentage"/></a>
							</th>
						</c:otherwise>
					</c:choose>		
				</tr>
				
				<c:forEach items="${stats}" var="stat">
				<tr>
					<td><c:out value="${stat.title}"/></td>
					<c:choose>
						<c:when test="${stat.customized eq true}">
							<td style="text-align: center"><a><img width="20px" height="20px" src="${pageContext.request.contextPath}/imagenes/customized.gif" border="none"/></a></td>
						</c:when>
						<c:otherwise>
							<td style="text-align: center"></td>
						</c:otherwise>
					</c:choose>
					<td align="center">		
						<input type="button" value='<fmt:message key="titleQuestionsStats"/>' onclick="window.location='${pageContext.request.contextPath}/tutor/managegroup.itest?method=showStatsQuestionsByExam&examid=${stat.id}'" title="<fmt:message key="viewStatsQuestionsByExam"/>"/>
					</td>
					<td style="text-align: center"><c:out value="${stat.maxGrade}"/></td>
					<td style="text-align: center"><c:out value="${stat.duration}"/></td>
					
					<c:choose>
						<c:when test="${view eq 'grade'}">
							<td style="text-align: center"><fmt:formatNumber type="number" maxFractionDigits="2"><c:out value="${stat.gradeMin}"/></fmt:formatNumber></td>
							<td style="text-align: center"><fmt:formatNumber type="number" maxFractionDigits="2"><c:out value="${stat.gradeMax}"/></fmt:formatNumber></td>
							<td style="text-align: center"><fmt:formatNumber type="number" maxFractionDigits="2"><c:out value="${stat.gradeAverage}"/></fmt:formatNumber></td>
							<td style="text-align: center"><fmt:formatNumber type="number" maxFractionDigits="2"><c:out value="${stat.gradeMedian}"/></fmt:formatNumber></td>
							<td style="text-align: center"><fmt:formatNumber type="number" maxFractionDigits="2"><c:out value="${stat.gradeStandardDeviation}"/></fmt:formatNumber></td>
						</c:when>
						<c:when test="${view eq 'time'}">
							<td style="text-align: center"><fmt:formatNumber type="number" maxFractionDigits="2"><c:out value="${stat.timeMin}"/></fmt:formatNumber></td>
							<td style="text-align: center"><fmt:formatNumber type="number" maxFractionDigits="2"><c:out value="${stat.timeMax}"/></fmt:formatNumber></td>
							<td style="text-align: center"><fmt:formatNumber type="number" maxFractionDigits="2"><c:out value="${stat.timeAverage}"/></fmt:formatNumber></td>
							<td style="text-align: center"><fmt:formatNumber type="number" maxFractionDigits="2"><c:out value="${stat.timeMedian}"/></fmt:formatNumber></td>
							<td style="text-align: center"><fmt:formatNumber type="number" maxFractionDigits="2"><c:out value="${stat.timeStandardDeviation}"/></fmt:formatNumber></td>
						</c:when>
						<c:otherwise>
							<td style="text-align: center"><c:out value="${stat.learnersNumber}"/></td>
							<td style="text-align: center"><c:out value="${stat.doneNumber}"/></td>
							<td style="text-align: center">
								<fmt:formatNumber type="percent" maxFractionDigits="2">
									<c:out value="${stat.passNumber / stat.doneNumber}"/>
								</fmt:formatNumber>
							</td>
							<td style="text-align: center">
								<fmt:formatNumber type="percent" maxFractionDigits="2">
									<c:out value="${(stat.doneNumber - stat.passNumber) / stat.doneNumber}"/>
								</fmt:formatNumber>
							</td>
							<td style="text-align: center">
								<fmt:formatNumber type="percent" maxFractionDigits="2">
									<c:out value="${(stat.learnersNumber - stat.doneNumber) / stat.learnersNumber}"/>
								</fmt:formatNumber>
							</td>
						</c:otherwise>
					</c:choose>			
				</tr>
				</c:forEach>
				
				<c:choose>
				 <c:when test="${empty stats}">
				  <tr>
				    <td align="center" colspan="10"><fmt:message key="noAvailableExamsStats"/></td>
				  </tr>
				 </c:when>
				 <c:otherwise>
				  <tr>
				    <td align="center" colspan="10"><hr/></td>
				  </tr>
				  <tr>
				    <td align="center" colspan="10"><b><fmt:message key="totalLabel"/> ${fn:length(stats)} </b></td>
				  </tr>				 
				 </c:otherwise>
			 	</c:choose>
			 
			</table>
		</div>
	</body>
</html>