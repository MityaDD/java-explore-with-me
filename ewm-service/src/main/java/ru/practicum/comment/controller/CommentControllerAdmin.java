package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.model.CommentStatus;
import ru.practicum.comment.service.CommentServiceImpl;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("admin/comments")
public class CommentControllerAdmin {
    private final CommentServiceImpl commentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> searchComments(@RequestParam(required = false) List<Long> users,
                                           @RequestParam(required = false) List<CommentStatus> status,
                                           @RequestParam(required = false) List<Long> events,
                                           @RequestParam(required = false) String rangeStart,
                                           @RequestParam(required = false) String rangeEnd,
                                           @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = "10") @Positive Integer size) {
        return commentService.searchComments(users, status, events, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@PathVariable Long commentId, @RequestParam CommentStatus status) {
        return commentService.updateCommentByAdmin(commentId, status);
    }
}
