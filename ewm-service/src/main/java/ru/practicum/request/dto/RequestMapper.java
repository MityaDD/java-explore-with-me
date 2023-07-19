package ru.practicum.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.request.model.Request;
import ru.practicum.event.dto.ParticipationRequestDto;

@UtilityClass
public class RequestMapper {
    public static ParticipationRequestDto toRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }
}
