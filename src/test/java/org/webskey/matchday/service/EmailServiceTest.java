package org.webskey.matchday.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.ITemplateEngine;
import org.webskey.matchday.builders.UsersDtoBuilder;
import org.webskey.matchday.dto.UsersDto;
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
	public void shouldSendWelcomeMail_whenWelcomeMessagePassed() throws Exception {
		//given
		UsersDto usersDto = UsersDtoBuilder.get();
		WelcomeMessage welcomeMessage = new WelcomeMessage(usersDto);
		when(htmlTemplateEngine.process(anyString(), any())).thenReturn("HTML message content");	
		//when
		emailService.sendHtmlEmail(welcomeMessage);
		//then
		Message[] messages = testSmtp.getReceivedMessages();
		assertEquals(1, messages.length);
		assertEquals(messages[0].getSubject(), "Welcome");
		assertEquals(messages[0].getAllRecipients()[0].toString(), usersDto.getEmail());

		verify(mailSender, times(1)).send(isA(MimeMessage.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentException_whenHtmlTemplateEngineIssues() throws Exception {
		//given
		UsersDto usersDto = UsersDtoBuilder.get();
		WelcomeMessage welcomeMessage = new WelcomeMessage(usersDto);
		//when
		emailService.sendHtmlEmail(welcomeMessage);
		//then
		verify(mailSender, times(0)).send(isA(MimeMessage.class));
	}

	@Test(expected = NullPointerException.class)
	public void shouldThrowNullPointerException_whenUsersDtoNull() throws Exception {
		//given
		WelcomeMessage welcomeMessage = new WelcomeMessage(null);
		//when
		emailService.sendHtmlEmail(welcomeMessage);
		//then
		verify(mailSender, times(0)).send(isA(MimeMessage.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentException_whenUsersDtoEmailNull() throws Exception {
		//given
		UsersDto usersDto = UsersDtoBuilder.get();
		usersDto.setEmail(null);
		WelcomeMessage welcomeMessage = new WelcomeMessage(usersDto);
		//when
		emailService.sendHtmlEmail(welcomeMessage);
		//then
		verify(mailSender, times(0)).send(isA(MimeMessage.class));
	}

	@Test(expected = NullPointerException.class)
	public void shouldThrowNullPointerException_whenMthlMessagePassedIsNull() {
		emailService.sendHtmlEmail(null);
	}
}
