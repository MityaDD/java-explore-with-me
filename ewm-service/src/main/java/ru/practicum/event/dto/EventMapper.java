package ru.practicum.event.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.category.model.Category;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;
import ru.practicum.user.dto.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class EventMapper {
    public static Event toEvent(NewEventDto newEventDto, User user, Category category) {
        return Event.builder()
                .id(0L)
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .confirmedRequests(0)
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(EventMapper.toLocalDateTime(newEventDto.getEventDate()))
                .initiator(user)
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(LocalDateTime.now())
                .requestModeration(newEventDto.getRequestModeration())
                .state(EventState.PENDING)
                .title(newEventDto.getTitle())
                .views(0)
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static LocalDateTime toLocalDateTime(String string) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(string, formatter);
    }
}
