package ru.practicum.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.model.Category;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "annotation")
    String annotation;
    @ManyToOne
    Category category;
    @Column(name = "confirmed_requests")
    Integer confirmedRequests;
    @Column(name = "created_on")
    LocalDateTime createdOn;
    @Column(name = "description")
    String description;
    @Column(name = "event_date")
    LocalDateTime eventDate;
    @ManyToOne
    User initiator;
    @Embedded
    Location location;
    @Column(name = "paid")
    Boolean paid;
    @Column(name = "participant_limit")
    Integer participantLimit;
    @Column(name = "published_on")
    LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    Boolean requestModeration;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    EventState state;
    @Column(name = "title")
    String title;
    @Column(name = "views")
    Integer views;
}

