package com.netcracker.spring.medical.service;

import com.netcracker.spring.medical.entity.Medicines;
import com.netcracker.spring.medical.repository.MedicinesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicinesService {

    @Autowired
    MedicinesRepository medicinesRepository;

    public List<Medicines> findAllMedicines(){
        return medicinesRepository.findAll();
    }

    public Medicines findMedicineById(Long id){
        return medicinesRepository.findMedicinesById(id);
    }

    public Medicines findMedicineByName(String name){
        return medicinesRepository.findMedicinesByName(name);
    }

    public Medicines save(Medicines medicines){
        return medicinesRepository.save(medicines);
    }
}
