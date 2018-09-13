package org.webskey.matchday.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller	
public class HomeControler {

	@RequestMapping("/")
	public String home() {	
		return "home";
	}

	@RequestMapping("/acces")
	public String acces(Principal prrinc) {
		System.out.println(prrinc.getName());
		return "acces";
	}
}