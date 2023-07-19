package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.storage.CategoryRepository;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.StateAdminRequest;
import ru.practicum.event.model.StateUserRequest;
import ru.practicum.event.model.Event;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.InvalidRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.dto.UpdateEventAdminRequest;
import ru.practicum.request.dto.UpdateEventUserRequest;
import ru.practicum.event.dto.ParticipationRequestDto;
import ru.practicum.request.dto.RequestMapper;
import ru.practicum.request.storage.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements AdminEventService, PrivateEventService, PublicEventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> searchEvents(List<Long> users, List<EventState> states,
                                           List<Long> categories, String rangeStart,
                                           String rangeEnd, Integer from, Integer size) {
        LocalDateTime start = null;
        LocalDateTime end = null;

        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        List<Event> events = eventRepository.findByAdmin(
                users, categories, states, start, end,
                PageRequest.of(from / size, size)
        );
        log.info("Поиск эвентов с параметрами: {}, {}, {}, {}, {}, {}, {} ",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest eventDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Эвент с id=" + eventId + " не найден"));

        if (eventDto.getEventDate() != null) {
            if (eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new InvalidRequestException("Дата начала эвента должна быть минимум +1 час от публикации");
            }
            event.setEventDate(eventDto.getEventDate());
        }

        if (eventDto.getStateAction() != null) {
            if (event.getState() != EventState.PENDING
                    && eventDto.getStateAction() == StateAdminRequest.PUBLISH_EVENT) {
                throw new ConflictException("Эвент можно публиковать только если оно в состоянии WAITING");
            }
            if (eventDto.getStateAction() == StateAdminRequest.REJECT_EVENT) {
                if (event.getState() == EventState.PUBLISHED) {
                    throw new ConflictException("Эвент можно отклонить, только если он не опубликован");
                } else {
                    event.setState(EventState.CANCELED);
                }
            } else if (eventDto.getStateAction() == StateAdminRequest.PUBLISH_EVENT) {
                event.setState(EventState.PUBLISHED);
            }
        }

        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }

        if (eventDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(() ->
                    new NotFoundException("Категория с id=" + eventId + " не найдена"));
            event.setCategory(category);
        }

        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }

        if (eventDto.getLocation() != null) {
            event.setLocation(eventDto.getLocation());
        }

        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }

        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }

        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }

        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
        log.info("Обновление эвента с id={} администратором", eventId);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id=" + userId + " не найден"));
        List<Event> events = eventRepository.findAllByInitiatorId(user.getId(),
                PageRequest.of(from / size, size));
        log.info("Запрос списка эвентов пользователя с id={}", userId);
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto addNewEvent(Long userId, NewEventDto eventDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id=" + userId + " не найден"));
        Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(() ->
                new NotFoundException("Категория с id=" + eventDto.getCategory() + " не найдена"));
        Event event = EventMapper.toEvent(eventDto, user, category);

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new InvalidRequestException("Эвент не может начаться раньше чем через 2 часа от текущего момента");
        }
        log.info("Создание нового эвента пользователем с id={}", userId);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getEventInformation(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id=" + userId + " не найден"));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Эвент с id=" + eventId + " не найден"));
        log.info("Запрос информации о эвенте с id={} пользователем с id={}", eventId, userId);
        return EventMapper.toEventFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest eventDto) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id=" + userId + " не найден"));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Эвент с id=" + eventId + " не найден"));

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Нельзя внести изменения в опубликованный эвент");
        }
        if (eventDto.getEventDate() != null) {
            if (eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new InvalidRequestException("Нельзя внести изменения в эвент до 2 часов");
            } else {
                event.setEventDate(eventDto.getEventDate());
            }
        }

        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(() ->
                    new NotFoundException("Категория c id=" + eventDto.getCategory() + " не найдена"));
            event.setCategory(category);
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }

        if (eventDto.getLocation() != null) {
            event.setLocation(eventDto.getLocation());
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
        if (eventDto.getStateAction() != null) {
            if (eventDto.getStateAction() == StateUserRequest.CANCEL_REVIEW) {
                event.setState(EventState.CANCELED);
            }

            if (eventDto.getStateAction() == StateUserRequest.SEND_TO_REVIEW) {
                event.setState(EventState.PENDING);
            }
        }

        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
        log.info("Обновление эвента c id={} пользователем с id={}", eventId, userId);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> getUserEventRequests(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь c id=" + userId + " не найден"));
        eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("эвент c id=" + eventId + " не найден"));

        List<Request> events = requestRepository.findAllByEventId(eventId);
        log.info("Запрос списков заявок на участие в эвенте c id={} пользователем с id={}", eventId, userId);
        return events.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult changeRequestsStatus(Long userId, Long eventId,
                                                               EventRequestStatusUpdateRequest requestDto) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь c id=" + userId + " не найден"));

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Эвент c id=" + eventId + " не найден"));

        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("Достигнут лимит заявок на участие в событии");
        }

        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();

        List<Request> requests = requestRepository.findAllById(requestDto.getRequestIds());
        for (Request r : requests) {
            if (r.getStatus() == RequestStatus.PENDING) {
                if (event.getParticipantLimit() == 0) {
                    r.setStatus(RequestStatus.CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                } else if (event.getParticipantLimit() > event.getConfirmedRequests()) {
                    if (!event.getRequestModeration()) {
                        r.setStatus(RequestStatus.CONFIRMED);
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    } else {
                        if (requestDto.getStatus() == RequestStatus.CONFIRMED) {
                            r.setStatus(RequestStatus.CONFIRMED);
                            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                        } else {
                            r.setStatus(RequestStatus.REJECTED);
                        }
                    }
                } else {
                    r.setStatus(RequestStatus.REJECTED);
                }
            } else {
                throw new ConflictException("Статус можно изменить только у заявок в статусе WAITING");
            }

            if (r.getStatus().equals((RequestStatus.CONFIRMED))) {
                confirmed.add(r);
            } else {
                rejected.add(r);
            }
        }
        eventRepository.save(event);
        log.info("Изменение статуса эвента с id={} пользователем с id={}", eventId, userId);

        return new EventRequestStatusUpdateResult(confirmed.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList()),
                rejected.stream()
                        .map(RequestMapper::toRequestDto)
                        .collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                         String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                         String sort, Integer from, Integer size) {
        LocalDateTime start = null;
        LocalDateTime end = null;

        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        if (start != null && end != null && start.isAfter(end)) {
            throw new InvalidRequestException("Дата и время эвента некорректые");
        }

        PageRequest pageRequest = null;

        if (sort != null) {
            if (sort.equals("EVENT_DATE")) {
                pageRequest = PageRequest.of(from / size, size, Sort.by("eventDate").descending());
            } else if (sort.equals("VIEWS")) {
                pageRequest = PageRequest.of(from / size, size, Sort.by("views").descending());
            }
        } else {
            pageRequest = PageRequest.of(from / size, size, Sort.by("id").descending());
        }

        List<Event> events = eventRepository.findByPublic(categories, paid, start, end, onlyAvailable, text, pageRequest);
        log.info("Запрос списка эвентов с параметрами: {}, {}, {}, {}, {}, {}, {}, {} "
                + text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Эвент с id=" + eventId + " не найден"));

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Эвент с id=" + eventId + " еще не опубликован");
        }

        event.setViews(event.getViews() + 1);
        eventRepository.save(event);
        log.info("Запрос эвента с id={}", eventId);
        return EventMapper.toEventFullDto(event);
    }
}
