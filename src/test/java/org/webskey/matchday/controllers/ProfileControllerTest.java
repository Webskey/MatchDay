package org.webskey.matchday.controllers;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.webskey.matchday.Main;
import org.webskey.matchday.builders.PasswordDtoBuilder;
import org.webskey.matchday.dto.PasswordDto;
import org.webskey.matchday.dto.UsersDto;
import org.webskey.matchday.services.ChangePasswordService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {Main.class})
@TestPropertySource(locations = "classpath:application.properties")
public class ProfileControllerTest {

	@MockBean
	private ChangePasswordService changePasswordService;

	@InjectMocks
	private ProfileController profileController;

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build();
	}

	@Test
	public void shouldReturnLoginPage_whenCalledProfileNotLogged() throws Exception {
		//when
		mockMvc.perform(get("/profile"))
		//then
		.andExpect(status().isFound())
		.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	public void shouldReturnProfileViewWithUserDetails_whenUserLogged() throws Exception {
		//given
		String username = "user";
		//when
		mockMvc.perform(get("/profile").with(user(username).password("pass").roles("USER")))
		//then
		.andExpect(status().isOk())	
		.andExpect(model().attribute("user", instanceOf(UsersDto.class)))
		.andExpect(model().attribute("user", hasProperty("username", is("user"))))
		.andExpect(view().name("profile"));
	}

	@Test
	public void shouldReturnLoginPage_whenCalledChangePasswordNotLogged() throws Exception {
		//when
		mockMvc.perform(get("/profile/change-password"))
		//then
		.andExpect(status().isFound())
		.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	public void shouldReturnChangePasswordWithPasswordDtoAttribute_whenUserLogged() throws Exception {
		//given
		when(changePasswordService.changePassword(any(), any(), any())).thenCallRealMethod();
		//when
		mockMvc.perform(post("/profile/change-password").with(user("user").password("pass").roles("USER")))
		//then
		.andExpect(status().isOk())	
		.andExpect(model().attribute("passwordDto", instanceOf(PasswordDto.class)))
		.andExpect(view().name("changePassword"));
	}

	@Test
	public void shouldRejectChangingPassword_whenPasswordNotValid() throws Exception {
		//given
		when(changePasswordService.changePassword(any(), any(), any())).thenCallRealMethod();
		//when
		mockMvc.perform(post("/profile/change-password").with(user("user").password("pass").roles("USER"))
				.param("password", "F").flashAttr("passwordDto", PasswordDtoBuilder.get()))
		//then
		.andExpect(status().isOk())	
		.andExpect(model().attributeHasFieldErrors("passwordDto", "password"))
		.andExpect(model().attribute("passwordDto", instanceOf(PasswordDto.class)))
		.andExpect(view().name("changePassword"));
	}

	@Test
	public void shouldRejectChangingPassword_whenConfirmPasswordNotValid() throws Exception {
		//given
		when(changePasswordService.changePassword(any(), any(), any())).thenCallRealMethod();
		//when
		mockMvc.perform(post("/profile/change-password").with(user("user").password("pass").roles("USER"))
				.param("confirmPassword", "F").flashAttr("passwordDto", PasswordDtoBuilder.get()))
		//then
		.andExpect(status().isOk())	
		.andExpect(model().attributeHasFieldErrors("passwordDto", "confirmPassword"))
		.andExpect(model().attribute("passwordDto", instanceOf(PasswordDto.class)))
		.andExpect(view().name("changePassword"));
	}

	@Test
	public void shouldRejectChangingPassword_whenOldPasswordNotValid() throws Exception {
		//given
		when(changePasswordService.changePassword(any(), any(), any())).thenCallRealMethod();
		//when
		mockMvc.perform(post("/profile/change-password").with(user("user").password("pass").roles("USER"))
				.param("oldPassword", "F").flashAttr("passwordDto", PasswordDtoBuilder.get()))
		//then
		.andExpect(status().isOk())	
		.andExpect(model().attributeHasFieldErrors("passwordDto", "oldPassword"))
		.andExpect(model().attribute("passwordDto", instanceOf(PasswordDto.class)))
		.andExpect(view().name("changePassword"));
	}

	@Test
	public void shouldRejectChangingPassword_whenPasswordAndConfirmPasswordNotMatching() throws Exception {
		//given
		when(changePasswordService.changePassword(any(), any(), any())).thenCallRealMethod();
		//when
		mockMvc.perform(post("/profile/change-password").with(user("user").password("pass").roles("USER"))
				.param("confirmPassword", "NotMatching").flashAttr("passwordDto", PasswordDtoBuilder.get()))
		//then
		.andExpect(status().isOk())	
		.andExpect(model().attributeHasFieldErrors("passwordDto", "confirmPassword"))
		.andExpect(model().attribute("passwordDto", instanceOf(PasswordDto.class)))
		.andExpect(view().name("changePassword"));
	}

	@Test
	public void shouldRejectChangingPassword_whenOldPasswordIncorrect() throws Exception {
		//given
		when(changePasswordService.isOldPasswordCorrect(any(), any())).thenReturn(false);
		when(changePasswordService.changePassword(any(), any(), any())).thenCallRealMethod();
		//when
		mockMvc.perform(post("/profile/change-password").with(user("user").password("pass").roles("USER"))
				.flashAttr("passwordDto", PasswordDtoBuilder.get()))
		//then
		.andExpect(status().isOk())	
		.andExpect(model().attributeHasFieldErrors("passwordDto", "oldPassword"))
		.andExpect(model().attribute("passwordDto", instanceOf(PasswordDto.class)))
		.andExpect(view().name("changePassword"));
	}

	@Test
	public void shouldChangePassword_whenInputValidAndCorrect() throws Exception {
		//given
		when(changePasswordService.isOldPasswordCorrect(any(), any())).thenReturn(true);
		when(changePasswordService.changePassword(any(), any(), any())).thenCallRealMethod();
		//when
		mockMvc.perform(post("/profile/change-password").with(user("user").password("pass").roles("USER"))
				.flashAttr("passwordDto", PasswordDtoBuilder.get()))
		//then
		.andExpect(status().isOk())	
		.andExpect(model().attributeHasNoErrors("passwordDto"))
		.andExpect(model().attribute("info", is("Password changed")))
		.andExpect(view().name("info"));

		verify(changePasswordService, times(1)).saveNewPassword("user", "passwordNew");
	}
}
