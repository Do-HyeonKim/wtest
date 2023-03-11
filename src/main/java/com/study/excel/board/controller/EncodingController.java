package com.study.excel.board.controller;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("test2")
@RequiredArgsConstructor
public class EncodingController {
	
	private final PasswordEncoder passwordEncoder;
	
	@PostMapping("encoding")
	  public void hashPassword() throws Exception {
		
	    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		    String password = "alalal";
		    System.out.println("!!!!!!!!!!!"+password);
		    password = encoder.encode(password);
		    System.out.println("???????????"+password);
		  }

}
