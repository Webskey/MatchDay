package org.webskey.matchday.services;

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
import org.webskey.matchday.builders.ResetPasswordEntityBuilder;
import org.webskey.matchday.builders.UsersDtoBuilder;
import org.webskey.matchday.builders.UsersEntityBuilder;
import org.webskey.matchday.dao.ResetPasswordDao;
import org.webskey.matchday.dao.UsersDao;
import org.webskey.matchday.dto.UsersDto;
import org.webskey.matchday.entities.UsersEntity;
import org.webskey.matchday.services.ResetPasswordService;

@RunWith(MockitoJUnitRunner.class)
public class ResetPasswordServiceTest {

	@Mock
	private UsersDao usersDao;

	@Mock
	private ResetPasswordDao resetPasswordDao;

	@Mock
	private BindingResult bindingResult;

	@InjectMocks
	private ResetPasswordService resetPasswordService;

	@Test
	public void shouldReturnInvalidLinkErrorAttribute_whenUsernameNotFound() {
		//given
		when(resetPasswordDao.findById("user")).thenReturn(Optional.empty());
		//when
		ModelAndView modelAndView = resetPasswordService.checkLink("user", "123-token-321");
		//then
		assertEquals(modelAndView.getViewName(), "resetPassword");
		assertEquals(modelAndView.getModel().get("error"), "Invalid link");
	}

	@Test
	public void shouldReturnIncorrectTokenErrorAttribute_whenTokensNotEqual() {
		//given
		when(resetPasswordDao.findById("user")).thenReturn(ResetPasswordEntityBuilder.getNow());
		//when
		ModelAndView modelAndView = resetPasswordService.checkLink("user", "123-token-321");
		//then
		assertEquals(modelAndView.getViewName(), "resetPassword");
		assertEquals(modelAndView.getModel().get("error"), "Incorrect token");
	}

	@Test
	public void shouldReturnTokenExpiredErrorAttribute_whenDateDifferenceMoreThan24h() {
		//given
		when(resetPasswordDao.findById("user")).thenReturn(ResetPasswordEntityBuilder.getPast());
		//when
		ModelAndView modelAndView = resetPasswordService.checkLink("user", "321-token-123");
		//then
		assertEquals(modelAndView.getViewName(), "resetPassword");
		assertEquals(modelAndView.getModel().get("error"), "Token expired");
	}

	@Test
	public void shouldReturnValidLinkAttributeTrue_whenLinkCorrect() {
		//given
		when(resetPasswordDao.findById("user")).thenReturn(ResetPasswordEntityBuilder.getNow());
		//when
		ModelAndView modelAndView = resetPasswordService.checkLink("user", "321-token-123");
		//then
		assertEquals(modelAndView.getViewName(), "resetPassword");
		assertEquals(modelAndView.getModel().get("validLink"), true);
		assertEquals(((UsersDto) modelAndView.getModel().get("user")).getUsername(), "user");
	}

	@Test
	public void shouldReturnResetPasswordView_whenPasswordNotValidated() {
		//given
		when(bindingResult.hasFieldErrors("password")).thenReturn(true);
		//when
		ModelAndView modelAndView = resetPasswordService.changePassword(UsersDtoBuilder.get(), bindingResult);
		//then
		assertEquals(modelAndView.getViewName(), "resetPassword");
		assertEquals(modelAndView.getModel().get("validLink"), true);
	}

	@Test
	public void shouldChangePasswordAndDeleteToken_whenPasswordValidated() {
		//given
		when(bindingResult.hasFieldErrors("password")).thenReturn(false);
		when(usersDao.findByUsername("user")).thenReturn(UsersEntityBuilder.get());
		when(resetPasswordDao.findById("user")).thenReturn(ResetPasswordEntityBuilder.getNow());
		//when
		ModelAndView modelAndView = resetPasswordService.changePassword(UsersDtoBuilder.get(), bindingResult);
		//then
		assertEquals(modelAndView.getViewName(), "info");
		assertEquals(modelAndView.getModel().get("info"), "Your password is changed, you can now login with new password.");

		verify(usersDao, times(1)).save(isA(UsersEntity.class));
		verify(resetPasswordDao, times(1)).delete(argThat(x -> x.getUsername().equals("user")));
	}
}
