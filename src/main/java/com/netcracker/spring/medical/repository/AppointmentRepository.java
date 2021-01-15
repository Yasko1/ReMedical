package com.netcracker.spring.medical.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.netcracker.spring.medical.entity.Appointment;

import java.util.List;

@Repository("appointmentRepository")
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Appointment findAppointmentById(Long id);

    List<Appointment> findAllByDoctorId(Long doctorId);

    List<Appointment> findAppointmentsByPatientCardId(Long id);
}