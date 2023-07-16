package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.StatDTO;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatisticsController {

    private final StatsClient statsClient;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestBody StatDTO statDTO) {
        return statsClient.create(statDTO);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> find(@RequestParam LocalDateTime start,
                                       @RequestParam LocalDateTime end,
                                       @RequestParam(required = false) List<String> uris,
                                       @RequestParam(defaultValue = "false") boolean unique) {
        return statsClient.find(start, end, uris, unique);
    }
}