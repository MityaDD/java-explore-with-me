package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.HitDTO;
import ru.practicum.StatDTO;
import ru.practicum.exception.NotValidException;
import ru.practicum.mapper.Mapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.storage.StatRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class StatServiceImpl implements StatService {
    private final StatRepository repository;

    @Override
    @Transactional
    public StatDTO addStat(StatDTO statDTO) {
        EndpointHit endpointHit = Mapper.fromDTO(statDTO);
        log.info("Создана точка в статистике {}", statDTO);
        return Mapper.toDto(repository.save(endpointHit));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HitDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        checkDate(start, end);
        List<ViewStats> result;
        if (uris.isEmpty()) {
            if (!unique) {
                result = repository.getAllStats(start, end);
            } else {
                result = repository.getAllStatsForUniqueIp(start, end);
            }
        } else {
            if (!unique) {
                result = repository.getStatByUris(start, end, uris);
            } else {
                result = repository.getStatForUniqueIp(start, end, uris);
            }
        }
        return result.stream()
                .map(Mapper::hitToDTO)
                .collect(Collectors.toList());
    }

    private void checkDate(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            log.info("Время окончания не может быть раньше времени начала {} {}", start, end);
            throw new NotValidException("Время окончания не может быть раньше времени начала");
        }
    }

}
