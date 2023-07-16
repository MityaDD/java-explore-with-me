package ru.practicum.user.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.user.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User as u " +
            "where (:ids is null or u.id in :ids or u.id is null)")
    List<User> findAllByAdmin(@Param("ids") List<Long> ids, Pageable pageable);
}
