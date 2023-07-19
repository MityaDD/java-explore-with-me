package ru.practicum.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.request.model.RequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PUBLIC)
public class ParticipationRequestDto {
    LocalDateTime created;
    Long event;
    Long id;
    Long requester;
    RequestStatus status;
}
