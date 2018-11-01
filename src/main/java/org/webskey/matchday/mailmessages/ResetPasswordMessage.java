package org.webskey.matchday.mailmessages;

import org.thymeleaf.context.Context;

public class ResetPasswordMessage implements HtmlMessage {

	private String email;
	private String username;
	private String link;	

	public ResetPasswordMessage(String email, String username, String link) {	
		this.email = email;
		this.username = username;
		this.link = link;
	}

	@Override
	public String getTemplate() {
		return "resetPassword";
	}

	@Override
	public String getTo() {		
		return email;
	}

	@Override
	public String getSubject() {
		return "Reset Password";
	}

	@Override
	public Context getContext() {	
		Context context = new Context();
		context.setVariable("link", link);
		context.setVariable("username", username);
		return context;
	}

}
