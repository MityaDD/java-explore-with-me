package ru.practicum.event.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.model.EventState;
import ru.practicum.event.service.EventServiceImpl;
import ru.practicum.request.dto.UpdateEventAdminRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminEventsController {

    final EventServiceImpl eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<EventFullDto> searchEvents(@RequestParam(required = false) List<Long> users,
                                    @RequestParam(required = false) List<EventState> states,
                                    @RequestParam(required = false) List<Long> categories,
                                    @RequestParam(required = false) String rangeStart,
                                    @RequestParam(required = false) String rangeEnd,
                                    @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                    @RequestParam(defaultValue = "10") @Positive Integer size) {
        return eventService.searchEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    EventFullDto updateEvent(@PathVariable Long eventId, @RequestBody @Valid UpdateEventAdminRequest eventDto) {
        return eventService.updateEvent(eventId, eventDto);
    }
}
