package com.cesfelipesegundo.itis.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.servlet.StickyCaptchaServlet;
import nl.captcha.text.producer.ChineseTextProducer;
import nl.captcha.text.producer.DefaultTextProducer;

public class StickyCaptchaServletImpl extends StickyCaptchaServlet{
	
	public void doGet(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		Captcha captcha = new Captcha.Builder(200, 50)
			.addBackground(new GradiatedBackgroundProducer())
			.addText()
			.addNoise()
			.gimp()
		    .build();
		session.setAttribute(Captcha.NAME, captcha);
		CaptchaServletUtil.writeImage(response, captcha.getImage());
	}
}
