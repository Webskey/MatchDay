package org.webskey.matchday.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.webskey.matchday.dao.UsersDao;
import org.webskey.matchday.dto.PasswordDto;
import org.webskey.matchday.entities.UsersEntity;

@Service
public class ChangePasswordService {
	
	@Autowired
	private UsersDao usersDao;

	public ModelAndView changePassword(PasswordDto passwordDto, BindingResult bindingResult, String username) {
		if(bindingResult.hasErrors()) {			
			return new ModelAndView("changePassword");
		}

		if(!passwordDto.getPassword().equals(passwordDto.getConfirmPassword())) {
			bindingResult.rejectValue("confirmPassword", "NotEqual", "Password and Confirm Password does not match");
			return new ModelAndView("changePassword");
		}

		if(!isOldPasswordCorrect(passwordDto, username)) {
			bindingResult.rejectValue("oldPassword", "NotEqual", "Old password incorrect");
			return new ModelAndView("changePassword");
		}
		
		saveNewPassword(username, passwordDto.getPassword());
				
		return new ModelAndView("info", "info", "Password changed");
	}
	
	public void saveNewPassword(String username, String password) {
		UsersEntity usersEntity = usersDao.findByUsername(username).get();
		usersEntity.setPassword(new BCryptPasswordEncoder().encode(password));
		
		usersDao.save(usersEntity);
	}

	public boolean isOldPasswordCorrect(PasswordDto passwordDto, String username) {
		String password = usersDao.findByUsername(username).get().getPassword();
		BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
		return bCrypt.matches(passwordDto.getOldPassword(), password);
	}
}
