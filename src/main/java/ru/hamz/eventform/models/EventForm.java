package ru.hamz.eventform.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventForm {

    @PrePersist
    private void init() {
        date_time_of_creation = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).toString().replace('T', ' ');

        status = "new";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "user_name")
    private String user_name;
    @Column(name = "user_surname")
    private String user_surname;
    @Column(name = "user_mail")
    private String user_mail;
    @Column(name = "user_number")
    private String user_number;
    @Column(name = "user_post")
    private String user_post;
    @Column(name = "event_name", columnDefinition = "TEXT")
    private String event_name;
    @Column(name = "event_description", columnDefinition = "TEXT")
    private String event_description;
    @Column(name = "date_and_time")
    private String date_and_time;
    @Column(name = "kvazar")
    private Boolean kvazar;
    @Column(name = "narnia")
    private Boolean narnia;
    @Column(name = "gargantua")
    private Boolean gargantua;
    @Column(name = "pulsar")
    private Boolean pulsar;
    @Column(name = "hovran")
    private Boolean hovran;
    @Column(name = "rck")
    private Boolean rck;
    @Column(name = "number_of_participants")
    private String number_of_participants;
    @Column(name = "projector")
    private Boolean projector;
    @Column(name = "laptop")
    private Boolean laptop;
    @Column(name = "clicker")
    private Boolean clicker;
    @Column(name = "interactive_whiteboard")
    private Boolean interactive_whiteboard;
    @Column(name = "printer")
    private Boolean printer;
    @Column(name = "microphone")
    private Boolean microphone;
    @Column(name = "photo_camera")
    private Boolean photo_camera;
    @Column(name = "nothing")
    private Boolean nothing;
    @Column(name = "status")
    private String status;
    @Column(name = "date_time_of_creation")
    private String date_time_of_creation;


    @Override
    public String toString() {
        return "Ваша заявка: " + '\n' +
                "Имя - " + user_name + '\n' +
                "Фамилия - " + user_surname + '\n' +
                "Почта - " + user_mail + '\n' +
                "Номер телефона - " + user_number + '\n' +
                "Должность - " + user_post + '\n' +
                "Название мероприятия - " + event_name + '\n' +
                "Описание мероприятия - " + event_description + '\n' +
                "Дата и время проведения - " + date_and_time + '\n' +
                "Залы: " + '\n' +
                (kvazar ? "  - Квазар \n" : "") +
                (narnia ? "  - Нарния \n" : "") +
                (gargantua ? "  - Гаргантюа \n" : "") +
                (pulsar ? "  - Пульсар \n" : "") +
                (hovran ? "  - Ховран \n" : "") +
                (rck ? "  - РЦК \n" : "")  +
                "Предположительное колличество участников - " + number_of_participants + '\n' +
                "Необходимое оборудование: " + '\n' +
                (projector ? "  - Проектор\n" : "") +
                (laptop ? "  - Ноутбук\n" : "") +
                (clicker ? "  - Кликер \n" : "") +
                (interactive_whiteboard ? "  - Интерактивная доска\n" : "") +
                (printer ? "  - Принтер\n" : "") +
                (microphone ? "  - Микрофон\n" : "") +
                (photo_camera ? "  - Фотоаппарат\n" : "") +
                (nothing ? "  - Ничего из перечисленного\n" : "");
    }

    public String toStringForTutorAndAdmin() {
        return "Заявка: " + '\n' +
                "Имя - " + user_name + '\n' +
                "Фамилия - " + user_surname + '\n' +
                "Почта - " + user_mail + '\n' +
                "Номер телефона - " + user_number + '\n' +
                "Должность - " + user_post + '\n' +
                "Название мероприятия - " + event_name + '\n' +
                "Описание мероприятия - " + event_description + '\n' +
                "Дата и время проведения - " + date_and_time + '\n' +
                "Залы: " + '\n' +
                (kvazar ? "  - Квазар \n" : "") +
                (narnia ? "  - Нарния \n" : "") +
                (gargantua ? "  - Гаргантюа \n" : "") +
                (pulsar ? "  - Пульсар \n" : "") +
                (hovran ? "  - Ховран \n" : "") +
                (rck ? "  - РЦК \n" : "")  +
                "Предположительное колличество участников - " + number_of_participants + '\n' +
                "Необходимое оборудование: " + '\n' +
                (projector ? "  - Проектор\n" : "") +
                (laptop ? "  - Ноутбук\n" : "") +
                (clicker ? "  - Кликер \n" : "") +
                (interactive_whiteboard ? "  - Интерактивная доска\n" : "") +
                (printer ? "  - Принтер\n" : "") +
                (microphone ? "  - Микрофон\n" : "") +
                (photo_camera ? "  - Фотоаппарат\n" : "") +
                (nothing ? "  - Ничего из перечисленного\n" : "");
    }
}
