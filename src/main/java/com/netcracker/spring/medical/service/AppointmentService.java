package com.netcracker.spring.medical.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.netcracker.spring.medical.entity.Appointment;
import com.netcracker.spring.medical.repository.AppointmentRepository;

@Service
public class AppointmentService {

	@Qualifier("appointmentRepository")
	@Autowired
	private AppointmentRepository appointmentRepository;

	public void save(Appointment app){
		appointmentRepository.save(app);
	}

	public List<Appointment> findAll() {
		return appointmentRepository.findAll();
	}

	public List<Appointment> findAllByDoctorId(Long doctorId) {
		return appointmentRepository.findAllByDoctorId(doctorId);
	}

	public Appointment findById(Long id){
		return appointmentRepository.findAppointmentById(id);
	}

	public List<Appointment> findAppointmentsByPatientCardId(Long id){
		return appointmentRepository.findAppointmentsByPatientCardId(id);
	}

}
