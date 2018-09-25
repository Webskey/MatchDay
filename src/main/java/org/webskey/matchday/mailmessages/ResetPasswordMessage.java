package org.webskey.matchday.mailmessages;

import org.thymeleaf.context.Context;

public class ResetPasswordMessage implements HtmlMessage {

	private String email;
	private String link;

	public ResetPasswordMessage(String email, String link) {	
		this.email = email;
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
		/*
		String message = "Hello  + user.getUsername() +  here is the link to reset your old password:\n" + link
				+ "\n\n If you didnt ask for reseting your password just ingore this email or contact website administration";
		 */
		Context context = new Context();
		context.setVariable("link", link);
		return context;
	}

}
