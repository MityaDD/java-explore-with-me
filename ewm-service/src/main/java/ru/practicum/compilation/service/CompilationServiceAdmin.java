package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;

public interface CompilationServiceAdmin {
    CompilationDto createCompilation(NewCompilationDto complicationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, NewCompilationDto complicationDto);
}
