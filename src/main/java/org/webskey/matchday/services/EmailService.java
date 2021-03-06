package org.webskey.matchday.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.webskey.matchday.mailmessages.HtmlMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private ITemplateEngine htmlTemplateEngine;

	private ExecutorService executorService = Executors.newCachedThreadPool();

	public void sendSimpleEmail(String to, String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage(); 
		message.setTo(to); 
		message.setSubject(subject); 
		message.setText(content);
		javaMailSender.send(message);
	}

	public void sendHtmlEmail(HtmlMessage htmlMessage) {
		executorService.submit(() -> {
			String body = htmlTemplateEngine.process("mail/" + htmlMessage.getTemplate(), htmlMessage.getContext());	
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();		
			try {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
				helper.setTo(htmlMessage.getTo());
				helper.setSubject(htmlMessage.getSubject());
				helper.setText(body, true);			
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			javaMailSender.send(mimeMessage);
			System.out.println("email sent");
		});
	}
}
