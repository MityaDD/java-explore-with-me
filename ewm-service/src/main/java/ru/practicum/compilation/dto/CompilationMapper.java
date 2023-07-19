package ru.practicum.compilation.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventMapper;

import java.util.stream.Collectors;


@UtilityClass
public class CompilationMapper {

    public static CompilationDto toComplicationDto(Compilation complication) {
        return CompilationDto.builder()
                .id(complication.getId())
                .title(complication.getTitle())
                .pinned(complication.getPinned())
                .events(complication.getEvents().stream()
                        .map(EventMapper::toEventShortDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
