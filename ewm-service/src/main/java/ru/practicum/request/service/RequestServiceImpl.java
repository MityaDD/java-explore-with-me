package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ConflictException;
import ru.practicum.event.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.dto.RequestMapper;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.storage.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements PrivateRequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id=" + userId + " не найден"));

        List<Request> requests = requestRepository.findAllByRequesterId(user.getId());
        log.info("Запрошен список запросов для пользователя с id=" + userId);
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id=" + userId + " не найден"));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Эвент с id=" + eventId + " не найден"));

        Request request = new Request(null, LocalDateTime.now(), event, user, RequestStatus.PENDING);

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Инициатор события не может добавить запрос на участие в нем");
        }

        if (!requestRepository.findByRequesterIdAndEventId(userId, eventId).isEmpty()) {
            throw new ConflictException("Нельзя добавить повторный запрос на участие в событии");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Нельзя добавить запрос на участие в неопубликованном событии");
        }

        if (event.getParticipantLimit() == 0) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
            request.setStatus(RequestStatus.CONFIRMED);
        } else if (event.getConfirmedRequests() < event.getParticipantLimit()) {
            if (!event.getRequestModeration()) {
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                eventRepository.save(event);
                request.setStatus(RequestStatus.CONFIRMED);
            }
        } else {
            throw new ConflictException("Достигнут лимит заявок на участие в событии");
        }
        log.info("Сохранен запрос на участие пользователем id=" + userId + " в событии с id=" + eventId);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с айди " + userId + " не найден"));
        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с айди " + requestId + " не найден"));

        request.setStatus(RequestStatus.CANCELED);
        log.info("Отмена запроса id=" + requestId + " пользователем с id=" + userId);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }
}
