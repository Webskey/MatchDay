package org.webskey.matchday.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.webskey.matchday.builders.ProfileDtoBuilder;
import org.webskey.matchday.builders.ProfileEntityBuilder;
import org.webskey.matchday.builders.UsersEntityBuilder;
import org.webskey.matchday.dao.ProfileDao;
import org.webskey.matchday.dao.UsersDao;
import org.webskey.matchday.dto.ProfileDto;
import org.webskey.matchday.services.ProfileService;

@RunWith(MockitoJUnitRunner.class)
public class ProfileServiceTest {

	@Mock
	private UsersDao usersDao;
	
	@Mock
	private ProfileDao profileDao;
	
	@Mock
	private BindingResult bindingResult;

	@InjectMocks
	private ProfileService profileService;

	@Test
	public void shouldUsersDetails_whenGetUsersDetailsMethodCalled() {
		//given
		String username = "user";
		when(usersDao.findByUsername(username)).thenReturn(UsersEntityBuilder.get());
		//when
		ProfileDto profileDto = profileService.getUsersDetails(username);
		//then
		assertEquals(profileDto.getUsername(), username);
		assertEquals(profileDto.getEmail(), "yerbashop.project@gmail.com");
		assertEquals(profileDto.getId(), 2);	
		assertEquals(profileDto.getLastname(), "Lastname");	
		assertEquals(profileDto.getCountry(), "Poland");	

		verify(usersDao, times(1)).findByUsername(username);
	}

	@Test(expected = NoSuchElementException.class)
	public void shouldThrow_whenUserDoesntExist() {
		//given		
		String username = "user";
		when(usersDao.findByUsername(username)).thenReturn(Optional.empty());
		//when
		ProfileDto profileDto = profileService.getUsersDetails(username);
		//then
		assertNull(profileDto.getId());
	}
	
	@Test
	public void shouldReturnProfileView_whenBindingResultHasErrors() {
		//given
		when(bindingResult.hasErrors()).thenReturn(true);
		//when
		ModelAndView modelAndView = profileService.changeDetails(ProfileDtoBuilder.get(), bindingResult);
		//then
		assertEquals(modelAndView.getViewName(), "profile");
	}
	
	@Test
	public void shouldReturnInfoView_whenBindingResultHasNoErrors() {
		//given
		when(bindingResult.hasErrors()).thenReturn(false);
		when(usersDao.findById(any())).thenReturn(UsersEntityBuilder.get());
		when(profileDao.findById(any())).thenReturn(ProfileEntityBuilder.get());
		//when
		ModelAndView modelAndView = profileService.changeDetails(ProfileDtoBuilder.get(), bindingResult);
		//then
		assertEquals(modelAndView.getViewName(), "info");

		verify(usersDao, times(1)).save(argThat(user -> user.getEmail().equals("yerbashop.project@gmail.co.uk")));
		verify(profileDao, times(1)).save(argThat(profile -> profile.getAdress().equals("Aleje Jerozolimskie 155/8")));
	}
}
