package org.webskey.matchday.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.webskey.matchday.dto.UsersDto;
import org.webskey.matchday.services.ResetPasswordService;

@Controller
public class ForgottenPasswordController {

	@Autowired
	private ResetPasswordService resetPasswordService;

	@RequestMapping(value = "/forgotten-password", method = RequestMethod.GET)
	public ModelAndView forgottenPassword() {
		return new ModelAndView("forgottenPassword", "user", new UsersDto());
	}

	@RequestMapping(value = "/forgotten-pass", method = RequestMethod.POST)
	public ModelAndView forgottenPass(@Valid @ModelAttribute("user") UsersDto usersDto, BindingResult bindingResult, HttpServletRequest request) {
		return resetPasswordService.sendEmail(bindingResult, usersDto, request.getRequestURL().toString());
	}

	@RequestMapping(value = "/forgotten-pass/{username}/{token}", method = RequestMethod.GET)
	public ModelAndView resetPassword(@PathVariable("username") String username, @PathVariable("token") String token) {
		return resetPasswordService.checkLink(username, token);
	}

	@RequestMapping(value = "/change-password", method = RequestMethod.POST)
	public ModelAndView changePassword(@Valid @ModelAttribute("user") UsersDto usersDto, BindingResult bindingResult) {
		return resetPasswordService.changePassword(usersDto, bindingResult);
	}
}
