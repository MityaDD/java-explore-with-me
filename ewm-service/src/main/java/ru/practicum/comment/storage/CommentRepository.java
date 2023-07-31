package ru.practicum.comment.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment AS c " +
            "WHERE (:users IS NULL OR c.author.id IN :users OR c.author.id IS NULL) " +
            "AND (:status is null OR c.status IN :status OR c.status IS NULL) " +
            "AND (:events is null OR c.event.id IN :events OR c.event.id IS NULL) " +
            "AND (coalesce(:start, c.created) <= c.created AND coalesce(:end, c.created) >= c.created)")
    List<Comment> findByAdmin(@Param("users") List<Long> users, @Param("status") List<CommentStatus> status,
                              @Param("events") List<Long> events, @Param("start") LocalDateTime rangeStart,
                              @Param("end") LocalDateTime rangeEnd, Pageable pageable);

    List<Comment> findAllByStatus(CommentStatus status, Pageable pageable);

    List<Comment> findCommentsByEventIdAndStatusIs(Long eventId, CommentStatus commentStatus, Pageable pageable);
}