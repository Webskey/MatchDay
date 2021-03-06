package org.webskey.matchday.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.webskey.matchday.controllers.HomeController;
import org.webskey.matchday.dao.ProfileDao;
import org.webskey.matchday.dao.UsersDao;
import org.webskey.matchday.dao.UsersRolesDao;
import org.webskey.matchday.dto.UsersDto;
import org.webskey.matchday.entities.ProfileEntity;
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
	private ProfileDao profileDao;
	
	@Autowired
	private EmailService emailService;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	public ModelAndView register(UsersDto usersDto, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return new ModelAndView("register");
		} 

		boolean areCredentialsTaken = false;
		
		if(isUsernameExisting(usersDto.getUsername()))	{		
			bindingResult.rejectValue("username", "AlreadyExists", "Username already exist.");
			areCredentialsTaken = true;
		}
		
		if(isEmailExisting(usersDto.getEmail())) {
			bindingResult.rejectValue("email", "AlreadyExists", "Email already exist.");
			areCredentialsTaken = true;
		}
		
		if(areCredentialsTaken) {
			return new ModelAndView("register");
		}
		
		logger.info("Register, new user: " + usersDto.getUsername() + "/t" + usersDto.getEmail());
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
		
		ProfileEntity profileEntity = new ProfileEntity();
		profileEntity.setId(usersEntity.getId());
		
		profileDao.save(profileEntity);
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
