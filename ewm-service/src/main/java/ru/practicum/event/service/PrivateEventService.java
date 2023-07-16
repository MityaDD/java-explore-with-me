package ru.practicum.event.service;

import ru.practicum.event.dto.*;
import ru.practicum.request.dto.UpdateEventUserRequest;
import ru.practicum.event.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateEventService {
    List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size);

    EventFullDto addNewEvent(Long userId, NewEventDto eventDto);

    EventFullDto getEventInformation(Long userId, Long eventId);

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest eventDto);

    List<ParticipationRequestDto> getUserEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult changeRequestsStatus(Long userId, Long eventId,
                                                        EventRequestStatusUpdateRequest requestDto);
}
