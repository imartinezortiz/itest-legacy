package com.cesfelipesegundo.itis.web.servlets;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cesfelipesegundo.itis.biz.api.UserManagementService;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.web.Constants;

public class HomeServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(HomeServlet.class);
	
	private Map homes;

	private ServletContext context;

	private UserManagementService userService;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		homes = new HashMap();
		for (Enumeration it = config.getInitParameterNames(); it
				.hasMoreElements();) {
			String paramName = ((String) it.nextElement());
			String paramValue = config.getInitParameter(paramName);
			homes.put(paramName, paramValue);
		}
		context = config.getServletContext();
	}

	protected void initService() {
		if (userService == null) {
			WebApplicationContext springContext = WebApplicationContextUtils
					.getWebApplicationContext(context);
			userService = (UserManagementService) springContext
					.getBean("userManagementService");
		}
	}


	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
		String url = request.getContextPath()+"/";

		for (Iterator it = homes.keySet().iterator(); it.hasNext();) {
			String role = (String) it.next();
			if (request.isUserInRole(role)) {
				url = (String) homes.get(role);
				if (session.getAttribute(Constants.USER) == null) {
					String remoteUser = request.getRemoteUser();
					initService();
					User user = userService.getUser(request.getRemoteUser());
					session.setAttribute(Constants.USER,
							user);
					userService.addNewConection(user,request.getRemoteAddr());
					log.info("Usuario: "+remoteUser+" autenticado, con rol: "+role+" ip: "+request.getRemoteAddr());
				}
				break;
			}
		}
		
		String redirectURL = request.getContextPath()+url;
		String encodedURL = response.encodeURL(redirectURL);
		String encodedResponseURL = response.encodeRedirectURL(encodedURL);
		response.sendRedirect(encodedResponseURL);
	}
}