package org.webskey.matchday.mailmessages;

import org.thymeleaf.context.Context;

public interface HtmlMessage {
	public String getTemplate();
	public String getTo();
	public String getSubject();
	public Context getContext();
}
