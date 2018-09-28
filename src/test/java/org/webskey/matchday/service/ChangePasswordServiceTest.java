package org.webskey.matchday.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.isA;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.webskey.matchday.builders.PasswordDtoBuilder;
import org.webskey.matchday.builders.UsersEntityBuilder;
import org.webskey.matchday.dao.UsersDao;
import org.webskey.matchday.dto.PasswordDto;
import org.webskey.matchday.entities.UsersEntity;
import org.webskey.matchday.services.ChangePasswordService;

@RunWith(SpringJUnit4ClassRunner.class)
public class ChangePasswordServiceTest {

	@Mock
	private UsersDao usersDao;
	
	@Mock
	private BindingResult bindingResult;

	@InjectMocks
	private ChangePasswordService changePasswordService;

	@Test
	public void shouldReturnChangePasswordView_whenBindingResultHasErrors() {
		//given
		String username = "user";
		PasswordDto passwordDto = PasswordDtoBuilder.get();
		when(bindingResult.hasErrors()).thenReturn(true);
		//when
		ModelAndView modelAndView = changePasswordService.changePassword(passwordDto, bindingResult, username);
		//then
		assertEquals(modelAndView.getViewName(), "changePassword");
	}
	
	@Test
	public void shouldRejectConfirmPasswordValue_whenPasswordAndConfirmPasswordNotEqual() {
		//given
		String username = "user";
		PasswordDto passwordDto = PasswordDtoBuilder.get();
		passwordDto.setConfirmPassword("notMatching");
		when(bindingResult.hasErrors()).thenReturn(false);
		//when
		ModelAndView modelAndView = changePasswordService.changePassword(passwordDto, bindingResult, username);
		//then
		assertEquals(modelAndView.getViewName(), "changePassword");
		
		verify(bindingResult, times(1)).rejectValue("confirmPassword", "NotEqual", "Password and Confirm Password does not match");
	}
	
	@Test
	public void shouldRejectOldPasswordValue_whenOldPasswordNotCorrect() {
		//given
		String username = "user";
		PasswordDto passwordDto = PasswordDtoBuilder.get();
		when(bindingResult.hasErrors()).thenReturn(false);
		when(usersDao.findByUsername(username)).thenReturn(UsersEntityBuilder.get());
		//when
		ModelAndView modelAndView = changePasswordService.changePassword(passwordDto, bindingResult, username);
		//then
		assertEquals(modelAndView.getViewName(), "changePassword");
		
		verify(bindingResult, times(1)).rejectValue("oldPassword", "NotEqual", "Old password incorrect");
	}
	
	@Test
	public void shouldChangePassword_whenNoErrors() {
		//given
		String username = "user";
		PasswordDto passwordDto = PasswordDtoBuilder.get();
		UsersEntity usersEntity = UsersEntityBuilder.get().get();
		usersEntity.setPassword(new BCryptPasswordEncoder().encode("password"));
		when(bindingResult.hasErrors()).thenReturn(false);
		when(usersDao.findByUsername(username)).thenReturn(Optional.of(usersEntity));		
		//when
		ModelAndView modelAndView = changePasswordService.changePassword(passwordDto, bindingResult, username);
		//then
		assertEquals(modelAndView.getViewName(), "info");
		
		verify(usersDao, times(1)).save(isA(UsersEntity.class));
	}
}
