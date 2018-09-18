package org.webskey.matchday.controllers;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private ResetPasswordService resetPasswordService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	@RequestMapping(value = "/login-error", method = RequestMethod.GET)
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return "login";
	}

	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){    
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accesssDenied(Principal principal) {
		logger.warn("User: " + principal.getName() + " was trying to get acces restricted page");
		return "403";
	}
	
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

		model.addAttribute("passReset", true);
		
		return "forgottenPassword";
	}
	
	@RequestMapping(value = "/forgotten-pass/{username}/{token}", method = RequestMethod.GET)
	public String resetPassword(@PathVariable("username") String username, @PathVariable("token") String token, Model model) {
		
		model.addAttribute("validLink", resetPasswordService.checkLink(username, token));
				
		return "resetPassword";
	}
}
