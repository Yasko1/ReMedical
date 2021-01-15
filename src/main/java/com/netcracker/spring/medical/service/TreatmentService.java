package com.netcracker.spring.medical.service;

import com.netcracker.spring.medical.entity.Medicines;
import com.netcracker.spring.medical.entity.Treatment;
import com.netcracker.spring.medical.repository.MedicinesRepository;
import com.netcracker.spring.medical.repository.TreatmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreatmentService {

    @Autowired
    TreatmentRepository  treatmentRepository;

    public List<Treatment> findAll() {
        return treatmentRepository.findAll();
    }

    public void save(Treatment t) {
        treatmentRepository.save(t);
    }

    public Treatment findById(Long id){
        return treatmentRepository.findTreatmentById(id);
    }

}
