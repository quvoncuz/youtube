package dasturlash.uz.repository.video;

import dasturlash.uz.dto.video.VideoTagMapper;
import dasturlash.uz.entity.video.VideoTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoTagRepository extends JpaRepository<VideoTagEntity, String> {
    Optional<VideoTagEntity> findVideoTagEntityByVideoIdAndTagId(String videoId, Integer tagId);

    void deleteVideoTagEntityByVideoIdAndTagId(String videoId, Integer tagId);

    @Query("""
            select vt.id as id,
                   vt.videoId as videoId,
                   vt.tagId as tagId,
                   vt.tag.name as tagName,
                   vt.createdDate as createdDate
            from VideoTagEntity as vt
            where vt.videoId = ?1
    """)
    List<VideoTagMapper> findAllByVideoId(String videoId);
}
