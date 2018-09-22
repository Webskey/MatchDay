package org.webskey.matchday.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
public class HomeControllerTest {
	
	@InjectMocks
	private HomeController homeController;

	private MockMvc mockMvc;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
	}
	
	@Test
	public void shouldReturnHomeView_whenCalledHomePage() throws Exception {		
		mockMvc.perform(get("/"))
		.andExpect(status().isOk())
		.andExpect(view().name("home"));
	}
}
