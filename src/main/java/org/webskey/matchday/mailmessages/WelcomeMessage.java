package org.webskey.matchday.mailmessages;

import org.thymeleaf.context.Context;
import org.webskey.matchday.dto.UsersDto;

public class WelcomeMessage implements HtmlMessage {

	private UsersDto usersDto;

	public WelcomeMessage(UsersDto usersDto) {
		this.usersDto = usersDto;
	}

	@Override
	public String getTemplate() {
		return "welcome";
	}

	@Override
	public String getTo() {
		return usersDto.getEmail();
	}

	@Override
	public String getSubject() {
		return "Welcome";
	}

	@Override
	public Context getContext() {
		Context context = new Context();
		context.setVariable("name", usersDto.getUsername());
		return context;
	}
}
