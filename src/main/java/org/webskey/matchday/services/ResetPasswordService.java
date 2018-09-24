package org.webskey.matchday.services;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.webskey.matchday.dao.ResetPasswordDao;
import org.webskey.matchday.dao.UsersDao;
import org.webskey.matchday.dto.UsersDto;
import org.webskey.matchday.entities.ResetPasswordEntity;
import org.webskey.matchday.entities.UsersEntity;
import org.webskey.matchday.mailmessages.ResetPasswordMessage;

@Service
public class ResetPasswordService {

	@Autowired
	private EmailService emailService;

	@Autowired
	private UsersDao usersDao;

	@Autowired
	private ResetPasswordDao resetPasswordDao;	

	//change name on this method
	public ModelAndView sendEmail(BindingResult bindingResult, UsersDto usersDto, String url) {
		if(bindingResult.hasFieldErrors("email")) {
			return new ModelAndView("forgottenPassword");
		}

		Optional<UsersEntity> user = usersDao.findByEmail(usersDto.getEmail());
		
		if(!user.isPresent()) {
			bindingResult.rejectValue("email", "NoSuchEmail", "There is no user with that email");
			return new ModelAndView("forgottenPassword");
		}

		createToken(user.get(), url);
		return new ModelAndView("info").addObject("info", "Email with futher instructions has been sent");
	}

	private void createToken(UsersEntity user, String url) {
		String token = UUID.randomUUID().toString();
		String link = url + "/" + user.getUsername() + "/" + token;
		System.out.println(link);

		ResetPasswordEntity rpe = new ResetPasswordEntity();
		rpe.setUsername(user.getUsername());
		rpe.setToken(token);
		rpe.setDate(new Date());

		resetPasswordDao.save(rpe);

		emailService.sendHtmlEmail(new ResetPasswordMessage(user, link));
	}

	public ModelAndView checkLink(String username, String token) {
		ModelAndView modelAndView = new ModelAndView("resetPassword");
		Optional<ResetPasswordEntity> rpeO = resetPasswordDao.findById(username);

		if(!rpeO.isPresent()) {
			return modelAndView.addObject("error", "Given username in link doesnt exists");
		} else {						
			ResetPasswordEntity rpe = rpeO.get();			
			if(!rpe.getToken().equals(token)) {
				return modelAndView.addObject("error", "Incorrect token");
			}

			long hoursSinceTokenSent = TimeUnit.MILLISECONDS.toHours(new Date().getTime() - rpe.getDate().getTime());
			if(hoursSinceTokenSent > 24) {
				return modelAndView.addObject("error","Token expired");		
			}

			modelAndView.addObject("validLink", true);
			//TODO extract , check it which one is nessesary and which one is not
			UsersDto usersDto = new UsersDto();
			usersDto.setUsername(username);
			modelAndView.addObject("user", usersDto);
			modelAndView.addObject("username", username);
			return modelAndView;
		}
	}

	public ModelAndView changePassword(UsersDto usersDto, BindingResult bindingResult) {
		if(bindingResult.hasFieldErrors("password")) {
			return new ModelAndView("resetPassword").addObject("validLink", true);
		}	
		UsersEntity usersEntity = usersDao.findByUsername(usersDto.getUsername()).get();
		usersEntity.setPassword(new BCryptPasswordEncoder().encode(usersDto.getPassword()));
		usersDao.save(usersEntity);
		return new ModelAndView("info").addObject("info", "Your password is changed, you can now login with new password.");
	}
}
