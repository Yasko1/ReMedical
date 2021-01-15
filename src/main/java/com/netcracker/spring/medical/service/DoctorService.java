package com.netcracker.spring.medical.service;

import com.netcracker.spring.medical.entity.Admin;
import com.netcracker.spring.medical.entity.Doctor;
import com.netcracker.spring.medical.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    DoctorRepository doctorRepository;

    public void save(Doctor doctor){
        doctorRepository.save(doctor);
    }

    public Doctor findByEmail(String user) {
        return doctorRepository.findByEmail(user);

    }

    public List<Doctor> findByRole(String user) {
        return doctorRepository.findByRole(user);
    }
}
