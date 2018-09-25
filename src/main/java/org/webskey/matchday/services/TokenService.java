package org.webskey.matchday.services;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.webskey.matchday.dao.ResetPasswordDao;
import org.webskey.matchday.dao.UsersDao;
import org.webskey.matchday.entities.ResetPasswordEntity;
import org.webskey.matchday.entities.UsersEntity;
import org.webskey.matchday.mailmessages.ResetPasswordMessage;

@Service
public class TokenService {

	@Autowired
	private UsersDao usersDao;

	@Autowired
	private EmailService emailService;

	@Autowired
	private ResetPasswordDao resetPasswordDao;	

	public ModelAndView forgottenPassword(BindingResult bindingResult, String email, String url) {
		if(bindingResult.hasFieldErrors("email")) {
			return new ModelAndView("forgottenPassword");
		}

		if(!isUserByEmailExisting(email)) {		
			bindingResult.rejectValue("email", "NoSuchEmail", "There is no user with that email");
			return new ModelAndView("forgottenPassword");
		}

		createSaveSendToken(email, url);		

		return new ModelAndView("info").addObject("info", "Email with futher instructions has been sent");
	}

	public boolean isUserByEmailExisting(String email) {
		return usersDao.findByEmail(email).isPresent();
	}
	
	public void createSaveSendToken(String email, String url) {
		UsersEntity usersEntity = usersDao.findByEmail(email).get();
		String token = UUID.randomUUID().toString();	
		String link = url + "/" + usersEntity.getUsername() + "/" + token;

		saveToken(usersEntity.getUsername(), token);
		emailService.sendHtmlEmail(new ResetPasswordMessage(email, link));
	}
	
	public void saveToken(String username, String token) {
		ResetPasswordEntity rpe = new ResetPasswordEntity();
		rpe.setUsername(username);
		rpe.setToken(token);
		rpe.setDate(new Date());

		resetPasswordDao.save(rpe);
	}
}
