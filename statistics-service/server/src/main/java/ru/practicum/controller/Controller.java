package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.HitDTO;
import ru.practicum.StatDTO;
import ru.practicum.service.StatService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class Controller {
    private final StatService service;
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public StatDTO createStat(@RequestBody @Valid StatDTO statDTO) {
        log.info("Запрошено сохранение информации о : {}", statDTO);
        return service.addStat(statDTO);
    }

    @GetMapping("/stats")
    public List<HitDTO> getHit(@RequestParam(name = "start") @DateTimeFormat(pattern = PATTERN) LocalDateTime start,
                               @RequestParam(name = "end") @DateTimeFormat(pattern = PATTERN) LocalDateTime end,
                               @RequestParam(name = "uris", required = false, defaultValue = "") List<String> uris,
                               @RequestParam(name = "unique", required = false, defaultValue = "false") Boolean unique) {
        log.info("Запрошена статистика по посещениям с {} по {}", start, end);
        return service.getStats(start, end, uris, unique);
    }

}
