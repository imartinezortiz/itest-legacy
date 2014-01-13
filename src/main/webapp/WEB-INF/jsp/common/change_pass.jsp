<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page errorPage="/error.jsp" %>
<%@ page import="com.cesfelipesegundo.itis.web.BreadCrumb" %>

<% 
	BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
	breadCrumb.addBundleStep("textChangePass","");
	request.setAttribute("breadCrumb",breadCrumb);
%>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" flush="true">
	<jsp:param name="menu" value="common"/>
	<jsp:param name="userRole" value="${user.role}"/>
</jsp:include>

    <script type="text/javascript">
    
		/* Check error before doing the save password operation */
		function goChangePasswd() {
			// Checking introduced data
			// Old paswd
			var oldPasswd = document.getElementById('oldPasswd').value;
			if ((oldPasswd.length == 0) || (oldPasswd.length > 9)) {
		          alert("<fmt:message key="cannotChangePasswd"/>\n\n<fmt:message key="msgOldPasswdError"/>");
		          return false;			
			}
			// New Passwd1
			var newPasswd1 = document.getElementById('newPasswd1').value;
			if ((newPasswd1.length == 0) || (newPasswd1.length > 9)) {
		          alert("<fmt:message key="cannotChangePasswd"/>\n\n<fmt:message key="msgNewPasswd1Error"/>");
		          return false;			
			}
			// New Passwd2
			var newPasswd2 = document.getElementById('newPasswd2').value;
			if ((newPasswd2.length == 0) || (newPasswd2.length > 9)) {
		          alert("<fmt:message key="cannotChangePasswd"/>\n\n<fmt:message key="msgNewPasswd2Error"/>");
		          return false;			
		    }
		    
		    if (newPasswd1 != newPasswd2) {
		          alert("<fmt:message key="cannotChangePasswd"/>\n\n<fmt:message key="msgDiffNewPasswdsdError"/>");
		          return false;		    	
		    }
		    
		    // Submit form
		    return true;
		    
		} // goChangePasswd

	</script>

		<div id="contenido">
				<fieldset id="changePassFieldset">
				<legend><fmt:message key="commonChangePassForm" /></legend>
					 <br>
			        <form id="formChangePass" action="${pageContext.request.contextPath}/common/index.itest?method=changePasswordStep2" onSubmit="return goChangePasswd();" method="post">
				     	<table align="center" style="width:90%;">
				     		<tr>
				  		  		<td width="60%"><fmt:message key="labelCommonOldPassword"/></td><td align="center"><input id="oldPasswd" type="password" name="oldPasswd" size="10" width="32"/></td>
				  		  	</tr>
				  		  	<tr>
				  		  		<td width="60%"><fmt:message key="labelCommonNewPassword1"/></td><td align="center"><input id="newPasswd1" type="password" name="newPasswd1" size="10" width="32"/></td>
							</tr>
							<tr>
				  		  		<td width="60%"><fmt:message key="labelCommonNewPassword2"/></td><td align="center"><input id="newPasswd2" type="password" name="newPasswd2" size="10" width="32"/></td>
				  			</tr>
				  		</table>
				  		<p><input id="btnSubmit" type="submit" name="submit" value="<fmt:message key="btnCommonGoChangePassword"/>"/></p>
			        </form>				  		
				</fieldset>
		</div>
	</body>
		
	</body>
</html>