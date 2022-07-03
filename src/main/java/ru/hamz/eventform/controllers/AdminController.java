package ru.hamz.eventform.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.hamz.eventform.models.EventForm;
import ru.hamz.eventform.models.User;
import ru.hamz.eventform.services.EventService;
import ru.hamz.eventform.services.MailSender;
import ru.hamz.eventform.services.UserDetailsServiceImpl;

import java.security.Principal;
import java.util.Properties;


@Controller
@RequestMapping("/admin")
public class AdminController {

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
    public AdminController(EventService eventService, UserDetailsServiceImpl userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @GetMapping("/events")
    public String getEventsByStatusDefault() {
        return "redirect:/admin/events/to-work";
    }

    @GetMapping("/events/to-work")
    public String getEventsByStatusToWork(Model model) {
        model.addAttribute("allEvents", eventService.findAllByStatus("to-work"));
        return "admin/index_to-work";
    }

    @GetMapping("/events/new")
    public String getEventsByStatusNew(Model model) {
        model.addAttribute("allEvents", eventService.findAllByStatus("new"));
        return "admin/index_new";
    }

    @GetMapping("/events/confirmed")
    public String getEventsByStatusConfirmed(Model model) {
        model.addAttribute("allEvents", eventService.findAllByStatus("confirmed"));
        return "admin/index_confirmed";
    }

    @GetMapping("/events/:{id}")
    public String adminFormEditPage(@PathVariable("id") long id, Model model) {
        model.addAttribute("event", eventService.findById(id));
        return "admin/show_event";
    }

    @PatchMapping("events/confirm/:{id}")
    public String confirmEventForm(@PathVariable("id") long id) {
        new Thread(() -> {
            User main = userService.findByEmail(mailAddress);
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
            mailSender.send(main.getEmail(), event.getUser_mail(), "Ваша заявка согласована", event.toString());
        }).start();

        eventService.changeEventStatus(id, "confirmed");
        return "redirect:/admin/events";
    }

    @PostMapping("/to_change_message")
    public String sendReasonsMessage(@RequestParam String reasons_message, @RequestParam Long id, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        EventForm event = eventService.findById(id);

        new Thread(() -> {
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
        return "redirect:/admin/events";
    }
}
