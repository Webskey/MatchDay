package org.webskey.matchday.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.webskey.matchday.dto.UsersDto;
import org.webskey.matchday.services.ResetPasswordService;

@Controller
public class ForgottenPasswordController {

	@Autowired
	private ResetPasswordService resetPasswordService;

	@RequestMapping(value = "/forgotten-password", method = RequestMethod.GET)
	public String forgottenPassword(Model model) {
		model.addAttribute("user", new UsersDto());
		return "forgottenPassword";
	}

	@RequestMapping(value = "/forgotten-pass", method = RequestMethod.POST)
	public String forgottenPass(@Valid @ModelAttribute("user") UsersDto usersDto, BindingResult bindingResult, Model model, HttpServletRequest request) {
		if(bindingResult.hasFieldErrors("email"))
			return "forgottenPassword";
		try {
			resetPasswordService.resetPassword(usersDto, request.getRequestURL().toString());
		} catch (ArithmeticException e) {
			bindingResult.rejectValue("email", "NoSuchEmail", "There is no user with that email");
			return "forgottenPassword";
		}

		model.addAttribute("info", "Email with futher instructions has been sent");

		return "info";
	}

	@RequestMapping(value = "/forgotten-pass/{username}/{token}", method = RequestMethod.GET)
	public String resetPassword(@PathVariable("username") String username, @PathVariable("token") String token, Model model) {

		if(resetPasswordService.checkLink(username, token).equals("ok")) {
			model.addAttribute("validLink", true);
			model.addAttribute("user", new UsersDto());
		} else {
			model.addAttribute("error", resetPasswordService.checkLink(username, token));
		}

		return "resetPassword";
	}

	@RequestMapping(value = "/change-password", method = RequestMethod.POST)
	public String changePassword(@Valid @ModelAttribute("user") UsersDto usersDto, BindingResult bindingResult, Model model) {
		if(bindingResult.hasFieldErrors("password")) {
			model.addAttribute("validLink", true);
			return "resetPassword";
		}

		model.addAttribute("info", "Your password is changed, you can now login with new password.");		
		resetPasswordService.changePassword(usersDto);
		return "info";
	}
}
