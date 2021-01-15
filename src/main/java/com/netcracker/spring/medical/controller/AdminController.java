package com.netcracker.spring.medical.controller;

import java.util.Date;
import java.util.List;

import com.netcracker.spring.medical.entity.Doctor;
import com.netcracker.spring.medical.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.netcracker.spring.medical.entity.Admin;
import com.netcracker.spring.medical.entity.Appointment;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DepartamentService departamentService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/user-details")
    public String index(Model model) {
        setLastSeen();
        List<Admin> list = adminService.findByRole("ROLE_USER");
        model.addAttribute("user", list);
        return "admin/user";
    }

    @GetMapping("/doctor-details")
    public String doctorDetails(Model model) {
        setLastSeen();
        List<Doctor> list = doctorService.findByRole("ROLE_DOCTOR");
        model.addAttribute("user", list);
        return "admin/doctor";
    }

    @GetMapping("/admin-details")
    public String adminDetails(Model model) {
        setLastSeen();
        List<Admin> list = adminService.findByRole("ROLE_ADMIN");
        model.addAttribute("user", list);
        return "admin/admin";
    }

    @GetMapping("/add-doctor")
    public String showFormForDoctorAdd(Model model) {
        setLastSeen();
        Doctor doctor = new Doctor();
        model.addAttribute("doctor", doctor);
        model.addAttribute("departaments", departamentService.getAll());
        return "admin/addDoctor";
    }

    @PostMapping("/save-doctor")
    public String saveEmployee(@ModelAttribute("doctor") Doctor doctor,
                               Model model) {

        Doctor doctorExists = doctorService.findByEmail(doctor.getEmail());
        if (doctorExists != null) {
            model.addAttribute("alreadyRegisteredMessage", "Oops!  There is already a user registered with the email provided.");
            return showFormForDoctorAdd(model);
        } else {
            doctor.setRole("ROLE_DOCTOR");
            doctor.setEnabled(true);
            doctor.setConfirmationToken("ByAdmin-Panel");
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
            doctorService.save(doctor);
        }
        return "redirect:/admin/doctor-details";
    }

    @GetMapping("/add-admin")
    public String showFormForAdminAdd(Model theModel) {
        setLastSeen();
        Admin admin = new Admin();
        theModel.addAttribute("admin", admin);
        return "admin/addAdmin";
    }


    @PostMapping("/save-admin")
    public String saveEmploye(@ModelAttribute("admin") Admin admin,
                              Model model) {

        Admin adminExists = adminService.findByEmail(admin.getEmail());
        if (adminExists != null) {
            model.addAttribute("alreadyRegisteredMessage", "Oops!  There is already a user registered with the email provided.");
            return showFormForAdminAdd(model);
        } else {
            admin.setRole("ROLE_ADMIN");
            admin.setEnabled(true);
            admin.setConfirmationToken("ByAdmin-Panel");
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            adminService.save(admin);
            return "redirect:/admin/admin-details";
        }
    }

    @GetMapping("/edit-my-profile")
    public String EditForm(Model theModel) {
        setLastSeen();
        theModel.addAttribute("profile", getAdmin());
        return "admin/updateMyProfile";
    }


    @PostMapping("/update")
    public String update(@ModelAttribute("profile") Admin admin) {
        adminService.save(admin);
        return "redirect:/admin/admin-details";
    }

    @GetMapping("/appointments")
    public String appointments(Model model) {
        setLastSeen();
        model.addAttribute("apps", appointmentService.findAll());
        return "admin/appointments";
    }

    @GetMapping("/appointments/{id}/changetime")
    public String changeTimePage(@PathVariable Long id, Model model) {
        Appointment a = findApp(id);
        if (a.getStatus().equals("OPEN")) {
            model.addAttribute("app", a);
            return "admin/changeTime";
        } else {
            model.addAttribute("aMessage", "You cant change time and date if status of appointment is not 'open'");
            return appointments(model);
        }

    }

    @PostMapping("/appointments/{id}/changetime")
    public String changeTime(@PathVariable Long id, @ModelAttribute("app") Appointment appointment) {
        Appointment a = findApp(id);
        a.setTime(appointment.getTime());
        appointmentService.save(a);
        SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo(a.getPatientCard().getUser().getEmail());
        registrationEmail.setSubject("Changing Appointment");
        registrationEmail.setText("Dear " + a.getPatientCard().getUser().getFullName() +
                ", your time of attending doctor was changed. Doctor will waiting you at : " +
                a.getTime());
        registrationEmail.setFrom("spring.email.medical@gmail.com");
        emailService.sendEmail(registrationEmail);
        return "redirect:/admin/appointments";
    }

    @GetMapping("/appointments/{id}/changedate")
    public String changeDatePage(@PathVariable Long id, Model model) {
        model.addAttribute("app", findApp(id));
        return "admin/changeDate";
    }

    @PostMapping("/appointments/{id}/changedate")
    public String changeDate(@PathVariable Long id, @ModelAttribute("app") Appointment appointment) {
        Appointment a = findApp(id);
        a.setDate(appointment.getDate());
        appointmentService.save(a);
        SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo(a.getPatientCard().getUser().getEmail());
        registrationEmail.setSubject("Changing Appointment");
        registrationEmail.setText("Dear " + a.getPatientCard().getUser().getFullName() +
                ", your date of attending doctor was changed. Doctor will waiting you : " +
                a.getDate());
        registrationEmail.setFrom("spring.email.medical@gmail.com");
        emailService.sendEmail(registrationEmail);
        return "redirect:/admin/appointments";
    }

    private Admin getAdmin() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return adminService.findByEmail(username);
        } else {
            String username = principal.toString();
            return adminService.findByEmail(username);
        }
    }

    private void setLastSeen() {
        Admin admin = adminService.findByEmail(getAdmin().getEmail());
        Date now = new Date();
        admin.setLastseen(now);
        adminService.save(admin);
    }

    private Appointment findApp(Long id){
        return appointmentService.findById(id);
    }

}
