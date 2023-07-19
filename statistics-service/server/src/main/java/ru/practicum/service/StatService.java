package ru.practicum.service;

import ru.practicum.dto.HitDTO;
import ru.practicum.dto.StatDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {

    StatDTO addStat(StatDTO statDTO);

    List<HitDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
