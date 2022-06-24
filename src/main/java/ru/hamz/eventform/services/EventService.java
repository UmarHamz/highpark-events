package ru.hamz.eventform.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hamz.eventform.models.EventForm;
import ru.hamz.eventform.repos.EventsRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EventService {

    private final EventsRepository repository;

    @Autowired
    public EventService(EventsRepository repository) {
        this.repository = repository;
    }

    public boolean saveEvent(EventForm event) {
        if (event == null) {
            return false;
        }
        event.setDate_and_time(event.getDate_and_time().replace('T', ' '));
        repository.save(event);
        return true;
    }

    public Iterable<EventForm> findAllByStatus(String status) {
        return repository.findAllByStatus(status);
    }

    public EventForm findById(Long id) {
        Optional<EventForm> event = repository.findById(id);
        return event.orElseThrow(() -> new NoSuchElementException("No element of event with id: " + id));
    }

    public boolean deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("No such event element with id: " + id);
        }
        repository.deleteById(id);
        return true;
    }

    public EventForm changeEventStatus(Long eventId, String status) {
        EventForm event = findById(eventId);
        event.setStatus(status);
        repository.save(event);
        return event;
    }
}
