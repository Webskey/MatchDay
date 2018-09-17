package org.webskey.matchday.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webskey.matchday.dao.UsersDao;
import org.webskey.matchday.entities.UsersEntity;

@Service
public class LoginDetailsService implements UserDetailsService {

	@Autowired
	private UsersDao usersDao;

	@Transactional(readOnly=true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UsersEntity user = usersDao.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found."));
		UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(username);
		builder.password(user.getPassword());
		String[] roles = user.getUsersRoles().stream().map(x -> x.getRole()).toArray(String[]::new);
		builder.roles(roles);

		return builder.build();
	}
}