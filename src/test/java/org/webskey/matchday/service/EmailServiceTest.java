package org.webskey.matchday.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.any;

import javax.mail.Message;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.webskey.matchday.builders.UsersDtoBuilder;
import org.webskey.matchday.mailmessages.WelcomeMessage;
import org.webskey.matchday.services.EmailService;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

	@Spy
	private JavaMailSenderImpl mailSender;
	
	@Mock
	private ITemplateEngine htmlTemplateEngine;

	@InjectMocks
	private EmailService emailService;

	private GreenMail testSmtp;

	@Before
	public void setUp() {
		testSmtp = new GreenMail(ServerSetupTest.SMTP);
		testSmtp.start();

		mailSender.setPort(3025);
		mailSender.setHost("localhost");	
	}

	@After
	public void cleanup(){
		testSmtp.stop();
	} 
	
	@Test
	public void shouldSendWelcomeMail_whenWelcomeMessagePassed() {
		//given
		WelcomeMessage welcomeMessage = new WelcomeMessage(UsersDtoBuilder.get());
		when(htmlTemplateEngine.process(anyString(), any())).thenReturn("Some text");
		//when
		emailService.sendHtmlEmail(welcomeMessage);
		//then
		Message[] messages = testSmtp.getReceivedMessages();
		assertEquals(1, messages.length);
	}
	
	@Test
	public void shouldSendWelcomeMail_wohenWelcomeMessagePassed() {
		//given
		WelcomeMessage welcomeMessage = new WelcomeMessage(UsersDtoBuilder.get());
		//when
		emailService.sendSimpleEmail("tp", "subject", "content");
		//then
		Message[] messages = testSmtp.getReceivedMessages();
		assertEquals(1, messages.length);
	}
}
