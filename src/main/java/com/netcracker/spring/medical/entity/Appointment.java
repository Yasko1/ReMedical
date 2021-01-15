package com.netcracker.spring.medical.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;


import java.sql.Timestamp;

@Entity
@Table(name = "appointment")
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "date")
	private java.sql.Date date;
	
	@Column(name = "time")
	private String time;

	@Column(name = "description")
	private String description;

	@ManyToOne
	@JoinColumn(name = "doctor_id", referencedColumnName = "id")
	private Doctor doctor;

	@Column(name = "status")
	private String status;

	@Column(name = "complaints")
	private String complaints;

	@Column(name = "diagnosis")
	private String diagnosis;

	@OneToOne
	@JoinColumn(name = "treatment_id", referencedColumnName = "id")
	private Treatment treatment;

	@OneToOne
	@JoinColumn(name = "patient_card_id", referencedColumnName = "id")
	@NotNull
	private PatientCard patientCard;

	@Column(name = "regtime")
	@Transient
	private java.util.Date regtime;

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComplaints() {
		return complaints;
	}

	public void setComplaints(String complaints) {
		this.complaints = complaints;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public Treatment getTreatment() {
		return treatment;
	}

	public void setTreatment(Treatment treatment) {
		this.treatment = treatment;
	}

	public PatientCard getPatientCard() {
		return patientCard;
	}

	public void setPatientCard(PatientCard patientCard) {
		this.patientCard = patientCard;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public java.sql.Date getDate() {
		return date;
	}

	public void setDate(java.sql.Date date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public java.util.Date getRegtime() {
		return regtime;
	}

	public void setRegtime(java.util.Date regtime) {
		this.regtime = regtime;
	}
}