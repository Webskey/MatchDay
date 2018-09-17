package org.webskey.matchday.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.webskey.matchday.dao.UsersDao;
import org.webskey.matchday.dao.UsersRolesDao;
import org.webskey.matchday.dto.UsersDto;
import org.webskey.matchday.entities.UsersEntity;
import org.webskey.matchday.entities.UsersRolesEntity;

@Service
public class RegisterService {

	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	private UsersRolesDao usersRolesDao;
	
	public void saveUser(UsersDto usersDto) throws Exception {		
		
		if(isUsernameExisting(usersDto.getUsername()) || isEmailExisting(usersDto.getEmail()))
			throw new Exception();
		
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
	
	private String encodePassword(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(password);
	}
	
	private boolean isUsernameExisting(String username) {		
		return usersDao.findByUsername(username).isPresent();
	}
	
	private boolean isEmailExisting(String email) {		
		return usersDao.findByEmail(email).isPresent();
	}
}
