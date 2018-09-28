package org.webskey.matchday.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.webskey.matchday.dao.UsersDao;
import org.webskey.matchday.dao.UsersRolesDao;
import org.webskey.matchday.dto.UsersDto;
import org.webskey.matchday.entities.UsersEntity;
import org.webskey.matchday.entities.UsersRolesEntity;
import org.webskey.matchday.mailmessages.WelcomeMessage;

@Service
public class RegisterService {

	@Autowired
	private UsersDao usersDao;

	@Autowired
	private UsersRolesDao usersRolesDao;

	@Autowired
	private EmailService emailService;

	public ModelAndView register(UsersDto usersDto, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return new ModelAndView("register");
		} 

		if(isUsernameExisting(usersDto.getUsername()) || isEmailExisting(usersDto.getEmail()))	{		
			bindingResult.reject("AlreadyExists", "Username or/and Email already exist.");
			return new ModelAndView("register");
		}

		saveUser(usersDto);
		sendEmail(usersDto);
		
		return new ModelAndView("info").addObject("info", "Succesfull user registration");
	}


	public void saveUser(UsersDto usersDto) {
		UsersEntity usersEntity = new UsersEntity();
		usersEntity.setUsername(usersDto.getUsername());
		usersEntity.setPassword(encodePassword(usersDto.getPassword()));
		usersEntity.setEmail(usersDto.getEmail());

		usersDao.save(usersEntity);

		UsersRolesEntity usersRolesEntity = new UsersRolesEntity();
		usersRolesEntity.setRole("USER");
		usersRolesEntity.setUsers_id(usersEntity.getId());
		usersRolesEntity.setUsersEntity(usersEntity);

		usersRolesDao.save(usersRolesEntity);
	}
	
	public void sendEmail(UsersDto usersDto) {
		emailService.sendHtmlEmail(new WelcomeMessage(usersDto));
	}

	private String encodePassword(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(password);
	}

	public boolean isUsernameExisting(String username) {		
		return usersDao.findByUsername(username).isPresent();
	}

	public boolean isEmailExisting(String email) {		
		return usersDao.findByEmail(email).isPresent();
	}
}
