package dasturlash.uz.repository.video;

import dasturlash.uz.dto.video.VideoLikeInfo;
import dasturlash.uz.entity.video.VideoLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoLikeRepository extends JpaRepository<VideoLikeEntity, String> {

    Optional<VideoLikeEntity> findVideoLikeEntityByVideoIdAndProfileId(String videoId, String profileId);

    @Query("""
            select
                vle.id as id,
                vle.videoId as videoId,
                ve.title as videoName,
                ve.channelId as channelId,
                ve.channel.name as channelName,
                ve.attach.duration as videoDuration,
                ae.id as previewAttachId,
                ae.path as previewAttachUrl
            from VideoLikeEntity as vle
            left join VideoEntity as ve on ve.id = vle.videoId 
            left join AttachEntity as ae on ae.id = ve.previewAttachId
            where vle.profileId = ?1
            order by vle.createdDate desc
            """)
    List<VideoLikeInfo> getVideoLikedByProfileId(String profileId);

    @Query("""
            select
                vle.id as id,
                vle.videoId as videoId,
                ve.title as videoName,
                ve.channelId as channelId,
                ve.channel.name as channelName,
                ve.attach.duration as videoDuration,
                ae.id as previewAttachId,
                ae.path as previewAttachUrl
            from VideoLikeEntity as vle
            left join VideoEntity as ve on ve.id = vle.videoId
            left join AttachEntity as ae on ae.id = ve.previewAttachId
            where vle.profileId = ?1
                  and ve.channel.profileId = ?2
            order by vle.createdDate desc
            """)
    List<VideoLikeInfo> getVideoLikedByProfileId(String profileId, String adminId);
}
