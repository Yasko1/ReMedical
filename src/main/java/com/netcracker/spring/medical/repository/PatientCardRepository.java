package com.netcracker.spring.medical.repository;

import com.netcracker.spring.medical.entity.PatientCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientCardRepository extends CrudRepository<PatientCard, Long> {

    PatientCard save(PatientCard card);

    PatientCard findPatientCardByUser_Id(Long id);
}
