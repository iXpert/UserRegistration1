package com.ixpert.sb.userregistration1.controller;

import com.ixpert.sb.userregistration1.model.User;
import com.ixpert.sb.userregistration1.service.EmailService;
import com.ixpert.sb.userregistration1.service.UserService;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

@Controller
public class RegisterController {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserService userService;
    private EmailService emailService;

    @Autowired
    public RegisterController(BCryptPasswordEncoder bCryptPasswordEncoder1, UserService userService1, EmailService emailService1){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder1;
        this.userService = userService1;
        this.emailService = emailService1;
    }


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView showRegistrationPage(ModelAndView modelAndView, User user){
        modelAndView.addObject("user",user);
        modelAndView.setViewName("register");
        return modelAndView;
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView processRegistrationForm(ModelAndView modelAndView, @Valid User user, BindingResult bindingResult, HttpServletRequest httpServletRequest){

        User userExists = userService.findByEmail(user.getEmail());

        System.out.println(userExists);

        if (userExists != null){
            modelAndView.addObject("alreadyRegisteredMessage","Oops! There is already a user registered with the provided email!");
            modelAndView.setViewName("register");
            bindingResult.reject("email");
        }

        if (bindingResult.hasErrors()){
            modelAndView.setViewName("register");
        } else {
            // disable user until he/she clicks on the link in the email
            user.setEnabled(false);

            //generate a random 36 letters code for the confirmation email
            user.setConfirmationToken(UUID.randomUUID().toString());

            userService.saveUser(user);

            //String appUrl = httpServletRequest.getScheme()+"://"+httpServletRequest.getServerName();
            String appUrl = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() +":"+httpServletRequest.getServerPort();
            SimpleMailMessage registrationEmail = new SimpleMailMessage();
            registrationEmail.setTo(user.getEmail());
            registrationEmail.setSubject("Registration Confirmation");
            registrationEmail.setText("To confirm your email address, please click on the link below: \n"+appUrl+"/confirm?token="+user.getConfirmationToken());
            registrationEmail.setFrom("amr.elnagar@outlook.com");

            emailService.sendEmail(registrationEmail);

            modelAndView.addObject("confirmationMessage","A confirmation email has been sent to "+user.getEmail());
            modelAndView.setViewName("register");

        }

        return  modelAndView;
    }


    // Process confirmation link
    @RequestMapping(value="/confirm", method = RequestMethod.GET)
    public ModelAndView showConfirmationPage(ModelAndView modelAndView, @RequestParam("token") String token) {

        User user = userService.findByConfirmationToken(token);

        if (user == null) { // No token found in DB
            modelAndView.addObject("invalidToken", "Oops!  This is an invalid confirmation link.");
        } else { // Token found
            modelAndView.addObject("confirmationToken", user.getConfirmationToken());
        }

        modelAndView.setViewName("confirm");
        return modelAndView;
    }

    // Process confirmation link
    @RequestMapping(value="/confirm", method = RequestMethod.POST)
    public ModelAndView processConfirmationForm(ModelAndView modelAndView, BindingResult bindingResult, @RequestParam Map requestParams, RedirectAttributes redir) {

        modelAndView.setViewName("confirm");

        Zxcvbn passwordCheck = new Zxcvbn();

        String currentPassword = requestParams.get("password").toString();

        Strength strength = passwordCheck.measure(currentPassword);

        if (strength.getScore() < 3) {
            bindingResult.reject("password");

            redir.addFlashAttribute("errorMessage", "Your password is too weak.  Choose a stronger one.");

            modelAndView.setViewName("redirect:confirm?token=" + requestParams.get("token"));
            System.out.println(requestParams.get("token"));
            return modelAndView;
        }

        // Find the user associated with the reset token
        User user = userService.findByConfirmationToken(requestParams.get("token").toString());

        // Set new password
        user.setPassword(bCryptPasswordEncoder.encode(requestParams.get("password").toString()));

        // Set user to enabled
        user.setEnabled(true);

        // Save user
        userService.saveUser(user);

        modelAndView.addObject("successMessage", "Your password has been set!");
        return modelAndView;
    }



}
