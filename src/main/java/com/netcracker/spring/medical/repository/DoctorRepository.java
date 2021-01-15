package com.netcracker.spring.medical.repository;

import com.netcracker.spring.medical.entity.Admin;
import com.netcracker.spring.medical.entity.Doctor;
import com.netcracker.spring.medical.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends CrudRepository<Doctor, Long> {

    Doctor findByEmail(String user);

    List<Doctor> findByRole(String user);
}
