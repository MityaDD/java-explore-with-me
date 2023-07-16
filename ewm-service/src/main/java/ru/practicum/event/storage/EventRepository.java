package ru.practicum.event.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("select e from Event as e " +
            "where (:users is null or e.initiator.id in :users or e.initiator.id is null) " +
            "and (:categories is null or e.category.id in :categories or e.category.id is null) " +
            "and (:states is null or e.state in :states or e.state is null) " +
            "and (coalesce(:start, e.eventDate) <= e.eventDate and coalesce(:end, e.eventDate) >= e.eventDate)")
    List<Event> findByAdmin(@Param("users") List<Long> users, @Param("categories") List<Long> categories,
                            @Param("states") List<EventState> states, @Param("start") LocalDateTime rangeStart,
                            @Param("end") LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event as e " +
            "where e.state = ru.practicum.event.model.EventState.PUBLISHED " +
            "and (:categories is null or e.category.id in :categories or e.category.id is null)" +
            "and (:paid is null or e.paid = :paid or e.paid is null) " +
            "and (coalesce(:start, e.eventDate) <= e.eventDate and coalesce(:end, e.eventDate) >= e.eventDate)" +
            "and (:onlyAvailable is null or ((:onlyAvailable = true and e.confirmedRequests < e.participantLimit) " +
            "or (:onlyAvailable = false and 1 = 1))) " +
            "and ((:text is null) " +
            "or lower(e.description) like lower(concat('%',:text,'%')) " +
            "or lower(e.annotation) like lower(concat('%',:text,'%')))")
    List<Event> findByPublic(@Param("categories") List<Long> categories, @Param("paid") Boolean paid,
                             @Param("start") LocalDateTime rangeStart, @Param("end") LocalDateTime rangeEnd,
                             @Param("onlyAvailable") Boolean onlyAvailable, @Param("text") String text,
                             Pageable pageable);

    List<Event> findAllByInitiatorId(Long id, Pageable pageable);
}
