package org.webskey.matchday.controllers;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.webskey.matchday.builders.UsersDtoBuilder;
import org.webskey.matchday.dao.ResetPasswordDao;
import org.webskey.matchday.dto.UsersDto;
import org.webskey.matchday.services.ResetPasswordService;
import org.webskey.matchday.services.ForgottenPasswordService;

@RunWith(SpringJUnit4ClassRunner.class)
public class ResetPasswordControllerTest {

	@Mock
	private ResetPasswordService resetPasswordService;	

	@Mock
	private HttpServletRequest request;

	@Mock
	private ForgottenPasswordService tokenService;

	@Mock
	private ResetPasswordDao resetPasswordDao;	

	@InjectMocks
	private ForgottenPasswordController resetPasswordController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("classpath:templates/");
		viewResolver.setSuffix(".html");

		this.mockMvc = MockMvcBuilders.standaloneSetup(resetPasswordController)	
				.setViewResolvers(viewResolver)
				.build();
	}

	@Test
	public void shouldReturnForgottenPasswordView_whenCalledForgottenPasswordPage() throws Exception {	
		//when
		mockMvc.perform(get("/forgotten-password"))
		//then
		.andExpect(status().isOk())
		.andExpect(model().attribute("user", instanceOf(UsersDto.class)))
		.andExpect(view().name("forgottenPassword"));
	}

	@Test
	public void shouldRejectReseting_whenEmailNotValidated() throws Exception {
		//given
		when(tokenService.forgottenPassword(any(), any(), any())).thenReturn(new ModelAndView("forgottenPassword"));
		//when
		mockMvc.perform(post("/forgotten-pass").param("email", "F").flashAttr("user", UsersDtoBuilder.get()))
		//then
		.andExpect(model().attributeHasFieldErrors("user", "email"))
		.andExpect(view().name("forgottenPassword"))
		.andExpect(status().isOk());
	}

	@Test
	public void shouldRejectReseting_whenUserByEmailNotFound() throws Exception {
		//given				
		when(tokenService.isUserByEmailExisting(any())).thenReturn(false);
		when(tokenService.forgottenPassword(any(), any(), any())).thenCallRealMethod();		
		//when
		mockMvc.perform(post("/forgotten-pass").flashAttr("user", UsersDtoBuilder.get()))
		//then
		.andExpect(model().attributeHasFieldErrors("user", "email"))
		.andExpect(view().name("forgottenPassword"))
		.andExpect(status().isOk());
	}

	@Test
	public void shouldReturnEmailHasBeenSentInfo_whenEmailEligible() throws Exception {
		//given
		when(tokenService.isUserByEmailExisting(any())).thenReturn(true);
		when(tokenService.forgottenPassword(any(), any(), any())).thenCallRealMethod();		
		when(request.getRequestURL()).thenReturn(new StringBuffer("ASD"));		
		//when
		mockMvc.perform(post("/forgotten-pass").flashAttr("user", UsersDtoBuilder.get()))
		//then
		.andExpect(model().attributeHasNoErrors("user"))
		.andExpect(model().attribute("user", hasProperty("email", is("yerbashop.project@gmail.com"))))
		.andExpect(model().attribute("info", is("Email with futher instructions has been sent")))
		.andExpect(view().name("info"))
		.andExpect(status().isOk());

		verify(tokenService, times(1)).createSaveSendToken("yerbashop.project@gmail.com",  "http://localhost/forgotten-pass");
	}

	@Test
	public void shouldReturnInvalidLinkError_whenNoTokenForUsernameInLink() throws Exception {
		//given		
		when(resetPasswordService.isTokenForUsernameExisting(any())).thenReturn(false);
		when(resetPasswordService.checkLink(any(), any())).thenCallRealMethod();
		//when
		mockMvc.perform(get("/forgotten-pass/ok/123-321"))
		//then		
		.andExpect(view().name("resetPassword"))
		.andExpect(model().attribute("error", is("Invalid link")))
		.andExpect(status().isOk());
	}

	@Test
	public void shouldReturnIncorrectTokkenError_whenTokenNotMatching() throws Exception {
		//given		
		when(resetPasswordService.isTokenForUsernameExisting(any())).thenReturn(true);
		when(resetPasswordService.isTokenEqualToken(any())).thenReturn(false);
		when(resetPasswordService.checkLink(any(), any())).thenCallRealMethod();
		//when
		mockMvc.perform(get("/forgotten-pass/username/123-token-321"))
		//then		
		.andExpect(view().name("resetPassword"))
		.andExpect(model().attribute("error", is("Incorrect token")))
		.andExpect(status().isOk());
	}

	@Test
	public void shouldReturnTokenExpiredError_whenTokenCreatedOver24hAgo() throws Exception {
		//given		
		when(resetPasswordService.isTokenForUsernameExisting(any())).thenReturn(true);
		when(resetPasswordService.isTokenEqualToken(any())).thenReturn(true);
		when(resetPasswordService.isTokenExpired()).thenReturn(true);
		when(resetPasswordService.checkLink(any(), any())).thenCallRealMethod();
		//when
		mockMvc.perform(get("/forgotten-pass/username/123-token-321"))
		//then		
		.andExpect(view().name("resetPassword"))
		.andExpect(model().attribute("error", is("Token expired")))
		.andExpect(status().isOk());
	}

	@Test
	public void shouldReturnValidLinkAttribute_whenLinkCheckPositive() throws Exception {
		//given		
		when(resetPasswordService.isTokenForUsernameExisting(any())).thenReturn(true);
		when(resetPasswordService.isTokenEqualToken(any())).thenReturn(true);
		when(resetPasswordService.isTokenExpired()).thenReturn(false);
		when(resetPasswordService.checkLink(any(), any())).thenCallRealMethod();
		//when
		mockMvc.perform(get("/forgotten-pass/username/123-token-321"))
		//then		
		.andExpect(view().name("resetPassword"))
		.andExpect(model().attribute("validLink", is(true)))
		.andExpect(model().attribute("user", instanceOf(UsersDto.class)))
		.andExpect(status().isOk());
	}

	@Test
	public void shouldRejectChangingPassword_whenPasswordNotValid() throws Exception {
		//given		
		when(resetPasswordService.changePassword(any(), any())).thenCallRealMethod();
		//when
		mockMvc.perform(post("/change-password").param("password", "P").flashAttr("user", UsersDtoBuilder.get()))
		//then		
		.andExpect(view().name("resetPassword"))
		.andExpect(model().attribute("validLink", is(true)))		
		.andExpect(status().isOk());
	}
	@Test
	public void shouldChangePasswordAndDeleteToken_whenPasswordValid() throws Exception {
		//given		
		when(resetPasswordService.changePassword(any(), any())).thenCallRealMethod();
		//when
		mockMvc.perform(post("/change-password").flashAttr("user", UsersDtoBuilder.get()))
		//then		
		.andExpect(view().name("info"))
		.andExpect(model().attribute("info", is("Your password is changed, you can now login with new password.")))		
		.andExpect(status().isOk());

		verify(resetPasswordService, times(1)).saveUserWithChangedPassword(any());
		verify(resetPasswordService, times(1)).deleteToken("user");
	}
}
