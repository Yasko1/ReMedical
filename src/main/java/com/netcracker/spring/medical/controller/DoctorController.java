package com.netcracker.spring.medical.controller;

import java.util.Date;
import java.util.List;

import com.netcracker.spring.medical.entity.*;
import com.netcracker.spring.medical.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private TreatmentService treatmentService;

    @Autowired
    private MedicinesService medicinesService;

    @Autowired
    private TherapyService therapyService;

    @Autowired
    private PatientCardService patientCardService;

    @RequestMapping("/index")
    public String index(Model model) {
        setLastSeen();
        List<Appointment> list = appointmentService.findAllByDoctorId(getDoctor().getId());
        model.addAttribute("name", getDoctor().getFirstName());
        model.addAttribute("email", getDoctor().getEmail());
        model.addAttribute("user", getDoctor().getFullName());
        model.addAttribute("app", list);
        model.addAttribute("statuses", AppointmentStatus.getAll());

        Appointment a = new Appointment();
        model.addAttribute("patient_app", a);

        return "doctor/index";
    }

    @GetMapping("/appointment/{id}")
    public String getAppointment(@PathVariable("id") Long id, Model model) {
        model.addAttribute("name", getDoctor().getFirstName());
        model.addAttribute("email", getDoctor().getEmail());
        model.addAttribute("app", appointmentService.findById(id));
        model.addAttribute("statuses", AppointmentStatus.getAll());
        model.addAttribute("status", "");
        model.addAttribute("medPill", "");
        model.addAttribute("medicines", medicinesService.findAllMedicines());

        return "doctor/appointment";
    }

    @PostMapping("/appointment/{id}/save-app")
    public String saveAppoint(@ModelAttribute("app") Appointment app,
                              @PathVariable("id") Long id) {
        Appointment a = appointmentService.findById(id);
        Treatment t = treatmentService.findById(a.getTreatment().getId());
        if (!app.getTreatment().getDescription().equals(null)) {
            t.setDescription(app.getTreatment().getDescription());
        } else {
            t.setDescription("");
        }
        treatmentService.save(t);
        a.setDescription(app.getDescription());
        a.setStatus(AppointmentStatus.CLOSE.getValue());
        appointmentService.save(a);
        return "redirect:/doctor/appointment/" + app.getId();
    }

    @GetMapping("/appointment/{id}/therapy")
    public String saveTherapy(@ModelAttribute("app") Appointment app, @PathVariable("id") Long id, Model model) {

        Appointment a = appointmentService.findById(id);
        if (app.getDescription() != null)
            a.setDescription(app.getDescription());
        appointmentService.save(a);
        model.addAttribute("therapy", "");
        model.addAttribute("therapies", therapyService.findAll());
        return "doctor/therapy";
    }

    @PostMapping("/appointment/{id}/therapy")
    public String saveTherapy(@PathVariable("id") Long id, @ModelAttribute("therapy") String therapy) {

        Appointment a = appointmentService.findById(id);
        Treatment treatment = treatmentService.findById(a.getTreatment().getId());

        List<Therapy> therapies = therapyService.findAll();
        for (Therapy t : therapies) {
            if (t.getName().equals(therapy)) {
                treatment.addTherapies(t);
                treatmentService.save(treatment);
                a.setTreatment(treatment);
                appointmentService.save(a);
            }
        }
        return "redirect:/doctor/appointment/" + id;
    }


    @PostMapping("appointment/{id}/changeStatus")
    public String changeAppStatus(@ModelAttribute("status") String s,
                                  @PathVariable("id") Long id,
                                  Model model) {
        Appointment a = appointmentService.findById(id);
        if (a.getStatus().equals(AppointmentStatus.OPEN.getValue())) {
            if (s.equals("CLOSE")) {
                model.addAttribute("aMessage", "You cant close before you come to doctor or not come.");
                return getAppointment(id, model);
            }
        } else if (a.getStatus().equals(AppointmentStatus.CLOSE.getValue())) {
            if (s.equals("OPEN")) {
                a.setTreatment(null);
                a.setStatus(s);
                appointmentService.save(a);
            }
        }
        if (s.equals("AT THE DOCTOR")) {
            Treatment treatment = new Treatment();
            treatmentService.save(treatment);
            a.setTreatment(treatment);
            a.setStatus(s);
            appointmentService.save(a);
            return "redirect:/doctor/appointment/" + id;
        } else {
            a.setStatus(s);
            appointmentService.save(a);
            return "redirect:/doctor/appointment/" + id;
        }
    }

    @PostMapping("appointment/{id}/addmedpills")
    public String addMedicines(@ModelAttribute("medPill") String medicines,
                               @PathVariable("id") Long id) {

        Appointment a = appointmentService.findById(id);
        Treatment t = treatmentService.findById(a.getTreatment().getId());

        if (a.getTreatment().getId() != null) {
            List<Medicines> med = medicinesService.findAllMedicines();
            for (Medicines m : med) {
                if (medicines.equals(m.getName())) {
                    t.addMedicines(m);
                    treatmentService.save(t);
                    a.setTreatment(t);
                    appointmentService.save(a);
                } else {
                    //todo вывести уведомление об ошибке
                }
            }
        }
        return "redirect:/doctor/appointment/" + id;
    }

    @GetMapping("/patient/{id}")
    public String getPatientCard(@PathVariable("id") Long id, Model model) {
        model.addAttribute("name", getDoctor().getFirstName());
        model.addAttribute("email", getDoctor().getEmail());
        model.addAttribute("card", findPC(id));
        model.addAttribute("genders", GenderEnum.getAll());
        return "doctor/patient";
    }

    @PostMapping("/patient/{id}")
    public String setPatientCard(@PathVariable("id") Long id,
                                 @ModelAttribute("card") PatientCard card) {

        PatientCard patientCard = findPC(id);
        patientCard.setBirthday(card.getBirthday());
        patientCard.setGenderEnum(card.getGenderEnum());
        patientCard.setHeight(card.getHeight());
        patientCard.setWeight(card.getWeight());
        patientCardService.save(patientCard);
        return "redirect:/doctor/patient/" + patientCard.getUser().getId();
    }

    @PostMapping("/patient/{id}/save-app")
    public String saveApp(@ModelAttribute("app") Appointment app,
                          @PathVariable("id") Long id) {
        Appointment appointment = new Appointment();
        PatientCard patientCard = findPC(id);
        appointment.setPatientCard(patientCard);
        Date now = new Date();
        appointment.setRegtime(now);
        appointment.setStatus(AppointmentStatus.OPEN.getValue());
        appointment.setDate(app.getDate());
        appointment.setTime(app.getTime());
        appointment.setComplaints(app.getComplaints());
        appointment.setDoctor(getDoctor());
        appointmentService.save(appointment);
        return "redirect:/doctor/index";
    }

    @GetMapping("/patient/{id}/add-app")
    public String addFutureApp(@PathVariable("id") Long id, Model model) {
        PatientCard patientCard = findPC(id);
        Appointment app = new Appointment();
        app.setPatientCard(patientCard);
        app.setDoctor(getDoctor());
        model.addAttribute("app", app);
        return "doctor/appointmentReg";
    }

    private Doctor getDoctor() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return doctorService.findByEmail(username);
        } else {
            String username = principal.toString();
            return doctorService.findByEmail(username);
        }
    }

    private void setLastSeen() {
        Doctor doctor = doctorService.findByEmail(getDoctor().getEmail());
        Date now = new Date();
        doctor.setLastseen(now);
        doctorService.save(doctor);
    }

    private PatientCard findPC(Long id) {
        return patientCardService.findPatientCardByUser(id);
    }
}
