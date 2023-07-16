package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationMapper;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.storage.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.exception.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationServiceAdmin, CompilationServicePublic {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = new Compilation();
        List<Event> events;
        if (compilationDto.getEvents() == null) {
            events = Collections.emptyList();
        } else {
            events = eventRepository.findAllById(compilationDto.getEvents());
        }
        compilation.setEvents(events);
        compilation.setPinned(compilationDto.getPinned());
        compilation.setTitle(compilationDto.getTitle());
        log.info("Создана новая подборка евентов");
        return CompilationMapper.toComplicationDto(compilationRepository.save(compilation));
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(Long compId, NewCompilationDto compilationDto) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка эвентов с id=" + compId + " не найдена"));
        List<Event> events;
        if (compilationDto.getEvents() == null) {
            events = Collections.emptyList();
        } else {
            events = eventRepository.findAllById(compilationDto.getEvents());
        }
        compilation.setEvents(events);
        if (compilationDto.getPinned() != null) {
            compilation.setPinned(compilationDto.getPinned());
        }
        if (compilationDto.getTitle() != null) {
            compilation.setTitle(compilationDto.getTitle());
        }
        log.info("Обновлена подборка эвентов с id=" + compId);
        return CompilationMapper.toComplicationDto(compilationRepository.save(compilation));
    }

    @Transactional
    @Override
    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка эвентов с id=" + compId + " не найдена"));
        log.info("Удалена подборка эвентов с id=" + compId);
        compilationRepository.deleteById(compilation.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        log.info("Запрошен список подборок еветов с параметром pinned=" + pinned);
        return compilationRepository.findAllByPinned(pinned, PageRequest.of(from / size, size)).stream()
                .map(CompilationMapper::toComplicationDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка эвентов с id=" + compId + " не найдена"));
        log.info("Запрошена подборка евентов с айди " + compId);
        return CompilationMapper.toComplicationDto(compilation);
    }
}
