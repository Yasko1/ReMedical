package com.netcracker.spring.medical.repository;

import com.netcracker.spring.medical.entity.Departament;
import com.netcracker.spring.medical.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartamentRepository extends JpaRepository<Departament, Long> {

    Departament findDepartamentById(Long id);

    @Query("select d from Doctor d join d.departaments departament where departament.id = :departamentId")
    List<Doctor> findDoctorsByDepartamentId(@Param("departamentId") Long departamentId);
}
