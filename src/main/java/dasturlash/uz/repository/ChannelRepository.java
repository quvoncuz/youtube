package dasturlash.uz.repository;

import dasturlash.uz.entity.ChannelEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<ChannelEntity, String> {

    @Query("from ChannelEntity where username = ?1")
    Optional<ChannelEntity> findChannelEntityByUsername(String username);

    List<ChannelEntity> findChannelEntitiesByProfileId(String profileId);

    @Modifying
    @Transactional
    @Query("update ChannelEntity set status = ?1 where id =?2")
    int updateStatus(String status, String id);
}
