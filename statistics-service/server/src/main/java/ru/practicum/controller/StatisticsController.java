package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDTO;
import ru.practicum.dto.StatDTO;
import ru.practicum.service.StatService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@Slf4j
public class StatisticsController {
    private final StatService service;
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public StatDTO createStat(@RequestBody @Valid StatDTO statDTO) {
        log.info("Запрошено сохранение информации о : {}", statDTO);
        return service.addStat(statDTO);
    }

    @GetMapping("/stats")
    public List<HitDTO> getHit(@RequestParam @DateTimeFormat(pattern = PATTERN) LocalDateTime start,
                               @RequestParam @DateTimeFormat(pattern = PATTERN) LocalDateTime end,
                               @RequestParam(required = false, defaultValue = "") List<String> uris,
                               @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("Запрошена статистика по посещениям с {} по {}", start, end);
        return service.getStats(start, end, uris, unique);
    }

}