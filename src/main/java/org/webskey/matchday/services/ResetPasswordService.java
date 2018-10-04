package org.webskey.matchday.services;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.webskey.matchday.controllers.HomeController;
import org.webskey.matchday.dao.ResetPasswordDao;
import org.webskey.matchday.dao.UsersDao;
import org.webskey.matchday.dto.UsersDto;
import org.webskey.matchday.entities.ResetPasswordEntity;
import org.webskey.matchday.entities.UsersEntity;

@Service
public class ResetPasswordService {

	@Autowired
	private UsersDao usersDao;

	@Autowired
	private ResetPasswordDao resetPasswordDao;	
	
	private ResetPasswordEntity resetPasswordEntity;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	public ModelAndView checkLink(String username, String token) {
		ModelAndView modelAndView = new ModelAndView("resetPassword");
		
		if(!isTokenForUsernameExisting(username)) {
			return modelAndView.addObject("error", "Invalid link");

		} else {						
			
			if(!isTokenEqualToken(token)) {
				return modelAndView.addObject("error", "Incorrect token");
			}			

			if(isTokenExpired()) {
				return modelAndView.addObject("error","Token expired");		
			}

			UsersDto usersDto = new UsersDto();
			usersDto.setUsername(username);

			modelAndView.addObject("user", usersDto);
			modelAndView.addObject("validLink", true);

			return modelAndView;
		}
	}
	
	public boolean isTokenForUsernameExisting(String username) {
		Optional<ResetPasswordEntity> rpeO = resetPasswordDao.findById(username);
		if(rpeO.isPresent()) {
			resetPasswordEntity = rpeO.get();	
			return true;
		}
		return false;
	}
	
	public boolean isTokenEqualToken(String token) {
		return resetPasswordEntity.getToken().equals(token);
	}
	
	public boolean isTokenExpired() {
		long hoursSinceTokenSent = TimeUnit.MILLISECONDS.toHours(new Date().getTime() - resetPasswordEntity.getDate().getTime());
		return hoursSinceTokenSent > 24;
	}

	public ModelAndView changePassword(UsersDto usersDto, BindingResult bindingResult) {
		if(bindingResult.hasFieldErrors("password")) {
			return new ModelAndView("resetPassword", "validLink", true);
		}	
		
		logger.info("Reset password for user: " + usersDto.getUsername());
		
		saveUserWithChangedPassword(usersDto);
		
		deleteToken(usersDto.getUsername());		

		return new ModelAndView("info", "info", "Your password is changed, you can now login with new password.");
	}
	
	public void saveUserWithChangedPassword(UsersDto usersDto) {
		UsersEntity usersEntity = usersDao.findByUsername(usersDto.getUsername()).get();
		usersEntity.setPassword(new BCryptPasswordEncoder().encode(usersDto.getPassword()));
		usersDao.save(usersEntity);
	}
	
	public void deleteToken(String username) {
		resetPasswordDao.delete(resetPasswordDao.findById(username).get());
	}
}
