package com.cesfelipesegundo.itis.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class NoCacheFilter implements Filter {

	public void init(FilterConfig arg0) throws ServletException {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletResponse _response = (HttpServletResponse) response;
		_response.setHeader("Cache-Control", "no-store");
		_response.setHeader("Pragma", "no-cache");
		_response.setDateHeader("Expires", 0);
		chain.doFilter(request, response);
	}

}
