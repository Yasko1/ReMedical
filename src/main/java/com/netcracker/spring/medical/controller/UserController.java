package com.netcracker.spring.medical.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.netcracker.spring.medical.entity.*;
import com.netcracker.spring.medical.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.websocket.server.PathParam;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DepartamentService departamentService;

    @Autowired
    private EmailService emailService;

    @Autowired
    PatientCardService patientCardService;

    @GetMapping("/index")
    public String index(Model model) {
        return "user/index";
    }

    @GetMapping("/blog")
    public String blog(Model model) {
        return "user/blog";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        return "user/contact";
    }

    @PostMapping("/contact")
    public String contact(@ModelAttribute("senderName") String senderName,
                          @ModelAttribute("senderEmail") String senderEmail,
                          @ModelAttribute("subject") String subject,
                          @ModelAttribute("message") String message,
                          Model model) {
        setLastSeen();
        SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo("annamedical0@gmail.com");
        registrationEmail.setSubject("QUESTION FROM USERS: " + subject);
        registrationEmail.setText(message);
        registrationEmail.setFrom(senderEmail);
        emailService.sendEmail(registrationEmail);

        model.addAttribute("confirmationMessage", "Your message was sent.  The administrator will contact you shortly");
        return "user/contact";
    }

    @GetMapping("/departments")
    public String getAllDepartaments(Model model) {
        model.addAttribute("departaments", departamentService.getAll());
        return "user/departments";
    }

    @GetMapping("/departaments/{departament}")
    public String getDepartament(Model model, @ModelAttribute("departament") Departament departament) {
        model.addAttribute("departament", departamentService.getDepartamentById(departament.getId()));
        model.addAttribute("doctors", departamentService.findDoctors(departament.getId()));
        Appointment app = new Appointment();
        model.addAttribute("app", app);
        return "user/departament";
    }

    @GetMapping("/save-doctor")
    public String getAppRegPage(@ModelAttribute("app") Appointment app, Model model) {
        model.addAttribute("app", app);
        return "user/appointmentReg";
    }

    @PostMapping("/save-app")
    public String saveApp(@ModelAttribute("app") Appointment app, Model model) {

        //checking that patient card for this user exist
        //if not : creating it
        if (findPatientCard() == null) {
            PatientCard card = new PatientCard();
            card.setUser(getUser());
            card.setGenderEnum(GenderEnum.UNKNOWN);
            patientCardService.save(card);
        }

        //checking that date isn't later than today
        Date now = new Date();
        if (app.getDate().before(now)) {
            model.addAttribute("aMessage", "Chose date correctly");
            return getAppRegPage(app, model);
        }

        app.setPatientCard(findPatientCard());
        app.setRegtime(now);
        app.setStatus(AppointmentStatus.OPEN.getValue());
        appointmentService.save(app);
        return "redirect:/user/appointments";
    }

    @GetMapping("/appointments")
    public String myApp(Model model) {
        List<Appointment> openlist = new ArrayList<>();
        List<Appointment> closelist = new ArrayList<>();
        List<Appointment> allAppointments = appointmentService
                .findAppointmentsByPatientCardId(findPatientCard().getId());
        for (Appointment a : allAppointments) {
            if (a.getStatus().equals("OPEN")) {
                openlist.add(a);
            } else if (a.getStatus().equals("CLOSE")) {
                closelist.add(a);
            }
        }
        model.addAttribute("apps", openlist);
        model.addAttribute("appsclose", closelist);
        return "user/appointments";
    }

    private User getUser() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userService.findByEmail(username);
        } else {
            String username = principal.toString();
            return userService.findByEmail(username);
        }
    }

    private void setLastSeen() {
        User user = userService.findByEmail(getUser().getEmail());
        Date now = new Date();
        user.setLastseen(now);
        userService.save(user);

    }

    private PatientCard findPatientCard() {
        return patientCardService.findPatientCardByUser(getUser().getId());
    }
}