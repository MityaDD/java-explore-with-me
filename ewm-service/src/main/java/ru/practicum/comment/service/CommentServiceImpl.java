package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentMapper;
import ru.practicum.comment.model.CommentStatus;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.storage.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.InvalidRequestException;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentServiceAdmin, CommentServicePrivate, CommentServicePublic {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> searchComments(List<Long> users, List<CommentStatus> status, List<Long> events,
                                           String rangeStart, String rangeEnd,
                                           Integer from, Integer size) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        log.info("Запрошен поиск комментариев с параметрами: {}, {}, {}, {}, {}, {}, {} ",
                users, status, events, start, end, from, size);
        return commentRepository.findByAdmin(users, status, events, start,
                        end, PageRequest.of(from / size, size)).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDto updateCommentByAdmin(Long commentId, CommentStatus status) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Коментарий с id=" + commentId + " не найден"));
        if (status.equals(CommentStatus.PENDING)) {
            throw new InvalidRequestException("Админ должен опубликовать или отменить публикацию этого комментария");
        } else if (status.equals(CommentStatus.CANCELED)) {
            if (comment.getStatus().equals(CommentStatus.PUBLISHED)) {
                throw new InvalidRequestException("Запрещено удалять уже опубликованный комментарий");
            }
        }
        comment.setStatus(status);
        log.info("Комментарий с id={} обновлен админом", commentId);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    @Override
    public CommentDto getCommentById(Long userId, Long commentId) {
        log.info("Запрошен комментарий с id={}", commentId);
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id=" + userId + " не найден"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Комментарий с id=" + commentId + " не найден"));
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public CommentDto createComment(Long eventId, Long userId, NewCommentDto commentDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id=" + userId + " не найден"));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Эвент с id=" + eventId + " не найден"));
        Comment comment = new Comment(null, user, event, LocalDateTime.now(),
                CommentStatus.PENDING, commentDto.getText());

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Запрещено оставлять комментарии к неопубликованному эвенту");
        }
        log.info("Комментарий к эвенту с id={} создан пользователем с id={}", eventId, userId);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public CommentDto updateCommentByUser(Long commentId, Long userId, NewCommentDto commentDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id=" + userId + " не найден"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Комментарий с id=" + commentId + " не найден"));
        if (!user.getId().equals(comment.getAuthor().getId())) {
            throw new InvalidRequestException("Пользователи могут обновлять только свои комментарии");
        }
        if (comment.getStatus().equals(CommentStatus.PENDING)) {
            throw new InvalidRequestException("Данный омментарий еще не опубликован админом");
        }
        comment.setText(commentDto.getText());
        comment.setStatus(CommentStatus.PENDING);
        log.info("Комментарий с id={} обнавлен пользователем с id={}", commentId, userId);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id=" + userId + " не найден"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Комментарий с id=" + commentId + " не найден"));
        if (!user.getId().equals(comment.getAuthor().getId())) {
            throw new InvalidRequestException("Пользователи могут удалять только свои комментарии");
        }

        if (comment.getStatus().equals(CommentStatus.PENDING)) {
            throw new InvalidRequestException("Комментарий еще не опубликован админом");
        }
        if (comment.getStatus().equals(CommentStatus.CANCELED)) {
            throw new InvalidRequestException("Публикация комментария отменена админом");
        }
        log.info("Комментарий с id={} удален пользователем с id={}", commentId, userId);
        commentRepository.deleteById(comment.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> getAllComments(Integer from, Integer size) {
        log.info("Запрошены комментарии с параметрами from={}, size={} ", from, size);
        return commentRepository.findAllByStatus(CommentStatus.PUBLISHED,
                        PageRequest.of(from / size, size)).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> getEventComments(Long eventId, Integer from, Integer size) {
        log.info("Запрошены комментарии к эвенту с id={} с параметрами from={}, size={}", eventId, from, size);
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Эвент с id=" + eventId + " не найден"));
        return commentRepository.findCommentsByEventIdAndStatusIs(event.getId(), CommentStatus.PUBLISHED,
                        PageRequest.of(from / size, size)).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}