package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.model.ViewStats(h.app, h.uri, COUNT((h.ip))) " +
            "FROM EndpointHit AS h " +
            "WHERE h.created BETWEEN ?1 AND ?2 AND h.uri IN ?3 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStats> getStatByUris(LocalDateTime start, LocalDateTime end, Collection<String> uris);

    @Query("SELECT new ru.practicum.model.ViewStats(h.app, h.uri, COUNT((h.ip))) " +
            "FROM EndpointHit AS h " +
            "WHERE h.created BETWEEN ?1 AND ?2 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.id) DESC")
    List<ViewStats> getAllStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.model.ViewStats(h.app, h.uri, COUNT(DISTINCT(h.ip))) " +
            "FROM EndpointHit AS h " +
            "WHERE h.created BETWEEN ?1 AND ?2 AND h.uri IN ?3 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.id) DESC")
    List<ViewStats> getStatForUniqueIp(LocalDateTime start, LocalDateTime end, Collection<String> uris);

    @Query("SELECT new ru.practicum.model.ViewStats(h.app, h.uri, COUNT(DISTINCT(h.ip))) " +
            "FROM EndpointHit AS h " +
            "WHERE h.created BETWEEN ?1 AND ?2 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStats> getAllStatsForUniqueIp(LocalDateTime start, LocalDateTime end);
}
