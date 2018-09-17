package org.webskey.matchday.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller	
public class HomeController {

	@RequestMapping("/")
	public String home() {	
		return "home";
	}

	@RequestMapping("/admin")
	public String acces() {		
		return "admin";
	}
}