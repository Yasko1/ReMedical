package com.netcracker.spring.medical.repository;

import com.netcracker.spring.medical.entity.Medicines;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicinesRepository extends JpaRepository<Medicines, Long> {

    Medicines findMedicinesById(Long id);

    Medicines findMedicinesByName(String name);
}
