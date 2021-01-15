package com.netcracker.spring.medical.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Async
	public void sendEmail(SimpleMailMessage email) {
		System.out.println("MAIL SENDER ");
		System.out.println(email.toString());
		try{
			mailSender.send(email);
		} catch (Exception e){
			e.printStackTrace();
		}
		System.out.println("sent");
	}
}
