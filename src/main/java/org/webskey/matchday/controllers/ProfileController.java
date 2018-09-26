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
import org.webskey.matchday.dto.UsersDto;
import org.webskey.matchday.services.ProfileService;

@Controller
public class ProfileController {

	@Autowired
	private ProfileService profileService;
	
	@RequestMapping("/profile/myprofile")
	public ModelAndView profile(Principal princ) {
		return new ModelAndView("profile", "user", profileService.getUsersDetails(princ.getName()));
	}
	
	/*@PostMapping("/change-profile-details")
	public ModelAndView changeProfileDetails(@Valid @ModelAttribute("user") UsersDto usersDto, BindingResult bindingResult) {
		return profileService.change(usersDto, bindingResult);
	}
	
	@PostMapping("/profile/change-password")
	public ModelAndView changePassword(@Valid @ModelAttribute("user") UsersDto usersDto, BindingResult bindingResult) {
		return profileService.changePassword(usersDto, bindingResult);
	}*/
}
