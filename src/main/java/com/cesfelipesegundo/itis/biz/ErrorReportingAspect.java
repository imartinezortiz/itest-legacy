package com.cesfelipesegundo.itis.biz;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.web.Constants;

public class ErrorReportingAspect implements Ordered {
	private static final Log log = LogFactory.getLog("com.cesfelipesegundo.itis.biz.error");
    
	private MailSender mailSender;
    private SimpleMailMessage templateMessage;

	private int order;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }
    
	public void doErrorReport(Throwable exception) {
        log.error("Excepción lanzada", exception);
		// Create a thread safe "copy" of the template message and customize it
        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
        
        StringBuffer buffer = new StringBuffer();
        
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        buffer.append("\nUsuario: ");
        User user = (User)attributes.getAttribute(Constants.USER, RequestAttributes.SCOPE_SESSION);
        buffer.append(user.getUserName());
        buffer.append("(");
        buffer.append(user.getId());
        buffer.append(")");
        
        buffer.append("\nFecha: ");
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd ,HH:mm:ss Z");
        buffer.append(format.format(new Date()));
        buffer.append("\nExcepcion lanzada:\n");
        buffer.append(exception.toString());
        buffer.append("\nStack trace:\n");
        StringWriter writer = new StringWriter();
        exception.printStackTrace(new PrintWriter(writer));
        buffer.append(writer.toString());
        
        HttpServletRequest request = attributes.getRequest();
        buffer.append("\n\nHTTPRequest\n\n");
        buffer.append("Server: ");
        buffer.append(request.getServerName());
        buffer.append("\nURL: ");
        buffer.append(request.getRequestURI());
        buffer.append("\nMETHOD: ");
        buffer.append(request.getMethod());
        buffer.append("\nPARAMS START:\n");
        Map parameters = request.getParameterMap();
        for(Iterator it = parameters.entrySet().iterator(); it.hasNext(); ){
        	Map.Entry entry = (Entry) it.next();
        	buffer.append(entry.getKey());
        	buffer.append("=");
        	buffer.append(entry.getValue());
        }
        buffer.append("\nPARAMS END\n");

        msg.setText(buffer.toString());
        try{
            this.mailSender.send(msg);
        }
        catch(MailException ex) {
        	log.error("Error enviando email",ex);
        }
	}
	
	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
