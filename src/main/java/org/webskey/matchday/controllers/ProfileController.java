package org.webskey.matchday.controllers;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.webskey.matchday.dto.PasswordDto;
import org.webskey.matchday.services.ChangePasswordService;
import org.webskey.matchday.services.ProfileService;

@Controller
public class ProfileController {

	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private ChangePasswordService changePasswordService;

	@RequestMapping("/profile")
	public ModelAndView profile(Principal principal) {
		return new ModelAndView("profile", "user", profileService.getUsersDetails(principal.getName()));
	}

	@RequestMapping("/profile/change-password")
	public ModelAndView changePassword() {		
		return new ModelAndView("changePassword", "passwordDto", new PasswordDto());
	}

	@PostMapping("/profile/change-password")
	public ModelAndView changePassword(@Valid @ModelAttribute("passwordDto") PasswordDto passwordDto, BindingResult bindingResult, Principal principal) {
		return changePasswordService.changePassword(passwordDto, bindingResult, principal.getName());		
	}
}
