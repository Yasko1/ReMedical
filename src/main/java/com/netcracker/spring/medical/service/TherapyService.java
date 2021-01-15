package com.netcracker.spring.medical.service;

import com.netcracker.spring.medical.entity.Therapy;
import com.netcracker.spring.medical.repository.TherapyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TherapyService {

    @Autowired
    TherapyRepository therapyRepository;

    public Therapy save(Therapy therapy){
        return therapyRepository.save(therapy);
    }

    public List<Therapy> findAll(){
        return therapyRepository.findAll();
    }
}
