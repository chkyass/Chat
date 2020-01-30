package chkyass.repository;

import chkyass.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Modifying
    @Query("delete from Message m where m.user =:user")
    void deleteUserMessages(@Param("user") String user);

    Optional<List<Message>> findFirst20ByOrderByTimestampDesc();
}
