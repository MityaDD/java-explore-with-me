package ru.practicum.comment.service;

import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.CommentDto;

import java.util.List;

@Service
public interface CommentServicePublic {

    List<CommentDto> getAllComments(Integer from, Integer size);

    List<CommentDto> getEventComments(Long eventId, Integer from, Integer size);
}