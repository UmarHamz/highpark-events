package ru.hamz.eventform.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.hamz.eventform.models.ERole;
import ru.hamz.eventform.models.EventForm;
import ru.hamz.eventform.models.User;
import ru.hamz.eventform.services.EventService;
import ru.hamz.eventform.services.MailSender;
import ru.hamz.eventform.services.UserDetailsServiceImpl;

import java.util.Properties;

@Controller
@RequestMapping("/user")
public class UserController implements ErrorController {

    private final EventService eventService;
    private final UserDetailsServiceImpl userService;

    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.protocol}")
    private String protocol;
    @Value("${mail.smtps.auth}")
    private String mailSmtpsAuth;
    @Value("${mail.address}")
    private String mailAddress;

    @Autowired
    public UserController(EventService eventService, UserDetailsServiceImpl userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @PostMapping("/event-form")
    public String formPosting(@ModelAttribute("eventForm") EventForm eventForm) {

        new Thread(() -> {
            User main = userService.findByEmail(mailAddress);

            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setHost(host);
            javaMailSender.setPort(port);
            javaMailSender.setUsername(main.getEmail());
            javaMailSender.setPassword(main.getPasswordForExternalApplication());
            javaMailSender.setProtocol("smtps");

            Properties javaMailProperties = javaMailSender.getJavaMailProperties();
            javaMailProperties.setProperty("mail.smtps.auth", mailSmtpsAuth);

            MailSender mailSender = new MailSender(javaMailSender);
            mailSender.send(main.getEmail(), eventForm.getUser_mail(), "Ваша заявка отправлена.", eventForm.toString());

            User user = userService.findByRole(ERole.ROLE_TUTOR);

            mailSender.send(main.getEmail(), user.getEmail(), "Поступила новая заявка.", eventForm.toStringForTutorAndAdmin());
        }).start();

        eventService.saveEvent(eventForm);
        return "redirect:/user/events";
    }

    @GetMapping("/events")
    public String index(Model model) {
        model.addAttribute("allEvents", eventService.findAllByStatus("confirmed"));
        model.addAttribute("eventForm", new EventForm());
        return "user/index";
    }

    @GetMapping("/events/:{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("event", eventService.findById(id));
        return "user/show_event";
    }
}
