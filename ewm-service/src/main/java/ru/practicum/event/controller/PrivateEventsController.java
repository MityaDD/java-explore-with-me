package ru.practicum.event.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventServiceImpl;
import ru.practicum.request.dto.UpdateEventUserRequest;
import ru.practicum.event.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrivateEventsController {
    final EventServiceImpl eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                      @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                      @RequestParam(defaultValue = "10") @Positive Integer size) {
        return eventService.getUserEvents(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    EventFullDto addNewEvent(@PathVariable Long userId, @RequestBody @Valid NewEventDto eventDto) {
        return eventService.addNewEvent(userId, eventDto);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    EventFullDto getEventInformation(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getEventInformation(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    EventFullDto updateEventByUser(@PathVariable Long userId, @PathVariable Long eventId,
                                   @RequestBody @Valid UpdateEventUserRequest eventDto) {
        return eventService.updateEventByUser(userId, eventId, eventDto);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    List<ParticipationRequestDto> getUserEventRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getUserEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    EventRequestStatusUpdateResult changeRequestsStatus(@PathVariable Long userId, @PathVariable Long eventId,
                                                        @RequestBody EventRequestStatusUpdateRequest requestDto) {
        return eventService.changeRequestsStatus(userId, eventId, requestDto);
    }
}
