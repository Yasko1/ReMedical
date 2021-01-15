package com.netcracker.spring.medical.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.netcracker.spring.medical.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	
	User findByEmail(String email);
	
	User findByConfirmationToken(String confirmationToken);
	 
	List<User> findAll();

	User save(User user);
}