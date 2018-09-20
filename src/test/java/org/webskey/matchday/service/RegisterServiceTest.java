package org.webskey.matchday.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.webskey.matchday.builders.UsersDtoBuilder;
import org.webskey.matchday.builders.UsersEntityBuilder;
import org.webskey.matchday.dao.UsersDao;
import org.webskey.matchday.dao.UsersRolesDao;
import org.webskey.matchday.mailmessages.WelcomeMessage;
import org.webskey.matchday.services.EmailService;
import org.webskey.matchday.services.RegisterService;

@RunWith(MockitoJUnitRunner.class)
public class RegisterServiceTest {

	@Mock
	private UsersDao usersDao;

	@Mock
	private UsersRolesDao usersRolesDao;

	@Mock
	private BindingResult bindingResult;

	@Mock
	private EmailService emailService;

	@InjectMocks
	private RegisterService registerService;

	@Test
	public void shouldSaveUserIntoDatabase_whenInvokedCorrectly() throws SecurityException {
		//given
		when(bindingResult.hasErrors()).thenReturn(false);
		//when
		ModelAndView modelAndView = registerService.register(UsersDtoBuilder.get(), bindingResult);
		//then
		assertEquals(modelAndView.getViewName(), "info");
		verify(emailService, times(1)).sendHtmlEmail(isA(WelcomeMessage.class));
		verify(usersDao, times(1)).save(any());
	}

	@Test
	public void shouldReturnRegisterView_whenBindingresultHasErrors() {
		//given
		when(bindingResult.hasErrors()).thenReturn(true);
		//when
		ModelAndView modelAndView = registerService.register(UsersDtoBuilder.get(), bindingResult);
		//then
		assertEquals(modelAndView.getViewName(), "register");
	}

	@Test
	public void shouldReturnRegisterViewWIthBindingResultError_whenUsernameTaken() {
		//given
		when(bindingResult.hasErrors()).thenReturn(false);
		String username = "user";
		when(usersDao.findByUsername(username)).thenReturn(UsersEntityBuilder.get());
		//when
		ModelAndView modelAndView = registerService.register(UsersDtoBuilder.get(), bindingResult);
		//then
		assertEquals(modelAndView.getViewName(), "register");
		verify(bindingResult, times(1)).reject(eq("AlreadyExists"), any());
	}

	@Test
	public void shouldReturnRegisterViewWIthBindingResultError_whenEmailTaken() {
		//given
		when(bindingResult.hasErrors()).thenReturn(false);
		String email = "yerbashop.project@gmail.com";
		when(usersDao.findByEmail(email)).thenReturn(UsersEntityBuilder.get());
		//when
		ModelAndView modelAndView = registerService.register(UsersDtoBuilder.get(), bindingResult);
		//then
		assertEquals(modelAndView.getViewName(), "register");
		verify(bindingResult, times(1)).reject(eq("AlreadyExists"), any());
	}
}
