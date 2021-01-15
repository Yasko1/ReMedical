package com.netcracker.spring.medical.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.netcracker.spring.medical.entity.Admin;
import com.netcracker.spring.medical.repository.AdminRepository;

@Service
public class AdminService  {

	@Qualifier("adminRepository")
	@Autowired
	private AdminRepository adminRepository;

	public List<Admin> findAll() {
		return adminRepository.findAll();
	}

	public void save(Admin admin) {
		adminRepository.save(admin);
	}

	public Admin findByEmail(String email) {
		return adminRepository.findByEmail(email);
	}

	public List<Admin> findByRole(String user) {
		return adminRepository.findByRole(user);
	}

	
}
