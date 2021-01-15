package com.netcracker.spring.medical.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

	@GetMapping("/loginPage")
	public String showHome() {
		return "login";
	}

}
