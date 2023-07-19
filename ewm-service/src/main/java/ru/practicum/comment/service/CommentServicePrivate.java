package ru.practicum.comment.service;


import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;

@Service
public interface CommentServicePrivate {
    CommentDto getCommentById(Long userId, Long commentId);

    CommentDto createComment(Long eventId, Long userId, NewCommentDto commentDto);

    CommentDto updateCommentByUser(Long commentId, Long userId, NewCommentDto commentDto);

    void deleteComment(Long commentId, Long userId);
}
