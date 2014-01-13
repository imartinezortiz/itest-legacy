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
	breadCrumb.addBundleStep("titleIndexStats",request.getContextPath()+"/tutor/managegroup.itest?method=showIndexStats");
	breadCrumb.addBundleStep("titleLearnersStats","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="userRole" value="tutor"/>
	<jsp:param name="menu" value="tutor"/>
</jsp:include>

		<div id="contenido">
			<table class="tabladatos">
				<col width="*"/>
				<col width="0*"/>
				<col width="0*"/>
				<col width="0*"/>
				<col width="0*"/>				
				<col width="0*"/>
				<col width="0*"/>
				<col width="0*"/>												
				<tr>
					<th>
						<c:choose>
							<c:when test="${orderby eq 'fullName' and reverse ne 'yes'}">
								<a href="?method=showStatsLearners&orderby=fullName&reverse">
							</c:when>
							<c:otherwise>
								<a href="?method=showStatsLearners&orderby=fullName">
							</c:otherwise>
						</c:choose>
						<fmt:message key="headerStatsLearners"/>
					</a></th>
					<th>
						<c:choose>
							<c:when test="${orderby eq 'examsNumber' and reverse ne 'yes'}">
								<a href="?method=showStatsLearners&orderby=examsNumber&reverse">
							</c:when>
							<c:otherwise>
								<a href="?method=showStatsLearners&orderby=examsNumber">
							</c:otherwise>
						</c:choose>
						<fmt:message key="headerStatsExamsNumber"/>
					</a></th>		
					<th>
						<c:choose>
							<c:when test="${orderby eq 'passedNumber' and reverse ne 'yes'}">
								<a href="?method=showStatsLearners&orderby=passedNumber&reverse">
							</c:when>
							<c:otherwise>
								<a href="?method=showStatsLearners&orderby=passedNumber">
							</c:otherwise>
						</c:choose>
						<fmt:message key="headerStatsPassedNumber"/>
					</a></th>								
					<th>
						<c:choose>
							<c:when test="${orderby eq 'gradeMin' and reverse ne 'yes'}">
								<a href="?method=showStatsLearners&orderby=gradeMin&reverse">
							</c:when>
							<c:otherwise>
								<a href="?method=showStatsLearners&orderby=gradeMin">
							</c:otherwise>
						</c:choose>
						<fmt:message key="headerStatsMinGrade"/>
					</a></th>
					<th>
						<c:choose>
							<c:when test="${orderby eq 'gradeMax' and reverse ne 'yes'}">
								<a href="?method=showStatsLearners&orderby=gradeMax&reverse">
							</c:when>
							<c:otherwise>
								<a href="?method=showStatsLearners&orderby=gradeMax">
							</c:otherwise>
						</c:choose>
						<fmt:message key="headerStatsMaxGrade"/>
					</a></th>
					<th>
						<c:choose>
							<c:when test="${orderby eq 'gradeAverage' and reverse ne 'yes'}">
								<a href="?method=showStatsLearners&orderby=gradeAverage&reverse">
							</c:when>
							<c:otherwise>
								<a href="?method=showStatsLearners&orderby=gradeAverage">
							</c:otherwise>
						</c:choose>
						<fmt:message key="headerStatsAverage"/>
					</a></th>
					<th>
						<c:choose>
							<c:when test="${orderby eq 'gradeMedian' and reverse ne 'yes'}">
								<a href="?method=showStatsLearners&orderby=gradeMedian&reverse">
							</c:when>
							<c:otherwise>
								<a href="?method=showStatsLearners&orderby=gradeMedian">
							</c:otherwise>
						</c:choose>
						<fmt:message key="headerStatsMedian"/>
					</a></th>	
					<th>
						<c:choose>
							<c:when test="${orderby eq 'gradeStandardDeviation' and reverse ne 'yes'}">
								<a href="?method=showStatsLearners&orderby=gradeStandardDeviation&reverse">
							</c:when>
							<c:otherwise>
								<a href="?method=showStatsLearners&orderby=gradeStandardDeviation">
							</c:otherwise>
						</c:choose>
						<fmt:message key="headerStatsStandardDeviation"/>
					</a></th>									
				</tr>
				<c:forEach items="${stats}" var="stat">
				<tr>
					<td><c:out value="${stat.surname}"/>, <c:out value="${stat.name}"/> (<c:out value="${stat.username}"/>)</td>
					<td style="text-align: center"><c:out value="${stat.examsNumber}"/></td>
					<td style="text-align: center"><c:out value="${stat.passedNumber}"/></td>
					<td style="text-align: center"><fmt:formatNumber type="number" maxFractionDigits="2"><c:out value="${stat.gradeMin}"/></fmt:formatNumber></td>
					<td style="text-align: center"><fmt:formatNumber type="number" maxFractionDigits="2"><c:out value="${stat.gradeMax}"/></fmt:formatNumber></td>
					<td style="text-align: center"><fmt:formatNumber type="number" maxFractionDigits="2"><c:out value="${stat.gradeAverage}"/></fmt:formatNumber></td>
					<td style="text-align: center"><fmt:formatNumber type="number" maxFractionDigits="2"><c:out value="${stat.gradeMedian}"/></fmt:formatNumber></td>
					<td style="text-align: center"><fmt:formatNumber type="number" maxFractionDigits="2"><c:out value="${stat.gradeStandardDeviation}"/></fmt:formatNumber></td>
				</tr>
				</c:forEach>
				
				<c:choose>
				 <c:when test="${empty stats}">
				  <tr>
				    <td align="center" colspan="8"><fmt:message key="noAvailableLearnersStats"/></td>
				  </tr>
				 </c:when>
				 <c:otherwise>
				  <tr>
				    <td align="center" colspan="8"><hr/></td>
				  </tr>
				  <tr>
				    <td align="center" colspan="8"><b><fmt:message key="totalLabel"/> ${fn:length(stats)} </b></td>
				  </tr>				 
				 </c:otherwise>
			 	</c:choose>
				
		  	</table>
		</div>
	</body>
</html>