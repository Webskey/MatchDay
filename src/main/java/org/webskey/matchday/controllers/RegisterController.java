package org.webskey.matchday.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.webskey.matchday.dto.UsersDto;
import org.webskey.matchday.services.RegisterService;

@RestController	
public class RegisterController {

	@Autowired
	private RegisterService registerService;

	@RequestMapping("/register")
	public ModelAndView register() {
		return new ModelAndView("register", "user", new UsersDto());
	}
	
	@PostMapping("/reg")
	public ModelAndView reg(@Valid @ModelAttribute("user") UsersDto usersDto, BindingResult bindingResult) {
		return registerService.register(usersDto, bindingResult);
	}
}
