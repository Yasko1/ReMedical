package com.netcracker.spring.medical.controller;

import com.netcracker.spring.medical.entity.GenderEnum;
import com.netcracker.spring.medical.entity.PatientCard;
import com.netcracker.spring.medical.entity.User;
import com.netcracker.spring.medical.service.EmailService;
import com.netcracker.spring.medical.service.PatientCardService;
import com.netcracker.spring.medical.service.UserService;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class RegistrationController {

    private static final Logger LOGGER = Logger.getLogger(RegistrationController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    PatientCardService patientCardService;

    @GetMapping(value = "/register")
    public ModelAndView showRegistrationPage(ModelAndView modelAndView, User user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @GetMapping(value = "/recovery")
    public ModelAndView showRecoveryPage(ModelAndView modelAndView, User user){
        modelAndView.addObject("user", user);
        modelAndView.setViewName("recover");
        return modelAndView;
    }

    @PostMapping(value = "/recovery")
    public ModelAndView recoverPassword(ModelAndView modelAndView,
                                        @Valid User user,
                                        BindingResult bindingResult,
                                        HttpServletRequest request){

        User userExists = userService.findByEmail(user.getEmail());
        if (userExists != null) {
                userExists.setEnabled(false);
                userExists.setConfirmationToken(UUID.randomUUID().toString()); // Generate random 36-character string token for confirmation link
                userExists.setPassword("password");
                try {
                    userExists = userService.save(userExists);
                }catch (Exception e){
                    LOGGER.info(e);
                }

                SimpleMailMessage recoverEmail = new SimpleMailMessage();
                recoverEmail.setTo(userExists.getEmail());
                recoverEmail.setSubject("Reset Password Confirmation");
                recoverEmail.setText("To confirm your e-mail address, please click the link below:\n"
                        + "http://161.35.98.191/confirm?token=" + userExists.getConfirmationToken());
                recoverEmail.setFrom("spring.email.medical@gmail.com");
                emailService.sendEmail(recoverEmail);

                modelAndView.addObject("confirmationMessage", "A confirmation e-mail has been sent to " + userExists.getEmail());
                modelAndView.setViewName("recover");
        } else {
            modelAndView.addObject("alreadyRegisteredMessage", "Oops!  There is not a user registered with this email.");
            modelAndView.setViewName("recover");
            bindingResult.reject("email");
        }
        return modelAndView;
    }

    @PostMapping(value = "/register")
    public ModelAndView processRegistrationForm(ModelAndView modelAndView,
                                                @Valid User user,
                                                BindingResult bindingResult,
                                                HttpServletRequest request) {

        User userExists = userService.findByEmail(user.getEmail());
        if (userExists != null) {
            modelAndView.addObject("alreadyRegisteredMessage", "Oops!  There is already a user registered with the email provided.");
            modelAndView.setViewName("register");
            bindingResult.reject("email");
        }

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("register");
        } else {
            user.setEnabled(false);
            user.setRole("ROLE_USER");
            user.setConfirmationToken(UUID.randomUUID().toString()); // Generate random 36-character string token for confirmation link
            user.setPassword("password");

            try {
                user = userService.save(user);
            }catch (Exception e){
                LOGGER.info(e);
            }

            SimpleMailMessage registrationEmail = new SimpleMailMessage();
            registrationEmail.setTo(user.getEmail());
            registrationEmail.setSubject("Registration Confirmation");
            registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
                    + "http://161.35.98.191/confirm?token=" + user.getConfirmationToken());
            registrationEmail.setFrom("spring.email.medical@gmail.com");
            emailService.sendEmail(registrationEmail);

            modelAndView.addObject("confirmationMessage", "A confirmation e-mail has been sent to " + user.getEmail());
            modelAndView.setViewName("register");
        }
        return modelAndView;
    }

    @GetMapping(value = "/confirm")
    public ModelAndView confirmRegistration(ModelAndView modelAndView,
                                            @RequestParam("token") String token) {

        User user = userService.findByConfirmationToken(token);
        if (user == null)
            modelAndView.addObject("invalidToken", "Oops!  This is an invalid confirmation link.");
        else
            modelAndView.addObject("confirmationToken", user.getConfirmationToken());

        modelAndView.setViewName("confirm");
        return modelAndView;
    }

    @PostMapping(value = "/confirm")
    public ModelAndView confirmRegistration(ModelAndView modelAndView,
                                            BindingResult bindingResult,
                                            @RequestParam Map<String, String> requestParams,
                                            RedirectAttributes redirect) {

        modelAndView.setViewName("confirm");

        if(!requestParams.get("password")
                .equals(requestParams.get("ConfirmPassword"))){
            redirect.addFlashAttribute("errorMessage", "Confirm your password correctly!");
            modelAndView.setViewName("redirect:confirm?token=" + requestParams.get("token"));
            return modelAndView;
        }

        Zxcvbn passwordCheck = new Zxcvbn();
        Strength strength = passwordCheck.measure(requestParams.get("password"));
        if (strength.getScore() < 3) {
            bindingResult.reject("password");
            redirect.addFlashAttribute("errorMessage", "Your password is too weak.  Choose a stronger one.");
            modelAndView.setViewName("redirect:confirm?token=" + requestParams.get("token"));
            return modelAndView;
        }

        User user = userService.findByConfirmationToken(requestParams.get("token"));
        user.setPassword(requestParams.get("password"));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(requestParams.get("password")));

        user.setEnabled(true);
        userService.save(user);

        modelAndView.addObject("successMessage", "Your password has been set!");
        modelAndView.setViewName("redirect:/loginPage");
        return modelAndView;
    }

}
