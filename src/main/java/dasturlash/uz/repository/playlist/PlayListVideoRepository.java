package dasturlash.uz.repository.playlist;

import dasturlash.uz.dto.playlist.PlayListVideoInfo;
import dasturlash.uz.entity.playlist.PlayListVideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayListVideoRepository extends JpaRepository<PlayListVideoEntity, String> {
    Optional<PlayListVideoEntity> findPlayListVideoEntityByPlayListIdAndVideoId(String playListId, String videoId);

    Optional<PlayListVideoEntity> findPlayListVideoEntityByVideoId(String videoId);

    void deletePlayListVideoEntityByPlayListIdAndVideoId(String playListId, String videoId);

    @Query("""
    select plv.playListId as playlistId,
           plv.videoId as videoId,
           plv.video.previewAttachId as videoPreviewAttachId,
           plv.video.previewAttach.path as videoPreviewAttachUrl,
           plv.video.title as title,
           plv.video.attach.duration as duration,
           plv.video.channelId as channelId,
           plv.video.channel.name as channelName,
           plv.video.publishedDate as createdDate,
           plv.orderNumber as orderNum
        from PlayListVideoEntity as plv
    where plv.playListId = ?1
    """)
    List<PlayListVideoInfo> findAllByPlayListId(String playListId);
}
