package com.netcracker.spring.medical.service;

import com.netcracker.spring.medical.entity.Departament;
import com.netcracker.spring.medical.entity.Doctor;
import com.netcracker.spring.medical.repository.DepartamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentService {

    @Autowired
    DepartamentRepository departamentRepository;

    public List<Departament> getAll() {
        return departamentRepository.findAll();
    }

    public Departament getDepartamentById(Long id) {
        return departamentRepository.findDepartamentById(id);
    }

    public List<Doctor> findDoctors(Long id){
        return departamentRepository.findDoctorsByDepartamentId(id);
    }

}
