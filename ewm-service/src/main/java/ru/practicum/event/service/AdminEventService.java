package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.model.EventState;
import ru.practicum.request.dto.UpdateEventAdminRequest;

import java.util.List;

public interface AdminEventService {
    List<EventFullDto> searchEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                    String rangeStart, String rangeEnd, Integer from, Integer size);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest eventDto);
}
