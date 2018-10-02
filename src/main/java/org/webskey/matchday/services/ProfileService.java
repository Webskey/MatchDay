package org.webskey.matchday.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.webskey.matchday.dao.ProfileDao;
import org.webskey.matchday.dao.UsersDao;
import org.webskey.matchday.dto.ProfileDto;
import org.webskey.matchday.entities.ProfileEntity;
import org.webskey.matchday.entities.UsersEntity;

@Service
public class ProfileService {

	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	private ProfileDao profileDao;

	public ProfileDto getUsersDetails(String username) {

		UsersEntity usersEntity = usersDao.findByUsername(username).get();
		
		ProfileDto profileDto = new ProfileDto();
		profileDto.setId(usersEntity.getId());
		profileDto.setUsername(usersEntity.getUsername());
		profileDto.setEmail(usersEntity.getEmail());
		profileDto.setFirstname(usersEntity.getProfileEntity().getFirstname());
		profileDto.setLastname(usersEntity.getProfileEntity().getLastname());
		profileDto.setPhoneNumber(usersEntity.getProfileEntity().getPhoneNumber());
		profileDto.setDateOfBirth(usersEntity.getProfileEntity().getDateOfBirth());
		profileDto.setCountry(usersEntity.getProfileEntity().getCountry());
		profileDto.setCity(usersEntity.getProfileEntity().getCity());
		profileDto.setAdress(usersEntity.getProfileEntity().getAdress());
		profileDto.setZipcode(usersEntity.getProfileEntity().getZipcode());

		return profileDto;
	}
	
	public ModelAndView changeDetails(ProfileDto profileDto, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {			
			return new ModelAndView("profile");
		}
		
		updateDetails(profileDto);
		
		return new ModelAndView("info", "info", "Profile details changed successfully");
	}
	
	public void updateDetails(ProfileDto profileDto) {
		ProfileEntity profileEntity = profileDao.findById(profileDto.getId()).get();
		profileEntity.setFirstname(profileDto.getFirstname());
		profileEntity.setLastname(profileDto.getLastname());
		profileEntity.setPhoneNumber(profileDto.getPhoneNumber());
		profileEntity.setDateOfBirth(profileDto.getDateOfBirth());
		profileEntity.setCountry(profileDto.getCountry());
		profileEntity.setCity(profileDto.getCity());
		profileEntity.setAdress(profileDto.getAdress());
		profileEntity.setZipcode(profileDto.getZipcode());
		
		profileDao.save(profileEntity);
		
		UsersEntity usersEntity = usersDao.findById(profileDto.getId()).get();
		usersEntity.setEmail(profileDto.getEmail());
		
		usersDao.save(usersEntity);
	}
}
