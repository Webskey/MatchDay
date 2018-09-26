package org.webskey.matchday.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.webskey.matchday.dao.UsersDao;
import org.webskey.matchday.dto.UsersDto;
import org.webskey.matchday.entities.UsersEntity;

@Service
public class ProfileService {

	@Autowired
	private UsersDao usersDao;

	public UsersDto getUsersDetails(String username) {

		UsersEntity usersEntity = usersDao.findByUsername(username).get();

		UsersDto usersDto = new UsersDto();
		usersDto.setId(usersEntity.getId());
		usersDto.setUsername(usersEntity.getUsername());
		usersDto.setEmail(usersEntity.getEmail());

		return usersDto;
	}

	/*public ModelAndView change(UsersDto usersDto, BindingResult bindingResult) {
		if(bindingResult.hasFieldErrors("email")) {			
			return new ModelAndView("profile");
		}

		if(bindingResult.hasFieldErrors("password") && !usersDto.getPassword().equals("")) {			
			return new ModelAndView("profile").addObject("passwordError", true);
		}

		changeUsersDetails(usersDto);

		return new ModelAndView("profile").addObject("succes", true);
	}

	public void changeUsersDetails(UsersDto usersDto) {
		UsersEntity usersEntity = usersDao.findByUsername(usersDto.getUsername()).get();
		
		if(!usersDto.getPassword().equals(""))
			usersEntity.setPassword(new BCryptPasswordEncoder().encode(usersDto.getPassword()));
		
		usersEntity.setEmail(usersDto.getEmail());

		usersDao.save(usersEntity);
	}
	
	public ModelAndView changePassword(UsersDto usersDto, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {			
			return new ModelAndView("profile");
		}
				
		return new ModelAndView("profile").addObject("succes", true);
	}*/
}
