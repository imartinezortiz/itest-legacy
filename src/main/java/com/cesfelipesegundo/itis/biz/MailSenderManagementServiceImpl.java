package com.cesfelipesegundo.itis.biz;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.cesfelipesegundo.itis.biz.api.MailSenderManagementService;

public class MailSenderManagementServiceImpl{
	private static final Log log = LogFactory.getLog("com.cesfelipesegundo.itis.biz.error");
	private MailSender mailSender;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
   
    public boolean sendMail(String userMail, String subject, String mensaje, String[]mailUsuariosForCC, String	mailUsuariosForTo){
    	// Create a thread safe "copy" of a message and customize it
        SimpleMailMessage msg = new SimpleMailMessage();
        
        String to[] = {mailUsuariosForTo,userMail};
        msg.setTo(to);
        msg.setSentDate(new Date());
        msg.setSubject(subject);
        msg.setText(mensaje);
        try{
            this.mailSender.send(msg);
        }catch(MailParseException e){
        	log.error("Uno de los emails o los dos están vacios");
        	return false;
        }
        catch(MailException ex) {
        	log.error("Error enviando email",ex);
        	return false;
        }
    	return true;
	}
    
    public void sendMail(String userMail, String subject, String mensaje) throws Exception{
    	// Create a thread safe "copy" of a message and customize it
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(userMail);
        msg.setSentDate(new Date());
        msg.setSubject(subject);
        msg.setText(mensaje);
        this.mailSender.send(msg);
	}

}