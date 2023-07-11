package ru.practicum.mapper;

import ru.practicum.HitDTO;
import ru.practicum.StatDTO;
import ru.practicum.model.ViewStats;
import ru.practicum.model.EndpointHit;

public class Mapper {

    public static EndpointHit fromDTO(StatDTO statDTO) {
        return EndpointHit.builder()
                .ip(statDTO.getIp())
                .uri(statDTO.getUri())
                .app(statDTO.getApp())
                .created(statDTO.getTimestamp())
                .build();
    }

    public static StatDTO toDto(EndpointHit endpointHit) {
        return StatDTO.builder()
                .ip(endpointHit.getIp())
                .uri(endpointHit.getUri())
                .app(endpointHit.getApp())
                .timestamp(endpointHit.getCreated())
                .build();
    }

    public static HitDTO hitToDTO(ViewStats viewStats) {
        HitDTO hitDTO = new HitDTO();
        hitDTO.setApp(viewStats.getApp());
        hitDTO.setUri(viewStats.getUri());
        hitDTO.setHits(viewStats.getHits());
        return hitDTO;
    }
}
