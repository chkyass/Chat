package chkyass.repository;

import chkyass.entity.UserRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {
    @Modifying
    @Query("update UserRoom ur set ur.roomId = ?2 where ur.userId = ?1")
    void updateRoomIdByUsername(String username, long roomId);
    Optional<List<UserRoom>> findByUserId(String userId);
    Optional<List<UserRoom>> findByRoomId(long roomId);
}
