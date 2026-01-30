package dasturlash.uz.repository.video;

import dasturlash.uz.entity.video.VideoWatchedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoWatchedRepository extends JpaRepository<VideoWatchedEntity, String> {

    Optional<VideoWatchedEntity> findVideoWatchedEntityByVideoIdAndProfileId(String videoId, String profileId);

    Long countByVideoId(String videoId);
}
