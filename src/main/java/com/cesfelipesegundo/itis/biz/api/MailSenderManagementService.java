package com.cesfelipesegundo.itis.biz.api;

public interface MailSenderManagementService {

	public void sendMail(String userMail, String subject, String mensaje, String[]mailUsuariosForCC, String	mailUsuariosForTo);
}
