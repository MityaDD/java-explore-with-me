package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.service.CommentServiceImpl;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentControllerPublic {
    private final CommentServiceImpl commentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<CommentDto> getAllComments(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                    @RequestParam(defaultValue = "10") @Positive Integer size) {
        return commentService.getAllComments(from, size);
    }

    @GetMapping("/event/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    List<CommentDto> getEventComments(@PathVariable Long eventId,
                                      @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                      @RequestParam(defaultValue = "10") @Positive Integer size) {
        return commentService.getEventComments(eventId, from, size);
    }
}