package org.webskey.matchday.controllers;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.webskey.matchday.Main;
import org.webskey.matchday.builders.UsersDtoBuilder;
import org.webskey.matchday.dto.UsersDto;
import org.webskey.matchday.services.ProfileService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {Main.class})
@TestPropertySource(locations = "classpath:application.properties")
public class ProfileControllerTest {

	@Mock
	private ProfileService profileService;

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
	public void shouldThrow_whenUserNotLogged() throws Exception {
		//when
		mockMvc.perform(get("/profile"))
		//then
		.andExpect(status().isFound())
		.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	public void shouldThrow_whenUserogged() throws Exception {
		String username = "user";
		when(profileService.getUsersDetails(username)).thenReturn(UsersDtoBuilder.get());
		//when
		mockMvc.perform(get("/profile").with(user(username).password("pass").roles("USER")))
		//then
		.andExpect(status().isOk())	
		.andExpect(model().attribute("user", instanceOf(UsersDto.class)))
		.andExpect(model().attribute("user", hasProperty("username", is("user"))))
		.andExpect(view().name("profile"));
	}
}
