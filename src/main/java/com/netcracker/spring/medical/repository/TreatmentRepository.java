package com.netcracker.spring.medical.repository;

import com.netcracker.spring.medical.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    Treatment findTreatmentById(Long id);

    Treatment save(Treatment t);

}
