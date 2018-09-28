package org.webskey.matchday.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
}
