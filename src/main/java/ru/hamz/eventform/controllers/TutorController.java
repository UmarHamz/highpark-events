package ru.hamz.eventform.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import java.security.Principal;
import java.util.Properties;

@Controller
@RequestMapping("/tutor")
public class TutorController {

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
    public TutorController(EventService eventService, UserDetailsServiceImpl userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @GetMapping("/events")
    public String getDefaultEvents() {
        return "redirect:/tutor/events/new";
    }

    @GetMapping("/events/new")
    public String getEventsByStatusNew(Model model) {
        model.addAttribute("allEvents", eventService.findAllByStatus("new"));
        return "tutor/index_new";
    }

    @GetMapping("/events/to-work")
    public String getEventsByStatusToWork(Model model) {
        model.addAttribute("allEvents", eventService.findAllByStatus("to-work"));
        return "tutor/index_to-work";
    }

    @GetMapping("/events/confirmed")
    public String getEventsByStatusConfirmed(Model model) {
        model.addAttribute("allEvents", eventService.findAllByStatus("confirmed"));
        return "tutor/index_confirmed";
    }

    @GetMapping("/events/:{id}")
    public String showEvent(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.findById(id));
        return "tutor/show_event";
    }

    @PatchMapping("/to-work/:{id}")
    public String editEventForm(@PathVariable("id") long id) {
        new Thread(() -> {
            User main = userService.findByEmail(mailAddress);
            User admin = userService.findByRole(ERole.ROLE_ADMIN);
            EventForm event = eventService.findById(id);

            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setHost(host);
            javaMailSender.setPort(port);
            javaMailSender.setUsername(main.getEmail());
            javaMailSender.setPassword(main.getPasswordForExternalApplication());
            javaMailSender.setProtocol("smtps");

            Properties javaMailProperties = javaMailSender.getJavaMailProperties();
            javaMailProperties.setProperty("mail.smtps.auth", mailSmtpsAuth);

            MailSender mailSender = new MailSender(javaMailSender);
            mailSender.send(main.getEmail(), admin.getEmail(), "Поступила новая заявка", event.toStringForTutorAndAdmin());
        }).start();

        eventService.changeEventStatus(id, "to-work");
        return "redirect:/tutor/events/new";
    }

    @PostMapping("/to_change_message")
    public String sendReasonsMessage(@RequestParam String reasons_message, @RequestParam Long id, Principal principal) {
        new Thread(() -> {
            User user = userService.findByEmail(principal.getName());
            EventForm event = eventService.findById(id);

            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setHost(host);
            javaMailSender.setPort(port);
            javaMailSender.setUsername(user.getEmail());
            javaMailSender.setPassword(user.getPasswordForExternalApplication());
            javaMailSender.setProtocol("smtps");

            Properties javaMailProperties = javaMailSender.getJavaMailProperties();
            javaMailProperties.setProperty("mail.smtps.auth", mailSmtpsAuth);

            MailSender mailSender = new MailSender(javaMailSender);
            mailSender.send(user.getEmail(), event.getUser_mail(), reasons_message, event.toString());
        }).start();

        eventService.deleteById(id);
        return "redirect:/tutor/events";
    }
}
