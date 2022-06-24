package ru.hamz.eventform.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hamz.eventform.models.EventForm;

@Repository
public interface EventsRepository extends JpaRepository<EventForm, Long> {
      Iterable<EventForm> findAllByStatus(String status);
}
