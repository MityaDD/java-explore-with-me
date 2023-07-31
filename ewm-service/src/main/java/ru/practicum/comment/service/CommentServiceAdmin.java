package ru.practicum.comment.service;

import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.model.CommentStatus;

import java.util.List;

@Service
public interface CommentServiceAdmin {
    List<CommentDto> searchComments(List<Long> users, List<CommentStatus> status, List<Long> events,
                                    String rangeStart, String rangeEnd, Integer from, Integer size);

    CommentDto updateCommentByAdmin(Long commentId, CommentStatus status);
}
