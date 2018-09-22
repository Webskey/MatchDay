package org.webskey.matchday.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.webskey.matchday.Main;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {Main.class})
@TestPropertySource(locations = "classpath:application.properties")
public class LoginControllerTest {

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
	public void shouldReturn302_whenNotLogged() throws Exception {
		//given
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/admin");
		//when
		mockMvc.perform(builder)
		//then
		.andExpect(status().isFound())
		.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	public void shouldReturn403_whenAdminPageCalledWithUserRole() throws Exception {
		//given
		String username = "username";
		String password = "password";
		String role = "USER";
		//when
		mockMvc.perform(get("/admin").with(user(username).password(password).roles(role)))
		//then
		.andExpect(status().isForbidden())
		.andExpect(forwardedUrl("/403"));
	}

	@Test
	public void shouldReturn200_whenAdminPageCalledWithAdminRole() throws Exception {
		//given
		String username = "username";
		String password = "password";
		String roleUser = "USER";
		String roleAdmin = "ADMIN";
		//when
		mockMvc.perform(get("/admin").with(user(username).password(password).roles(roleUser, roleAdmin)))
		//then
		.andExpect(status().isOk())
		.andExpect(view().name("admin"));
	}

	@Test
	public void shouldLoginWithUserRole_whenLoggedAsUser() throws Exception {
		//given
		String username = "user";
		String password = "pass";
		//when
		this.mockMvc
		//then
		.perform(formLogin().user(username).password(password))
		.andExpect(status().isFound())
		.andExpect(authenticated().withRoles("USER").withUsername(username));
	}

	@Test
	public void shouldLoginWithAdminRole_whenLoggedAsAdmin() throws Exception {
		//given
		String username = "admin";
		String password = "boss";
		//when
		this.mockMvc
		.perform(formLogin().user(username).password(password))
		//then
		.andExpect(status().isFound())
		.andExpect(authenticated().withRoles("USER","ADMIN").withUsername(username));
	}

	@Test
	public void shouldFailLogin_whenWrongUsername() throws Exception {
		//given
		String username = "addmin";
		String password = "boss";
		//when
		this.mockMvc
		.perform(formLogin().user(username).password(password))
		//then
		.andExpect(redirectedUrl("/login-error"))
		.andExpect(unauthenticated());
	}

	@Test
	public void shouldFailLogin_whenWrongPassword() throws Exception {
		//given
		String username = "admin";
		String password = "bosss";
		//when
		this.mockMvc
		.perform(formLogin().user(username).password(password))
		//then
		.andExpect(redirectedUrl("/login-error"))
		.andExpect(unauthenticated());
	}

	@Test
	public void shouldRedirectToLogoutPage_whenLoggedOut() throws Exception {
		//given
		String username = "admin";
		String password = "boss";
		//when
		this.mockMvc
		.perform(formLogin().user(username).password(password));
		mockMvc
		.perform(logout())
		//then
		.andExpect(redirectedUrl("/login?logout"))
		.andExpect(unauthenticated())
		.andExpect(status().isFound());
	}
}
