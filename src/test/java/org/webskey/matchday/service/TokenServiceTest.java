package org.webskey.matchday.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.webskey.matchday.builders.UsersEntityBuilder;
import org.webskey.matchday.dao.ResetPasswordDao;
import org.webskey.matchday.dao.UsersDao;
import org.webskey.matchday.mailmessages.ResetPasswordMessage;
import org.webskey.matchday.services.EmailService;
import org.webskey.matchday.services.TokenService;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {

	@Mock
	private UsersDao usersDao;

	@Mock
	private BindingResult bindingResult;

	@Mock
	private ResetPasswordDao resetPasswordDao;

	@Mock
	private EmailService emailService;

	@InjectMocks
	private TokenService tokenService;

	@Test
	public void shouldReturnForgottenPasswordView_whenEmailFieldHasErrors() {
		//given
		when(bindingResult.hasFieldErrors("email")).thenReturn(true);
		//when
		ModelAndView modelAndView = tokenService.forgottenPassword(bindingResult, "email@co.uk", "www.internet.pl");
		//then
		assertEquals(modelAndView.getViewName(), "forgottenPassword");
	}

	@Test
	public void shouldReturnForgottenPasswordView_whenNoUserFoundByEmail() {
		//given
		when(usersDao.findByEmail("email@co.uk")).thenReturn(Optional.empty());		
		when(bindingResult.hasFieldErrors("email")).thenReturn(false);
		//when
		ModelAndView modelAndView = tokenService.forgottenPassword(bindingResult, "email@co.uk", "www.internet.pl");
		//then
		assertEquals(modelAndView.getViewName(), "forgottenPassword");
	}

	@Test
	public void shouldCreateSaveAndSendToken_whenEmailCorrect() {
		//given		
		when(usersDao.findByEmail("email@co.uk")).thenReturn(UsersEntityBuilder.get());	
		//when
		ModelAndView modelAndView = tokenService.forgottenPassword(bindingResult, "email@co.uk", "www.internet.pl");
		//then
		assertEquals(modelAndView.getViewName(), "info");
		assertEquals(modelAndView.getModel().get("info"), "Email with futher instructions has been sent");

		verify(emailService, times(1)).sendHtmlEmail(isA(ResetPasswordMessage.class));
		verify(resetPasswordDao, times(1)).save(argThat(x -> x.getUsername().equals("user")));
	}
}
