package org.webskey.matchday.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.webskey.matchday.builders.UsersEntityBuilder;
import org.webskey.matchday.dao.UsersDao;
import org.webskey.matchday.services.LoginService;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

	@Mock
	private UsersDao usersDao;

	@InjectMocks
	private LoginService loginService;

	@Test
	public void shouldReturnUserDetails_whenInvokedCorrectly() {
		//given
		String username = "user";
		when(usersDao.findByUsername(username)).thenReturn(UsersEntityBuilder.get());
		//when
		UserDetails userDetails = loginService.loadUserByUsername(username);
		//then
		assertEquals(userDetails.getUsername(), username);
		assertEquals(userDetails.getPassword(), "password");
		assertEquals(userDetails.getAuthorities().toString(), "[ROLE_USER]");
	}

	@Test(expected = UsernameNotFoundException.class)
	public void shouldThrowUsernameNotFoundException_whenUsersEntityNull() {
		//given
		when(usersDao.findByUsername("user")).thenReturn(Optional.empty());
		//when
		UserDetails userDetails = loginService.loadUserByUsername("user");
		//then
		assertNull(userDetails);
	}
}
