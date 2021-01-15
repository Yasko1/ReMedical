package com.netcracker.spring.medical.service;

import com.netcracker.spring.medical.entity.PatientCard;
import com.netcracker.spring.medical.repository.PatientCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientCardService {

    @Autowired
    PatientCardRepository cardRepository;

    public void save(PatientCard card){
        try {
            cardRepository.save(card);
        } catch (Exception e) {
            e.printStackTrace(); //todo logger here
        }
    }

    public PatientCard findPatientCardByUser(Long id){

        return cardRepository.findPatientCardByUser_Id(id);
    }
}
