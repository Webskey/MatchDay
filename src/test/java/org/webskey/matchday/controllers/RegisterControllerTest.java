package org.webskey.matchday.controllers;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import org.webskey.matchday.dto.UsersDto;
import org.webskey.matchday.services.RegisterService;

@RunWith(SpringJUnit4ClassRunner.class)
public class RegisterControllerTest {

	@Mock
	private RegisterService registerService;

	@InjectMocks
	private RegisterController registerController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("classpath:templates/");
		viewResolver.setSuffix(".html");

		this.mockMvc = MockMvcBuilders.standaloneSetup(registerController)	
				.setViewResolvers(viewResolver)
				.build();
	}

	@Test
	public void shouldReturnRegisterView_whenCalledRegisterPage() throws Exception {	
		//when
		mockMvc.perform(get("/register"))
		//then
		.andExpect(status().isOk())
		.andExpect(model().attribute("user", instanceOf(UsersDto.class)))
		.andExpect(view().name("register"));
	}

	@Test
	public void shouldRejectRegistration_whenUsernameNotValidated() throws Exception {
		//given
		when(registerService.register(any(), any())).thenReturn(new ModelAndView("register"));
		//when
		mockMvc.perform(post("/reg").param("username", "F").flashAttr("user", UsersDtoBuilder.get()))
		//then
		.andExpect(model().attributeHasFieldErrors("user", "username"))
		.andExpect(model().attribute("user", hasProperty("password", is("password"))))
		.andExpect(view().name("register"))
		.andExpect(status().isOk());
	}

	@Test
	public void shouldRejectRegistration_whenPasswordNotValidated() throws Exception {
		//given
		when(registerService.register(any(), any())).thenReturn(new ModelAndView("register"));
		//when
		mockMvc.perform(post("/reg").param("password", "F").flashAttr("user", UsersDtoBuilder.get()))
		//then
		.andExpect(model().attributeHasFieldErrors("user", "password"))
		.andExpect(model().attribute("user", hasProperty("username", is("user"))))
		.andExpect(view().name("register"))
		.andExpect(status().isOk());
	}

	@Test
	public void shouldRejectRegistration_whenEmailNotValidated() throws Exception {
		//given
		when(registerService.register(any(), any())).thenReturn(new ModelAndView("register"));
		//when
		mockMvc.perform(post("/reg").param("email", "F").flashAttr("user", UsersDtoBuilder.get()))
		//then
		.andExpect(model().attributeHasFieldErrors("user", "email"))
		.andExpect(model().attribute("user", hasProperty("password", is("password"))))
		.andExpect(view().name("register"))
		.andExpect(status().isOk());
	}

	@Test
	public void shouldRejectRegistration_whenEmailAlreadyExists() throws Exception {
		//given
		when(registerService.isEmailExisting(any())).thenReturn(true);
		when(registerService.register(any(), any())).thenCallRealMethod();
		//when
		mockMvc.perform(post("/reg").flashAttr("user", UsersDtoBuilder.get()))
		//then
		.andExpect(model().attributeHasErrors("user"))
		.andExpect(model().attribute("user", hasProperty("password", is("password"))))
		.andExpect(view().name("register"))
		.andExpect(status().isOk());
	}

	@Test
	public void shouldRejectRegistration_whenUsernameAlreadyExists() throws Exception {
		//given
		when(registerService.isUsernameExisting(any())).thenReturn(true);
		when(registerService.register(any(), any())).thenCallRealMethod();
		//when
		mockMvc.perform(post("/reg").flashAttr("user", UsersDtoBuilder.get()))
		//then
		.andExpect(model().attributeHasErrors("user"))
		.andExpect(model().attribute("user", hasProperty("password", is("password"))))
		.andExpect(view().name("register"))
		.andExpect(status().isOk());
	}

	@Test
	public void shouldPassRegistration_whenNoErrors() throws Exception {
		//given		
		when(registerService.register(any(), any())).thenReturn(new ModelAndView("info").addObject("info", "Succesfull user registration"));
		//when
		mockMvc.perform(post("/reg").flashAttr("user", UsersDtoBuilder.get()))
		//then
		.andExpect(model().attributeHasNoErrors("user"))
		.andExpect(model().attribute("user", hasProperty("username", is("user"))))
		.andExpect(model().attribute("user", hasProperty("password", is("password"))))
		.andExpect(model().attribute("info", is("Succesfull user registration")))
		.andExpect(view().name("info"))
		.andExpect(status().isOk());
	}
}
