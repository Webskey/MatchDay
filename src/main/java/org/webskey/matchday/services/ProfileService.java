package org.webskey.matchday.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webskey.matchday.dao.UsersDao;
import org.webskey.matchday.dto.ProfileDto;
import org.webskey.matchday.entities.UsersEntity;

@Service
public class ProfileService {

	@Autowired
	private UsersDao usersDao;

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
}
