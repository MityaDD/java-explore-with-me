package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.StatDTO;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class StatsClient extends BaseClient {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl,
                       RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }


    public ResponseEntity<Object> save(HttpServletRequest httpServletRequest) {
        StatDTO endpointStatDTO = new StatDTO();
        endpointStatDTO.setIp(httpServletRequest.getRemoteAddr());
        endpointStatDTO.setUri(httpServletRequest.getRequestURI());
        endpointStatDTO.setApp(httpServletRequest.getServerName());
        endpointStatDTO.setTimestamp(LocalDateTime.now());
        return create(endpointStatDTO);
    }

    public ResponseEntity<Object> create(StatDTO endpointStatDTO) {
        return post("/hit", endpointStatDTO);
    }

    public ResponseEntity<Object> find(LocalDateTime start,
                                       LocalDateTime end,
                                       List<String> uris,
                                       boolean unique) {

        String urisString = String.join(", ", uris);

        Map<String, Object> parameters = Map.of(
                "start", start.format(FORMATTER),
                "end", end.format(FORMATTER),
                "uris", urisString,
                "unique", unique);
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}
