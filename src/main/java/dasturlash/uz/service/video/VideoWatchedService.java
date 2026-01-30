package dasturlash.uz.service.video;

import dasturlash.uz.entity.video.VideoWatchedEntity;
import dasturlash.uz.repository.video.VideoWatchedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VideoWatchedService {

    @Autowired
    private VideoWatchedRepository videoWatchedRepository;

    public void create(String profileId, String videoId){
        Optional<VideoWatchedEntity> videoWatchedEntityByVideoIdAndProfileId = videoWatchedRepository.findVideoWatchedEntityByVideoIdAndProfileId(videoId, profileId);
        if (videoWatchedEntityByVideoIdAndProfileId.isEmpty()){
            VideoWatchedEntity videoWatchedEntity = new VideoWatchedEntity();
            videoWatchedEntity.setProfileId(profileId);
            videoWatchedEntity.setVideoId(videoId);
            videoWatchedRepository.save(videoWatchedEntity);
        }
    }

    public Long findVideoCount(String videoId) {
        return videoWatchedRepository.countByVideoId(videoId);
    }
}
