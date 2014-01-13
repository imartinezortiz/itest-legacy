package com.cesfelipesegundo.itis.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;

public class LogOutServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession sesion = request.getSession();
		sesion.invalidate();
		response.sendRedirect(request.getContextPath() + "/");
	}
}