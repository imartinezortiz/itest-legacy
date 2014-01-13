package com.cesfelipesegundo.itis.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cesfelipesegundo.itis.web.Constants;

public class SecurityFilter implements Filter {
	public static final String HOME_SERVLET_PATH = "HOME_SERVLET_PATH";

	public static final String PROTECTED_PATHS = "PROTECTED_PATHS";

	private String homeServletPath;

	private String[] protectedPaths;

	public void init(FilterConfig config) throws ServletException {
		homeServletPath = config.getInitParameter(HOME_SERVLET_PATH);
		String paths = config.getInitParameter(PROTECTED_PATHS);
		protectedPaths = paths.split(",");
		for (int i = 0; i < protectedPaths.length; i++) {
			protectedPaths[i] = protectedPaths[i];
		}
		
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest _request = (HttpServletRequest) request;
		HttpServletResponse _response = (HttpServletResponse) response;
		String pathInfo = _request.getRequestURI();
		if (checkSecurity(_request, pathInfo)) {
            _response.sendRedirect(_response.encodeRedirectURL(_request
                    .getContextPath().concat(homeServletPath)));
		} else {
			chain.doFilter(request, response);
		}
	}

	private boolean checkSecurity(HttpServletRequest request, String pathInfo) {
		String actualHomeServletPath = request.getContextPath().concat(
				homeServletPath);

		boolean result = false;

		if (pathInfo.contains("j_security_check")
				&& request.getAuthType() != null) {
			/*
			 * 1. Check if the user has logged successfully but push browser
			 * back button and submit again the login form
			 */
			result = true;
		} else if (!pathInfo.startsWith(actualHomeServletPath)) {

			if (request.getRemoteUser() == null) {
				/* 2. Check if it is a protected path */
				result = false;
                for (int i = 0; i < protectedPaths.length; i++) {
                    String actualProtectedPath = request.getContextPath()
                            .concat(protectedPaths[i]);
                    if (pathInfo.startsWith(actualProtectedPath)) {
                        result = true;
                        break;
                    }
                }
			} else if ((request.getSession().getAttribute(Constants.USER) == null)
					&& request.getAuthType() != null) {
				/*
				 * 3. Check if the user has logged successfully but through a
				 * path different of /common/Home
				 */
				result = true;
			}
		}
		return result;
	}
}
