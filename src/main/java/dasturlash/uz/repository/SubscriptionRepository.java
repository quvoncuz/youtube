package dasturlash.uz.repository;

import dasturlash.uz.dto.SubscriptionInfo;
import dasturlash.uz.entity.SubscriptionEntity;
import dasturlash.uz.enums.NotificationType;
import dasturlash.uz.enums.Status;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, String> {
    Optional<SubscriptionEntity> findSubscriptionEntityByChannelIdAndProfileId(String channelId, String profileId);

    @Modifying
    @Transactional
    @Query("update SubscriptionEntity set status = ?3 where profileId = ?1 and channelId = ?2")
    int updateStatus(String profileId, String channelId, Status status);

    @Modifying
    @Transactional
    @Query("update SubscriptionEntity set notificationType = ?3 where profileId = ?1 and channelId = ?2")
    int updateNotification(String profileId, String channelId, NotificationType type);

    @Query("""
            select s.id as id,
                   s.channelId as channelId,
                   s.channel.name as channelName,
                   s.channel.photo.id as channelPhotoId,
                   s.channel.photo.path as channelPhotoUrl,
                   s.notificationType as notificationType
            from SubscriptionEntity as s
            where s.profileId = ?1 and s.status = 'ACTIVE'
            """)
    List<SubscriptionInfo> findAllByProfileIdAndStatusIs(String profileId);
}
